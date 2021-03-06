import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class EditDomainTests {
    private static final String EDIT_DOMAIN_FORM_DATA = "testData\\EditDomainFormData.json";

    private final String serviceName;
    private final String url;
    private final String serviceType;
    private final String method;
    private final boolean auth;
    private final String user;
    private final String password;
    private final String parameters;
    private final String email;
    private final String interval;
    private final String threshold;
    private final boolean active;
    private final JSONArray expectedFailedFields;

    public EditDomainTests(Object serviceName, Object url, Object serviceType, Object method, Object auth, Object user,
                          Object password, Object parameters, Object email, Object interval, Object threshold,
                          Object active, Object expectedFailedFields) {
        this.serviceName = (String) serviceName;
        this.url = (String) url;
        this.serviceType = (String) serviceType;
        this.method = (String) method;
        this.auth = (Boolean) auth;
        this.user = (String) user;
        this.password = (String) password;
        this.parameters = (String) parameters;
        this.email = (String) email;
        this.interval = (String) interval;
        this.threshold = (String) threshold;
        this.active = (Boolean) active;
        this.expectedFailedFields = (JSONArray) expectedFailedFields;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws Exception {
        JSONArray dataArray = Selenium.readJson(EDIT_DOMAIN_FORM_DATA);
        ArrayList<Object[]> parameters = new ArrayList<Object[]>();
        for (Object o : dataArray) {
            JSONObject data = (JSONObject) o;
            Object[] parameter = {data.get("serviceName"), data.get("url"), data.get("serviceType"),
                    data.get("method"), data.get("auth"), data.get("user"), data.get("password"),
                    data.get("parameters"), data.get("email"), data.get("interval"), data.get("threshold"),
                    data.get("active"), data.get("expectedFailedFields")};
            parameters.add(parameter);
        }
        return parameters;
    }

    @BeforeClass
    public static void setupChrome() {
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
        Selenium.implicitlyWait();
        Selenium.loginDefault();
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
    }

    @Before
    public void addTestDomain() {
        Selenium.addDomain("mockapi", "http://88.222.15.11:8080/mockapi", "Service - REST",
                "GET", true, "admin", "password", "",
                "watchhoundapi@gmail.com","60", "1000", true);
        Selenium.waitForModalToClose(Selenium.WAIT_TIME_SEC);
    }

    @Test
    public void testEditDomain() {
        WebElement editLastDomain = Selenium.waitForClickableByCss("tr:last-child "
                + Selenium.EDIT_BUTTON_SELECTOR, Selenium.WAIT_TIME_SEC);
        editLastDomain.click();
        Selenium.fillDomainForm(serviceName, url, serviceType, method, auth, user, password, parameters, email, interval,
                threshold, active);
        WebElement saveButton = Selenium.findElementByCss(Selenium.SAVE_BUTTON_SELECTOR);
        saveButton.click();
        if (expectedFailedFields.isEmpty()) {
            Selenium.waitForModalToClose(Selenium.WAIT_TIME_SEC);
            Assert.assertFalse("Modal is: open, expected: closed", Selenium.isModalOpen());
        }
        else {
            try { Selenium.waitForModalToClose(Selenium.WAIT_TIME_SEC); }
            catch (TimeoutException ignored) {}
            Assert.assertTrue("Modal is: closed, expected: open", Selenium.isModalOpen());
            /*for (Object expectedFailedField : expectedFailedFields) {
                String fieldName = (String) expectedFailedField;
                WebElement field = Selenium.findElementByName(fieldName);
                String borderColor = field.getCssValue("border-top-color");
                Assert.assertEquals(fieldName + " border color", Selenium.INVALID_INPUT_BORDER_COLOR, borderColor); // <==== norim raudono border an inputo
            }*/
        }
    }

    @After
    public void removeTestDomain() {
        Selenium.goToWebAddress(Selenium.DASHBOARD_WEB_ADDRESS + "domains");
        Selenium.deleteAll();
    }

    @AfterClass
    public static void closeBrowser() {
        Selenium.closeBrowser();
    }
}
