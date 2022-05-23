import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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
    private ArrayList<MemeItem> memeCollection = new ArrayList<MemeItem>();
    private Fetch apiClient;
    private FileReadWrite imageWriter;
    private JLabel memeLabel;
    private JFrame greetingWindow, memeSelectionWindow, memeEditorWindow;
    private JScrollPane memeInfo;
    private JTextPane greetingDisplay,memeSelectionDisplay,memeEditorDisplay;
    private JTextField topCaptionInput, bottomCaptionInput, memeInput, nameInput;

    public Interface() {
        // make api call before to get data first
        imageWriter = new FileReadWrite("src/images");
        apiClient = new Fetch("https://api.imgflip.com/get_memes");
        apiClient.getRequest();

        // set up the windows here
        greetingWindow = new JFrame("Meme Creator");
        greetingWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        memeSelectionWindow = new JFrame();
        memeSelectionWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        memeEditorWindow = new JFrame();
        memeEditorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void runInterface() {
        // displays the acceptable memes in a scroll
        JTextArea memeList = new JTextArea(30,35);
        memeList.setEditable(false); // make it so user cant edit list
        fillMemeCollection(); // fill up with acceptable memes
        loadMemes(memeList); // load to jTextField
        // create jfield for displaying acceptable memes and make it scrollable
        memeInfo = new JScrollPane(memeList);
        memeInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // set up the text input question and input fields
        Font kanitFont = new Font("Kanit",Font.BOLD,20);
        greetingDisplay = new JTextPane();
        greetingDisplay.setFont(kanitFont);
        greetingDisplay.setEditable(false);
        greetingDisplay.setText("Welcome, please enter a username so you can store your memes!");
        memeSelectionDisplay = new JTextPane();
        memeSelectionDisplay.setEditable(false);
        memeSelectionDisplay.setFont(kanitFont);
        memeEditorDisplay = new JTextPane();
        memeEditorDisplay.setEditable(false);
        memeEditorDisplay.setFont(kanitFont);
        // center text on all displays
        centerText(greetingDisplay);
        centerText(memeSelectionDisplay);
        centerText(memeEditorDisplay);
        // inputs created below
        nameInput = new JTextField();
        memeInput = new JTextField();
        topCaptionInput = new JTextField();
        bottomCaptionInput = new JTextField();
        nameInput.setFont(kanitFont);
        memeInput.setFont(kanitFont);
        topCaptionInput.setFont(kanitFont);
        bottomCaptionInput.setFont(kanitFont);
        memeLabel = new JLabel(); // set up meme display image
        memeLabel.setHorizontalAlignment(JLabel.CENTER);

        setGreetingWindow();
    }
    public void setGreetingWindow() { 
        // add textdisplay and textInput to the greetingWindow with a button to read action
        // add to the greetingWindow
        greetingWindow.add(greetingDisplay,BorderLayout.NORTH);
        greetingWindow.add(nameInput,BorderLayout.CENTER);
        greetingWindow.add(createButton("Submit Name"),BorderLayout.SOUTH);

        // show the frame by compiling it
        greetingWindow.pack();
        greetingWindow.setSize(800,150);
        greetingWindow.setLocationRelativeTo(memeSelectionWindow);
        greetingWindow.setVisible(true);

    }
    public void setMemeSelectionWindow() {
        if (memeSelectionWindow.getTitle().equals("")) {
            // this means that the program runs this for the first time
            memeSelectionWindow.setTitle("User: " + user); // update title

            // set up the memeinfo window
            memeSelectionWindow.add(memeSelectionDisplay,BorderLayout.NORTH);
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

            memeEditorWindow.setTitle("User Editing Meme #" + memeIndex + ": " + user); // update title
            // set up the meme editor window
            memeEditorWindow.add(memeEditorDisplay,BorderLayout.NORTH);
            setMemeImage(memeCollection.get(memeIndex - 1),memeCollection.get(memeIndex - 1).getImageURL()); // offset the index
            memeEditorWindow.add(memeLabel,BorderLayout.CENTER);
            JPanel editorInputs = new JPanel();
            editorInputs.setLayout(new BoxLayout(editorInputs, BoxLayout.Y_AXIS)); // set vertical layout
            editorInputs.add(topCaptionInput);
            editorInputs.add(bottomCaptionInput);
            editorInputs.add(createButton("Submit Captions"));
            memeEditorWindow.add(editorInputs,BorderLayout.SOUTH);
            memeEditorWindow.pack();
            memeEditorWindow.setLocationRelativeTo(memeSelectionWindow);
            memeEditorWindow.setVisible(true);
        }
        else {
            // update the editor window
            memeEditorWindow.setTitle("User Editing Meme #" + memeIndex + ": " + user);
            setMemeImage(memeCollection.get(memeIndex - 1), memeCollection.get(memeIndex - 1).getImageURL());
            // change location here
        }

    }
    public void centerText(JTextPane textContainer) {
        StyledDocument doc = textContainer.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
    public JButton createButton(String label) {
        JButton localButton = new JButton(); // create button to select meme
        localButton.setText(label); // make a function to automatically do this and return the button
        localButton.addActionListener(this);
        return localButton;
    }
    public void setMemeImage(MemeItem meme,String imageURL) {
        // display meme from index
        String imgTitle = user + "_" + meme.getName();
        imageWriter.setSrc("src/images/" + imgTitle + ".jpg"); // change the source of the file // saves the image to the folder 
        imageWriter.imageSave(imageURL, imgTitle + ".jpg");
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
            this.memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.");
            greetingWindow.setVisible(false);
            setMemeSelectionWindow();
        }
        if (buttonName.equals("Select Meme")) {
            // get meme selection number from the text input and redirect to meme display page
            try {
                int memeNumber = Integer.parseInt(memeInput.getText());
                if (memeNumber <= memeCollection.size()) {
                    memeInput.setText("");
                    // integer is within range so selection is ok
                    memeEditorDisplay.setText("You can edit the captions here by changing the top caption and bottom caption.\n Make sure to submit the captions!");
                    setMemeEditorWindow(memeNumber);
                }
                else {
                    memeInput.setText("");
                    // selection is out of range
                    memeSelectionDisplay.setText("");
                    memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.\n**Please enter a number within the range available!**");
                    memeSelectionWindow.pack();
                }
            }
            catch (NumberFormatException nf) {
                memeInput.setText("");
                memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a meme already created, please select the same meme template under the same username.\n**Please enter a valid number!**");
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
            //api post request here
            String topText = topCaptionInput.getText();
            String bottomText = bottomCaptionInput.getText();
            // reset input fields
            topCaptionInput.setText("");
            bottomCaptionInput.setText("");
            // get index of meme object from window title
            if (memeEditorWindow.getTitle() != "") {
                // title is filled with the info
                String title = memeEditorWindow.getTitle();
                int memeIndex = Integer.parseInt(title.substring(title.indexOf("#") + 1,title.indexOf(":"))) - 1;
                MemeItem currentMeme = memeCollection.get(memeIndex);
                String imageURL = apiClient.postRequest(currentMeme.getId(),(int) currentMeme.getBoxCount(),topText,bottomText);
                setMemeImage(currentMeme,imageURL);
                // save the image to the images directory with same file name so it will overwrite
                // this also displays image to window
            }


        }
    }
}
