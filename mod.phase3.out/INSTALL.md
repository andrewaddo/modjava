cd mod.phase3.out
./mvnw clean install
./mvnw -f web-app/pom.xml spring-boot:run

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