#!/bin/bash

# Prompt for user input
read -p "Enter your Google Cloud Project ID: " PROJECT_ID
read -p "Enter your desired Google Cloud Region (e.g., us-central1): " REGION
read -p "Enter a strong password for the Cloud SQL root user: " DB_ROOT_PASSWORD
read -p "Enter a strong password for the Cloud SQL application user: " DB_USER_PASSWORD

# Create Cloud SQL instance
echo "Creating Cloud SQL instance..."
gcloud sql instances create shopping-cart-db \
  --database-version=POSTGRES_13 \
  --region=$REGION \
  --root-password=$DB_ROOT_PASSWORD \
  --tier=db-f1-micro # Added tier for PostgreSQL compatibility

# Create database
echo "Creating database 'testdb'வைக்"
gcloud sql databases create testdb --instance=shopping-cart-db

# Create application user
echo "Creating application user 'testuser'வைக்"
gcloud sql users create testuser --instance=shopping-cart-db \
  --password=$DB_USER_PASSWORD

# Get Cloud SQL connection name
CLOUD_SQL_CONNECTION_NAME=$(gcloud sql instances describe shopping-cart-db --format="value(connectionName)")

echo ""
echo "Cloud SQL setup complete!"
echo "Please use the following information for your Cloud Run deployment:"
echo "PROJECT_ID: $PROJECT_ID"
echo "REGION: $REGION"
echo "DB_USER_PASSWORD: $DB_USER_PASSWORD"
echo "CLOUD_SQL_CONNECTION_NAME: $CLOUD_SQL_CONNECTION_NAME"