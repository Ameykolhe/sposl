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
	HashMap<String, String> regCode;
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
		
		regCode = new HashMap<String,String>();
		regCode.put("AREG","1");
		regCode.put("BREG","2");
		regCode.put("CREG","3");
		regCode.put("DREG","4");
		
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
	
	@SuppressWarnings("rawtypes")
	void pass2() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("../assignmentA1/intermediate.t")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("target.txt"));
	    String line;
	    
	    String target = "";
	    
	    String lc = null;
	    
	    int litIndex = 0;
	    
	    while ((line = br.readLine()) != null) {
	    	String words[] = line.split("[\\s]+");
	    	
	    	target = "";
	    	
	    	if(!line.contains("=")) {
	    		String lineNO = words[0];
		    	lc = words[1];
		    	
		    	target = lc + "\t)\t"; 
		    	
    			String[] instruction = null;
    			String[] operands = null;
    			if(words[2].contains(",")){
    				words[2] = words[2].replace("(","");
        			words[2] = words[2].replace(")","");
    				instruction = words[2].split(",");
	    			target += instruction[1] + " ";

	    			if(instruction[0].equalsIgnoreCase("AD")) {
	    				target = "";
    	    			}
	    			else if(instruction[0].equalsIgnoreCase("DL") && ( instruction[1].equals("02")  || instruction[1].equals("05") ) ) {
	    				target = "";
	    			}
	    			else if(instruction[0].equalsIgnoreCase("DL") && instruction[1].equals("01")) {
	    				words[3] = words[3].replace("(","");
	    				words[3] = words[3].replace(")","");
	    				operands = words[3].split(",");
	    				target = String.format("%s\t)\t00 0 00%s",lc,operands[1]);
	    			}
	    			else {
	        			if(words.length >= 4) {
	        				if(words[3].contains(".")) {
	    	    	    		operands = words[3].split("\\.");
	    	    	    		
	    	    	    		target += String.format("%s " , regCode.get(operands[0]));
	    	    	    		if(operands[1].contains(",")) {
	    	    	    			String temp =operands[1].replace("(","");
	    	    	    			temp = temp.replace(")","");
	    	    	    			String[] temp1 = temp.split(",");
	    	    	    			target += String.format("%d",litAdd.get(litIndex));
	    	    	    			litIndex += 1;
	    	    	    		}
	    	    	    		else {
	    	    	    			ArrayList ar = (ArrayList)symTab.get(operands[1]);
	    	    	    			int address = (Integer)ar.get(0);
	    	    	    			target += String.format("%d",address);
	    	    	    		}
	    	    	    		
	        				}
	        				else if( (!words[3].contains(",")) && (!words[3].contains("+")) && (!words[3].contains("-")) ) {
	        					ArrayList ar = (ArrayList)symTab.get(words[3]);
    	    	    			int address = (Integer)ar.get(0);
    	    	    			target += String.format("0 %d",address);
	        				}
	        				
	        			}
	        			else {
	        				target += "0 000";
	        			}
	    			}
    			 			
    			}
	    	}
	    	else if(words[2].contains("=")) {
				String temp = words[2].replace("'","");
				temp = temp.replace("=","");
				target = String.format("%s\t)\t00 0 00%s",lc,temp);
			}
	    	
	    	
	    	System.out.println(target);
	    	
	    	bw.write(target);
    		bw.newLine();	
	    
	    }
	    
	    br.close();
	    bw.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Assembler assembler = new Assembler();
		assembler.pass2();
	}

}
