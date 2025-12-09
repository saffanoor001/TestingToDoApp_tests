package com.todo.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest extends BaseTest {

    @Test
    public void testUserRegistration() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        driver.get(baseUrl + "/register");
        
        driver.findElement(By.id("username")).sendKeys("testuser" + timestamp);
        driver.findElement(By.id("email")).sendKeys("test" + timestamp + "@example.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("register-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("success-message")));
        WebElement successMsg = driver.findElement(By.id("success-message"));
        assertTrue(successMsg.getText().contains("Registration successful"));
    }

    @Test
    public void testValidLogin() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "loginuser" + timestamp;
        registerUser(username, "login" + timestamp + "@test.com", "password123");
        
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("login-btn")).click();
        
        wait.until(ExpectedConditions.urlContains(baseUrl));
        assertTrue(driver.getCurrentUrl().equals(baseUrl + "/"));
    }

    @Test
    public void testInvalidLogin() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.id("username")).sendKeys("wronguser");
        driver.findElement(By.id("password")).sendKeys("wrongpass");
        driver.findElement(By.id("login-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("error-message")));
        WebElement errorMsg = driver.findElement(By.id("error-message"));
        assertEquals("Invalid username or password", errorMsg.getText());
    }

    @Test
    public void testUserLogout() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "logoutuser" + timestamp;
        registerUser(username, "logout" + timestamp + "@test.com", "password123");
        loginUser(username, "password123");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout-btn")));
        driver.findElement(By.id("logout-btn")).click();
        
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
        
        driver.get(baseUrl + "/");
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
