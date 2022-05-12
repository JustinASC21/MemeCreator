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
        try {
            f.createNewFile();
            s = new Scanner(f);
        }
        catch (IOException ie) {
            System.out.println("Can not create file");
        }


    }
    public boolean doesExist() {
        return f.exists();
    }
    public void write(String text) {
        if (f.canWrite()) {
            try {
                FileWriter fWriter = new FileWriter(f.getPath());
                fWriter.write(text);
                fWriter.close();
            }
            catch (IOException i) {
                System.out.println("Can not create file");
            }

        }
    }
    public String read() {
        if (f.canRead() && s.hasNextLine()) {
            return s.nextLine();
        }
        return null;
    }

    public File getF() {
        return f;
    }
}
