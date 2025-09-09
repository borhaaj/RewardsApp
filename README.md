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
    "amount": 120,
    "date": "2025-08-15"
  },
  {
    "customerId": "C001",
    "amount": 75,
    "date": "2025-07-20"
  },
  {
    "customerId": "C001",
    "amount": 150,
    "date": "2025-07-10"
  },
  {
    "customerId": "C002",
    "amount": 60,
    "date": "2025-07-05"
  },
  {
    "customerId": "C002",
    "amount": 200,
    "date": "2025-08-12"
  }
]

### Response 
```json
[
    {
        "customerId": "C002",
        "monthlyRewards": [
            {
                "amount": 60.0,
                "month": "2025-07",
                "points": 10
            },
            {
                "amount": 200.0,
                "month": "2025-08",
                "points": 250
            }
        ],
        "totalPoints": 260
    },
    {
        "customerId": "C001",
        "monthlyRewards": [
            {
                "amount": 225.0,
                "month": "2025-07",
                "points": 175
            },
            {
                "amount": 120.0,
                "month": "2025-08",
                "points": 90
            }
        ],
        "totalPoints": 265
    }
]

ScreenShot:
<img width="638" height="442" alt="image" src="https://github.com/user-attachments/assets/cb318102-f233-4add-aaef-3ed69c4030ca" />

