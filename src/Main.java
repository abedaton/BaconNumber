import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Parser parser = new Parser();

        ArrayList<Person> people = parser.parseNameBasic("datasets/name.basics.tsv", 1000000);
        HashMap<String, String> translatedFilm = parser.parseTitleAkas("datasets/title.akas.tsv", "US");

        BaconGraph baconGraph = new BaconGraph(people, translatedFilm);
        System.out.println(baconGraph.pathToBacon("Tom Cruise"));
//        System.out.println(baconGraph.pathFromActor("Morgan Freeman", "Kevin Bacon"));

//        System.out.println(baconGraph.pathFromActor("Natalie Portman", "Morgan Freeman"));
//        System.out.println(baconGraph.pathFromActor("Morgan Freeman", "Natalie Portman"));
//        System.out.println(baconGraph.diameter());


    }
}
