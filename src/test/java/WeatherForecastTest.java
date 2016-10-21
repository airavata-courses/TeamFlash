/**
 * Created by pranav on 10/20/16.
 */

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;

import io.github.bonigarcia.wdm.MarionetteDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WeatherForecastTest {

    private String baseUrl;
    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        MarionetteDriverManager.getInstance().setup();
    }

    @Before
    public void openBrowser() {
        driver = new MarionetteDriver();
        baseUrl = "https://www.google.com";
        driver.get(baseUrl);
    }

    @After
    public void closeBrowser() throws IOException {
        if(driver!=null){
            driver.quit();
        }
    }

    @Test
    public void login() throws IOException {
        assertEquals("The page title should equal Google at the start of the test.", "Google", driver.getTitle());
        WebElement searchField = driver.findElement(By.name("q"));
        searchField.sendKeys("Drupal!");
        searchField.submit();
        assertTrue("The page title should start with the search string after the search.",
                (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        return d.getTitle().toLowerCase().startsWith("drupal!");
                    }
                })
        );
    }
}
