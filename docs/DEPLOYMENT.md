# Deployment Guide

## Local Development Setup

### Prerequisites
- Java 17 JDK
- Maven 3.8+
- Docker & Docker Compose
- Git
- IntelliJ IDEA (recommended) or VS Code

### Step 1: Clone Repository

```bash
git clone <repository-url>
cd E-Commerce
```

### Step 2: Start Infrastructure

```bash
# Start PostgreSQL, Redis, RabbitMQ, Prometheus, Grafana
docker-compose up postgres redis rabbitmq prometheus grafana
```

### Step 3: Build Services

```bash
mvn clean package -DskipTests
```

### Step 4: Run Services in IntelliJ

Each service runs on a different port:

1. **Auth Service** (8081)
   - Main Class: `com.ecommerce.authservice.AuthServiceApplication`
   - Profiles: `default`

2. **Product Service** (8082)
   - Main Class: `com.ecommerce.productservice.ProductServiceApplication`
   - Profiles: `default`

3. **Order Service** (8083)
   - Main Class: `com.ecommerce.orderservice.OrderServiceApplication`
   - Profiles: `default`

4. **User Service** (8084)
   - Main Class: `com.ecommerce.userservice.UserServiceApplication`
   - Profiles: `default`

5. **API Gateway** (8080)
   - Main Class: `com.ecommerce.apigateway.ApiGatewayApplication`
   - Profiles: `default`

---

## Docker Compose Deployment

### Quick Start

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d --build
```

### Service Orchestration

All services are automatically:
- Built with multi-stage Dockerfile
- Started in dependency order
- Connected via bridge network
- Configured with environment variables
- Exposed on defined ports

### Environment Configuration

Services use environment variables for Docker deployments. See `application-docker.yml` files:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:postgres}:${DB_PORT:5432}/${DB_NAME:ecommerce_auth}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:postgres}
```

### Docker Network

All services communicate via the `ecommerce` bridge network:

```bash
# List network
docker network ls

# Inspect network
docker network inspect ecommerce

# Services can reference each other by name:
# auth-service:8081, product-service:8082, etc.
```

### Persistent Volumes

Data persists across container restarts:

```bash
# List volumes
docker volume ls

# View volume location
docker volume inspect ecommerce_postgres_data
```

---

## Production Deployment Considerations

### 1. Security Hardening

```yaml
# JWT Secret - USE A STRONG SECRET IN PRODUCTION
jwt:
  secret: ${JWT_SECRET}  # Load from environment/vault

# Database credentials
spring:
  datasource:
    username: ${DB_USER}
    password: ${DB_PASSWORD}

# RabbitMQ credentials
spring:
  rabbitmq:
    username: ${RABBITMQ_USER}
    password: ${RABBITMQ_PASSWORD}
```

### 2. High Availability

```bash
# Multi-instance deployment
docker-compose scale product-service=3
docker-compose scale order-service=2
```

Add load balancer:
```yaml
nginx:
  image: nginx:alpine
  ports:
    - "80:80"
  volumes:
    - ./nginx.conf:/etc/nginx/nginx.conf
  depends_on:
    - api-gateway
```

### 3. Database Backups

```bash
# Backup PostgreSQL
docker-compose exec postgres pg_dump -U postgres ecommerce_auth > backup.sql

# Restore
docker exec -i <container> psql -U postgres < backup.sql
```

### 4. Logging & Monitoring

```yaml
# Centralized logging
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Loki + Promtail (Grafana)

# Distributed tracing
- Jaeger
- Zipkin
```

### 5. Service Mesh (Optional)

```bash
# Istio for advanced routing, circuit breaking, observability
# Linkerd for lightweight service mesh
```

### 6. Kubernetes Deployment

Create `k8s/` directory with manifests:

```bash
k8s/
├── namespace.yml
├── auth-service/
│   ├── deployment.yml
│   ├── service.yml
│   └── configmap.yml
├── product-service/
├── order-service/
├── user-service/
├── api-gateway/
├── postgres/
│   ├── statefulset.yml
│   └── persistent-volume.yml
├── redis/
├── rabbitmq/
└── prometheus/
```

Deploy to Kubernetes:
```bash
kubectl apply -f k8s/
```

---

## CI/CD Pipeline Setup

### GitHub Actions (Already Configured)

Workflow file: `.github/workflows/ci.yml`

**Triggers:**
- Merge to main/develop
- Pull requests

**Steps:**
1. Build with Maven
2. Run tests
3. Build Docker images
4. Push to registry

### Push to Docker Registry

```yaml
# Add to ci.yml
- name: Push to Docker Hub
  uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: ${{ secrets.DOCKER_USERNAME }}/auth-service:latest
```

### Deploy to Cloud

#### AWS ECS

```bash
# Push to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account>.dkr.ecr.us-east-1.amazonaws.com

docker tag auth-service:latest <account>.dkr.ecr.us-east-1.amazonaws.com/auth-service:latest

docker push <account>.dkr.ecr.us-east-1.amazonaws.com/auth-service:latest
```

#### DigitalOcean App Platform

```bash
# Create doctl configuration
doctl apps create --spec app.yaml
```

#### Google Cloud Run

```bash
# Push to GCR
gcloud builds submit --tag gcr.io/$PROJECT/auth-service

# Deploy
gcloud run deploy auth-service --image gcr.io/$PROJECT/auth-service
```

---

## Monitoring & Troubleshooting

### View Logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs auth-service

# Follow logs
docker-compose logs -f api-gateway

# Last 100 lines
docker-compose logs --tail=100 product-service
```

### Health Checks

```bash
# Check service health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health

# Check API Gateway connectivity
curl -i http://localhost:8080/auth/api/auth/login
```

### Database Maintenance

```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U postgres

# View databases
\l

# Connect to specific database
\c ecommerce_auth

# List tables
\dt

# Execute query
SELECT * FROM users;
```

### RabbitMQ Management

```bash
# Access web UI
open http://localhost:15672  # guest/guest

# CLI commands
docker-compose exec rabbitmq rabbitmqctl list_queues
docker-compose exec rabbitmq rabbitmqctl list_bindings
docker-compose exec rabbitmq rabbitmqctl list_exchanges
```

### Performance Metrics

```bash
# CPU & Memory usage
docker stats

# JVM memory
curl http://localhost:8081/actuator/prometheus | grep jvm_memory

# HTTP request metrics
curl http://localhost:8081/actuator/prometheus | grep http_requests
```

---

## Troubleshooting Guide

### Port Already in Use

```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>

# Or use different port in docker-compose.yml
ports:
  - "8090:8080"
```

### Database Connection Failed

```bash
# Check PostgreSQL is running
docker-compose ps postgres

# Restart database
docker-compose restart postgres

# Check database logs
docker-compose logs postgres
```

### RabbitMQ Connection Failed

```bash
# Check RabbitMQ status
docker-compose exec rabbitmq rabbitmqctl status

# Restart RabbitMQ
docker-compose restart rabbitmq
```

### Out of Disk Space

```bash
# Clean up Docker
docker system prune -a

# Remove volumes
docker volume prune

# Check disk usage
docker system df
```

### Memory Issues

```bash
# Increase Docker memory limit
# Edit Docker Desktop settings or docker-compose.yml

# Add resource limits
services:
  auth-service:
    ...
    deploy:
      resources:
        limits:
          memory: 1G
        reservations:
          memory: 512M
```

---

## Performance Tuning

### Database Connection Pool

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
```

### Redis Cache Optimization

```yaml
spring:
  redis:
    lettuce:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 5
```

### RabbitMQ Performance

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 10
        concurrency: 5
```

### JVM Tuning

```bash
# Adjust heap size in Dockerfile
ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC"

ENTRYPOINT ["java", ${JAVA_OPTS}, "-jar", "/app/auth-service.jar"]
```

---

## Backup & Recovery

### Automated Backups

```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/backups"
DATE=$(date +%Y%m%d_%H%M%S)

# PostgreSQL backup
docker-compose exec -T postgres pg_dump -U postgres ecommerce_auth \
  > "$BACKUP_DIR/ecommerce_auth_$DATE.sql"

# Compress
gzip "$BACKUP_DIR/ecommerce_auth_$DATE.sql"
```

Schedule with cron:
```bash
0 2 * * * /path/to/backup.sh  # Daily at 2 AM
```

### Restore from Backup

```bash
# Restore database
docker-compose exec -T postgres psql -U postgres < backup.sql

# Restore Redis
docker-compose exec redis redis-cli --pipe < backup.rdb
```

---

## Scaling Strategies

### Horizontal Scaling

```bash
# Scale product service to 3 instances
docker-compose up -d --scale product-service=3

# Add load balancer (nginx)
upstream product_service {
  server product-service:8082;
  server product-service_2:8082;
  server product-service_3:8082;
}
```

### Vertical Scaling

Increase resources in docker-compose.yml:
```yaml
auth-service:
  deploy:
    resources:
      limits:
        cpus: '2'
        memory: 2G
```

---

## Maintenance Windows

```bash
# Drain active connections gracefully
docker-compose pause auth-service

# Wait for in-flight requests to complete
sleep 30

# Perform maintenance
docker-compose exec postgres pg_dump ...

# Resume service
docker-compose unpause auth-service
```

---

## Disaster Recovery

### RTO/RPO Targets

- **Recovery Time Objective (RTO):** 1 hour
- **Recovery Point Objective (RPO):** 15 minutes

### Backup Strategy

1. **Database:** Daily backups to S3
2. **Configuration:** Version controlled in Git
3. **Container Images:** Stored in registry
4. **Secrets:** Stored in vault (Vault, AWS Secrets Manager)

### Runbook

1. Restore database from latest backup
2. Pull latest container images
3. Redeploy services to Kubernetes/ECS
4. Verify health checks
5. Monitor logs for errors

---

**Last Updated:** November 17, 2024

