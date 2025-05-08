import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.ForgotPasswordPage;
import pageobjects.LoginPage;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import restAPI.UserRequests;
import user.User;
import webdriver.WebDriverCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RunWith(Parameterized.class)
public class LoginTests {

    private String email;
    private String password;
    private final List<String> tokens = new ArrayList<>();
    UserRequests userRequests;
    WebDriver driver;
    String browserName;
    private LoginPage loginPage;
    private MainPage mainPage;
    private RegisterPage registerPage;
    private ForgotPasswordPage forgotPasswordPage;


    @Parameterized.Parameters
    public static Object[][] testParameters() {
        return new Object[][]{
                {"chrome"},
                {"yandex"}
        };
    }

    public LoginTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @DisplayName("Подготовка тестовых данных перед началом каждой проверки")
    public void prepareTestData() {
        email = UUID.randomUUID() + "@somemail.com";
        password = "password";
        String name = "Name";
        userRequests = new UserRequests();
        User user = new User(email, password, name);
        ValidatableResponse requestResponse = userRequests.createUser(user);
        tokens.add(requestResponse.extract().path("accessToken"));
        driver = WebDriverCreator.getWebDriver(browserName);
        driver.get("https://stellarburgers.nomoreparties.site/");
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        registerPage = new RegisterPage(driver);
        forgotPasswordPage = new ForgotPasswordPage(driver);
    }

    @After
    @DisplayName("Удаление тестовых данных и закрытие браузера после каждой проверки")
    public void cleanUp() {
        driver.quit();
        if (tokens.isEmpty())
            return;
        for (String token : tokens) {
            userRequests.deleteUser(token);
        }
    }

    @Step("Ввод логина и пароля с нажатием кнопки 'Войти'")
    private void inputCredentialsAndLogIn() {
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickAuthButton();
        loginPage.waitFormSubmitted();
    }

    @Test
    @DisplayName("Авторизация пользователя с главной страницы")
    public void userAuthorizationFromMainPage() {
        mainPage.clickAuthButton();
        loginPage.waitAuthFormVisible();
        inputCredentialsAndLogIn();

        Assert.assertEquals("Авторизация прошла некорректно", "Оформить заказ", mainPage.getBasketButtonText());
    }

    @Test
    @DisplayName("Авторизация после нажатия на ссылку 'Личный кабинет'")
    public void userAuthorizationFromProfileLink() {
        mainPage.clickLinkToProfile();
        loginPage.waitAuthFormVisible();
        inputCredentialsAndLogIn();

        Assert.assertEquals("Авторизация прошла некорректно", "Оформить заказ", mainPage.getBasketButtonText());
    }

    @Test
    @DisplayName("Вход через кнопку на форме регистрации")
    public void userAuthorizationFromRegistrationForm() {
        mainPage.clickLinkToProfile();
        loginPage.waitAuthFormVisible();
        loginPage.clickRegisterLink();
        registerPage.clickAuthLink();
        loginPage.waitAuthFormVisible();
        inputCredentialsAndLogIn();

        Assert.assertEquals("Авторизация прошла некорректно", "Оформить заказ", mainPage.getBasketButtonText());
    }

    @Test
    @DisplayName("Вход через кнопку на форме восстановления пароля")
    public void userAuthorizationFromForgotPasswordForm() {
        mainPage.clickLinkToProfile();
        loginPage.waitAuthFormVisible();
        loginPage.clickForgotPasswordLink();
        forgotPasswordPage.clickAuthLink();
        loginPage.waitAuthFormVisible();
        inputCredentialsAndLogIn();

        Assert.assertEquals("Авторизация прошла некорректно", "Оформить заказ", mainPage.getBasketButtonText());
    }
}