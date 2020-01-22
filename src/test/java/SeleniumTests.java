import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

public class SeleniumTests {
    @BeforeClass
    public static void setupChrome() {
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
    }

    @Test
    public void testSearch() {
        Selenium.goToWebAddress("http://kitm.lt/");
        WebElement search = Selenium.waitForClickableByCss("input[name=\"s\"]");
        search.sendKeys("testuotojo");
        search.submit();
    }

    @AfterClass
    public static void closeBrowser() {
        Selenium.closeBrowser();
    }
}
