import java.util.List;

/**
 * Class that represent a person, having name, a position in the graph, and a list of film they played in
 * This class only has a constructor, getters and a overridden {@code toString} method that override the default
 * toString method from the Object class
 */
public class Person  {
    private String name;
    private int graphId;
    private List<String> films;

    public Person(String name, int graphId, List<String> films){
        this.name = name;
        this.graphId = graphId;
        this.films = films;
    }

    @Override
    public String toString(){
        return "Name: " + name  + "\nGraph id: " + this.graphId + "\nfilms: " + films.toString();
    }

    public String getName() {
        return name;
    }

    public int getGraphId() {
        return graphId;
    }

    public List<String> getFilms() {
        return films;
    }
}
