import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Interface implements ActionListener {
    private String user;
    private Scanner input;
    private ArrayList<MemeItem> memeCollection = new ArrayList<MemeItem>();
    private Fetch apiClient;
    private FileReadWrite imageWriter;
    private JTextArea memeInfo;
    private JTextField textInput;

    public Interface() {
        // make api call before to get data first
        imageWriter = new FileReadWrite("src/images");
        apiClient = new Fetch("https://api.imgflip.com/get_memes");
        apiClient.getRequest();

        // ask for username
        input = new Scanner(System.in);
        System.out.print("Welcome user, enter a username so you can store your memes: ");
        user = input.nextLine();

        fillMemeCollection(); // fill up with acceptable memes
        memeInfo = new JTextArea(30,35);
        memeInfo.setEditable(false); // make it so user cant edit list
        setUpInterface();
        runInterface();
    }
    public void setUpInterface() {


        // create frame and set up its width x height
        JFrame window = new JFrame("Meme Creator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create jfield for displaying acceptable memes and make it scrollable
        JScrollPane scroll = new JScrollPane(memeInfo);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel memePanelList = new JPanel();
        loadMemes(); // loads memes to the textarea
        memePanelList.add(scroll); // add the meme info list to the panel

        //add panels to the window
        window.add(memePanelList);

        // show the frame by compiling it
        window.pack();
        window.setVisible(true);
    }
    public void runInterface() {

        System.out.println("Great, nice to meet you " + user + ". Let's get started with some memes!");
        // set to post request url
        apiClient.setURL("https://api.imgflip.com/caption_image");
        while (true) {

            System.out.print("There are 100 memes so please select a number between 1 and 100 \n(If you want to edit a meme that you have already created, please select the same meme under the same username.) : ");
            int index = input.nextInt() - 1;
            MemeItem selectedMeme = new MemeItem(apiClient.getResponseArrayAt(index)); // create meme object from information passed
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
                String memeURL = apiClient.postRequest(selectedMeme.getId(),(int)selectedMeme.getBoxCount(),text1,text2);
                // have parse method in post request to add %20 for spaces
                // use file read write object to save the new image over the original
                imageWriter.imageSave(memeURL, selectedMeme.getName());
            }

        }
        

    }
    public void displayMeme(int index) {
        // display meme from index
        ImageIcon image = new ImageIcon("src/tmdblogo.jpg");
        Image imageData = image.getImage(); // transform it
        Image scaledImage = imageData.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        image = new ImageIcon(scaledImage);  // transform it back
        JLabel pictureLabel = new JLabel(image);
        JLabel welcomeLabel = new JLabel("   Movies Now Playing!");
        welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.blue);

    }
    public void loadMemes() {
        String displayText = "";
        for (int index = 0; index < memeCollection.size(); index++) {
            displayText += (index + 1) + ". " + memeCollection.get(index).getName() + "\n";
        }
        memeInfo.setText(displayText);
    }
    public void fillMemeCollection() {
        for (int i = 0; i < apiClient.getResponseArraySize(); i++) {
            // loop through all json objects and filter into arraylist only by box counts of 2
            if ((long) apiClient.getResponseArrayAt(i).get("box_count") == 2) {
                MemeItem meme = new MemeItem(apiClient.getResponseArrayAt(i));
                // create acceptable meme object and append it to list
                memeCollection.add(meme);
            }
        }
    }
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae.getSource());
    }
}
