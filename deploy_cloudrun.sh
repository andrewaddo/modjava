#!/bin/bash

# Non-interactive deployment script for Shopping Cart application to Cloud Run

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

# --- Main Script ---
echo_green "Starting Shopping Cart Application Deployment to Cloud Run..."

# Step 1: Build the Docker image
echo_green "\nBuilding the Docker image..."
docker build -t gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT /home/ducdo/workspace/modjava/mod.phase3.out/web-app

# Step 2: Push the Docker image to Google Container Registry
echo_green "\nPushing the Docker image to Google Container Registry..."
docker push gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT

# Step 3: Deploy to Cloud Run
echo_green "\nDeploying to Cloud Run..."
gcloud run deploy shopping-cart-web-app \
  --image gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT \
  --platform managed \
  --region ${REGION} \
  --port=8080 \
  --allow-unauthenticated \
  --add-cloudsql-instances=${CLOUD_SQL_CONNECTION_NAME} \
  --set-env-vars=SPRING_DATASOURCE_URL="jdbc:postgresql:///testdb?cloudSqlInstance=${CLOUD_SQL_CONNECTION_NAME}&socketFactory=com.google.cloud.sql.postgres.SocketFactory" \
  --set-env-vars=SPRING_DATASOURCE_USERNAME="testuser" \
  --set-env-vars=SPRING_DATASOURCE_PASSWORD="${DB_USER_PASSWORD}"

echo_green "\nCloud Run deployment complete!"

