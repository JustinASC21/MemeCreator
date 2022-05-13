import java.io.FileNotFoundException;
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
    private JSONArray responseData;

    public Fetch(String url) {
            requestClient = HttpClient.newHttpClient();
            responseData = new JSONArray(); // to store data in json
            apiUrl = url;
    }

    public void getRequest() {
        // file object
        System.out.println("get request called");
        FileReadWrite f = new FileReadWrite("src/data/apiData");
        // check if file with api request data already exists, if not make request and create it

        if (!f.doesExist()) { // if file does not exists then write a new file

            try {
                // create URI with api url
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
                        JSONObject obj = (JSONObject) ((JSONObject) parser.parse(response.body())).get("data"); // json object with meme key
                        responseData = (JSONArray) (obj.get("memes"));
//                        System.out.println(responseData);
                        f.fileWrite(obj.toString()); // write Json object to file
                        System.out.println("First time Written to file");
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
        else {
            // read off the existing file and set it to response data
            JSONParser parser = new JSONParser();
            try {
                JSONObject obj = (JSONObject) parser.parse(f.fileRead());
                responseData = (JSONArray) obj.get("memes"); // get array of memes
                System.out.println(((JSONObject) ((JSONObject) (responseData.get(0))).get("name")));
            }
            catch (ParseException pe) {
                System.out.println("Can not parse file");
            }
        }

    }

    public String getURL() {
        return apiUrl;
    }
}
