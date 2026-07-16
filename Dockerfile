# ==========================
# Stage 1 - Build Spring Boot
# ==========================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Maven wrapper
COPY athleticax/.mvn .mvn
COPY athleticax/mvnw .
COPY athleticax/pom.xml .

RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY athleticax/src src

# Build project
RUN ./mvnw clean package -DskipTests


# ==========================
# Stage 2 - Runtime
# ==========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Install Python
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

# Copy Spring Boot jar
COPY --from=builder /app/target/*.jar app.jar

# Copy ML model
COPY ml-model ./ml-model

# Install Python dependencies
RUN pip3 install --no-cache-dir -r ./ml-model/requirements.txt

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
