public class Test {
    public static void main(String[] args) {
        Fetch api = new Fetch("https://api.imgflip.com/get_memes");
        api.getRequest();
    }
}
