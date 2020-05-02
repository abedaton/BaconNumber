import utils.Graph;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BeaconGraph implements Serializable {
    transient private Database db;
    private static final long serialVersionUID = -1853510224997155847L;
    private Graph graph;

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

        this.db = db;
        String sql = "SELECT * FROM PeopleInFilms LIMIT 0, 1000000;";
        String sql2 = "SELECT COUNT(*) c FROM PeopleInFilms;";
        Statement stmt = db.conn.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        Statement stmt2 = db.conn.createStatement();
        ResultSet result2 = stmt2.executeQuery(sql2);


        int nbrFilms = result2.getInt("c");
        System.out.println("Number of films: " + nbrFilms);
        graph = new Graph(nbrFilms);


        int a = 0;
        while (result.next()){
            Set<String> people = new HashSet<>(Arrays.asList(result.getString("People").split(",")));
            System.out.println(a + ": Number of Actor for film " + result.getString("tconst") + ": " + people.size());

            int i = 0;
            int j;

            while (i < people.size()){
                j = 0;
                while (j < i){
                    graph.addEdge(i, j);
                    j++;
                }
                i++;
            }
            a++;
        }

        System.out.println("finito, got " + a);
        Iterable<Integer> plop = graph.adj(30);
        plop.forEach(System.out::println);

    }

    public BeaconGraph(){}


    public Graph getGraph(){
        return graph;
    }

}
