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

public class MacroExpansion {

	ArrayList <String> mdt;
	HashMap <String,Integer> mnt;
	
	MacroExpansion() {
		mdt = new ArrayList<String>();
		mnt = new HashMap<String,Integer>();
	}
	
	
	void pass1() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("sample.asm")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("intermediate.t"));
		String line = null;
		
		while( (line = br.readLine()) != null) {
			
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
