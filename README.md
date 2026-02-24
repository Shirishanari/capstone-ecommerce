# 🛍️ E-Commerce Full-Stack Application

A complete end-to-end e-commerce web application built with React.js, Node.js, Express.js, and MongoDB. This application simulates a real-world retail platform with user authentication, product search, shopping cart, checkout flow, and order management.

## 🚀 Tech Stack

### Frontend
- **React.js** (JavaScript) - UI library
- **Tailwind CSS** - Styling framework
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **React Toastify** - Toast notifications

### Backend
- **Node.js** - Runtime environment
- **Express.js** - Web framework
- **MongoDB** - Database
- **Mongoose** - ODM for MongoDB
- **JWT** - Authentication
- **bcryptjs** - Password hashing

## 📁 Project Structure

```
e-commerce/
├── backend/
│   ├── config/
│   │   └── db.js
│   ├── controllers/
│   │   ├── authController.js
│   │   ├── productController.js
│   │   ├── cartController.js
│   │   └── orderController.js
│   ├── middleware/
│   │   └── auth.js
│   ├── models/
│   │   ├── User.js
│   │   ├── Product.js
│   │   ├── Cart.js
│   │   └── Order.js
│   ├── routes/
│   │   ├── auth.js
│   │   ├── products.js
│   │   ├── cart.js
│   │   └── orders.js
│   ├── scripts/
│   │   └── seed.js
│   ├── .env.example
│   ├── .gitignore
│   ├── package.json
│   └── server.js
├── frontend/
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/
│   │   │   ├── Navbar.js
│   │   │   └── ProtectedRoute.js
│   │   ├── pages/
│   │   │   ├── Home.js
│   │   │   ├── Login.js
│   │   │   ├── Register.js
│   │   │   ├── ProductDetail.js
│   │   │   ├── Cart.js
│   │   │   ├── Checkout.js
│   │   │   └── OrderHistory.js
│   │   ├── utils/
│   │   │   ├── api.js
│   │   │   └── auth.js
│   │   ├── App.js
│   │   ├── index.js
│   │   └── index.css
│   ├── .env.example
│   ├── .gitignore
│   ├── package.json
│   ├── tailwind.config.js
│   └── postcss.config.js
└── README.md
```

## 🛠️ Setup Instructions

### Prerequisites
- Node.js (v14 or higher)
- MongoDB (local installation or MongoDB Atlas account)
- npm or yarn

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Create environment file:**
   ```bash
   cp .env.example .env
   ```

4. **Configure environment variables:**
   Edit `.env` file and set:
   ```env
   PORT=5000
   MONGODB_URI=mongodb://localhost:27017/ecommerce
   JWT_SECRET=your_super_secret_jwt_key_change_this_in_production
   NODE_ENV=development
   ```

5. **Start MongoDB:**
   - If using local MongoDB, ensure MongoDB service is running
   - If using MongoDB Atlas, update `MONGODB_URI` in `.env`

6. **Seed the database (optional):**
   ```bash
   npm run seed
   ```
   This will populate the database with sample products.

7. **Start the backend server:**
   ```bash
   npm start
   ```
   Or for development with auto-reload:
   ```bash
   npm run dev
   ```

   The backend server will run on `http://localhost:5000`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Create environment file:**
   ```bash
   cp .env.example .env
   ```

4. **Configure environment variables:**
   Edit `.env` file and set:
   ```env
   REACT_APP_API_URL=http://localhost:5000/api
   ```

5. **Start the frontend development server:**
   ```bash
   npm start
   ```

   The frontend will run on `http://localhost:3000`

## 📋 Features

### 1. User Authentication
- ✅ User registration with email uniqueness validation
- ✅ Password hashing using bcrypt
- ✅ Login with JWT authentication
- ✅ Logout functionality
- ✅ Protected routes
- ✅ Error handling for invalid credentials

### 2. Product Management
- ✅ Product listing page
- ✅ Search products by keyword
- ✅ Filter by category and price range
- ✅ Pagination support
- ✅ Product detail page
- ✅ Handle "no results found" scenario

### 3. Shopping Cart
- ✅ Add multiple items to cart
- ✅ Update item quantities
- ✅ Remove items from cart
- ✅ Auto price calculation (subtotal, tax, total)
- ✅ Cart persistence (backend)
- ✅ Stock validation

### 4. Checkout Flow
- ✅ Complete checkout process
- ✅ Address form with validation
- ✅ Mock payment gateway simulation
- ✅ Order creation
- ✅ Order confirmation

### 5. Order History
- ✅ View past orders (user-specific)
- ✅ Display products, total price, and status
- ✅ Order details page

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user (protected)

### Products
- `GET /api/products` - Get all products (with search, filter, pagination)
- `GET /api/products/categories` - Get all categories
- `GET /api/products/:id` - Get single product

### Cart (Protected)
- `GET /api/cart` - Get user cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/item/:itemId` - Update cart item quantity
- `DELETE /api/cart/item/:itemId` - Remove item from cart
- `DELETE /api/cart/clear` - Clear cart

### Orders (Protected)
- `POST /api/orders` - Create order
- `GET /api/orders` - Get user orders
- `GET /api/orders/:id` - Get single order

## 🧪 Test Cases

### Authentication Tests

#### 1. User Registration
- **Test Case:** Register with valid data
  - **Input:** `{ name: "John Doe", email: "john@example.com", password: "password123" }`
  - **Expected:** User created successfully, JWT token returned

- **Test Case:** Register with duplicate email
  - **Input:** `{ name: "Jane Doe", email: "john@example.com", password: "password123" }`
  - **Expected:** Error: "Email already exists"

- **Test Case:** Register with short password
  - **Input:** `{ name: "John Doe", email: "john@example.com", password: "123" }`
  - **Expected:** Error: "Password must be at least 6 characters"

#### 2. User Login
- **Test Case:** Login with valid credentials
  - **Input:** `{ email: "john@example.com", password: "password123" }`
  - **Expected:** Login successful, JWT token returned

- **Test Case:** Login with invalid credentials
  - **Input:** `{ email: "john@example.com", password: "wrongpassword" }`
  - **Expected:** Error: "Invalid credentials"

### Product Search Tests

#### 1. Search Products
- **Test Case:** Search by keyword
  - **Endpoint:** `GET /api/products?search=headphones`
  - **Expected:** Returns products matching "headphones"

- **Test Case:** Filter by category
  - **Endpoint:** `GET /api/products?category=Electronics`
  - **Expected:** Returns only Electronics products

- **Test Case:** Filter by price range
  - **Endpoint:** `GET /api/products?minPrice=50&maxPrice=150`
  - **Expected:** Returns products within price range

- **Test Case:** No results found
  - **Endpoint:** `GET /api/products?search=nonexistentproduct`
  - **Expected:** Empty array returned

### Cart Tests

#### 1. Add to Cart
- **Test Case:** Add item to cart
  - **Endpoint:** `POST /api/cart/add`
  - **Body:** `{ productId: "product_id", quantity: 2 }`
  - **Expected:** Item added to cart

- **Test Case:** Add item with insufficient stock
  - **Endpoint:** `POST /api/cart/add`
  - **Body:** `{ productId: "product_id", quantity: 1000 }`
  - **Expected:** Error: "Insufficient stock"

#### 2. Update Cart
- **Test Case:** Update item quantity
  - **Endpoint:** `PUT /api/cart/item/:itemId`
  - **Body:** `{ quantity: 5 }`
  - **Expected:** Cart item quantity updated

#### 3. Remove from Cart
- **Test Case:** Remove item
  - **Endpoint:** `DELETE /api/cart/item/:itemId`
  - **Expected:** Item removed from cart

### Checkout Flow Tests

#### 1. Create Order
- **Test Case:** Create order with valid address
  - **Endpoint:** `POST /api/orders`
  - **Body:** `{ address: { street: "123 Main St", city: "City", state: "State", zipCode: "12345", country: "Country" } }`
  - **Expected:** Order created, cart cleared

- **Test Case:** Create order with empty cart
  - **Endpoint:** `POST /api/orders`
  - **Expected:** Error: "Cart is empty"

- **Test Case:** Create order with incomplete address
  - **Endpoint:** `POST /api/orders`
  - **Body:** `{ address: { street: "123 Main St" } }`
  - **Expected:** Error: "Please provide complete address information"

## 📝 Postman Collection Example

You can test the API using Postman. Here's an example collection structure:

```json
{
  "info": {
    "name": "E-Commerce API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "http://localhost:5000/api/auth/register",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"John Doe\",\n  \"email\": \"john@example.com\",\n  \"password\": \"password123\"\n}"
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "http://localhost:5000/api/auth/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"email\": \"john@example.com\",\n  \"password\": \"password123\"\n}"
            }
          }
        }
      ]
    },
    {
      "name": "Products",
      "item": [
        {
          "name": "Get Products",
          "request": {
            "method": "GET",
            "url": "http://localhost:5000/api/products"
          }
        },
        {
          "name": "Search Products",
          "request": {
            "method": "GET",
            "url": "http://localhost:5000/api/products?search=headphones"
          }
        }
      ]
    }
  ]
}
```

## 🔒 Security Features

- Password hashing with bcrypt
- JWT token authentication
- Protected routes middleware
- Input validation
- Error handling middleware
- CORS configuration

## 🎨 UI Features

- Responsive design with Tailwind CSS
- Toast notifications for user feedback
- Loading states
- Form validation
- Error messages
- Protected routes

## 📦 Database Schema

### Users Collection
```javascript
{
  name: String (required),
  email: String (required, unique),
  password: String (required, hashed),
  createdAt: Date
}
```

### Products Collection
```javascript
{
  name: String (required),
  description: String (required),
  price: Number (required),
  category: String (required),
  stock: Number (required),
  image: String,
  createdAt: Date
}
```

### Cart Collection
```javascript
{
  userId: ObjectId (required, unique),
  items: [{
    productId: ObjectId (required),
    quantity: Number (required)
  }],
  updatedAt: Date
}
```

### Orders Collection
```javascript
{
  userId: ObjectId (required),
  items: [{
    productId: ObjectId (required),
    quantity: Number (required),
    price: Number (required)
  }],
  totalPrice: Number (required),
  address: {
    street: String (required),
    city: String (required),
    state: String (required),
    zipCode: String (required),
    country: String (required)
  },
  status: String (enum: ['Pending', 'Completed', 'Cancelled']),
  createdAt: Date
}
```

## 🚦 Running the Application

1. **Start MongoDB** (if using local installation)
2. **Start Backend:**
   ```bash
   cd backend
   npm install
   npm run seed  # Optional: seed database
   npm start
   ```
3. **Start Frontend:**
   ```bash
   cd frontend
   npm install
   npm start
   ```
4. **Open browser:** Navigate to `http://localhost:3000`

## 📝 Notes

- The application uses a mock payment gateway - no real payments are processed
- Tax is calculated at 10% of subtotal
- JWT tokens expire after 7 days
- Cart is stored in the database (not localStorage)
- Stock is automatically decremented when an order is placed

## 🤝 Contributing

This is a demonstration project. Feel free to fork and modify as needed.

## 📄 License

This project is open source and available for educational purposes.
