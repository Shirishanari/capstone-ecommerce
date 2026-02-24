package com.ecommerce.tests;

public class TestUtil {
    // allow overrides via environment variable for CI or local runs
    public static final String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://localhost:3000/");
}
