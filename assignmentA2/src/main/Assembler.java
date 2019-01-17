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
	HashMap<String, String> conditionCode;
	LinkedHashMap<String,ArrayList> symTab;
	ArrayList<String> litTab;
	ArrayList<Integer> litAdd;
	ArrayList<Integer> poolTab;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Assembler() throws ClassNotFoundException, IOException {
		
		conditionCode = new HashMap<String,String>();
		conditionCode.put("LT","1");
		conditionCode.put("LTE","2");
		conditionCode.put("EQ","3");
		conditionCode.put("GT","4");
		conditionCode.put("GTE","5");
		conditionCode.put("ANY","6");
		
		FileInputStream fos = new FileInputStream("../assignmentA1/symTab.t");
	    ObjectInputStream oos = new ObjectInputStream(fos);
	    symTab = (LinkedHashMap<String,ArrayList>)oos.readObject();
	    
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
	    
	    String target = "";
	    
	    while ((line = br.readLine()) != null) {
	    	String words[] = line.split("[\\s]+");
	    	
	    	target = "";
	    	
	    	if(!line.contains("=")) {
	    		String lineNO = words[0];
		    	String lc = words[1];
		    	
		    	target = lc + ")\t"; 
		    	
		    	words[2] = words[2].replace("(","");
    			words[2] = words[2].replace(")","");
    			String[] instruction = null;
    			if(words[2].contains(",")){
    	    		instruction = words[2].split(",");
	    			target += instruction[1] + " ";
	    			//System.out.print(instruction[j] + "\t*\t");
	    			if(!instruction[0].equalsIgnoreCase("AD")) {
	    				System.out.println(target);
    	    		}
    	    	}
    			String[] operands = null;
    			if(words.length >= 4){
    				if(words[3].contains(".")){
	    	    		operands = words[3].split("\\.");
	    	    		for(int j= 0 ; j< operands.length ;j++){
	    	    			//System.out.print(operands[j] + "\t*\t");
	    	    		}
	    	    	}
    			}
    			 			
	    	}
	    	
	    	
	    	/*
	    	for(int i=0 ; i < words.length;i++){
	    		if(i==2){
	    			//System.out.println(words[2]);
	    			words[2] = words[2].replace("(","");
	    			words[2] = words[2].replace(")","");
	    			if(words[2].contains(",")){
	    	    		String[] instruction = words[2].split(",");
	    	    		for(int j= 0 ; j< instruction.length ;j++){
	    	    			System.out.print(instruction[j] + "\t*\t");
	    	    		}
	    	    	}
	    		}
	    		else if(i==3){
    				if(words[3].contains(".")){
	    	    		String[] operands = words[3].split("\\.");
	    	    		for(int j= 0 ; j< operands.length ;j++){
	    	    			System.out.print(operands[j] + "\t*\t");
	    	    		}
	    	    	}	
    			}
	    		else {
	    			System.out.print(words[i] + "\t*\t");
	    		}
	    	}
	    	System.out.println();
	    	*/
	 
	    
	    }
	    
	    br.close();
	    bw.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Assembler assembler = new Assembler();
		assembler.pass2();
	}

}
