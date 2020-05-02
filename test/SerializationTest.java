import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SerializationTest {
    BeaconGraph actualGraph;
    BeaconGraph loadedGraph;

    private void generateGraphs() throws SQLException, IOException, ClassNotFoundException {
        Database dbTest = new Database("databaseTest");
        dbTest.createDatabase();
        NewParser newParser = new NewParser(dbTest);
        newParser.parseName_basic("datasets/name.basics.tsv", 1000);
        actualGraph = new BeaconGraph(dbTest);

        File graphFile = new File("graphTest.obj");
        graphFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(graphFile);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(actualGraph);
        oos.close();
        fout.close();

        FileInputStream fi = new FileInputStream(new File("graphTest.obj"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        loadedGraph = (BeaconGraph)oi.readObject();
        oi.close();
        fi.close();
    }

    @AfterEach
    public void clean(){
        File dbTestFile = new File("databaseTest");
        System.out.println(dbTestFile.delete());
        File graphFile = new File("graphTest.obj");
        System.out.println(graphFile.delete());
    }

    @Test
    public void testReadGraphDegree() throws SQLException, IOException, ClassNotFoundException {
        generateGraphs();
        assertEquals(actualGraph.getGraph().degree(1), loadedGraph.getGraph().degree(1));
    }

    @Test
    public void testCompareAdj() throws SQLException, IOException, ClassNotFoundException {
        generateGraphs();
        List<Integer> actualList = new ArrayList<>();
        List<Integer> loadedList = new ArrayList<>();
        actualGraph.getGraph().adj(1).iterator().forEachRemaining(actualList::add);
        loadedGraph.getGraph().adj(1).iterator().forEachRemaining(loadedList::add);
        System.out.println(actualList.toString());
        System.out.println(loadedList.toString());
        assertEquals(actualList.toString(), loadedList.toString());
    }
}
