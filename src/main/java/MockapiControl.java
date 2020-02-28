import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class MockapiControl {
    static void setDefaults(String code, String delay) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut httpget = new HttpPut("http://88.222.15.11:8080/mockapi/?code=" + code + "&delay=" + delay);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
