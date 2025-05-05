import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.MainPage;
import webdriver.WebDriverCreator;

@RunWith(Parameterized.class)
public class MainPageTests {
    private WebDriver driver;
    private final String browserName;
    private MainPage mainPage;

    @Parameterized.Parameters
    public static Object[][] testParameters() {
        return new Object[][]{
                {"chrome"},
                {"yandex"}
        };
    }

    public MainPageTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @DisplayName("Подготовка тестовых данных перед началом каждой проверки")
    public void prepareTestData() {
        driver = WebDriverCreator.getWebDriver(browserName);
        driver.get("https://stellarburgers.nomoreparties.site/");
        mainPage = new MainPage(driver);
    }

    @After
    @DisplayName("Удаление тестовых данных и закрытие браузера после каждой проверки")
    public void cleanUp() {
        driver.quit();
    }

    @Test
    @DisplayName("Переключение на вкладку 'Булки'")
    public void checkClickOnBunsTab() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();
        mainPage.clickSaucesTab();
        mainPage.clickBunsTab();

        Assert.assertEquals("Вкладка 'Булки' не выбрана", expectedLocation, mainPage.getBunsLocation());
    }

    @Test
    @DisplayName("Переключение на вкладку 'Соусы'")
    public void checkClickOnSaucesTab() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();
        mainPage.clickSaucesTab();

        Assert.assertEquals("Вкладка 'Соусы' не выбрана", expectedLocation, mainPage.getSaucesLocation());
    }

    @Test
    @DisplayName("Переключение на вкладку 'Булки'")
    public void checkClickOnFillingsTab() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();
        mainPage.clickFillingsTab();

        Assert.assertEquals("Вкладка 'Начинки' не выбрана", expectedLocation, mainPage.getFillingsLocation());
    }
}
