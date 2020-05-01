import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewParser {
    List<Person> people;
    Database db;
    int count = 0;

    public NewParser(Database db){
        this.people = new ArrayList<>();
        this.db = db;
    }

    public void parseName_basic(String fileName) {
        db.beginBatch(500, false);
        //db.beginBatch(500, true);
        List<String> list;
        try (Stream<String> stream = Files.lines(Paths.get(fileName)).skip(1)) {
            list = stream.collect(Collectors.toList());
            list.forEach(this::handleLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
        db.doBatchPeople();

        db.doBatchPeopleInFilm();
    }

    private void handleLine(String line) {
        if (count <= 1000000) {
            List<String> parsedLine = Arrays.asList(line.split("\t"));
            List<String> professions = Arrays.asList(parsedLine.get(4).split(","));
            if (professions.contains("actor") || professions.contains("actress")) {
                if (!parsedLine.get(5).contains("\\N")) {
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
                    List<String> films = Arrays.asList(parsedLine.get(5).split(","));
                    films.forEach(film -> {
                        try {
                            db.addFilmToBatch(film, parsedLine.get(0));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    try {
                        db.addPersonToBatch(parsedLine.get(0), parsedLine.get(1), birthYear, deathYear, parsedLine.get(4), parsedLine.get(5));
                    } catch (SQLException e) {
                        System.out.println("impossible d'ajouter cette personne");
                        e.printStackTrace();
                    }
                    //                db.addPerson(parsedLine.get(0), parsedLine.get(1), birthYear, deathYear, parsedLine.get(4), parsedLine.get(5));
                    count++;
                    if (count % 1000 == 0) {
                        System.out.println(count);
                    }
                    //                people.add(new Person(parsedLine.get(0), parsedLine.get(1), birthYear, deathYear, Arrays.asList(parsedLine.get(4).split(",")), Arrays.asList(parsedLine.get(5).split(","))));
                }
            }
        }
    }
}
