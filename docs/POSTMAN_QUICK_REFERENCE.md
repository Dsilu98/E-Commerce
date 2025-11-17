# Postman Quick Reference Card

## 📥 Setup (5 minutes)

### 1. Import Collection & Environment
```
File → Import
├─ docs/Postman_Collection.json    (Collection)
└─ docs/Postman_Environment.json   (Environment)
```

### 2. Set Active Environment
- Top right → Select "E-Commerce Environment"

### 3. Start Services
```bash
cd E:\SpringBootProjects\simple-ecommerce\E-Commerce
docker-compose up --build
```

---

## 🔑 Quick Test Flow (Copy & Paste)

### 1. Register
```
POST http://localhost:8080/auth/api/auth/register

Body (JSON):
{
  "username": "testuser1",
  "email": "test1@example.com",
  "password": "Test@123"
}
```

### 2. Login ⭐ (Get Token)
```
POST http://localhost:8080/auth/api/auth/login

Body (JSON):
{
  "username": "testuser1",
  "password": "Test@123"
}

Response: Copy the "token" value to {{token}}
```

### 3. Get Products
```
GET http://localhost:8080/products/api/products

Headers: None needed
```

### 4. Create Order ⭐
```
POST http://localhost:8080/orders/api/orders

Headers:
Authorization: Bearer {{token}}

Body (JSON):
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

---

## 📋 All Endpoints at a Glance

### Authentication (No Auth Required)
```
POST   /auth/api/auth/register      Body: {username, email, password}
POST   /auth/api/auth/login         Body: {username, password}
```

### Products (No Auth Required to View)
```
GET    /products/api/products                    (Cached 30s)
GET    /products/api/products/{id}               (Cached 30s)
POST   /products/api/products      Auth Required  Body: {name, description, price, stock}
PUT    /products/api/products/{id} Auth Required  Body: {name, description, price, stock}
PUT    /products/api/products/{id}/stock?quantity=5  Internal Use
```

### Orders (Auth Required)
```
POST   /orders/api/orders                        Body: {items: [{productId, quantity}]}
GET    /orders/api/orders/{id}                   
GET    /orders/api/orders/user/{userId}
```

### Users
```
GET    /users/api/users/{userId}                 No Auth
POST   /users/api/users                          Body: {userId, email, address, phone}
PUT    /users/api/users/{userId}    Auth Required Body: {email, address, phone}
```

### Health & Metrics (No Auth Required)
```
GET    /actuator/health                          (All services)
GET    /actuator/prometheus                      (All services)
```

---

## 🎯 Common Tasks

### Get JWT Token
1. Click: **Authentication → Login User**
2. Send request
3. Token auto-saved to `{{token}}`

### Create Product
1. Need: Valid JWT token (from login)
2. Need: ROLE_ADMIN
3. Click: **Products → Create Product**
4. Update product ID in environment if needed

### Create Order
1. Need: Valid JWT token
2. Click: **Orders → Create Order**
3. Body uses productId from database

### Test Error Cases
- Wrong password → 401 Unauthorized
- Non-existent product → 404 Not Found
- No auth header → 401 Unauthorized
- Service down → 503 Service Unavailable

---

## 🔍 Debugging Tips

### View Response Headers
- Click **Headers** tab under Response
- Look for: `Content-Type`, `Cache-Control`, `Date`

### View Full Response
- Click **Response Body** dropdown
- Select **Pretty**, **Raw**, **Preview**

### Check Variable Values
- Bottom left → **Manage Environments**
- Select "E-Commerce Environment"
- View all variable values

### View Request Details
- Click on request → **Code** (top right)
- See generated code for curl/other languages

### Test Scripts Output
- Click **Test Results** tab
- See which assertions passed/failed
- View console logs if any

---

## 🚀 Common Requests

### Register & Login (One-Click Setup)
```bash
# Use Testing Scenarios → Scenario 1
Runs all 4 steps automatically
```

### Create Full Order (With Stock)
```
1. Get Products (list all)
2. Note a product ID
3. Create Order with that ID
4. Check RabbitMQ (localhost:15672)
```

### Test Circuit Breaker
```
1. Stop product-service: docker stop product-service
2. Try to create order
3. See 503 error (circuit breaker active)
4. Resume service: docker start product-service
5. Try again (should work)
```

### Monitor Cache
```
1. Get All Products (first time - fresh)
2. Get All Products (second time - cached)
3. Create Product (cache invalidates)
4. Get All Products (fresh again)
```

---

## 📊 Testing Matrix

| Feature | Method | Auth | Notes |
|---------|--------|------|-------|
| Register | POST | ❌ | Public |
| Login | POST | ❌ | Returns token |
| Get Products | GET | ❌ | Cached 30s |
| Create Product | POST | ✅ | ROLE_ADMIN |
| Create Order | POST | ✅ | Complex flow |
| Update Profile | PUT | ✅ | JWT required |

---

## 🔐 Token Management

### Get Token
```
POST /auth/api/auth/login
Response: {token: "...", expiresIn: 86400}
```

### Auto-Save Token
- Test scripts auto-save to `{{token}}`
- Or manually: Environment → Set `token` value

### Use Token
```
Headers:
Authorization: Bearer {{token}}
```

### Token Expiration
- Valid for: 24 hours
- When expired: Re-login to get new token

---

## 📱 Environment Variables

| Variable | Value | Purpose |
|----------|-------|---------|
| `{{token}}` | JWT from login | Authentication |
| `{{base_url}}` | http://localhost:8080 | API Gateway |
| `{{productId}}` | 1 | Test product |
| `{{orderId}}` | 1 | Test order |
| `{{userId}}` | 1 | Test user |

### Set Variables Manually
1. Top right → Environments
2. Select "E-Commerce Environment"
3. Edit values as needed
4. Save

---

## 🎬 Test Execution Steps

### Quick Test (5 minutes)
1. Login → Get token
2. Get products
3. Create order
4. View order

### Full Test (15 minutes)
1. Register user
2. Login
3. Browse products
4. Create product (admin)
5. Create order
6. Update profile
7. View metrics

### Integration Test (30 minutes)
1. Complete scenario above
2. Test error cases
3. Monitor RabbitMQ
4. Check Prometheus
5. Review logs

---

## 🐳 Docker Verification

```bash
# Check all services running
docker-compose ps

# View logs
docker-compose logs -f

# Stop specific service
docker-compose stop order-service

# Resume service
docker-compose start order-service

# Check specific service logs
docker-compose logs order-service --tail=50
```

---

## 📡 Service Ports

| Service | Port | Direct URL |
|---------|------|------------|
| API Gateway | 8080 | http://localhost:8080 |
| Auth | 8081 | http://localhost:8081 |
| Product | 8082 | http://localhost:8082 |
| Order | 8083 | http://localhost:8083 |
| User | 8084 | http://localhost:8084 |
| RabbitMQ Admin | 15672 | http://localhost:15672 |
| Prometheus | 9090 | http://localhost:9090 |
| Grafana | 3000 | http://localhost:3000 |

---

## ✅ Validation Checklist

After each request, verify:
- [ ] Status code is correct (2xx for success)
- [ ] Response body contains expected fields
- [ ] Headers include `Content-Type: application/json`
- [ ] No error messages in response
- [ ] Variables updated correctly (if applicable)

---

## 🆘 Troubleshooting

### "Cannot connect to localhost:8080"
```bash
# Services not running
docker-compose up --build
```

### "Authorization failed"
```bash
# Token invalid or expired
# Re-run login to get new token
POST /auth/api/auth/login
```

### "Product not found (404)"
```bash
# Product doesn't exist
# Create product first or use correct ID
```

### "Insufficient stock"
```bash
# Order quantity > available stock
# Check product stock: GET /products/{id}
# Reduce quantity or create new product
```

### "Circuit breaker open"
```bash
# Product service down
# Check: docker-compose ps product-service
# Restart: docker-compose restart product-service
```

---

## 📚 Additional Resources

- **Full API Docs:** `docs/API.md`
- **Setup Guide:** `docs/SETUP.md`
- **Deployment:** `docs/DEPLOYMENT.md`
- **README:** `README.md`
- **Postman Guide:** `docs/POSTMAN_GUIDE.md`

---

## 🎯 Quick Links

- **Postman Collection:** `docs/Postman_Collection.json`
- **Environment File:** `docs/Postman_Environment.json`
- **RabbitMQ UI:** http://localhost:15672 (guest/guest)
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3000 (admin/admin)

---

**Version:** 1.0.0
**Last Updated:** November 17, 2024
**Status:** ✅ Ready for Testing

---

