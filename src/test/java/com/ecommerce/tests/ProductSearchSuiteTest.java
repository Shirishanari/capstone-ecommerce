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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ProductSearchSuiteTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // ✅ WebDriverManager setup
        WebDriverManager.chromedriver().setup();

        // ✅ Headless + CI friendly Chrome
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
        if(driver != null) driver.quit();
    }

    // ✅ 1. Search Product by Keyword
    @Test
    public void testSearchByKeyword() {
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']"))
        );

        searchBox.clear();
        searchBox.sendKeys("Laptop");
        searchBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Laptop') or contains(text(),'No')]")
        ));

        List<WebElement> products = driver.findElements(
                By.xpath("//*[contains(text(),'Laptop')]")
        );

        Assert.assertTrue(products.size() > 0, "No products found for keyword search");
    }

    // ✅ 2. Filter by Category
    @Test
    public void testFilterByCategory() {
        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//select"))
        );

        Select select = new Select(dropdown);
        select.selectByVisibleText("Electronics");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Electronics')]")
        ));

        List<WebElement> products = driver.findElements(
                By.xpath("//*[contains(text(),'Electronics')]")
        );

        Assert.assertTrue(products.size() > 0, "No products found for selected category");
    }

    // ✅ 4. No Results Found
    @Test
    public void testNoResultsFound() {
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']"))
        );

        searchBox.clear();
        searchBox.sendKeys("InvalidProduct123");
        searchBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        List<WebElement> products = driver.findElements(
                By.xpath("//div[contains(@class,'card') or contains(@class,'product')]")
        );

        Assert.assertTrue(products.isEmpty(), "Products displayed for invalid search");
    }

    // ✅ 5. Case Insensitive Search
    @Test
    public void testCaseInsensitiveSearch() {
        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']"))
        );

        searchBox.clear();
        searchBox.sendKeys("pHoNe");
        searchBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(translate(text(),'PHONE','phone'),'phone')]")
        ));

        List<WebElement> products = driver.findElements(
                By.xpath("//*[contains(translate(text(),'PHONE','phone'),'phone')]")
        );

        Assert.assertTrue(products.size() > 0, "Case insensitive search failed");
    }

    // ✅ 6. Min Price Filter
    @Test
    public void testMinPriceFilter() {
        WebElement min = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@name,'min')]"))
        );

        min.clear();
        min.sendKeys("1000", Keys.ENTER);

        List<WebElement> prices = driver.findElements(
                By.xpath("//span[contains(@class,'price')]")
        );

        for (WebElement price : prices) {
            String amount = price.getText().replaceAll("[^0-9]", "");
            if (!amount.isEmpty()) {
                int value = Integer.parseInt(amount);
                Assert.assertTrue(value >= 1000, "Product below min price found");
            }
        }
    }
    

    // ✅ 7. Max Price Filter
    @Test
    public void testMaxPriceFilter() {
        WebElement max = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//input[contains(@name,'max')]"))
        );

        max.clear();
        max.sendKeys("1500", Keys.ENTER);

        List<WebElement> prices = driver.findElements(
                By.xpath("//span[contains(@class,'price')]")
        );

        for (WebElement price : prices) {
            String amount = price.getText().replaceAll("[^0-9]", "");
            if (!amount.isEmpty()) {
                int value = Integer.parseInt(amount);
                Assert.assertTrue(value <= 1500, "Product above max price found");
            }
        }
    }
}