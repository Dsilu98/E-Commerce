@echo off
REM Windows Batch Script for E-Commerce API Testing
REM Usage: Save as test_api.bat and run in CMD or PowerShell

setlocal enabledelayedexpansion

echo ==========================================
echo E-Commerce API Testing with curl
echo ==========================================

set BASE_URL=http://localhost:8080
set /a TIMESTAMP=%RANDOM%

echo.
echo ========== 1. AUTHENTICATION - Register User ==========
echo.

curl -s -X POST "%BASE_URL%/auth/api/auth/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser_%TIMESTAMP%\",\"email\":\"testuser_%TIMESTAMP%@example.com\",\"password\":\"TestPassword123\"}"

echo.
echo Waiting 1 second...
timeout /t 1 /nobreak

echo.
echo ========== 2. AUTHENTICATION - Login User ==========
echo.

REM For Windows, we'll use a simpler approach without jq
curl -s -X POST "%BASE_URL%/auth/api/auth/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser_%TIMESTAMP%\",\"password\":\"TestPassword123\"}"

echo.
echo NOTE: Copy the 'token' value from the response above
echo Set it as: set TOKEN=your_token_here
set TOKEN=YOUR_TOKEN_HERE

echo Token: %TOKEN%
timeout /t 1 /nobreak

echo.
echo ========== 3. PRODUCTS - Get All Products ==========
echo.

curl -s -X GET "%BASE_URL%/products/api/products"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 4. PRODUCTS - Get Product by ID ==========
echo.

curl -s -X GET "%BASE_URL%/products/api/products/1"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 5. PRODUCTS - Create Product (Requires JWT) ==========
echo.

curl -s -X POST "%BASE_URL%/products/api/products" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"name\":\"Test Keyboard\",\"description\":\"Mechanical keyboard\",\"price\":149.99,\"stock\":100}"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 6. ORDERS - Create Order (Most Complex!) ==========
echo.

curl -s -X POST "%BASE_URL%/orders/api/orders" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"items\":[{\"productId\":1,\"quantity\":2}]}"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 7. ORDERS - Get Order ==========
echo.

curl -s -X GET "%BASE_URL%/orders/api/orders/1" ^
  -H "Authorization: Bearer %TOKEN%"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 8. USERS - Get User Profile (Public) ==========
echo.

curl -s -X GET "%BASE_URL%/users/api/users/1"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 9. USERS - Create User Profile ==========
echo.

curl -s -X POST "%BASE_URL%/users/api/users" ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":123,\"email\":\"test@example.com\",\"address\":\"123 Main St\",\"phone\":\"555-1234\"}"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 10. USERS - Update User Profile ==========
echo.

curl -s -X PUT "%BASE_URL%/users/api/users/1" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"email\":\"updated@example.com\",\"address\":\"456 Oak Ave\",\"phone\":\"555-5678\"}"

echo.
timeout /t 1 /nobreak

echo.
echo ========== 11. HEALTH CHECKS ==========
echo.

echo API Gateway Health:
curl -s "%BASE_URL%/actuator/health"

echo.
echo.
echo Auth Service Health:
curl -s "http://localhost:8081/actuator/health"

echo.
echo.
echo Product Service Health:
curl -s "http://localhost:8082/actuator/health"

echo.
echo.
echo Order Service Health:
curl -s "http://localhost:8083/actuator/health"

echo.
echo ========== TEST COMPLETE ==========
echo.
echo Next Steps:
echo   1. RabbitMQ UI: http://localhost:15672 (guest/guest)
echo   2. Prometheus: http://localhost:9090
echo   3. Grafana: http://localhost:3000 (admin/admin)
echo.

pause

