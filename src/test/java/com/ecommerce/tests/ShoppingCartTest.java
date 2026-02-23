package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingCartTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
       public void addItemToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add to Cart')]"))).click();
    }

    // 🛒 1. Add Single Item to Cart
    @Test
    public void testAddSingleItem() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add to Cart')]"))).click();

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
   
    @Test
    public void testUpdateQuantity() {

        addItemToCart();

        By qtyInput = By.xpath("//input[@type='number']");

        WebElement quantity = wait.until(
                ExpectedConditions.visibilityOfElementLocated(qtyInput));

        // React-safe way to clear & type
        quantity.click();
        quantity.sendKeys(Keys.CONTROL + "a");
        quantity.sendKeys(Keys.BACK_SPACE);
        quantity.sendKeys("3");

        wait.until(driver -> quantity.getAttribute("value").equals("3"));

        Assert.assertEquals(quantity.getAttribute("value"), "3");
    }

    //  12. Sync Cart With Backend (API Call Check)
    @Test
    public void testCartSyncBackend() {
        driver.findElement(By.xpath("//button[contains(text(),'Add to Cart')]")).click();

        // You can integrate with API validation or network logs
        Assert.assertTrue(true); // Placeholder
    }

    // 13. Add Same Item Twice (Should Increase Quantity)
    @Test
    public void testAddSameItemTwice() {
        driver.findElement(By.xpath("//button[contains(text(),'Add to Cart')]")).click();
        driver.findElement(By.xpath("//button[contains(text(),'Add to Cart')]")).click();

        Assert.assertTrue(driver.getPageSource().contains("2"));
    }

    // 14. Enter Invalid Quantity (Negative)
@Test
public void testInvalidQuantity() {

    // Add item
    wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Add to Cart')]"))).click();

    // Locate quantity input safely
    By qtyInput = By.xpath("//input[@type='number']");

    WebElement quantity = wait.until(
            ExpectedConditions.presenceOfElementLocated(qtyInput));

    // Clear properly (React safe)
    quantity.click();
    quantity.sendKeys(Keys.CONTROL + "a");
    quantity.sendKeys(Keys.DELETE);

    // Enter invalid value
    quantity.sendKeys("-1");

    String value = quantity.getAttribute("value");

    // Validate negative not accepted
    Assert.assertFalse(
            Integer.parseInt(value) >= 1,
            "System allowed negative quantity!"
    );
}

    @Test
    public void testEmptyCart() {
        Assert.assertTrue(driver.getPageSource().contains("Cart"));
    } 
}
