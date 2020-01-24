import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class Selenium {
    private static WebDriver browser;
    private static final int WAIT_TIME_SEC = 2;

    /**
     * Chrome 79
     */
    static void setupChrome() {
        System.setProperty("webdriver.chrome.driver", "target\\classes\\chromedriver.exe");
        browser = new ChromeDriver();
    }

    /**
     * Firefox 72
     */
    static void setupFirefox() {
        System.setProperty("webdriver.gecko.driver", "target\\classes\\geckodriver.exe");
        browser = new FirefoxDriver();
    }

    /**
     * EdgeHTML 18 needs to run DISM.exe /Online /Add-Capability /CapabilityName:Microsoft.WebDriver~~~~0.0.1.0
     * in elevated command prompt
     */
    static void setupEdge() {
        browser = new EdgeDriver();
    }

    static void maximizeBrowserWindow() {
        browser.manage().window().maximize();
    }

    static void goToWebAddress(String address) {
        browser.get(address);
    }

    static WebElement waitForClickableByCss(String selector) {
        WebDriverWait waiter = new WebDriverWait(browser, WAIT_TIME_SEC);
        return waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
    }

    static void closeBrowser() {
        browser.close();
    }

}
