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
docker build -t gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT /home/ducdo/workspace/modjava/mod.andrew.out/web-app

# Step 2: Push the Docker image to Google Container Registry
echo_green "\nPushing the Docker image to Google Container Registry..."
docker push gcr.io/${PROJECT_ID}/web-app:0.0.1-SNAPSHOT

# Step 3: Deploy to Cloud Run
echo_green "\nDeploying to Cloud Run..."
# IAM policy configuration is handled manually for controlled environments like Argolis.
# gcloud iam service-accounts create "${SERVICE_ACCOUNT_NAME}" \
#   --display-name "Service Account for Cloud Run" \
#   --project="${PROJECT_ID}"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/cloudsql.client"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/logging.logWriter"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/monitoring.metricWriter"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/trace.agent"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/cloudtasks.enqueuer"

# gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
#   --member="serviceAccount:${SERVICE_ACCOUNT_NAME}@${PROJECT_ID}.iam.gserviceaccount.com" \
#   --role="roles/secretmanager.secretAccessor"


echo_green "\nCloud Run deployment complete!"
echo_green "You can find your service at: https://${REGION}-run.app/shopping-cart-web-app"
