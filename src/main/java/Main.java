import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String apiKey = "UwvRzShCfqII1YYwFGRjcC4p7oZT5ys2SoSbJBh8";

        CloseableHttpClient httpClient = createHttpClient();
        CloseableHttpResponse response = getResponse(httpClient);

        List<Post> posts = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );

        posts.stream()
                .filter(value -> value.getUpvotes() != null)
                .forEach(System.out::println);

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

    public static CloseableHttpResponse getResponse(CloseableHttpClient httpClient) throws IOException {
        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        request.setHeader(HttpHeaders.ACCEPT, org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType());
        return httpClient.execute(request);
    }

}





