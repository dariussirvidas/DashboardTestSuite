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
    static final int WAIT_TIME_SEC = 2;
    static final String DASHBOARD_WEB_ADDRESS = "http://localhost:3000/";
    //static final String DASHBOARD_WEB_ADDRESS = "http://watchhound.azurewebsites.net/";
    static final String DELETE_BUTTON_SELECTOR = "form>button:last-child";
    static final String SAVE_BUTTON_SELECTOR = "form>button:nth-last-child(3)";
    static final String CONFIRM_BUTTON_SELECTOR = "form>button:nth-child(2)";
    static final String EDIT_BUTTON_SELECTOR = "i.material-icons.iconHover";
    static final String INVALID_INPUT_BORDER_COLOR = "rgba(255, 0, 0, 1)";
    static final String LOGIN_USERNAME = "Darius";
    static final String LOGIN_PASSWORD = "Password-1";

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

    static WebElement findElementByXpath(String xpath) {
        WebDriverWait waiter = new WebDriverWait(browser, 10);
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return browser.findElement(By.xpath(xpath));
    }

    static WebElement findElementByClass(String className) {
        return browser.findElement(By.className(className));
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
        /////////////////////////////////// service name ///////////////////////////////////////
        if (!serviceName.isEmpty()) {
            inputServiceName.clear();
            inputServiceName.sendKeys(serviceName);
        }
        /////////////////////////////// url ///////////////////////////////////////////
        if (!url.isEmpty()) {
            inputUrl.clear();
            inputUrl.sendKeys(url);
        }
        //////////////////////////// service type ////////////////////////////////
        if (!serviceType.isEmpty()) sSelectServiceType.selectByVisibleText(serviceType);
        ///////////////////////// method /////////////////////////////////////////
        if (selectMethod.isEnabled() && !method.isEmpty()) sSelectMethod.selectByVisibleText(method);
        ///////////////// auth ////////////////////////////////
        if (inputAuth.isSelected()) {
            if (!auth) inputAuth.click();
        } else if (auth) {
            inputAuth.click();
        }
        //////////////////////// user ////////////////////////////////
        if (inputUser.isEnabled() && !user.isEmpty()) {
            inputUser.clear();
            inputUser.sendKeys(user);
        }
        //////////////////////// password /////////////////////////////////
        if (inputPassword.isEnabled() && !password.isEmpty()) {
            inputPassword.clear();
            inputPassword.sendKeys(password);
        }
        ////////////////////////// parameters /////////////////////////////
        if (textareaParameters.isEnabled() && !parameters.isEmpty()) {
            textareaParameters.clear();
            textareaParameters.sendKeys(parameters);
        }
        //////////////////////////// email ////////////////////////////////
        if (!email.isEmpty()) {
            inputEmail.clear();
            inputEmail.sendKeys(email);
        }
        ///////////////////////// check interval /////////////////////////////
        if (!checkInterval.isEmpty()) {
            inputCheckInterval.clear();
            inputCheckInterval.sendKeys(checkInterval);
        }
        ////////////////////// threshold //////////////////////////////////
        if (!threshold.isEmpty()) {
            inputLatencyThreshold.clear();
            inputLatencyThreshold.sendKeys(threshold);
        }
        ////////////////////////// active //////////////////////////////////////
        if (inputActive.isSelected()) {
            if (!active) inputActive.click();
        } else if (active) {
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
        inputUsername.clear();
        inputUsername.sendKeys(username);
        inputPassword.clear();
        inputPassword.sendKeys(password);
        buttonLogin.click();
        //waitForClickableByCss("a.navbar-brand", WAIT_TIME_SEC);
    }

    static void logout(){
        WebElement logOut = Selenium.findElementByXpath("/html/body/div/div[1]/div/nav/div/ul/a[5]/button");
        logOut.click();
    }

    static void loginDefault() {
        goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS);
        login(LOGIN_USERNAME, LOGIN_PASSWORD);
    }

    static void delete(WebElement editButton) {
        editButton.click();
        WebElement buttonDelete = waitForClickableByCss(DELETE_BUTTON_SELECTOR, WAIT_TIME_SEC);
        buttonDelete.click();
        WebElement buttonConfirm = waitForClickableByCss(CONFIRM_BUTTON_SELECTOR, WAIT_TIME_SEC);
        buttonConfirm.click();
        waitForModalToClose(WAIT_TIME_SEC);
    }

    static void deleteAll() {
        ArrayList<WebElement> buttonsEdit = (ArrayList<WebElement>) findElementsByCss(EDIT_BUTTON_SELECTOR);
        for (WebElement buttonEdit : buttonsEdit) {
            delete(buttonEdit);
        }
    }

    static void closeBrowser() {
        if (browser != null) browser.quit();
    }

}
