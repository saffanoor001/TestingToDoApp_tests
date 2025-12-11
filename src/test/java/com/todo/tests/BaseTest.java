package com.todo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl = "http://13.51.205.213:8000";

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void registerUser(String username, String email, String password) {
        driver.get(baseUrl + "/register");
        driver.findElement(org.openqa.selenium.By.id("username")).sendKeys(username);
        driver.findElement(org.openqa.selenium.By.id("email")).sendKeys(email);
        driver.findElement(org.openqa.selenium.By.id("password")).sendKeys(password);
        driver.findElement(org.openqa.selenium.By.id("register-btn")).click();
    }

    protected void loginUser(String username, String password) {
        driver.get(baseUrl + "/login");
        driver.findElement(org.openqa.selenium.By.id("username")).sendKeys(username);
        driver.findElement(org.openqa.selenium.By.id("password")).sendKeys(password);
        driver.findElement(org.openqa.selenium.By.id("login-btn")).click();
    }
}
