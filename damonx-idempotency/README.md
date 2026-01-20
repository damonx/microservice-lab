# Idempotent Microservice Demo (Spring Boot + Gradle Kotlin DSL)

A practical example of how to build an **idempotent REST API** using:

✅ `Idempotency-Key` header  
✅ **Upsert (create-or-update) database writes**  
✅ **State-based conditional operations**

---

## Features

| Pattern            | Implementation Approach                                  |
|--------------------|-----------------------------------------------------------|
| Idempotency-Key     | Stored in memory mapping to response / database record    |
| Upsert Instead Insert | If entity exists → update; else → insert               |
| Conditional Update  | Skip execution if current resource state already matches |

---

## Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Gradle (Kotlin DSL)**
- **PostgreSQL Database + TestContainer**

---

## Running the App

```bash
./gradlew bootRun
```

---
# Designing an Idempotent API

Idempotency ensures that multiple identical requests have the **same effect** as a single request. This is essential for **network retries**, **failure recovery**, and **at-least-once delivery** scenarios.

---

## When to Make APIs Idempotent

| HTTP Method | Expected Idempotent? | Example Operation                 |
|-------------|---------------------|----------------------------------|
| GET         | ✅ Yes              | Fetch resource                   |
| PUT         | ✅ Yes              | Replace/update resource          |
| DELETE      | ✅ Yes              | Delete resource                  |
| POST        | ❌ No (by default)  | Create resource / trigger action |
| PATCH       | ❌ Depends          | Partial update                   |

For **POST requests that create resources or trigger side effects**, idempotency must be **explicitly implemented**.

---

## Core Strategy: Idempotency Key

Clients send a unique identifier per logical request.  
Servers **store the result** and **return the same response** on subsequent retries.

### 1. Client Sends Key
POST /payments
Idempotency-Key: 9fbc1d34-5d6a-4ef6-a12f-7c82ac

{
“amount”: 100,
“userId”: “123”
}

### 2. Server Handles Request

| Scenario                         | Server Action                            |
|----------------------------------|------------------------------------------|
| First request with new Key      | Process request → Cache outcome          |
| Duplicate request with same Key | Return **cached response** without redoing |

---

## Implementation Flow

```text
Receive Request
 └── Extract Idempotency-Key
      ├── Missing → Reject (400 Bad Request)
      ├── Exists in Store → Return Stored Response
      └── Not Found → Process Request
                └── Save (Key + Response) in Store
```

Storage Options for Idempotency
--
| Storage Layer | Pros                     | Cons              |
|---------------|--------------------------|-------------------|
| Redis         | Fast, TTL support        | Requires infra    |
| Database      | Durable, easy to query   | Higher latency    |
| In-Memory     | Simple, single-node only | Not scalable      |

Response Codes
| Response          | Status Code                 |
|------------------|-----------------------------|
| First-time success | 200 / 201                   |
| Duplicate request  | 200 / 201 (same as original) |
| Missing key        | 400 Bad Request             |

### Best Practices

- Require Idempotency-Key for POST creation APIs
- Store result until operation is final
- Match key + request payload hash to avoid misuse
- Expire keys after reasonable TTL (e.g., 24 hours)

⸻

Summary

Idempotency is not just about HTTP verbs — it’s about guaranteeing consistency under retries.
By using Idempotency Keys + Response Caching, you can safely handle duplicate requests without unintended side effects.
