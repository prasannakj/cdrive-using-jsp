package com.johnroid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {
	public static Connection Connect() {
		Connection con=null;
	try{  
		Class.forName("com.mysql.cj.jdbc.Driver");  
		//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sqldb?autoReconnect=true&useSSL=false","root","johnroid");  
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sqldb?useSSL=false","root","johnroid");  
	}catch(Exception e) {
		//return null;
		e.getStackTrace();
	}
	return con;
	}
}
