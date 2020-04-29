import java.sql.*;

public class Database {
    private String databaseName;
    Connection conn;
    PreparedStatement pstmtBatch;
    int countingBatch;
    String batchSQL;
    int batchSize;

    public Database(String filename){
        this.databaseName = filename;
    }

    public void createDatabase() throws SQLException {
        conn = connect();
        Statement stmt = conn.createStatement();
        String createPersonTable = "CREATE TABLE IF NOT EXISTS People" +
                "(nconst TEXT NOT NULL, " +
                "primaryName TEXT NOT NULL, " +
                "birthYear INT, " +
                "deathYear INT, " +
                "primaryProfession STRING NOT NULL, " +
                "films STRING NOT NULL);";
        stmt.executeUpdate(createPersonTable);
    }

    private Connection connect() throws SQLException{
        String url = "jdbc:sqlite:" + databaseName;
        return DriverManager.getConnection(url);
    }

    public boolean addPerson(String nconst, String primaryName, int birthYear, int deathYear, String professions, String films) {
        String sql = "INSERT INTO People(nconst,primaryName,birthYear,deathYear,primaryProfession,films)" + "VALUES(?,?,?,?,?,?);";
        PreparedStatement pstmt = null;
        try {
            pstmt = prepareUserStatement(nconst, primaryName, birthYear, deathYear, professions, films, sql);
        }catch (SQLException e){
            System.out.println("Failed to add User");
        }
        if (pstmt != null){
            try {
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Failed to add User2");
            }
        }
        return false;
    }

    public void beginBatch(int batchSize){
        // TODO : Add Batch insert rather than inserting one by one
        batchSQL = "INSERT INTO People(nconst,primaryName,birthYear,deathYear,primaryProfession,films)" + " VALUES(?,?,?,?,?,?);";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToBatch(String nconst, String primaryName, int birthYear, int deathYear, String professions, String films) throws SQLException {
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

    public void doBatch(){
        try {
            int[] result = pstmtBatch.executeBatch();
            conn.commit();
            System.out.println("Inserted " + result.length + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareUserStatement(String nconst, String primaryName, int birthYear, int deathYear, String professions, String films, String sql) throws SQLException{
        Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nconst);
        pstmt.setString(2, primaryName);
        pstmt.setInt(3, birthYear);
        pstmt.setInt(4, deathYear);
        pstmt.setString(5, professions);
        pstmt.setString(6, films);
        return pstmt;
    }
}
