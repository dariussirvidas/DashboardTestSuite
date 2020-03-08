import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginTests {
    @Before
    public void setupChrome() {
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
        Selenium.implicitlyWait();
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS);
    }

    @Test
    public void successfulLogin() throws InterruptedException {
        Selenium.login("Sandra", "Norvegija12?");
        WebElement userName = Selenium.findElementByXpath("/html/body/div/div[1]/div/nav/h5[2]");
        Assert.assertTrue(userName.getText().contains("Sandra"));
        WebElement menuIcon = Selenium.findElementByCss("#imageDropdown");
        WebElement toast = Selenium.findElementByClass("notification-message");
        toast.click();
        menuIcon.click();
        Selenium.logout();
    }

    @Test
    public void multipleUnsuccessfulLogins() {
        testUnsuccessfulLogin("Sandra1", "Norvegija12?");
        testUnsuccessfulLogin("", "Norvegija12?");
        testUnsuccessfulLogin("Sandra1", "");
        testUnsuccessfulLogin("Sandra1", "");
        testUnsuccessfulLogin("", "");
    }

    private void testUnsuccessfulLogin(String userName, String password) {
        Selenium.login(userName, password);
        WebElement toast = Selenium.findElementByXpath("//div[contains(@class, 'notification-message')]");
        Assert.assertTrue(toast.getText().contains("Error!"));
        Assert.assertTrue(toast.getText().contains("Invalid credentials!"));
    }

    @After
    public void closeBrowser() {
        Selenium.closeBrowser();
    }
}

