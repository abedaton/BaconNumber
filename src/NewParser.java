import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewParser {
    List<Person> people;
    Database db;
    int a = 0;
    int b = 0;
    int count = 0;

    public NewParser(Database db){
        this.people = new ArrayList<>();
        this.db = db;
    }

    public List<Person> parseName_basic(String fileName) {

        List<String> list;
        try (Stream<String> stream = Files.lines(Paths.get(fileName)).skip(1)) {
            list = stream.collect(Collectors.toList());
            list.forEach(this::handleLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(a);
        System.out.println(b);
        return people;

    }

    private void handleLine(String line) {
        List<String> parsedLine = Arrays.asList(line.split("\t"));
        List<String> professions = Arrays.asList(parsedLine.get(4).split(","));
        if (professions.contains("actor") || professions.contains("actress")){
//        if (parsedLine.get(4).contains("actor") || parsedLine.get(5).contains("actress")){
            a++;
            if (!parsedLine.get(5).contains("\\N")){
                b++;
                int birthYear;
                try {
                    birthYear = Integer.parseInt(parsedLine.get(2));
                } catch (NumberFormatException notUsed) {
                    birthYear = -1;
                }
                int deathYear;
                try {
                    deathYear = Integer.parseInt(parsedLine.get(3));
                } catch (NumberFormatException notUsed) {
                    deathYear = -1;
                }
                db.addPerson(parsedLine.get(0), parsedLine.get(1), birthYear, deathYear, parsedLine.get(4), parsedLine.get(5));
                count++;
//                people.add(new Person(parsedLine.get(0), parsedLine.get(1), birthYear, deathYear, Arrays.asList(parsedLine.get(4).split(",")), Arrays.asList(parsedLine.get(5).split(","))));
                System.out.println("actor added " + count);
            }
        }
    }
}
