package com.johnroid;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User implements Serializable {
	
//	public User(String uname,String pass) throws SQLException{
//		Connection con=DbConnect.Connect();
//		
//		Statement stmt=con.createStatement();  
//		ResultSet rs=stmt.executeQuery("select * from drive_users where uname=\'"+uname+"\' and pass=\'"+pass+"\'");  
//		if(rs.next()) { 
//			IS_SIGNED_IN=true;
//			USER_NAME=rs.getString(1);
//			MAIL_ID=rs.getString(3);
//			FULL_NAME=rs.getString(5);
//			System.out.println("Welcome "+FULL_NAME);
//		}
//		else
//		{
//			System.out.println("User name or Password are incorrect");
//		}
//		con.close();  
//	}
	
	
	
	public boolean signIn(String uname,String pass) throws SQLException{
		Connection con=DbConnect.Connect();
		
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from drive_users where uname=\'"+uname+"\' and pass=\'"+pass+"\'");  
		if(rs.next()) {
			IS_SIGNED_IN=true;
			USER_NAME=rs.getString(1);
			MAIL_ID=rs.getString(3);
			FULL_NAME=rs.getString(5);
			WORK_DIR="USER_DIR_"+USER_NAME;
			System.out.println("Welcome "+FULL_NAME);
		}
		else
		{
			return false;
		}
		con.close();  
		sharedFiles=FileShare.getsharedfiles(USER_NAME);
		return true;
	}

	public void setSharedFiles(ArrayList<SharedFile> sharedFiles) {
		this.sharedFiles = sharedFiles;
	}

	public static boolean isUser(String username) throws SQLException {
		Connection con=DbConnect.Connect();
		
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select uname from drive_users where uname=\'"+username+"\'");  
		if(rs.next()) { 
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean newUser(String uname,String pass,String mail,String fullname,String usr_type) {
		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			int result=stmt.executeUpdate("insert into drive_users(uname,pass,mail,utype,fullname) values('"+uname+"','"+pass+"','"+mail+"','"+usr_type+"','"+fullname+"')");  
			//System.out.println(result+" records affected");  
			
			if(result>0) {
				IS_SIGNED_IN=true;
				USER_NAME=uname;
				FULL_NAME=fullname;
				MAIL_ID=mail;
				WORK_DIR="USER_DIR_"+USER_NAME;
				new File(WORK_DIR).mkdir();
				// else
				// 	System.out.println("Error creating Workspace");
					
			}else {
				return false;
			}
			con.close();  
		 }
		catch(SQLException e) {
			return false;
		}
		sharedFiles=FileShare.getsharedfiles(USER_NAME);
		return true;
	}
	
	public ArrayList<SharedFile> getSharedFiles(){
		return sharedFiles=FileShare.getsharedfiles(USER_NAME);
	}
	
	
	
	public void signOut() {
		IS_SIGNED_IN=false;
	}
	
	public void setWorkSpace(String dir) {
		WORK_DIR=dir;
	}
	
	public String getMailid() {
		return MAIL_ID;
	}
	
	public String getUserName() {
		return USER_NAME;
	}
	
	public String getFullName() {
		return FULL_NAME;
	}
	
	public void setCurDir(String dir) {
		CURRENT_DIR=dir;
	}
	
	public String getWorkSpace(){
		return WORK_DIR;
	}

	public String getCurDir(){
		return CURRENT_DIR;
	}
	
	public boolean isSignedIn() {
		return IS_SIGNED_IN;
	}

	

	private String WORK_DIR="";
	private String CURRENT_DIR="";
	private Boolean IS_SIGNED_IN=false;
	private String USER_NAME="";
	private String MAIL_ID="";
	private String FULL_NAME="";
	
	public String FULL_DIR="";

	public static int HOME_MODE=0;
	public static int SHARED_MODE=1;
	public int mode=HOME_MODE;

	ArrayList<SharedFile> sharedFiles=null;
}
