package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class MoyoTest {
    private WebDriver firefoxDriver;
    private static final String baseUrl = "https://www.moyo.ua/ua/";

    // умова для відкривання браузера
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // запуск драйвера
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        this.firefoxDriver = new FirefoxDriver(firefoxOptions);
        // очікування для завантаження елементів
        firefoxDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    // умова для переходу на головну сторінку сайту
    @BeforeMethod
    public void preconditions() {
        // відкривання головної сторінки
        firefoxDriver.get(baseUrl);
    }

    // закривання вікна браузера після тесту
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        // закривання браузера
        firefoxDriver.quit();
    }

    // тест для виконання кліку по елементу
    @Test
    public void testClickOnLogin() {
        // знаходження елемента за його xpath
        WebElement loginButton = firefoxDriver.findElement(By.xpath("/html/body/div[1]/header/div[1]/div/div[2]/ul/li[2]/a"));
        // перевірка
        Assert.assertNotNull(loginButton);
        loginButton.click();
        // перевірка зміни сторінки
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    }

    // тест для введення даних у поле та перевірки наявності даних
    @Test
    public void testInput() {
        // знаходження елемента за його id
        WebElement searchInput = firefoxDriver.findElement(By.id("search-input"));
        // перевірка
        Assert.assertNotNull(searchInput);

        // введення тексту в поле
        String inputText = "Test query";
        searchInput.sendKeys(inputText);
        // перевірка
        String actualText = searchInput.getAttribute("value");
        Assert.assertEquals(actualText, inputText);
    }

    // тест для перевірки кольору елемента
    @Test
    public void testElementColor() {
        // знаходження елемента за його класом
        WebElement testElem = firefoxDriver.findElement(By.className("header_catalog_btn_icon"));
        String backgroundColor = testElem.getCssValue("background-color");
        String expectedBGColor = "rgba(0, 0, 0, 0)";

        // перевірка кольору
        Assert.assertEquals(backgroundColor, expectedBGColor);
    }

}











