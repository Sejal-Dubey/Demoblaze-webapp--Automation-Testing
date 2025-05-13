package MavenTestNGProject.MavenTestNGProject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Alert;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileInputStream;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.testng.Reporter;

public class PurchaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentReports report;
    private ExtentTest test;
    private String baseUrl = "https://www.demoblaze.com/";
    private String excelFilePath = "C:\\Users\\Sejal\\eclipse-workspace\\MavenTestNGProject\\target\\test_data.xlsx";
    private JavascriptExecutor js;
    private Actions actions;

    @BeforeMethod
    public void setUp() {
        report = new ExtentReports("./Reports/DemoblazeTestReport.html", true);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        driver.get(baseUrl);
        Reporter.log("Test Environment Setup Completed");
    }

    @DataProvider(name = "Purchase")
    public Object[][] getTestData() throws Exception {
        FileInputStream fis = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("Purchase");
        
        int rowCount = sheet.getLastRowNum();
        Object[][] data = new Object[rowCount][13];
        
        for (int i = 0; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i + 1);
            if (row != null) {
                for (int j = 0; j < 13; j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                data[i][j] = cell.getStringCellValue().trim();
                                break;
                            case NUMERIC:
                                if (j == 4) {
                                    data[i][j] = String.format("%.0f", cell.getNumericCellValue());
                                } else {
                                    data[i][j] = String.valueOf((int)cell.getNumericCellValue());
                                }
                                break;
                            default:
                                data[i][j] = "N/A";
                        }
                    } else {
                        data[i][j] = "N/A";
                    }
                }
            }
        }
        workbook.close();
        fis.close();
        return data;
    }

    @Test(dataProvider = "Purchase")
    public void testDemoblaze(String testCase, String username, String password, 
                            String productName, String nameOnCard, String cardNumber,
                            String country, String city, String month, String year,
                            String category, String price, String message) throws Exception {
        test = report.startTest("Test Case: " + testCase);
        Thread.sleep(1500);
        
        switch (testCase) {
            case "TC01":
                testValidSignup(username, password);
                Reporter.log("TC01 - Valid Signup Test Completed Successfully");
                break;
            case "TC02":
                testSignupLoginAddToCart(username, password, productName);
                Reporter.log("TC02 - Signup, Login and Add to Cart Test Completed Successfully");
                break;
            case "TC03":
                testDuplicateSignup(username, password);
                Reporter.log("TC03 - Duplicate Signup Test Completed Successfully");
                break;
            case "TC04":
                testAdvancedCartManagement(username, password, productName, 
                                         nameOnCard, cardNumber, country, 
                                         city, month, year, price);
                Reporter.log("TC04 - Advanced Cart Management Test Completed Successfully");
                break;
            case "TC05":
                testCategoryNavigationAndContact(username, password, productName, 
                                               category, price, message);
                Reporter.log("TC05 - Category Navigation and Contact Test Completed Successfully");
                break;
            case "TC06":
                testCrossCategoryShopping(username, password, category, productName);
                Reporter.log("TC06 - Cross-Category Shopping Test Completed Successfully");
                break;
        }
    }

    private void testValidSignup(String username, String password) throws Exception {
        Thread.sleep(1000);
        WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
        js.executeScript("arguments[0].click();", signupButton);
        
        Thread.sleep(1000);
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        usernameField.clear();
        usernameField.sendKeys(username);
        
        WebElement passwordField = driver.findElement(By.id("sign-password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        WebElement signupSubmit = driver.findElement(By.xpath("//button[contains(text(),'Sign up')]"));
        js.executeScript("arguments[0].click();", signupSubmit);
        
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        
        if (alertText.contains("success") || alertText.contains("exist")) {
            test.log(LogStatus.PASS, "Signup test completed: " + alertText);
            Reporter.log("Valid Signup Test Result: " + alertText);
        } else {
            Reporter.log("Valid Signup Test Failed: " + alertText);
            Assert.fail("Unexpected alert message: " + alertText);
        }
        
        Thread.sleep(1000);
    }

    private void performLogin(String username, String password) throws Exception {
        Thread.sleep(1000);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login2")));
        js.executeScript("arguments[0].click();", loginButton);
        
        Thread.sleep(1000);
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
        usernameField.clear();
        usernameField.sendKeys(username);
        
        WebElement passwordField = driver.findElement(By.id("loginpassword"));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        WebElement loginSubmit = driver.findElement(By.xpath("//button[contains(text(),'Log in')]"));
        js.executeScript("arguments[0].click();", loginSubmit);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
        Thread.sleep(1000);
    }

    private void testSignupLoginAddToCart(String username, String password, String productName) throws Exception {
        testValidSignup(username, password);
        performLogin(username, password);
        
        if (!productName.equals("N/A")) {
            navigateToProductAndAdd(productName);
        }
        
        test.log(LogStatus.PASS, "Product added to cart successfully");
        Reporter.log("Signup, Login, and Add to Cart Test Result: Product added successfully");
    }

    private void testDuplicateSignup(String username, String password) throws Exception {
        Thread.sleep(1000);
        WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
        js.executeScript("arguments[0].click();", signupButton);
        
        Thread.sleep(1500);
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        usernameField.clear();
        usernameField.sendKeys(username);
        
        WebElement passwordField = driver.findElement(By.id("sign-password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        WebElement signupSubmit = driver.findElement(By.xpath("//button[contains(text(),'Sign up')]"));
        js.executeScript("arguments[0].click();", signupSubmit);
        
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        
        Thread.sleep(2000);
        
        WebElement closeButton = driver.findElement(By.xpath("//div[@id='signInModal']//button[contains(text(),'Close')]"));
        if (closeButton.isDisplayed()) {
            js.executeScript("arguments[0].click();", closeButton);
            Thread.sleep(1500);
        }
        
        signupButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signin2")));
        js.executeScript("arguments[0].click();", signupButton);
        
        Thread.sleep(1500);
        usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        usernameField.clear();
        usernameField.sendKeys(username);
        
        passwordField = driver.findElement(By.id("sign-password"));
        passwordField.clear();
        passwordField.sendKeys(password);
        
        signupSubmit = driver.findElement(By.xpath("//button[contains(text(),'Sign up')]"));
        js.executeScript("arguments[0].click();", signupSubmit);
        
        alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        
        Assert.assertEquals(alertText, "This user already exist.");
        test.log(LogStatus.PASS, "Duplicate signup test successful");
        Reporter.log("Duplicate Signup Test Result: User already exists verification successful");
        
        Thread.sleep(1000);
        closeButton = driver.findElement(By.xpath("//div[@id='signInModal']//button[contains(text(),'Close')]"));
        if (closeButton.isDisplayed()) {
            js.executeScript("arguments[0].click();", closeButton);
        }
    }

    private void testAdvancedCartManagement(String username, String password, 
            String productName, String nameOnCard, String cardNumber, String country, 
            String city, String month, String year, String expectedPrice) throws Exception {
        testValidSignup(username, password);
        performLogin(username, password);

        navigateToCategory("Phones");
        Thread.sleep(2000);
        
        js.executeScript("window.scrollTo(0, 0)");
        Thread.sleep(1000);
        
        String productXPath = String.format("//div[@class='card-block']//a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'%s')]",
                productName.toLowerCase());
        
        for (int i = 0; i < 5; i++) {
            js.executeScript("window.scrollBy(0, 300)");
            Thread.sleep(500);
            
            WebElement product = driver.findElement(By.xpath(productXPath));
            if (product.isDisplayed()) {
                js.executeScript("arguments[0].scrollIntoView(true);", product);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", product);
                break;
            }
        }

        Thread.sleep(1000);
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(),'Add to cart')]")));
        js.executeScript("arguments[0].click();", addToCartButton);
        
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        
        Thread.sleep(1000);
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur")));
        js.executeScript("arguments[0].click();", cartLink);
        
        completePurchase(nameOnCard, cardNumber, country, city, month, year);
        test.log(LogStatus.PASS, "Advanced cart management test successful");
        Reporter.log("Advanced Cart Management Test Result: Purchase completed successfully");
    }

    private void testCategoryNavigationAndContact(String username, String password, 
            String productName, String category, String price, String message) throws Exception {
        testValidSignup(username, password);
        performLogin(username, password);

        if (!category.equals("N/A")) {
            navigateToCategory(category);
        }
        
        Thread.sleep(1000);
        WebElement contactLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Contact")));
        js.executeScript("arguments[0].click();", contactLink);
        
        Thread.sleep(1000);
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("recipient-email")));
        emailField.sendKeys(username + "@test.com");
        
        WebElement nameField = driver.findElement(By.id("recipient-name"));
        nameField.sendKeys(username);
        
        WebElement messageField = driver.findElement(By.id("message-text"));
        messageField.sendKeys(message);
        
        WebElement sendButton = driver.findElement(By.xpath("//button[contains(text(),'Send message')]"));
        js.executeScript("arguments[0].click();", sendButton);

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertTrue(alert.getText().contains("Thanks"));
        alert.accept();

        test.log(LogStatus.PASS, "Category navigation and contact test successful");
        Reporter.log("Category Navigation and Contact Test Result: Message sent successfully");
    }

    private void testCrossCategoryShopping(String username, String password, 
            String category, String productName) throws Exception {
        testValidSignup(username, password);
        performLogin(username, password);

        if (!category.equals("N/A") && !productName.equals("N/A")) {
            navigateToCategory(category);
            Thread.sleep(2000);
            
            js.executeScript("window.scrollTo(0, 0)");
            Thread.sleep(1000);
            
            String productXPath = String.format("//div[@class='card-block']//a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'%s')]",
                    productName.toLowerCase());
            
            for (int i = 0; i < 5; i++) {
                js.executeScript("window.scrollBy(0, 300)");
                Thread.sleep(500);
                
                WebElement product = driver.findElement(By.xpath(productXPath));
                if (product.isDisplayed()) {
                    js.executeScript("arguments[0].scrollIntoView(true);", product);
                    Thread.sleep(500);
                    js.executeScript("arguments[0].click();", product);
                    break;
                }
            }
            
            Thread.sleep(1000);
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Add to cart')]")));
            js.executeScript("arguments[0].click();", addToCartButton);
            
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        }

        test.log(LogStatus.PASS, "Cross-category shopping test successful");
        Reporter.log("Cross-Category Shopping Test Result: Product added to cart from category " + category);
    }

    private void navigateToCategory(String category) throws Exception {
        Thread.sleep(1500);
        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(),'" + category + "')]")));
        js.executeScript("arguments[0].scrollIntoView(true);", categoryLink);
        Thread.sleep(500);
        js.executeScript("arguments[0].click();", categoryLink);
        Thread.sleep(1500);
    }

    private void navigateToProductAndAdd(String productName) throws Exception {
        Thread.sleep(1000);
        String productXPath = String.format("//div[@class='card-block']//a[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'%s')]",
                productName.toLowerCase());
        
        for (int i = 0; i < 5; i++) {
            js.executeScript("window.scrollBy(0, 300)");
            Thread.sleep(500);
            
            WebElement product = driver.findElement(By.xpath(productXPath));
            if (product.isDisplayed()) {
                js.executeScript("arguments[0].scrollIntoView(true);", product);
                Thread.sleep(500);
                js.executeScript("arguments[0].click();", product);
                break;
            }
        }
        
        Thread.sleep(1000);
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(),'Add to cart')]")));
        js.executeScript("arguments[0].click();", addToCartButton);
        
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }

    private void completePurchase(String nameOnCard, String cardNumber, String country, 
            String city, String month, String year) throws Exception {
        Thread.sleep(1000);
        WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Place Order')]")));
        js.executeScript("arguments[0].click();", placeOrderButton);
        
        Thread.sleep(1000);
        WebElement purchaseForm = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("orderModal")));
            
        fillPurchaseForm(purchaseForm, nameOnCard, cardNumber, country, city, month, year);
        
        WebElement purchaseButton = driver.findElement(By.xpath("//button[contains(text(),'Purchase')]"));
        js.executeScript("arguments[0].click();", purchaseButton);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h2[contains(text(),'Thank you')]")));
        
        WebElement okButton = driver.findElement(By.xpath("//button[contains(text(),'OK')]"));
        js.executeScript("arguments[0].click();", okButton);
    }

    private void fillPurchaseForm(WebElement form, String nameOnCard, String cardNumber, 
            String country, String city, String month, String year) {
        if (!nameOnCard.equals("N/A")) form.findElement(By.id("name")).sendKeys(nameOnCard);
        if (!country.equals("N/A")) form.findElement(By.id("country")).sendKeys(country);
        if (!city.equals("N/A")) form.findElement(By.id("city")).sendKeys(city);
        if (!cardNumber.equals("N/A")) form.findElement(By.id("card")).sendKeys(cardNumber);
        if (!month.equals("N/A")) form.findElement(By.id("month")).sendKeys(month);
        if (!year.equals("N/A")) form.findElement(By.id("year")).sendKeys(year);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        report.endTest(test);
        report.flush();
        Reporter.log("Test Execution Completed");
    }
}