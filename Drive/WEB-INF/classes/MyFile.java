package com.johnroid;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.text.SimpleDateFormat;
public class MyFile extends File {

	FileSearch fs;
	
	
	public MyFile(String parent, String child) {
		super(parent, child);
		this.HOME_DIR=parent;
		fs=new FileSearch(parent);
		
		if((SHARING_INFO=FileShare.getSharingInfo(this.getPath()))!=null)
			this.SHARED=true;
		
		this.OWNER=FileShare.getOwnerInfo(this.getPath());
		//System.out.println(OWNER);
	}

	public MyFile(String pathname) {
		super(pathname);
		this.HOME_DIR=pathname.substring(0,((pathname.contains("/"))?pathname.indexOf('/'):pathname.length()));
		
		if((SHARING_INFO=FileShare.getSharingInfo(this.getPath()))!=null)
			this.SHARED=true;
		
		this.OWNER=FileShare.getOwnerInfo(this.getPath());
		//fs=new FileSearch(HOME_DIR);
		
	}
	
	public ArrayList<SharingInfo> getSharingInfo(){
		return SHARING_INFO;
	}

	public void setHomeDir(String dir) {
		this.HOME_DIR=dir;
	}
	public String getHomeDir() {
		return this.HOME_DIR;
	}
	
public boolean mkdir(String owner) {
	if((this.mkdir()) && (FileShare.newFile(this.getPath(), owner))){
		this.OWNER=owner;
		return true;
	}else return false;
}
	
public boolean createNewFile(String owner) throws IOException {
	if((this.createNewFile()) && (FileShare.newFile(this.getPath(), owner))) {
		this.OWNER=owner;
		return true;
	}
	else
		return false;
}

public boolean shareFile(String user,int permission) {
	return FileShare.shareTo(this.getPath(), user, permission);
}


	
//	public MyFile(File parent, String child) {
//		super(parent, child);
//		// TODO Auto-generated constructor stub
//	}

//	public MyFile(String pathname) {
//		super(pathname);
//	}
	
	public Boolean isShared() {
		return SHARED;
	}
	
	public Boolean isPublic() {
		return IS_PUBLIC;
	}
	
	public String getSizeText(){
		long length=this.length();
		return (length>1000000)?((length>1000000000)?String.format("%.02f",(length*1e-9))+" GB":String.format("%.02f",(length*1e-6))+" MB"):String.format("%.02f",(length*0.001))+" KB";
	}

	public String getDateText(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm a");
		return df.format(new Date(this.lastModified()));
	}


	public String open() {
		try {
			Scanner file=new Scanner(this);
			String text="";
			for (String line; file.hasNextLine() && (line = file.nextLine()) != null; ) {
				//System.out.println(line);
				text +=line+"\n";
            }
            file.close();
            return text;
		} catch (FileNotFoundException e) {
			//  System.out.println("The file " + this.getName() + " could not be found! " + e.getMessage());
	         return "";
		}
	}
	
// 	public void edit() {
// 		try {
// 			Desktop.getDesktop().open(this);
// //			String path=this.getPath();
// //			fs.update(path.substring(0,path.indexOf('/')));
// 		} catch (IOException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
// 	}
	
	public Boolean moveTo(String dest) {
		boolean r=false;
		
		// if(!(Files.exists(Paths.get(dest+"/"+this.getName()))))
			r= this.rename(dest+"/"+this.getName());
		// if(r)
		// 	r=FileShare.rename(this.getPath(), dest+"/"+this.getName());
		//fs.delTrie();
		//fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));
		return r;
	}

	public boolean moveToTrash(){
		File trash=new File(this.HOME_DIR+"/.trash");
		if(!trash.exists())
			trash.mkdir();
		boolean r=this.moveTo(trash.getPath());
		if(r)
			r=FileShare.addTrash(this.getPath(), trash.getPath()+"/"+this.getName());
		return r;
	}
	
	public String copyTo(String dest) {
		String src=this.getPath();
		String destName=this.getName();
		//dest=dest+"/"+this.getName();
		// boolean success=true;
		int i=1;
		String append="",ext="";
		if(this.isFile()){
			int ind=destName.lastIndexOf('.');
			if((ind!=-1)&&(ind!=0)){
				ext=destName.substring(ind);
				destName=destName.substring(0, ind);
			}
		}
		if(src.equals(dest+"/"+destName+ext))
			while(Files.exists(Paths.get(dest+"/"+destName+append+ext))){
				append =((i==1)?" copy":" copy "+i);
				i++;
			}
		destName=destName+append+ext;
		dest=dest+"/"+destName;
		if(this.isFile()) {
			try {
				InputStream is=new FileInputStream(this);
				OutputStream os=new FileOutputStream(new File(dest));
				byte[] buffer = new byte[1024];
		        int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				is.close();
				os.close();
				FileShare.newFile(dest, this.OWNER);
			} catch (Exception e) {
				destName="";
				// System.out.println("Error copying File "+this.getName());
				// e.printStackTrace();
			}
		} else if(this.isDirectory()) {
			try {
				copyFolder(this,new File(dest));
			} catch (IOException e) {
				destName="";
				// e.printStackTrace();
			}
		}
		// fs.delTrie();
		// fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));

//		String path=this.getPath();
//		fs.update(path.substring(0,path.indexOf('/')));
		return destName;
	}
	

	private void copyFolder(File sourceFolder, File destinationFolder) throws IOException
    {
		FileShare.newFile(destinationFolder.getPath(), this.OWNER);
        if (sourceFolder.isDirectory())
        {
            if (!destinationFolder.exists())
            {
                destinationFolder.mkdir();
                //System.out.println("Directory created :: " + destinationFolder);
            }
             
            String files[] = sourceFolder.list();
             
            for (String file : files)
            {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);
                 
                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //System.out.println("File copied :: " + destinationFolder);
        }
    }
	
	public Boolean rename(String newname) {
		//final long startTime = System.nanoTime();
		//fs.renameFile(this.getPath(),new File(newname).getPath());
		//final long duration = System.nanoTime() - startTime; 
		//System.out.println(duration);
		File f=new File(newname);
		boolean r=false;
		if(!f.exists()){
			r= this.renameTo(f);
			if((r))
				if(this.isFile()){
					r=FileShare.rename(this.getPath(), newname);
				}
				else{
					r=FileShare.renameFolder(this.getPath(), newname);
				}
		}
		// fs.delTrie();
		// fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));
		
		
//		String path=this.getPath();
//		fs.update(path.substring(0,path.indexOf('/')));
		
		return r;
		
	}
	
	public void getInfo() throws IOException {
		System.out.println("Name          : " + this.getName());
		System.out.println("Owner         : " + this.OWNER);
		System.out.println("Path : " + this.getCanonicalPath());
		System.out.println("Size          : " + this.length()+" bytes");
		System.out.println("Last modified : " + new Date(this.lastModified()));
		System.out.println("Can read      : " + (this.canRead()?"Yes":"No"));
		System.out.println("Can write     : " + (this.canWrite()?"Yes":"No"));
		System.out.println("Can execute   : " + (this.canExecute()?"Yes":"No"));
		
		if(SHARED) {
			System.out.println("\n\nShared to:");
			for(int i=0;i<SHARING_INFO.size();i++) {
				SharingInfo info=SHARING_INFO.get(i);
				
				System.out.println(info.user+" | "+((info.permission==READ_ONLY)?"Read only":"Read / write")+"  |  "+info.shared_on);
			}
		}
	}
	
	public boolean forceDel(File file) throws IOException {
		if(file.isDirectory()) {
			if(file.list().length>0)
				for(File f:file.listFiles()) {
					forceDel(f);
				}
//			else
//				return file.delete();
		}
//		else if(!file.isHidden()) {
//			fs.deleteFile(file.getPath());
//			
//		}
		return file.delete();
		//return false;
	}
	
	public boolean del() throws IOException {
		// TODO custom delete operations

		boolean r = false;
		System.out.println("File: "+this.getPath());
		if(this.isDirectory()) {
			if(this.list().length>0) {
				System.out.println("Folder is not empty!\nDo you want to force delete(y/n)?: ");
				switch(System.console().readLine()) {
				case "y":
				case "Y":
					//final long startTime = System.nanoTime();
					r= forceDel(this);
					fs.delTrie();
					fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));
					//final long duration = System.nanoTime() - startTime; 
					//System.out.println(duration);
					
				}
			}
		}
		else {
			//if(fs.deleteFile(this.getPath()))
				r= this.delete();
				fs.delTrie();
				fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));
		}
		if(r) 
			System.out.println("Delted Successfully");
		else
			System.out.println("Failed");
		//String path=this.getPath();
		//fs.update(path.substring(0,path.indexOf('/')));
		
		return r;
	}
	
	 public MyFile[] listFiles() {
		 String files[]=this.list();
		 MyFile f[]=new MyFile[files.length];
		 for(int i=0;i<files.length;i++) {
			 f[i]=new MyFile(this.getPath()+"/"+files[i]);
		 }
		return f;
	 }
	
	public void updateSearch() {
		//final long startTime = System.nanoTime();
		//System.out.println(FileShare.getsharedfileList(this.OWNER));
		fs.update(HOME_DIR,FileShare.getsharedfileList(this.OWNER));
	//	final long duration = System.nanoTime() - startTime; 
		//System.out.println(duration);
	}
	
	public boolean search(String word) {
		//System.out.println(this.getPath());
		//fs.update(this.getPath());
		return fs.search(this.getPath(),word);
	}
	
	public void listDir() {
		try {
		String files[]=this.list();
		Arrays.sort(files);
		for(int i=0;i<files.length;i++)
			System.out.println(files[i]);
		}
		catch(Exception e) {
			//e.printStackTrace();
			System.out.println("Wrong option");
		}
	}
	
	public void listDir(int opt,int order) {
		try {
		File files[]=this.listFiles();
		if(opt==SORT_BY_NAME)
			Arrays.sort(files);
		else if(opt==SORT_BY_SIZE)
			Arrays.sort(files, (f1, f2) -> {
		         return new Long(f1.length()).compareTo(new Long(f2.length()));
		      });
		else if(opt==SORT_BY_TYPE)
			Arrays.sort(files, (f1, f2) -> {
		        if (f1.isDirectory() && !f2.isDirectory()) {
		           return -1;
		        } else if (!f1.isDirectory() && f2.isDirectory()) {
		           return 1;
		        } else {
		           return f1.compareTo(f2);
		        }
		    });
		else if(opt==SORT_BY_DATE)
			Arrays.sort(files, (f1, f2) -> {
		         return new Date(f1.lastModified()).compareTo(new Date(f2.lastModified()));
		      });

	     SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	     if(order==ORDER_ASC)
			for (File file : files) {
		         String size=(file.length()>1000000)?((file.length()>1000000000)?String.format("%.04f",(file.length()*1e-9))+" GB":String.format("%.04f",(file.length()*1e-6))+" MB"):String.format("%.04f",(file.length()*0.001))+" KB";
		         if (!file.isHidden()) {
		            if (file.isDirectory()) {
		               System.out.println("DIR \t" + " " 
		                     + df.format(new Date(file.lastModified())) + "\t" +size+ "\t" + file.getName() );
		            } else {
		               System.out.println("FILE\t" + " " 
		                     + df.format(new Date(file.lastModified()))+ "\t" +size + "\t" + file.getName());
		            }
		         }
		      }
	     if(order==ORDER_DESC)
	    	 for(int i=files.length-1;i>=0;i--) {
		         String size=(files[i].length()>1000000)?((files[i].length()>1000000000)?String.format("%.04f",(files[i].length()*1e-9))+" GB":String.format("%.04f",(files[i].length()*1e-6))+" MB"):String.format("%.04f",(files[i].length()*0.001))+" KB";
		         if (!files[i].isHidden()) {
		            if (files[i].isDirectory()) {
		               System.out.println("DIR \t" + " " 
		                     + df.format(new Date(files[i].lastModified())) + "\t" +size+ "\t" + files[i].getName() );
		            } else {
		               System.out.println("FILE\t" + " " 
		                     + df.format(new Date(files[i].lastModified()))+ "\t" +size + "\t" + files[i].getName());
		            }
		         }
	    	 }
		}
		catch(Exception e) {
			//e.printStackTrace();
			System.out.println("Wrong option");
		}
	}
	
	
	
	
	
	public String getOwnerName() {
		return OWNER;
	}
//	
//	public void setHomeDir(String dir) {
//		HOME_DIR=dir;
//	}
//	
//	public String getHomeDir() {
//		return HOME_DIR;
//	}

	public static int SORT_BY_NAME=0;
	public static int SORT_BY_DATE=1;
	public static int SORT_BY_SIZE=2;
	public static int SORT_BY_TYPE=3;
	

	public static int ORDER_ASC=0;
	public static int ORDER_DESC=1;
	
	public static int READ_ONLY=2;
	public static int READ_WRITE=1;
	public static int DEFAULT_PERMISSION=0;
	
	private String HOME_DIR;
	private Boolean SHARED=false;
	private Boolean IS_PUBLIC=false;
	private String OWNER;
	private ArrayList<SharingInfo> SHARING_INFO;
}