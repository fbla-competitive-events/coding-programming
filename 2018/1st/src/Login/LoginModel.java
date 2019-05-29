package Login;

import Database.dbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    Connection connection;

    public LoginModel(){
        try{
            this.connection = dbConnect.getConnection();
        }
        catch(SQLException e){
            System.err.println("Error" + e);
        }
        if(this.connection == null){
            System.out.print("No connection found");
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected() {
        return this.connection != null;
    }

    public boolean isLogin(String user, String pass, String opt)throws Exception{
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM login WHERE username = ? and password = ? and usertype = ?";

        try{
            pr = connection.prepareStatement(sql);
            pr.setString(1,user);
            pr.setString(2,pass);
            pr.setString(3,opt);

            rs = pr.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }
        catch(SQLException e){
            System.err.print("Error" + e);
            return false;
        }
        finally{
            pr.close();
            rs.close();
        }
    }
}
