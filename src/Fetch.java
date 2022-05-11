import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.simple.JSONObject;

public class Fetch {
    private String apiUrl;
    private HttpClient requestClient;
    private JSONObject responseData;

    public Fetch(String url) {
            requestClient = HttpClient.newHttpClient();
            responseData = new JSONObject(); // to store data in json
            apiUrl = url;
    }

    public void getRequest() {
        // create URI with api url
        try {
            URI urlEncoded = new URI(this.apiUrl);
            // create a new httpBuilder with URI param to send requests
            HttpRequest req = HttpRequest.newBuilder(urlEncoded).GET() // set the URI to api URl and set the request to GET
                    .build(); // build request
            // get response as string with request and client
            try {
                HttpResponse<String> response = requestClient.send(req, HttpResponse.BodyHandlers.ofString()); // get response
                // use Json object to encode to easy to access object
                JSONObject obj = new JSONObject(); // neecd to convert to json object
                obj.putAll(response.body());
            }
            catch (IOException e) {
                System.out.println("Error when sending");
            }
            catch(InterruptedException a) {
                System.out.println("Operation has been interrupted, try again later...");
            }

        }
        catch (URISyntaxException e) {
            // catch throws exception from encoding
            System.out.println("Error: Unacceptable URL");
        }

    }

    public String getURL() {
        return apiUrl;
    }
}
