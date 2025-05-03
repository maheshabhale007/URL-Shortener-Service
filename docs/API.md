# URL Shortener Service API Documentation

A RESTful API for creating, redirecting, and viewing statistics of shortened URLs.

**Base URL**: `http://localhost:8080/api`

## Endpoints

### 1. Create a Short URL
**POST /api/shorten**

Creates a short URL for a given long URL.

#### Request
- **Method**: POST
- **URL**: `/api/shorten`
- **Headers**:
  - `Content-Type: application/json`
- **Body**:
  ```json
  {
      "longUrl": "https://www.google.com"
  }
  ```

#### Responses
- **200 OK**: Short URL created successfully.
  ```json
  {
      "id": 1,
      "longUrl": "https://www.google.com",
      "shortCode": "abc123",
      "clickCount": 0,
      "createdAt": "2025-05-03T12:34:56"
  }
  ```
- **400 Bad Request**: Invalid URL format.
  ```json
  {
      "message": "Invalid URL format"
  }
  ```

### 2. Redirect to Long URL
**GET /api/{shortCode}**

Redirects to the original long URL based on the short code.

#### Request
- **Method**: GET
- **URL**: `/api/{shortCode}` (e.g., `/api/abc123`)
- **Headers**: None

#### Responses
- **302 Found**: Redirects to the long URL.
  - Header: `Location: https://www.google.com`
- **404 Not Found**: Short URL not found.
  ```json
  {
      "message": "Short URL not found"
  }
  ```

### 3. Get URL Statistics
**GET /api/stats/{shortCode}**

Retrieves statistics for a short URL, including click count.

#### Request
- **Method**: GET
- **URL**: `/api/stats/{shortCode}` (e.g., `/api/stats/abc123`)
- **Headers**: None

#### Responses
- **200 OK**: Statistics retrieved successfully.
  ```json
  {
      "id": 1,
      "longUrl": "https://www.google.com",
      "shortCode": "abc123",
      "clickCount": 5,
      "createdAt": "2025-05-03T12:34:56"
  }
  ```
- **404 Not Found**: Short URL not found.
  ```json
  {
      "message": "Short URL not found"
  }
  ```

## Error Handling
- **400 Bad Request**: Returned for invalid input (e.g., malformed URL).
- **404 Not Found**: Returned when a short URL does not exist.

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
   Follows redirect to `https://www.google.com`.

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
- Ensure the application is running on `http://localhost:8080`.
- Short codes are unique and case-sensitive.
- Contact the developer for API access or issues.