# resilience4j-spring-boot-demo

Demo project showing Resilience4j features with Spring Boot 3.5.6 and Java 21:
- Circuit Breaker, Retry, Rate Limiter, Bulkhead, Time Limiter
- Caffeine cache with Spring Cache

Run:
1. ./gradlew bootRun
2. POST /api/payments/{id}
```bash
➜ curl -Sv -X POST localhost:8080/api/payments/1
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> POST /api/payments/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sun, 12 Oct 2025 03:28:37 GMT
< 
* Connection #0 to host localhost left intact
{"id":"1","amount":86.71647931700136}
```
3. GET /api/payments/{id} (cached)
```bash
➜ curl -Sv -X GET localhost:8080/api/payments/1
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/payments/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sun, 12 Oct 2025 03:29:20 GMT
< 
* Connection #0 to host localhost left intact
{"id":"1","amount":75.77156322445107}
```
4. GET /api/bulk
```bash
➜ curl -Sv -X GET localhost:8080/api/bulk
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/bulk HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 17
< Date: Sun, 12 Oct 2025 03:29:50 GMT
< 
* Connection #0 to host localhost left intact
bulkhead-fallback
```
5. GET /api/timed/{id}
```bash
➜ curl -Sv -X GET localhost:8080/api/timed/1
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/timed/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 504 
< Content-Length: 0
< Date: Sun, 12 Oct 2025 03:30:03 GMT
< 
* Connection #0 to host localhost left intact
```

Notes:
- Resilience4j starter expects actuator & AOP to be present.  [oai_citation:4‡resilience4j](https://resilience4j.readme.io/docs/getting-started-3?utm_source=chatgpt.com)
- This demo uses simple in-memory fallback logic for illustration.