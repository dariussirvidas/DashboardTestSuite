import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Selenium {
    private static WebDriver browser;
    private static final int WAIT_TIME_SEC = 2;
    static final String DASHBOARD_WEB_ADDRESS = "http://localhost:3000/";
    //static final String DASHBOARD_WEB_ADDRESS = "http://watchhound.azurewebsites.net/";
    static final String DELETE_BUTTON_SELECTOR = "form>button:last-child";
    static final String CONFIRM_BUTTON_SELECTOR = "form>button:nth-child(2)";
    static final String EDIT_BUTTON_SELECTOR = "i.material-icons.iconHover";//"a.btn.btn-link.btn-sm.txt";
    static final String INVALID_INPUT_BORDER_COLOR = "rgba(255, 0, 0, 1)";

    /**
     * Chrome 79
     */
    static void setupChrome() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        browser = new ChromeDriver();
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

    static void waitForModalToClose(int seconds) {
        WebDriverWait waiter = new WebDriverWait(browser, seconds);
        waiter.until(ExpectedConditions.attributeToBe(findElementByCss("body"), "class", ""));
    }

    static WebElement findElementByCss(String selector) {
        return browser.findElement(By.cssSelector(selector));
    }

    static List<WebElement> findElementsByCss(String selector) {
        return browser.findElements(By.cssSelector(selector));
    }

    static WebElement findElementByName(String name) {
        return browser.findElement(By.name(name));
    }

    static void fillDomainForm(String serviceName, String url, String serviceType, String method, boolean auth,
                               String user, String password, String parameters, String email, String checkInterval,
                               String threshold, boolean active) {
        WebElement inputServiceName = findElementByCss("input[name=\"serviceName\"]");
        WebElement inputUrl = findElementByCss("input[name=\"url\"]");
        WebElement selectServiceType = findElementByCss("select[name=\"serviceType\"]");
        Select sSelectServiceType = new Select(selectServiceType);
        WebElement selectMethod = findElementByCss("select[name=\"method\"]");
        Select sSelectMethod = new Select(selectMethod);
        WebElement inputAuth = findElementByCss("input[name=\"auth\"]");
        WebElement inputUser = findElementByCss("input[name=\"user\"]");
        WebElement inputPassword = findElementByCss("input[name=\"password\"]");
        WebElement textareaParameters = findElementByCss("textarea[name=\"parameters\"]");
        WebElement inputEmail = findElementByCss("input[name=\"email\"]");
        WebElement inputCheckInterval = findElementByCss("input[name=\"interval\"]");
        WebElement inputLatencyThreshold = findElementByCss("input[name=\"threshold\"]");
        WebElement inputActive = findElementByCss("input[name=\"active\"]");
        inputServiceName.sendKeys(serviceName);
        inputUrl.sendKeys(url);
        sSelectServiceType.selectByVisibleText(serviceType);
        if (selectMethod.isEnabled()) sSelectMethod.selectByVisibleText(method);
        if (inputAuth.isEnabled() & auth) {          // assuming unchecked by default
            inputAuth.click();
        }
        if (inputUser.isEnabled()) inputUser.sendKeys(user);
        if (inputPassword.isEnabled()) inputPassword.sendKeys(password);
        if (textareaParameters.isEnabled())textareaParameters.sendKeys(parameters);
        inputEmail.sendKeys(email);
        inputCheckInterval.sendKeys(checkInterval);
        inputLatencyThreshold.sendKeys(threshold);
        if (!active) {      // assuming checked by default
            inputActive.click();
        }
    }
    static void addDomain(String serviceName, String url, String serviceType, String method, boolean auth,
                          String user, String password, String parameters, String email, String checkInterval,
                          String threshold, boolean active) {
        goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        findElementByCss("div.domainButton button").click();
        fillDomainForm(serviceName, url, serviceType, method, auth, user, password, parameters, email, checkInterval,
                threshold, active);
        WebElement buttonSubmit = findElementByCss("button[type=\"submit\"]");
        buttonSubmit.click();
    }

    static JSONArray readJson(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(reader);
    }

    static boolean isModalOpen() {
        String bodyClass = findElementByCss("body").getAttribute("class");
        return bodyClass.equals("modal-open");
    }

    static void login(String username, String password) {
        WebElement inputUsername = findElementByCss("input[name=\"username\"]");
        WebElement inputPassword = findElementByCss("input[name=\"password\"]");
        WebElement buttonLogin = findElementByCss("button[type=\"submit\"]");
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        buttonLogin.click();
        waitForClickableByCss("a.navbar-brand", WAIT_TIME_SEC);
    }

    static void delete(WebElement editButton) {
        editButton.click();
        System.out.println("after edit button click");
        WebElement buttonDelete = waitForClickableByCss(DELETE_BUTTON_SELECTOR, 2);
        buttonDelete.click();
        System.out.println("after delete button click");
        WebElement buttonConfirm = waitForClickableByCss(CONFIRM_BUTTON_SELECTOR, 2);
        buttonConfirm.click();
        //waitForModalToClose(2);
        /*try {
            Thread.sleep(10000);
        }
        catch (Exception ignored) {}*/
    }

    static void deleteAll() {
        ArrayList buttonsEdit = (ArrayList) findElementsByCss(EDIT_BUTTON_SELECTOR);
        int length = buttonsEdit.size();
        System.out.println("array length: " + length);
        for (int i = 0; i < length; i++) {
            System.out.println("i = " + i);
            delete((WebElement) buttonsEdit.get(i));
            System.out.println("after delete " + i);
        }
    }

    static void closeBrowser() {
        browser.close();
    }

}
