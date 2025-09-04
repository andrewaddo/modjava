# Tested in a fresh VM modjava2

## Install git

```bash
sudo dnf install git
```
## Install JDK 17

```bash
sudo dnf install java-17-openjdk-devel
# if you have more than 1 java version installed
sudo alternatives --config java
```

## Install maven

```bash
sudo dnf install maven
```

# Install podman

```bash
sudo dnf module enable -y container-tools:rhel8
sudo dnf install -y podman
systemctl --user enable --now podman.socket
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock
```

Adding that export to your ~/.bashrc

```bash
echo "export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock" >> ~/.bashrc
```

## Install PostgreSQL

```bash
# client
sudo dnf install postgresql
# server
sudo dnf install postgresql-server
# init
sudo postgresql-setup --initdb
# enable the service
sudo systemctl enable postgresql
sudo systemctl start postgresql
sudo systemctl status postgresql
# change postgres password
sudo -i
root> cd /var/lib/pgsql
root> sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'your_password';"
root> exit
# edit the password settings
sudo vi /var/lib/pgsql/data/pg_hba.conf
# Change `peer` to `md5` or `scram-sha-256`. `md5` is fine for local development.
# local   all             all                                     md5
# # IPv4 local connections:
# host    all             all             127.0.0.1/32            md5
# # IPv6 local connections:
# host    all             all             ::1/128                 md5

sudo systemctl restart postgresql
# configure a local database
sudo -u postgres psql
postgres=# CREATE DATABASE shoppingcart;
postgres=#\q

sudo systemctl restart postgresql
```

## optional: for manual e2e test

```bash
export GOOGLE_API_KEY=YOUR_GEMINI_API_KEY
```

## To run test

``` bash
cd mod.phase3.out
./mvnw clean install
```

## To run e2e test locally

1. Create a Gmail email account
2. [Create a Gmail app password](https://myaccount.google.com/apppasswords)
3. Create application.properties from application.properties.sample under web-app/src/main/resources
4. Update the application.properties file with the DB and Gmail credentials

```bash
cd mod.phase3.out
./mvnw -f web-app/pom.xml spring-boot:run
```

### Additional information

1. Existing users
   1. Guest: guest@gmail.com / guest
   1. Admin: shoppingcartemail@gmail.com / admin