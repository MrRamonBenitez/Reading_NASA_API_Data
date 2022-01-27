import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        String remoteURL = "https://api.nasa.gov/planetary/apod?api_key=";
        String apiKey = "UwvRzShCfqII1YYwFGRjcC4p7oZT5ys2SoSbJBh8";

        CloseableHttpClient httpClient = createHttpClient();
        String targetURL = remoteURL + apiKey;
        CloseableHttpResponse response = getResponse(httpClient, targetURL);

        String json = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        NASAResponse nAsAResponse = jsonToNASAResponse(json);

        targetURL = nAsAResponse.getUrl();

        response = getResponse(httpClient, targetURL);

        

        System.out.println(nAsAResponse);




        response.close();
        httpClient.close();
    }

    public static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setUserAgent("RU Netology")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

    public static CloseableHttpResponse getResponse(CloseableHttpClient httpClient, String targetURL) throws IOException {
        HttpGet request = new HttpGet(targetURL);
        request.setHeader(HttpHeaders.ACCEPT, org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType());
        return httpClient.execute(request);
    }

    public static NASAResponse jsonToNASAResponse(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, NASAResponse.class);
    }

}





