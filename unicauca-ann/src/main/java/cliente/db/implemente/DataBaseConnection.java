package cliente.db.implemente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel pusil<danielpusil@unicauca.edu.co>
 */
public class DataBaseConnection {//singleton connection

    private static DataBaseConnection INSTANCE = null;
    public static Statement stmtObj;
    public static Connection connObj;
    public static ResultSet resultSetObj;
    public static PreparedStatement pstmt;
    public static final int DEFAULT_PORT = 3306;
    public static String DEFAULT_DB_NAME = "ipsdb";

    // Private constructor suppresses 
    public DataBaseConnection() {
        getConnection();
    }

    /* Method To Establish Database Connection */
    public static Connection getConnection() {
        try {
            String db_url = "jdbc:mysql://localhost:3306/experimentELM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    db_userName = "ipsuser",
                    db_password = "ipsuser";
            connObj = DriverManager.getConnection(db_url, db_userName, db_password);
            connObj.setAutoCommit(false);
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        try {
            connObj.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connObj;
    }

    private synchronized static void createInstance() {
        try {
            if (INSTANCE == null || connObj.isClosed()) {
                INSTANCE = new DataBaseConnection();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connObj.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static DataBaseConnection getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public ResultSet executeQuery(String query) {
        try {
            if (createStatement() != 0) {
                resultSetObj = stmtObj.executeQuery(query);
            }
        } catch (SQLException sqlException) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, sqlException);
        }
        return resultSetObj;
    }

    public int prepareStatement(String query) {
        int result = 0;
        try {
            createInstance();
            pstmt = connObj.prepareStatement(query);
            result = 1;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int createStatement() {
        int result = 0;
        try {
            createInstance();
            stmtObj = connObj.createStatement();
            // connObj.setAutoCommit(false);
            result = 1;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static void closeConnection() {
        try {
            connObj.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PreparedStatement getPstmt() {
        return pstmt;
    }
}
