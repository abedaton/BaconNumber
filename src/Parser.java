import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    public ArrayList<Person> people;
    private HashMap<String, String> translatedFilms;
    private String language;


    private int graphId = 0;
    private int count = 0;
    private int limit;

    public Parser(){
        this.people = new ArrayList<>();
    }


    public ArrayList<Person> parseName_basic(String fileName){
        return parseName_basic(fileName, Integer.MAX_VALUE);
    }

    public ArrayList<Person> parseName_basic(String fileName, int limit) {
        this.limit = limit;
        if (count == Integer.MAX_VALUE){
            System.out.println("Starting parsing names...");
        } else {
            System.out.println("Parsing " + limit + " names...");
        }

        try (Stream<String> stream = Files.lines(Paths.get(fileName)).skip(1)) {
            stream.collect(Collectors.toList()).forEach(this::handleLine);
        } catch (IOException e) {
            System.out.println("Can't read file " + fileName + ". The program will now exit");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Parsing names is now finished");
        return people;
    }

    private void handleLine(String line) {
        if (count < limit) {
            List<String> parsedLine = Arrays.asList(line.split("\t"));
            List<String> professions = Arrays.asList(parsedLine.get(4).split(","));
            if (professions.contains("actor") || professions.contains("actress")) {
                if (!parsedLine.get(5).contains("\\N")) {
                    List<String> films = Arrays.asList(parsedLine.get(5).split(","));

                    people.add(new Person(parsedLine.get(1), graphId, films));
                    count++;
                    graphId++;
                }
            }
        }
    }

    public HashMap<String, String> parseTitleAkas(String filename, String language){
        System.out.println("Finding film names in " + language + "...");
        this.language = language;
        this.translatedFilms = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(filename)).skip(1)) {
            stream.collect(Collectors.toList()).forEach(this::handleAkasLine);
        } catch (IOException e) {
            System.out.println("Can't read file " + filename + ". The program will now exit");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("All films have been translated to " + language);
        return translatedFilms;
    }

    private void handleAkasLine(String line) {
        List<String> parsedLine = Arrays.asList(line.split("\t"));
        if ((parsedLine.get(3).equalsIgnoreCase(language) || parsedLine.get(4).equalsIgnoreCase(language)) && !translatedFilms.containsKey(parsedLine.get(0))) {
            translatedFilms.put(parsedLine.get(0), parsedLine.get(2));
        }
    }
}
