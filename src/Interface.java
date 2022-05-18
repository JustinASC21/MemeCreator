import java.io.File;
import java.util.Scanner;
public class Interface {
    private String user;
    private Scanner input;
    

    public Interface() {
        input = new Scanner(System.in);
        System.out.print("Welcome user, enter a username so you can store your memes: ");
        user = input.nextLine();
    }

    public void runInterface() {
        FileReadWrite imageWriter = new FileReadWrite("src/images");
        Fetch api = new Fetch("https://api.imgflip.com/get_memes");
        api.getRequest();
        System.out.println("Great, nice to meet you " + user + ". Let's get started with some memes!");
        // set to post request url
        api.setURL("https://api.imgflip.com/caption_image");
        while (true) {

            System.out.print("There are 100 memes so please select a number between 1 and 100 \n(If you want to edit a meme that you have already created, please select the same meme under the same username.) : ");
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
                input.nextLine(); // this avoids skipping the first input
                System.out.print("Based on the image, please enter a phrase for the top caption: ");
                String text1 = input.nextLine();
                System.out.print("Based on the image, please enter a phrase for the bottom caption: ");
                String text2 = input.nextLine();
                String memeURL = api.postRequest(selectedMeme.getId(),(int)selectedMeme.getBoxCount(),text1,text2);
                // have parse method in post request to add %20 for spaces
                // use file read write object to save the new image over the original
                imageWriter.imageSave(memeURL, selectedMeme.getName());
            }

        }
        

    }
}
