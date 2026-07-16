# ==========================
# STAGE 1 : Build Spring Boot
# ==========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and pom
COPY athleticax/.mvn .mvn
COPY athleticax/mvnw .
COPY athleticax/pom.xml .

RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY athleticax/src src

# Build application
RUN ./mvnw clean package -DskipTests


# ==========================
# STAGE 2 : Runtime
# ==========================
FROM eclipse-temurin:21-jre

# Install Python
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Spring Boot jar
COPY --from=builder /app/target/*.jar app.jar

# Copy ML model folder
COPY ml-model ./ml-model

# Install Python dependencies
RUN pip3 install --no-cache-dir -r ml-model/requirements.txt

# Railway/Render port
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
