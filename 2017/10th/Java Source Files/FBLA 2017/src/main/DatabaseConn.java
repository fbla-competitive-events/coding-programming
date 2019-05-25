package main;

public class DatabaseConn {
	
	private final static String CONNECTION = "jdbc:mysql://limitunknown.com:3306/admin_colin_db"; 
	private final static String USERNAME = "admin_colin_usr";
	private final static String PASSWORD = "tomnookgettin'nookie4lyf";
	
	public static String getConnection(){
		return CONNECTION;
	}
	
	public static String getUsername(){
		return USERNAME;
	}
	
	public static String getPassword(){
		return PASSWORD;
	}
}
