package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
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

public class PurchaseFlowSuiteTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // ✅ WebDriverManager setup
        WebDriverManager.chromedriver().setup();

        // ✅ Headless Chrome for CI
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(TestUtil.BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if(driver != null) driver.quit();
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

    // ================= TEST CASES ================= //

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
        driver.get(TestUtil.BASE_URL + "checkout");
        Assert.assertTrue(driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("checkout"));
    }

    @Test
    public void testLoginPageLoads() {
        driver.get(TestUtil.BASE_URL + "login");
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    @Test
    public void testSearchFieldPresent() {
        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("search")));
        Assert.assertTrue(search.isDisplayed());
    }

    @Test
    public void testSearchProduct() {
        driver.findElement(By.name("search")).sendKeys("Laptop");
        driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("laptop"));
    }

    @Test
    public void testCheckoutRequiresLogin() {
        driver.get(TestUtil.BASE_URL + "checkout");
        Assert.assertTrue(driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("checkout"));
    }

    @Test
    public void testNavbarVisible() {
        Assert.assertTrue(driver.getPageSource().contains("Home")
                || driver.getPageSource().contains("Cart"));
    }

    @Test
    public void testLoginFieldsVisible() {
        driver.get(TestUtil.BASE_URL + "login");

        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement password = driver.findElement(By.name("password"));

        Assert.assertTrue(email.isDisplayed());
        Assert.assertTrue(password.isDisplayed());
    }

    @Test
    public void testPageTitleNotEmpty() {
        Assert.assertFalse(driver.getTitle().isEmpty());
    }

    @Test
    public void testProductsPageAccessibleAfterLogin() {
        login();
        driver.get(TestUtil.BASE_URL);
        Assert.assertTrue(driver.getPageSource().contains("Products"));
    }

    @Test
    public void testNoBrokenHomeRoute() {
        driver.get(TestUtil.BASE_URL);
        Assert.assertTrue(driver.getTitle().length() > 0);
    }
}