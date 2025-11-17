# 📋 Postman Testing Files Index

## 🎯 What You Need

### To Import into Postman
1. **`Postman_Collection.json`** - All API endpoints (18+)
2. **`Postman_Environment.json`** - Variables & configuration

### To Learn How to Use
Choose based on your needs:
- **Quick Start:** `POSTMAN_QUICK_REFERENCE.md` (2 minutes)
- **Step-by-Step:** `POSTMAN_GUIDE.md` (10 minutes)
- **Complete Guide:** `COMPLETE_POSTMAN_GUIDE.md` (30 minutes)
- **Summary:** `POSTMAN_COMPLETE_SUMMARY.md` (5 minutes)

### To Test via Command Line
- **Linux/macOS:** `test_api.sh` (bash script)
- **Windows:** `test_api.bat` (batch script)

---

## 📂 File List & Descriptions

### Postman Files (Import These)

#### 1. **Postman_Collection.json** ⭐ REQUIRED
- **Type:** Postman Collection
- **Size:** ~50KB
- **Contains:** 18+ API endpoints
- **Action:** Import into Postman
- **Features:**
  - Auto-saving test scripts
  - Variable management
  - Mock responses
  - Complete documentation

#### 2. **Postman_Environment.json** ⭐ REQUIRED
- **Type:** Postman Environment
- **Size:** ~2KB
- **Contains:** Variables & URLs
- **Action:** Import into Postman
- **Features:**
  - Base URLs for all services
  - Auto-saving variables (token, IDs)
  - Service ports
  - Infrastructure URLs

---

### Documentation Files (Read These)

#### 3. **POSTMAN_QUICK_REFERENCE.md** ✅ START HERE
- **Time to Read:** 2 minutes
- **Purpose:** Quick reference card
- **Contains:**
  - Setup in 5 minutes
  - All endpoints at a glance
  - Quick test flow
  - Common tasks
  - Troubleshooting table

**Best for:** Users who want to start immediately

#### 4. **POSTMAN_GUIDE.md**
- **Time to Read:** 10 minutes
- **Purpose:** Complete setup guide
- **Contains:**
  - Import instructions (3 methods)
  - Variable setup
  - All endpoints with examples
  - Testing scenarios
  - Error handling
  - Troubleshooting

**Best for:** Users who like detailed guidance

#### 5. **COMPLETE_POSTMAN_GUIDE.md**
- **Time to Read:** 30 minutes
- **Purpose:** Comprehensive testing guide
- **Contains:**
  - Detailed endpoint documentation
  - 5 complete testing scenarios
  - Variable management deep dive
  - Advanced testing techniques
  - Full troubleshooting guide
  - Code examples

**Best for:** Advanced users & learners

#### 6. **POSTMAN_COMPLETE_SUMMARY.md**
- **Time to Read:** 5 minutes
- **Purpose:** Overview of what was created
- **Contains:**
  - What was generated
  - Quick start
  - All endpoints list
  - Key features
  - Testing scenarios
  - Usage examples

**Best for:** Users who want overview first

---

### Script Files (Run These)

#### 7. **test_api.sh** (Bash Script)
- **OS:** Linux & macOS
- **Requirements:** curl, jq (optional)
- **What it does:** Complete API testing flow
- **Usage:** `bash test_api.sh`
- **Output:** Formatted JSON responses
- **Auto-saves:** Token, IDs

**Best for:** Command-line users on Linux/macOS

#### 8. **test_api.bat** (Batch Script)
- **OS:** Windows
- **Requirements:** curl, Windows CMD
- **What it does:** Complete API testing flow
- **Usage:** Double-click or `test_api.bat`
- **Output:** Raw curl responses
- **Auto-saves:** Prompts for values

**Best for:** Command-line users on Windows

---

## 🚀 Getting Started (3 Steps)

### Step 1: Import Collections
```
Open Postman
↓
Click Import (top-left)
↓
Select Postman_Collection.json
↓
Click Import
↓
Repeat with Postman_Environment.json
```

### Step 2: Select Environment
```
Top-right dropdown
↓
Select "E-Commerce Environment"
↓
Done! All variables ready
```

### Step 3: Start Testing
```
Click any endpoint
↓
Click Send
↓
See response in body
```

---

## 📋 Which File to Read?

### I want to start IMMEDIATELY
→ Read: **POSTMAN_QUICK_REFERENCE.md** (2 min)
→ Then import collections and test

### I want STEP-BY-STEP instructions
→ Read: **POSTMAN_GUIDE.md** (10 min)
→ Follow each section carefully

### I want to LEARN everything
→ Read: **COMPLETE_POSTMAN_GUIDE.md** (30 min)
→ Work through all scenarios

### I want an OVERVIEW first
→ Read: **POSTMAN_COMPLETE_SUMMARY.md** (5 min)
→ Then choose detailed guide

### I prefer COMMAND LINE
→ Run: **test_api.sh** (Linux/macOS)
→ Or: **test_api.bat** (Windows)

---

## 📊 Endpoint Categories

### By Authentication
- **No Auth:** Products (list), Users (get), Health
- **Auth Required:** Orders, Profile updates
- **Admin Only:** Product create/update

### By Method
- **GET:** Get products, get orders, get profiles, health
- **POST:** Register, login, create product, create order
- **PUT:** Update product, update profile, update stock

### By Service
- **Auth Service:** Register, login
- **Product Service:** All product endpoints
- **Order Service:** All order endpoints
- **User Service:** All user endpoints
- **All Services:** Health & metrics

---

## 🎯 Quick Test Paths

### Path 1: 5-Minute Test
```
1. Register (POST)
2. Login (POST) → get token
3. Get Products (GET)
4. Create Order (POST)
5. Done!
```
**Files:** Use Postman collection directly

### Path 2: 15-Minute Test
```
1. Register & Login
2. Browse Products
3. Create Product (admin)
4. Create Order
5. Update Profile
6. View Metrics
```
**Files:** POSTMAN_QUICK_REFERENCE.md

### Path 3: 30-Minute Test
```
All 18+ endpoints
All error scenarios
Circuit breaker test
Cache validation
RabbitMQ check
```
**Files:** COMPLETE_POSTMAN_GUIDE.md

### Path 4: Command Line
```
Run test_api.sh or test_api.bat
Get token automatically
Test all endpoints
See formatted output
```
**Files:** test_api.sh or test_api.bat

---

## 🔍 Finding Specific Endpoints

### Authentication
- Register: **POSTMAN_GUIDE.md** → Section 2
- Login: **POSTMAN_GUIDE.md** → Section 2

### Products
- Get All: **POSTMAN_GUIDE.md** → Section 3
- Get One: **POSTMAN_GUIDE.md** → Section 3
- Create: **POSTMAN_GUIDE.md** → Section 3 (admin)
- Update: **POSTMAN_GUIDE.md** → Section 3 (admin)

### Orders
- Create: **POSTMAN_GUIDE.md** → Section 4
- Get: **POSTMAN_GUIDE.md** → Section 4
- List: **POSTMAN_GUIDE.md** → Section 4

### Users
- Get: **POSTMAN_GUIDE.md** → Section 5
- Create: **POSTMAN_GUIDE.md** → Section 5
- Update: **POSTMAN_GUIDE.md** → Section 5

### Testing Scenarios
- 5 complete flows: **COMPLETE_POSTMAN_GUIDE.md** → Scenarios section

---

## 🐛 Troubleshooting

### Can't connect to localhost:8080
→ **POSTMAN_GUIDE.md** → Troubleshooting section

### Authorization failed (401)
→ **POSTMAN_QUICK_REFERENCE.md** → Token Management

### Product not found (404)
→ **POSTMAN_GUIDE.md** → Error Testing

### Service unavailable (503)
→ **COMPLETE_POSTMAN_GUIDE.md** → Circuit Breaker section

### Token not saving
→ **COMPLETE_POSTMAN_GUIDE.md** → Variable Management

---

## 📱 Sample Endpoints

### Register & Login
```bash
# Get token (will use in other requests)
POST /auth/api/auth/login
Body: {username, password}
Response: {token, expiresIn}
```

### Create Order (Most Important!)
```bash
# Requires token from login
POST /orders/api/orders
Headers: Authorization: Bearer {{token}}
Body: {items: [{productId, quantity}]}
Response: Full order with ID
```

### Get Cached Products
```bash
# No auth required
# Cached for 30 seconds
GET /products/api/products
Response: Array of products
```

---

## ✅ Validation

### Quick Validation (1 minute)
- [ ] Collections imported
- [ ] Environment selected
- [ ] Services running

### Full Validation (15 minutes)
- [ ] All endpoints responding
- [ ] Token saving correctly
- [ ] Responses are valid JSON
- [ ] Status codes correct

---

## 📞 Need Help?

### Import Problems
→ **POSTMAN_GUIDE.md** → Import Instructions

### Testing Problems
→ **POSTMAN_QUICK_REFERENCE.md** → Troubleshooting

### Want to Learn More
→ **COMPLETE_POSTMAN_GUIDE.md** → Full Guide

### Command Line Preference
→ **test_api.sh** or **test_api.bat**

---

## 🎉 You're Ready!

1. ✅ Import the collections
2. ✅ Read a guide (choose from above)
3. ✅ Start testing
4. ✅ Have fun!

**All files are in:** `E:\SpringBootProjects\simple-ecommerce\E-Commerce\docs\`

---

**Status:** ✅ Complete Postman Setup Ready
**Generated:** November 17, 2024
**Total Files:** 8 (2 Postman + 4 Guides + 2 Scripts)

Start with: **Postman_Collection.json** + **POSTMAN_QUICK_REFERENCE.md**

