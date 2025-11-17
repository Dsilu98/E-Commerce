# Microservice E-Commerce Platform

A complete, educational microservice e-commerce platform built with Java 17, Spring Boot 3.x, and modern cloud-native technologies.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Client Applications                          │
└────────────────────────────┬────────────────────────────────────────┘
                             │
                      ┌──────▼──────┐
                      │ API Gateway │ (Port 8080)
                      │ Spring Cloud│
                      │   Gateway   │
                      └──────┬──────┘
        ┌─────────────────────┼─────────────────────┬──────────────┐
        │                     │                     │              │
    ┌───▼────┐          ┌────▼───┐          ┌──────▼──┐       ┌──▼──────┐
    │  Auth  │          │Product │          │ Order  │       │  User   │
    │Service │          │Service │          │Service │       │ Service │
    │ :8081  │          │ :8082  │          │ :8083  │       │ :8084   │
    └───┬────┘          └────┬───┘          └──┬─────┘       └────┬────┘
        │                    │                 │                  │
        └────────────────────┼─────────────────┼──────────────────┘
                             │                 │
                    ┌────────▼──────────┐      │
                    │   PostgreSQL      │      │
                    │   Database        │      │
                    └───────────────────┘      │
                                       ┌──────▼─────────┐
                                       │   RabbitMQ     │
                                       │   Message Broker
                                       └────────────────┘
        ┌─────────────────────┬─────────────────┐
        │                     │                 │
    ┌───▼────┐           ┌───▼────┐       ┌────▼───┐
    │ Redis  │           │Prometheus     │ Grafana│
    │ Cache  │           │ Monitoring    │        │
    └────────┘           └────────┘      └────────┘
```

## Tech Stack

- **Java 17** - Language
- **Spring Boot 3.2** - Framework
- **Spring Cloud Gateway** - API Gateway & Routing
- **Spring Security + JWT** - Stateless Authentication
- **Spring Data JPA + Hibernate** - ORM
- **PostgreSQL** - Primary Database
- **Redis** - Caching Layer
- **RabbitMQ** - Asynchronous Messaging
- **Spring AMQP** - Message Integration
- **Resilience4j** - Circuit Breaker & Fault Tolerance
- **Lombok** - Boilerplate Reduction
- **MapStruct** - DTO Mapping
- **Micrometer + Prometheus** - Monitoring
- **Docker + Docker Compose** - Containerization
- **Maven** - Build Tool

## Project Structure

```
.
├── api-gateway/                 # Spring Cloud Gateway
├── auth-service/                # JWT Authentication
├── product-service/             # Product Catalog with Redis Cache
├── order-service/               # Order Processing with RabbitMQ & Resilience4j
├── user-service/                # User Profiles
├── docker-compose.yml           # Full stack orchestration
├── prometheus.yml               # Prometheus configuration
├── pom.xml                      # Root Maven POM (parent)
├── README.md                    # This file
├── .gitignore
└── .github/workflows/ci.yml     # GitHub Actions CI/CD
```

## Service Descriptions

### Auth Service (Port 8081)
- User registration & login
- JWT token generation & validation
- BCrypt password hashing
- PostgreSQL persistence

**Key Endpoints:**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Get JWT token
- `GET /actuator/health` - Health check
- `GET /actuator/prometheus` - Metrics

**Database:** `ecommerce_auth`

### Product Service (Port 8082)
- Product catalog management
- Redis caching (30 seconds TTL)
- Stock management
- Role-based product creation (ADMIN only)

**Key Endpoints:**
- `GET /api/products` - List all products (cached)
- `GET /api/products/{id}` - Get product details (cached)
- `POST /api/products` - Create product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `PUT /api/products/{id}/stock?quantity=X` - Update stock

**Database:** `ecommerce_product`
**Cache:** Redis with 30s TTL

### Order Service (Port 8083)
- Order creation and management
- Real-time product stock validation via WebClient
- Resilience4j circuit breaker for product-service calls
- RabbitMQ event publishing (order.created)
- Order event consumption via @RabbitListener

**Key Endpoints:**
- `POST /api/orders` - Create order (requires JWT)
- `GET /api/orders/{id}` - Get order details
- `GET /api/orders/user/{userId}` - Get user's orders

**Database:** `ecommerce_order`
**Message Broker:** RabbitMQ
- Exchange: `ecommerce`
- Routing Key: `order.created`
- Queue: `order.created`

**Features:**
- Validates JWT token
- Fetches product details from product-service
- Validates stock availability
- Updates stock via product-service
- Publishes order.created event
- Circuit breaker fallback for product-service failures

### User Service (Port 8084)
- User profile management
- Email, address, phone storage
- Public read access
- JWT-protected write operations

**Key Endpoints:**
- `GET /api/users/{userId}` - Get user profile (public)
- `POST /api/users` - Create user profile
- `PUT /api/users/{userId}` - Update profile (JWT required)

**Database:** `ecommerce_user`

### API Gateway (Port 8080)
- Request routing to microservices
- CORS handling
- Authorization header forwarding
- Route-level security

**Routing:**
- `/auth/**` → auth-service:8081
- `/products/**` → product-service:8082
- `/orders/**` → order-service:8083
- `/users/**` → user-service:8084

## Quick Start

### Prerequisites

- Docker & Docker Compose (for containerized setup)
- Java 17 (for local development)
- Maven 3.8+
- Git

### Option 1: Docker Compose (Recommended for Testing)

```bash
# Navigate to project directory
cd E-Commerce

# Build and start all services
docker-compose up --build

# Services will be available at:
# API Gateway: http://localhost:8080
# Auth Service: http://localhost:8081
# Product Service: http://localhost:8082
# Order Service: http://localhost:8083
# User Service: http://localhost:8084
# RabbitMQ Admin: http://localhost:15672 (guest/guest)
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3000 (admin/admin)
```

### Option 2: Local Development (IntelliJ IDEA)

#### 1. Start Infrastructure Services

```bash
# Start Docker services only (no Spring apps)
docker-compose up postgres redis rabbitmq prometheus grafana
```

#### 2. Build All Services

```bash
mvn clean package -DskipTests
```

#### 3. Run Each Service in IntelliJ

Create Run Configurations for each service:

**Auth Service:**
- Main Class: `com.ecommerce.authservice.AuthServiceApplication`
- VM Options: `-Dspring.profiles.active=default`
- Working Directory: `$ProjectFileDir$`
- Port: 8081

**Product Service:**
- Main Class: `com.ecommerce.productservice.ProductServiceApplication`
- VM Options: `-Dspring.profiles.active=default`
- Port: 8082

**Order Service:**
- Main Class: `com.ecommerce.orderservice.OrderServiceApplication`
- VM Options: `-Dspring.profiles.active=default`
- Port: 8083

**User Service:**
- Main Class: `com.ecommerce.userservice.UserServiceApplication`
- VM Options: `-Dspring.profiles.active=default`
- Port: 8084

**API Gateway:**
- Main Class: `com.ecommerce.apigateway.ApiGatewayApplication`
- VM Options: `-Dspring.profiles.active=default`
- Port: 8080

## Sample API Calls

### 1. Register User

```bash
curl -X POST http://localhost:8080/auth/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "message": "User registered successfully"
}
```

### 2. Login & Get JWT Token

```bash
curl -X POST http://localhost:8080/auth/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400
}
```

### 3. Create Product (Admin Only)

First, register an admin user or update an existing user in the database:

```bash
curl -X POST http://localhost:8080/products/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "stock": 50
  }'
```

### 4. Get All Products (Cached)

```bash
curl -X GET http://localhost:8080/products/api/products
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "stock": 50
  }
]
```

### 5. Create Order

```bash
curl -X POST http://localhost:8080/orders/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

**Response:**
```json
{
  "id": 1,
  "userId": 123,
  "totalAmount": 2599.98,
  "status": "CONFIRMED",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ],
  "createdAt": "2024-11-17T10:30:00"
}
```

### 6. Get User Profile

```bash
curl -X GET http://localhost:8080/users/api/users/1
```

### 7. Update User Profile (Requires JWT)

```bash
curl -X PUT http://localhost:8080/users/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "email": "john_updated@example.com",
    "address": "123 Main St",
    "phone": "555-1234"
  }'
```

## Monitoring

### Prometheus
- Access: http://localhost:9090
- Scrapes metrics every 15 seconds
- Collects metrics from:
  - Auth Service (`:8081/actuator/prometheus`)
  - Product Service (`:8082/actuator/prometheus`)
  - Order Service (`:8083/actuator/prometheus`)
  - API Gateway (`:8080/actuator/prometheus`)

### Grafana
- Access: http://localhost:3000
- Default Credentials: admin/admin
- Add Prometheus as data source:
  - URL: `http://prometheus:9090`

### RabbitMQ Management UI
- Access: http://localhost:15672
- Default Credentials: guest/guest
- View message queues, exchanges, and bindings
- Monitor order.created queue

## Database Initialization

PostgreSQL automatically creates databases via Spring JPA `ddl-auto: update`:

- `ecommerce_auth` - Auth service
- `ecommerce_product` - Product service
- `ecommerce_order` - Order service
- `ecommerce_user` - User service

Connect manually:
```bash
psql -h localhost -U postgres -d ecommerce_auth
```

## Testing

### Run All Tests
```bash
mvn clean test
```

### Run Specific Service Tests
```bash
mvn test -f auth-service/pom.xml
mvn test -f product-service/pom.xml
mvn test -f order-service/pom.xml
mvn test -f user-service/pom.xml
```

### Included Tests
- **Auth Service**: Password hashing & repository tests
- **Product Service**: Product repository CRUD tests
- **Order Service**: WebClient mocking with Resilience4j
- **User Service**: Basic context loading tests

## Building Docker Images

### Build All Images
```bash
docker-compose build
```

### Build Specific Image
```bash
docker build -t auth-service:latest -f auth-service/Dockerfile .
docker build -t product-service:latest -f product-service/Dockerfile .
docker build -t order-service:latest -f order-service/Dockerfile .
docker build -t user-service:latest -f user-service/Dockerfile .
docker build -t api-gateway:latest -f api-gateway/Dockerfile .
```

### Multi-Stage Build Benefits
- Reduced image size (only runtime dependencies)
- Faster deployment
- Separate build and runtime stages

## CI/CD Pipeline

### GitHub Actions Workflow
Located in `.github/workflows/ci.yml`

**Triggers:**
- Push to main/develop branches
- Pull requests to main/develop

**Steps:**
1. Checkout code
2. Setup Java 17
3. Build with Maven (-DskipTests)
4. Run unit tests
5. Build Docker images
6. Upload artifacts

### Running Locally
```bash
# This mimics the CI pipeline
mvn clean package
mvn test
docker-compose build
```

## Key Features & Implementation Details

### 1. JWT Authentication
- Stateless authentication via JWT tokens
- HS256 signing algorithm
- 24-hour token expiration
- Extracted in every request header

### 2. Product Caching with Redis
- 30-second TTL on product listings
- Cache invalidation on create/update
- Used by product-service and order-service

### 3. RabbitMQ Messaging
- Direct exchange: `ecommerce`
- Routing key: `order.created`
- Event-driven order processing
- @RabbitListener for async consumption

### 4. Circuit Breaker (Resilience4j)
- Protects order-service from product-service failures
- Failure threshold: 50%
- Half-open state: 10 seconds wait
- Fallback method for graceful degradation

### 5. MapStruct DTOs
- Type-safe DTO mapping
- Zero-reflection performance
- Used across all services

### 6. CORS Configuration
- Allows requests from any origin (configurable)
- Exposes Authorization headers
- Supports all HTTP methods

### 7. Actuator Endpoints
All services expose:
- `/actuator/health` - Health check
- `/actuator/prometheus` - Metrics (if included)
- `/actuator/info` - Application info

## Environment Variables

### Docker Compose Environment (Auto-configured)

For local development override in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce_auth
    username: postgres
    password: postgres
  rabbitmq:
    host: localhost
    port: 5672
  redis:
    host: localhost
    port: 6379

jwt:
  secret: my-super-secret-key-...
  expiration: 86400000
```

## Troubleshooting

### Services Won't Start
```bash
# Check if ports are in use
lsof -i :8080  # API Gateway
lsof -i :8081  # Auth Service
lsof -i :8082  # Product Service
lsof -i :8083  # Order Service
lsof -i :8084  # User Service

# Kill process if needed
kill -9 <PID>
```

### Database Connection Issues
```bash
# Check PostgreSQL is running
docker-compose ps postgres

# View logs
docker-compose logs postgres

# Reset database
docker volume rm ecommerce-postgres_data
docker-compose up postgres -d
```

### RabbitMQ Connection Issues
```bash
# Check RabbitMQ status
docker-compose logs rabbitmq

# Access management UI
http://localhost:15672
```

### Cache Issues
```bash
# Clear Redis cache
docker-compose exec redis redis-cli FLUSHALL

# Verify Redis
docker-compose exec redis redis-cli ping
```

## Performance Tuning

### Hibernate Optimization
- Batch size: 20 (configured in each service)
- Enabled insert/update ordering
- Lazy loading for associations

### Redis Caching Strategy
- Product listings: 30s TTL
- Invalidate on every write
- Consider cache-aside pattern for high traffic

### RabbitMQ Tuning
- Pre-fetch count: 1 (default)
- Message persistence enabled
- Dead letter exchange for failed messages

### Database Optimization
- Indexes on frequently queried columns
- Connection pooling: default 10 connections
- Query optimization via @Query annotations

## Contributing

1. Create feature branch: `git checkout -b feat/your-feature`
2. Commit changes: `git commit -m "feat: description"`
3. Push to branch: `git push origin feat/your-feature`
4. Create Pull Request

### Commit Convention
- `chore:` - Build/infrastructure changes
- `feat:` - New features
- `fix:` - Bug fixes
- `docs:` - Documentation
- `test:` - Test additions/updates

## License

This project is provided as educational material.

## Support & Documentation

- Spring Boot: https://spring.io/projects/spring-boot
- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Spring Security: https://spring.io/projects/spring-security
- RabbitMQ: https://www.rabbitmq.com/documentation.html
- Redis: https://redis.io/documentation
- Resilience4j: https://resilience4j.readme.io/
- Docker Compose: https://docs.docker.com/compose/

## Architecture Decisions

### Why Microservices?
- Independent deployment & scaling
- Technology flexibility per service
- Clear separation of concerns
- Easier team autonomy

### Why RabbitMQ over Kafka?
- Simpler setup for small-to-medium projects
- Built-in retry & DLX mechanisms
- Lower operational overhead
- Perfect for this educational scale

### Why Redis for Caching?
- Fast in-memory operations
- Automatic TTL support
- Spring integration via @Cacheable
- Scales horizontally

### Why Resilience4j?
- Lightweight & composable
- Spring Boot integration
- Fine-grained configuration
- Observable via metrics

## Next Steps / Future Enhancements

- [ ] Distributed tracing with Jaeger/Zipkin
- [ ] Config server for centralized configuration
- [ ] Service discovery with Consul/Eureka
- [ ] API versioning & deprecation strategy
- [ ] GraphQL layer for flexible queries
- [ ] Rate limiting & throttling
- [ ] Advanced caching strategies (multi-level)
- [ ] Saga pattern for distributed transactions
- [ ] Integration tests with Testcontainers
- [ ] Security: mTLS, OAuth2 Proxy

---

**Last Updated:** November 17, 2024
**Version:** 1.0.0

