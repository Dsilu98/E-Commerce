@echo off
REM Quick start script for Windows

echo ======================================
echo E-Commerce Microservices - Quick Start
echo ======================================

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not installed
    exit /b 1
)

REM Check if Docker Compose is installed
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker Compose is not installed
    exit /b 1
)

echo.
echo 1. Building Maven artifacts...
call mvn clean package -DskipTests

echo.
echo 2. Building Docker images...
call docker-compose build

echo.
echo 3. Starting all services...
call docker-compose up -d

echo.
echo 4. Waiting for services to start...
timeout /t 10 /nobreak

echo.
echo ======================================
echo Services Started Successfully!
echo ======================================
echo.
echo API Gateway:     http://localhost:8080
echo Auth Service:    http://localhost:8081
echo Product Service: http://localhost:8082
echo Order Service:   http://localhost:8083
echo User Service:    http://localhost:8084
echo.
echo RabbitMQ Admin:  http://localhost:15672 (guest/guest)
echo Prometheus:      http://localhost:9090
echo Grafana:         http://localhost:3000 (admin/admin)
echo.
echo To stop services: docker-compose down
echo To view logs:     docker-compose logs -f
echo.

