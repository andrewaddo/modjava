# Prompts

```logs
/p
check git logs, local changes that are not yet committed, run an end-2-end test, and then give me a summary of what phases and sub-stages have you completed and what is pending. Please wait for my instructions before proceeding.
/p 
check git logs, local changes that are not yet committed, run an end-2-end test, and then give me a summary of what phases and sub-stages have you completed and what is pending. refer to mod2.out folder for the sample code, these modules worked fine there
/p 
follow the instruction and start modernizing the shopping-cart app
/p
for those tests with redirection, please change the tests to look at the logs for the redirection action, and avoid waiting for the rediction to actually happen, mark the test limitations and move on.
/p
refer to the code in mod2.out, there are similar elements in there with the solutions that you are solving.
/p
proceed with actual cloud deployment
/p
modify setup_gcp_resources.sh for this purpose for me to run these setup, output the information you then need for the deployment
/p
update deploy.sh to take in parameters for the deployment 
│    PROJECT_ID: addo-argolis-demo                                                  │
│    REGION: asia-southeast1                                                        │
│    DB_USER_PASSWORD: shop18cart                                                   │
│    CLOUD_SQL_CONNECTION_NAME: addo-argolis-demo:asia-southeast1:shopping-cart-db
/p
I have configured the IAM policy manually, document that that step can only be done manually for certain controlled             │
│   environemtn e.g. Argolis. Skip that step and move forward.
/p
the proxy is running, run the app yourself, tell me how to test using the browser, keep tracking logs and fix errorr
/p
the original shopping-cart allows the user to access the default products page without logging in. Logging in is only           │
│   required for cart and payment, etc. You are supposed to stay true to the original legacy app logics.                            

```

# Points

```logs
1. Certain tests were proven to be complex and expensive for the agent to resolve. For examples, GUI redirection tests, which has different delay depending on the underlying logics and gui components. It's logically to mark those tests as limitations and move on instead of pushing the agent.
1. The agent does not have the memory of the previous solutions, or might not have the ability to search widely for alternate solutions. For example, the issue with chromedirver in selenium tests. I would attempt to point it to specific solution as a reference to assist with the current problems. This has been proven to be able to unblock the agent.
1. Avoid asking the agent to leepfrog or complete 5 steps in one as it will face issues that it won't be able to get out off. Try to guide it in smaller step.
1. Cloud Run in Argolis has a limitation of configuring public facing service programmatically. Debugging in Cloud Run is also much more restricted comparing to GKE. Let's use GKE instead of Cloud Run to ensure the deployment is working, before the approach using Cloud Run.
```
                                       
# Logs

```logs
Cloud SQL setup complete!
Please use the following information for your Cloud Run deployment:
PROJECT_ID: addo-argolis-demo
REGION: asia-southeast1
DB_USER_PASSWORD: ******
CLOUD_SQL_CONNECTION_NAME: addo-argolis-demo:asia-southeast1:shopping-cart-db

gcloud beta run services add-iam-policy-binding --region=asia-southeast1 --member=allUsers --role=roles/run.invoker shopping-cart-web-app

./deploy.sh addo-argolis-demo asia-southeast1 shop18cart addo-argolis-demo:asia-southeast1:shopping-cart-db


## Test locally first before GKE
curl -O https://storage.googleapis.com/cloud-sql-proxy/v2.18.0/cloud_sql_proxy.linux.amd64
chmod +x cloud-sql-proxy.linux.amd64
sudo mv cloud-sql-proxy.linux.amd64 /usr/local/bin/cloud_sql_proxy
/usr/local/bin/cloud_sql_proxy addo-argolis-demo:asia-southeast1:shopping-cart-db
 mvn spring-boot:run

Top local postgresql server
sudo systemctl stop postgresql
sudo systemctl disable postgresql
sudo systemctl status postgresql
```