# Postman Collection Guide

## 📥 Import Collection

### Step 1: Download Collection
The Postman collection is located at:
```
docs/Postman_Collection.json
```

### Step 2: Import into Postman

#### Option A: Direct Import
1. Open Postman
2. Click **Import** button (top-left)
3. Select **Upload Files**
4. Choose `docs/Postman_Collection.json`
5. Click **Import**

#### Option B: From Link
1. Click **Import** in Postman
2. Select **Link** tab
3. Paste the file path or content

---

## 🔑 Setting Up Variables

### Automatic (Recommended)
The collection has **test scripts** that automatically set variables:
- `{{token}}` - Set after successful login
- `{{productId}}` - Set after creating a product
- `{{orderId}}` - Set after creating an order

### Manual Setup
If auto-setup doesn't work:

1. Click **Postman Environment** (top-right)
2. Create new environment: `E-Commerce`
3. Add variables:
   ```
   token     = (get from login response)
   productId = 1
   orderId   = 1
   ```

---

## 📋 API Endpoints by Category

### 1️⃣ Authentication
Start here! You must get a token for protected endpoints.

#### Register User
- **Method:** POST
- **URL:** `http://localhost:8080/auth/api/auth/register`
- **Body:**
  ```json
  {
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }
  ```
- **Response:** 201 Created

#### Login User ⭐ (Get Token)
- **Method:** POST
- **URL:** `http://localhost:8080/auth/api/auth/login`
- **Body:**
  ```json
  {
    "username": "john_doe",
    "password": "securePassword123"
  }
  ```
- **Response:** 200 OK
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 86400
  }
  ```
- **Important:** Token is auto-saved to `{{token}}` variable

---

### 2️⃣ Products
Manage product catalog with caching.

#### Get All Products
- **Method:** GET
- **URL:** `http://localhost:8080/products/api/products`
- **Auth:** Not required
- **Cache:** 30 seconds
- **Response:** Array of products

#### Get Product by ID
- **Method:** GET
- **URL:** `http://localhost:8080/products/api/products/1`
- **Auth:** Not required
- **Response:** Single product

#### Create Product (Admin Only) ⭐
- **Method:** POST
- **URL:** `http://localhost:8080/products/api/products`
- **Auth:** Bearer token (ROLE_ADMIN)
- **Body:**
  ```json
  {
    "name": "Mechanical Keyboard",
    "description": "RGB Mechanical Keyboard",
    "price": 149.99,
    "stock": 100
  }
  ```
- **Response:** 201 Created

#### Update Product (Admin Only)
- **Method:** PUT
- **URL:** `http://localhost:8080/products/api/products/3`
- **Auth:** Bearer token (ROLE_ADMIN)
- **Body:** Same as create

#### Update Stock (Internal Use)
- **Method:** PUT
- **URL:** `http://localhost:8080/products/api/products/1/stock?quantity=5`
- **Auth:** Bearer token
- **Query Param:** `quantity=5` (amount to deduct)

---

### 3️⃣ Orders ⭐ (Most Complex)
Create and manage orders.

#### Create Order (Most Important!) ⭐⭐⭐
- **Method:** POST
- **URL:** `http://localhost:8080/orders/api/orders`
- **Auth:** Required (Bearer token)
- **Body:**
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
- **Response:** 201 Created
  ```json
  {
    "id": 1,
    "userId": 123,
    "totalAmount": 2659.97,
    "status": "CONFIRMED",
    "items": [...],
    "createdAt": "2024-11-17T10:30:00"
  }
  ```

**Behind the Scenes:**
1. ✅ JWT token validated
2. ✅ userId extracted from token
3. ✅ Product details fetched via WebClient
4. ✅ Stock validated for each item
5. ✅ Order persisted to database
6. ✅ Stock updated in product-service
7. ✅ Event published to RabbitMQ
8. ✅ Event consumed by listener

#### Get Order
- **Method:** GET
- **URL:** `http://localhost:8080/orders/api/orders/1`
- **Auth:** Required

#### Get User Orders
- **Method:** GET
- **URL:** `http://localhost:8080/orders/api/orders/user/123`
- **Auth:** Required

---

### 4️⃣ Users
User profile management.

#### Get User Profile
- **Method:** GET
- **URL:** `http://localhost:8080/users/api/users/1`
- **Auth:** Not required (public)

#### Create User Profile
- **Method:** POST
- **URL:** `http://localhost:8080/users/api/users`
- **Auth:** Not required
- **Body:**
  ```json
  {
    "userId": 1,
    "email": "john@example.com",
    "address": "123 Main St",
    "phone": "555-1234"
  }
  ```

#### Update User Profile
- **Method:** PUT
- **URL:** `http://localhost:8080/users/api/users/1`
- **Auth:** Required (Bearer token)
- **Body:**
  ```json
  {
    "email": "john.updated@example.com",
    "address": "456 Oak Ave",
    "phone": "555-5678"
  }
  ```

---

### 5️⃣ Monitoring
Health checks and metrics.

#### Service Health Checks
- Auth Service: `http://localhost:8081/actuator/health`
- Product Service: `http://localhost:8082/actuator/health`
- Order Service: `http://localhost:8083/actuator/health`
- User Service: `http://localhost:8084/actuator/health`

#### Prometheus Metrics
- Auth Service: `http://localhost:8081/actuator/prometheus`
- Product Service: `http://localhost:8082/actuator/prometheus`
- Order Service: `http://localhost:8083/actuator/prometheus`

---

## 🧪 Complete Testing Flow

### Scenario 1: User Journey

**Time: ~2 minutes**

1. **Register User**
   - Use `Authentication → Register User`
   - Change username to be unique (use timestamp)

2. **Login**
   - Use `Authentication → Login User`
   - Token auto-saved to `{{token}}`

3. **Browse Products**
   - Use `Products → Get All Products`
   - Check Redis cache (refresh = cached for 30s)

4. **Get Product Details**
   - Use `Products → Get Product by ID`

5. **Create Order**
   - Use `Orders → Create Order`
   - Uses products from step 3

6. **Check Order**
   - Use `Orders → Get Order by ID`
   - Order ID auto-saved from step 5

7. **View User Orders**
   - Use `Orders → Get User Orders`

---

### Scenario 2: Admin Products

**Prerequisites:** User with ROLE_ADMIN

1. **Create Product**
   - Use `Products → Create Product (Admin Only)`
   - Requires token with ROLE_ADMIN

2. **Update Product**
   - Use `Products → Update Product (Admin Only)`
   - Invalidates cache

3. **Verify Cache Invalidation**
   - Get all products twice (should differ)

---

### Scenario 3: Error Testing

**Test error handling:**

1. **Insufficient Stock**
   - Try creating order with quantity > available stock
   - Expected: 400 Bad Request

2. **Invalid JWT**
   - Modify token in header
   - Expected: 401 Unauthorized

3. **Non-existent Product**
   - Use productId that doesn't exist
   - Expected: 404 Not Found

4. **Product Service Down** (Circuit Breaker)
   - Stop product-service: `docker stop product-service`
   - Try to create order
   - Should return: 503 Service Unavailable
   - Resume: `docker start product-service`

---

## 📊 Quick Test Checklist

### Authentication
- [ ] Register new user
- [ ] Login and get token
- [ ] Use token for protected endpoints
- [ ] Test expired/invalid token

### Products
- [ ] Get all products (check cache header)
- [ ] Get single product
- [ ] Create product as admin
- [ ] Update product as admin
- [ ] Try create without admin role (should fail)

### Orders
- [ ] Create simple order (1 item)
- [ ] Create complex order (multiple items)
- [ ] Get order details
- [ ] List user orders
- [ ] Check RabbitMQ for order.created event

### Users
- [ ] Get user profile (public)
- [ ] Create user profile
- [ ] Update user profile (requires auth)
- [ ] Try update without auth (should fail)

### Monitoring
- [ ] Check all service health endpoints
- [ ] View Prometheus metrics
- [ ] Monitor RabbitMQ (localhost:15672)
- [ ] Check Grafana (localhost:3000)

---

## 🔐 Using Bearer Tokens

### In Postman Header

1. Open any request requiring auth
2. Go to **Headers** tab
3. Find or add: `Authorization: Bearer {{token}}`
4. Token auto-filled from environment

### Manual Token Entry

If auto-complete doesn't work:
1. Execute login request
2. Copy token from response
3. In request header: `Authorization: Bearer <paste-token>`

---

## 🐛 Troubleshooting

### Issue: "Collection not found"
**Solution:** Ensure `docs/Postman_Collection.json` exists

### Issue: Token variable empty
**Solution:** 
1. Run Login request first
2. Check test output for errors
3. Manually set in environment

### Issue: 401 Unauthorized
**Solution:**
1. Check token is not expired (24 hours)
2. Verify token is in Authorization header
3. Login again

### Issue: 403 Forbidden
**Solution:**
1. Check user has required role (ROLE_ADMIN)
2. Create new admin user
3. Or use public endpoints

### Issue: RabbitMQ events not visible
**Solution:**
1. Check RabbitMQ running: `docker-compose ps rabbitmq`
2. Access UI: http://localhost:15672
3. Check queue: `order.created`

### Issue: Cache not working
**Solution:**
1. Redis running: `docker-compose ps redis`
2. Get endpoint twice - second should be cached
3. Check response headers for cache info

---

## 🚀 Advanced Testing

### Test with Postman Runner

1. Select Collection → **Run**
2. Choose which requests to run
3. Set delays between requests
4. View results

### Test with Postman CLI

```bash
# Install newman
npm install -g newman

# Run collection
newman run docs/Postman_Collection.json \
  -e environment.json \
  --bail
```

### Generate Report

```bash
newman run docs/Postman_Collection.json \
  --reporters cli,html \
  --reporter-html-export report.html
```

---

## 📝 Notes

- **Token Expiration:** 24 hours - login again if expired
- **Cache TTL:** 30 seconds for products
- **RabbitMQ:** Check management UI for events
- **Prometheus:** Metrics available at `/actuator/prometheus`
- **Grafana:** Pre-configured at `localhost:3000`

---

## 🎯 Next Steps

1. **Import collection** into Postman
2. **Start all services**: `docker-compose up --build`
3. **Run authentication** flow first
4. **Test all endpoints** in order
5. **Monitor** using Prometheus/Grafana

---

**Happy Testing! 🎉**

For more details, see:
- `README.md` - Complete project guide
- `docs/API.md` - API reference
- `docs/DEPLOYMENT.md` - Production setup

