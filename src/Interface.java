import java.io.File;
import java.util.Scanner;
public class Interface {
    private String user;
    private Scanner input;
    

    public Interface() {
        input = new Scanner(System.in);
        System.out.print("Welcome user, enter a username: ");
        user = input.nextLine();
    }

    public void runInterface() {
        FileReadWrite imageWriter = new FileReadWrite("src/images");
        Fetch api = new Fetch("https://api.imgflip.com/caption_image");
        System.out.println(api.postRequest("247375501",4));
//        while (false) {
//            System.out.println("Great, nice to meet you " + user + ". Let's get started with some memes!");
//            System.out.print("There are 100 memes (Hardcoded here) so please select a number between 1 and 100: ");
//            int index = input.nextInt() - 1;
//            MemeItem selectedMeme = new MemeItem(api.getResponseArrayAt(index)); // create meme object from information passed
//            // use FileReadWrite object to write image with meme picture
//            // image title below
//            String imgTitle = user + "_" + selectedMeme.getName();
//            imageWriter.setSrc("src/images/" + imgTitle + ".jpg"); // change the source of the file
//            imageWriter.imageSave(selectedMeme.getImageURL(), imgTitle + ".jpg"); // create the image file
//            // need to get memes with json array from fetch class - dont duplicate make method to get information
//            // ooh get string and use that string in constructor for meme item -> parse information
//        }
        

    }
}
