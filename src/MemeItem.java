import org.json.simple.JSONObject;

public class MemeItem {
    private String name;
    private long width;
    private long height;
    private String id;
    private String url;
    private long boxCount;

    public MemeItem(JSONObject info) {
        name = info.get("name").toString();
        width = (long) info.get("width");
        url = info.get("url").toString();
        height = (long) info.get("height");
        id = info.get("id").toString();
        boxCount = (long) info.get("box_count");
    }

    public String getImageURL() {
        return url;
    }
    public String getName() {
        return name;
    }
    public long getBoxCount() {
        return boxCount;
    }

    public String getId() {
        return id;
    }
}
