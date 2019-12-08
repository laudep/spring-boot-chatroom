package edu.udacity.java.nano;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
    classes = WebSocketChatApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebSocketChatApplicationTest {

  public static final String LOGIN_PAGE_TITLE = "Chat Room Login";
  public static final String CHAT_PAGE_TITLE = "Chat Room";
  private static boolean HEADLESS_MODE = true;
  private static boolean USE_FIREFOX = true;
  private static WebDriver firstDriver;
  private static WebDriver secondDriver;

  @BeforeClass
  public static void setupClass() {
    if (USE_FIREFOX) {
      setupFirefox();
    } else {
      setupChrome();
    }
  }

  private static void setupChrome() {
    WebDriverManager.chromedriver().setup();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ChromeOptions options = new ChromeOptions();
    if (HEADLESS_MODE) {
      options.addArguments("--headless");
      options.addArguments("--disable-gpu");
    }
    firstDriver = new ChromeDriver(options);
    secondDriver = new ChromeDriver(options);
  }

  private static void setupFirefox() {
    WebDriverManager.firefoxdriver().setup();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    FirefoxOptions options = new FirefoxOptions();
    if (HEADLESS_MODE) {
      FirefoxBinary firefoxBinary = new FirefoxBinary();
      firefoxBinary.addCommandLineOptions("--headless");
      options.setBinary(firefoxBinary);
    }

    firstDriver = new FirefoxDriver(options);
    secondDriver = new FirefoxDriver(options);
  }

  @AfterClass
  public static void teardown() {
    closeDriver(firstDriver);
    closeDriver(secondDriver);
  }

  private static void closeDriver(WebDriver driver) {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  public void test1LoginPageLoad() {
    loadStartPage(firstDriver);
    loadStartPage(secondDriver);
  }

  private void loadStartPage(WebDriver driver) {
    driver.get("http://localhost:8080/");
    while (!((JavascriptExecutor) driver)
        .executeScript("return document.readyState")
        .equals("complete")) ;
    Assert.assertTrue(driver.getTitle().equalsIgnoreCase(LOGIN_PAGE_TITLE));
  }

  @Test
  public void test2Login() {
    performLogin(firstDriver, "firstUser");
    performLogin(secondDriver, "secondUser");
  }

  private void performLogin(WebDriver driver, String username) {
    Assert.assertTrue(driver.getTitle().equalsIgnoreCase(LOGIN_PAGE_TITLE));
    WebElement usernameElement = driver.findElement(By.id("username"));
    usernameElement.sendKeys(username);

    WebElement loginElement = driver.findElement(By.tagName("a"));
    loginElement.click();

    while (!((JavascriptExecutor) driver)
        .executeScript("return document.readyState")
        .equals("complete")) ;
    Assert.assertTrue(driver.getTitle().equalsIgnoreCase(CHAT_PAGE_TITLE));
  }

  @Test
  public void test3Chat() {
    final String FIRST_MESSAGE = "This is test message one.";
    final String SECOND_MESSAGE = "This is test message two.";

    sendMessage(firstDriver, FIRST_MESSAGE);
    sendMessage(secondDriver, SECOND_MESSAGE);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    checkMessageDisplayed(firstDriver, FIRST_MESSAGE);
    checkMessageDisplayed(secondDriver, FIRST_MESSAGE);
    checkMessageDisplayed(firstDriver, SECOND_MESSAGE);
    checkMessageDisplayed(secondDriver, SECOND_MESSAGE);
  }

  private void sendMessage(WebDriver driver, String messageText) {
    WebElement inputElement = driver.findElement(By.id("msg"));
    inputElement.sendKeys(messageText);

    WebElement sendButtonElement = driver.findElement(By.id("sendMessage"));
    sendButtonElement.click();
  }

  private void checkMessageDisplayed(WebDriver driver, String messageText) {
    WebElement container = driver.findElement(By.id("messageContainer"));
    List<WebElement> content = container.findElements(By.className("message-content"));
    StringBuilder messageContent = new StringBuilder();

    for (WebElement contentElement : content) {
      messageContent.append(contentElement.getText());
    }

    Assert.assertEquals(true, messageContent.toString().contains(messageText));
  }

  @Test
  public void test4Logout() {
    performLogout(firstDriver);
    performLogout(secondDriver);
  }

  private void performLogout(WebDriver driver) {
    WebElement logoutElement = driver.findElement(By.tagName("a"));
    logoutElement.click();

    while (!((JavascriptExecutor) driver)
        .executeScript("return document.readyState")
        .equals("complete")) ;
    Assert.assertTrue(driver.getTitle().equalsIgnoreCase(LOGIN_PAGE_TITLE));
  }
}
