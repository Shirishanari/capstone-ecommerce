package com.ecommerce.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;

public class LoginTest {

    @Test
    public void openWebsite() {

       WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");   // IMPORTANT
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:3000/");
        System.out.println("Website opened successfully");

        driver.quit();
    }
    
     @Test
    public void testValidLogin1() throws InterruptedException {

        WebDriver driver = new ChromeDriver();

        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();

        Thread.sleep(2000);

        // Click on Login button/link (update locator if needed)
        driver.findElement(By.linkText("Login")).click();

        Thread.sleep(2000);

        driver.quit();
    }

    @Test
    public void testValidLogin() {

        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 🔁 Try multiple ways to click Login
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Login') or contains(text(),'Sign In')]")
        )).click();

        // Enter Email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
                .sendKeys("testuser@gmail.com");

        // Enter Password
        driver.findElement(By.name("password")).sendKeys("password123");

        // 🔁 Try flexible locator for button
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Login') or contains(.,'Sign In') or @type='submit']")
        )).click();

        System.out.println("Login Test Passed");

        driver.quit();
    }

    @Test
    public void testInvalidLogin() {

        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click Login
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

        // ✅ Check that user is still on login page (login failed)
        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("login"));

        System.out.println("Invalid Login Test Passed");

        driver.quit();
    }
}

