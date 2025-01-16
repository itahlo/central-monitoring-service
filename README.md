# Warehouse Monitor

A simple Spring Boot application that:
- Listens for sensor data (temperature, humidity) over UDP.
- Publishes parsed measurements to RabbitMQ.
- Consumes sensor data from RabbitMQ and raises alarms if thresholds are exceeded.

## Requirements
- Java 21+
- Gradle
- RabbitMQ on `localhost:5672` (default credentials: `guest/guest`)

## Building & Running

1. **Start RabbitMQ (e.g., via Docker):**
   ```bash
   docker run -d --hostname my-rabbit --name some-rabbit \
   -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# Build & Run with Gradle 
    ./gradlew clean build
    ./gradlew bootRun

# Testing the Application

To test the application, you can use `netcat` or a similar tool to send UDP packets to the application. Follow these steps:

## Temperature Monitoring (Threshold = 35°C)
1. Open a terminal.
2. Send a temperature packet to **UDP port 3344**:
   ```bash
   echo "sensor_id=t1; value=32" | nc -4u -w1 localhost 3344
