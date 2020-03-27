import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

public class ScenarioTests {
    static final int CHECK_INTERVAL = 10;

    @BeforeClass
    public static void setupChrome() {
        MockapiControl.setDefaults("200", "0");
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
        Selenium.implicitlyWait();
        Selenium.loginDefault();
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
        Selenium.addDomain("mockapi", "http://88.222.15.11:8080/mockapi/",
                "Service - REST", "GET", true, "admin", "password",
                "", "watchhoundapi@gmail.com", String.valueOf(CHECK_INTERVAL), "2000", true);
        Selenium.waitForModalToClose(Selenium.WAIT_TIME_SEC);
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS);
    }

    @Test
    public void testSticker_code200_delay0() {
        testSticker("200", "0", ".tile-success");
    }
    @Test
    public void testSticker_code301_delay0() {
        testSticker("301", "0", ".tile-fail");
    }
    @Test
    public void testSticker_code204_delay2000() {
        testSticker("204", "2000", ".tile-amber");
    }
    @Test
    public void testSticker_code404_delay2000() {
        testSticker("404", "2000", ".tile-fail");
    }
    @Test
    public void testSticker_code200_delay0_2() {
        testSticker("200", "0", ".tile-success");
    }
    @Test
    public void testSticker_code503_delay0() {
        testSticker("503", "0", ".tile-fail");
    }
    @Test
    public void testSticker_code200_delay2000() {
        testSticker("200", "2000", ".tile-amber");
    }
    @Test
    public void testSticker_code400_delay0() {
        testSticker("400", "0", ".tile-fail");
    }

    @AfterClass
    public static void closeBrowser() {
        MockapiControl.setDefaults("200", "0");
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
        Selenium.closeBrowser();
    }

    private void testSticker(String code, String delay, String expected) {
        MockapiControl.setDefaults(code, delay);
        Selenium.waitForCountdownToReach(CHECK_INTERVAL, CHECK_INTERVAL);
        Selenium.waitForCountdownToReach(CHECK_INTERVAL / 2, CHECK_INTERVAL);
        try {
            Selenium.findElementByCss(expected);
        }
        catch (NoSuchElementException e) {
            Assert.fail("Incorrect sticker color");
        }
    }
}
