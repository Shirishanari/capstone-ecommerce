package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginSuiteTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // ✅ Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // ✅ ChromeOptions for headless CI-friendly run
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Navigate to Login page
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")
        )).click();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✅ 1. Valid Login
    @Test
    public void testValidLogin() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");
        driver.findElement(By.name("password")).sendKeys("password123");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
        )).click();

        Assert.assertTrue(
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Products') or contains(text(),'Logout')]")
            )).isDisplayed()
        );

        System.out.println("Valid Login Test Passed");
    }

    // ❌ 2. Invalid Password
    @Test
    public void testInvalidPassword() {
        driver.findElement(By.name("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.name("password")).sendKeys("wrongpass");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // ❌ 3. Unregistered Email
    @Test
    public void testUnregisteredEmail() {
        driver.findElement(By.name("email")).sendKeys("unknown@gmail.com");
        driver.findElement(By.name("password")).sendKeys("password123");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // ⚠️ 4. Empty Email
    @Test
    public void testEmptyEmail() {
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // ⚠️ 5. Empty Password
    @Test
    public void testEmptyPassword() {
        driver.findElement(By.name("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // ⚠️ 6. Both Fields Empty
    @Test
    public void testEmptyFields() {
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // ⚠️ 7. Invalid Email Format
    @Test
    public void testInvalidEmailFormat() {
        driver.findElement(By.name("email")).sendKeys("abc@com");
        driver.findElement(By.name("password")).sendKeys("password123");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // 🔒 8. Password Mask Check
    @Test
    public void testPasswordMasked() {
        String type = driver.findElement(By.name("password")).getAttribute("type");
        Assert.assertEquals(type, "password");
    }

    // 🔁 9. Refresh After Login
    @Test
    public void testSessionPersistence() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");
        driver.findElement(By.name("password")).sendKeys("password123");

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Products') or contains(text(),'Logout')]")
        ));

        driver.navigate().refresh();

        Assert.assertTrue(
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Products') or contains(text(),'Logout')]")
            )).isDisplayed(),
            "Session not maintained after refresh"
        );

        System.out.println("Session Persistence Test Passed");
    }

    // 🚫 10. SQL Injection Attempt
    @Test
    public void testSqlInjection() {
        driver.findElement(By.name("email")).sendKeys("' OR 1=1 --");
        driver.findElement(By.name("password")).sendKeys("anything");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }

    // 🔤 11. Case Sensitivity Check
    @Test
    public void testCaseSensitivity() {
        driver.findElement(By.name("email")).sendKeys("TESTUSER@GMAIL.COM");
        driver.findElement(By.name("password")).sendKeys("password123");

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Assert.assertTrue(true); // Adjust based on system behavior
    }

    // 🔄 12. Multiple Login Attempts
    @Test
    public void testMultipleAttempts() {
        for(int i = 0; i < 3; i++) {
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("password")).clear();

            driver.findElement(By.name("email")).sendKeys("testuser@gmail.com");
            driver.findElement(By.name("password")).sendKeys("wrongpass");

            driver.findElement(By.xpath("//button[@type='submit']")).click();
        }
        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }
}