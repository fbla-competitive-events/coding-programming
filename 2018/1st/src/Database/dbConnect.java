package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnect {

    private static final String SQCONN = "jdbc:sqlite:Library.db";

    public static Connection getConnection()throws SQLException{

        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SQCONN);
        }
        catch(ClassNotFoundException e){
            System.err.println("Error" + e);
        }
        return null;
    }
}
