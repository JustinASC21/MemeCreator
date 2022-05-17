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
        Fetch api = new Fetch("https://api.imgflip.com/get_memes");
        api.getRequest();
        System.out.println("Great, nice to meet you " + user + ". Let's get started with some memes!");
//        System.out.println(api.postRequest("87743020",3,"sample","another%20sample"));
        while (true) {

            System.out.print("There are 100 memes (Hardcoded here) so please select a number between 1 and 100: ");
            int index = input.nextInt() - 1;
            MemeItem selectedMeme = new MemeItem(api.getResponseArrayAt(index)); // create meme object from information passed
            // use FileReadWrite object to write image with meme picture
            // image title below
            if (selectedMeme.getBoxCount() > 2) {
                System.out.println("Sorry, currently we only support 2 caption memes");
            }
            else {
                String imgTitle = user + "_" + selectedMeme.getName();
                imageWriter.setSrc("src/images/" + imgTitle + ".jpg"); // change the source of the file
                imageWriter.imageSave(selectedMeme.getImageURL(), imgTitle + ".jpg"); // create the image file
                // need to get memes with json array from fetch class - dont duplicate make method to get information
                // get string and use that string in constructor for meme item -> parse information\
                System.out.print("Based on the image, please enter a phrase for the top caption: ");
                String text1 = input.nextLine();
                System.out.print("Based on the image, please enter a phrase for the bottom caption: ");
                String text2 = input.nextLine();
                System.out.println(api.postRequest(selectedMeme.getId(),(int)selectedMeme.getBoxCount(),text1,text2));
                // have parse method in post request to add %20 for spaces
            }

        }
        

    }
}
