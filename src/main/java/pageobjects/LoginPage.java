package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final By header = By.tagName("h1");
    private final By inputs = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//input");
    private final By authButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");
    private final By title = By.xpath(".//main//h2");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div/div[starts-with(@class, 'Modal_modal_overlay')]");
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");
    private final By forgotPasswordLink = By.xpath("//a[text()='Восстановить пароль']");
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    public String getTitle() {
        return driver.findElement(title).getText();
    }


    @Step("Заполнение email")
    public void setEmail(String email) {
        driver.findElements(inputs).get(0).sendKeys(email);
    }

    @Step("Заполнение password")
    public void setPassword(String password) {
        driver.findElements(inputs).get(1).sendKeys(password);
    }

    @Step("Нажатие на кнопку авторизации")
    public void clickAuthButton() {
        waitButtonIsClickable();
        driver.findElement(authButton).click();
    }

    @Step("Нажатие на ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        waitButtonIsClickable();
        driver.findElement(registerLink).click();
    }

    @Step("Нажатие на ссылку 'Восстановить пароль'")
    public void clickForgotPasswordLink() {
        waitButtonIsClickable();
        driver.findElement(forgotPasswordLink).click();
    }

    public void waitButtonIsClickable() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(driver.findElement(modalOverlay)));
    }
    public void waitFormSubmitted() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(header));
    }
    public void waitAuthFormVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.textToBe(title, "Вход"));
    }
}