# Flight Service - AI-Assisted Testing & CD

This project is a Spring Boot application developed as part of the **Continuous Build and Delivery** module. It serves as a practical implementation of modern testing strategies, featuring a refactored architecture for high testability and the integration of AI-assisted testing tools.

---

## 🚀 Key Features

* **Flight Management:** Backend services to handle flight data and passenger assignments.
* **Many-to-Many Relationships:** Robust handling of the relationship between Flights and Passengers.
* **Architectural Refactor:** Migrated from Field Injection (`@Autowired`) to **Constructor-based Dependency Injection** to ensure clean code and testability.
* **AI-Assisted Workflow:** Developed in collaboration with **Junie (IntelliJ AI Assistant)** for test generation, gap analysis, and code auditing.

---

## 🛠️ Tech Stack

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Data JPA** (H2 In-memory Database)
* **JUnit 5 & Mockito** (Testing Frameworks)
* **Maven** (Build Tool)

---

## 🧪 Testing Strategy (The Test Pyramid)

This project strictly adheres to the **Test Pyramid** to ensure a fast, reliable, and maintainable CI/CD pipeline.

| Layer | Focus | Technology |
| :--- | :--- | :--- |
| **Unit Tests** | Data Mapping (Mappers) | JUnit 5 |
| **Service Tests** | Business Logic | Mockito (Mocking Repositories) |
| **API Tests** | REST Contracts & Status Codes | MockMvc |

### **Highlights:**
* **Mocking:** Used strategically in the Service layer to isolate business rules.
* **Performance:** Tests are decoupled from the Spring Context whenever possible to ensure near-instant execution.
* **Coverage:** High line coverage achieved through AI-identified edge cases and logic analysis.

---

## 📖 API Documentation

The following endpoints are available in the application:

### **Flights**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/flights` | Returns a list of all flights. |
| `GET` | `/api/flights/{id}` | Returns details of a specific flight. |
| `POST` | `/api/flights/{id}/passengers` | Adds a new passenger to a specific flight. |

**Example Request (Add Passenger):**
```json
POST /api/flights/1/passengers
Content-Type: application/json

{
  "firstName": "Gilvan",
  "lastName": "Almeida",
  "email": "gilvan@example.com"
}
```
## 🤖 AI Collaboration Log
The testing suite includes a mix of human-authored and AI-generated tests. Key interactions with the AI assistant (Junie) included:

1. **Accepted Outputs:** Generation of boilerplate mapper tests for DTO conversion.
2. **Modified Outputs:** Correction of AI-generated MockMvc paths and HTTP status codes (201 Created vs 200 OK).
3. **Rejected Outputs:** Rejection of heavy @SpringBootTest suggestions in favor of isolated @WebMvcTest.
4. **Gap Analysis:** Using AI to identify missing error scenarios, such as 404 Not Found for missing flight IDs.

---

## 🏁 Getting Started

**Prerequisites**
* JDK 17 or higher.
* Maven 3.6+.

**Installation & Execution**
1. **Clone the repository:**
```bash
git clone https://github.com/iamgilvan/flightservices.git
```

2. **Build the project:**
```bash
mvn clean install
```

3. **Run the automated tests (with coverage):**
```bash
mvn test
```

4. **Run the application:**
```bash
mvn spring-boot:run
```
---

## 📊 Coverage Report
To view the test coverage within IntelliJ IDEA:

1. Right-click the src/test/java folder.
2. Select **'Run All Tests with Coverage'**.
3. Analyze results in the **Coverage** tool window.

---

**Developed by:** Gilvan Almeida

**Academic Context:** Continuous Build and Delivery - MSc in Software Engineering.









