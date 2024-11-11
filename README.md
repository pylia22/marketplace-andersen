# Category and Product Marketplace API

## Overview
This project provides a RESTful API for managing categories and products. It includes endpoints for creating, retrieving, 
updating, and deleting categories and products. The API also supports file uploads for category and product logos.

## Getting Started

### Prerequisites
•  Java 17

•  Gradle

• Docker

### Note

Kindly note for PUT api/products/{productId} authorization is required.
Please use username: admin / password: admin

Swagger documentation is available at
http://localhost:8080/swagger-ui/index.html#/.

## Important
Please insert S3_REGION, S3_ACCESS_KEY, S3_SECRET_KEY and S3_BUCKET_NAME into compose.yaml before running docker compose up. 
The S3 credentials will be provided upon request or to associate RM

## Installation

build the project, open Docker Desktop and run docker compose command.
```bash
./gradlew build -x test    
docker compose up

