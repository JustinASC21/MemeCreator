import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class MemeGUI implements ActionListener {
    private String user;
    private ArrayList<MemeItem> memeCollection = new ArrayList<MemeItem>();
    private FetchData apiClient;
    private FileEditor imageWriter;
    private Font kanitFont, poppinsFont;
    private JLabel memeLabel;
    private JFrame greetingWindow, memeSelectionWindow, memeEditorWindow;
    private JTextArea memeList;
    private JScrollPane memeScrollableInfo;
    private JTextPane greetingDisplay,memeSelectionDisplay,memeEditorDisplay;
    private JTextField topCaptionInput, bottomCaptionInput, memeInput, nameInput;

    public MemeGUI() {
        // make api call before to get data first
        imageWriter = new FileEditor("src/images");
        apiClient = new FetchData("https://api.imgflip.com/get_memes");
        apiClient.getRequest();

        // set up fonts
        kanitFont = new Font("Kanit",Font.BOLD,20);
        poppinsFont = new Font("Poppins",Font.ITALIC, 16);
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
        memeList = new JTextArea(30,35);
        memeList.setFont(poppinsFont);
        memeList.setEditable(false); // make it so user cant edit list
        fillMemeCollection(); // fill up with acceptable memes
        loadMemes(memeList); // load to jTextField
        // create jfield for displaying acceptable memes and make it scrollable
        memeScrollableInfo = new JScrollPane(memeList);
        memeScrollableInfo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // set up the text input question and input fields

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

            // set up the memeScrollableInfo window
            // add panel to filter results
            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.X_AXIS));
            JLabel searchLabel = new JLabel("Search by name: ");
            searchLabel.setFont(kanitFont);
            searchPanel.add(searchLabel);
            JTextField searchInput = new JTextField();
            searchInput.addActionListener(this);
            searchPanel.add(searchInput);
            JPanel topWindowSection = new JPanel();
            topWindowSection.setLayout(new BoxLayout(topWindowSection, BoxLayout.Y_AXIS));
            topWindowSection.add(memeSelectionDisplay);
            topWindowSection.add(searchPanel); // controls the search controls
            memeSelectionWindow.add(topWindowSection,BorderLayout.NORTH);
            JPanel memeJPanel = new JPanel();
            memeJPanel.setLayout(new BoxLayout(memeJPanel, BoxLayout.Y_AXIS)); // set alignment vertically
            memeJPanel.add(memeScrollableInfo);
            memeInput.setText("");
            memeJPanel.add(memeInput);
            memeSelectionWindow.add(memeJPanel,BorderLayout.CENTER);
            JPanel memeSelectionInputPanel = new JPanel(); // panel to store the inputs
            memeSelectionInputPanel.setLayout(new BoxLayout(memeSelectionInputPanel,BoxLayout.Y_AXIS));
            JPanel memeInputPanel = new JPanel();
            JPanel memeButtonsPanel = new JPanel();
            memeInputPanel.setLayout(new BoxLayout(memeInputPanel,BoxLayout.X_AXIS));
            JLabel memeInputLabel = new JLabel();
            memeInputLabel.setText("Meme Number: ");
            memeInputLabel.setFont(kanitFont);
            memeInputPanel.add(memeInputLabel);
            memeInputPanel.add(memeInput);
            memeButtonsPanel.add(createButton("Select Meme"));
            memeButtonsPanel.add(createButton("Change Username"));
            memeSelectionInputPanel.add(memeInputPanel);
            memeSelectionInputPanel.add(memeButtonsPanel);
            memeSelectionWindow.add(memeSelectionInputPanel,BorderLayout.SOUTH); // add panel with inputs

            memeSelectionWindow.setLocation(0,0);
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
        MemeItem meme = memeCollection.get(memeIndex - 1); // get ref to the meme
        if (memeEditorWindow.getTitle().equals("")) {

            // first time because title is empty
            // change the url to handle post requests
            apiClient.setURL("https://api.imgflip.com/caption_image");
            memeEditorWindow.setTitle("User Editing '" + meme.getName() +  "' Meme #" + memeIndex + ": " + user); // update title
            // set up the meme editor window
            memeEditorWindow.add(memeEditorDisplay,BorderLayout.NORTH);
            setMemeImage(meme,meme.getImageURL()); // offset the index
            memeEditorWindow.add(memeLabel,BorderLayout.CENTER);
            JPanel bottomCaptionPanel = new JPanel();
            bottomCaptionPanel.setLayout(new BoxLayout(bottomCaptionPanel, BoxLayout.X_AXIS));
            JPanel topCaptionPanel = new JPanel();
            topCaptionPanel.setLayout(new BoxLayout(topCaptionPanel,BoxLayout.X_AXIS));
            JLabel bottomCaptionLabel = new JLabel();
            JLabel topCaptionLabel = new JLabel();
            bottomCaptionLabel.setFont(kanitFont);
            topCaptionLabel.setFont(kanitFont);
            bottomCaptionLabel.setText("Bottom Caption: ");
            bottomCaptionLabel.setSize(10,50);
            topCaptionLabel.setText("Top Caption: ");
            bottomCaptionPanel.add(bottomCaptionLabel);
            bottomCaptionPanel.add(bottomCaptionInput);
            topCaptionPanel.add(topCaptionLabel);
            topCaptionPanel.add(topCaptionInput);

            JPanel editorInputs = new JPanel();
            editorInputs.setLayout(new BoxLayout(editorInputs, BoxLayout.Y_AXIS)); // set vertical layout
            editorInputs.add(topCaptionPanel);
            editorInputs.add(bottomCaptionPanel);
            editorInputs.add(createButton("Submit Captions"));
            memeEditorWindow.add(editorInputs,BorderLayout.SOUTH);
            memeEditorWindow.pack();
            memeEditorWindow.setLocation(memeSelectionWindow.getX() + memeSelectionWindow.getWidth(),0);
            memeEditorWindow.setVisible(true);
        }
        else {
            // update the editor window
            memeEditorWindow.setTitle("User Editing '" + meme.getName() + "' Meme #" + memeIndex + ": " + user);
            setMemeImage(meme, meme.getImageURL());
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
        localButton.setFont(new Font("Sans serif",Font.BOLD,18));
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
    public void loadMemes(String keyword) { // edits the text aread object with new updated infromation
        String newDisplayText = "";
        // load memes that match with a specific keyword -> for search function
        for (int index = 0; index < memeCollection.size();index++) {
            MemeItem memeAtIndex = memeCollection.get(index);
            if (memeAtIndex.getName().toLowerCase().indexOf(keyword) != -1) {
                // this means that keyword is present in meme name
                newDisplayText += (index + 1) + ". " + memeAtIndex.getName() + "\n";
            }
        }
       memeList.setText(newDisplayText);
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
        try { // this will only work if the event is from an input, catch statemnet would work for button actions
            JTextField searchField = (JTextField) ae.getSource();
            loadMemes(searchField.getText().toLowerCase()); // edits the text list that is stored in the jscroll pane
            memeSelectionWindow.pack();
        }
        catch (ClassCastException ce) {
            JButton button = (JButton) ae.getSource();
        String buttonName = button.getText(); // returns the label, so can distinguish between buttons
        if (buttonName.equals("Submit Name")) {
            // get user after submission
            this.user = nameInput.getText();
            this.memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a previously saved meme, please select the same meme template under the same username.");
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
                    memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a previously saved meme, please select the same meme template under the same username.");
                    memeEditorDisplay.setText("You can edit the captions here by changing the top caption and bottom caption.\n Make sure to submit the captions!");
                    setMemeEditorWindow(memeNumber);
                }
                else {
                    memeInput.setText("");
                    // selection is out of range
                    memeSelectionDisplay.setText("");
                    memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a previously saved meme, please select the same meme template under the same username.\n**Please enter a number within the range available!**");
                    memeSelectionWindow.pack();
                }
            }
            catch (NumberFormatException nf) {
                memeInput.setText("");
                memeSelectionDisplay.setText("Hello " + this.user + ". Please select from the list of meme templates below by referencing the numbers.\n\nIf you wish to edit a previously saved meme, please select the same meme template under the same username.\n**Please enter a valid number!**");
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
            if (!(topText.equals("") && bottomText.equals(""))) {
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
        }
        
}
