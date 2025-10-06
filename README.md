# Marketing Campaign Platform - Full-Stack Enterprise Application

[![Angular](https://img.shields.io/badge/Angular-17-red)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue)](https://www.postgresql.org/)
[![AWS](https://img.shields.io/badge/AWS-Cloud-yellow)](https://aws.amazon.com/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-blue)](https://www.docker.com/)

A production-ready, full-stack marketing campaign management platform built with Angular, Spring Boot, and AWS cloud services. This enterprise-grade application demonstrates modern software development practices, cloud-native architecture, and professional UI/UX design.

## 🌟 Overview

This Marketing Campaign Platform is a comprehensive admin dashboard that enables businesses to manage their marketing campaigns efficiently. Built as a portfolio project, it showcases expertise in full-stack development, cloud architecture, and modern DevOps practices. The application features a responsive Angular frontend powered by CoreUI, a robust Spring Boot backend with JWT authentication, and seamless AWS cloud integration for scalability and reliability.

### Key Highlights
- **Production-Ready**: Deployed on AWS with enterprise-grade infrastructure
- **Full-Stack**: End-to-end implementation from database to UI
- **Cloud-Native**: Leverages multiple AWS services for scalability
- **Secure**: JWT authentication, role-based access control, and Spring Security
- **Modern Tech Stack**: Latest versions of Angular 17 and Spring Boot 3.2
- **Professional UI**: CoreUI admin template with responsive design
- **Data-Driven**: Analytics dashboard with real-time metrics and visualizations

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication system
- User registration and login
- Role-based access control (Admin/User roles)
- Secure password hashing with BCrypt
- Token expiration and refresh handling
- Protected routes and API endpoints

### 📊 Campaign Management
- **Full CRUD Operations**: Create, read, update, and delete marketing campaigns
- **Campaign Details**: Name, description, budget, dates, target audience
- **Status Management**: Draft, Active, Paused, and Completed states
- **Advanced Filtering**: Search by name, filter by status
- **Bulk Operations**: Manage multiple campaigns efficiently
- **Data Validation**: Client and server-side validation

### 📁 File Management
- **AWS S3 Integration**: Secure cloud storage for campaign assets
- **Drag-and-Drop Upload**: Intuitive file upload interface
- **Multiple File Types**: Support for images (JPG, PNG, GIF), PDFs, and videos (MP4)
- **File Preview**: Visual preview of uploaded assets
- **Size Limits**: Up to 50MB per file with validation
- **Asset Management**: View and delete campaign assets

### 📈 Analytics Dashboard
- **Real-Time Metrics**: Live campaign performance data
- **Interactive Charts**: Line graphs, bar charts, and pie charts using Chart.js
- **Key Performance Indicators**:
  - Total impressions, clicks, and conversions
  - Click-through rate (CTR) calculations
  - Conversion rate analytics
  - Campaign ROI tracking
- **Top Performers**: Identify best-performing campaigns
- **Date Range Filtering**: Analyze data for custom periods
- **Export Capabilities**: Download reports and data

### 🎨 User Experience
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **CoreUI Admin Template**: Professional, modern interface
- **Real-Time Notifications**: Toast notifications for user actions
- **Loading States**: Smooth loading indicators for async operations
- **Empty States**: Helpful messages and calls-to-action
- **Keyboard Navigation**: Accessibility-focused design
- **Dark/Light Mode**: Theme switching capability

## 🛠️ Tech Stack

### Frontend
- **Framework**: Angular 17 (Latest Stable Version)
- **UI Library**: CoreUI Admin Template
- **Language**: TypeScript 5.x with strict mode
- **Styling**: SCSS with modular architecture
- **Charts**: Chart.js with ng2-charts wrapper
- **State Management**: RxJS for reactive programming
- **HTTP Client**: Angular HttpClient with interceptors
- **Forms**: Reactive Forms with custom validators
- **Notifications**: ngx-toastr for toast messages
- **Routing**: Angular Router with lazy loading

### Backend
- **Framework**: Spring Boot 3.2
- **Language**: Java 17 LTS
- **Security**: Spring Security with JWT
- **Database ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven 3.8+
- **Utilities**: Lombok for boilerplate reduction
- **AWS SDK**: AWS SDK for Java v2
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Validation**: Jakarta Bean Validation
- **Testing**: JUnit 5, Mockito

### Database
- **Development**: PostgreSQL 14+ (Local)
- **Production**: AWS RDS PostgreSQL
- **Migration Tool**: Flyway
- **Connection Pooling**: HikariCP

### Cloud Infrastructure & DevOps
- **File Storage**: AWS S3 with CloudFront CDN
- **Database**: AWS RDS PostgreSQL (Multi-AZ)
- **Backend Hosting**: AWS ECS Fargate with ALB
- **Frontend Hosting**: AWS S3 + CloudFront
- **Containerization**: Docker with multi-stage builds
- **Container Registry**: AWS ECR
- **Monitoring**: AWS CloudWatch
- **SSL/TLS**: AWS Certificate Manager
- **Secrets Management**: AWS Secrets Manager

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CloudFront CDN                           │
│                                                                 │
│  ┌──────────────┐                    ┌──────────────┐         │
│  │   Angular    │                    │     AWS      │         │
│  │   Frontend   │◄──────HTTPS────────┤   WAF/ALB    │         │
│  │   (S3)       │                    │              │         │
│  └──────────────┘                    └──────┬───────┘         │
│                                              │                 │
│                                              ▼                 │
│                                    ┌──────────────┐           │
│                                    │  Spring Boot │           │
│                                    │   Backend    │           │
│                                    │  (ECS Fargate)│          │
│                                    └──────┬───────┘           │
│                                           │                    │
│                    ┌──────────────────────┼──────────┐        │
│                    ▼                      ▼          ▼        │
│         ┌──────────────┐     ┌──────────────┐  ┌─────────┐  │
│         │   AWS RDS    │     │    AWS S3    │  │ Secrets │  │
│         │  PostgreSQL  │     │ File Storage │  │ Manager │  │
│         └──────────────┘     └──────────────┘  └─────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Application Flow

1. **User Authentication**
   - User submits credentials to `/api/auth/login`
   - Spring Security validates credentials
   - JWT token generated and returned
   - Frontend stores token in localStorage
   - Subsequent requests include token in Authorization header

2. **Campaign Management**
   - User creates/updates campaign via Angular form
   - Data validated on frontend
   - Request sent to Spring Boot API
   - Spring Data JPA persists to PostgreSQL
   - Response returned with updated data

3. **File Upload**
   - User drags file to upload zone
   - Frontend validates file type/size
   - Multipart form data sent to backend
   - Backend generates unique S3 key
   - File uploaded to S3 bucket
   - S3 URL stored in database

## 📊 Data Model

### Entity Relationship Diagram

```
┌─────────────┐        ┌─────────────────┐        ┌────────────────┐
│    User     │        │    Campaign     │        │ CampaignAsset  │
├─────────────┤        ├─────────────────┤        ├────────────────┤
│ id (UUID)   │◄───────┤ userId (FK)     │        │ id (UUID)      │
│ email       │ 1    * │ id (UUID)       │◄───────┤ campaignId(FK) │
│ password    │        │ name            │ 1    * │ fileName       │
│ name        │        │ description     │        │ s3Key          │
│ role        │        │ budget          │        │ s3Url          │
│ createdAt   │        │ startDate       │        │ fileType       │
└─────────────┘        │ endDate         │        │ fileSize       │
                       │ targetAudience  │        │ uploadedAt     │
                       │ status          │        └────────────────┘
                       │ createdAt       │
                       │ updatedAt       │        ┌────────────────┐
                       └─────────────────┘        │CampaignMetric  │
                                    ▲              ├────────────────┤
                                    │              │ id (UUID)      │
                                    └──────────────┤ campaignId(FK) │
                                           1    *  │ impressions    │
                                                  │ clicks         │
                                                  │ conversions    │
                                                  │ date           │
                                                  └────────────────┘
```

### Database Schema

#### User Table
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Campaign Table
```sql
CREATE TABLE campaigns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    budget DECIMAL(12,2),
    start_date DATE,
    end_date DATE,
    target_audience VARCHAR(500),
    status VARCHAR(50) DEFAULT 'DRAFT',
    user_id UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔌 API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123",
  "name": "John Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

### Campaign Management Endpoints

#### Get All Campaigns
```http
GET /api/campaigns?status=ACTIVE
Authorization: Bearer {token}
```

#### Create Campaign
```http
POST /api/campaigns
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Summer Sale 2024",
  "description": "Annual summer promotion",
  "budget": 50000.00,
  "startDate": "2024-06-01",
  "endDate": "2024-08-31",
  "targetAudience": "18-35 age group",
  "status": "DRAFT"
}
```

#### Update Campaign
```http
PUT /api/campaigns/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

#### Delete Campaign
```http
DELETE /api/campaigns/{id}
Authorization: Bearer {token}
```

### File Management Endpoints

#### Upload Asset
```http
POST /api/campaigns/{campaignId}/assets
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

#### Get Campaign Assets
```http
GET /api/campaigns/{campaignId}/assets
Authorization: Bearer {token}
```

#### Delete Asset
```http
DELETE /api/assets/{assetId}
Authorization: Bearer {token}
```

### Analytics Endpoints

#### Dashboard Summary
```http
GET /api/dashboard/summary
Authorization: Bearer {token}
```

#### Campaign Metrics
```http
GET /api/campaigns/{campaignId}/metrics?startDate=2024-01-01&endDate=2024-12-31
Authorization: Bearer {token}
```

#### Top Campaigns
```http
GET /api/dashboard/top-campaigns?limit=5
Authorization: Bearer {token}
```

## 💻 Installation

### Prerequisites
- Java 17 JDK
- Node.js 18+ and npm 9+
- PostgreSQL 14+
- Maven 3.8+
- AWS Account (for S3 integration)
- Docker (optional, for containerization)

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/mghondo/marketing-campaign-platform-angular-spring-aws.git
cd marketing-campaign-platform-angular-spring-aws
```

2. **Configure PostgreSQL Database**
```bash
# Create database
psql -U postgres
CREATE DATABASE marketing_campaigns;
\q
```

3. **Set Environment Variables**
```bash
# Create application-local.properties in backend/src/main/resources/
spring.datasource.url=jdbc:postgresql://localhost:5432/marketing_campaigns
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT Configuration
jwt.secret=your-256-bit-secret-key
jwt.expiration=86400000

# AWS Configuration
aws.access.key.id=your-aws-access-key
aws.secret.access.key=your-aws-secret-key
aws.s3.bucket.name=your-s3-bucket-name
aws.region=us-east-1
```

4. **Build and Run Backend**
```bash
cd backend
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=local
```
The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Install Dependencies**
```bash
cd frontend
npm install
```

2. **Configure Environment**
```typescript
// Create environment.local.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  awsS3Url: 'https://your-bucket.s3.amazonaws.com'
};
```

3. **Run Development Server**
```bash
npm start
```
The frontend will be available at `http://localhost:4200`

### Docker Setup (Optional)

1. **Build Backend Docker Image**
```bash
cd backend
docker build -t marketing-platform-backend .
```

2. **Run with Docker Compose**
```bash
docker-compose up -d
```

## 📱 Usage

### Getting Started

1. **Register a New Account**
   - Navigate to `http://localhost:4200/register`
   - Fill in your details
   - Submit to create account

2. **Login**
   - Go to `http://localhost:4200/login`
   - Enter credentials
   - Access the dashboard

3. **Create Your First Campaign**
   - Click "New Campaign" button
   - Fill in campaign details
   - Set budget and dates
   - Save campaign

4. **Upload Campaign Assets**
   - Open campaign details
   - Drag and drop files to upload area
   - View uploaded assets
   - Delete unwanted files

5. **View Analytics**
   - Navigate to Dashboard
   - Review campaign metrics
   - Filter by date range
   - Export reports

### User Roles

#### Admin Role
- Full access to all campaigns
- Can view system-wide analytics
- Manage user accounts
- Access to admin panel

#### User Role
- Create and manage own campaigns
- Upload and manage assets
- View own campaign analytics
- Standard dashboard access

## 🚀 Deployment

### AWS Infrastructure Setup

#### 1. RDS PostgreSQL Setup
```bash
# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier marketing-platform-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 14.9 \
  --master-username admin \
  --master-user-password YourSecurePassword \
  --allocated-storage 20
```

#### 2. S3 Bucket Configuration
```bash
# Create S3 bucket for assets
aws s3 mb s3://marketing-platform-assets

# Enable public access for campaign assets
aws s3api put-bucket-policy --bucket marketing-platform-assets --policy file://bucket-policy.json
```

#### 3. ECS Fargate Deployment
```bash
# Create ECR repository
aws ecr create-repository --repository-name marketing-platform-backend

# Push Docker image
docker tag marketing-platform-backend:latest [account-id].dkr.ecr.us-east-1.amazonaws.com/marketing-platform-backend:latest
docker push [account-id].dkr.ecr.us-east-1.amazonaws.com/marketing-platform-backend:latest

# Deploy to ECS
aws ecs create-service \
  --cluster marketing-cluster \
  --service-name marketing-backend \
  --task-definition marketing-backend-task \
  --desired-count 2 \
  --launch-type FARGATE
```

#### 4. Frontend Deployment to S3/CloudFront
```bash
# Build production frontend
cd frontend
npm run build --prod

# Deploy to S3
aws s3 sync dist/ s3://marketing-platform-frontend --delete

# Create CloudFront distribution
aws cloudfront create-distribution --distribution-config file://cloudfront-config.json
```

### Environment Variables

#### Production Backend (.env)
```bash
SPRING_PROFILES_ACTIVE=production
DB_HOST=marketing-platform-db.xxxx.rds.amazonaws.com
DB_PORT=5432
DB_NAME=marketing_campaigns
DB_USERNAME=${SECRET_DB_USERNAME}
DB_PASSWORD=${SECRET_DB_PASSWORD}
JWT_SECRET=${SECRET_JWT_KEY}
AWS_ACCESS_KEY_ID=${SECRET_AWS_ACCESS_KEY}
AWS_SECRET_ACCESS_KEY=${SECRET_AWS_SECRET_KEY}
AWS_S3_BUCKET=marketing-platform-assets
```

#### Production Frontend (environment.prod.ts)
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.marketing-platform.com/api',
  awsS3Url: 'https://d1234567890.cloudfront.net'
};
```

## 📸 Screenshots

### Login Page
![Login Page](screenshots/login.png)
*Clean, modern login interface with form validation*

### Dashboard Overview
![Dashboard](screenshots/dashboard.png)
*Analytics dashboard with real-time metrics and charts*

### Campaign List
![Campaign List](screenshots/campaign-list.png)
*Comprehensive campaign management interface with filtering*

### Campaign Details
![Campaign Details](screenshots/campaign-detail.png)
*Detailed campaign view with asset management*

### File Upload
![File Upload](screenshots/file-upload.png)
*Drag-and-drop file upload with progress indicators*

### Mobile Responsive
![Mobile View](screenshots/mobile-view.png)
*Fully responsive design for mobile devices*

## 🔮 Future Enhancements

### Phase 2 Features
- **Email Campaign Integration**: Connect with SendGrid/Mailchimp
- **A/B Testing**: Built-in A/B testing capabilities
- **Advanced Analytics**: Machine learning-powered insights
- **Team Collaboration**: Multi-user campaign management
- **Template Library**: Pre-built campaign templates
- **Webhook Integration**: Real-time event notifications
- **Export/Import**: Bulk campaign import/export
- **Campaign Scheduling**: Automated campaign lifecycle

### Technical Improvements
- **Microservices Architecture**: Split into smaller services
- **GraphQL API**: Alternative to REST endpoints
- **Redis Caching**: Improved performance
- **Elasticsearch**: Advanced search capabilities
- **Kubernetes**: Container orchestration
- **CI/CD Pipeline**: GitHub Actions/Jenkins
- **Performance Monitoring**: New Relic/DataDog integration
- **API Rate Limiting**: Prevent abuse

### UI/UX Enhancements
- **Custom Dashboard Builder**: Drag-and-drop widgets
- **Advanced Filters**: Save custom filter presets
- **Bulk Actions**: Select multiple campaigns
- **Keyboard Shortcuts**: Power user features
- **Data Export**: CSV/Excel/PDF reports
- **White-label Support**: Custom branding
- **Multi-language**: i18n support
- **Accessibility**: WCAG 2.1 AA compliance

## 🧪 Testing

### Backend Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

### Frontend Testing
```bash
# Run unit tests
npm test

# Run e2e tests
npm run e2e

# Generate coverage report
npm run test:coverage
```

## 📚 Documentation

- [API Documentation](./docs/API_DOCUMENTATION.md) - Complete API reference
- [Deployment Guide](./docs/DEPLOYMENT.md) - Step-by-step deployment instructions
- [Developer Guide](./docs/DEVELOPER_GUIDE.md) - Setup and contribution guidelines
- [Architecture Decisions](./docs/ADR.md) - Architectural decision records

## 🤝 Contributing

While this is a portfolio project, suggestions and feedback are welcome! Please feel free to:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Morgan Hondros**
- GitHub: [@mghondo](https://github.com/mghondo)
- LinkedIn: [Your LinkedIn Profile]
- Portfolio: [Your Portfolio Website]
- Email: your.email@example.com

## 🙏 Acknowledgments

- **CoreUI** - For the excellent admin template
- **Spring Boot Team** - For the amazing framework
- **Angular Team** - For the powerful frontend framework
- **AWS** - For reliable cloud infrastructure
- **PostgreSQL** - For the robust database system
- **Open Source Community** - For all the libraries and tools

## 📊 Project Status

🚧 **Current Phase**: Active Development

- [x] Project Setup and Planning
- [x] README Documentation
- [ ] Backend Implementation (In Progress)
- [ ] Frontend Implementation
- [ ] AWS Deployment
- [ ] Testing & Polish
- [ ] Production Launch

---

<p align="center">
  Built with ❤️ using Angular, Spring Boot, and AWS
  <br>
  <strong>A Portfolio Project by Morgan Hondros</strong>
</p>