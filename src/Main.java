import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Parser parser = new Parser();

        ArrayList<Person> people = parser.parseName_basic("datasets/name.basics.tsv", 100000);
        HashMap<String, String> translatedFilm = parser.parseTitleAkas("datasets/title.akas.tsv", "US");

        BaconGraph baconGraph = new BaconGraph(people, translatedFilm);
//        String path = baconGraph.pathToBacon("Morgan Freeman");
//        System.out.println(path);
        System.out.println(baconGraph.pathFromActor("Natalie Portman", "Natalie Portman"));
//        System.out.println(baconGraph.diameter());


    }
}
