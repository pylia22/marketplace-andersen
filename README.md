# Category and Product Management API

## Overview
This project provides a RESTful API for managing categories and products. It includes endpoints for creating, retrieving, updating, and deleting categories and products. The API also supports file uploads for category and product logos.

## Technologies Used
•  [**Spring Boot**](https://www.bing.com/search?form=SKPBOT&q=Spring%20Boot): For building the RESTful API.

•  [**Spring Data JPA**](https://www.bing.com/search?form=SKPBOT&q=Spring%20Data%20JPA): For database interactions.

•  [**Hibernate**](https://www.bing.com/search?form=SKPBOT&q=Hibernate): As the ORM framework.

•  [**H2 Database**](https://www.bing.com/search?form=SKPBOT&q=H2%20Database): For in-memory database during development.

•  [**AWS S3**](https://www.bing.com/search?form=SKPBOT&q=AWS%20S3): For storing and managing logos.

•  [**Lombok**](https://www.bing.com/search?form=SKPBOT&q=Lombok): For reducing boilerplate code.

•  [**Swagger**](https://www.bing.com/search?form=SKPBOT&q=Swagger): For API documentation.


## Getting Started

### Prerequisites
•  Java 11 or higher

•  Maven

•  AWS S3 account (for storing logos)


### Installation

1. [**Clone the repository:**](https://www.bing.com/search?form=SKPBOT&q=Clone%20the%20repository%3A)
```sh
git clone https://github.com/your-repo/category-product-api.git
cd category-product-api

1. 
Configure AWS S3:
•  Set up your AWS credentials.

•  Update the application.properties file with your S3 bucket details.

1. 
Build the project:

mvn clean install

1. 
Run the application:

mvn spring-boot:run

API Endpoints
Category Endpoints
•  Get all categories:

GET /api/categories?page=0&size=5

Retrieves a paginated list of categories.

•  Add a new category:

POST /api/categories

Request body (multipart/form-data):
•  content: JSON representation of CategoryDto.

•  file: Logo file.

•  Delete a category:

DELETE /api/categories/{id}

Deletes a category by ID.

•  Get a category by ID:

GET /api/categories/{id}

Retrieves a category by ID.

Product Endpoints
•  Get all products:

GET /api/products?page=0&size=5

Retrieves a paginated list of products.

•  Get unique products:

GET /api/products/unique

Retrieves a set of unique product names.

•  Search products:

POST /api/products/search

Request body (JSON):

{
"productCategory": "Electronics",
"productName": "Laptop"
}

•  Add a new product:

POST /api/products

Request body (multipart/form-data):
•  content: JSON representation of ProductDto.

•  file: Logo file.

•  Edit a product:

PUT /api/products/{id}

Request body (multipart/form-data):
•  content: JSON representation of ProductDto.

•  file: Logo file (optional).

•  Delete a product:

DELETE /api/products/{id}

Deletes a product by ID.

•  Get a product by ID:

GET /api/products/{id}

Retrieves a product by ID.

Caching
The project uses a custom caching mechanism for categories and products to improve performance. The cache is implemented using a GenericCache interface and a CategoryCache class.

Logging
Logging is configured using SLF4J and Logback. Logs are generated for key operations such as fetching data from the repository and handling exceptions.
