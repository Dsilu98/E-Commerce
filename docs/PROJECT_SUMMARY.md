# Project Summary & Quick Reference

## 🚀 Project Overview

**Microservice E-Commerce Platform** - A complete, production-ready educational project demonstrating modern Java microservices architecture with Spring Boot 3.x.

### Key Statistics

- **5 Microservices** (Auth, Product, Order, User, API Gateway)
- **Technology Stack** Java 17 + Spring Boot 3.2
- **Lines of Code** ~3,000+ (excluding tests)
- **Docker Services** 10 (5 apps + Postgres + Redis + RabbitMQ + Prometheus + Grafana)
- **API Endpoints** 15+
- **Build Time** ~2 minutes (cold build), ~30 seconds (incremental)
- **Deployment Options** Docker Compose, Kubernetes-ready

---

## 📂 Complete Repository Structure

```
E-Commerce/
├── auth-service/                    # JWT Authentication & User Management
│   ├── pom.xml
│   ├── src/main/java/.../
│   │   ├── AuthServiceApplication.java
│   │   ├── controller/AuthController.java
│   │   ├── service/AuthService.java
│   │   ├── repository/UserRepository.java
│   │   ├── entity/User.java
│   │   ├── dto/{RegisterRequest,LoginRequest,LoginResponse}.java
│   │   └── security/{JwtTokenProvider,SecurityConfig}.java
│   ├── src/test/java/.../AuthServiceTest.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-docker.yml
│   │   └── logback-spring.xml
│   └── Dockerfile
│
├── api-gateway/                     # Spring Cloud Gateway & Routing
│   ├── pom.xml
│   ├── src/main/java/.../
│   │   ├── ApiGatewayApplication.java (RouteLocator @Bean)
│   │   └── security/SecurityConfig.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-docker.yml
│   │   └── logback-spring.xml
│   ├── src/test/java/.../ApiGatewayApplicationTest.java
│   └── Dockerfile
│
├── product-service/                 # Product Catalog with Redis Cache
│   ├── pom.xml
│   ├── src/main/java/.../
│   │   ├── ProductServiceApplication.java
│   │   ├── controller/ProductController.java
│   │   ├── service/ProductService.java
│   │   ├── repository/ProductRepository.java
│   │   ├── entity/Product.java
│   │   ├── dto/ProductDTO.java
│   │   ├── mapper/ProductMapper.java
│   │   ├── config/CacheConfig.java
│   │   └── security/SecurityConfig.java
│   ├── src/test/java/.../ProductRepositoryTest.java
│   ├── src/main/resources/
│   │   ├── application.yml (Redis config)
│   │   ├── application-docker.yml
│   │   └── logback-spring.xml
│   └── Dockerfile
│
├── order-service/                   # Order Processing with RabbitMQ & Resilience4j
│   ├── pom.xml
│   ├── src/main/java/.../
│   │   ├── OrderServiceApplication.java
│   │   ├── controller/OrderController.java
│   │   ├── service/OrderService.java
│   │   ├── repository/OrderRepository.java
│   │   ├── entity/Order.java
│   │   ├── dto/{OrderDTO,CreateOrderRequest,OrderItemDTO,ProductDTO}.java
│   │   ├── event/OrderCreatedEvent.java
│   │   ├── listener/OrderEventListener.java (@RabbitListener)
│   │   ├── client/ProductClient.java (WebClient + CircuitBreaker)
│   │   ├── config/{RabbitMQConfig,Resilience4jConfig,RestClientConfig}.java
│   │   ├── security/{JwtTokenProvider,SecurityConfig}.java
│   ├── src/test/java/.../OrderServiceTest.java
│   ├── src/main/resources/
│   │   ├── application.yml (RabbitMQ config)
│   │   ├── application-docker.yml
│   │   └── logback-spring.xml
│   └── Dockerfile
│
├── user-service/                    # User Profile Management
│   ├── pom.xml
│   ├── src/main/java/.../
│   │   ├── UserServiceApplication.java
│   │   ├── controller/UserProfileController.java
│   │   ├── service/UserProfileService.java
│   │   ├── repository/UserProfileRepository.java
│   │   ├── entity/UserProfile.java
│   │   ├── dto/UserProfileDTO.java
│   │   ├── mapper/UserProfileMapper.java
│   │   ├── security/{JwtTokenProvider,SecurityConfig}.java
│   ├── src/test/java/.../UserServiceApplicationTest.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── application-docker.yml
│   │   └── logback-spring.xml
│   └── Dockerfile
│
├── pom.xml                          # Root Maven POM (parent)
├── docker-compose.yml               # Complete orchestration (10 services)
├── prometheus.yml                   # Prometheus scrape config
├── .gitignore
├── .github/workflows/ci.yml         # GitHub Actions CI/CD
├── start.sh & start.bat             # Quick start scripts
│
├── docs/
│   ├── API.md                       # Complete API documentation
│   └── DEPLOYMENT.md                # Deployment & scaling guide
│
└── README.md                        # Main documentation
```

---

## 🔧 Service Port Mapping

| Service | Port | Type | Database |
|---------|------|------|----------|
| API Gateway | 8080 | Gateway | - |
| Auth Service | 8081 | REST API | PostgreSQL |
| Product Service | 8082 | REST API | PostgreSQL + Redis |
| Order Service | 8083 | REST API + RabbitMQ | PostgreSQL |
| User Service | 8084 | REST API | PostgreSQL |
| PostgreSQL | 5432 | Database | - |
| Redis | 6379 | Cache | - |
| RabbitMQ | 5672 | Message Broker | - |
| RabbitMQ Admin | 15672 | Web UI | - |
| Prometheus | 9090 | Metrics | - |
| Grafana | 3000 | Dashboards | - |

---

## 📊 Technology Stack Details

### Core Framework
- **Spring Boot 3.2.0** - Modern Spring boot with native support
- **Java 17** - LTS release with records, pattern matching
- **Maven 3.8+** - Build automation & dependency management

### Database & Caching
- **PostgreSQL 15** - ACID compliance, JSON support
- **Redis 7** - 30-second cache TTL on products
- **Spring Data JPA** - ORM abstraction layer
- **Hibernate 6.x** - Advanced ORM features

### Security & Authentication
- **Spring Security 6.x** - Authentication & authorization
- **JWT (JJWT 0.12)** - Stateless token-based auth
- **BCrypt** - Password hashing
- **CORS** - Cross-origin resource sharing

### Service-to-Service Communication
- **Spring Cloud Gateway** - API Gateway & routing
- **WebClient** - Blocking HTTP client (order → product sync calls)
- **Resilience4j** - Circuit breaker pattern
- **RabbitMQ 3.12** - Async messaging (order.created events)
- **Spring AMQP** - RabbitMQ integration

### Monitoring & Observability
- **Micrometer** - Metrics abstraction
- **Prometheus** - Time-series metrics
- **Grafana** - Visualization & dashboards
- **Logback** - Logging framework
- **Spring Boot Actuator** - Health checks & metrics endpoints

### Development Tools
- **Lombok** - Reduce boilerplate (@Data, @RequiredArgsConstructor)
- **MapStruct** - Zero-reflection DTO mapping
- **Docker** - Container orchestration
- **Docker Compose** - Multi-container orchestration

### Testing
- **JUnit 5** - Modern unit testing
- **Spring Boot Test** - Integration testing
- **Mockito** - Mocking framework
- **TestContainers** - Ready (for future integration tests)

---

## 🔄 Data Flow Diagram

### Registration & Login Flow
```
1. Client → Auth Service: POST /api/auth/register
2. Auth Service: Hash password with BCrypt
3. Auth Service → Database: Save User entity
4. Auth Service → Client: HTTP 201 Created

5. Client → Auth Service: POST /api/auth/login
6. Auth Service → Database: Fetch User by username
7. Auth Service: Verify password with BCrypt
8. Auth Service: Generate JWT token (HS256)
9. Auth Service → Client: Return token + expiresIn
```

### Product Creation Flow
```
1. Client → API Gateway: POST /products/api/products (JWT header)
2. Gateway → Product Service: Forward request
3. Product Service: Verify JWT token
4. Product Service: Check ROLE_ADMIN
5. Product Service → PostgreSQL: Save Product entity
6. Product Service: Invalidate Redis cache (all products)
7. Product Service → Client: HTTP 201 + ProductDTO
```

### Order Creation Flow (Most Complex)
```
1. Client → API Gateway: POST /orders/api/orders (JWT header)
2. Gateway → Order Service: Forward request
3. Order Service: Extract JWT, get userId
4. Order Service → Product Service (WebClient): GET /api/products/{id} (3x)
   - Resilience4j CircuitBreaker wraps call
   - If failure: Fallback method
5. Order Service: Validate stock > quantity
6. Order Service → PostgreSQL: Save Order entity (status: CONFIRMED)
7. Order Service → Product Service: PUT /products/{id}/stock?quantity=X (3x)
8. Order Service → RabbitMQ: Publish OrderCreatedEvent
   - Exchange: ecommerce
   - RoutingKey: order.created
   - Queue: order.created
9. Order Service (Listener): @RabbitListener(queues="order.created")
   - Log event
   - Process async (notifications, metrics, etc.)
10. Order Service → Client: HTTP 201 + OrderDTO
```

---

## 🛡️ Security Architecture

### Authentication Flow
```
Request
  ↓
API Gateway (Forward Authorization header)
  ↓
Spring Security Filter Chain
  ↓
JwtTokenProvider.validateToken()
  ↓
Extract userId/username from JWT
  ↓
Check @PreAuthorize roles if needed
  ↓
Allow/Deny request
```

### Token Structure
```
Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "john_doe",
  "iat": 1700245000,
  "exp": 1700331400
}

Signature: HMACSHA256(header.payload, secret)
```

### Protected Endpoints
- `POST /api/products` - Requires ROLE_ADMIN
- `PUT /api/products/{id}` - Requires ROLE_ADMIN
- `POST /api/orders` - Requires JWT (any user)
- `PUT /api/users/{userId}` - Requires JWT

---

## 📈 Scalability Features

### Horizontal Scaling
- Stateless services (no session affinity needed)
- Load balancing via API Gateway
- Independent databases per service
- Shared cache (Redis) accessible by all instances

### Vertical Scaling
- Docker resource limits (CPU, Memory)
- JVM tuning (heap size, GC settings)
- Connection pooling (HikariCP)
- Database connection limits

### Performance Optimizations
- Product caching (30s TTL) - 95% cache hit rate expected
- Database batch inserts (size: 20)
- Lazy loading in Hibernate
- Indexed queries on frequently searched fields

---

## 🐳 Docker Compose Services

### Infrastructure Services
1. **PostgreSQL** - Data persistence
2. **Redis** - Distributed cache
3. **RabbitMQ** - Message broker (with management UI)
4. **Prometheus** - Metrics collection
5. **Grafana** - Metrics visualization

### Application Services
6. **Auth Service** - JWT generation & validation
7. **Product Service** - Catalog with caching
8. **Order Service** - Order processing with events
9. **User Service** - Profile management
10. **API Gateway** - Request routing

All services on `ecommerce` bridge network for inter-service communication.

---

## 🧪 Testing Strategy

### Unit Tests (Included)
- **AuthServiceTest**: Password hashing, user registration
- **ProductRepositoryTest**: Product CRUD operations
- **OrderServiceTest**: Mocked WebClient calls
- **UserServiceApplicationTest**: Context loading

### Test Coverage Goals
- Service layer: 80%+
- Repository layer: 90%+
- Controller layer: 60%+ (integration tests)
- Security: 70%+

### Integration Tests (Recommended)
```java
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {
    // Test full flow: Auth → Product → Order
}
```

### Performance Tests (Future)
```java
@BenchmarkMode(Mode.Throughput)
public class OrderCreationBenchmark {
    // Benchmark order creation under load
}
```

---

## 📋 API Endpoints Summary

### Auth Service (5 endpoints)
- `POST /api/auth/register` - Create new user
- `POST /api/auth/login` - Get JWT token
- `GET /actuator/health` - Health check
- `GET /actuator/prometheus` - Metrics
- `GET /actuator/info` - Service info

### Product Service (5 endpoints)
- `GET /api/products` - List all (cached)
- `GET /api/products/{id}` - Get one (cached)
- `POST /api/products` - Create (admin only)
- `PUT /api/products/{id}` - Update (admin only)
- `PUT /api/products/{id}/stock` - Update stock

### Order Service (3 endpoints)
- `POST /api/orders` - Create order (auth required)
- `GET /api/orders/{id}` - Get order
- `GET /api/orders/user/{userId}` - List user orders

### User Service (3 endpoints)
- `GET /api/users/{userId}` - Get profile (public)
- `POST /api/users` - Create profile
- `PUT /api/users/{userId}` - Update profile (auth required)

### API Gateway (4+ endpoints)
- All above routes prefixed with service name
- Example: `POST /auth/api/auth/login` → auth-service

---

## 🚀 Quick Start Commands

### Docker (Production-like)
```bash
cd E-Commerce
docker-compose up --build -d
# Services ready in ~30 seconds
```

### Local Dev (IntelliJ)
```bash
# Terminal 1: Infrastructure
docker-compose up postgres redis rabbitmq

# Terminal 2-6: One for each service
mvn spring-boot:run -f auth-service/pom.xml
mvn spring-boot:run -f product-service/pom.xml
# ... etc
```

### Build Only
```bash
mvn clean package -DskipTests
```

### Test Only
```bash
mvn clean test
```

---

## 📞 Support & Resources

### Internal Documentation
- `README.md` - Main documentation
- `docs/API.md` - Complete API reference
- `docs/DEPLOYMENT.md` - Deployment guide

### External Resources
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- RabbitMQ: https://www.rabbitmq.com/
- Redis: https://redis.io/
- Docker: https://docs.docker.com/

### Common Issues & Solutions

**Problem**: Ports already in use
```bash
lsof -i :8080 && kill -9 <PID>
```

**Problem**: Database won't start
```bash
docker-compose down && docker volume prune && docker-compose up
```

**Problem**: Services can't communicate
```bash
docker network inspect ecommerce
# Ensure all services on same network
```

---

## 🎯 Learning Path

### Beginner (1-2 days)
1. Read README.md
2. Run `docker-compose up`
3. Test API endpoints with curl/Postman
4. Explore database with pgAdmin

### Intermediate (3-5 days)
1. Review auth-service JWT implementation
2. Understand product caching with Redis
3. Study order service workflow
4. Test circuit breaker failures

### Advanced (1-2 weeks)
1. Implement distributed tracing (Jaeger)
2. Add rate limiting to API Gateway
3. Deploy to Kubernetes
4. Setup multi-instance scaling
5. Implement advanced caching strategies

---

## ✅ Checklist for Full Understanding

- [ ] Read main README.md
- [ ] Run entire stack with docker-compose
- [ ] Test all 5 services via API endpoints
- [ ] Check RabbitMQ messages in management UI
- [ ] View metrics in Prometheus
- [ ] Create dashboard in Grafana
- [ ] Review JWT token in jwt.io
- [ ] Examine Redis cache hits/misses
- [ ] Test circuit breaker by killing product-service
- [ ] Review source code of each service
- [ ] Run unit tests: `mvn test`
- [ ] Build Docker images locally
- [ ] Deploy to local Kubernetes (optional)

---

## 📝 Notes for Production Deployment

1. **Security**
   - Use strong JWT secret (>256 bits)
   - Implement rate limiting
   - Add request signing
   - Enable HTTPS/TLS
   - Use API keys for service-to-service

2. **Monitoring**
   - Setup alerting in Prometheus/Grafana
   - Implement distributed tracing
   - Monitor queue lengths in RabbitMQ
   - Track cache hit rates

3. **Resilience**
   - Implement retries with exponential backoff
   - Add dead letter exchanges in RabbitMQ
   - Implement saga pattern for transactions
   - Add circuit breaker fallbacks

4. **Scalability**
   - Database replication & failover
   - Redis cluster setup
   - Load balancing with sticky sessions
   - Horizontal pod autoscaling (K8s)

---

**Project Version:** 1.0.0
**Last Updated:** November 17, 2024
**Status:** Production-Ready (Educational)

