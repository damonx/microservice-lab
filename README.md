# Idempotent Microservice Demo (Spring Boot + Gradle Kotlin DSL)

A practical example of how to build an **idempotent REST API** using:

✅ `Idempotency-Key` header  
✅ **Upsert (create-or-update) database writes**  
✅ **State-based conditional operations**

---

## 🚀 Features

| Pattern            | Implementation Approach                                  |
|--------------------|-----------------------------------------------------------|
| Idempotency-Key     | Stored in memory mapping to response / database record    |
| Upsert Instead Insert | If entity exists → update; else → insert               |
| Conditional Update  | Skip execution if current resource state already matches |

---

## 📦 Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Gradle (Kotlin DSL)**
- **H2 In-Memory Database**

---

## ▶️ Running the App

```bash
./gradlew bootRun
```