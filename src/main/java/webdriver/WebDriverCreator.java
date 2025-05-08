package webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverCreator {
    private static final String CHROMEDRIVER_PATH = "C:/Projects/WebDriver/bin/chromedriver.exe";
    private static final String YANDEX_BROWSER_PATH = "C:/Users/maxim/AppData/Local/Yandex/YandexBrowser/Application/browser.exe";

    public static WebDriver getWebDriver(String browserName) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        switch (browserName) {
            case "chrome":
                return new ChromeDriver(options);

            case "yandex":
                System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
                options.setBinary(YANDEX_BROWSER_PATH);
                return new ChromeDriver(options);

            default:
                throw new RuntimeException("Incorrect browser name");
        }
    }
}
