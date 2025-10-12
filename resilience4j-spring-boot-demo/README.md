# resilience4j-spring-boot-demo

Demo project showing Resilience4j features with Spring Boot 3.5.6 and Java 21:
- Circuit Breaker, Retry, Rate Limiter, Bulkhead, Time Limiter
- Caffeine cache with Spring Cache

Run:
1. ./gradlew bootRun
2. POST /api/payments/{id}
3. GET /api/payments/{id} (cached)
4. GET /api/bulk
5. GET /api/timed/{id}

Notes:
- Resilience4j starter expects actuator & AOP to be present.  [oai_citation:4‡resilience4j](https://resilience4j.readme.io/docs/getting-started-3?utm_source=chatgpt.com)
- This demo uses simple in-memory fallback logic for illustration.