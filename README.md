# 🍽️ MOC Restaurant Management System

Hệ thống quản lý nhà hàng toàn diện được xây dựng bằng Spring Boot, cung cấp giải pháp quản lý từ đặt bàn, đặt món đến thanh toán và thống kê doanh thu.

## 📋 Giới thiệu dự án

MOC Restaurant Management System là một hệ thống backend RESTful API được thiết kế để hỗ trợ vận hành nhà hàng hiện đại. Hệ thống tích hợp đầy đủ các tính năng từ quản lý menu, đơn hàng, nhân viên đến thanh toán online và hỗ trợ khách hàng thông qua AI Chatbot.

## ✨ Tính năng chính

### 👥 Quản lý người dùng & Phân quyền
- Hệ thống đăng nhập/đăng ký với JWT Authentication
- Phân quyền Role-Based Access Control (RBAC) với các vai trò:
  - **ADMIN**: Quyền quản trị toàn hệ thống
  - **MANAGER**: Quản lý menu, nguyên liệu, bàn, địa điểm
  - **DATA_ENTRY**: Nhập liệu menu và nguyên liệu
  - **CHEF**: Xem và cập nhật trạng thái đơn hàng
  - **STAFF**: Tạo đơn hàng tại quán
  - **CUSTOMER**: Đặt bàn và món online

### 📦 Quản lý Menu & Nguyên liệu
- CRUD menu items với phân loại theo danh mục
- Quản lý nguyên liệu (Ingredients) và đơn vị tính
- Upload và quản lý hình ảnh món ăn với Cloudinary
- Lọc và tìm kiếm menu items linh hoạt
- Quản lý trạng thái món ăn (available/unavailable)

### 🍴 Quản lý Đơn hàng
- Đặt món trực tuyến (Online Order)
- Đặt món tại quán (Staff Order)
- Đặt bàn trước (Reservation)
- Quản lý trạng thái đơn hàng theo thời gian thực
- Hỗ trợ nhiều loại đơn hàng: DINE_IN, TAKEAWAY, DELIVERY, RESERVATION
- Theo dõi đơn hàng theo từng bàn (Table)

### 💳 Thanh toán
- Tích hợp VNPay Payment Gateway
- Thanh toán online cho đơn hàng và đặt bàn
- Xử lý callback và cập nhật trạng thái thanh toán tự động

### 💬 AI Chatbot
- Tích hợp OpenAI GPT-4o-mini để hỗ trợ khách hàng
- Trả lời câu hỏi về menu, giá cả, và thông tin nhà hàng
- WebSocket hỗ trợ chat real-time

### 📊 Thống kê & Báo cáo
- Thống kê doanh thu theo giờ, ngày
- Đếm tổng số đơn hàng, khách hàng, món ăn
- Top món bán chạy
- Thống kê trạng thái đơn hàng trong ngày

### 🏢 Quản lý Đa cơ sở
- Quản lý nhiều địa điểm (Locations)
- Upload và quản lý hình ảnh cho mỗi cơ sở
- Quản lý bàn ăn theo từng địa điểm
- Thống kê theo từng cơ sở

### 🔔 Thông báo
- Hệ thống thông báo real-time
- Tích hợp Kafka để xử lý thông báo không đồng bộ

### 📧 Email Service
- Gửi email xác thực tài khoản
- Gửi email thông báo đơn hàng

## 🛠️ Công nghệ sử dụng

### Backend Framework
- **Spring Boot 3.x** - Framework chính
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM và Database Access
- **Spring WebSocket** - Real-time communication
- **Spring Mail** - Email service

### Database & Caching
- **MySQL 8.0** - Database chính
- **Redis** - Caching và session management
- **Hibernate** - ORM framework

### Message Queue
- **Apache Kafka** - Asynchronous message processing

### External Services
- **Cloudinary** - Image storage và CDN
- **VNPay** - Payment gateway
- **OpenAI API** - AI Chatbot integration

### Security
- **JWT (JSON Web Token)** - Stateless authentication
- **BCrypt** - Password encryption
- **CORS** - Cross-origin resource sharing

### Tools & Libraries
- **Lombok** - Code generation
- **MapStruct** - Object mapping
- **Jakarta Validation** - Input validation

## 🏗️ Kiến trúc hệ thống
          ┌───────────────────── Frontend App ─────────────────────┐
          │                     (React/Vue/Angular)               │
          └─────────────────────┬─────────────────────────────────┘
                                │
                                ▼
                           ┌─────────┐
                           │  API    │
                           └─────────┘
                                │
                                ▼
                 ┌─────────────────────────────┐
                 │       Spring Boot           │
                 │  (Controller, Service, Repo)│
                 └──────────┬──────────────────┘
                            │
         ┌──────────────────┼───────────────────┐
         ▼                  ▼                   ▼
     ┌─────────┐        ┌─────────┐         ┌─────────┐
     │ MySQL   │        │ Redis   │         │ Kafka   │
     └─────────┘        └─────────┘         └─────────┘
                            │
                            ▼
             ┌─────────────────────────┐
             │ External Services       │
             │ - Cloudinary            │
             │ - VNPay                 │
             │ - OpenAI API            │
             └─────────────────────────┘

## 📁 Cấu trúc dự án
┌───────────────────────── src/main/java/com/example/MocBE ─────────────────────────┐
│                                                                                 │
│ ┌───────────── config/ ─────────────┐  # Cấu hình (Security, Redis, Kafka, etc.) │
│ ├───────────── controller/ ─────────┤  # REST Controllers                        │
│ ├───────────── dto/ ─────────────────┤  # Data Transfer Objects                  │
│ │   ├── request/                     │  # Request DTOs                           │
│ │   └── response/                    │  # Response DTOs                          │
│ ├───────────── enums/ ───────────────┤  # Enumerations                           │
│ ├───────────── exception/ ──────────┤  # Exception handlers                       │
│ ├───────────── mapper/ ─────────────┤  # Object mappers                          │
│ ├───────────── model/ ──────────────┤  # Entity models                            │
│ ├───────────── repository/ ─────────┤  # Data access layer                        │
│ │   └── spec/                        │  # JPA Specifications                      │
│ ├───────────── security/ ────────────┤  # Security configurations                 │
│ ├───────────── service/ ────────────┤  # Business logic layer                    │
│ │   └── imp/                         │  # Service implementations                │
│ └───────────── util/ ────────────────┤  # Utility classes                          │
└─────────────────────────────────────────────────────────────────────────────────┘

