import org.json.simple.JSONObject;

public class MemeItem {
    private final JSONObject memeObj;

    public MemeItem(JSONObject info) {
        memeObj = info;
    }
    public long getWidth() {
        return (long) memeObj.get("width");
    }
    public long getHeight() {
        return (long) memeObj.get("height");
    }
    public String getImageURL() {
        return memeObj.get("url").toString();
    }
    public String getName() {
        return memeObj.get("name").toString();
    }
    public long getBoxCount() {
        return (long) memeObj.get("box_count");
    }
    public String getId() {
        return memeObj.get("id").toString();
    }
    public JSONObject getMemeObj() { return memeObj; }
}
