import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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
    private JLabel memeLabel;
    private JFrame greetingWindow;
    private JFrame memeSelectionWindow;
    private JFrame memeEditorWindow;
    private JScrollPane memeInfo;
    private JTextArea greetingDisplay,textDisplay,memeEditorDisplay;
    private JTextField topCaptionInput, bottomCaptionInput, memeInput, nameInput;

    public Interface() {
        input = new Scanner(System.in);
        // make api call before to get data first
        imageWriter = new FileReadWrite("src/images");
        apiClient = new Fetch("https://api.imgflip.com/get_memes");
        apiClient.getRequest();
        
        // displays the acceptable memes in a scroll
        JTextArea memeList = new JTextArea(30,35);
        memeList.setEditable(false); // make it so user cant edit list
        fillMemeCollection(); // fill up with acceptable memes
        loadMemes(memeList); // load to jTextField
        // create jfield for displaying acceptable memes and make it scrollable
        memeInfo = new JScrollPane(memeList);
        memeInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // set up windows
        greetingWindow = new JFrame("Meme Creator");
        greetingWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        memeSelectionWindow = new JFrame();
        memeSelectionWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        memeEditorWindow = new JFrame();
        memeEditorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up the text input question and input fields
        Font kanitFont = new Font("Kanit",Font.BOLD,18);
        greetingDisplay = new JTextArea();
        greetingDisplay.setFont(kanitFont);
        greetingDisplay.setEditable(false);
        greetingDisplay.setText("Welcome, please enter a username so you can store your memes!");
        textDisplay = new JTextArea();
        textDisplay.setEditable(false);
        textDisplay.setFont(kanitFont);
        memeEditorDisplay = new JTextArea();
        memeEditorDisplay.setEditable(false);
        memeEditorDisplay.setFont(kanitFont);

        nameInput = new JTextField();
        memeInput = new JTextField();
        topCaptionInput = new JTextField();
        bottomCaptionInput = new JTextField();
        memeLabel = new JLabel(); // set up meme display image 
        
        setGreetingWindow();
        runInterface();
    }
    public void setGreetingWindow() { 
        // add textdisplay and textInput to the greetingWindow with a button to read action
        // add to the greetingWindow
        greetingWindow.add(greetingDisplay,BorderLayout.NORTH);
        greetingWindow.add(nameInput,BorderLayout.CENTER);
        greetingWindow.add(createButton("Submit Name"),BorderLayout.SOUTH);

        // show the frame by compiling it
        greetingWindow.pack();
        greetingWindow.setVisible(true);

    }
    public void setMemeSelectionWindow() {
        if (memeSelectionWindow.getTitle().equals("")) {
            // this means that the program runs this for the first time
            memeSelectionWindow.setTitle("User: " + user); // update title

            // set up the memeinfo window
            memeSelectionWindow.add(textDisplay,BorderLayout.NORTH);
            JPanel memeJPanel = new JPanel();
            memeJPanel.setLayout(new BoxLayout(memeJPanel, BoxLayout.Y_AXIS)); // set alignment vertically
            memeJPanel.add(memeInfo);
            memeInput.setText("");
            memeJPanel.add(memeInput);
            memeSelectionWindow.add(memeJPanel,BorderLayout.CENTER);
            JPanel memeSelectionInputPanel = new JPanel(); // panel to store the inputs

            //  memeSelectionInputPanel.
            //  memeSelectionInputPanel.add(textInput);
            memeSelectionInputPanel.add(createButton("Select Meme"));
            memeSelectionInputPanel.add(createButton("Change Username"));
            memeSelectionWindow.add(memeSelectionInputPanel,BorderLayout.SOUTH); // add panel with inputs

            memeSelectionWindow.invalidate();
            memeSelectionWindow.validate();
            memeSelectionWindow.repaint();
            memeSelectionWindow.pack();
            memeSelectionWindow.setVisible(true);
            System.out.println("First time creating window");
        }
        else {
            // not the first time because the title of the window has a value
            memeSelectionWindow.setTitle("User: " + user);
            memeSelectionWindow.pack();
            memeSelectionWindow.setVisible(true);
        }

    }
    public void setMemeEditorWindow(int memeIndex) {
        if (memeEditorWindow.getTitle().equals("")) {
            // first time because title is empty
            // change the url to handle post requests
            apiClient.setURL("https://api.imgflip.com/caption_image");

            memeEditorWindow.setTitle("User Editing: " + user); // update title
            // set up the meme editor window
            memeEditorWindow.add(memeEditorDisplay,BorderLayout.NORTH);
            setMemeImage(memeCollection.get(memeIndex - 1)); // offset the index
            memeEditorWindow.add(memeLabel,BorderLayout.CENTER);
            JPanel editorInputs = new JPanel();
            editorInputs.setLayout(new BoxLayout(editorInputs, BoxLayout.Y_AXIS)); // set vertical layout
            editorInputs.add(topCaptionInput);
            editorInputs.add(bottomCaptionInput);
            editorInputs.add(createButton("Submit Captions"));
            memeEditorWindow.add(editorInputs,BorderLayout.SOUTH);
            memeEditorWindow.pack();
            memeEditorWindow.setVisible(true);
        }
        else {
            // update the editor window
            memeEditorWindow.setTitle("User Editing: " + user);
            setMemeImage(memeCollection.get(memeIndex - 1));
            // change location here
        }

    }
       
    public void runInterface() {

        System.out.println("Great, nice to meet you " + user + ". Let's get started with some memes!");
        // set to post request url
        
        // while (true) {

        //     System.out.print("There are 100 memes so please select a number between 1 and 100 \n(If you want to edit a meme that you have already created, please select the same meme under the same username.) : ");
        //     int index = input.nextInt() - 1;
        //     MemeItem selectedMeme = new MemeItem(apiClient.getResponseArrayAt(index)); // create meme object from information passed
        //     // use FileReadWrite object to write image with meme picture
        //     // image title below
        //     if (selectedMeme.getBoxCount() > 2) {
        //         System.out.println("Sorry, currently we only support 2 caption memes");
        //     }
        //     else {
        //         String imgTitle = user + "_" + selectedMeme.getName();
        //         imageWriter.setSrc("src/images/" + imgTitle + ".jpg"); // change the source of the file
        //         imageWriter.imageSave(selectedMeme.getImageURL(), imgTitle + ".jpg"); // create the image file
        //         // need to get memes with json array from fetch class - dont duplicate make method to get information
        //         // get string and use that string in constructor for meme item -> parse information\
        //         input.nextLine(); // this avoids skipping the first input
        //         System.out.print("Based on the image, please enter a phrase for the top caption: ");
        //         String text1 = input.nextLine();
        //         System.out.print("Based on the image, please enter a phrase for the bottom caption: ");
        //         String text2 = input.nextLine();
        //         String memeURL = apiClient.postRequest(selectedMeme.getId(),(int)selectedMeme.getBoxCount(),text1,text2);
        //         // have parse method in post request to add %20 for spaces
        //         // use file read write object to save the new image over the original
        //         imageWriter.imageSave(memeURL, selectedMeme.getName());
        //     }

        // }
        

    }
    public JButton createButton(String label) {
        JButton localButton = new JButton(); // create button to select meme
        localButton.setText(label); // make a function to automatically do this and return the button
        localButton.addActionListener(this);
        return localButton;
    }
    public void setMemeImage(MemeItem meme) {
        // display meme from index
        String imgTitle = user + "_" + meme.getName();
        imageWriter.setSrc("src/images/" + imgTitle + ".jpg"); // change the source of the file // saves the image to the folder 
        imageWriter.imageSave(meme.getImageURL(), imgTitle + ".jpg");
        ImageIcon image = new ImageIcon("src/images/" + imgTitle + ".jpg"); // check here * user name may skew
        Image imageData = image.getImage(); // transform it
        Image scaledImage = imageData.getScaledInstance((int)meme.getWidth(), (int)meme.getHeight(), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        image = new ImageIcon(scaledImage);  // transform it back
        memeLabel.setIcon(image);
    }
    public void loadMemes(JTextArea memeList) {
        String displayText = "";
        for (int index = 0; index < memeCollection.size(); index++) {
            displayText += (index + 1) + ". " + memeCollection.get(index).getName() + "\n";
        }
        memeList.setText(displayText);
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
        JButton button = (JButton) ae.getSource();
        String buttonName = button.getText(); // returns the label, so can distinguish between buttons
        if (buttonName.equals("Submit Name")) {
            // get user after submission
            this.user = nameInput.getText();
            this.textDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.");
            greetingWindow.setVisible(false);
            setMemeSelectionWindow();
        }
        if (buttonName.equals("Select Meme")) {
            // get meme selection number from the text input and redirect to meme display page
            try {
                int memeNumber = Integer.parseInt(memeInput.getText());
                if (memeNumber <= memeCollection.size()) {
                    // integer is within range so selection is ok
                    textDisplay.setText("You can edit the captions here by changing the top caption and bottom caption.\n Make sure to submit the captions!");
                    setMemeEditorWindow(memeNumber);
                }
                else {
                    // selection is out of range
                    textDisplay.setText("");
                    textDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.\n**Please enter a number within the range available!**");
                    memeSelectionWindow.pack();
                }
            }
            catch (NumberFormatException nf) {
                memeInput.setText("");
                textDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.\n**Please enter a valid number!**");
                memeSelectionWindow.pack();
            }
        }
        if (buttonName.equals("Change Username")) {
            // redirect to original page with function
            //            setGreetingWindow();
            nameInput.setText("");
            greetingWindow.setVisible(true);
        }

        if (buttonName.equals("Submit Captions")) {
            System.out.println("Submitting Captions");
            //api post request here
        }
    }
}
