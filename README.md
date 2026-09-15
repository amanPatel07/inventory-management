# Inventory Management API

## Overview

This project is a Spring Boot REST API for managing inventory. It supports suppliers, products, stock movements, and inventory reports. The API can be used through Swagger UI, Postman, or another HTTP client.

The application uses Java 21, Spring Boot, Spring MVC, Spring Data JPA, PostgreSQL, Jakarta Bean Validation, Lombok, and Springdoc OpenAPI.

## Running the Application

The project requires Java 21 and PostgreSQL.

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux or macOS:

```bash
./mvnw spring-boot:run
```

The database connection is configured through a local `.env` file. Create it from the example:

```powershell
Copy-Item .env.example .env
```

Update `.env` with your local PostgreSQL credentials:

```properties
DB_URL=jdbc:postgresql://localhost:5432/inventory-management
DB_USERNAME=postgres
DB_PASSWORD=your-password
```

`.env` is ignored by Git and must not be committed. Spring Boot imports it through `spring.config.import` in `application.properties`. In deployment environments, set these variables directly through the environment or secret manager instead of using a file.

## Base URL and Context Path

The application configures `/api` once as its global context path:

```properties
server.servlet.context-path=/api
```

The base URL is:

```text
http://localhost:8080/api
```

Controller mappings must define only resource paths. For example, `/products` is exposed as `/api/products`; controllers must not add `/api` again.

## Architecture

The application follows a conventional layered architecture:

```text
HTTP request
	-> Controller
	-> Service interface
	-> Service implementation
	-> Spring Data JPA repository
	-> PostgreSQL
```

Controllers define routes, request parameters, validation, and response types. Services contain business rules and calculations. Repositories provide persistence access. DTOs define the public API contract instead of exposing JPA entities directly.

The main business areas are:

- Suppliers that provide products.
- Products with category, price, current stock, and minimum-stock thresholds.
- Stock movements that add or remove inventory.
- Reports based on current products and historical movements.

## Design Decisions

### Global API prefix

The `/api` prefix is configured once as the application context path. This prevents duplicated routes such as `/api/api/products`.

### Transactional stock updates

Creating a stock movement updates the product and persists the movement in one transaction. A persistence failure rolls back the stock change as well.

### Negative stock protection

An `OUT` movement is accepted only when enough stock is available. Insufficient stock returns `409 Conflict`.

### Stream-based calculations

Reports and the simplified movement-history filtering use Java Streams. This keeps the calculations readable and consistent with the current application scope.

### Date filtering

History filters use `LocalDate` at the API boundary. The service converts `from` to the start of that day and `to` to the start of the following day, making the end date inclusive. Both dates must be supplied together.

### Monetary values

Prices currently use `double`, matching the existing model. Migrating the application to `BigDecimal` is outside the current scope.

## API Reference

All URLs below include the `/api` context path.

### Suppliers

#### Create supplier

```text
POST /api/suppliers
```

```json
{
	"name": "Acme Supplies",
	"email": "contact@acme.test",
	"phone": "+1-555-0100"
}
```

The name, email, and phone are required. The email must be valid.

#### List suppliers

```text
GET /api/suppliers
```

### Products

#### Create product

```text
POST /api/products
```

```json
{
	"name": "Wireless Keyboard",
	"category": "Electronics",
	"price": 49.99,
	"minimumStock": 5,
	"supplierId": "SUPPLIER_UUID"
}
```

The supplier must already exist. The name and category cannot be blank, price cannot be negative, and minimum stock cannot be negative. New products start with zero current stock.

#### List products

```text
GET /api/products
```

Product responses contain `id`, `name`, `category`, `price`, `currentStock`, `minimumStock`, and `supplierId`.

### Stock Movements

#### Create movement

```text
POST /api/stock/movement
```

Stock in:

```json
{
	"productId": "PRODUCT_UUID",
	"type": "IN",
	"quantity": 20,
	"reason": "Initial warehouse stock"
}
```

Stock out:

```json
{
	"productId": "PRODUCT_UUID",
	"type": "OUT",
	"quantity": 5,
	"reason": "Customer order"
}
```

The product must exist, type must be `IN` or `OUT`, and quantity must be positive. `IN` increases stock and `OUT` decreases stock.

#### Get movement history

```text
GET /api/stock/movements
```

Optional query parameters:

```text
productId={uuid}
type=IN|OUT
from=yyyy-MM-dd
to=yyyy-MM-dd
```

Examples:

```text
GET /api/stock/movements
GET /api/stock/movements?productId=PRODUCT_UUID
GET /api/stock/movements?type=IN
GET /api/stock/movements?from=2026-09-01&to=2026-09-15
GET /api/stock/movements?productId=PRODUCT_UUID&type=OUT&from=2026-09-01&to=2026-09-15
```

Each provided filter is applied. No filters returns all movements, results are newest first, and no matches return `200 OK` with `[]`. The `from` and `to` dates must be supplied together, and the end date includes the entire day.

Movement responses contain `id`, `productId`, `productName`, `type`, `quantity`, `reason`, and `createdAt`.

### Reports

#### Low-stock products

```text
GET /api/reports/low-stock
```

Returns products where `currentStock <= minimumStock`.

#### Total stock value

```text
GET /api/reports/stock-value
```

Calculates `sum(currentStock * price)`.

#### Stock value by category

```text
GET /api/reports/stock-value/category
```

Returns a map of category names to their total stock value.

#### Inventory summary

```text
GET /api/reports/summary
```

Example response:

```json
{
	"totalProducts": 25,
	"totalUnitsInStock": 480,
	"lowStockProducts": 4,
	"totalInventoryValue": 125750.5,
	"totalStockIn": 650,
	"totalStockOut": 170
}
```

The summary combines current product data and all stock movements. It reports product count, current units, low-stock count, current inventory value, total `IN` quantity, and total `OUT` quantity.

## Swagger / OpenAPI

The API documentation is provided by `springdoc-openapi`.

Start the application with:

```bash
./mvnw spring-boot:run
```

Then open:

- Swagger UI: http://localhost:8080/api/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs
- OpenAPI YAML: http://localhost:8080/api/v3/api-docs.yaml

The application uses `/api` as its global context path. Controllers define only resource paths, so endpoints are exposed under `/api/...`.

## Error Handling

Errors use a shared structured response from the global REST exception handler:

```json
{
	"timestamp": "2026-09-15T12:00:00Z",
	"status": 400,
	"error": "Bad Request",
	"message": "Request validation failed",
	"path": "/api/products",
	"fieldErrors": {
		"supplierId": "must not be null"
	}
}
```

Main status mappings:

| Condition | HTTP status |
| --- | ---: |
| Invalid body or validation failure | 400 |
| Invalid UUID, enum, date, or date range | 400 |
| Supplier or product not found | 404 |
| Insufficient stock | 409 |
| Unexpected server failure | 500 |

Unexpected failures return a generic message rather than exposing stack traces, SQL details, or infrastructure information.

## Typical Test Sequence

1. Create a supplier and copy its generated UUID.
2. Create a product using that supplier UUID and copy the product UUID.
3. Add stock using an `IN` movement.
4. Remove stock using an `OUT` movement.
5. Query movement history.
6. Query the low-stock, value, category, and summary reports.

## Build and Test

Compile the application:

```powershell
.\mvnw.cmd clean compile
```

Run all tests:

```powershell
.\mvnw.cmd test
```

A Java 21 JDK is required because the Maven compiler target is Java 21. PostgreSQL must be available for the Spring application-context test.

## Development Strategies

- Keep controllers thin and place business rules in services.
- Reuse the existing repositories, DTO conventions, and exception contract.
- Validate request bodies at the controller boundary with Jakarta Validation.
- Use domain-specific exceptions for not-found and insufficient-stock conditions.
- Return response DTOs instead of exposing JPA entities directly.
- Preserve existing endpoint behavior when adding reports or history features.
- Use focused tests for calculations, filtering, validation, and error status codes.
- Keep new features within the existing controller-service-repository architecture.

## Future Considerations

These items are outside the current implementation scope:

- Authentication and authorization.
- Pagination for large list and history responses.
- Database-level aggregate queries for very large datasets.
- Replacing `double` with `BigDecimal` for monetary calculations.
- Using a deployment secret manager for database credentials.
- Additional audit and operational logging.
