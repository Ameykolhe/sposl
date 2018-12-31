package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class AssemblerPass1 {
	
	Hashtable<String,ArrayList<Integer>> symTab;
	LinkedHashMap<Integer,Integer> litTab;
	ArrayList<Integer> poolTab;
	
	int lc;
	
	AssemblerPass1(){
		symTab = new Hashtable<String,ArrayList<Integer>>();
		litTab = new LinkedHashMap<Integer,Integer>();
		poolTab = new ArrayList<Integer>();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("sample.asm")); 
	    String line;
	    while ((line = br.readLine()) != null) {
	    	String[] content = line.split("\t");
	    	
	    	if(!content[0].equalsIgnoreCase("")){
    			System.out.println("Symbol");
    		}
	    	
	    	for(int i = 0; i < content.length; i++){
	    		System.out.print(content[i] + "\t");
	    	}
	    	System.out.println("");
	    }
	}

}
