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
/p
user the database schema and script in the legacy shopping-cart folder to create necessary tables and data for the manual       │
│   test. Convert to postgresql if needed. Save the new schema and data.sql in the same folder mod.andrew.out. DO NOT INCLUDE       │
│   these in the unit or integration test. 
/p
please check logs for error, restart the local web-app so I can contiously test it
/p
try the approach to run the app in the background, output the logs to a file so you can monitor. You can also conduct test      │
│   with curl, identify error, and fix if any.
/p - didn't work given how long the image binary is, the read-file step was truncated
parse mysql_query.sql to extract product IDs and ignore the image data. Then, create a new data.sql without image data, a separate
  update_images.sql with Cloud Storage URLs for each product's image, save the binary image data to files, and upload those files to
  Cloud Storage. I'm beginning with image and product ID extraction.
/p 
parse mysql_query.sql to extract product IDs and ignore the image data. Then, create a new data.sql with the image as a url to the image , a separate
  update_images.sql with Cloud Storage URLs for each product's image, create a dummy image file for each product, and upload those files to
  Cloud Storage. I will fix those images manually later.
/p
update the code to the recent change to image from binary to now a url to the image
/p
why is there category_id in the product table? check the legacy code in shopping-cart if this field is there. Then WAIT         │
│   for my instruction to proceed
/p
does category existing in the legacy code shopping-cart ? why is it in the new code mod.andrew.out ?
/p
does category existing in the legacy code shopping-cart ? why is it in the new code mod.andrew.out ? Ignore the generated       │
│   code in mod2.out and mod3.out. Use the legacy code shopping-cart as a base of truth. You should not implement new logics        │
│   unless is told to do so.
/p
step back, scan the code in mod.andrew.out, there are few warnings and errors in the code notified by my ide
/p
no, category_id is not even in the log!!! I checked and there is no tables in the shoppingcart database. Did you implement the   │
│    schema.sql and data.sql to this database ?  
/p
no, you already completed the migration. Now it is testing phase. Run a local test. try the approach to run the app in the      │
│   background, output the logs to a file so you can monitor. You can also conduct test      │                                      │
│   │   with curl, identify error, and fix if any.   
/p
run the local test using the source, not the generated built jar file
/p
why the in schema.sql you renamed the column with "p" as prefix for all column names ? The original mysql_query.sql in the   │
│    legacy app doesn't have this "p" prefix !
/p
sync the column names in schema.sql and data.sql with the legacy mysql_query.sql. When you are done, stop and wait.
/p
start the web-app locally on the background, output logs to app.log, and continue monitor the app.log for errors. If there is error, stop the web-app process, fix the error, then repeat.
/p
refer to the legacy styling in index.jsp, keep the div configuration the same, including the height, image style ="height: 150px; max-width: 180px", the product div section should have the same configuration style='height: 350px;'>
/p
refer to the legacy styling in index.jsp, keep the div configuration the same style='height: 350px;', also apply the css from <div                     │
│   class="thumbnail"> for the image      
/p
> answer this, but dont start making changes. The legacy app has "Add to Cart" and "Buy Now" buttons for each product on the   │
│    index page, why the new page has "View" and "Add to Cart" ?                                                                  │
╰─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────╯

✦ The new app's product page uses "View" and "Add to Cart" instead of "Buy Now" for a standard e-commerce UX. "View" leads to a detailed
   product page for informed decisions, while "Add to Cart" supports flexible multi-item shopping. "Buy Now" is likely reserved for the
  product details or checkout, avoiding clutter and promoting a guided purchasing workflow. I will explain this to the user.
/p
continue monitor the app.log for errors. If there is error, stop the web-app process, fix the error, then repeat.
/p
it seems like the user table may have a conflict with the system user table in postgres, can you double check? search the   │
│    internet if needed  
/p
applied quick fix to the table user by escape quote it in the select queries 
/p
there is an error in the log due to the clash between the user table and the postgres system user table. applied quick fix to   │
│    the table user by escape quote it in the select queries
/p
you just finished the code migration. Now it's the testing phase. Roger ?
/p
For the testing phase, use the remote Cloud SQL instance shopping-cart-db, database shoppingcart, region asia-southeast1,   │
│    project addo-argolis-demo. Roger?
/p
now, you just renamed the user table to app_user, scan the code for relevance plases to cascade this name change
/p
DO NOT INCLUDE the data.sql file in the unit or integration tests
/p
change the plan, test with local postgresql server instead of the Cloud SQL. Make changes so that schema.sql and data.sql can be │
│     used to create database tables and populate data for local testing. 
/p
it's pointless (or hopeless) to expect the agent to be able to get it right the first time. Small steps. Test the app locally first!
/p
you just finished the code migration. Now it's the testing phase. Roger ?
run the test locally, with the code not the built jar, check and kill if the previous process is still running on port 8081, output the logs to mod.andrew.out/web-app/app.log, and clean up app.log for each run, continue monitor the app.log for errors. If there is error, stop the web-app process, fix the error, then repeat.

```

# Limitations

```logs
The most significant concern is the change in the orders table's primary key. This will break the application's ability to handle
  multiple items within a single order.
```

# Points

```logs
1. Certain tests were proven to be complex and expensive for the agent to resolve. For examples, GUI redirection tests, which has different delay depending on the underlying logics and gui components. It's logically to mark those tests as limitations and move on instead of pushing the agent.
1. The agent does not have the memory of the previous solutions, or might not have the ability to search widely for alternate solutions. For example, the issue with chromedirver in selenium tests. I would attempt to point it to specific solution as a reference to assist with the current problems. This has been proven to be able to unblock the agent.
1. Avoid asking the agent to leepfrog or complete 5 steps in one as it will face issues that it won't be able to get out off. Try to guide it in smaller step.
1. Cloud Run in Argolis has a limitation of configuring public facing service programmatically. Debugging in Cloud Run is also much more restricted comparing to GKE. Let's use GKE instead of Cloud Run to ensure the deployment is working, before the approach using Cloud Run.
1. In an IDE, e.g.e Eclipse, when there is a change to an element of the code e.g. rename id to pid, the changes will be cascaded to all other references. This is not the same behavior when the agent made a change, it did not cascade the changes to other places. This led to a very expensive regression changes process.
1. The agent is definitely having limitation in managing cross-references between schema, data and java code when one change is required. For example, given the difficulty of migrating binary image data, we changed to use the URL to the image stored in GCS which is the better approach given the media can be cached. The agent couldn't identify all related places where the image is used to cascade the changes accordingly.
1. For some reasons, the agent generated Category.java which was not there in the legacy code.
1. The first few runs, the new app didn't route user to the login page upon adding the product to the cart. Obviously, the feature "redirect the user to the login page if the user is not yet logged in, when the user adds a product to the cart" was not there. The generated feature files should be reviewed carefully and specify fully before implementing.

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

mvn -f mod.andrew.out/pom.xml clean install

mvn -f mod.andrew.out/web-app/pom.xml spring-boot:run &

mvn -f mod.andrew.out/web-app/pom.xml spring-boot:run > mod.andrew.out/web-app/app.log 2>&1 &

gsutil iam ch serviceAccount:p827859227929-dtoer1@gcp-sa-cloud-sql.iam.gserviceaccount.com:objectCreator gs://addo-modjava

psql -U postgres -d shoppingcart -f mod.andrew.out/web-app/src/main/resources/schema.sql
psql -U postgres -d shoppingcart -f mod.andrew.out/web-app/src/main/resources/data.sql

Facing Gemini CLI ratelimit issue
https://github.com/google-gemini/gemini-cli/issues/1502
```