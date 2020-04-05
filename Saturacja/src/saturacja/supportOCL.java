package saturacja;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class supportOCL {

    public String readOCLKernel(Path path) {
        String kernel = "";
        try {

            File myObj = new File(path.toString());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                kernel = kernel + data + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nie udalo sie wgrac OpenCL kernel z pliku.");
            e.printStackTrace();
        }
        return kernel;
    }
}
