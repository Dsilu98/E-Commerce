# Complete Postman Setup & Testing Guide

## 📥 Import Instructions

### Step 1: Download Files
From the project directory `docs/`:
- `Postman_Collection.json` - API endpoints
- `Postman_Environment.json` - Variables & configuration

### Step 2: Import into Postman

#### Method A: From Files
1. Open Postman
2. Click **Import** (top-left corner)
3. Click **Upload Files**
4. Select `Postman_Collection.json`
5. Click **Import**
6. Repeat for `Postman_Environment.json`

#### Method B: From Folder
1. Postman → **File** → **Import**
2. Drag and drop the folder
3. Select both JSON files

### Step 3: Activate Environment
1. Top-right corner dropdown
2. Select **E-Commerce Environment**
3. You'll see environment variables populated

---

## 🎯 First Test Run (5 minutes)

### Step 1: Ensure Services Are Running
```bash
cd E:\SpringBootProjects\simple-ecommerce\E-Commerce
docker-compose up --build
# Wait for all services to start (~60 seconds)
```

### Step 2: Register User
1. **Collection** → **Authentication** → **Register User**
2. Click **Send**
3. Should see: `201 Created`
4. Response: `"User registered successfully"`

### Step 3: Login (Get Token)
1. **Collection** → **Authentication** → **Login User**
2. Click **Send**
3. Should see: `200 OK`
4. Response includes: `token` and `expiresIn`
5. **Token automatically saved** to `{{token}}` variable

### Step 4: Create Order
1. **Collection** → **Orders** → **Create Order**
2. Click **Send**
3. Should see: `201 Created`
4. Response includes: `id`, `status: "CONFIRMED"`, `totalAmount`

**Congratulations!** ✅ You've tested the complete flow!

---

## 📊 Detailed Endpoint Testing

### Authentication (Start Here)

#### Register User
```
POST /auth/api/auth/register
Body: {username, email, password}
Response: 201 Created
```
**Test:** Create different usernames each time (or use timestamp)

#### Login User ⭐
```
POST /auth/api/auth/login
Body: {username, password}
Response: 200 OK + {token, expiresIn}
```
**Important:** Token auto-saved to `{{token}}` variable

---

### Products

#### Get All (Cached)
```
GET /products/api/products
Auth: Not required
Cache: 30 seconds
Response: 200 OK + array of products
```
**Test:** Call twice within 30 seconds - see cache in headers

#### Get by ID (Cached)
```
GET /products/api/products/1
Auth: Not required
Response: 200 OK + single product
```

#### Create (Admin Only)
```
POST /products/api/products
Auth: Required (Bearer {{token}})
Body: {name, description, price, stock}
Response: 201 Created + product with ID
```
**Important:** User needs ROLE_ADMIN

#### Update (Admin Only)
```
PUT /products/api/products/{id}
Auth: Required (Bearer {{token}})
Body: Partial or complete product data
Response: 200 OK
```

#### Update Stock
```
PUT /products/api/products/{id}/stock?quantity=5
Auth: Required
Query Param: quantity (how much to deduct)
Response: 204 No Content
```

---

### Orders ⭐ (Most Important!)

#### Create Order (Complex Flow)
```
POST /orders/api/orders
Auth: Required (Bearer {{token}})
Body: {items: [{productId, quantity}, ...]}
Response: 201 Created + full order
```

**Behind the Scenes:**
1. ✅ Validates JWT token
2. ✅ Extracts userId from token
3. ✅ Calls product-service via WebClient (3x)
4. ✅ Validates stock for each item
5. ✅ Creates order in database
6. ✅ Updates stock in product-service
7. ✅ Publishes order.created event to RabbitMQ
8. ✅ Event consumed by listener

**Test:** 
- Simple order (1 item)
- Complex order (multiple items)
- Insufficient stock error

#### Get Order
```
GET /orders/api/orders/{id}
Auth: Required
Response: 200 OK + order details
```

#### Get User Orders
```
GET /orders/api/orders/user/{userId}
Auth: Required
Response: 200 OK + array of user's orders
```

---

### Users

#### Get Profile (Public)
```
GET /users/api/users/{userId}
Auth: Not required
Response: 200 OK + profile
```

#### Create Profile
```
POST /users/api/users
Auth: Not required
Body: {userId, email, address, phone}
Response: 201 Created
```

#### Update Profile
```
PUT /users/api/users/{userId}
Auth: Required (Bearer {{token}})
Body: {email, address, phone}
Response: 200 OK
```

---

## 🧪 Testing Scenarios

### Scenario 1: Happy Path (10 minutes)

1. **Register** → 201 Created
2. **Login** → 200 OK (token saved)
3. **Get Products** → 200 OK (array)
4. **Get Single Product** → 200 OK (object)
5. **Create Order** → 201 Created
6. **Get Order** → 200 OK
7. **View Profile** → 200 OK

### Scenario 2: Admin Features (15 minutes)

1. **Login** with admin user
2. **Create Product** → 201 Created (token saved)
3. **Get All Products** → See new product
4. **Update Product** → 200 OK
5. **Update Stock** → 204 No Content

### Scenario 3: Error Handling (10 minutes)

**Test 401 Unauthorized:**
- Remove Authorization header
- Or use invalid token
- Expected: 401 Unauthorized

**Test 404 Not Found:**
- Use non-existent ID
- Expected: 404 Not Found

**Test 400 Bad Request:**
- Create order with insufficient stock
- Expected: 400 Bad Request

**Test 403 Forbidden:**
- Try to create product without ROLE_ADMIN
- Expected: 403 Forbidden

### Scenario 4: Circuit Breaker (15 minutes)

1. **Ensure product-service running**
2. **Create Order** → 201 Created (works)
3. **Stop product-service:** `docker stop product-service`
4. **Try Create Order** → 503 Service Unavailable
5. **Start product-service:** `docker start product-service`
6. **Wait 10 seconds** (circuit breaker recovery)
7. **Create Order** → 201 Created (works again)

### Scenario 5: Cache Testing (10 minutes)

1. **Get All Products** (first call - fresh data)
   - Look at response headers: `Cache-Control`, `Date`
2. **Get All Products** (within 30 seconds - cached)
   - Response same as first (from cache)
3. **Create Product** (cache invalidates)
4. **Get All Products** (fresh data again)

---

## 🔑 Using Variables in Postman

### Automatic Variables (Auto-Populated)

**After Login:**
```
{{token}} → JWT token (24 hour expiration)
```

**After Create Product:**
```
{{productId}} → Product ID
```

**After Create Order:**
```
{{orderId}} → Order ID
```

### Manual Variables

Set in **Environment Settings:**
- `{{base_url}}` = http://localhost:8080
- `{{productId}}` = 1
- `{{orderId}}` = 1
- `{{userId}}` = 1

### How to Use in Requests

**In URL:**
```
GET {{base_url}}/products/api/products/{{productId}}
```

**In Headers:**
```
Authorization: Bearer {{token}}
```

**In Body:**
```json
{
  "productId": {{productId}},
  "quantity": 2
}
```

---

## 📈 Postman Test Scripts

Each request has automated tests that:
- ✅ Check response status code
- ✅ Validate response structure
- ✅ Save important data to variables
- ✅ Assert expected values

### View Test Results
1. Send any request
2. Look at **Test Results** tab
3. See which assertions passed/failed
4. Variables saved automatically

---

## 🐛 Troubleshooting

### Issue: "Connection refused"
```
Error: connect ECONNREFUSED 127.0.0.1:8080

Solution:
1. Verify services running: docker-compose ps
2. Check ports: docker port <service>
3. Restart: docker-compose restart api-gateway
```

### Issue: "401 Unauthorized"
```
Response: {"error": "Invalid token"}

Solution:
1. Re-run Login to get fresh token
2. Token might be expired (24 hours)
3. Check token in {{token}} variable
```

### Issue: "403 Forbidden"
```
Response: {"error": "Access denied"}

Solution:
1. User needs ROLE_ADMIN for create/update
2. Use different user
3. Check user roles in database
```

### Issue: "404 Not Found"
```
Response: {"error": "Product not found: 999"}

Solution:
1. Use correct ID
2. Create product first
3. Check available products: GET /products
```

### Issue: "Token variable empty"
```
Solution:
1. Run Login request first
2. Check Test Results tab for errors
3. Manually set: Environment → token = <paste-token>
```

### Issue: "Service unavailable (503)"
```
Response: Product service unavailable

Solution:
1. Check if product-service is running
2. This is circuit breaker protecting order-service
3. Wait 10 seconds and retry
4. Check logs: docker-compose logs order-service
```

---

## 🚀 Advanced Testing

### Run Full Collection

1. **Collection** → **▶ Run** (play icon)
2. **Select requests** to run
3. **Set delays** between requests
4. Click **Run** button
5. See results in **Collection Runner**

### Save Request Responses

1. Send request
2. Click **Save Response** button
3. Give it a name (e.g., "Order Created")
4. Later: Click dropdown under **Save Response**
5. Compare with new responses

### Export as Code

1. Send any request
2. Click **Code** link (top-right)
3. Choose language: bash, Python, Node.js, etc.
4. Copy code for use in scripts

---

## 📝 Postman Organization

### Collection Structure
```
Authentication
├── Register User
├── Login User
└── Health Check - Auth Service

Products
├── Get All Products
├── Get Product by ID
├── Create Product (Admin)
├── Update Product (Admin)
├── Update Stock
└── Health Check - Product Service

Orders
├── Create Order ⭐
├── Get Order
├── Get User Orders
└── Health Check - Order Service

Users
├── Get User Profile
├── Create User Profile
├── Update User Profile
└── Health Check - User Service

Monitoring
├── API Gateway Health
├── Auth Metrics
├── Product Metrics
└── Order Metrics

Testing Scenarios
└── Scenario 1: User Journey
    ├── Register
    ├── Login
    ├── Browse Products
    └── Create Order
```

---

## 🎯 Quick Test Checklist

### Before Testing
- [ ] Services running: `docker-compose ps`
- [ ] Postman installed
- [ ] Collection imported
- [ ] Environment selected

### Basic Testing
- [ ] Register user (201)
- [ ] Login user (200, token saved)
- [ ] Get products (200, cached)
- [ ] Create product (201, admin)
- [ ] Create order (201, complex)
- [ ] Get order (200)
- [ ] Update profile (200, auth)

### Error Testing
- [ ] Missing auth (401)
- [ ] Invalid token (401)
- [ ] Invalid ID (404)
- [ ] No role (403)
- [ ] Bad request (400)

### Advanced Testing
- [ ] Circuit breaker (stop service)
- [ ] Cache validation (30s TTL)
- [ ] RabbitMQ messages
- [ ] Prometheus metrics

---

## 📚 Additional Resources

### In Project
- `README.md` - Complete guide
- `docs/API.md` - API reference
- `docs/SETUP.md` - Environment setup
- `docs/test_api.sh` - Curl scripts
- `docs/test_api.bat` - Windows batch

### External
- **Postman Docs:** https://learning.postman.com/
- **REST API Best Practices:** https://restfulapi.net/
- **HTTP Status Codes:** https://httpwg.org/specs/rfc7231.html

---

## 🔗 Service URLs

| Component | URL |
|-----------|-----|
| API Gateway | http://localhost:8080 |
| Auth Service | http://localhost:8081 |
| Product Service | http://localhost:8082 |
| Order Service | http://localhost:8083 |
| User Service | http://localhost:8084 |
| RabbitMQ Admin | http://localhost:15672 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 |

---

## ✅ Success Indicators

✅ **All tests passing:**
- Status codes correct
- Response bodies valid
- Variables auto-saved
- No error messages

✅ **Services healthy:**
- All /actuator/health return UP
- No service down errors
- Logs show no errors

✅ **Features working:**
- Cache invalidation working
- JWT tokens valid
- Circuit breaker functional
- RabbitMQ events published

---

**Status:** ✅ Ready for Testing
**Version:** 1.0.0
**Last Updated:** November 17, 2024

---

