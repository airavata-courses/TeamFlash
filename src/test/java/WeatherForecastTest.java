import java.io.IOException;
import io.github.bonigarcia.wdm.MarionetteDriverManager;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        baseUrl = "http://52.55.196.63:8888/login";
        driver.get(baseUrl);
    }

    @After
    public void closeBrowser() throws IOException {
        if(driver!=null){
            driver.quit();
        }
    }

    @Test
    public void firstTest() throws IOException {
        WebElement submit = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.name("commit")));


        WebElement user = driver.findElement(By.name("login"));
        user.sendKeys("pranavpande");

        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("pranavpande");

        submit.submit();

        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("dataIngestorButton")));
    }

    @Test
    public void secondTest() throws IOException {
        WebElement date = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("date")));

        date.sendKeys("05/04/1988");

        WebElement time = driver.findElement(By.name("time"));
        time.sendKeys("00408");

        new Select(driver.findElement(By.id("station"))).selectByVisibleText("Miami");

        WebElement forecast = driver.findElement(By.id("dataIngestorButton"));
        forecast.submit();

        //add result check
    }

    @Test
    public void thirdTest() throws IOException {
        WebElement audit = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("auditButton")));

        audit.click();

        //add table check
    }

    @Test
    public void fourthTest() throws IOException {
        WebElement logout = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("logoutButton")));

        logout.submit();

        WebElement submit = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.name("commit")));
    }
}