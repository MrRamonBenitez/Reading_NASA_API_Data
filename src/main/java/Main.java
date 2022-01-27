import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        String remoteURL = "https://api.nasa.gov/planetary/apod?api_key=";
        String apiKey = "zyBh3vaJ95ZL5j0Eyvj1H1nb2oVJkvgm2kG2VkXR";
        String targetURL = remoteURL + apiKey;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = getResponse(httpClient, targetURL);

        NASAResponse nAsAResponse = jsonToObject(response);

        targetURL = nAsAResponse.getUrl();

        String fileName = getFileName(targetURL);

        response = getResponse(httpClient, targetURL);

        getFile(response, fileName);

        response.close();
        httpClient.close();
    }

    public static CloseableHttpResponse getResponse(CloseableHttpClient httpClient, String targetURL) throws IOException {
        HttpUriRequest httpGet = new HttpGet(targetURL);
        return httpClient.execute(httpGet);
    }

    public static NASAResponse jsonToObject(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return mapper.readValue(entity.getContent(), new TypeReference<>() {});
    }

    public static String getFileName(String url) {
        int y = 0;
        int z = url.length();
        int x = z - 1;
        while (x < z) {
            char symbol = url.charAt(x);
            if (symbol == '/') {
                y = x + 1;
                break;
            }
            x--;
        }
        return url.substring(y, z);
    }

    public static void getFile(CloseableHttpResponse response, String fileName) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(fileName);
            entity.writeTo(fos);
            fos.close();
        }
    }

}





