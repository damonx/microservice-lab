# Java Micrometer + Prometheus Demo

This demo shows a minimal Spring Boot (Java 21) application instrumented with Micrometer and exposed to Prometheus. Grafana is pre-provisioned with a simple dashboard.

## Quick Start

1. Build the app jar:
```bash
./gradlew bootJar
```

2. Start Docker Compose (will build the app image and start Prometheus + Grafana):
```bash
docker compose up --build
```

3. Endpoints:
- App: http://localhost:8080/api/hello
- Prometheus UI: http://localhost:9090
- Grafana UI: http://localhost:3000 (admin/admin)

## Notes
- The app exposes metrics at `/actuator/prometheus`.
- Grafana is pre-configured to connect to Prometheus and load a basic dashboard.

