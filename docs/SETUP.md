# Environment Setup Guide

## Prerequisites Installation

### Windows 10/11

#### 1. Java 17 JDK

```bash
# Option A: Using Chocolatey
choco install openjdk17

# Option B: Manual Download
# Download from: https://adoptium.net/
# Set JAVA_HOME environment variable
# Add %JAVA_HOME%\bin to PATH

# Verify installation
java -version
javac -version
```

#### 2. Maven

```bash
# Option A: Using Chocolatey
choco install maven

# Option B: Manual Download
# Download from: https://maven.apache.org/download.cgi
# Extract to C:\Program Files\Maven
# Add MAVEN_HOME to environment variables
# Add %MAVEN_HOME%\bin to PATH

# Verify installation
mvn -version
```

#### 3. Docker Desktop

```bash
# Download from: https://www.docker.com/products/docker-desktop
# Install and enable WSL2 backend

# Verify installation
docker --version
docker-compose --version
```

#### 4. Git

```bash
# Download from: https://git-scm.com/download/win
# Use default installation

# Verify installation
git --version
```

#### 5. IDE: IntelliJ IDEA

```bash
# Option A: IntelliJ Community (Free)
# Download from: https://www.jetbrains.com/idea/download/

# Option B: IntelliJ Ultimate (Paid)
# Full-featured Spring Boot development

# After installation:
# 1. Install Spring Boot plugin (usually pre-installed)
# 2. Install Docker plugin
# 3. Install RabbitMQ Browser plugin (optional)
```

---

### macOS

#### 1. Java 17 JDK

```bash
# Using Homebrew
brew install openjdk@17

# Set JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc

# Verify
java -version
```

#### 2. Maven

```bash
brew install maven

# Verify
mvn -version
```

#### 3. Docker Desktop

```bash
# Download from: https://www.docker.com/products/docker-desktop
# Install Apple Silicon or Intel version

# Verify
docker --version
docker-compose --version
```

#### 4. Git

```bash
brew install git

# Configure (if first time)
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

#### 5. IDE: IntelliJ IDEA

```bash
# Using Homebrew
brew install --cask intellij-idea-community

# Or download directly from:
# https://www.jetbrains.com/idea/download/
```

---

### Linux (Ubuntu/Debian)

#### 1. Java 17 JDK

```bash
sudo apt update
sudo apt install openjdk-17-jdk-headless openjdk-17-jre

# Verify
java -version
```

#### 2. Maven

```bash
sudo apt install maven

# Verify
mvn -version
```

#### 3. Docker

```bash
# Install Docker
sudo apt install docker.io docker-compose

# Add current user to docker group (avoid sudo)
sudo usermod -aG docker $USER

# Verify (after relogin)
docker --version
docker-compose --version
```

#### 4. Git

```bash
sudo apt install git

# Configure
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

#### 5. IDE: IntelliJ IDEA

```bash
sudo snap install intellij-idea-community --classic

# Or download from:
# https://www.jetbrains.com/idea/download/
```

---

## Environment Variables Configuration

### Windows PowerShell

```powershell
# Check current Java version
$env:JAVA_HOME

# Temporarily set for current session
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.1+12"
$env:MAVEN_HOME = "C:\Program Files\apache-maven-3.8.1"
$env:PATH += ";$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin"

# Permanently set (requires admin)
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.1+12"
setx MAVEN_HOME "C:\Program Files\apache-maven-3.8.1"
```

### Linux/macOS Bash

```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export MAVEN_HOME=/usr/local/opt/maven/libexec
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH

# Reload shell
source ~/.bashrc
# or
source ~/.zshrc
```

---

## IntelliJ IDEA Configuration

### 1. Project Setup

1. Open IntelliJ IDEA
2. File → Open → Navigate to E-Commerce directory
3. Choose "Open" to load the project

### 2. Maven Configuration

```
Settings → Build, Execution, Deployment → Build Tools → Maven

✓ Maven home path: Auto-detected or custom path
✓ User settings file: ~/.m2/settings.xml (if needed)
✓ Local repository: ~/.m2/repository
```

### 3. JDK Configuration

```
Settings → Project Structure → Project

Project SDK: 17 (openjdk-17 or similar)
Language level: SDK default (17)
```

### 4. Run Configurations

Create 5 Run Configurations (one per service):

**Auth Service:**
```
Main class: com.ecommerce.authservice.AuthServiceApplication
VM options: -Dspring.profiles.active=default
Program arguments: (leave empty)
Working directory: $ProjectFileDir$
Environment variables: (leave empty)
```

**Product Service:**
```
Main class: com.ecommerce.productservice.ProductServiceApplication
VM options: -Dspring.profiles.active=default
```

**Order Service:**
```
Main class: com.ecommerce.orderservice.OrderServiceApplication
VM options: -Dspring.profiles.active=default
```

**User Service:**
```
Main class: com.ecommerce.userservice.UserServiceApplication
VM options: -Dspring.profiles.active=default
```

**API Gateway:**
```
Main class: com.ecommerce.apigateway.ApiGatewayApplication
VM options: -Dspring.profiles.active=default
```

### 5. Docker Integration

```
Settings → Project Structure → Project SDK

Add new SDK → Docker
Docker daemon: Connect to Docker daemon
URI: unix:///var/run/docker.sock (Linux/macOS)
         npipe:////./pipe/docker_engine (Windows)
```

### 6. Database Tools

```
View → Tool Windows → Database

Click "+" → PostgreSQL
Host: localhost
Port: 5432
User: postgres
Password: postgres
Database: ecommerce_auth

Test Connection → OK
```

---

## VS Code Alternative Setup

### Extensions Required

1. **Extension Pack for Java** (Microsoft)
   - Debugger for Java
   - Language Support for Java
   - Project Manager for Java
   - Visual Studio IntelliCode

2. **Spring Boot Extension Pack** (VMware)
   - Spring Boot Tools
   - Spring Cloud CLI
   - Cloud Code

3. **Docker** (Microsoft)
4. **REST Client** (Huachao Mao)
5. **Rainbow Brackets** (2gua)

### settings.json Configuration

```json
{
  "java.home": "/usr/libexec/java_home -v 17",
  "maven.executable.path": "/usr/local/bin/mvn",
  "[java]": {
    "editor.defaultFormatter": "redhat.java",
    "editor.formatOnSave": true
  },
  "spring-boot.dashboard.openOnStartup": true,
  "spring-boot.ls.java.home": "/path/to/java17"
}
```

---

## Maven Settings Configuration

### ~/.m2/settings.xml

For corporate proxy or custom repositories:

```xml
<settings>
  <proxies>
    <proxy>
      <id>corporate-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy.corporate.com</host>
      <port>8080</port>
      <username>username</username>
      <password>password</password>
    </proxy>
  </proxies>

  <repositories>
    <repository>
      <id>central</id>
      <url>https://repo.maven.apache.org/maven2</url>
    </repository>
  </repositories>

  <pluginRepositories>
    <pluginRepository>
      <id>central</id>
      <url>https://repo.maven.apache.org/maven2</url>
    </pluginRepository>
  </pluginRepositories>
</settings>
```

---

## Docker Configuration

### Docker Desktop Settings

#### Windows/macOS

```
Settings → Resources → Advanced

CPUs: 4
Memory: 8 GB
Swap: 1 GB
Disk image size: 100 GB
```

#### File Sharing (Windows/macOS)

Add project directory:
```
Settings → Resources → File Sharing
Add: /Users/yourname/Projects  (macOS)
Add: C:\Users\YourName\Projects (Windows)
```

### Docker Daemon Configuration

Create or edit `/etc/docker/daemon.json`:

```json
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  },
  "storage-driver": "overlay2",
  "bridge": "docker0"
}
```

Restart Docker after changes.

---

## PostgreSQL Client Setup

### Windows

```bash
# Install pgAdmin (GUI)
choco install pgadmin4

# Or install psql only
choco install postgresql

# Command line connection
psql -h localhost -U postgres -d ecommerce_auth
```

### macOS

```bash
# Install PostgreSQL client via Homebrew
brew install libpq

# Add to PATH
echo 'export PATH="/usr/local/opt/libpq/bin:$PATH"' >> ~/.zshrc

# Connect
psql -h localhost -U postgres -d ecommerce_auth
```

### Linux

```bash
# Already included with docker-compose postgres image
# To connect from host:
sudo apt install postgresql-client

psql -h localhost -U postgres -d ecommerce_auth
```

---

## Useful Tools & Extensions

### Chrome/Firefox Extensions

1. **REST Client / Postman**
   - API testing
   - Save requests
   - Environment variables

2. **JWT Debugger**
   - Decode JWT tokens
   - Verify signatures
   - https://jwt.io/

3. **JSON Formatter**
   - Pretty print JSON
   - Syntax highlighting

### Command Line Tools

```bash
# HTTPie (alternative to curl)
pip install httpie
http POST localhost:8080/auth/api/auth/login \
  username=john_doe password=pass123

# jq (JSON query tool)
curl -s localhost:8080/api/products | jq '.[] | .name'

# Docker compose completion
source <(docker-compose completion bash)  # Linux
eval "$(docker-compose --completion-script-bash)"  # macOS
```

---

## Network Troubleshooting

### Check Port Availability

```bash
# Windows PowerShell
Get-NetTCPConnection -LocalPort 8080

# macOS/Linux
lsof -i :8080
netstat -an | grep 8080
```

### Verify Service Connectivity

```bash
# Test API Gateway
curl -i http://localhost:8080/actuator/health

# Test individual services
curl -i http://localhost:8081/actuator/health  # Auth
curl -i http://localhost:8082/actuator/health  # Product
curl -i http://localhost:8083/actuator/health  # Order
curl -i http://localhost:8084/actuator/health  # User
```

### DNS Resolution

```bash
# Verify Docker network DNS
docker run --rm --net ecommerce alpine nslookup auth-service

# Check network connectivity
docker network inspect ecommerce | grep -A 20 "Containers"
```

---

## Performance Tuning

### JVM Heap Size

```bash
# Set for local development
export JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC"

# Or in IDE run configuration
VM options: -Xms512m -Xmx1g -XX:+UseG1GC -XX:+UnlockDiagnosticVMOptions
```

### Maven Performance

```bash
# Parallel builds
mvn clean install -T 4  # Use 4 threads

# Skip tests
mvn clean package -DskipTests

# Offline mode (after first build)
mvn clean package -o
```

### Docker Performance

```bash
# Use BuildKit (faster builds)
export DOCKER_BUILDKIT=1
docker-compose build

# Prune unused resources
docker system prune -a
docker volume prune
```

---

## Troubleshooting Common Issues

### Issue: "Could not find or load main class"

```bash
# Solution: Rebuild Maven
mvn clean compile

# Or check classpath
mvn dependency:tree
```

### Issue: "Connection refused" to database

```bash
# Solution: Ensure PostgreSQL is running
docker-compose ps postgres

# Or restart
docker-compose restart postgres
```

### Issue: Port already in use

```bash
# Windows PowerShell
$ProcessId = (Get-NetTCPConnection -LocalPort 8080).OwningProcess
Stop-Process -Id $ProcessId -Force

# macOS/Linux
kill -9 $(lsof -t -i :8080)
```

### Issue: Docker daemon not running

```bash
# Restart Docker daemon
# Windows: Docker Desktop → Restart
# macOS: open --hide -a Docker
# Linux: sudo systemctl restart docker
```

### Issue: Out of disk space

```bash
# Clean Docker
docker system prune -a --volumes

# Check disk usage
docker system df

# Linux: Check file system
df -h
```

---

## CI/CD Local Testing

### GitHub Actions Locally

Install act (local GitHub Actions runner):

```bash
# macOS
brew install act

# Linux
# Download from: https://github.com/nektos/act/releases

# Run workflow
act -j build
```

### Pre-commit Hooks

```bash
#!/bin/bash
# .git/hooks/pre-commit

mvn clean test
if [ $? -ne 0 ]; then
  echo "Tests failed. Commit aborted."
  exit 1
fi
```

---

## Debugging Tips

### Enable Debug Mode

```bash
# Java remote debugging
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
  -jar app.jar

# IntelliJ: Run → Debug (or Shift+F9)
```

### Spring Boot Debug Logging

```yaml
logging:
  level:
    com.ecommerce: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

### Docker Container Debugging

```bash
# Connect to running container
docker exec -it order-service /bin/sh

# View logs
docker logs -f order-service

# Inspect resource usage
docker stats order-service
```

---

**Last Updated:** November 17, 2024

