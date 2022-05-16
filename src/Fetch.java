import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
        // check if file with api request data already exists, if not make request and
        // create it

        if (!f.doesExist()) { // if file does not exists then write a new file

            try {
                // create URI with api url
                URI urlEncoded = new URI(this.apiUrl);
                // create a new httpBuilder with URI param to send requests
                HttpRequest req = HttpRequest.newBuilder(urlEncoded).GET() // set the URI to api URl and set the request
                                                                           // to GET
                        .build(); // build request
                // get response as string with request and client
                try {
                    HttpResponse<String> response = requestClient.send(req, HttpResponse.BodyHandlers.ofString()); // get
                                                                                                                   // response
                    // use Json object to encode to easy to access object
                    // use parser to parse string information into json
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject obj = (JSONObject) ((JSONObject) parser.parse(response.body())).get("data"); // json
                                                                                                                // object
                                                                                                                // with
                                                                                                                // meme
                                                                                                                // key
                        responseData = (JSONArray) (obj.get("memes"));
                        // System.out.println(responseData);
                        f.fileWrite(obj.toString()); // write Json object to file
                        System.out.println("First time Written to file");
                    } catch (ParseException p) {
                        System.out.println("Data was not able to be retrieved");
                    }

                } catch (IOException e) {
                    System.out.println("Error when sending");
                } catch (InterruptedException a) {
                    System.out.println("Operation has been interrupted, try again later...");
                }

            } catch (URISyntaxException e) {
                // catch throws exception from encoding
                System.out.println("Error: Unacceptable URL");
            }
        } else {
            // read off the existing file and set it to response data 
            JSONParser parser = new JSONParser();
            try {
                JSONObject obj = (JSONObject) parser.parse(f.fileRead());
                responseData = (JSONArray) obj.get("memes"); // get array of memes
            }
              catch (ParseException pe) {
                System.out.println("Can not parse file");
            }
        }

    }
    public String postRequest(String memeID,int numBoxes) {
        // numBoxes refers to text fields for memes
        JSONArray boxes = new JSONArray();
        for (int i = 0; i < numBoxes; i ++) { // loop thru how many num boxes there are creating the text
            JSONObject tempJO = new JSONObject();
            tempJO.put("text", "sample");
            tempJO.put("color","#FFFFFF");
            tempJO.put("outline_color","#000000");
            boxes.add(tempJO); // add the json object for one box to the array
        }

        // data to sent with request
        Map<Object,Object> data = new HashMap<>();
        data.put("template_id",memeID);
        data.put("username","JustinLema");
        data.put("password","tech1234");
        data.put("boxes",boxes);



//        try { // trying to get http parameters to send
//            String urlParameters = "template_id=247375501&username=JustinLema&password=tech1234&boxes=" + boxes.toString();
//            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
//            int postDataLength = postData.length;
//            String request = apiUrl;
//            URL url = new URL(request);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setInstanceFollowRedirects(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("charset", "utf-8");
//            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
//            conn.setUseCaches(false);
//            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//                wr.write(postData);
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//            StringBuilder sb = new StringBuilder();
//            String output;
//            while ((output = br.readLine()) != null) {
//                sb.append(output);
//            }
//            System.out.println(sb.toString());
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
//                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")

                .POST(HttpRequest.BodyPublishers.ofString(boxes.toString()))
                .build();
        try { // sends json info, but http parameters needed
                HttpResponse<String> response = requestClient.send(postReq, HttpResponse.BodyHandlers.ofString());
                System.out.println("Successful connection");
                return response.body();
            } catch (InterruptedException | IOException ie) {
                System.out.println("Error on sending post request");
            }
        return null;
    }
    public JSONObject getResponseArrayAt(int index) {
        return (JSONObject) responseData.get(index);
    }
    public String getURL() {
        return apiUrl;
    }
}
