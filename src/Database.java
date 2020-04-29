import java.sql.*;

public class Database {
    private String databaseName;

    public Database(String filename){
        this.databaseName = filename;
    }

    public void createDatabase() throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        String createPersonTable = "CREATE TABLE IF NOT EXISTS People" +
                "(nconst TEXT NOT NULL, " +
                "primaryName TEXT NOT NULL, " +
                "birthYear INT, " +
                "deathYear INT, " +
                "primaryProfession STRING NOT NULL, " +
                "films STRING NOT NULL);";
        stmt.execute(createPersonTable);
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

    public void batchInsert(String nconst, String primaryName, int birthYear, int deathYear, String professions, String films){
        // TODO : Add Batch insert rather than inserting one by one
//        String sql = "INSERT INTO People(nconst,primaryName,birthYear,deathYear,primaryProfession,films)" + "VALUES(?,?,?,?,?,?);";
//        int count = 0;
//        int batchSize = 50;
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
