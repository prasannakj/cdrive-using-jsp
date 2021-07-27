package com.johnroid;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
class SharedFile{
	int fid;
	String path;
	int permission;
	String owner;
	Date sharedOn;
}
public class FileShare {
	
	public static ArrayList<SharingInfo> getSharingInfo(String file) {
		try {
			ArrayList<SharingInfo> sharing_info=new ArrayList<SharingInfo>();
			
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			// SELECT shared_files.*,drive_users.uname FROM shared_files,drive_users where fid= (select fid from files where location='USER_DIR_johnroid/file3.txt') and shared_files.shared_to=drive_users.uid ;
			// ResultSet rs=stmt.executeQuery("SELECT * FROM shared_files where fid= (select fid from files where location='"+file+"')");  
			ResultSet rs=stmt.executeQuery("SELECT shared_files.*,drive_users.uname FROM shared_files,drive_users where fid= (select fid from files where location='"+file+"') and shared_files.shared_to=drive_users.uid");  
			if(!rs.next()) {
				return null;
			}else
				do { 
					//this.SHARED=true;
					SharingInfo s=new SharingInfo();
					s.sid=rs.getInt(1);
					//this.OWNER=rs.getString(3);
					s.user=rs.getString(6);
					s.permission=rs.getInt(4);
					s.shared_on=rs.getDate(5);
					sharing_info.add(s);
				}while(rs.next());
				con.close();

				return sharing_info;
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	
				return null;
		}
	}
	
	public static boolean rename(String oldfile,String newfile) {
		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			int result=stmt.executeUpdate("UPDATE `files` SET `location` = '"+newfile+"' WHERE (`location` = '"+oldfile+"');");  
			//System.out.println(result+" records affected");  
			con.close();
			if(result>0) {
				return true;
			}else {
				return false;
			}
			  
		 }
		catch(SQLException e) {
			return false;
		}
	}

	public static boolean renameFolder(String oldfile,String newfile){
		boolean r=true;
		if(rename(oldfile,newfile)){
			try {
				Connection con=DbConnect.Connect();
				Statement stmt=con.createStatement();  
				ResultSet rs=stmt.executeQuery("SELECT * FROM sqldb.files WHERE location LIKE '"+ oldfile +"/%'");  
				String oldf,newf;
				while(rs.next()) { 
					oldf=rs.getString(2);
					newf=oldf.replace(oldfile, newfile);
					r=rename(oldf,newf);
				}
				con.close();

			}
			catch (SQLException e) {
				// e.printStackTrace();
				r=false;
			}
		}

		return r;
	}

	public static boolean addToStared(String file){
		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			int result=stmt.executeUpdate("insert into stared(fid) values((select fid from files where location='"+file+"'))");  
			//System.out.println(result+" records affected");  
			con.close();
			if(result>0) {
				return true;
			}else {
				return false;
			}
			  
		 }
		catch(SQLException e) {
			return false;
		}
	}

	public static boolean addTrash(String preLoc,String newLoc){
		// INSERT INTO `sqldb`.`trash` (`fid`, `pre_loc`) VALUES ((select fid from files where location='USER_DIR_johnroid/new folder123/sample.txt'), 'USER_DIR_johnroid/new folder123/sample.txt');

		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			int result=stmt.executeUpdate("INSERT INTO `sqldb`.`trash` (`fid`, `pre_loc`) VALUES ((select fid from files where location='"+newLoc+"'), '"+preLoc+"')");  
			//System.out.println(result+" records affected");  
			con.close();
			if(result>0) {
				return true;
			}else {
				return false;
			}
			  
		 }
		catch(SQLException e) {
			return false;
		}
	}

	public static String getOwnerInfo(String file) {
		String owner = null;
		
		try {
		Connection con=DbConnect.Connect();
		Statement stmt=con.createStatement();  
		// ResultSet rs=stmt.executeQuery("SELECT owner from files where location='"+file+"'");  
		// SELECT uname FROM sqldb.drive_users where uid=( SELECT owner from files where location='USER_DIR_johnroid/file2.txt')
		ResultSet rs=stmt.executeQuery("SELECT uname FROM sqldb.drive_users where uid=( SELECT owner from files where location='"+file+"')");  
		
			if(rs.next()) 
				owner=rs.getString(1);

			con.close();
			
			
	}catch (SQLException e) {
			return null;
		}
		return owner;
	}
	
	
	public static boolean newFile(String path,String owner) {
		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			int result=stmt.executeUpdate("INSERT INTO `files` (`location`,`owner`) VALUES ('"+path+"',(select uid from drive_users where uname='"+owner+"'))");  
			//System.out.println(result+" records affected");  
			con.close();
			if(result>0) {
				return true;
			}else {
				return false;
			}
			  
		 }
		catch(SQLException e) {
			return false;
		}
	}
	
	
	public static boolean shareTo(String file,String user,int permission) {
		try {
			// insert into shared_files(fid,shared_to,permission,shared_on) values((select fid from files where location='USER_DIR_johnroid/nfolder'),(select uid from drive_users where uname='johnroid'),1,'2019-02-04');
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			// int result=stmt.executeUpdate("insert into shared_files(fid,shared_to,permission,shared_on) values((select fid from files where location='"+file+"'),'"+user+"',"+permission+",'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+"')");  
			int result=stmt.executeUpdate("insert into shared_files(fid,shared_to,permission,shared_on) values((select fid from files where location='"+file+"'),(select uid from drive_users where uname='"+user+"'),"+permission+",'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+"')");  
			//System.out.println(result+" records affected");  
			con.close();
			if(result>0) {
				return true;
			}else {
				return false;
			}
		 }
		catch(SQLException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	
	
	public static ArrayList<SharedFile> getsharedfiles(String user) {
		
		try {
		Connection con=DbConnect.Connect();
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("SELECT sf.fid,f.location,sf.permission,sf.shared_on,f.owner FROM shared_files sf, files f where (sf.fid=f.fid) & (sf.shared_to='"+user+"')"); 
		ArrayList<SharedFile> sharedFiles=new ArrayList<SharedFile>();
		if(!rs.next()) {
			return null;
		}else
			do { 
				SharedFile sf=new SharedFile();
				sf.fid=rs.getInt(1);
				sf.path=rs.getString(2);
				sf.permission=rs.getInt(3);
				sf.sharedOn=rs.getDate(4);
				sf.owner=rs.getString(5);
				sharedFiles.add(sf);
			}while(rs.next());
			con.close();

			return sharedFiles;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}
	
public static ArrayList<MyFile> getsharedfileList(String user) {
		try {
			Connection con=DbConnect.Connect();
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("SELECT files.location FROM files,shared_files where files.fid=shared_files.fid && shared_files.shared_to='"+user+"'"); 
			ArrayList<MyFile> files=new ArrayList<MyFile>();
			if(!rs.next()) {
				return null;
			}else
				do { 
					MyFile f=new MyFile(rs.getString(1));
					files.add(f);
				}while(rs.next());
			con.close();

			return files;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}

}
