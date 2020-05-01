import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String DBNAME = "databaseTest.db";

        Database db = new Database(DBNAME);
        try {
            db.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

            System.out.println("pouette");

            NewParser newParser = new NewParser(db);
            System.out.println("Starting parsing");
            newParser.parseName_basic("datasets/name.basics.tsv");


        System.out.println("Done");
        try {
            BeaconGraph beaconGraph = new BeaconGraph(db);
            File graphFile = new File("graph.obj");
            if (!graphFile.exists()){
                graphFile.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(graphFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
//            oos.writeObject(beaconGraph);

        } catch (SQLException  e){
            System.out.println("Impossible de creer le graph");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Impossible ");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
