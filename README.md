# URL Shortener Service

A RESTful API built with **Spring Boot** for creating short URLs, redirecting to long URLs, and tracking click statistics. This project demonstrates modern Java development practices, including REST API design, database integration with MySQL, and comprehensive testing.

## Features
- **Shorten URLs**: Convert long URLs into short, unique codes.
- **Redirect**: Use short codes to redirect to the original URLs.
- **Statistics**: View click counts and metadata for short URLs.
- **Error Handling**: Robust handling of invalid inputs and non-existent URLs.
- **Testing**: Unit and integration tests with JUnit and Mockito.
- **Documentation**: Detailed API documentation via Postman and markdown.

## Prerequisites
- **Java**: JDK 17 or later
- **Maven**: 3.8.x or later
- **MySQL**: 8.0 or later
- **Postman**: For testing and viewing API documentation
- **IDE**: IntelliJ IDEA, VS Code, or similar (optional)

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/url-shortener.git
   cd url-shortener
   ```

2. **Configure MySQL**:
   - Install MySQL if not already installed.
   - Create a database named `url_shortener`:
     ```sql
     CREATE DATABASE url_shortener;
     ```
   - Update `src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
     spring.datasource.username=root
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```
   - The API will be available at `http://localhost:8080/api`.

## Running Tests
- Execute unit and integration tests:
  ```bash
  mvn test
  ```
- Tests include:
  - `UrlServiceTest`: Unit tests for URL creation and validation.
  - `UrlControllerTest`: Integration tests for API endpoints.
  - `UrlShortenerApplicationTests`: Context loading test.

## API Documentation
Explore the API using the following resources:
- **[Postman Documentation](https://documenter.getpostman.com/view/12345678/abc123)**: Interactive documentation with example requests and responses.
- **[API Reference](docs/API.md)**: Detailed markdown-based API reference.

### Endpoints
- `POST /api/shorten`: Create a short URL.
- `GET /api/{shortCode}`: Redirect to the long URL.
- `GET /api/stats/{shortCode}`: View URL statistics.

## Example Usage
1. **Create a Short URL**:
   ```bash
   curl -X POST http://localhost:8080/api/shorten \
   -H "Content-Type: application/json" \
   -d '{"longUrl": "https://www.google.com"}'
   ```
   Response:
   ```json
   {
       "id": 1,
       "longUrl": "https://www.google.com",
       "shortCode": "abc123",
       "clickCount": 0,
       "createdAt": "2025-05-03T12:34:56"
   }
   ```

2. **Redirect**:
   ```bash
   curl -L http://localhost:8080/api/abc123
   ```
   Redirects to `https://www.google.com`.

3. **View Stats**:
   ```bash
   curl http://localhost:8080/api/stats/abc123
   ```
   Response:
   ```json
   {
       "id": 1,
       "longUrl": "https://www.google.com",
       "shortCode": "abc123",
       "clickCount": 5,
       "createdAt": "2025-05-03T12:34:56"
   }
   ```

## Notes
- Ensure MySQL is running before starting the application.
- Short codes are unique and case-sensitive.
- For issues or contributions, contact the developer via [GitHub Issues](https://github.com/your-username/url-shortener/issues).

## License
[MIT License](LICENSE)