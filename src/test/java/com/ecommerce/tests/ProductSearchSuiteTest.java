package com.ecommerce.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductSearchSuiteTest {

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

    // ✅ 1. Search Product by Keyword
    @Test
    public void testSearchByKeyword() {

        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@type='text']")
                )
        );

        searchBox.clear();
        searchBox.sendKeys("Laptop");
        searchBox.sendKeys(Keys.ENTER);

        // Wait for results to update
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Laptop') or contains(text(),'No')]")
        ));

        List<WebElement> products = driver.findElements(
                By.xpath("//*[contains(text(),'Laptop')]")
        );

        Assert.assertTrue(products.size() > 0,
                "No products found for keyword search");

        System.out.println("Search by Keyword Test Passed");
    }

    // ✅ 2. Filter by Category
    @Test
    public void testFilterByCategory() {

        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//select")
                )
        );

        Select select = new Select(dropdown);
        select.selectByVisibleText("Electronics");

        // Wait for category results
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Electronics')]")
        ));

        List<WebElement> products = driver.findElements(
                By.xpath("//*[contains(text(),'Electronics')]")
        );

        Assert.assertTrue(products.size() > 0,
                "No products found for selected category");

        System.out.println("Category Filter Test Passed");
    }

    // ✅ 3. Filter by Price Range
    @Test
    public void testFilterByPriceRange() throws InterruptedException {

        WebElement min = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[contains(@placeholder,'Min') or contains(@name,'min')]")
                )
        );

        WebElement max = driver.findElement(
                By.xpath("//input[contains(@placeholder,'Max') or contains(@name,'max')]")
        );

        min.clear();
        min.sendKeys("1000");

        max.clear();
        max.sendKeys("5000");
        max.sendKeys(Keys.ENTER);

        // Wait for price update
        Thread.sleep(2000); // temporary wait for React render

        List<WebElement> prices = driver.findElements(
                By.xpath("//*[contains(text(),'₹') or contains(text(),'$')]")
        );

        for (WebElement price : prices) {
            String priceText = price.getText().replaceAll("[^0-9]", "");
            if (!priceText.isEmpty()) {
                int actualPrice = Integer.parseInt(priceText);
                Assert.assertTrue(actualPrice >= 1000 && actualPrice <= 5000,
                        "Price out of selected range");
            }
        }

        System.out.println("Price Range Filter Test Passed");
    }

   @Test
    public void testNoResultsFound() {

    WebElement searchBox = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@type='text']")
            )
    );

    searchBox.clear();
    searchBox.sendKeys("InvalidProduct123");
    searchBox.sendKeys(Keys.ENTER);

    // Small wait for React rendering
    try { Thread.sleep(2000); } catch (InterruptedException e) {}

    List<WebElement> products = driver.findElements(
            By.xpath("//div[contains(@class,'card') or contains(@class,'product')]")
    );

    System.out.println("Products found: " + products.size());

    Assert.assertTrue(products.isEmpty(),
            "Products are displayed even for invalid search");

    System.out.println("No Results Scenario Test Passed");
}

@Test
public void testSearchNoResults() {

    WebElement searchBox = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']"))
    );

    searchBox.clear();
    searchBox.sendKeys("asdhjkasdhjkashd"); // random string
    searchBox.sendKeys(Keys.ENTER);

    List<WebElement> products = driver.findElements(
            By.xpath("//*[contains(text(),'asdhjkasdhjkashd')]")
    );

    Assert.assertTrue(products.size() == 0,
            "Unexpected results found for invalid search");

    System.out.println("Search No Results Test Passed");
}
 

@Test
public void testMinPriceFilter() {

    WebElement min = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[contains(@name,'min')]"))
    );

    min.clear();
    min.sendKeys("1000");
    min.sendKeys(Keys.ENTER);

    // Validate price elements
    List<WebElement> prices = driver.findElements(
            By.xpath("//span[contains(@class,'price')]")
    );

    for (WebElement price : prices) {
        String amount = price.getText().replaceAll("[^0-9]", "");
        if (!amount.isEmpty()) {
            int value = Integer.parseInt(amount);
            Assert.assertTrue(value >= 1000,
                    "Product below min price found");
        }
    }

    System.out.println("Minimum Price Filter Test Passed");
}
 
@Test
public void testMaxPriceFilter() {

    WebElement max = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[contains(@name,'max')]"))
    );

    max.clear();
    max.sendKeys("1500");
    max.sendKeys(Keys.ENTER);

    List<WebElement> prices = driver.findElements(
            By.xpath("//span[contains(@class,'price')]")
    );

    for (WebElement price : prices) {
        String amount = price.getText().replaceAll("[^0-9]", "");
        if (!amount.isEmpty()) {
            int value = Integer.parseInt(amount);
            Assert.assertTrue(value <= 1500,
                    "Product above max price found");
        }
    }

    System.out.println("Maximum Price Filter Test Passed");
}
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

    Assert.assertTrue(products.size() > 0,
            "Case insensitive search failed");

    System.out.println("Case Insensitive Search Test Passed");
}


}