# Rewards Program API

## Overview
This project provides a RESTful API to calculate reward points for customers based on their purchase transactions. The reward points are calculated as follows:
- 1 point for every dollar spent between $50 and $100 in each transaction
- 2 points for every dollar spent over $100 in each transaction

## Design
### Architecture
- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic for reward calculation
- **Model Layer**: Data transfer objects for request/response
- **Exception Handling**: Global exception handler for consistent error responses

### Technical Details
- **Framework**: Spring Boot 2.7.x
- **Java Version**: 8
- **Build Tool**: Maven
- **Validation**: Bean Validation (JSR 380)
- **Logging**: SLF4J with Logback
- **Testing**: JUnit 5 and Mockito

## API Details
### Endpoint
- **POST /api/rewards/calculate**

### Request Body
```json
[
  {
    "customerId": "C001",
    "amount": 120.0,
    "date": "2023-01-15"
  }
]
