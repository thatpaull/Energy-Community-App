
# ⚡ Energy Community Platform

A modular, microservice-based energy-sharing platform built with Java (Spring Boot) and Docker. It enables simulation, tracking, and management of energy consumption and production across user communities with RESTful APIs and a web-based GUI.

---


## 🏗️ Architecture

The platform consists of independently deployable services, communicating via REST APIs:

```
energy-community/
├── energy-community-rest-api             # API Gateway and routing
├── energy-community-usage-service       # Tracks energy consumption
├── energy-community-producer            # Handles production data
├── energy-community-user                # Manages user data and auth
├── energy-community-percentage-service  # Allocates energy contribution/usage ratios
├── energy-community-gui                 # Web interface (frontend)
├── docker-compose.yml                   # Orchestrates multi-service deployment
├── pom.xml                              # Maven multi-module project configuration
```

---

## 🧰 Tech Stack

| Layer         | Technologies                            |
|---------------|------------------------------------------|
| Backend       | Java 17, Spring Boot, Maven              |
| Frontend      | JavaFX                                   |
| Containerization | Docker, Docker Compose               |
| API           | RESTful APIs                             |
|              |

---

## 🚀 Getting Started

### ✅ Prerequisites

- [Java 17+](https://adoptopenjdk.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Docker](https://www.docker.com/products/docker-desktop)

### 🔧 Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-org/energy-community.git
   cd energy-community
   ```

2. **Build the application:**

   ```bash
   cd Energy-Community-App 
   mvn clean install
    ```

 3. **Start JavaFX:**


  ```bash  
  cd energy-community-gui
  mvn javafx:run
 ```



 4. **Start RestAPI:**
  ```bash
  cd energy-community-rest-api
  mvn spring-boot:run

```

---
