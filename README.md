
---

```markdown
# FinOps Backend ⚙️

A production-grade fraud detection backend built with **Spring Boot**, secured using **Spring Security**, and deployed on **Render**.

## ⚡ Highlights

- REST APIs for transactions, accounts, alerts
- Session-based authentication (`JSESSIONID` with CORS)
- Spring Security + BCrypt for login and role-based access
- Serverless PostgreSQL (Neon) integration
- CORS & Cookie propagation for Vercel frontend

## 🛠 Stack

- Java 21, Spring Boot 3.x
- Spring Security
- PostgreSQL (Neon)
- Dockerized Deployment (Multi-stage)
- Render Web Service Hosting

## 🔐 Auth

- `/login` → form POST endpoint (email/password)
- `/logout` → invalidates session
- Secure cookies with `SameSite=None`, `HttpOnly`, `Secure`

## 🌍 API Endpoints

```http
POST /login
GET  /api/analysts/me
GET  /api/accounts
POST /api/transactions
...
And 20+ more RESTful APIs. Refer to each entity's controller folder for more infomation.

# Build locally
docker build -t finopsbk .
docker run -p 8080:8080 finopsbk
