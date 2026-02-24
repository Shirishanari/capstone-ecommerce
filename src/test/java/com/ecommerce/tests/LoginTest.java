package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // ✅ Setup ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // ✅ Headless + CI friendly options
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
    public void teardown() {
        if (driver != null) driver.quit();
    }

    // ✅ 1. Open Website
    @Test
    public void openWebsite() {
        System.out.println("Website opened successfully: " + driver.getCurrentUrl());
    }

    // ✅ 2. Valid Login
    @Test
    public void testValidLogin() {
        // Navigate to Login page
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")
        )).click();

        // Enter Email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");

        // Enter Password
        driver.findElement(By.name("password")).sendKeys("password123");

        // Click Login button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Login') or contains(.,'Sign In') or @type='submit']")
        )).click();

        System.out.println("Login Test Passed");
    }

    // ✅ 3. Invalid Login
    @Test
    public void testInvalidLogin() {
        // Navigate to Login page
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")
        )).click();

        // Enter Email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");

        // Enter wrong password
        driver.findElement(By.name("password")).sendKeys("wrongpassword");

        // Click Login
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
        )).click();

        // ✅ Check still on login page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"));

        System.out.println("Invalid Login Test Passed");
    }
}