# Evolyn-API

**Evolyn-API** is a **Helidon MP (MicroProfile)**–based backend service that provides authentication, authorization, and secure access for the Evolyn platform. It serves as the entry point for client applications and issues **JWT tokens** used to access downstream Evolyn services.

---

## Purpose

Evolyn-API is responsible for:

* Authenticating users
* Issuing and validating JWT tokens
* Enforcing authorization rules
* Acting as an authentication gateway for Evolyn services

Business logic and persistence are handled by downstream core services.

---

## Architecture Overview

All client requests pass through Evolyn-API for authentication and authorization before reaching core services.

```
Frontend
   ↓
Evolyn-API (Helidon MP)
   ↓ JWT
Evolyn Core Service (Spring Boot)
```

---

## Responsibilities

* Handle login and authentication flows
* Generate and sign JWT tokens
* Validate incoming JWT tokens
* Enforce authorization using MicroProfile Security
* Secure APIs using JAX-RS filters
* Provide health and readiness endpoints

---

## Tech Stack

* Java 21
* **Helidon MP (MicroProfile)**
* Jakarta REST (JAX-RS)
* MicroProfile Security
* JWT (JSON Web Tokens)
* JSON-B
* Maven

---

## Project Structure

```
evolyn-api/
├── api/
│   ├── authentication/   # Login & auth endpoints
│   ├── health/           # Health & readiness checks
│   └── filters/          # Security & request filters
├── security/             # JWT config & utilities
├── config/               # application.yaml & MP config
└── Main.java             # Helidon MP bootstrap
```

---

## Security

* JWT-based authentication using **MicroProfile JWT**
* Tokens are issued after successful login
* All protected endpoints require:

```
Authorization: Bearer <JWT_TOKEN>
```

* JWT validation is enforced via MP Security on each secured request

---

## Running the Service

### Prerequisites

* Java 17
* Maven

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/*.jar
```

Service runs at:

```
http://localhost:3001
```

(port configurable via `application.yaml`)

---

## Example Authentication Flow

1. Client sends login request
2. Evolyn-API validates credentials
3. Evolyn-API issues a signed JWT token
4. Client sends JWT in `Authorization` header
5. Protected APIs validate token before processing

---

## Health Check

```http
GET /health
```

Response:

```json
{
  "status": "UP"
}
```


---

## Why Helidon MP Fits Evolyn

* Standards-based (Jakarta EE & MicroProfile)
* Annotation-driven, clean APIs
* Built-in security & health checks
* Cloud-native and lightweight
* Easy onboarding for enterprise teams

---
