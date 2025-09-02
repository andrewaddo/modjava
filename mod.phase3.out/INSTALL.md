Requiring Java 17, Maven 3.6.3+, Chrome
export JAVA_HOME=/home/ducdo/.local/share/jdk-17.0.16+8 
export PATH=$JAVA_HOME/bin:$PATH 
export PATH=/home/ducdo/.local/share/maven/apache-maven-3.9.11/bin:$PATH
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock 
mvn verify -f mod.phase3.out/pom.xml             
mvn clean install -f mod.phase3.out/pom.xml

## Install Java, Maven

Simply ask Gemini CLI to install these for you

## Install Chrome
sudo tee /etc/yum.repos.d/google-chrome.repo << 'EOF'
[google-chrome]
name=google-chrome
baseurl=http://dl.google.com/linux/chrome/rpm/stable/$basearch
enabled=1
gpgcheck=1
gpgkey=https://dl.google.com/linux/linux_signing_key.pub
EOF

sudo dnf install google-chrome-stable