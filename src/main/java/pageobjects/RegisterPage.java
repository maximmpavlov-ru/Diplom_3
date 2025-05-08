package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegisterPage {
    private final WebDriver driver;
    private final By inputs = By.xpath("//input[@class='text input__textfield text_type_main-default']");
    private final By registerButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");
    private final By errorMessage = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//p[starts-with(@class,'input__error')]");
    private final By title = By.xpath(".//main//h2");
    private final By authLink = By.xpath(".//a[starts-with(@class,'Auth_link')]");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div[starts-with(@class, 'Modal_modal')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    private List<WebElement> getInputs() {
        return driver.findElements(inputs);
    }

    @Step("Заполнение имени")
    public void setName(String name) {
        List<WebElement> inputsList = getInputs();
        if (inputsList.size() >= 1) {
            inputsList.get(0).sendKeys(name);
        } else {
            throw new RuntimeException("Поле 'Имя' не найдено");
        }
    }

    @Step("Заполнение email")
    public void setEmail(String email) {
        List<WebElement> inputsList = getInputs();
        if (inputsList.size() >= 2) {
            inputsList.get(1).sendKeys(email);
        } else {
            throw new RuntimeException("Поле 'Email' не найдено");
        }
    }

    @Step("Заполнение password")
    public void setPassword(String password) {
        List<WebElement> inputsList = getInputs();
        if (inputsList.size() >= 3) {
            inputsList.get(2).sendKeys(password);
        } else {
            throw new RuntimeException("Поле 'Пароль' не найдено");
        }
    }

    @Step("Нажатие на кнопку регистрации")
    public void clickRegisterButton() {
        waitButtonIsClickable();
        driver.findElement(registerButton).click();
    }

    private void waitButtonIsClickable() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(driver.findElement(modalOverlay)));
    }

    public void waitFormSubmitted(String expectedTitle) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.textToBe(title, expectedTitle));
    }

    public void waitErrorIsVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(driver.findElement(errorMessage)));
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public void clickAuthLink() {
        waitButtonIsClickable();
        driver.findElement(authLink).click();
    }
}