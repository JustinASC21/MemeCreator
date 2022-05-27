import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.net.URL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FileEditor {
    private File file;
    private Scanner scanner;

    public FileEditor(String src) {
        file = new File(src);
    }

    public boolean doesExist() {
        try {
            scanner = new Scanner(file);
            return true;
        } catch (FileNotFoundException nf) {
            return false;
        }
    }

    public void fileWrite(String text) {
        try {
            FileWriter fWriter = new FileWriter(file.getPath());
            fWriter.write(text);
            fWriter.close();
        } catch (IOException i) {
            System.out.println("Can not create file");
        }

    }

    public String fileRead() {
        while (scanner.hasNextLine() && file.canRead()) {
            String line = scanner.nextLine();
            if (line.contains("memes")) return line;
        }
        return null;
    }

    public void imageSave(String paramURL, String name) {
        try {
            URL url = new URL(paramURL); // read the url
            BufferedImage image = ImageIO.read(url); // for png
            ImageIO.write(image, "jpg", new File(file.getPath())); // for jpg
        } catch (IOException e) {
            System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
        }
    }
    public void setSrc(String src) {
        file = new File(src);
    }
    public String getSrc() {
        return file.getPath();
    }
    public File getFile() {
        return file;
    }
}
