package com.johnroid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TrieNode implements Serializable {
	char c;
	HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
	boolean isLeaf;
	
	HashMap<String, ArrayList<String>> location=null;
	//boolean isSharedFile=false;
	public TrieNode() {
		isLeaf=false;
	}
	public TrieNode(char c){
		this.c = c;
		isLeaf=false;
	}
}
