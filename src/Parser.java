
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Parser {

    public static ArrayList<Person> parseName_basic(String fileName) {
        ArrayList<Person> people = new ArrayList<>();
        Path path = Paths.get(fileName);

        try (Stream<String> lines = Files.lines(path).skip(1)) {

            for (Object tmp : lines.toArray()) {
                String line = tmp.toString();
                ArrayList<String> parsedLine = new ArrayList<>(Arrays.asList(line.split("\t")));

                ArrayList<String> professions = new ArrayList<>(Arrays.asList(parsedLine.get(4).split(",")));
                if (professions.contains("actor") || professions.contains("actress")) {
                    ArrayList<String> films = new ArrayList<>(Arrays.asList(parsedLine.get(5).split(",")));
                    if (!films.contains("\\N")) {
                        String id = parsedLine.get(0);
                        String name = parsedLine.get(1);
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
                        Person person = new Person(id, name, birthYear, deathYear, professions, films);
                        people.add(person);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return people;
    }

    public static void writeNewNameBacis(ArrayList<Person> people, String newFileTmp){
        File newFile = new File(newFileTmp);
        try {
            if (newFile.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists, reusing old one");
                newFile.delete();
                newFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(newFile);
            try {
                writer.write("nconst\tprimaryName\tbirthYear\tdeathYear\tprimaryProfession\tknownForTitles\n");
            } catch (IOException e){
                e.printStackTrace();
            }
            people.forEach(person -> {
                try {
                    writer.write(person.getId() + "\t" + person.getName() + "\t" + person.getBirthYear() + "\t" +
                            person.getDeathYear() + "\t" + person.getProfession().toString().replace("[", "").replace("]", "").replace(" ", "") + "\t" + person.getFilms().toString().replace("[", "").replace("]", "").replace(" ", "")  + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void writeObjectToFile(Object serObj) {
        try {
            FileOutputStream fileOut = new FileOutputStream("theList");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
