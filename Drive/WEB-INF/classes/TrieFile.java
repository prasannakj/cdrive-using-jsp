package com.johnroid;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class TrieFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private TrieNode root;
	public TrieFile() {
		root = new TrieNode();
	}

	// To insert a word to the Trie Tree...
	
	public void insert(String word,String fname,String lno) {
		word=word.toLowerCase();
		
		HashMap<Character, TrieNode> children = root.children;
		

		ArrayList<String> loc = null;
		
		
		for(int i=0; i<word.length(); i++){
			char c = word.charAt(i);
			TrieNode t;
			if(children.containsKey(c)){
				t = children.get(c);
			}else{
				t = new TrieNode(c);
				children.put(c, t);
			}
			children = t.children;

			if(i==word.length()-1) {
				t.isLeaf = true;
				//t.isSharedFile=isShared;
				if(t.location==null)
				{
					t.location=new HashMap<String,ArrayList<String>>();
					loc=new ArrayList<>();
					loc.add(lno);
					t.location.put(fname, loc);
				}
				else {
					if(t.location.containsKey(fname)) {
						if(!t.location.get(fname).contains(lno))
							t.location.get(fname).add(lno);
					}
					else {
						loc=new ArrayList<>();
						loc.add(lno);
						t.location.put(fname, loc);
					}
				}
			}
		}
	}
	

	//  To Search a word
	public boolean search(String word,String homedir) {
		word=word.toLowerCase();
		TrieNode t = searchNode(word);
		if(t != null && t.isLeaf) {
			System.out.println("Word "+word +" exacrly found in "+t.location.size()+" files ");
			String path,homeDir;

			for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
			{
				path=(String)loc.getKey();
				homeDir=path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
				//System.out.println(path+"\n"+homeDir);
				MyFile myfile=new MyFile(path);
				if(myfile.getHomeDir().equals(homedir))
					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
				else
					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Shared")):("Shared/"+path))+"\nLine numbers:\t"+loc.getValue().toString()+"\t\tOwner\t\t"+myfile.getOwnerName());
			}
			return true;
		}
		else
			return false;
	}
	

	
	//  StartsWith search
	public boolean startsWith(String prefix,String h) {
		prefix=prefix.toLowerCase();
		TrieNode t=searchNode(prefix);
		if(t == null)
			return false;
		else {
			if(!t.children.isEmpty())
				findLeaf(t,prefix,prefix);
			else if(t.isLeaf)
			{
				System.out.println("Word "+prefix +" exacrly found in "+t.location.size()+" files ");
				String path,homeDir;

				for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
				{
					path=(String)loc.getKey();
					homeDir=path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
					//System.out.println(path+"\n"+homeDir);
//					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
					MyFile myfile=new MyFile(path);
					if(myfile.getHomeDir().equals(h))
						System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
					else
						System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Shared")):("Shared/"+path))+"\nLine numbers:\t"+loc.getValue().toString()+"\t\tOwner\t\t"+myfile.getOwnerName());
				}
			}
			return true;
		}
	}
	
	

	//  Similarity search
	public boolean similar(String k,String h) {
		k=k.toLowerCase();
		
		Map.Entry<String, TrieNode> m=startNode(k).firstEntry();
		TrieNode t = m.getValue();
		String s = m.getKey();
		
		
//		System.out.println(s);
		if(t == null)
			return false;
		else {
			if(!t.children.isEmpty())
				findSimilar(s,t,k);
			else if(t.isLeaf)
			{
				for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
				{
					String path = (String)loc.getKey();
					String homeDir = path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
					//System.out.println(path+"\n"+homeDir);
					MyFile myfile=new MyFile(path);
					if(myfile.getHomeDir().equals(h))
						System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
					else
						System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Shared")):("Shared/"+path))+"\nLine numbers:\t"+loc.getValue().toString()+"\t\tOwner\t\t"+myfile.getOwnerName());
				}
				//MyFile myfile=new MyFile(t.location.ge\\\\);
//				System.out.println(k+" : "+t.location.toString());
//				MyFile myfile=new MyFile(path);
//				if(myfile.getHomeDir().equals(homeDir))
//					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
//				else
//					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Shared")):("Shared/"+path))+"\nLine numbers:\t"+loc.getValue().toString()+"\t\tOwner\t\t"+myfile.getOwnerName());
			}
			return true;
		}
	}
	
	
	
	// To find similarity between two string
	private double Similarity(String a,String b) {
	    Set<Character> s1 = toSet(a.toLowerCase());
	    Set<Character> s2 = toSet(b.toLowerCase());
	    
	    final int sa = s1.size();
	    final int sb = s2.size();
	    s1.retainAll(s2);
	    final int intersection = s1.size();
	    
	    return 1d / (sa + sb - intersection) * intersection;
	}
	// To traverse all leaf node from given node ('prefix' is the input given by user and 'word' is the word that starts with 'prefix')
	private void findLeaf(TrieNode t,String prefix,String word) {
		if(t.children.isEmpty()) {
			if(t.isLeaf) {
				System.out.println("The Word '"+word +"' starts with '"+prefix+"' in "+t.location.size()+" files ");
				String path,homeDir;
				
				//System.out.println("word:"+word);
				for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
				{
					path=(String)loc.getKey();
					homeDir=path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
					//System.out.println(path+"\n"+homeDir);
					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
				}
				//System.out.println(t.location.toString());
			}
		}
		else {
			if(t.isLeaf) {
				//System.out.println(t.location.toString());

				System.out.println("The Word '"+word +"' starts with '"+prefix+"' in "+t.location.size()+" files ");
				String path,homeDir;

				//System.out.println("word:"+word);
				for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
				{
					path=(String)loc.getKey();
					homeDir=path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
					//System.out.println(path+"\n"+homeDir);
					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
				}
			}
			for(Map.Entry<Character,TrieNode> m:t.children.entrySet()) {
				findLeaf(m.getValue(),prefix,word+m.getKey());
			}
		}
	}
	
	public boolean deleteFile(String file) throws IOException {
//		for(Map.Entry<Character, TrieNode> m:root.children.entrySet()) {
//			deleteNodes(file,m.getValue(),root,m.getKey());
//			//curNode=m.getValue();
//		}
		
		
		
//		deleteNodes(file,root,root,' ');
//		for(Map.Entry<TrieNode,String> m:cr.entrySet()) {
//			m.getKey().location.remove(m.getValue());
//		}
//		for(Map.Entry<TrieNode,Character> m:lb.entrySet()) {
//			m.getKey().children.remove(m.getValue());
//		}
		
		
		
		//System.out.println("Entered into delete file");
		
		
		deleteNodes(root,file);
		return true;
	}
		
		
//		try {
//			Scanner f=new Scanner(new File(file));
//			for (String line; f.hasNextLine() && (line = f.nextLine()) != null; ) {
//                // System.out.println(line);
//				String[] words=line.split("\\s");
//				//lineno += 1;
//				for(String w:words){  
//					//t.insert(w.trim(),listOfFiles[i].getPath(),String.valueOf(lineno));
//					//System.out.println(w);
//					deleteNodes(w.trim().toLowerCase(),file);
//				}
//            }
//            f.close();
//           // return true;
//		} catch (FileNotFoundException e) {
//			// System.out.println("The file " + file + " could not be found! " + e.getMessage());
//	         return false;
//		}
//		return true;
//	}
	
//	
//	Map<TrieNode,String> cr=new HashMap<TrieNode, String>();
//	Map<TrieNode,Character> lb=new HashMap<TrieNode, Character>();
	
	
	public boolean deleteNodes(TrieNode curr,String file) {
		if(curr.isLeaf&&curr.location.containsKey(file))
		{
			return true;
		}
		else
		for(Map.Entry<Character, TrieNode> m:curr.children.entrySet()) {
			if(deleteNodes(m.getValue(),file)) {
				curr.children.remove(m.getKey());
				if(curr.children.size()>1)
					return false;
				else
					return true;
			}
		}
		return false;
	}
	
	
	public void renameFile(String oldname,String newname) {
		rename(root,oldname,newname);
	}
	
	public void rename(TrieNode curr,String oldname,String newname) {
		if(curr.isLeaf)
		{
			if(new File(oldname).isDirectory())
				for(int i=0;i<curr.location.size();i++) {
					String key=(String) curr.location.keySet().toArray()[i];

					if(key.startsWith(oldname)) {
						curr.location.put(key.replace(oldname, newname), curr.location.get(key));
						curr.location.remove(key);
						//System.out.println("\n\n"+key);
						//System.out.println(key.replace(oldname, newname)+"\n\n");
					}
				}
				
				
			
//			for(Map.Entry<String, ArrayList<String>> m:curr.location.entrySet()) {
//				String key=m.getKey();
//				if(key.startsWith(oldname))
//					curr.location.put(key.replace(oldname, newname), curr.location.remove(key));
//			}
			
			
			
			else
				curr.location.put(newname,curr.location.remove(oldname));
		}
		else
			
		for(Map.Entry<Character, TrieNode> m:curr.children.entrySet()) {
			rename(m.getValue(),oldname,newname);
			//m.wait();
		}
	}
	
	public void deleteNodes(String file,TrieNode curNode,TrieNode lastBranch,char childKey) {
		
		//System.out.println("function Called");
		//if(curNode.children==null) {
			if((curNode.isLeaf)&&(curNode.location.containsKey(file)))
			{
				try {
				if(curNode.location.size()>1) {
					curNode.location.remove(file);
					//cr.put(curNode, file);
					//System.out.println("File removed from :"+curNode.location.toString()+"\nFile: "+file);
				}
				else {
					lastBranch.children.remove(childKey);
					//lb.put(lastBranch, childKey);
					//System.out.println("File removed from :"+lastBranch.children.keySet().toString()+"\nKey: "+childKey);
				}
				}catch(Exception e) {System.out.println("Exception: "+e.getMessage());}
				
			}
		//}
		else {
			//TrieNode curr = curNode;
			for(Map.Entry<Character, TrieNode> m:curNode.children.entrySet()) {
				if(curNode.children.size()>1) {
					lastBranch=curNode;
					childKey=m.getKey();
				}
				deleteNodes(file,m.getValue(),lastBranch,childKey);
				//curNode=m.getValue();
			}
		}
	}
	
	
	public boolean deleteNodes(String word,String file) {
		TrieNode lastBranch=null;
		char childKeyOfLastBranch=' ';
		//System.out.println("entered into deletenodes");
		//Map<Character, TrieNode> children = root.children;
		TrieNode curr=root;
//		TrieNode t = null;
		for(int i=0; i<word.length(); i++){
			char c = word.charAt(i);
			if(curr.children.containsKey(c)){
				if(curr.children.size()>1) {
					lastBranch=curr;
					childKeyOfLastBranch=c;
				}
				//t = children.get(c);
				curr = curr.children.get(c);
				}
			else{
				return false;
//				return null;
			}
		}
		//System.out.println("Key:"+file+"\n\nlocatin: "+curr.location.toString());
		//System.out.println("\n\nContains?: "+curr.location.containsKey(file));
		//System.out.println(curr.location.toString());
		if((curr.isLeaf)&&(curr.location.containsKey(file))&&(lastBranch!=null)&&(childKeyOfLastBranch!=' ')) {
			if(curr.location.size()==1) {
				lastBranch.children.remove(childKeyOfLastBranch);
				//System.out.println("Removed  branch: "+lastBranch.children.toString()+"\n\nKey: "+childKeyOfLastBranch);
			}
			else
				curr.location.remove(file);
		}
		
		
		return true;
		
	}
	
	private void findSimilar(String s,TrieNode t,String k) {
		
		if(t.isLeaf) {
			Double d=Similarity(s,k);
			if(d>=0.4) 
			{

				System.out.println("The Word '"+k +"' is "+(int) (d*100)+"% matching with '"+s+"' in "+t.location.size()+" files ");
				String path,homeDir;

				//System.out.println("word:"+word);
				for(Map.Entry<String, ArrayList<String>> loc:t.location.entrySet())
				{
					path=(String)loc.getKey();
					homeDir=path.substring(0, ((path.contains("/"))?(path.indexOf('/')):(path.length())));
					//System.out.println(path+"\n"+homeDir);
					System.out.println("File name:\t"+((path.startsWith(homeDir)&&!path.equals(homeDir))?(path.replace(homeDir, "Home")):("Home/"+path))+"\nLine numbers:\t"+loc.getValue().toString());
				}
				//System.out.println(s+"  :  "+(int) (d*100)+"% similar"+"  :  "+t.location.toString());
			}
		}
		
		if(!t.children.isEmpty()) {
			for(Map.Entry<Character,TrieNode> m:t.children.entrySet()) {
				findSimilar(s+m.getKey(),m.getValue(),k);
			}
		}
	}
	
	private Set<Character> toSet(String s){
		List<Character> characterList = asList(s);
		Set<Character> characterSet = new HashSet<Character>(characterList);
		return characterSet;
	}
	private List<Character> asList(final String string) {
	    return new AbstractList<Character>() {
	       public int size() { return string.length(); }
	       public Character get(int index) { return string.charAt(index); }
	    };
	}
	
	private TrieNode searchNode(String str){
		Map<Character, TrieNode> children = root.children;
		TrieNode t = null;
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(children.containsKey(c)){
				t = children.get(c);
				children = t.children;
				}
			else{
				return null;
			}
		}
		return t;
	}
	private TreeMap<String,TrieNode> startNode(String str){
		Map<Character, TrieNode> children = root.children;
		
		TreeMap<String,TrieNode> tm=null;
		TrieNode t = null;
		String s="";
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(children.containsKey(c)){
				t = children.get(c);
				s=s+c;
				children = t.children;
				}
			else{
				tm=new TreeMap<String,TrieNode>();
				tm.put(s, t);
				
				return tm;
			}
		}
		tm=new TreeMap<String,TrieNode>();
		tm.put(s, t);
		return tm;
	}
	
	
	
}