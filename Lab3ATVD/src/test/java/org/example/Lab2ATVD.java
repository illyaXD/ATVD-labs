package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class Lab2ATVD {
    private WebDriver firefoxDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

    // умова для відкривання браузера
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // запуск драйвера
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // встановлення повноекранного режиму
        firefoxOptions.addArguments("--start-maximized");
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

    // тест для пошуку елемента header
    @Test
    public void testHeaderExists() {
        // знаходження елемента за його cssSelector
        WebElement header = firefoxDriver.findElement(By.cssSelector("header"));
        // перевірка
        Assert.assertNotNull(header);
    }

    // тест для пошуку кнопки (посилання) за xpath
    @Test
    public void testClickOnForStudent() {
        // знаходження елемента за його xpath
        WebElement forStudentButton = firefoxDriver.findElement(By.xpath("//*[@id=\"menu-item-3727\"]/a"));
        // перевірка
        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();
        // перевірка зміни сторінки
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    }

    // тест для пошуку елемента за тегом
    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/student_life/students/";
        firefoxDriver.get(baseUrl + studentPageUrl);
        // знаходження елемента за tagName
        WebElement searchField = firefoxDriver.findElement(By.tagName("input"));
        // перевірка
        Assert.assertNotNull(searchField);
        // виведення різних параметрів searchField
        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("ID attribute: %s", searchField.getAttribute("id")) +
                String.format("Type attribute: %s", searchField.getAttribute("type")) +
                String.format("Value attribute: %s", searchField.getAttribute("value")) +
                String.format("Position: (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("Size: %dx%d", searchField.getSize().height, searchField.getSize().width));
        // введення значення
        String inputValue = "I need info";
        searchField.sendKeys(inputValue);
        // перевірка тексту
        Assert.assertEquals(searchField.getText(), inputValue);
        // натиснення Enter
        searchField.sendKeys(Keys.ENTER);
        // перевірка зміни сторінки
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageUrl);
    }
}













