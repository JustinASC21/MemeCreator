import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileReadWrite {
    private File f;
    private Scanner s;

    public FileReadWrite(String src) {
        f = new File(src);
    }
    public boolean doesExist() {
        try {
            s = new Scanner(f);
            return true;
        }
        catch (FileNotFoundException nf) {
            return false;
        }
    }
    public void fileWrite(String text) {
            try {
                System.out.println("Write method");
                FileWriter fWriter = new FileWriter(f.getPath());
                fWriter.write(text);
                fWriter.close();
            }
            catch (IOException i) {
                System.out.println("Can not create file");
            }

    }
    public String fileRead() {
        if (f.canRead() && s.hasNextLine()) {
            return s.nextLine();
        }
        return null;
    }

    public File getF() {
        return f;
    }
}
