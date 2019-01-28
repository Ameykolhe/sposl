package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class MacroExpansion {

	ArrayList <String> mdt;
	HashMap <String,Pair<Integer,Integer>> mnt;
	
	MacroExpansion() {
		mdt = new ArrayList<String>();
		mnt = new HashMap<String,Pair<Integer,Integer>>();
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
	
	
	void pass1() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("input.asm")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("intermediate.t"));
		String line = null;
		
		String[] words = null;
		int defLevel = 0;
		String[] macroParameter = null;
		
		while( (line = br.readLine()) != null) {
			words = line.split("\t");
			if(words[1].equals("MACRO")){
				macroParameter = words[3].split(",");
				mnt.put(new String(words[2]),new Pair<Integer,Integer>(mdt.size(),macroParameter.length));
				HashMap<String,String> fvsa = new HashMap<String,String>();
				System.out.println("\nFormal vs Positional Parameter for : " + line);
				for(int i=1; i<=macroParameter.length; i++) {
					fvsa.put(macroParameter[i-1],String.format("#%d",i));
					System.out.println(macroParameter[i-1] + "\t#" + i);
				}
				do {
					line = br.readLine();
					words = line.split("\t");
					for (Map.Entry<String,String> entry : fvsa.entrySet()) { 
			            line = line.replaceAll(entry.getKey(),entry.getValue());
					} 
					mdt.add(line);
				}while(!words[1].equals("MEND"));
				
			}
			else {
				bw.write(line);
				bw.newLine();
			}
		}
		
		FileOutputStream fos = new FileOutputStream("mnt.t");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(mnt);
	    
	    fos = new FileOutputStream("mdt.t");
	    oos = new ObjectOutputStream(fos);
	    oos.writeObject(mdt);
		
		br.close();
		bw.close();
	}	
	
	public static void main(String[] args) throws IOException {
		
		MacroExpansion m = new MacroExpansion();
		m.pass1();
		m.printTables();
	}

}
