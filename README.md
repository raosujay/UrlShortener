# URL Shortener Service

A production-ready RESTful backend application that transforms long URLs into short, manageable links with comprehensive analytics tracking. Built with Java 17, Spring Boot 3, and MongoDB.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Usage Examples](#usage-examples)
- [Contributing](#contributing)

## ✨ Features

- **URL Shortening**: Convert long URLs into 7-character alphanumeric short codes
- **Custom Aliases**: Create branded short links with custom aliases
- **Link Expiration**: Set expiration dates for short URLs with automatic deactivation
- **Analytics Tracking**: 
  - Total click count
  - Geographic data (country, region, city)
  - Referrer sources (direct, social media, search engines)
  - Custom UTM parameters (source, medium, campaign)
  - Click trends by date
- **User Authentication**: Secure JWT-based authentication and authorization
- **User Management**: Each user manages their own collection of short URLs
- **RESTful API**: Well-structured REST endpoints with proper HTTP methods
- **API Documentation**: Interactive Swagger/OpenAPI documentation

## 🛠 Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Maven
- **Database**: MongoDB
- **Data Layer**: Spring Data MongoDB
- **Security**: Spring Security 6 with JWT
- **API Documentation**: Swagger/OpenAPI 3 (springdoc-openapi)
- **Additional Libraries**: Lombok, JJWT

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.8+**
- **MongoDB 4.4+** (local installation or MongoDB Atlas)
- **Git** (for cloning the repository)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/url-shortener-service.git
cd url-shortener-service
```

### 2. Install Dependencies

```bash
mvn clean install
```

## ⚙️ Configuration

### 1. MongoDB Setup

**Option A: Local MongoDB**

Ensure MongoDB is running on `localhost:27017`

```bash
mongod
```

**Option B: MongoDB Atlas**

Update `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://username:password@cluster.mongodb.net/url_shortener
```

### 2. Application Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: url-shortener-service
  
  data:
    mongodb:
      host: localhost
      port: 27017
      database: url_shortener

server:
  port: 8080

jwt:
  secret: your-256-bit-secret-key-change-this-in-production-must-be-at-least-32-characters
  expiration: 86400000  # 24 hours

app:
  base-url: http://localhost:8080
```

**⚠️ Important**: Change the JWT secret key in production!

### 3. Environment Variables (Optional)

You can also configure using environment variables:

```bash
export MONGODB_URI=mongodb://localhost:27017/url_shortener
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
export BASE_URL=http://localhost:8080
```

## 🏃‍♂️ Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

### Production Build

```bash
mvn clean package
java -jar target/url-shortener-service-1.0.0.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🔗 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/register` | Register new user | No |
| POST | `/api/v1/auth/login` | Login user | No |

### URL Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/urls` | Create short URL | Yes |
| GET | `/api/v1/urls` | Get all user URLs | Yes |
| GET | `/api/v1/urls/{shortCode}` | Get URL details | Yes |
| PUT | `/api/v1/urls/{shortCode}` | Update URL | Yes |
| DELETE | `/api/v1/urls/{shortCode}` | Delete URL | Yes |
| GET | `/r/{shortCode}` | Redirect to original URL | No |

### Analytics Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/analytics/{shortCode}` | Get URL analytics | Yes |

## 📁 Project Structure

```
url-shortener-service/
├── src/main/java/com/urlshortener/
│   ├── UrlShortenerApplication.java
│   ├── config/                    # Configuration classes
│   │   ├── MongoConfig.java
│   │   ├── SecurityConfig.java
│   │   ├── JwtConfig.java
│   │   └── OpenApiConfig.java
│   ├── controller/                # REST controllers
│   │   ├── AuthController.java
│   │   ├── UrlController.java
│   │   └── AnalyticsController.java
│   ├── dto/                       # Data Transfer Objects
│   │   ├── request/
│   │   └── response/
│   ├── entity/                    # MongoDB entities
│   │   ├── User.java
│   │   ├── ShortUrl.java
│   │   └── ClickEvent.java
│   ├── repository/                # MongoDB repositories
│   │   ├── UserRepository.java
│   │   ├── ShortUrlRepository.java
│   │   └── ClickEventRepository.java
│   ├── service/                   # Business logic interfaces
│   │   └── impl/                  # Service implementations
│   ├── security/                  # JWT security components
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   ├── exception/                 # Exception handling
│   │   └── GlobalExceptionHandler.java
│   └── util/                      # Utility classes
│       ├── GeoLocationUtil.java
│       └── UrlValidator.java
└── src/main/resources/
    └── application.yml
```

## 💡 Usage Examples

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepassword123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "john_doe",
  "email": "john@example.com"
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securepassword123"
  }'
```

### 3. Create Short URL

```bash
curl -X POST http://localhost:8080/api/v1/urls \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "originalUrl": "https://www.example.com/very/long/url/with/many/parameters",
    "customAlias": "mylink",
    "expiresAt": "2025-12-31T23:59:59"
  }'
```

**Response:**
```json
{
  "id": "674a2b3c1d8f9e001234abcd",
  "shortCode": "mylink",
  "shortUrl": "http://localhost:8080/r/mylink",
  "originalUrl": "https://www.example.com/very/long/url/with/many/parameters",
  "totalClicks": 0,
  "expiresAt": "2025-12-31T23:59:59",
  "createdAt": "2025-11-17T23:30:00",
  "active": true
}
```

### 4. Get URL Analytics

```bash
curl -X GET http://localhost:8080/api/v1/analytics/mylink \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
{
  "shortCode": "mylink",
  "totalClicks": 150,
  "clicksByCountry": {
    "India": 80,
    "United States": 50,
    "United Kingdom": 20
  },
  "clicksByRegion": {
    "Karnataka": 60,
    "California": 40,
    "London": 20
  },
  "clicksByReferrer": {
    "direct": 70,
    "social": 50,
    "search": 30
  },
  "clicksByDate": {
    "2025-11-15": 45,
    "2025-11-16": 60,
    "2025-11-17": 45
  }
}
```

### 5. Redirect Using Short URL

Simply visit or curl:
```bash
curl -L http://localhost:8080/r/mylink
```

This will redirect to the original URL.

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

## 🐛 Troubleshooting

### Common Issues

**1. Application fails to start with `mongoAuditingHandler` bean error**

Ensure `@EnableMongoAuditing` is only present in `MongoConfig.java` and not in the main application class.

**2. MongoDB connection refused**

Verify MongoDB is running:
```bash
# Check if MongoDB is running
sudo systemctl status mongod

# Start MongoDB if not running
sudo systemctl start mongod
```

**3. JWT token validation fails**

Ensure your JWT secret key is at least 256 bits (32 characters) and matches between encoding and decoding.

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 👤 Author

**Sujay S**
- GitHub: [@raosujay](https://github.com/raosujay)
- Email: sujay.92@yahoo.com

***
