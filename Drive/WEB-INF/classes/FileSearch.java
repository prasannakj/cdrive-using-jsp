package com.johnroid;
import java.io.*;
import java.util.ArrayList;

public class FileSearch {
	

	FileSearch(String home){
		HOME_DIR=home;
		
		//System.out.println(HOME_DIR);
	}
	
	public void updateShared(ArrayList<MyFile> files,TrieFile t) {
		for(int i=0;i<files.size();i++) {
			updateAll(files.get(i),t);
			//System.out.println("Shared file: "+files.get(i));
		}
	}
	
	private void updateAll(MyFile file,TrieFile t) {
		try {
			//System.out.println("All File: "+file.getPath()+"is exist: "+file.exists());
			String fileName=file.getName();
			if(file.isFile()&&(!file.isHidden())) {//&&(fileName.lastIndexOf(".") != -1 )&& (fileName.lastIndexOf(".") != 0)&&(fileName.substring(fileName.lastIndexOf(".")+1).equals("txt"))) {
				String line;

				//System.out.println("All text File: "+file.getPath());
				BufferedReader br=new BufferedReader(new FileReader(file));
				long lineno = 0;
				//System.out.println(file.getHomeDir()+"\t\t"+this.HOME_DIR+"\t\t"+file.getHomeDir().equals(this.HOME_DIR));
				while((line = br.readLine()) != null) {
					String[] words=line.split("\\s");
					lineno += 1;
					for(String w:words){  
						t.insert(w.trim().toLowerCase(),file.getPath(),String.valueOf(lineno));
					}  
				}
			}
			else if(file.isDirectory()){
				for(MyFile f:file.listFiles()) {
					 updateAll(f,t);
				}
			}
		}
		catch(IOException e) {
			System.out.println("Exception:"+e.getLocalizedMessage());
		}
	}
	
	
	
	public void renameFile(String oldname,String newname) {
		TrieFile t=getTrieFile();
		t.renameFile(oldname, newname);
		saveTrieFile(t);
	}
	public boolean deleteFile(String f) throws IOException {

		//System.out.println(f.substring(0,f.indexOf('/')));
		TrieFile t=getTrieFile();
		boolean b=false;
		if(t!=null) {
			b=t.deleteFile(f);
			saveTrieFile(t);
		}
		else {
			try {
				//System.out.println(f.substring(0,f.indexOf('/')));
				updateAll(new MyFile(f.substring(0,f.indexOf('/'))),t);
				b=t.deleteFile(f);
				saveTrieFile(t);
			}catch(Exception e) {System.out.println("Can't update"+e.getMessage());}
		}
		
		return b;
	}
	
	private TrieFile getTrieFile() {

		TrieFile t = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(HOME_DIR+"/.trie.obj"));

			t=(TrieFile)in.readObject();  
			in.close();
		} catch (Exception e) {
			t=new TrieFile();
			//System.out.println("triefile not found");
			//return null;
			//e.printStackTrace();
		}
		return t;
	}
	
//	public void update(String dir) {
//		TrieFile t=getTrieFile();
//		updateAll(new File(dir),t);
//		saveTrieFile(t);
//	}
	
	public void update(String dir,ArrayList<MyFile> s) {
		TrieFile t=getTrieFile();
		updateAll(new MyFile(dir),t);
		updateShared(s, t);
		saveTrieFile(t);
	}
	
	public void delTrie() {
		File f=new File(HOME_DIR+"/.trie.obj");
		f.delete();
	}

	private void saveTrieFile(TrieFile t) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(HOME_DIR+"/.trie.obj"));
			out.writeObject(t);  
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean search(String dir,String word) {
		TrieFile t=getTrieFile();
		if(t==null)
			updateAll(new MyFile(dir),t);
		if(t.search(word,this.HOME_DIR)||t.startsWith(word,this.HOME_DIR)||t.similar(word,this.HOME_DIR))
			return true;
		else 
			return false;
	}
	
//	public static void main(String args[]) {
//		FileSearch fs=new FileSearch();
//		System.out.println(fs.search("tamil"));
//	}
	
	
	
	private String HOME_DIR="";
}
