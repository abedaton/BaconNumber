import utils.Graph;

import java.util.*;

public class BeaconGraph extends Graph {

    Database db;

    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param people List of person
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public BeaconGraph(List<Person> people, Database db) {
        super(people.size());
        this.db = db;
        System.out.println(people.size());
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        int person = 0;
        for(Person i : people){
            if (person%10000 == 0){
                System.out.println(person);
            }
            for (String film : i.getFilms()){
                if (map.containsKey(film)){
                    map.get(film).add(person);
                } else {
                    ArrayList<Integer> tmp = new ArrayList<>(person);
                    map.put(film, tmp);
                }
            }
            person++;
        }
        Set<Map.Entry<String, ArrayList<Integer>>> entry = map.entrySet();
        for (Map.Entry<String, ArrayList<Integer>> elem : entry) {
            System.out.println(elem.getKey() + " : " + elem.getValue().size());
        }
    }
}
