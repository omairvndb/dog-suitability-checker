# Dog Suitability Checker

A microservice system that checks whether a dog breed is suitable for the current weather in a given city. Submit a breed and city, and the system fetches real-time weather data and breed characteristics to give you a suitability verdict (GOOD, MODERATE, or BAD).

## Architecture

```
[Frontend (React)]
       |
       | POST /check, GET /status, GET /result
       v
[dog-checker-api]  ──── PostgreSQL
       |  ^
       |  | (listens to response queue, updates DB)
       |  |
       | sends to "check-request" queue
       v
[Artemis Message Broker]
       |
       | delivers from "check-request" queue
       v
[dog-checker-worker]
       |── calls Dogs API   (api.api-ninjas.com)
       |── calls Weather API (weatherapi.com)
       |── evaluates suitability
       |
       | sends to "check-response" queue
       v
[Artemis Message Broker] → [dog-checker-api] → [PostgreSQL]
```

## Tech Stack

| Layer           | Technology                              |
|-----------------|----------------------------------------|
| Framework       | Quarkus 3.x (Java 21)                  |
| REST            | RESTEasy Reactive + Jackson             |
| ORM             | Hibernate ORM with Panache              |
| Database        | PostgreSQL                              |
| Messaging       | SmallRye Reactive Messaging + AMQP (Artemis) |
| HTTP Client     | MicroProfile REST Client                |
| Fault Tolerance | SmallRye Fault Tolerance (Retry, Timeout, CircuitBreaker, Fallback) |
| Health          | SmallRye Health                         |
| Metrics         | Micrometer + Prometheus                 |
| Frontend        | React (Vite)                            |
| Containerization| Docker + Docker Compose                 |

## Project Structure

```
dog-suitability-checker/
├── docker-compose.yml          ← Infrastructure + all services
├── dog-checker-api/            ← REST API + database + messaging
├── dog-checker-worker/         ← Background worker + external API calls
└── dog-checker-frontend/       ← React frontend
```

## Prerequisites

- **Docker** and **Docker Compose**
- **JDK 21** (for dev mode)
- **Node.js 18+** (for frontend dev mode)
- **API Keys:**
  - Dogs API key from [api-ninjas.com](https://api-ninjas.com)
  - Weather API key from [weatherapi.com](https://www.weatherapi.com)

## API Keys Setup

Create a `.env` file in the `dog-checker-worker/` directory:

```
DOGS_API_KEY=your_api_ninjas_key_here
WEATHER_API_KEY=your_weather_api_key_here
```

## Running

### Production Mode (Docker)

Runs everything in containers with a single command:

```bash
docker compose up --build
```

- Frontend: http://localhost:5173
- API: http://localhost:8080
- Artemis Admin UI: http://localhost:8161 (artemis/artemis)

### Dev Mode

Dev mode gives you hot reload for both backend and frontend.

1. Start infrastructure:
   ```bash
   docker compose up -d artemis postgres
   ```

2. Start the API (new terminal):
   ```bash
   cd dog-checker-api
   ./mvnw quarkus:dev
   ```

3. Start the worker (new terminal):
   ```bash
   cd dog-checker-worker
   ./mvnw quarkus:dev
   ```

4. Start the frontend (new terminal):
   ```bash
   cd dog-checker-frontend
   npm run dev
   ```

- Frontend: http://localhost:5173
- API: http://localhost:8080

## API Endpoints

### POST /check
Submit a suitability check request.
```bash
curl -X POST http://localhost:8080/check \
  -H "Content-Type: application/json" \
  -d '{"breed": "labrador", "city": "Brussels"}'
```
Response: `{"id": 1, "status": "PENDING"}`

### GET /status/{id}
Check the status of a request.
```bash
curl http://localhost:8080/status/1
```
Response: `{"id": 1, "status": "PENDING|PROCESSING|DONE|FAILED"}`

### GET /result/{id}
Get the full result (only available when status is DONE or FAILED).
```bash
curl http://localhost:8080/result/1
```
Response:
```json
{
  "id": 1,
  "breed": "labrador",
  "city": "Brussels",
  "status": "DONE",
  "suitability": "GOOD",
  "reason": "High-energy dogs thrive in moderate, cool climates perfect for outdoor activity."
}
```

## Observability

| Endpoint              | Service | Description          |
|-----------------------|---------|----------------------|
| `GET /q/health`       | API     | Database connectivity |
| `GET /q/health`       | Worker  | API key configuration |
| `GET /q/metrics`      | API     | Prometheus metrics    |
| `GET /q/metrics`      | Worker  | Prometheus metrics    |

- API health/metrics: http://localhost:8080/q/health, http://localhost:8080/q/metrics
- Worker health/metrics: http://localhost:8081/q/health, http://localhost:8081/q/metrics
