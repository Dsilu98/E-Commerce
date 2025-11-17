#!/bin/bash
# Curl Commands for E-Commerce Microservices API Testing
# Usage: Copy & paste commands into terminal
# Save as: test_api.sh and run: bash test_api.sh

echo "=========================================="
echo "E-Commerce API Testing with curl"
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Base URL
BASE_URL="http://localhost:8080"
SLEEP_TIME=1

# Function to print section headers
print_section() {
    echo -e "\n${BLUE}========== $1 ==========${NC}\n"
}

# Function to pretty print JSON
pretty_json() {
    echo "$1" | jq '.' 2>/dev/null || echo "$1"
}

# ============================================
# 1. AUTHENTICATION
# ============================================

print_section "1. AUTHENTICATION - Register User"

REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser_'$(date +%s)'",
    "email": "testuser_'$(date +%s)'@example.com",
    "password": "TestPassword123"
  }')

echo "Register Response:"
pretty_json "$REGISTER_RESPONSE"
sleep $SLEEP_TIME

# ============================================
# Login and get token
# ============================================

print_section "2. AUTHENTICATION - Login User"

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser_'$(date +%s)'",
    "password": "TestPassword123"
  }')

echo "Login Response:"
pretty_json "$LOGIN_RESPONSE"

# Extract token (using jq if available)
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null || echo "TOKEN_NOT_EXTRACTED")

if [ "$TOKEN" != "TOKEN_NOT_EXTRACTED" ]; then
    echo -e "\n${GREEN}✓ Token extracted: ${TOKEN:0:50}...${NC}"
else
    echo -e "\n${YELLOW}⚠ Could not extract token. Please copy manually.${NC}"
    TOKEN="YOUR_TOKEN_HERE"
fi

sleep $SLEEP_TIME

# ============================================
# 3. PRODUCTS - Get All
# ============================================

print_section "3. PRODUCTS - Get All Products (Cached)"

GET_PRODUCTS=$(curl -s -X GET "$BASE_URL/products/api/products")
echo "All Products:"
pretty_json "$GET_PRODUCTS"
sleep $SLEEP_TIME

# ============================================
# 4. PRODUCTS - Get Single
# ============================================

print_section "4. PRODUCTS - Get Product by ID"

GET_PRODUCT=$(curl -s -X GET "$BASE_URL/products/api/products/1")
echo "Product with ID 1:"
pretty_json "$GET_PRODUCT"
sleep $SLEEP_TIME

# ============================================
# 5. PRODUCTS - Create (requires admin)
# ============================================

print_section "5. PRODUCTS - Create Product (Admin Only)"

CREATE_PRODUCT=$(curl -s -X POST "$BASE_URL/products/api/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Keyboard",
    "description": "Mechanical keyboard with RGB",
    "price": 149.99,
    "stock": 100
  }')

echo "Create Product Response:"
pretty_json "$CREATE_PRODUCT"

PRODUCT_ID=$(echo "$CREATE_PRODUCT" | jq -r '.id' 2>/dev/null || echo "1")
sleep $SLEEP_TIME

# ============================================
# 6. PRODUCTS - Update
# ============================================

print_section "6. PRODUCTS - Update Product (Admin Only)"

UPDATE_PRODUCT=$(curl -s -X PUT "$BASE_URL/products/api/products/$PRODUCT_ID" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Updated Keyboard",
    "description": "Updated description",
    "price": 159.99,
    "stock": 80
  }')

echo "Update Product Response:"
pretty_json "$UPDATE_PRODUCT"
sleep $SLEEP_TIME

# ============================================
# 7. ORDERS - Create Order (Most Complex!)
# ============================================

print_section "7. ORDERS - Create Order ⭐"

CREATE_ORDER=$(curl -s -X POST "$BASE_URL/orders/api/orders" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }')

echo "Create Order Response:"
pretty_json "$CREATE_ORDER"

ORDER_ID=$(echo "$CREATE_ORDER" | jq -r '.id' 2>/dev/null || echo "1")
sleep $SLEEP_TIME

# ============================================
# 8. ORDERS - Get Order
# ============================================

print_section "8. ORDERS - Get Order by ID"

GET_ORDER=$(curl -s -X GET "$BASE_URL/orders/api/orders/$ORDER_ID" \
  -H "Authorization: Bearer $TOKEN")

echo "Get Order Response:"
pretty_json "$GET_ORDER"
sleep $SLEEP_TIME

# ============================================
# 9. ORDERS - Get User Orders
# ============================================

print_section "9. ORDERS - Get User Orders"

USER_ID=$(echo "$CREATE_ORDER" | jq -r '.userId' 2>/dev/null || echo "1")

GET_USER_ORDERS=$(curl -s -X GET "$BASE_URL/orders/api/orders/user/$USER_ID" \
  -H "Authorization: Bearer $TOKEN")

echo "User Orders Response:"
pretty_json "$GET_USER_ORDERS"
sleep $SLEEP_TIME

# ============================================
# 10. USERS - Get Profile
# ============================================

print_section "10. USERS - Get User Profile"

GET_USER=$(curl -s -X GET "$BASE_URL/users/api/users/1")

echo "User Profile Response:"
pretty_json "$GET_USER"
sleep $SLEEP_TIME

# ============================================
# 11. USERS - Create Profile
# ============================================

print_section "11. USERS - Create User Profile"

CREATE_USER=$(curl -s -X POST "$BASE_URL/users/api/users" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 123,
    "email": "test@example.com",
    "address": "123 Main St",
    "phone": "555-1234"
  }')

echo "Create User Profile Response:"
pretty_json "$CREATE_USER"
sleep $SLEEP_TIME

# ============================================
# 12. USERS - Update Profile
# ============================================

print_section "12. USERS - Update User Profile"

UPDATE_USER=$(curl -s -X PUT "$BASE_URL/users/api/users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "email": "updated@example.com",
    "address": "456 Oak Ave",
    "phone": "555-5678"
  }')

echo "Update User Profile Response:"
pretty_json "$UPDATE_USER"
sleep $SLEEP_TIME

# ============================================
# 13. HEALTH CHECKS
# ============================================

print_section "13. HEALTH CHECKS"

echo "API Gateway Health:"
curl -s "$BASE_URL/actuator/health" | jq '.'

echo -e "\nAuth Service Health:"
curl -s "http://localhost:8081/actuator/health" | jq '.'

echo -e "\nProduct Service Health:"
curl -s "http://localhost:8082/actuator/health" | jq '.'

echo -e "\nOrder Service Health:"
curl -s "http://localhost:8083/actuator/health" | jq '.'

# ============================================
# Summary
# ============================================

print_section "TEST SUMMARY"

echo -e "${GREEN}✓ All basic API tests completed!${NC}\n"

echo "Extracted Values:"
echo "  Token: ${TOKEN:0:50}..."
echo "  Product ID: $PRODUCT_ID"
echo "  Order ID: $ORDER_ID"
echo "  User ID: $USER_ID"

echo -e "\n${BLUE}Next Steps:${NC}"
echo "  1. Check RabbitMQ UI: http://localhost:15672 (guest/guest)"
echo "  2. View order.created events in queue"
echo "  3. Check Prometheus metrics: http://localhost:9090"
echo "  4. Monitor Grafana: http://localhost:3000 (admin/admin)"

echo -e "\n${GREEN}========== Testing Complete ==========${NC}\n"

