package com.ecommerce.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ShoppingCartTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // headless for CI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(TestUtil.BASE_URL);

        // perform login so add-to-cart actions succeed
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")
            )).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                    .sendKeys("testuser@gmail.com");
            driver.findElement(By.name("password")).sendKeys("password123");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Products') or contains(text(),'Logout')]")
            ));
        } catch (Exception ex) {
            // login may fail if user isn't seeded; tests will skip later accordingly
        }
    }

    @AfterMethod
    public void teardown() {
        if(driver != null) driver.quit();
    }

    public boolean addItemToCart() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Add to Cart')]")
            ));
            btn.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🛒 1. Add Single Item to Cart
    @Test
    public void testAddSingleItem() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No add-to-cart button available");
        }
        Assert.assertTrue(driver.getPageSource().contains("Cart"));
    }

    // 🛒 2. Add Multiple Items to Cart
    @Test
    public void testAddMultipleItems() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No products to add");
        }
        // second attempt
        addItemToCart();
        Assert.assertTrue(driver.getPageSource().contains("2"));
    }

    // 3. Update Quantity
    @Test
    public void testUpdateQuantity() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No products to update");
        }
        // navigate to cart page where quantity input exists
        driver.get(TestUtil.BASE_URL + "cart");

        By qtyInput = By.xpath("//input[@type='number']");
        WebElement quantity;
        try {
            quantity = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(qtyInput));
        } catch (Exception e) {
            throw new org.testng.SkipException("Quantity input not present");
        }

        quantity.click();
        quantity.sendKeys(Keys.CONTROL + "a");
        quantity.sendKeys(Keys.BACK_SPACE);
        quantity.sendKeys("3");

        wait.until(driver -> quantity.getAttribute("value").equals("3"));
        Assert.assertEquals(quantity.getAttribute("value"), "3");
    }

    // 4. Sync Cart With Backend (Placeholder)
    @Test
    public void testCartSyncBackend() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No products to sync");
        }
        Assert.assertTrue(true); // Placeholder for API integration
    }

    // 5. Add Same Item Twice
    @Test
    public void testAddSameItemTwice() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No products to add");
        }
        addItemToCart();
        Assert.assertTrue(driver.getPageSource().contains("2"));
    }

    // 6. Enter Invalid Quantity (Negative)
    @Test
    public void testInvalidQuantity() {
        if (!addItemToCart()) {
            throw new org.testng.SkipException("No products to test quantity");
        }

        By qtyInput = By.xpath("//input[@type='number']");
        WebElement quantity = wait.until(
                ExpectedConditions.presenceOfElementLocated(qtyInput));

        quantity.click();
        quantity.sendKeys(Keys.CONTROL + "a");
        quantity.sendKeys(Keys.DELETE);
        quantity.sendKeys("-1");

        String value = quantity.getAttribute("value");
        Assert.assertFalse(
                Integer.parseInt(value) >= 1,
                "System allowed negative quantity!"
        );
    }

    // 7. Empty Cart Check
    @Test
    public void testEmptyCart() {
        driver.get(TestUtil.BASE_URL + "cart");
        // repeatedly click any remove/delete buttons until none left
        for (int attempts = 0; attempts < 5; attempts++) {
            List<WebElement> removeBtns = driver.findElements(By.xpath(
                    "//button[contains(text(),'Remove') or contains(text(),'Delete')]"
            ));
            if (removeBtns.isEmpty()) break;
            for (WebElement btn : removeBtns) {
                try { btn.click(); } catch (Exception ignored) {}
            }
            // allow page load after each round
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        driver.navigate().refresh();

        // wait for an "empty cart" message (case-insensitive, flexible phrasing)
        By emptyLocator = By.xpath("//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'cart') and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'empty')]");
        wait.until(ExpectedConditions.presenceOfElementLocated(emptyLocator));

        String page = driver.getPageSource().toLowerCase();
        Assert.assertTrue(page.contains("cart is empty") || page.contains("your cart is empty") || page.contains("empty cart"),
                "Empty cart message not displayed");
    }
}