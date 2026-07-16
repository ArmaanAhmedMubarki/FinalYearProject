# AthleticaX

AthleticaX is a web-based College Sports Management System developed as a Final Year B.Tech Project. The platform provides role-based access for Athletes, Coaches, and Administrators to manage sports events, athlete registrations, sports news, and AI-assisted cricket squad recommendations. The backend is built using Spring Boot with JWT authentication, while the frontend uses HTML, CSS, JavaScript, and Bootstrap. A Python-based machine learning module integrated with Ollama powers the squad recommendation chatbot.

---

# Running the Project Locally

## Prerequisites

Install the following software before running the project:

- Java 17
- Apache Maven
- MySQL 8
- Python 3.10+
- Git
- Ollama
- Visual Studio Code / IntelliJ IDEA

---

# Environment Variables

Create a `.env` file inside the Spring Boot project directory and configure the following values:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=athleticax
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

JWT_KEY=YOUR_BASE64_ENCODED_SECRET_KEY
JWT_EXPIRATION=86400000

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

> **Note**
>
> `JWT_KEY` must be a Base64-encoded secret key of at least 256 bits for HS256 signing.

---

# MySQL Configuration

1. Install MySQL 8.
2. Create a database named:

```sql
CREATE DATABASE athleticax;
```

3. Update the `.env` file with your database credentials.

4. Run the Spring Boot application. Hibernate will create the required tables automatically (if configured).

---

# Ollama Installation

The AI Squad Recommendation Chatbot currently uses **Ollama** as the local Large Language Model (LLM).

## Step 1

Download and install Ollama:

https://ollama.com/download

---

## Step 2

Verify installation:

```bash
ollama --version
```

---

## Step 3

Pull the required model (example: Llama 3)

```bash
ollama pull llama3
```

or whichever model is configured in the project.

---

## Step 4

Start the Ollama server

```bash
ollama serve
```

The server runs locally at:

```
http://localhost:11434
```

Spring Boot communicates with this endpoint for captain and vice-captain recommendations.

---

# Python Chatbot Module

Install the required Python libraries:

```bash
pip install pandas
pip install numpy
pip install scikit-learn
pip install flask
pip install requests
```

Start the chatbot service before launching Spring Boot.

---

# Running the Application

### Backend

```bash
mvn spring-boot:run
```

Runs on:

```
http://localhost:8080
```

---

### Frontend

Open the frontend using Live Server in Visual Studio Code or any local web server.

Default frontend URL:

```
http://127.0.0.1:5500
```

---

# Notes

- This repository contains the development version of AthleticaX.
- The chatbot currently requires a local Ollama installation.
- For cloud deployment, Ollama will be replaced with the Groq API, eliminating the need for local LLM installation.
