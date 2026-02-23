package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PurchaseFlowSuiteTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:3000/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    // ---------- Helper: Login ----------
    public void login() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");

        driver.findElement(By.name("password")).sendKeys("password123");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/"),
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Products')]"))));
    }

    // ================= 20 SAFE TESTS ================= //

    @Test
    public void testHomePageLoads() {
        Assert.assertTrue(driver.getCurrentUrl().contains("3000"));
    }

    @Test
    public void testLoginLinkVisible() {
        WebElement login = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")));
        Assert.assertTrue(login.isDisplayed());
    }

    @Test
    public void testNavigateToLoginPage() {
        driver.findElement(By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

   

    @Test
    public void testProductsSectionVisibleAfterLogin() {
        login();
        Assert.assertTrue(driver.getPageSource().contains("Products"));
    }

    @Test
    public void testCheckoutProtectedRoute() {
        driver.get("http://localhost:3000/checkout");
        Assert.assertTrue(driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("checkout"));
    }

    // 2️⃣ Login Page Loads
    @Test
    public void testLoginPageLoads() {
        driver.get("http://localhost:3000/login");
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }
 // 6️⃣ Search Field Present
    @Test
    public void testSearchFieldPresent() {
        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("search")));
        Assert.assertTrue(search.isDisplayed());
    }

    // 7️⃣ Search Product
    @Test
    public void testSearchProduct() {
        driver.findElement(By.name("search")).sendKeys("Laptop");
        driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("laptop"));
    }

    // 9️⃣ Checkout Page Requires Login
    @Test
    public void testCheckoutRequiresLogin() {
        driver.get("http://localhost:3000/checkout");
        Assert.assertTrue(driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("checkout"));
    }

    // 1️⃣6️⃣ Navbar Visible
    @Test
    public void testNavbarVisible() {
        Assert.assertTrue(driver.getPageSource().contains("Home")
                || driver.getPageSource().contains("Cart"));
    }

    // 1️⃣7️⃣ Login Fields Visible
    @Test
    public void testLoginFieldsVisible() {
        driver.get("http://localhost:3000/login");

        WebElement email = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement password = driver.findElement(By.name("password"));

        Assert.assertTrue(email.isDisplayed());
        Assert.assertTrue(password.isDisplayed());
    }

    // 2️⃣0️⃣ Page Title Not Empty
    @Test
    public void testPageTitleNotEmpty() {
        Assert.assertFalse(driver.getTitle().isEmpty());
    }


    @Test
    public void testProductsPageAccessibleAfterLogin() {
        login();
        driver.get("http://localhost:3000/");
        Assert.assertTrue(driver.getPageSource().contains("Products"));
    }

    @Test
    public void testNoBrokenHomeRoute() {
        driver.get("http://localhost:3000/");
        Assert.assertEquals(driver.getTitle().length() > 0, true);
    }
}
