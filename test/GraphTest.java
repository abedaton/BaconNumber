import org.junit.Test;
import utils.Graph;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void addDifferentTest(){
        int v = 3;
        int w = 1;
        Graph graph = new Graph(4);
        graph.addEdge(v, w);
        assertEquals(graph.degree(v), 1);
    }

    @Test
    public void addSameTest(){
        int v = 3;
        int w = 1;
        Graph graph = new Graph(4);
        graph.addEdge(v, w);
        graph.addEdge(v, w);
        graph.addEdge(w, v);
        assertEquals(graph.degree(v), 1);
    }
}
