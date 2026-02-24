const mongoose = require('mongoose');
require('dotenv').config();
const Product = require('../models/Product');

const products = [
  {
    name: 'Wireless Bluetooth Headphones',
    description: 'Premium noise-cancelling wireless headphones with 30-hour battery life',
    price: 129.99,
    category: 'Electronics',
    stock: 50,
    image: 'https://via.placeholder.com/300x300?text=Headphones'
  },
  {
    name: 'Smart Watch Pro',
    description: 'Feature-rich smartwatch with heart rate monitor and GPS',
    price: 249.99,
    category: 'Electronics',
    stock: 30,
    image: 'https://via.placeholder.com/300x300?text=Smart+Watch'
  },
  {
    name: 'Cotton T-Shirt',
    description: 'Comfortable 100% cotton t-shirt, available in multiple colors',
    price: 19.99,
    category: 'Clothing',
    stock: 100,
    image: 'https://via.placeholder.com/300x300?text=T-Shirt'
  },
  {
    name: 'Denim Jeans',
    description: 'Classic fit denim jeans, perfect for everyday wear',
    price: 49.99,
    category: 'Clothing',
    stock: 75,
    image: 'https://via.placeholder.com/300x300?text=Jeans'
  },
  {
    name: 'Running Shoes',
    description: 'Lightweight running shoes with cushioned sole for maximum comfort',
    price: 89.99,
    category: 'Footwear',
    stock: 60,
    image: 'https://via.placeholder.com/300x300?text=Running+Shoes'
  },
  {
    name: 'Leather Boots',
    description: 'Durable leather boots for all weather conditions',
    price: 119.99,
    category: 'Footwear',
    stock: 40,
    image: 'https://via.placeholder.com/300x300?text=Boots'
  },
  {
    name: 'Coffee Maker',
    description: 'Programmable coffee maker with thermal carafe',
    price: 79.99,
    category: 'Home & Kitchen',
    stock: 45,
    image: 'https://via.placeholder.com/300x300?text=Coffee+Maker'
  },
  {
    name: 'Stand Mixer',
    description: 'Professional stand mixer with multiple attachments',
    price: 299.99,
    category: 'Home & Kitchen',
    stock: 25,
    image: 'https://via.placeholder.com/300x300?text=Mixer'
  },
  {
    name: 'Yoga Mat',
    description: 'Non-slip yoga mat with carrying strap',
    price: 29.99,
    category: 'Sports',
    stock: 80,
    image: 'https://via.placeholder.com/300x300?text=Yoga+Mat'
  },
  {
    name: 'Dumbbell Set',
    description: 'Adjustable dumbbell set, 5-50 lbs per dumbbell',
    price: 199.99,
    category: 'Sports',
    stock: 35,
    image: 'https://via.placeholder.com/300x300?text=Dumbbells'
  },
  {
    name: 'Laptop Backpack',
    description: 'Water-resistant backpack with padded laptop compartment',
    price: 59.99,
    category: 'Accessories',
    stock: 55,
    image: 'https://via.placeholder.com/300x300?text=Backpack'
  },
  {
    name: 'Wireless Mouse',
    description: 'Ergonomic wireless mouse with long battery life',
    price: 24.99,
    category: 'Electronics',
    stock: 90,
    image: 'https://via.placeholder.com/300x300?text=Mouse'
  }
];

const seedProducts = async () => {
  try {
    await mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/ecommerce', {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });

    console.log('Connected to MongoDB');

    // Clear existing products
    await Product.deleteMany({});
    console.log('Cleared existing products');

    // Insert new products
    await Product.insertMany(products);
    console.log(`✅ Seeded ${products.length} products`);

    process.exit(0);
  } catch (error) {
    console.error('Error seeding products:', error);
    process.exit(1);
  }
};

seedProducts();
