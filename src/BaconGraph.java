import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;

import java.util.*;

/**
 * The class that is a Graph, where vertices are people and are linked by edges if they played in a common film or not
 */
public class BaconGraph {
    private Graph graph;
    private final ArrayList<Person> people;
    HashMap<String, ArrayList<Integer>> filmsToActors;
    HashMap<String, String> translatedFilms = null;

    /**
     * Constructor that will set the hashmap for the translated films
     * @param ppeople an ArrayList of Person, represent all the actors that has been parsed previously
     * @param ptranslated a HashMap of type <tconst, translatedName> for the films
     */
    public BaconGraph(ArrayList<Person> ppeople, HashMap<String, String> ptranslated){
        this.translatedFilms = ptranslated;
        this.people = ppeople;
        createGraph();
    }

    /**
     * Constructor if you don't want to translate the films
     * @param ppeople an ArrayList of Person, represent all the actors that has been parsed previously
     */
    public BaconGraph(ArrayList<Person> ppeople) {
        this.people = ppeople;
        createGraph();
    }

    /**
     * Method that will firstly fill up the HashMap filmsToActors <tconst, ArrayList of actor>
     * Then, we will create a Graph with the size of the ArrayList we got in parameter
     * The method will then iterate on the previously constructed filmsToActors HashMap to add all the edges of the graph
     */
    private void createGraph(){
        // Creation de la HashMap de film, en O(people.size() * M)
        filmsToActors = new HashMap<>();
        int person = 0;
        for(Person i : people){  // O(people.size())
            for (String film : i.getFilms()){  // Taille max = M donc O(M)
                if (filmsToActors.containsKey(film)){   // O(1)
                    filmsToActors.get(film).add(person);
                } else {
                    ArrayList<Integer> tmp = new ArrayList<>();
                    tmp.add(person);
                    filmsToActors.put(film, tmp);
                }
            }
            person++;
        }

        this.graph = new Graph(people.size());   // O(V)

        // ajouts de toutes les arretes en O(NQM^2)
        boolean done;
        for (Map.Entry<String, ArrayList<Integer>> someone :  filmsToActors.entrySet()){   // O(M)
            for (int person1 = 0; person1 < someone.getValue().size(); person1++){        // O(N)
                for (int person2 = person1 + 1; person2 < someone.getValue().size(); person2++){ // O(N)
                    Iterable<Integer> neighbours = graph.adj(someone.getValue().get(person1));  // O(1)
                    done = false;
                    for (int neighbourID : neighbours){ // O(Q)
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

    /**
     * This method will call the findPath {@see findPath} method with the 2 given actors.
     * The method also checks if the 2 actors are the same or not
     * @param actor1 The name of the first actor
     * @param actor2 The name of the second actor
     * @return The return will either return a message if the user has entered the same name, or will call / return
     *         the findPath method {@see findPath}
     */
    public String pathFromActor(String actor1, String actor2){
        return actor1.equalsIgnoreCase(actor2) ? "You entered the same actors." : findPath(actor1, actor2);
    }

    /**
     * This method will call the findPath {@see findPath} method with the given actor. The first actor will be Kevin Bacon
     * @param actor2 The name of the second actor
     * @return The return will will call / return the findPath method {@see findPath}
     */
    public String pathToBacon(String actor2){
        return findPath("Kevin Bacon", actor2);
    }

    /**
     * This method will compute the path between the 2 given actors if both of them exists in the People ArrayList
     * The code will iterate on the return of the {@see BreadthFirstPath#pathTo} method. For every person in the path,
     * we will iterate on all their films and see if the 2 actors played in it or not, if yes, we add it. This will
     * help us to make a beautiful {@code stringBuilder} to show the path with the correct actors and the film they played in.
     * If there is no path, the method return {@code null}.
     * @param actor1 The first actor
     * @param actor2 The second actor
     * @return
     */
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
                for (int person : path) { // O(n)
                    tmpFilm = people.get(person).getFilms();
                    if (person != previous) {
                        for (String film : tmpFilm) { // O(m)
                            if (people.get(previous).getFilms().contains(film)) {
                                films.add(film);
                                previous = person;
                                break;
                            }
                        }
                        name.add(people.get(person).getName());
                    }
                }
            } else {
                return null;
            }
            for (int actor = 0; actor < name.size() - 1; actor++) { // O(n)
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

    /**
     * This method will iterate on the arraylist of people and find the one we are looking for
     * @param name The name of the person we're looking for
     * @return an Integer if the name has been found, or {@code null}
     */
    private Integer findGraphIdFromName(String name){
        for (Person person : people){ // O(n)
            if (person.getName().equals(name)){
                return person.getGraphId();
            }
        }
        return null;
    }


    /**
     * This method will call the diameter function with the default parameters
     * @return the call of the {@see diameter} function
     */
    public int diameter(){
        return diameter(graph.V(), -1, false);
    }

    /**
     * Overloaded method of the diameter method, this method calls the diameter function with the heuristic boolean
     * The default constant used for the heuristic method will be 2000
     * @param heuristic boolean that says we should compute the heuristic diameter
     * @return the result of the heuristic diameter method
     */
    public int diameter(boolean heuristic){
        return heuristic ? diameter(2000, (int) (Math.random() * graph.V()), true) : diameter();
    }

    /**
     * Overloaded method of the diameter method taking a give constant
     * @param heuristic boolean that says we should compute the heuristic diameter
     * @param constant a int that will be used later as a constant for the heuristic method
     * @return the result of the heuristic diameter method
     */
    public int diameter(boolean heuristic, int constant){
        return heuristic ? diameter(constant, (int) (Math.random() * graph.V()), true) : diameter();
    }

    /**
     * Method that will compute the diameter of the graph, it will compute the heuristic one if the parameter {@code heuristic}
     * boolean is set to true or not.
     * @param constant The constant that will be used to compute the heuristic
     * @param random a random number between 0 and {@code graph.V()}
     * @param heuristic boolean that says we should compute the heuristic diameter
     * @return return the diameter of the graph, either for the heuristic or the exact one
     */
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