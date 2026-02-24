package com.ecommerce.tests;

import java.time.Duration;

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
        driver.get("http://localhost:3000/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void teardown() {
        if(driver != null) driver.quit();
    }

    public void addItemToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add to Cart')]"))).click();
    }

    // 🛒 1. Add Single Item to Cart
    @Test
    public void testAddSingleItem() {
        addItemToCart();
        Assert.assertTrue(driver.getPageSource().contains("Cart"));
    }

    // 🛒 2. Add Multiple Items to Cart
    @Test
    public void testAddMultipleItems() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(text(),'Add to Cart')])[1]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(text(),'Add to Cart')])[2]"))).click();

        Assert.assertTrue(driver.getPageSource().contains("2"));
    }

    // 3. Update Quantity
    @Test
    public void testUpdateQuantity() {
        addItemToCart();

        By qtyInput = By.xpath("//input[@type='number']");
        WebElement quantity = wait.until(
                ExpectedConditions.visibilityOfElementLocated(qtyInput));

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
        addItemToCart();
        Assert.assertTrue(true); // Placeholder for API integration
    }

    // 5. Add Same Item Twice
    @Test
    public void testAddSameItemTwice() {
        addItemToCart();
        addItemToCart();
        Assert.assertTrue(driver.getPageSource().contains("2"));
    }

    // 6. Enter Invalid Quantity (Negative)
    @Test
    public void testInvalidQuantity() {
        addItemToCart();

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
        Assert.assertTrue(driver.getPageSource().contains("Cart"));
    }
}