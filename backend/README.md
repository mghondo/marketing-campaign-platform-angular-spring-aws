# Marketing Campaign Manager - Backend API

A robust RESTful API for managing marketing campaigns built with Spring Boot 3.2, PostgreSQL, and AWS S3 integration. This backend service provides JWT authentication, campaign management, file uploads, and analytics endpoints.

## 🛠 Tech Stack

- **Framework:** Spring Boot 3.2
- **Language:** Java 17
- **Database:** PostgreSQL 14+
- **ORM:** Spring Data JPA with Hibernate
- **Security:** Spring Security with JWT authentication
- **Cloud Storage:** AWS S3 for file uploads
- **Build Tool:** Maven 3.8+
- **Additional Libraries:** Lombok, JWT (jjwt), AWS SDK v2

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6 or higher
- AWS Account (for S3 integration) - optional for local development

## 🗄 Database Setup

1. **Install PostgreSQL** if not already installed:
   - Mac: `brew install postgresql`
   - Windows: Download from [postgresql.org](https://www.postgresql.org/download/)
   - Linux: `sudo apt-get install postgresql`

2. **Create the database:**
   ```bash
   psql -U postgres
   CREATE DATABASE campaignmanager;
   \q
   ```

3. **Database Connection Details:**
   - URL: `jdbc:postgresql://localhost:5432/campaignmanager`
   - Default Username: `postgres`
   - Default Password: `postgres`

## ⚙️ Configuration

### Environment Variables

You can set environment variables in multiple ways:

1. **Copy the example file:**
   ```bash
   cp .env.example .env
   ```

2. **Edit the `.env` file with your values:**
   ```properties
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   JWT_SECRET=your-256-bit-secret-key
   AWS_S3_BUCKET_NAME=your-bucket-name
   AWS_REGION=us-east-1
   AWS_ACCESS_KEY_ID=your-access-key
   AWS_SECRET_ACCESS_KEY=your-secret-key
   ```

3. **Or export them directly:**
   ```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_password
   export JWT_SECRET=your-secret-key
   ```

### Application Configuration

The application uses `application.yml` for configuration. Default values are provided for local development, but you should override them for production using environment variables.

## 🚀 How to Run

### 1. Clone the Repository
```bash
git clone https://github.com/mghondo/marketing-campaign-platform-angular-spring-aws.git
cd marketing-campaign-platform-angular-spring-aws/backend
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

Or with a specific profile:
```bash
mvn spring-boot:run -Dspring.profiles.active=local
```

### 4. Access the Application
The backend API will be available at: `http://localhost:8080`

Health check endpoint: `http://localhost:8080/actuator/health`

## 📁 Project Structure

```
src/main/java/com/portfolio/campaignmanager/
├── config/              # Spring configuration classes
├── controller/          # REST controllers
├── service/            # Business logic layer
├── repository/         # JPA repositories  
├── model/
│   ├── entity/        # JPA entities
│   ├── dto/           # Data Transfer Objects
│   └── enums/         # Enums (UserRole, CampaignStatus)
├── security/          # Security config, JWT utilities
├── exception/         # Custom exceptions, error handlers
└── CampaignManagerApplication.java  # Main application class
```

## 📊 Data Model

### Entities

#### User
- `id` (UUID) - Primary key
- `email` (String) - Unique user email
- `password` (String) - BCrypt hashed password
- `name` (String) - User's full name
- `role` (UserRole) - ADMIN or USER
- `createdAt` (LocalDateTime) - Registration timestamp

#### Campaign
- `id` (UUID) - Primary key
- `name` (String) - Campaign name
- `description` (String) - Campaign description
- `budget` (BigDecimal) - Campaign budget
- `startDate` (LocalDate) - Campaign start date
- `endDate` (LocalDate) - Campaign end date
- `targetAudience` (String) - Target audience description
- `status` (CampaignStatus) - DRAFT, ACTIVE, PAUSED, COMPLETED
- `createdAt` (LocalDateTime) - Creation timestamp
- `updatedAt` (LocalDateTime) - Last update timestamp
- `user` (User) - Campaign owner (ManyToOne relationship)

#### CampaignAsset
- `id` (UUID) - Primary key
- `campaign` (Campaign) - Associated campaign (ManyToOne)
- `fileName` (String) - Original file name
- `s3Key` (String) - Unique S3 object key
- `s3Url` (String) - Public S3 URL
- `fileType` (String) - MIME type
- `fileSize` (Long) - File size in bytes
- `uploadedAt` (LocalDateTime) - Upload timestamp

#### CampaignMetric
- `id` (UUID) - Primary key
- `campaign` (Campaign) - Associated campaign (ManyToOne)
- `impressions` (Integer) - Number of impressions
- `clicks` (Integer) - Number of clicks
- `conversions` (Integer) - Number of conversions
- `date` (LocalDate) - Metric date

### Relationships
- **User → Campaign**: One-to-Many
- **Campaign → CampaignAsset**: One-to-Many (cascade delete)
- **Campaign → CampaignMetric**: One-to-Many (cascade delete)

## 🔄 Phase 1 Implementation Status

✅ **Completed:**
- Spring Boot 3.2 project setup
- Maven configuration with all dependencies
- PostgreSQL integration
- JPA entities with proper relationships
- Enum definitions (UserRole, CampaignStatus)
- Application configuration (application.yml)
- Project structure organization
- Environment variable support
- Cascade delete configuration
- UUID primary keys
- Timestamp handling

⏳ **Next Phase (Phase 2):**
- Repository interfaces
- Service layer implementation
- REST controllers
- JWT authentication
- AWS S3 file upload
- Error handling
- Input validation
- API documentation

## 🧪 Testing

Run tests with:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

## 🐛 Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running: `pg_ctl status`
- Check database exists: `psql -U postgres -l`
- Verify connection details in application.yml

### Build Issues
- Ensure Java 17 is installed: `java -version`
- Clear Maven cache: `mvn clean`
- Update dependencies: `mvn dependency:resolve`

### Application Won't Start
- Check port 8080 is not in use: `lsof -i :8080`
- Review logs for specific errors
- Ensure all required environment variables are set

## 📝 Notes

- The application uses Hibernate's `update` strategy for DDL. Tables will be created automatically on first run.
- Default JWT expiration is set to 24 hours (86400000 ms)
- File uploads are limited to 50MB per file
- Logging is configured at DEBUG level for development

## 🔒 Security Considerations

- Never commit `.env` files or actual credentials
- Use strong JWT secrets in production (minimum 256 bits)
- Database passwords should be complex
- AWS credentials should have minimal required permissions
- Consider using AWS IAM roles in production instead of access keys

## 📮 Contact

For questions or issues, please open an issue on GitHub or contact the development team.

---

**Current Phase:** Phase 1 - Data Model and Configuration ✅  
**Next Step:** Phase 2 - Repositories and Services