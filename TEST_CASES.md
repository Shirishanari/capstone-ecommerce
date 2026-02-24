# 🧪 Test Cases Documentation

This document provides comprehensive test cases for the E-Commerce application.

## 📋 Table of Contents
1. [Authentication Tests](#authentication-tests)
2. [Product Search & Filtering Tests](#product-search--filtering-tests)
3. [Shopping Cart Tests](#shopping-cart-tests)
4. [Checkout Flow Tests](#checkout-flow-tests)
5. [Order History Tests](#order-history-tests)

---

## 🔐 Authentication Tests

### Test Case 1.1: User Registration - Valid Data
**Description:** Register a new user with valid information

**Steps:**
1. Send POST request to `/api/auth/register`
2. Body: `{ "name": "John Doe", "email": "john@example.com", "password": "password123" }`

**Expected Result:**
- Status Code: 201
- Response contains user object (without password)
- Response contains JWT token
- User is created in database

**Postman Collection:**
```json
POST http://localhost:5000/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

---

### Test Case 1.2: User Registration - Duplicate Email
**Description:** Attempt to register with an existing email

**Steps:**
1. Register a user (use Test Case 1.1)
2. Attempt to register again with the same email

**Expected Result:**
- Status Code: 400
- Error message: "User with this email already exists" or "Email already exists"

---

### Test Case 1.3: User Registration - Short Password
**Description:** Attempt to register with password less than 6 characters

**Steps:**
1. Send POST request to `/api/auth/register`
2. Body: `{ "name": "Jane Doe", "email": "jane@example.com", "password": "123" }`

**Expected Result:**
- Status Code: 400
- Error message: "Password must be at least 6 characters"

---

### Test Case 1.4: User Registration - Missing Fields
**Description:** Attempt to register without required fields

**Steps:**
1. Send POST request to `/api/auth/register`
2. Body: `{ "name": "John Doe" }` (missing email and password)

**Expected Result:**
- Status Code: 400
- Error message: "Please provide name, email, and password"

---

### Test Case 1.5: User Login - Valid Credentials
**Description:** Login with correct email and password

**Steps:**
1. Register a user first (use Test Case 1.1)
2. Send POST request to `/api/auth/login`
3. Body: `{ "email": "john@example.com", "password": "password123" }`

**Expected Result:**
- Status Code: 200
- Response contains user object
- Response contains JWT token

**Postman Collection:**
```json
POST http://localhost:5000/api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

---

### Test Case 1.6: User Login - Invalid Credentials
**Description:** Login with incorrect password

**Steps:**
1. Register a user first
2. Send POST request to `/api/auth/login`
3. Body: `{ "email": "john@example.com", "password": "wrongpassword" }`

**Expected Result:**
- Status Code: 401
- Error message: "Invalid credentials"

---

### Test Case 1.7: User Login - Non-existent User
**Description:** Login with email that doesn't exist

**Steps:**
1. Send POST request to `/api/auth/login`
2. Body: `{ "email": "nonexistent@example.com", "password": "password123" }`

**Expected Result:**
- Status Code: 401
- Error message: "Invalid credentials"

---

### Test Case 1.8: Get Current User - Authenticated
**Description:** Get current user information with valid token

**Steps:**
1. Login to get JWT token
2. Send GET request to `/api/auth/me`
3. Header: `Authorization: Bearer <token>`

**Expected Result:**
- Status Code: 200
- Response contains user object (without password)

---

### Test Case 1.9: Get Current User - Unauthenticated
**Description:** Attempt to get current user without token

**Steps:**
1. Send GET request to `/api/auth/me`
2. No Authorization header

**Expected Result:**
- Status Code: 401
- Error message: "Authentication required. Please login."

---

## 🔍 Product Search & Filtering Tests

### Test Case 2.1: Get All Products
**Description:** Retrieve all products without filters

**Steps:**
1. Send GET request to `/api/products`

**Expected Result:**
- Status Code: 200
- Response contains array of products
- Response contains pagination information

**Postman Collection:**
```json
GET http://localhost:5000/api/products
```

---

### Test Case 2.2: Search Products by Keyword
**Description:** Search products using keyword

**Steps:**
1. Send GET request to `/api/products?search=headphones`

**Expected Result:**
- Status Code: 200
- Response contains products matching "headphones" in name or description

**Postman Collection:**
```json
GET http://localhost:5000/api/products?search=headphones
```

---

### Test Case 2.3: Filter Products by Category
**Description:** Filter products by specific category

**Steps:**
1. Send GET request to `/api/products?category=Electronics`

**Expected Result:**
- Status Code: 200
- All returned products have category "Electronics"

**Postman Collection:**
```json
GET http://localhost:5000/api/products?category=Electronics
```

---

### Test Case 2.4: Filter Products by Price Range
**Description:** Filter products within price range

**Steps:**
1. Send GET request to `/api/products?minPrice=50&maxPrice=150`

**Expected Result:**
- Status Code: 200
- All returned products have price between 50 and 150

**Postman Collection:**
```json
GET http://localhost:5000/api/products?minPrice=50&maxPrice=150
```

---

### Test Case 2.5: Combined Search and Filters
**Description:** Use multiple filters together

**Steps:**
1. Send GET request to `/api/products?search=watch&category=Electronics&minPrice=100&maxPrice=300`

**Expected Result:**
- Status Code: 200
- Products match all criteria

---

### Test Case 2.6: No Results Found
**Description:** Search for non-existent product

**Steps:**
1. Send GET request to `/api/products?search=nonexistentproductxyz`

**Expected Result:**
- Status Code: 200
- Empty products array returned

---

### Test Case 2.7: Pagination
**Description:** Test pagination functionality

**Steps:**
1. Send GET request to `/api/products?page=1&limit=5`
2. Send GET request to `/api/products?page=2&limit=5`

**Expected Result:**
- Status Code: 200
- First request returns first 5 products
- Second request returns next 5 products
- Pagination metadata is correct

**Postman Collection:**
```json
GET http://localhost:5000/api/products?page=1&limit=5
```

---

### Test Case 2.8: Get Single Product
**Description:** Retrieve details of a single product

**Steps:**
1. Get a product ID from products list
2. Send GET request to `/api/products/:id`

**Expected Result:**
- Status Code: 200
- Response contains single product object with all details

---

### Test Case 2.9: Get Product - Invalid ID
**Description:** Attempt to get product with invalid ID

**Steps:**
1. Send GET request to `/api/products/invalidid123`

**Expected Result:**
- Status Code: 404 or 500
- Error message indicates product not found

---

### Test Case 2.10: Get Categories
**Description:** Retrieve all available categories

**Steps:**
1. Send GET request to `/api/products/categories`

**Expected Result:**
- Status Code: 200
- Response contains array of unique categories

**Postman Collection:**
```json
GET http://localhost:5000/api/products/categories
```

---

## 🛒 Shopping Cart Tests

### Test Case 3.1: Get Cart - Empty Cart
**Description:** Get cart for new user (should be empty)

**Steps:**
1. Login to get token
2. Send GET request to `/api/cart`
3. Header: `Authorization: Bearer <token>`

**Expected Result:**
- Status Code: 200
- Cart exists but items array is empty

**Postman Collection:**
```json
GET http://localhost:5000/api/cart
Authorization: Bearer <token>
```

---

### Test Case 3.2: Add Item to Cart
**Description:** Add a product to cart

**Steps:**
1. Login to get token
2. Get a product ID
3. Send POST request to `/api/cart/add`
4. Body: `{ "productId": "<product_id>", "quantity": 2 }`
5. Header: `Authorization: Bearer <token>`

**Expected Result:**
- Status Code: 200
- Item added to cart
- Cart contains the new item

**Postman Collection:**
```json
POST http://localhost:5000/api/cart/add
Authorization: Bearer <token>
Content-Type: application/json

{
  "productId": "product_id_here",
  "quantity": 2
}
```

---

### Test Case 3.3: Add Item to Cart - Insufficient Stock
**Description:** Attempt to add more items than available in stock

**Steps:**
1. Login to get token
2. Get a product with limited stock
3. Send POST request to `/api/cart/add`
4. Body: `{ "productId": "<product_id>", "quantity": 10000 }`

**Expected Result:**
- Status Code: 400
- Error message: "Insufficient stock"

---

### Test Case 3.4: Add Item to Cart - Invalid Product ID
**Description:** Attempt to add non-existent product

**Steps:**
1. Login to get token
2. Send POST request to `/api/cart/add`
3. Body: `{ "productId": "invalid_id", "quantity": 1 }`

**Expected Result:**
- Status Code: 404
- Error message: "Product not found"

---

### Test Case 3.5: Add Same Item Twice
**Description:** Add the same product to cart multiple times

**Steps:**
1. Add item to cart (use Test Case 3.2)
2. Add the same product again with quantity 1

**Expected Result:**
- Status Code: 200
- Quantity is increased (not duplicate item)

---

### Test Case 3.6: Update Cart Item Quantity
**Description:** Update quantity of item in cart

**Steps:**
1. Add item to cart
2. Get cart item ID
3. Send PUT request to `/api/cart/item/:itemId`
4. Body: `{ "quantity": 5 }`

**Expected Result:**
- Status Code: 200
- Item quantity updated to 5

**Postman Collection:**
```json
PUT http://localhost:5000/api/cart/item/:itemId
Authorization: Bearer <token>
Content-Type: application/json

{
  "quantity": 5
}
```

---

### Test Case 3.7: Remove Item from Cart
**Description:** Remove an item from cart

**Steps:**
1. Add item to cart
2. Get cart item ID
3. Send DELETE request to `/api/cart/item/:itemId`

**Expected Result:**
- Status Code: 200
- Item removed from cart

**Postman Collection:**
```json
DELETE http://localhost:5000/api/cart/item/:itemId
Authorization: Bearer <token>
```

---

### Test Case 3.8: Clear Cart
**Description:** Remove all items from cart

**Steps:**
1. Add multiple items to cart
2. Send DELETE request to `/api/cart/clear`

**Expected Result:**
- Status Code: 200
- Cart items array is empty

**Postman Collection:**
```json
DELETE http://localhost:5000/api/cart/clear
Authorization: Bearer <token>
```

---

### Test Case 3.9: Cart Operations - Unauthenticated
**Description:** Attempt cart operations without authentication

**Steps:**
1. Send GET request to `/api/cart` without token

**Expected Result:**
- Status Code: 401
- Error message: "Authentication required"

---

## 💳 Checkout Flow Tests

### Test Case 4.1: Create Order - Valid Data
**Description:** Create order with valid address and items in cart

**Steps:**
1. Login and add items to cart
2. Send POST request to `/api/orders`
3. Body:
```json
{
  "address": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  }
}
```
4. Header: `Authorization: Bearer <token>`

**Expected Result:**
- Status Code: 201
- Order created successfully
- Cart is cleared
- Product stock is decremented
- Order contains correct items and total price

**Postman Collection:**
```json
POST http://localhost:5000/api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "address": {
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  }
}
```

---

### Test Case 4.2: Create Order - Empty Cart
**Description:** Attempt to create order with empty cart

**Steps:**
1. Login (ensure cart is empty)
2. Send POST request to `/api/orders` with valid address

**Expected Result:**
- Status Code: 400
- Error message: "Cart is empty"

---

### Test Case 4.3: Create Order - Incomplete Address
**Description:** Attempt to create order with missing address fields

**Steps:**
1. Login and add items to cart
2. Send POST request to `/api/orders`
3. Body: `{ "address": { "street": "123 Main St" } }`

**Expected Result:**
- Status Code: 400
- Error message: "Please provide complete address information"

---

### Test Case 4.4: Create Order - Insufficient Stock
**Description:** Attempt to create order when product stock is insufficient

**Steps:**
1. Login and add item with quantity exceeding stock
2. Create order

**Expected Result:**
- Status Code: 400
- Error message: "Insufficient stock for [product name]"

---

### Test Case 4.5: Order Total Calculation
**Description:** Verify order total includes tax

**Steps:**
1. Add items to cart (note subtotal)
2. Create order
3. Verify total = subtotal + 10% tax

**Expected Result:**
- Order total is correctly calculated
- Tax is 10% of subtotal

---

## 📜 Order History Tests

### Test Case 5.1: Get User Orders
**Description:** Retrieve all orders for authenticated user

**Steps:**
1. Login to get token
2. Create some orders
3. Send GET request to `/api/orders`
4. Header: `Authorization: Bearer <token>`

**Expected Result:**
- Status Code: 200
- Response contains array of user's orders
- Orders sorted by date (newest first)

**Postman Collection:**
```json
GET http://localhost:5000/api/orders
Authorization: Bearer <token>
```

---

### Test Case 5.2: Get Single Order
**Description:** Retrieve details of a specific order

**Steps:**
1. Login and create an order
2. Get order ID
3. Send GET request to `/api/orders/:id`

**Expected Result:**
- Status Code: 200
- Response contains complete order details
- Order includes populated product information

**Postman Collection:**
```json
GET http://localhost:5000/api/orders/:orderId
Authorization: Bearer <token>
```

---

### Test Case 5.3: Get Order - Other User's Order
**Description:** Attempt to access another user's order

**Steps:**
1. Login as User A and create order
2. Login as User B
3. Attempt to get User A's order

**Expected Result:**
- Status Code: 404
- Error message: "Order not found"

---

### Test Case 5.4: Get Orders - Unauthenticated
**Description:** Attempt to get orders without authentication

**Steps:**
1. Send GET request to `/api/orders` without token

**Expected Result:**
- Status Code: 401
- Error message: "Authentication required"

---

### Test Case 5.5: Order Status
**Description:** Verify order status is set correctly

**Steps:**
1. Create an order
2. Check order status

**Expected Result:**
- Order status is "Pending" by default

---

## 🔄 End-to-End Flow Test

### Test Case 6.1: Complete Purchase Flow
**Description:** Test the complete flow from registration to order

**Steps:**
1. Register a new user
2. Login
3. Browse products (search/filter)
4. Add products to cart
5. View cart
6. Update quantities
7. Proceed to checkout
8. Enter address
9. Place order
10. View order history
11. View order details

**Expected Result:**
- All steps complete successfully
- Data persists correctly
- User experience is smooth

---

## 📊 Test Coverage Summary

| Module | Test Cases | Status |
|--------|-----------|--------|
| Authentication | 9 | ✅ |
| Product Search | 10 | ✅ |
| Shopping Cart | 9 | ✅ |
| Checkout Flow | 5 | ✅ |
| Order History | 5 | ✅ |
| End-to-End | 1 | ✅ |
| **Total** | **39** | ✅ |

---

## 🚀 Running Tests

### Using Postman
1. Import the Postman collection (create from examples above)
2. Set environment variables:
   - `base_url`: `http://localhost:5000/api`
   - `token`: (will be set after login)
3. Run tests in sequence

### Manual Testing
1. Start backend server: `cd backend && npm start`
2. Use Postman or curl to test endpoints
3. Verify responses match expected results

### Frontend Testing
1. Start frontend: `cd frontend && npm start`
2. Test UI flows manually
3. Verify API calls in browser DevTools Network tab

---

## 📝 Notes

- All protected routes require JWT token in Authorization header
- Token format: `Bearer <token>`
- Replace `<token>` and `<product_id>` with actual values
- Ensure MongoDB is running before testing
- Seed database before testing: `npm run seed` in backend directory
