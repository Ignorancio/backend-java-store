# Ecommerce api

This is an **e-commerce API** designed for managing products, orders, and users.  
Built with **Java 17** and **Spring Boot 3**, it follows the principles of **hexagonal architecture**, ensuring a clear separation between business logic and infrastructure layers.  
Additionally, it implements **design patterns** to enhance scalability, maintainability, and efficiency.
## Features

- Authentication and Authorization using JWT
- Product, orders management
- Caching for products
- Documentation using OpenAPI
- Hexagonal architecture

## Technologies

- **Java 17**
- **Spring Boot 3**
- **Spring Security** Authentication and Authorization using JWT
- **Spring Data JPA** for data access
- **Postgres** as database
- **OpenAPI** for API documentation
- **Docker** for containerization
- **Docker Compose** for running the application
- **Redis** for caching
- **Elasticsearch** for search

## How to run

1. Clone the repository
```bash
git clone https://github.com/Ignorancio/backend-java-store
cd backend-java-store
```
2. Build the project
```bash
docker compose build
```
3. Run the project
```bash
docker compose up -d
```

## Endpoints

- **Authentication:**
    - `POST /auth/login` - Login and get a JWT token
    - `POST /auth/register` - Register a new user
    - `POST /auth/refresh` - Refresh a JWT token
    - `POST /auth/register/admin` - Register a new admin user, only for testing purposes

- **Products:**
    - `GET /api/v1/products` - Get all products
    - `GET /api/v1/products/{id}` - Get a product by ID
    - `POST /api/v1/products` - Create a new product
    - `PUT /api/v1/products/` - Update a product
    - `DELETE /api/v1/products/{id}` - Delete a product

- **Categories:**
    - `GET /api/v1/categories` - Get all categories
    - `GET /api/v1/categories/{id}` - Get a category by ID
    - `POST /api/v1/categories` - Create a new category
    - `PUT /api/v1/categories/` - Update a category
    - `DELETE /api/v1/categories/{id}` - Delete a category

- **Orders:**
    - `GET /api/v1/orders` - Get all orders associated with the authenticated user
    - `GET /api/v1/orders/{id}` - Get an order by ID
    - `GET /api/v1/orders/all` - Get all orders for admin users
    - `POST /api/v1/orders` - Create a new order
    - `PUT /api/v1/orders/{id}` - Update an order
    - `DELETE /api/v1/orders/{id}` - Delete an order

- **Search:**
    - `GET /api/v1/search` - Search for products by name

## TODO

- [ ] Refactor Auth service
- [ ] Improve persistence for caching
- [ ] Add tests
- [ ] Add CI/CD
- [ ] Add Elasticsearch for search
- [ ] Add monitoring