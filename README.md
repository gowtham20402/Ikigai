# Parcel Delivery Management System

A comprehensive web-based parcel delivery management system built with **Angular** (frontend), **Spring Boot** (backend), and **Derby** (database).

## Features Overview

### 🔐 Authentication & Authorization
- User registration for customers and officers
- Secure login with JWT tokens
- Role-based access control (Customer/Officer)
- Password encryption with industry-standard hashing

### 👤 Customer Features
- **Registration**: Self-register with personal details, contact info, and preferences
- **Booking Service**: Create parcel delivery bookings with cost calculation
- **Payment**: Secure card payment processing with validation
- **Tracking**: Track parcel status using booking ID
- **Previous Bookings**: View booking history with filters and pagination
- **Feedback**: Provide feedback for delivered parcels
- **Booking Cancellation**: Cancel bookings with appropriate status validation

### 👮 Officer Features
- **Booking Management**: Create bookings on behalf of customers
- **Tracking**: Track any parcel using booking ID with full customer details
- **Pickup Scheduling**: Update pickup and delivery times
- **Delivery Status**: Update parcel status throughout delivery lifecycle
- **View All Bookings**: Access all customer bookings with advanced filters
- **Booking Cancellation**: Cancel customer bookings with refund processing
- **Feedback Management**: View all customer feedback

### 💰 Cost Calculation
- **Dynamic Pricing**: Real-time cost calculation based on:
  - Base Rate: ₹50
  - Weight Charge: ₹0.02 per gram
  - Delivery Type: Standard (₹30), Express (₹80), Same-Day (₹150)
  - Packing: Basic (₹10), Premium (₹30)
  - Admin Fee: ₹50 (for officer bookings)
  - Tax: 5%

### 📊 Advanced Features
- **Pagination**: Efficient data handling for large datasets
- **Filtering**: Advanced search and filter options
- **Export**: Download booking data in Excel/PDF format
- **Invoice Generation**: Automatic invoice creation with payment details
- **Status Management**: Complete parcel lifecycle tracking
- **Responsive Design**: Modern, mobile-friendly UI with Bootstrap

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security with JWT
- **Database**: Apache Derby (Embedded)
- **ORM**: Spring Data JPA/Hibernate
- **Validation**: Bean Validation (JSR-303)
- **Documentation**: Built-in API documentation

### Frontend
- **Framework**: Angular 17
- **UI Library**: Bootstrap 5.3
- **HTTP Client**: Angular HttpClient with interceptors
- **Routing**: Angular Router with guards
- **Forms**: Reactive Forms with validation
- **State Management**: Services with RxJS

### Database Schema
- **Users**: Customer and officer information
- **Bookings**: Parcel booking details with relationships
- **Payments**: Payment transactions and invoices
- **Feedback**: Customer feedback for delivered parcels

## Quick Start

### Prerequisites
- **Java 17+**
- **Node.js 18+**
- **Maven 3.6+**
- **Angular CLI 17+**

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd parcel-delivery-system
   ```

2. **Run the Spring Boot application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

3. **API Documentation**
   - Base URL: `http://localhost:8080/api`
   - Authentication endpoints: `/api/auth/*`
   - Customer endpoints: `/api/customer/*`
   - Officer endpoints: `/api/officer/*`
   - Common endpoints: `/api/common/*`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   # or
   ng serve
   ```
   
   The frontend will start on `http://localhost:4200`

### Default Access

The application will automatically create an in-memory Derby database. You can register new users or create default accounts for testing.

**Sample Test Data** (Register these users):
- **Customer**: Register through the registration form
- **Officer**: Use the officer registration endpoint or create via backend

## API Endpoints

### Authentication
- `POST /api/auth/register` - Customer registration
- `POST /api/auth/register-officer` - Officer registration
- `POST /api/auth/login` - User login

### Customer Endpoints
- `POST /api/customer/bookings` - Create booking
- `GET /api/customer/bookings` - Get customer bookings
- `POST /api/customer/bookings/{id}/cancel` - Cancel booking

### Officer Endpoints
- `POST /api/officer/bookings` - Create booking for customer
- `GET /api/officer/bookings` - Get all bookings
- `PUT /api/officer/bookings/{id}/status` - Update booking status
- `PUT /api/officer/bookings/{id}/schedule` - Update pickup schedule

### Common Endpoints
- `GET /api/common/bookings/{id}` - Get booking by ID
- `POST /api/common/calculate-cost` - Calculate shipping cost

## Features Implementation Status

### ✅ Completed Features
1. **Backend Infrastructure**
   - Spring Boot application with Derby database
   - Entity models (User, Booking, Payment, Feedback)
   - JWT authentication and authorization
   - REST APIs for registration and login
   - Booking service APIs with cost calculation

2. **Frontend Infrastructure**
   - Angular project setup with routing
   - Authentication service and HTTP interceptor
   - Login component with form validation
   - Modern responsive UI with Bootstrap

### 🚧 In Progress
- Registration component
- Customer and Officer dashboards
- Booking management components

### 📋 Pending Features
- Payment processing APIs
- Tracking and status management
- Invoice generation
- Feedback system
- Export functionality (Excel/PDF)

## Database Configuration

The application uses Apache Derby embedded database with the following configuration:
- **URL**: `jdbc:derby:memory:parceldb;create=true`
- **Mode**: In-memory (data persists during application lifecycle)
- **Schema**: Auto-created using JPA/Hibernate DDL

For production, you can easily switch to other databases by updating the `application.properties` file.

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt hashing for password security
- **Role-based Access**: Endpoint protection based on user roles
- **CORS Configuration**: Properly configured for frontend integration
- **Input Validation**: Comprehensive validation on all endpoints

## Error Handling

- **Global Exception Handling**: Consistent error responses
- **Validation Errors**: Detailed field-level validation messages
- **HTTP Status Codes**: Proper status codes for different scenarios
- **User-friendly Messages**: Clear error messages for frontend display

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions, please create an issue in the repository or contact the development team.

---

**Built with ❤️ using Angular, Spring Boot, and Derby Database**