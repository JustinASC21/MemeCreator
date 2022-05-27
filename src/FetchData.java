import java.io.*;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class FetchData {
    private final String USERNAME = "JustinLema";
    private final String PASSWORD = "tech1234";
    private String apiUrl;
    private final HttpClient requestClient;
    private JSONArray responseData;

    public FetchData(String url) {
        requestClient = HttpClient.newHttpClient();
        responseData = new JSONArray(); // to store data in json
        apiUrl = url;
    }

    public void getRequest() {
        // file object
        FileEditor dataFile = new FileEditor("src/data/apiData");
        // check if file with api request data already exists, if not make request and
        // create it

        if (!dataFile.doesExist()) { // if file does not exists then write a new file

            try {
                // create URI with api url
                URI urlEncoded = new URI(this.apiUrl);
                // create a new httpBuilder with URI param to send requests
                HttpRequest req = HttpRequest.newBuilder(urlEncoded).GET() // set the URI to api URl and set the
                    .build(); // build request
                // get response as string with request and client
                try {
                    HttpResponse<String> response = requestClient.send(req, HttpResponse.BodyHandlers.ofString()); // get
                                                                                                                   // response
                    // use Json object to encode to easy to access object
                    // use parser to parse string information into json
                    try {
                        JSONParser parser = new JSONParser();
                        JSONObject obj = (JSONObject) ((JSONObject) parser.parse(response.body())).get("data"); // json object with meme key
                        responseData = (JSONArray) (obj.get("memes"));
                        dataFile.fileWrite(obj.toString()); // write Json object to file
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
                JSONObject obj = (JSONObject) parser.parse(dataFile.fileRead());
                responseData = (JSONArray) obj.get("memes"); // get array of memes
            }
              catch (ParseException pe) {
                System.out.println("Can not parse file");
            }
        }

    }
    public String postRequest(String memeID,String firstText, String secondText) {
        // numBoxes refers to text fields for memes
        // returns revised image url
        // data to send with request
        String urlReqAppend = "?template_id=" + memeID+ "&username=" + USERNAME + "&password=" + PASSWORD + "&text0=" + parseToParam(firstText) + "&text1=" + parseToParam(secondText);
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + urlReqAppend))
                .header("Content-Type", "application/json")
                .build();
        try { // sends json info, but http parameters needed
                HttpResponse<String> response = requestClient.send(postReq, HttpResponse.BodyHandlers.ofString());
                // return the image url
            try {
                JSONParser parser = new JSONParser();
                String newMemeURL =((JSONObject) ((JSONObject) parser.parse(response.body())).get("data")).get("url").toString();
                // String imgURL = ;
                return newMemeURL;
            }
            catch (ParseException pe) {
                System.out.println("Unable to parse information");
            }
            } catch (InterruptedException | IOException ie) {
                System.out.println("Error on sending post request");
            }
        return null;
    }
    public int getResponseArraySize() {
        return responseData.size();
    }
    public JSONObject getResponseArrayAt(int index) {
        return (JSONObject) responseData.get(index);
    }
    public String parseToParam(String text) {
        String newParsedParam = "";
        for (int i = 0; i < text.length(); i ++) {
            if (text.charAt(i) == ' ') {
                newParsedParam += "%20";
            }
            else {
                newParsedParam += text.substring(i,i+1);
            }
        }
        return newParsedParam;
    }
    public String getURL() {
        return apiUrl;
    }
    public void setURL(String newURL) {
        apiUrl = newURL;
    }
}
