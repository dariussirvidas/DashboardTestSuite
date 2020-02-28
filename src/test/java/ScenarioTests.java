import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScenarioTests {
    static final int CHECK_INTERVAL = 5;

    @BeforeClass
    public static void setupChrome() {
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
        Selenium.implicitlyWait();
        Selenium.loginDefault();
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
        MockapiControl.setDefaults("200", "0");
        Selenium.addDomain("mockapi", "http://88.222.15.11:8080/mockapi/auth",
                "Service - REST", "GET", true, "admin", "password",
                "", "watchhoundapi@gmail.com", String.valueOf(CHECK_INTERVAL), "1000", true);
        Selenium.waitForModalToClose(Selenium.WAIT_TIME_SEC);
    }

    @Test
    public void mockapiScenarioTest() {
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS);
        Selenium.waitForClickableByCss(".tile-success", CHECK_INTERVAL);
        MockapiControl.setDefaults("301", "0");
        Selenium.waitForClickableByCss(".tile-fail", CHECK_INTERVAL);
        MockapiControl.setDefaults("204", "1000");
        Selenium.waitForClickableByCss(".tile-amber", CHECK_INTERVAL);
        MockapiControl.setDefaults("404", "1000");
        Selenium.waitForClickableByCss(".tile-fail", CHECK_INTERVAL);
        MockapiControl.setDefaults("200", "0");
        Selenium.waitForClickableByCss(".tile-success", CHECK_INTERVAL);
        MockapiControl.setDefaults("503", "0");
        Selenium.waitForClickableByCss(".tile-fail", CHECK_INTERVAL);
        MockapiControl.setDefaults("200", "1000");
        Selenium.waitForClickableByCss(".tile-amber", CHECK_INTERVAL);
        MockapiControl.setDefaults("400", "1000");
        Selenium.waitForClickableByCss(".tile-fail", CHECK_INTERVAL);
    }

    @AfterClass
    public static void closeBrowser() {
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
        Selenium.closeBrowser();
        MockapiControl.setDefaults("200", "0");
    }
}
