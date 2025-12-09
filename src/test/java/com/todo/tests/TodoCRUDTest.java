package com.todo.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodoCRUDTest extends BaseTest {

    @BeforeEach
    public void loginBeforeTest() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String username = "todouser" + timestamp;
        registerUser(username, "todo" + timestamp + "@test.com", "password123");
        loginUser(username, "password123");
        wait.until(ExpectedConditions.urlContains(baseUrl + "/"));
    }

    @Test
    public void testCreateTodo() {
        driver.findElement(By.id("title")).sendKeys("Buy groceries");
        driver.findElement(By.id("description")).sendKeys("Milk, Bread, Eggs");
        driver.findElement(By.id("add-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-item")));
        List<WebElement> todos = driver.findElements(By.className("todo-item"));
        assertTrue(todos.size() > 0);
        assertTrue(todos.get(0).getText().contains("Buy groceries"));
    }

    @Test
    public void testViewTodos() {
        driver.findElement(By.id("title")).sendKeys("Test Todo 1");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("todo-item")));
        
        driver.findElement(By.id("title")).sendKeys("Test Todo 2");
        driver.findElement(By.id("add-btn")).click();
        
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("todo-item"), 2));
        List<WebElement> todos = driver.findElements(By.className("todo-item"));
        assertEquals(2, todos.size());
    }

    @Test
    public void testUpdateTodo() {
        driver.findElement(By.id("title")).sendKeys("Original Title");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("edit-btn")));
        
        driver.findElement(By.className("edit-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-modal")));
        
        WebElement editTitle = driver.findElement(By.id("edit-title"));
        editTitle.clear();
        editTitle.sendKeys("Updated Title");
        
        driver.findElement(By.cssSelector("#edit-form button[type='submit']")).click();
        
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.className("todo-item"), "Updated Title"));
        WebElement todo = driver.findElement(By.className("todo-item"));
        assertTrue(todo.getText().contains("Updated Title"));
    }

    @Test
    public void testDeleteTodo() {
        driver.findElement(By.id("title")).sendKeys("Todo to Delete");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("delete-btn")));
        
        driver.findElement(By.className("delete-btn")).click();
        driver.switchTo().alert().accept();
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//*[contains(text(), 'Todo to Delete')]")));
        
        List<WebElement> todos = driver.findElements(By.className("todo-item"));
        assertEquals(0, todos.size());
    }

    @Test
    public void testCompleteTodo() {
        driver.findElement(By.id("title")).sendKeys("Todo to Complete");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("toggle-btn")));
        
        driver.findElement(By.className("toggle-btn")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".todo-item.completed")));
        WebElement completedTodo = driver.findElement(By.cssSelector(".todo-item.completed"));
        assertTrue(completedTodo.getAttribute("class").contains("completed"));
    }
}
