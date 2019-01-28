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
import java.util.Map;

import javafx.util.Pair;

public class MacroExpansion {
	
	ArrayList <String> mdt;
	HashMap <String,Pair<Integer,Integer>> mnt;
	
	@SuppressWarnings("unchecked")
	MacroExpansion() throws ClassNotFoundException, IOException {
		
		FileInputStream fos = new FileInputStream("../assignmentA3/mnt.t");
	    ObjectInputStream oos = new ObjectInputStream(fos);
	    mnt = (HashMap <String,Pair<Integer,Integer>>)oos.readObject();
	    
	    fos.close();
	    oos.close();
	    
	    fos = new FileInputStream("../assignmentA3/mdt.t");
	    oos = new ObjectInputStream(fos);
	    mdt= (ArrayList<String>)oos.readObject();

	    fos.close();
	    oos.close();
	    
	}
	
	
	void printTables() {
		System.out.println("\n\nMDT\n");
		for(int i=0; i< mdt.size(); i++) {
			System.out.println( i + "\t" + mdt.get(i));
		}
		System.out.println("\n\nMNT\n");
		for (Map.Entry<String,Pair<Integer,Integer>> entry : mnt.entrySet()) {
			Pair<Integer,Integer> p = entry.getValue();
            System.out.println(entry.getKey() + "\t" + p.getKey()  + "\t" + p.getValue());
		}
	}
	
	
	void expandMacro(int index , HashMap<String,String> avsp , BufferedWriter bw) throws IOException {
		String line = null;
		String[] words = null;
		String[] macroParameter = null;
		
		do {
			
			line = mdt.get(index);
			for (Map.Entry<String,String> entry : avsp.entrySet()) {
	            line = line.replaceAll(entry.getKey(),entry.getValue());
			}
			words = line.split("\t");
			
			if(mnt.containsKey(words[1])) {
				Pair<Integer,Integer> p = mnt.get(words[1]);
				int index1 = p.getKey();
				int noOfParameters = p.getValue();
				
				macroParameter = words[2].split(",");
				if(macroParameter.length == noOfParameters) {
					HashMap<String,String> avsp1 = new HashMap<String,String>();
					System.out.println("\n\nActual vs Positional Parameter for : " + line);
					for(int i = 1; i<=macroParameter.length; i++) {
						avsp1.put(String.format("#%d",i),macroParameter[i-1]);
						System.out.println("#" + i + "\t" + macroParameter[i-1]);
					}	
					expandMacro(index1,avsp1,bw);
				}
				else {
					System.out.println("Invalid Parameters");
				}
				
			}
			else {
				bw.write(line);
				bw.newLine();
			}
			index +=1;
		} while(!words[1].contains("MEND"));
		
	}
	
	
	@SuppressWarnings("unused")
	void pass2() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("../assignmentA3/intermediate.t")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("output.asm"));
		String line = null;
		
		String[] words = null;
		String[] macroParameter = null;
		
		while( (line = br.readLine()) != null) {
			words = line.split("\t");
			if(mnt.containsKey(words[1])) {
				Pair<Integer,Integer> p = mnt.get(words[1]);
				int index = p.getKey();
				int noOfParameters = p.getValue();
				
				macroParameter = words[2].split(",");
				if(macroParameter.length == noOfParameters) {
					HashMap<String,String> avsp = new HashMap<String,String>();
					System.out.println("\n\nActual vs Positional Parameter for : " + line);
					for(int i = 1; i<=macroParameter.length; i++) {
						avsp.put(String.format("#%d",i),macroParameter[i-1]);
						System.out.println("#" + i + "\t" + macroParameter[i-1]);
					}
					expandMacro(index,avsp,bw);
				}
				else {
					System.out.println("Invalid Parameters");
				}
				
			}
			else {
				bw.write(line);
				bw.newLine();
			}
		}
		
		br.close();
		bw.close();
	}
	

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		MacroExpansion m = new MacroExpansion();
		//m.printTables();
		m.pass2();
		System.out.println("Expansion Complete");
	}

}
