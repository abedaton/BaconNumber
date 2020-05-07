import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * a parser class that will have some utility methods to parse some files
 */
public class Parser {
    public ArrayList<Person> people;
    private HashMap<String, String> translatedFilms;
    private String language;

    private int graphId = 0;
    private int count = 0;
    private int limit;

    /**
     * Default constructor, will instantiate an empty ArrayList of People
     */
    public Parser(){
        this.people = new ArrayList<>();
    }

    /**
     *
     * @param fileName The path of the file that should be parsed
     * @return a call to parseNameBasic with the given filename and the maximum value of an integer to read the number of line
     */
    public ArrayList<Person> parseNameBasic(String fileName){
        return parseNameBasic(fileName, Integer.MAX_VALUE);
    }

    /**
     * This method will parse a file like name.basic.tsv, the method will call {@code handleLine} every line
     * @param fileName The path of the file that should be parsed
     * @param limit Limit the number of line that should be parsed
     * @return and ArrayList containing all the people that has been parsed
     */
    public ArrayList<Person> parseNameBasic(String fileName, int limit) {
        this.limit = limit;
        if (limit == Integer.MAX_VALUE){
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

    /**
     * Method that will parse a single line and create a person object if it can
     * @param line The raw line that should be parsed
     */
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

    /**
     * This method will parse a file like title.akas.tsv, it will call {@code handleAkasLine} for every line it finds
     * @param filename The path of the file that should be parsed
     * @param language The language that we should get for the films ex: US, FR, CA, JP...
     * @return a HashMap with a tconst as a key and a translated film as a value
     */
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

        System.out.println( translatedFilms.size() + " films have been translated to " + language);
        return translatedFilms;
    }

    /**
     * Method that will parse a single line and get the correct language of the name in the line
     * @param line The raw line that should be parsed
     */
    private void handleAkasLine(String line) {
        List<String> parsedLine = Arrays.asList(line.split("\t"));
        if ((parsedLine.get(3).equalsIgnoreCase(language) || parsedLine.get(4).equalsIgnoreCase(language)) && !translatedFilms.containsKey(parsedLine.get(0))) {
            translatedFilms.put(parsedLine.get(0), parsedLine.get(2));
        }
    }
}
