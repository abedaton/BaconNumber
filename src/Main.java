import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Parser parser = new Parser();

        ArrayList<Person> people = parser.parseName_basic("datasets/name.basics.tsv", 100000);
        HashMap<String, String> translatedFilm = parser.parseTitleAkas("datasets/title.akas.tsv", "US");

        BeaconGraph beaconGraph = new BeaconGraph(people, translatedFilm);
//        String path = beaconGraph.pathToBacon("Morgan Freeman");
//        System.out.println(path);
        System.out.println(beaconGraph.pathFromActor("Natalie Portman", "Natalie Portman"));
//        System.out.println(beaconGraph.diameter());


    }
}
