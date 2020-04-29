import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestParser {

    ArrayList<String> people;
    Database db;

    public TestParser(Database db){
        this.people = new ArrayList<>();
        this.db = db;
    }


    public ArrayList<String> parseName_basic(String fileName) {
        FileInputStream inputStream = null;
        Scanner scanner = null;

        try {
            inputStream = new FileInputStream(fileName);
            scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                people.add(line);
            }
            if (scanner.ioException() != null){
                throw scanner.ioException();
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            assert scanner != null;
            scanner.close();

        }
        return people;
    }

}
