# Milestone

Aug 24 2pm: cart works
Aug 26 9:40pm: logout works, payment works
Aug 31 : starting with BMAD to implement / enhance the search function

# Prompts

```logs
/p
ok, make a git commit to mark this milestone, then proceed to the next story  
/p
@architect please review the generated materials, if it is ok, proceed         │
│   with develeper role to implement the shopping-cart-search feature in           │
│   mod.phase3.out/features/shopping-cart-search/prd.md         
/p BMAD
Start with PM (to generate PRD), then Architect (Winston) (to generate TDD, docs), then Dev (James)
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
│   test. Convert to postgresql if needed. Save the new schema and data.sql in the same folder mod.phase3.out. DO NOT INCLUDE       │
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
does category existing in the legacy code shopping-cart ? why is it in the new code mod.phase3.out ?
/p
does category existing in the legacy code shopping-cart ? why is it in the new code mod.phase3.out ? Ignore the generated       │
│   code in mod2.out and mod3.out. Use the legacy code shopping-cart as a base of truth. You should not implement new logics        │
│   unless is told to do so.
/p
step back, scan the code in mod.phase3.out, there are few warnings and errors in the code notified by my ide
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
run the test locally, with the code not the built jar, check and kill if the previous process is still running on port 8081, output the logs to mod.phase3.out/web-app/app.log, and clean up app.log for each run, continue monitor the app.log for errors. If there is error, stop the web-app process, fix the error, then repeat.
p/
can you check the profile page, I had error going to /profile 
/p
 I like the layout in /checkout or /profile, specifically on the menu. Can you   │
│    make the landing index page looks consistent like that ?      
/p
 hm, the index page still have a different look and feel. The "Shopping   │
│    Center" and the home icon is not the same with /checkout or /profile
/p
the /orders page is not working
/p
the page /cart is not styled, it does not have any css like the other pages
/p
the page /login is not styled, it does not have any css like the other pages
/p
there are links to /products page which is not working
/p
 hm, the styling for the landing page is still not the same with /cart page     │
│    which I like. Please rework the dropdown menu link in the landing page to be   │
│    consistent with the other pages such as /cart  
/p
the /orders page menu is not consitent with other pages
/p
the styling for each product box in the landing page (or products page) was   │
│    distorted with the previous version change. Don't change the verion, I like   │
│    this verion. Just change the product box e.g. white background, align the     │
│    view button, "add to cart" button and the price accordingly
/p
well changes.css is not found on the client browser
/p
first thing first, the product card white box is now working with              │
│   changes.css is loaded. But there is no gap between the above and the           │
│   below products. Please align the product text centrally. Also, the             │
│   "View", "Add to Cart" buttons are not aligned. Please refer to the             │
│   legacy app for reference styling. Or do it better yourself.
/p
ok, that's better. Now, align the make the "View" link a button with the same    │
│    look and feel like "Add to Cart" button. Align them on the same line with the    │
│    pricing details. Fix the pricing placement as it is too close to the box border.
/p
clearly I don't want to have email password commited to github, how should I go │
│     about fixing that failed test ? 
/p
refactor the email testing code to avoid using sending the actual email
/p
Please redirect the user to the previous page the user was in before           │
│   signing in. If there is no previous page, redirect them to the landing         │
│   page.
/p
please update the password in data.sql to use BCrypt instead of plain text
/p
please update the password in data.sql to use BCrypt instead of the old        │
│   plain text. The data.sql is in web-app/src/main/resources. There are 2         │
│   records to add users to app_user, with the column password. Please keep        │
│   the same password values, but encrypt it while saving to the database.
/p
mark that add product test as a technical debt, then run a test
/p
migrate UserAuthenticationIT.java from integration-tests to web-app
/p
validate UserAuthenticationIT.java by comparing it to ShoppingCartIT.java   │
│    which passed all the tests
/p
do you have test for the logout function ? if not, implement it
/p
/funny
analyze the test method testProductManagement() and the relevant HTML
  templates to identify and fix any incorrect locators or assertions.
  I've reviewed fragments/header.html.

  There are no explicit navigation links for admin-specific pages like
  /admin/products in the header. The links available are for "Products", "Cart",
  "Orders", and "Profile" when authenticated.

  Therefore, after logging in as admin@test.com, you will be redirected to the home
  page (/). From there, you will need to manually navigate to the admin products
  page by typing the URL directly in your browser:

  http://localhost:8080/admin/products (or your application's port).

╭───────────────────────────────────────────────────────────────────────────────╮
│  > add a menu link to go to that page if the user is logged in as admin role 
/p
can you add tests for the recent fixes? It's bad not having those tests to   │
│    start with   
/p
evert UI tests, it's too hard for you. Keep and implement more page           │
│   routing tests.  
```

# Limitations

```logs
The most significant concern is the change in the orders table's primary key. This will break the application's ability to handle
  multiple items within a single order.
```

# Points

```logs
1. BMAD is promising. It's still pretty tricky to resume from an interupted session. 
1. Certain tests were proven to be complex and expensive for the agent to resolve. For examples, GUI redirection tests, which has different delay depending on the underlying logics and gui components. It's logically to mark those tests as limitations and move on instead of pushing the agent.
1. The agent does not have the memory of the previous solutions, or might not have the ability to search widely for alternate solutions. For example, the issue with chromedirver in selenium tests. I would attempt to point it to specific solution as a reference to assist with the current problems. This has been proven to be able to unblock the agent.
1. Avoid asking the agent to leepfrog or complete 5 steps in one as it will face issues that it won't be able to get out off. Try to guide it in smaller step.
1. Cloud Run in Argolis has a limitation of configuring public facing service programmatically. Debugging in Cloud Run is also much more restricted comparing to GKE. Let's use GKE instead of Cloud Run to ensure the deployment is working, before the approach using Cloud Run.
1. In an IDE, e.g.e Eclipse, when there is a change to an element of the code e.g. rename id to pid, the changes will be cascaded to all other references. This is not the same behavior when the agent made a change, it did not cascade the changes to other places. This led to a very expensive regression changes process.
1. The agent is definitely having limitation in managing cross-references between schema, data and java code when one change is required. For example, given the difficulty of migrating binary image data, we changed to use the URL to the image stored in GCS which is the better approach given the media can be cached. The agent couldn't identify all related places where the image is used to cascade the changes accordingly.
1. For some reasons, the agent generated Category.java which was not there in the legacy code.
1. The first few runs, the new app didn't route user to the login page upon adding the product to the cart. Obviously, the feature "redirect the user to the login page if the user is not yet logged in, when the user adds a product to the cart" was not there. The generated feature files should be reviewed carefully and specify fully before implementing.
1. For cost optimisation, maybe it's a good strategy to avoid asking the agent to run test, check for errors and fix those, since the test logs can be huge. We can run the test separately, then copy/paste the error back to the agent. Once manual test past, ask the agent to run full test.
1. The agent is obviously not great at CSS and GUI tests
1. Flash is not even close yet for this task!
1. Sometimes the agent failed to proceed, but immediately resolved all issues the morning after. What caused this behavior? 
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

mvn -f mod.phase3.out/pom.xml clean install

sudo lsof -i :8081
sudo netstat -tulnp | grep 8081
 kill -9 <PID>

mvn test -f mod.phase3.out/web-app/pom.xml
mvn -f mod.phase3.out/web-app/pom.xml spring-boot:run &

mvn -f mod.phase3.out/web-app/pom.xml spring-boot:run > mod.phase3.out/web-app/app.log 2>&1 &

gsutil iam ch serviceAccount:p827859227929-dtoer1@gcp-sa-cloud-sql.iam.gserviceaccount.com:objectCreator gs://addo-modjava

psql -U postgres -d shoppingcart -f mod.phase3.out/web-app/src/main/resources/schema.sql
psql -U postgres -d shoppingcart -f mod.phase3.out/web-app/src/main/resources/data.sql

Facing Gemini CLI ratelimit issue
https://github.com/google-gemini/gemini-cli/issues/1502


ask Gemini CLI to run
`cd mod.phase3.out && ./mvnw clean install`

Requiring Java 17, Maven 3.6.3+, Google Chrome 139.0.7258.66
export JAVA_HOME=/home/ducdo/.local/share/jdk-17.0.16+8 
export PATH=$JAVA_HOME/bin:$PATH 
export PATH=/home/ducdo/.local/share/maven/apache-maven-3.9.11/bin:$PATH
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock 


mvn verify -f mod.phase3.out/pom.xml             
mvn clean install -f mod.phase3.out/pom.xml

## Install Java, Maven

Simply ask Gemini CLI to install these for you

## Maven wrapper
cd mod.phase3.out && mvn -N wrapper:wrapper

## Install Chrome
sudo tee /etc/yum.repos.d/google-chrome.repo << 'EOF'
[google-chrome]
name=google-chrome
baseurl=http://dl.google.com/linux/chrome/rpm/stable/$basearch
enabled=1
gpgcheck=1
gpgkey=https://dl.google.com/linux/linux_signing_key.pub
EOF
### Re-install Chrome
sudo dnf remove google-chrome-stable
sudo rpm -i /path/to/google-chrome-stable-139.0.7258.66-1.x86_64.rpm

sudo dnf install google-chrome-stable

## Install postgresql

```bash
# Test the app locally
## client
sudo dnf install postgresql # client
## server
sudo dnf install postgresql-server # server
### initialize the database
sudo postgresql-setup --initdb
sudo systemctl enable postgresql
sudo systemctl start postgresql
sudo systemctl status postgresql

# Open the `pg_hba.conf` file in a text editor with `sudo` privileges. Find the line that looks like this:
sudo vi /var/lib/pgsql/data/pg_hba.conf

# local   all             postgres                                peer

And change `peer` to `md5` or `scram-sha-256`. `md5` is fine for local development.
# local   all             postgres                                peer
# local   all             all                                     md5
# # IPv4 local connections:
# host    all             all             127.0.0.1/32            md5
# # IPv6 local connections:
# host    all             all             ::1/128                 md5

## change postgres user password

sudo -i
root> cd /var/lib/pgsql
root> sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'your_password';"
root> exit

sudo systemctl restart postgresql


java -jar /home/ducdo/workspaces/modjava/mod.phase3.out/web-app/target/web-app-0.0.1-SNAPSHOT.jar                                                
java -jar /home/ducdo/workspaces/modjava/web-app-0.0.1-SNAPSHOT.jar
```

# BMAD logs

```logs
@.bmad-core/agents/pm.md @.bmad-core/tasks/brownfield-create-epic.md
/logs
The above command started John, the PM
/p
you can find those in mod.phase3.out for the existing tech stack, source tree and coding standards.
/p
You can read mod.phase3.out/gemini-docs for existing docs of the project. For the enhancement, I want to implement a new feature that will provide additional product information using AI web search when a product is selected. 
/p
yes, please keep the  new docs under features/ai-product-info
@ @.bmad-core/agents/sm.md Please draft the stories for the AI Product Information epic.
/logs
The above command started BOB, the SM
/p
should the stories be in the same features/ai-product-info folder or a separate docs folder ?
/p
can you edit the story to use an Gemini model API to perform the web           │
│   search for the selected product ?
/p
hm, I changed my mind, let's keep the story generic. The specific API          │
│   implementation using Gemini API or other APIs should be dediced later.         │
│   Do you agree?
/p
yes, proceed with the Gemini API as the chosen solution                        │
│     for now?    
```