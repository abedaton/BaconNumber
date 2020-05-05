import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;

import java.util.*;

public class BaconGraph {
    private Graph graph;
    private final ArrayList<Person> people;
    HashMap<String, ArrayList<Integer>> filmsToActors;
    HashMap<String, String> translatedFilms = null;

    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param ppeople List of person
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public BaconGraph(ArrayList<Person> ppeople, HashMap<String, String> ptranslated){
        this.translatedFilms = ptranslated;
        this.people = ppeople;
        createGraph();
    }

    public BaconGraph(ArrayList<Person> ppeople) {
        this.people = ppeople;
        createGraph();
    }

    private void createGraph(){
        filmsToActors = new HashMap<>();
        int person = 0;
        for(Person i : people){
            for (String film : i.getFilms()){
                if (filmsToActors.containsKey(film)){
                    filmsToActors.get(film).add(person);
                } else {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(person);
                    filmsToActors.put(film, tmp);
                }
            }
            person++;
        }

        this.graph = new Graph(people.size());

        boolean done;
        for (Map.Entry<String, ArrayList<Integer>> someone :  filmsToActors.entrySet()){
            for (int person1 = 0; person1 < someone.getValue().size(); person1++){
                for (int person2 = person1 + 1; person2 < someone.getValue().size(); person2++){
                    Iterable<Integer> neighbours = graph.adj(someone.getValue().get(person1));
                    done = false;
                    for (int neighbourID : neighbours){
                        if (neighbourID == someone.getValue().get(person2)){
                            done = true;
                        }
                    }
                    if (!done) {
                        graph.addEdge(someone.getValue().get(person1), someone.getValue().get(person2));
                    }
                }
            }
        }
        System.out.println("Graph Generated");
    }

    public String pathFromActor(String actor1, String actor2){
        return actor1.equalsIgnoreCase(actor2) ? "You entered the same actors." : findPath(actor1, actor2);
    }

    public String pathToBacon(String actor2){
        return findPath("Kevin Bacon", actor2);
    }

    private String findPath(String actor1, String actor2){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Path from ").append(actor1).append(" to ").append(actor2).append(": \n");

        Integer target1 = findGraphIdFromName(actor1);
        Integer target2 = findGraphIdFromName(actor2);

        if (target1 != null && target2 != null) {
            BreadthFirstPaths bfs = new BreadthFirstPaths(graph, target1);
            Iterable<Integer> path = bfs.pathTo(target2);
            List<String> name = new ArrayList<>();
            name.add(actor1);
            List<String> films = new ArrayList<>();
            List<String> tmpFilm;
            int previous = target1;
            if (path != null) {
                for (int person : path) {
                    tmpFilm = people.get(person).getFilms();
                    if (person != previous) {
                        for (String film : tmpFilm) {
                            if (people.get(previous).getFilms().contains(film)) {
                                films.add(film);
                                previous = person;
                                break;
                            }
                        }
                        name.add(people.get(person).getName());
                    }
                }
            }
            assert name.size() == films.size();
            for (int actor = 0; actor < name.size() - 1; actor++) {
                stringBuilder.append("- ").append(name.get(actor)).append(" Played with ").append(name.get(actor + 1)).append(" in the film ");
                if (translatedFilms != null){
                    stringBuilder.append(translatedFilms.get(films.get(actor)));
                } else {
                    stringBuilder.append(films.get(actor));
                }
                stringBuilder.append("\n");
            }
        } else {
            System.out.println("Unknown actor.");
            System.exit(1);
        }

        return stringBuilder.toString();

    }

    private Integer findGraphIdFromName(String name){
        for (Person person : people){
            if (person.getName().equals(name)){
                return person.getGraphId();
            }
        }
        return null;
    }

    public int diameter(){
        return diameter(graph.V(), -1, false);
    }

    public int diameter(boolean heuristic){
        return diameter(2000, (int) (Math.random() * graph.V()), heuristic);
    }

    public int diameter(boolean heuristic, int constant){
        return diameter(constant, (int) (Math.random() * graph.V()), heuristic);
    }

    private int diameter(int constant, int random, boolean heuristic){
        if (heuristic){
            System.out.println("Calculating heuristic diameter of the graph...");
        } else {
            System.out.println("Calculating diameter of the graph...");
        }
        int maxDia = -1;
        BreadthFirstPaths bfs;
        for (int first = 0; first < constant; first++){
            if (heuristic) {
                bfs = new BreadthFirstPaths(graph, random);
            } else {
                bfs = new BreadthFirstPaths(graph, first);
            }
            for (int second = 0; second < graph.V(); second++){
                if (bfs.hasPathTo(second)) {
                    int len = bfs.distTo(second);
                    if (len > maxDia) {
                        maxDia = len;
                        if (heuristic) random = second;
                    }
                }
            }
        }
        return maxDia;
    }

}
