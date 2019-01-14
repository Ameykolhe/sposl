package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Assembler {
	
	HashMap<String, String> opTab;
	LinkedHashMap<String,ArrayList> symTab;
	ArrayList<String> litTab;
	ArrayList<Integer> litAdd;
	ArrayList<Integer> poolTab;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Assembler() throws ClassNotFoundException, IOException {
		
		FileInputStream fos = new FileInputStream("../assignmentA1/symTab.t");
	    ObjectInputStream oos = new ObjectInputStream(fos);
	    symTab = (LinkedHashMap<String,ArrayList>)oos.readObject();
	    
	    fos.close();
	    oos.close();
	    
	    fos = new FileInputStream("../assignmentA1/opTab.t");
	    oos = new ObjectInputStream(fos);
	    opTab = (HashMap<String, String>)oos.readObject();
	    
	    fos.close();
	    oos.close();
	    
	    fos = new FileInputStream("../assignmentA1/litTab.t");
	    oos = new ObjectInputStream(fos);
	    litTab = (ArrayList<String>)oos.readObject();

	    fos.close();
	    oos.close();
	    
	    fos = new FileInputStream("../assignmentA1/litAdd.t");
	    oos = new ObjectInputStream(fos);
	    litAdd = (ArrayList<Integer>)oos.readObject();

	    fos.close();
	    oos.close();
	}
	
	@SuppressWarnings("rawtypes")
	public void printTables() {
	
		//Display optab
		Set mapSet = opTab.entrySet();
		Iterator mapIt = mapSet.iterator();
		System.out.println("\n\nOptab\n");
		while(mapIt.hasNext()) {
			Map.Entry mapEntry = (Map.Entry)mapIt.next();
			String key = (String)mapEntry.getKey();
			String val = (String)mapEntry.getValue();
			System.out.println(key + "\t" + val);
		}
		
		//Display SymTab
		mapSet = symTab.entrySet();
		mapIt = mapSet.iterator();
		System.out.println("\n\nsymTab\n");
		while(mapIt.hasNext()) {
			Map.Entry mapEntry = (Map.Entry)mapIt.next();
			String key = (String)mapEntry.getKey();
			ArrayList val = (ArrayList)mapEntry.getValue();
			System.out.print(key + "\t");
			for(int i=0 ; i<val.size(); i++) {
				System.out.print(val.get(i) + "\t");
			}
			System.out.println();
		}
		
		//Display litTab
		System.out.println("\n\nLitTab");
		for(int i=0; i<litTab.size(); i++) {
			System.out.println(litTab.get(i) + "\t" + litAdd.get(i));
		}
		
	}
	
	void pass2() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("../assignmentA1/intermediate.t")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("target.txt"));
	    String line;
	    
	    while ((line = br.readLine()) != null) {
	    	System.out.println(line);
	    }
	    
	    br.close();
	    bw.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Assembler assembler = new Assembler();
		assembler.pass2();
	}

}
