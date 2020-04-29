import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String DBNAME = "database.db";

        Database db = new Database(DBNAME);
        File dbFile = new File(DBNAME);

        if (!dbFile.exists()){
            try {
                db.createDatabase();
                NewParser newParser = new NewParser(db);
                TestParser testParser = new TestParser(db);
                System.out.println("Starting parsing");
                List<Person> people = newParser.parseName_basic(args[0]);
            } catch (SQLException e) {
                System.out.println("The database could not be created. The program will now exit.");
                e.printStackTrace();
                System.exit(1);
            }
        }


//        System.out.println(people.size());
//        BeaconGraph beaconGraph = new BeaconGraph(people, db);
    }
}
