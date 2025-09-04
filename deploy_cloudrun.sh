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
  echo "Usage: $0 <PROJECT_ID> <REGION>"
  exit 1
}

# --- Check for arguments ---
if [ "$#" -ne 2 ]; then
  usage
fi

# --- Assign arguments to variables ---
PROJECT_ID=$1
REGION=$2

# --- Main Script ---
echo_green "Starting Shopping Cart Application Deployment to Cloud Run..."

echo_green "\nBuilding the project with Maven..."
mvn clean install -f /home/ducdo/workspace/modjava/mod.phase3.out/pom.xml

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
  --set-env-vars=SPRING_PROFILES_ACTIVE=cloud \
  --set-secrets=GOOGLE_API_KEY=my-google-api-key:latest \
  --set-secrets=SPRING_DATASOURCE_PASSWORD=db-user-password:latest \
  --set-secrets=SPRING_MAIL_PASSWORD=smtp-app-password:latest

echo_green "\nCloud Run deployment complete!"