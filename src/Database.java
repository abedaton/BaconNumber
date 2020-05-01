

import java.io.File;
import java.sql.*;

public class Database {
    private String databaseName;
    Connection conn;
    PreparedStatement pstmtBatch;
    PreparedStatement pstmtBatchFilms;
    int countingBatch;
    String batchSQL, batchSQLFilms;
    int batchSize;

    public Database(String filename) {
        this.databaseName = filename;
        try {
            conn = connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        String createPersonTable = "CREATE TABLE IF NOT EXISTS People" +
                "(nconst TEXT NOT NULL, " +
                "primaryName TEXT NOT NULL, " +
                "birthYear INT, " +
                "deathYear INT, " +
                "primaryProfession STRING NOT NULL, " +
                "films STRING NOT NULL);";
        stmt.executeUpdate(createPersonTable);
        String createPersonInFilmTable = "CREATE TABLE IF NOT EXISTS PeopleInFilms" +
                "(tconst TEXT NOT NULL UNIQUE," +
                "People TEXT DEFAULT '');";

        stmt.executeUpdate(createPersonInFilmTable);
    }

    private Connection connect() throws SQLException{
        String url = "jdbc:sqlite:" + databaseName;
        return DriverManager.getConnection(url);
    }

    public void addPersonToFilm(String filmID, String person){
        String sql = "INSERT INTO PeopleInFilms(tconst, People) VALUES(?,?);";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, filmID);
            stmt.setString(2, person);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void beginBatch(int batchSize, boolean films){
        // TODO : Add Batch insert rather than inserting one by one
        batchSQL = "INSERT INTO People(nconst,primaryName,birthYear,deathYear,primaryProfession,films)" + " VALUES(?,?,?,?,?,?);";
        batchSQLFilms = "INSERT INTO PeopleInFilms(tconst, People)" + " VALUES(?,?);";
        batchSQLFilms = "INSERT INTO PeopleInFilms(tconst, People) VALUES (?,?) ON CONFLICT(tconst) DO UPDATE SET People = People || ',' || ?;";
        countingBatch = 0;
        pstmtBatch = null;
        this.batchSize = batchSize;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
                pstmtBatch = conn.prepareStatement(batchSQL);
                pstmtBatchFilms = conn.prepareStatement(batchSQLFilms);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPersonToBatch(String nconst, String primaryName, int birthYear, int deathYear, String professions, String films) throws SQLException {
//        pstmtBatch = prepareUserStatement(nconst, primaryName, birthYear, deathYear, professions, films, batchSQL);
        // TODO remove duplicated code
        pstmtBatch.setString(1, nconst);
        pstmtBatch.setString(2, primaryName);
        pstmtBatch.setInt(3, birthYear);
        pstmtBatch.setInt(4, deathYear);
        pstmtBatch.setString(5, professions);
        pstmtBatch.setString(6, films);
        pstmtBatch.addBatch();
        countingBatch++;
    }

    public void addFilmToBatch(String tconst, String actor) throws SQLException {
//        pstmtBatch = prepareUserStatement(nconst, primaryName, birthYear, deathYear, professions, films, batchSQL);
        // TODO remove duplicated code
        pstmtBatchFilms.setString(1, tconst);
        pstmtBatchFilms.setString(2, actor);
        pstmtBatchFilms.setString(3, actor);
        pstmtBatchFilms.addBatch();
//        countingBatchFilm++;
    }

    public void doBatchPeople(){
        try {
            int[] result = pstmtBatch.executeBatch();
            conn.commit();
            System.out.println("Inserted " + result.length + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doBatchPeopleInFilm(){
        try {
            int[] result = pstmtBatchFilms.executeBatch();
            conn.commit();
            System.out.println("Inserted " + result.length + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean exists(){
        File file = new File(databaseName);
        return file.exists();
    }
}
