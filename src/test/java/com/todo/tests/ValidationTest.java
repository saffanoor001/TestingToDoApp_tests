package com.todo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest extends BaseTest {

    @Test
    public void testRegistrationValidation() {
        driver.get(baseUrl + "/register");
        
        driver.findElement(By.id("username")).sendKeys("user");
        driver.findElement(By.id("email")).sendKeys("invalidemail");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.id("register-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("error-message")));
        WebElement errorMsg = driver.findElement(By.id("error-message"));
        assertTrue(errorMsg.isDisplayed());
    }

    @Test
    public void testLoginValidation() {
        driver.get(baseUrl + "/login");
        
        driver.findElement(By.id("login-btn")).click();
        
        WebElement usernameField = driver.findElement(By.id("username"));
        String validationMessage = usernameField.getAttribute("validationMessage");
        assertFalse(validationMessage.isEmpty());
    }

    @Test
    public void testSearchFunctionality() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "searchuser" + timestamp;
        registerUser(username, "search" + timestamp + "@test.com", "password123");
        loginUser(username, "password123");
        wait.until(ExpectedConditions.urlContains(baseUrl + "/"));
        
        driver.findElement(By.id("title")).sendKeys("Search Test Todo");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-item")));
        
        driver.findElement(By.id("title")).sendKeys("Another Todo");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("todo-item"), 2));
        
        driver.findElement(By.id("search-input")).sendKeys("Search Test");
        driver.findElement(By.id("search-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-item")));
        int searchResults = driver.findElements(By.className("todo-item")).size();
        assertEquals(1, searchResults);
        
        WebElement result = driver.findElement(By.className("todo-item"));
        assertTrue(result.getText().contains("Search Test Todo"));
    }

    @Test
    public void testEmptyTodoValidation() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "validuser" + timestamp;
        registerUser(username, "valid" + timestamp + "@test.com", "password123");
        loginUser(username, "password123");
        wait.until(ExpectedConditions.urlContains(baseUrl + "/"));
        
        driver.findElement(By.id("add-btn")).click();
        
        WebElement titleField = driver.findElement(By.id("title"));
        String validationMessage = titleField.getAttribute("validationMessage");
        assertFalse(validationMessage.isEmpty());
    }
}
