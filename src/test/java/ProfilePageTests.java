import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.LoginPage;
import pageobjects.MainPage;
import pageobjects.ProfilePage;
import restAPI.UserRequests;
import user.User;
import webdriver.WebDriverCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(Parameterized.class)
public class ProfilePageTests {
    private String email;
    private String password;
    private final List<String> tokens = new ArrayList<>();
    private WebDriver driver;
    private final String browserName;
    private LoginPage loginPage;
    private MainPage mainPage;
    private ProfilePage profilePage;
    private UserRequests userRequests;

    @Parameterized.Parameters
    public static Object[][] testParameters() {
        return new Object[][]{
                {"chrome"},
                {"yandex"}
        };
    }

    public ProfilePageTests(String browserName) {
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
        profilePage = new ProfilePage(driver);
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
    @DisplayName("Переход по клику на ссылку 'Личный кабинет'")
    public void navigateAfterProfileLinkClick() {
        mainPage.clickLinkToProfile();

        Assert.assertEquals("Отображается неправильный заголовок страницы", "Вход", loginPage.getTitle());
    }

    @Test
    @DisplayName("Переход после клика на ссылку 'Конструктор'")
    public void navigationAfterClickOnConstructorLink() {
        mainPage.clickLinkToProfile();
        profilePage.clickLinkToConstructor();

        Assert.assertTrue(mainPage.authButtonIsDisplayed());
    }

    @Test
    @DisplayName("Переход после клика на логотип 'Stellar burgers'")
    public void navigationAfterClickOnBurgerLogo() {
        mainPage.clickLinkToProfile();
        profilePage.clickLinkOnLogo();

        Assert.assertTrue(mainPage.authButtonIsDisplayed());
    }

    @Test
    @DisplayName("Переход после клика по кнопке 'Выйти' в личном кабинете")
    public void navigationAfterClickOnExitButton() {
        mainPage.clickAuthButton();
        loginPage.setEmail(email);
        loginPage.setPassword(password);
        loginPage.clickAuthButton();
        mainPage.waitHeaderIsVisible();
        mainPage.clickLinkToProfile();
        profilePage.waitAuthFormVisible();
        profilePage.clickLogoutLink();
        loginPage.waitAuthFormVisible();

        Assert.assertEquals("Отображается неправильная страница", "Вход", loginPage.getTitle());
    }
}
