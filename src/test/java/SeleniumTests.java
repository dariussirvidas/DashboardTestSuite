import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.io.FileReader;

public class SeleniumTests {
    private final String ADD_DOMAIN_FORM_DATA = "testData\\AddDomainFormData.json";
    private final String INVALID_INPUT_BORDER_COLOR = "rgba(255, 0, 0, 1)";

    @BeforeClass
    public static void setupChrome() {
        Selenium.setupChrome();
        Selenium.maximizeBrowserWindow();
        Selenium.implicitlyWait();
    }

    @Test
    public void testAddDomain() throws Exception {
        Object data = readJson(ADD_DOMAIN_FORM_DATA).get(1);
        Selenium.goToWebAddress("http://localhost:3000/domains");
        Selenium.findElementByCss("#root > div:nth-child(2) > div > div > div > button").click();    //<========== button needs a unique class or name asap
        addDomainFromJson(data);
        if (isDataValid(data)) {
            Assert.assertFalse("Modal is: open, expected: closed", isModalOpen());
        }
        else {
            Assert.assertTrue("Modal is: closed, expected: open", isModalOpen());
            JSONArray expectedFailedFields = getExpectedFailedFields(data);
            for (Object expectedFailedField : expectedFailedFields) {
                String fieldName = expectedFailedField.toString();
                WebElement field = Selenium.findElementByName(fieldName);
                String borderColor = field.getCssValue("border-top-color");
                Assert.assertEquals(fieldName + " border color", INVALID_INPUT_BORDER_COLOR, borderColor); // <==== norim raudono border an inputo
            }
        }
    }

    @AfterClass
    public static void closeBrowser() {
        Selenium.closeBrowser();
    }

    private static JSONArray readJson(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(reader);
    }

    private static void addDomainFromJson(Object data) {
        JSONObject dataJson = (JSONObject) data;
        Selenium.addDomain(dataJson.get("serviceName").toString(), dataJson.get("url").toString(),
                dataJson.get("serviceType").toString(), dataJson.get("method").toString(),
                (Boolean) dataJson.get("auth"), dataJson.get("user").toString(), dataJson.get("password").toString(),
                dataJson.get("parameters").toString(), dataJson.get("email").toString(),
                dataJson.get("interval").toString(), (Boolean) dataJson.get("active"));
    }

    private static JSONArray getExpectedFailedFields(Object data) {
        JSONObject dataJson = (JSONObject) data;
        return (JSONArray) dataJson.get("expectedFailedFields");
    }

    private static boolean isDataValid(Object data) {
        return getExpectedFailedFields(data).size() < 1;
    }

    private static boolean isModalOpen() {
        String bodyClass = Selenium.findElementByCss("body").getAttribute("class");
        System.out.println(bodyClass);
        return bodyClass.equals("modal-open");
    }
}
