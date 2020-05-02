import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        final String DBNAME = "databaseTiny.db";
//
//        Database db = new Database(DBNAME);
//        try {
//            db.createDatabase();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//            System.out.println("pouette");
//
//            NewParser newParser = new NewParser(db);
//            System.out.println("Starting parsing");
//            newParser.parseName_basic("datasets/name.basics.tsv");
//
//
//        System.out.println("Done");
//        try {
//            BeaconGraph beaconGraph = new BeaconGraph(db);
//            File graphFile = new File("graph.obj");
//            if (!graphFile.exists()){
//                graphFile.createNewFile();
//            }
//
//            FileOutputStream fout = new FileOutputStream(graphFile);
//            ObjectOutputStream oos = new ObjectOutputStream(fout);
//            oos.writeObject(beaconGraph);
//            oos.close();
//            fout.close();
//
//        } catch (SQLException  e){
//            System.out.println("Impossible de creer le graph");
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            System.out.println("Impossible ");
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        FileInputStream fi = null;
        try {
            fi = new FileInputStream(new File("graph.obj"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            BeaconGraph graph = (BeaconGraph)oi.readObject();
            System.out.println(graph.getGraph().toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
