import utils.Graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BeaconGraph {

    Database db;
    Graph graph;
    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param people List of person
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public BeaconGraph(List<Person> people) {

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

    public BeaconGraph(Database db) throws SQLException {


        String sql = "SELECT * FROM PeopleInFilms LIMIT 0, 1000000;";
        String sql2 = "SELECT COUNT(*) c FROM PeopleInFilms;";
        Statement stmt = db.conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        Statement stmt2 = db.conn.createStatement();
        ResultSet result2 = stmt2.executeQuery(sql2);


        int nbrFilms = result2.getInt("c");
        System.out.println("Number of films: " + nbrFilms);
        Graph graph = new Graph(nbrFilms);


        int a = 0;
        while (result.next()){
            Set<String> people = new HashSet<>(Arrays.asList(result.getString("People").split(",")));
            System.out.println(a + ": Number of Actor for film " + result.getString("tconst") + ": " + people.size());
            List<int[]> combinations = generate(people.size(), 2);
            for (int[] combinaison : combinations){
                graph.addEdge(combinaison[0], combinaison[1]);
            }

//            for (int person1 = 0; person1 < people.size(); person1++) {
//                for (int person2 = 0; person2 < people.size(); person2++) {
//                    if (person1 != person2) {
//                        graph.addEdge(person1, person2);
//                    }
//                }
//            }
            a++;
        }

        System.out.println("finito, got " + a);
        Iterable<Integer> plop = graph.adj(30);
        plop.forEach(System.out::println);

    }

    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 0, n-1, 0);
        return combinations;
    }

}
