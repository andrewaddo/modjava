#!/bin/bash

# Non-interactive deployment script for Shopping Cart application to GKE

# --- Helper Functions ---

echo_green() {
  echo -e "\033[0;32m$1\033[0m"
}

echo_red() {
  echo -e "\033[0;31m$1\033[0m"
}

# --- Usage ---
usage() {
  echo "Usage: $0 <PROJECT_ID> <REGION> <DB_USER_PASSWORD> <CLOUD_SQL_CONNECTION_NAME>"
  exit 1
}

# --- Check for arguments ---
if [ "$#" -ne 4 ]; then
  usage
fi

# --- Assign arguments to variables ---
PROJECT_ID=$1
REGION=$2
DB_USER_PASSWORD=$3
CLOUD_SQL_CONNECTION_NAME=$4
CLUSTER_NAME="shopping-cart-cluster"

# --- Main Script ---
echo_green "Starting Shopping Cart Application Deployment to GKE..."

# Step 1: Build the Docker image
echo_green "\nBuilding all modules with dependencies..."
mvn clean install -U -f /home/ducdo/workspace/modjava/mod.andrew.out/pom.xml
echo_green "\nBuilding the Docker image..."
docker build --no-cache -t gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT /home/ducdo/workspace/modjava/mod.phase3.out/web-app

# Step 2: Push the Docker image to Google Container Registry
echo_green "\nPushing the Docker image to Google Container Registry..."
docker push gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT

# Step 3: Check and create GKE cluster if it doesn't exist
echo_green "\nChecking for GKE cluster..."
if ! gcloud container clusters describe ${CLUSTER_NAME} --region ${REGION} --project ${PROJECT_ID} &> /dev/null; then
  echo_green "GKE cluster ${CLUSTER_NAME} not found. Creating..."
  gcloud container clusters create ${CLUSTER_NAME} --region ${REGION} --project ${PROJECT_ID} --num-nodes=1
else
  echo_green "GKE cluster ${CLUSTER_NAME} already exists."
fi

# Step 4: Get kubectl credentials
echo_green "\nGetting kubectl credentials..."
gcloud container clusters get-credentials ${CLUSTER_NAME} --region ${REGION} --project ${PROJECT_ID}

# Step 5: Deploy to GKE using Kubernetes manifests
echo_green "\nDeploying to GKE..."
# Create k8s directory if it doesn't exist
mkdir -p k8s

# Create Kubernetes Deployment manifest
cat <<EOF > k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: shopping-cart-web-app
  labels:
    app: shopping-cart-web-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: shopping-cart-web-app
  template:
    metadata:
      labels:
        app: shopping-cart-web-app
    spec:
      serviceAccountName: shopping-cart-ksa
      containers:
      - name: shopping-cart-web-app
        image: gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT
        ports:
        - containerPort: 8080
        env:
        - name: PORT
          value: "8080"
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:postgresql:///shop18cart?cloudSqlInstance=${CLOUD_SQL_CONNECTION_NAME}&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
        - name: SPRING_DATASOURCE_USERNAME
          value: "testuser"
        - name: SPRING_DATASOURCE_PASSWORD
          value: "${DB_USER_PASSWORD}"
      # Cloud SQL Proxy sidecar
      - name: cloudsql-proxy
        image: gcr.io/cloud-sql-connectors/cloud-sql-proxy:latest
        args: ["${CLOUD_SQL_CONNECTION_NAME}", "--auto-iam-authn"]
        securityContext:
          runAsNonRoot: true
        # Required for Cloud SQL Proxy to connect to the database
        # You might need to create a service account and bind it to the pod
        # For simplicity, this example assumes default service account has permissions
EOF

# Create Kubernetes Service manifest
cat <<EOF > k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: shopping-cart-web-app
  labels:
    app: shopping-cart-web-app
spec:
  selector:
    app: shopping-cart-web-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
EOF

kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

echo_green "\nGKE deployment complete!"
echo_green "It may take a few minutes for the LoadBalancer IP to be available."
echo_green "To get the external IP, run: kubectl get service shopping-cart-web-app"
