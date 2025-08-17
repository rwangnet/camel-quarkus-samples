# Hotel Scheduler - Spring Boot + Apache Camel (Hexagonal)

- Domain-driven, hexagonal architecture (domain, application, infrastructure)
- REST API secured with JWT (OAuth2 Resource Server) and optional mTLS profile
- H2 in-memory database using Spring Data JPA
- Apache Camel for async routes (logs booking-created events)

## Build

```bash
mvn -DskipTests package
```

## Run (HTTP + JWT)

```bash
mvn spring-boot:run
```

- Base URL: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:hotelscheduler`)
- Endpoints require a Bearer JWT token signed with HS256 using the Base64 secret in `application.yml` (`app.security.jwt.secret`).

Example curl (replace <JWT>):

```bash
curl -sS -X POST http://localhost:8080/api/bookings \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <JWT>' \
  -d '{
    "userFullName": "Jane Doe",
    "userEmail": "jane@example.com",
    "roomNumber": "101",
    "checkInDate": "2099-01-10",
    "checkOutDate": "2099-01-12",
    "notes": "Late check-in"
  }'
```

## Run with mTLS + JWT

- Generate server and client keystores/truststores (example):

```bash
# 1) Server keystore (PKCS12)
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore server-keystore.p12 -storepass changeit -dname "CN=localhost, OU=Dev, O=Example, L=, S=, C=US"
# 2) Export server cert
keytool -exportcert -alias server -keystore server-keystore.p12 -storepass changeit -rfc -file server.crt
# 3) Client keystore
keytool -genkeypair -alias client -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore client-keystore.p12 -storepass changeit -dname "CN=client, OU=Dev, O=Example, L=, S=, C=US"
# 4) Export client cert
keytool -exportcert -alias client -keystore client-keystore.p12 -storepass changeit -rfc -file client.crt
# 5) Server truststore: trust the client cert
keytool -importcert -alias client -file client.crt -keystore server-truststore.p12 -storepass changeit -noprompt
# 6) Client truststore: trust the server cert
keytool -importcert -alias server -file server.crt -keystore client-truststore.p12 -storepass changeit -noprompt
```

- Place `server-keystore.p12` and `server-truststore.p12` under any folder and use env vars to run with profile `mtls`:

```bash
APP_SSL_KEY_STORE=$(pwd)/server-keystore.p12 \
APP_SSL_KEY_STORE_PASSWORD=changeit \
APP_SSL_TRUST_STORE=$(pwd)/server-truststore.p12 \
APP_SSL_TRUST_STORE_PASSWORD=changeit \
SPRING_PROFILES_ACTIVE=mtls \
  mvn spring-boot:run
```

- Base URL: `https://localhost:8443`

Curl example using client PKCS12 for mTLS (replace <JWT>):

```bash
curl -sS -X GET https://localhost:8443/api/bookings \
  --cert client-keystore.p12:changeit --cert-type P12 \
  --cacert server.crt \
  -H 'Authorization: Bearer <JWT>'
```

## Notes
- JWT is validated with HS256 symmetric key from `app.security.jwt.secret`.
- To simplify demo, audience/issuer are not enforced; configure them in `SecurityConfig` if needed.
- Architecture layers:
  - domain: entities and ports
  - application: use cases/services
  - infrastructure: adapters (REST/JPA), security, Camel