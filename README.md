# PayDay Bank Application

## About this project
Project is about stock sell/buy, transactions in stocks, show risky stocks for handle investment strategy, get email after stock transactions, risk analysis and sending emails about stock changes.

## Technologies

- Spring Boot
- Spring Security
- Spring Data
- Liquibase
- Netflix Zuul
- Netflix Eureka
- Docker
- PostgreSQL
- RabbitMQ
- SendGrid
- Swagger

## Solution Architecture
![alt text](https://github.com/shahriyar-ibar/payday/blob/master/Payday%20Architecture.png)

## Microservices

Each microservice is maven project:
- api-gateway
- service-discovery
- ms-auth
- ms-user
- ms-notification
- ms-stock

 Each microservice has its own Dockerfile for creation of docker image.

## Run this project

1. In production mode, run 'docker-compose up'

2. Setup SendGrid `apiKey` in ms-notification to send email to users.

## Endpoints

| Endpoint                                          | Method            | Desccription                  |
| -------------                                     |:-------------:    | -----:                        |
| http://localhost:8000/stock/stocks                | GET               | Get available stocks          |
| http://localhost:8000/stock/stocks/preferential   | GET               | Get preferential stocks       |
| http://localhost:8000/stock/users/{userId}/stocks | GET               | Get users stocks              |
| http://localhost:8000/stock/transaction           | POST              | POST stock transaction        |

Note: ms-auth security disabled for uncompleted implementation due to time deadline