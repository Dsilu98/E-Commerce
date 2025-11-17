# API Documentation

## Base URL

### Docker Compose
```
http://localhost:8080  (API Gateway)
```

### Local Development
```
http://localhost:8080  (API Gateway)
```

All service endpoints are routed through the API Gateway.

---

## Authentication Service

### Register User

**Endpoint:** `POST /auth/api/auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response:** 201 Created
```json
{
  "message": "User registered successfully"
}
```

**Error Response:** 400 Bad Request
```json
{
  "error": "Username already exists"
}
```

---

### Login

**Endpoint:** `POST /auth/api/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response:** 200 OK
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTcwMDI0NTAwMCwiZXhwIjoxNzAwMzMxNDAwfQ.abc123...",
  "expiresIn": 86400
}
```

**Error Response:** 401 Unauthorized
```json
{
  "error": "Invalid username or password"
}
```

---

## Product Service

### Get All Products

**Endpoint:** `GET /products/api/products`

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "stock": 50
  },
  {
    "id": 2,
    "name": "Mouse",
    "description": "Wireless mouse",
    "price": 29.99,
    "stock": 200
  }
]
```

**Cache:** Cached for 30 seconds

---

### Get Product by ID

**Endpoint:** `GET /products/api/products/{id}`

**Example:** `GET /products/api/products/1`

**Response:** 200 OK
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99,
  "stock": 50
}
```

**Error Response:** 404 Not Found
```json
{
  "error": "Product not found: 1"
}
```

**Cache:** Cached for 30 seconds

---

### Create Product

**Endpoint:** `POST /products/api/products`

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "price": 149.99,
  "stock": 100
}
```

**Response:** 201 Created
```json
{
  "id": 3,
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "price": 149.99,
  "stock": 100
}
```

**Authorization:** Requires `ROLE_ADMIN`

**Error Response:** 403 Forbidden
```json
{
  "error": "Access denied. Admin role required."
}
```

---

### Update Product

**Endpoint:** `PUT /products/api/products/{id}`

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Updated Keyboard",
  "description": "Updated description",
  "price": 159.99,
  "stock": 80
}
```

**Response:** 200 OK
```json
{
  "id": 3,
  "name": "Updated Keyboard",
  "description": "Updated description",
  "price": 159.99,
  "stock": 80
}
```

**Authorization:** Requires `ROLE_ADMIN`

---

### Update Stock

**Endpoint:** `PUT /products/api/products/{id}/stock?quantity={quantity}`

**Example:** `PUT /products/api/products/1/stock?quantity=10`

**Response:** 204 No Content

**Description:** Deducts the specified quantity from product stock

---

## Order Service

### Create Order

**Endpoint:** `POST /orders/api/orders`

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "userId": 123,
  "totalAmount": 2659.97,
  "status": "CONFIRMED",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ],
  "createdAt": "2024-11-17T10:30:00"
}
```

**Authorization:** Requires valid JWT token

**Error Responses:**

404 Not Found - Product not found
```json
{
  "error": "Product not found: 999"
}
```

400 Bad Request - Insufficient stock
```json
{
  "error": "Insufficient stock for product: 1"
}
```

503 Service Unavailable - Product service down (circuit breaker)
```json
{
  "error": "Product service unavailable. Unable to update stock."
}
```

**Async Processing:**
- Order is persisted immediately
- Stock is updated asynchronously
- `order.created` event published to RabbitMQ
- Resilience4j circuit breaker protects product-service calls

---

### Get Order

**Endpoint:** `GET /orders/api/orders/{id}`

**Response:** 200 OK
```json
{
  "id": 1,
  "userId": 123,
  "totalAmount": 2659.97,
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

---

### Get User Orders

**Endpoint:** `GET /orders/api/orders/user/{userId}`

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "userId": 123,
    "totalAmount": 2659.97,
    "status": "CONFIRMED",
    "items": [...],
    "createdAt": "2024-11-17T10:30:00"
  }
]
```

---

## User Service

### Get User Profile

**Endpoint:** `GET /users/api/users/{userId}`

**Response:** 200 OK
```json
{
  "id": 1,
  "userId": 123,
  "email": "john@example.com",
  "address": "123 Main St",
  "phone": "555-1234"
}
```

**Authorization:** Public access

---

### Create User Profile

**Endpoint:** `POST /users/api/users`

**Request Body:**
```json
{
  "userId": 123,
  "email": "john@example.com",
  "address": "123 Main St",
  "phone": "555-1234"
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "userId": 123,
  "email": "john@example.com",
  "address": "123 Main St",
  "phone": "555-1234"
}
```

---

### Update User Profile

**Endpoint:** `PUT /users/api/users/{userId}`

**Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john_updated@example.com",
  "address": "456 Oak Ave",
  "phone": "555-5678"
}
```

**Response:** 200 OK
```json
{
  "id": 1,
  "userId": 123,
  "email": "john_updated@example.com",
  "address": "456 Oak Ave",
  "phone": "555-5678"
}
```

**Authorization:** JWT token required

---

## Health & Monitoring

### Health Check

**Endpoint:** `GET /health` (via API Gateway)

**Response:** 200 OK
```json
{
  "status": "UP"
}
```

### Prometheus Metrics

**Direct Endpoints:**
- Auth Service: `http://localhost:8081/actuator/prometheus`
- Product Service: `http://localhost:8082/actuator/prometheus`
- Order Service: `http://localhost:8083/actuator/prometheus`
- API Gateway: `http://localhost:8080/actuator/prometheus`

**Access via Prometheus:** http://localhost:9090

**Sample Metrics:**
```
http_requests_total
jvm_memory_used_bytes
jvm_gc_memory_allocated_bytes_total
logback_events_total
spring_data_repository_invocations_total
```

---

## Error Handling

### Common HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |
| 503 | Service Unavailable |

### Error Response Format

```json
{
  "error": "Error message",
  "timestamp": "2024-11-17T10:30:00",
  "status": 400
}
```

---

## Rate Limiting & Throttling

Currently **not implemented**. Recommended for production:
- Use Spring Cloud Gateway rate limiter
- Implement token bucket algorithm
- Per-user or per-IP limits

---

## JWT Token Format

All tokens are signed with **HS256** and include:
- `sub` (subject): username
- `iat` (issued at): timestamp
- `exp` (expiration): 86400 seconds (24 hours)

**Example Decoded JWT:**
```json
{
  "sub": "john_doe",
  "iat": 1700245000,
  "exp": 1700331400
}
```

---

## Pagination & Filtering

Currently **not implemented**. Endpoints return all results. Recommended for production:
- Add `page`, `size`, `sort` query parameters
- Implement Spring Data Web support
- Return paginated responses with metadata

---

## Versioning

Current API version: **v1** (implicit)

For future versions:
- Use `/api/v2/...` paths
- Support multiple versions simultaneously
- Deprecate old versions with notice period

---

## Rate Limiting Examples

```bash
# Register user
curl -X POST http://localhost:8080/auth/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","email":"user1@test.com","password":"pass123"}'

# Login
curl -X POST http://localhost:8080/auth/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'

# Create product (with JWT)
TOKEN="your_jwt_token_here"
curl -X POST http://localhost:8080/products/api/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","description":"Desc","price":99.99,"stock":10}'

# Create order (with JWT)
curl -X POST http://localhost:8080/orders/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"productId":1,"quantity":2}]}'
```

---

**Last Updated:** November 17, 2024

