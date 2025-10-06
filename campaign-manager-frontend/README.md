# Campaign Manager Frontend

A modern Marketing Campaign Management Platform built with Angular 17 and CoreUI.

## Project Overview

This is the frontend component of the Marketing Campaign Management Platform - a comprehensive solution for managing marketing campaigns with analytics, reporting, and campaign lifecycle management.

## Tech Stack

- **Angular 17** - Latest Angular framework with standalone components
- **CoreUI Angular** - Professional admin template and UI components
- **TypeScript** - Type-safe JavaScript development
- **SCSS** - Enhanced CSS with variables and mixins
- **RxJS** - Reactive programming with Observables
- **Angular Router** - Client-side routing and navigation

## Additional Dependencies

- **Chart.js** + **ng2-charts** - Data visualization and charts
- **ngx-toastr** - Toast notifications
- **@coreui/icons** - Professional icon set

## Project Structure

```
src/
├── app/
│   ├── core/                    # Core application modules
│   │   ├── guards/             # Route guards
│   │   ├── interceptors/       # HTTP interceptors
│   │   ├── models/            # Data models and interfaces
│   │   └── services/          # Core services
│   ├── features/              # Feature modules
│   │   ├── auth/              # Authentication (login/register)
│   │   ├── campaigns/         # Campaign management
│   │   └── dashboard/         # Main dashboard
│   ├── layouts/               # Application layouts
│   │   └── default-layout/    # Main application layout with sidebar
│   ├── shared/                # Shared components and utilities
│   │   ├── components/        # Reusable components
│   │   ├── directives/        # Custom directives
│   │   └── pipes/            # Custom pipes
│   ├── app.component.*        # Root application component
│   ├── app.config.ts         # Application configuration
│   └── app.routes.ts         # Application routing
├── environments/              # Environment configurations
│   ├── environment.ts        # Development environment
│   └── environment.prod.ts   # Production environment
├── styles.scss               # Global styles
└── main.ts                   # Application bootstrap
```

## Getting Started

### Prerequisites

- Node.js 20.x or higher
- npm 10.x or higher
- Angular CLI 20.x

### Installation

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Install Angular CLI globally (if not already installed):**
   ```bash
   npm install -g @angular/cli
   ```

### Development

1. **Start the development server:**
   ```bash
   npm start
   # or
   ng serve
   ```

2. **Navigate to:**
   ```
   http://localhost:4200
   ```

The application will automatically reload when you change any of the source files.

### Available Scripts

- `npm start` or `ng serve` - Start development server
- `npm run build` or `ng build` - Build the project
- `npm test` or `ng test` - Run unit tests
- `npm run watch` or `ng build --watch` - Build in watch mode

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

For production build:
```bash
ng build --configuration production
```

### Testing

Run `ng test` to execute the unit tests via Karma.

## Environment Configuration

The application uses environment-specific configurations:

- **Development:** `src/environments/environment.ts`
  - API URL: `http://localhost:8080/api`
  - Production: `false`

- **Production:** `src/environments/environment.prod.ts`
  - API URL: `https://your-production-api-url.com/api`
  - Production: `true`

## Features Implemented

### ✅ Phase 1 - Foundation (Completed)

- [x] Angular 17 project setup with standalone components
- [x] CoreUI integration with professional admin template
- [x] Project folder structure following Angular best practices
- [x] Environment configuration for development and production
- [x] Basic layout with sidebar navigation
- [x] Routing configuration with lazy loading
- [x] Placeholder components for:
  - Dashboard with welcome message and feature overview
  - Campaigns list with sample data table
  - Authentication (Login/Register) with reactive forms

### 🚧 Planned Features (Phase 2+)

- [ ] User authentication and authorization
- [ ] Campaign CRUD operations
- [ ] Campaign analytics dashboard with charts
- [ ] Campaign status management
- [ ] Performance metrics and reporting
- [ ] Responsive design optimization
- [ ] API integration with backend services
- [ ] Advanced filtering and search
- [ ] Export functionality
- [ ] User management
- [ ] Real-time notifications

## Application Routes

- `/` - Redirects to dashboard
- `/dashboard` - Main dashboard (within default layout)
- `/campaigns` - Campaign management (within default layout)
- `/auth/login` - User login (standalone)
- `/auth/register` - User registration (standalone)

## CoreUI Components Used

- **Layout:** Container, Grid, Cards, Sidebar, Header, Footer
- **Forms:** Form controls, Input groups, Buttons
- **Navigation:** Sidebar navigation with icons
- **Data Display:** Tables, Cards, Badges
- **Responsive:** Bootstrap-based responsive grid system

## Development Guidelines

### Code Style

- Follow Angular style guide
- Use TypeScript strict mode
- Implement reactive forms with validation
- Use standalone components (Angular 17 approach)
- Organize imports and follow consistent naming conventions

### Component Structure

- Use standalone components with explicit imports
- Implement proper lifecycle hooks
- Use reactive programming patterns with RxJS
- Follow single responsibility principle

### State Management

- Use services for shared state
- Implement proper error handling
- Use reactive forms for user input

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Create feature branches from `main`
2. Follow the established project structure
3. Write meaningful commit messages
4. Test your changes before submitting
5. Update documentation as needed

## License

This project is part of the Marketing Campaign Management Platform.

---

**Ready for Phase 2 Development!** 🚀

The foundation is now complete with Angular 17, CoreUI integration, and all basic components in place. Ready to implement campaign management features, authentication, and backend integration.
