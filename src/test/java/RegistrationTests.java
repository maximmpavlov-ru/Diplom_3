import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.RegisterPage;
import restAPI.UserRequests;
import user.UserCredentials;
import webdriver.WebDriverCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(Parameterized.class)
public class RegistrationTests {

    private final List<String> tokens = new ArrayList<>();
    UserRequests userRequests;
    WebDriver driver;
    String browserName;
    RegisterPage registerPage;


    @Parameterized.Parameters
    public static Object[][] testParameters() {
        return new Object[][]{
                {"chrome"},
                {"yandex"}
        };
    }

    public RegistrationTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @DisplayName("Подготовка тестовых данных перед началом каждой проверки")
    public void prepareTestData() {
        userRequests = new UserRequests();
        driver = WebDriverCreator.getWebDriver(browserName);
        driver.get("https://stellarburgers.nomoreparties.site/register");
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

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void userRegistration() {
        String email = UUID.randomUUID() + "@somemail.com";
        String password = "password";
        String name = "Name";
        UserCredentials userCredentials = new UserCredentials(email, password);
        registerPage = new RegisterPage(driver);
        registerPage.setName(name);
        registerPage.setEmail(email);
        registerPage.setPassword(password);
        registerPage.clickRegisterButton();
        registerPage.waitFormSubmitted("Вход");

        ValidatableResponse requestResponse = userRequests.loginUser(userCredentials);
        tokens.add(requestResponse.extract().path("accessToken"));

        Assert.assertEquals("Не сработала авторизация для созданного пользователя",
                HttpStatus.SC_OK,
                requestResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Попытка регистрации с некорректным паролем")
    public void userRegistrationWithIncorrectPassword() {
        String email = UUID.randomUUID() + "@somemail.com";
        String password = "short";
        String name = "Name";
        registerPage = new RegisterPage(driver);
        registerPage.setName(name);
        registerPage.setEmail(email);
        registerPage.setPassword(password);
        registerPage.clickRegisterButton();
        registerPage.waitErrorIsVisible();

        Assert.assertEquals("Некорректное сообщение об ошибке", "Некорректный пароль", registerPage.getErrorMessage());
    }

}
