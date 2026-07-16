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
    apt-get install -y python3 python3-pip python3-venv && \
    rm -rf /var/lib/apt/lists/*

# Create virtual environment
RUN python3 -m venv /opt/venv

# Use the virtual environment
ENV PATH="/opt/venv/bin:$PATH"

# Install dependencies
RUN pip install --upgrade pip && \
    pip install --no-cache-dir -r ./ml-model/requirements.txt

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
