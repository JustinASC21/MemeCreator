import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

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
        // file object
        FileReadWrite f = new FileReadWrite("data/apiData");
        // check if file with api request data already exists, if not make request and create it
        if (f.doesExist()) {
            // read off the file and set it to response data
            JSONParser parser = new JSONParser();
            try {
                JSONObject obj = (JSONObject) parser.parse(f.read());
                responseData = (JSONArray) () obj; // work on here more
            }
            catch (ParseException pe) {
                System.out.println("Can not parse file");
            }
        }
        else { // make request and write data to file
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
                    // use parser to parse string information into json
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject obj = (JSONObject) parser.parse(response.body());
                        responseData = (JSONObject) obj.get("data"));
                    }
                    catch (ParseException p) {
                        System.out.println("Data was not able to be retrieved");
                    }

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

    }

    public String getURL() {
        return apiUrl;
    }
}
