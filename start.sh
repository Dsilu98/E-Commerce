#!/usr/bin/env bash
# Quick start script for the microservice e-commerce project

set -e

echo "======================================"
echo "E-Commerce Microservices - Quick Start"
echo "======================================"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose is not installed"
    exit 1
fi

echo ""
echo "1. Building Maven artifacts..."
mvn clean package -DskipTests

echo ""
echo "2. Building Docker images..."
docker-compose build

echo ""
echo "3. Starting all services..."
docker-compose up -d

echo ""
echo "4. Waiting for services to start..."
sleep 10

echo ""
echo "======================================"
echo "Services Started Successfully!"
echo "======================================"
echo ""
echo "API Gateway:     http://localhost:8080"
echo "Auth Service:    http://localhost:8081"
echo "Product Service: http://localhost:8082"
echo "Order Service:   http://localhost:8083"
echo "User Service:    http://localhost:8084"
echo ""
echo "RabbitMQ Admin:  http://localhost:15672 (guest/guest)"
echo "Prometheus:      http://localhost:9090"
echo "Grafana:         http://localhost:3000 (admin/admin)"
echo ""
echo "To stop services: docker-compose down"
echo "To view logs:     docker-compose logs -f"
echo ""

