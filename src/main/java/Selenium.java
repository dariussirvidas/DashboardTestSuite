import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

class Selenium {
    private static WebDriver browser;
    private static final int WAIT_TIME_SEC = 2;

    /**
     * Chrome 79
     */
    static void setupChrome() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
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

    static void implicitlyWait() {
        browser.manage().timeouts().implicitlyWait(WAIT_TIME_SEC, TimeUnit.SECONDS);
    }
    static void implicitlyWait(int seconds) {
        browser.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    static void goToWebAddress(String address) {
        browser.get(address);
    }

    static WebElement waitForClickableByCss(String selector, int seconds) {
        WebDriverWait waiter = new WebDriverWait(browser, seconds);
        return waiter.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
    }

    static WebElement findElementByCss(String selector) {
        return browser.findElement(By.cssSelector(selector));
    }

    static WebElement findElementByName(String name) {
        return browser.findElement(By.name(name));
    }

    static void addDomain(String serviceName, String url, String serviceType, String method, boolean auth,
                          String user, String password, String parameters, String email, String checkInterval,
                          boolean active) {
        WebElement inputServiceName = findElementByCss("input[name=\"serviceName\"]");
        WebElement inputUrl = findElementByCss("input[name=\"url\"]");
        Select selectServiceType = new Select(findElementByCss("select[name=\"serviceType\"]"));
        Select selectMethod = new Select(findElementByCss("select[name=\"method\"]"));
        WebElement inputAuth = findElementByCss("input[name=\"auth\"]");
        WebElement inputUser = findElementByCss("input[name=\"user\"]");
        WebElement inputPassword = findElementByCss("input[name=\"password\"]");
        WebElement textareaParameters = findElementByCss("textarea[name=\"parameters\"]");
        WebElement inputEmail = findElementByCss("input[name=\"email\"]");
        WebElement inputCheckInterval = findElementByCss("input[name=\"interval\"]");
        WebElement inputActive = findElementByCss("input[name=\"active\"]");
        WebElement buttonSubmit = findElementByCss("*[type=\"submit\"]");
        System.out.println("submit button: " + buttonSubmit.getText());
        inputServiceName.sendKeys(serviceName);
        inputUrl.sendKeys(url);
        selectServiceType.selectByVisibleText(serviceType);
        selectMethod.selectByVisibleText(method);
        if (auth) {          // assuming unchecked by default
            inputAuth.click();
        }
        inputUser.sendKeys(user);
        inputPassword.sendKeys(password);
        textareaParameters.sendKeys(parameters);
        inputEmail.sendKeys(email);
        inputCheckInterval.sendKeys(checkInterval);
        if (!active) {      // assuming checked by default
            inputActive.click();
        }
        buttonSubmit.click();

        try {
            Thread.sleep(5000);      // <===== find a better wait method
        }
        catch (Exception ignored) {}
    }

    static void closeBrowser() {
        browser.close();
    }

}
