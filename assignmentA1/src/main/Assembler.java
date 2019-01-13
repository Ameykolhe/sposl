package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {
	
	HashMap<String, String> optab;
	LinkedHashMap<String,ArrayList> symTab;
	ArrayList<String> litTab;
	ArrayList<Integer> litAdd;
	ArrayList<Integer> poolTab;
	
	int lc;
	
	@SuppressWarnings("rawtypes")
	Assembler(){
		
		optab =  new HashMap<String,String>();
		
		optab.put("STOP", "IS,00");
		optab.put("ADD", "IS,01");
		optab.put("SUB", "IS,02");
		optab.put("MULT", "IS,03");
		optab.put("MOVER", "IS,04");
		optab.put("MOVEM", "IS,05");
		optab.put("COMP", "IS,06");
		optab.put("BC", "IS,07");
		optab.put("DIV", "IS,08");
		optab.put("READ", "IS,09");
		optab.put("PRINT", "IS,10");
		optab.put("DC", "DL,01");
		optab.put("DS", "DL,02");
		optab.put("START", "AD,01");
		optab.put("END", "AD,02");
		optab.put("ORIGIN", "AD,03");
		optab.put("EQU", "AD,04");
		optab.put("LTORG", "DL,05");
		
		
		symTab = new LinkedHashMap<String,ArrayList>();
		litTab = new ArrayList<String>();
		litAdd = new ArrayList<Integer>();;
		poolTab = new ArrayList<Integer>();
		poolTab.add(new Integer(0));
		
		lc = 0;
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public void printTables() {
	
		//Display optab
		Set mapSet = optab.entrySet();
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

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void insertToSymtab(String symbol, Integer address, Integer length){
		
			//System.out.println("inserting " + symbol);
			if(symTab.containsKey(symbol)){
				System.out.println("Symbol : " + symbol + " already defined");
			}
			else{
				ArrayList ar = new ArrayList();
				ar.add(address);
				ar.add(length);
				symTab.put(symbol,ar);
			}
	}
	
	
	void insertToLitTab(String value) {
		litTab.add(value);
		litAdd.add(new Integer(0));
	}
	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	void pass1() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("sample.asm")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("Sample.intermediate"));
	    String line;
	    
	    Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*)([\\s]+)([a-zA-Z]+)([\\s]*)(.*)$");									//Group1 : Label			Group3 : Instruction	Group5 : operand
	    Pattern regLabelPattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");					//Group1 : op1(Register)	Group4 : op2(Label)
		Pattern regLiteralPattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)='([0-9]+)'$");						//Group1 : op1(Register)	Group4 : op2(Literal)
		Pattern bcPattern = Pattern.compile("^(LT|LTE|EQ|GT|GTE|ANY)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");							//Group1 : condition		Group4 : Label
	    Pattern labelOffsetPattern = Pattern.compile("^([a-zA-Z0-9]+)([\\s]*)([+|-])([\\s]*)([0-9]+)$");						//Group1 : Label			Group4 : Offset(int)
	    Pattern numOperandPattern = Pattern.compile("^([0-9]+)$");																//Group1 : Number(int)
	    Pattern constantPattern = Pattern.compile("^'([0-9]+)'$");																//Group1 : constant(int)
	    Pattern literralPattern = Pattern.compile("^='([0-9]+)'$");																//Group1 : Literal(int)
	    Pattern labelPattern = Pattern.compile("^([a-zA-Z1-9]*)$");
	    
	    Matcher matcher;
	    Matcher operandMatcher,operandMatcher1;
	    
	    int lineNO = 1;
	    
	    String label;
	    String instruction;
	    String operand;
	    
	    String operand1,operand2;
	    
	    String operator;
	    
	    String intermediateLine = "";
    	
	    while ((line = br.readLine()) != null) {
	    	
            matcher = pattern.matcher(line);
            if (matcher.find()) {
            	
            	System.out.println("Line no     : " + lineNO + "\nLC          : " + lc);
            	
            	label = matcher.group(1);
            	instruction = matcher.group(3);
            	operand = matcher.group(5);
            	
            	intermediateLine = String.format("(%s)",optab.get(instruction.toUpperCase()));
            	
            	if(!label.equalsIgnoreCase("") && !instruction.equalsIgnoreCase("equ") && !instruction.equalsIgnoreCase("ds")) {
            		System.out.println("Symbol      : " + label);
            		insertToSymtab(label , new Integer(lc), new Integer(1));
            	}
            	
            	System.out.println("Instruction : " + instruction);
            	
            	//START
            	if(instruction.equalsIgnoreCase("start")){
            		operandMatcher = numOperandPattern.matcher(operand);
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            			lc = Integer.parseInt(operand1);
            			intermediateLine += String.format(" (c,%s)",operand1);
            		}
            	}
            	// MOVEM MOVER ADD SUB MULT DIV
            	else if(instruction.equalsIgnoreCase("stop")) {
            		lc+=1;
            	}
            	else if(instruction.equalsIgnoreCase("movem")||instruction.equalsIgnoreCase("mover")||instruction.equalsIgnoreCase("add")||instruction.equalsIgnoreCase("sub")||instruction.equalsIgnoreCase("mult")||instruction.equalsIgnoreCase("div")) {
            		
            		operandMatcher = regLabelPattern.matcher(operand);
            		operandMatcher1 = regLiteralPattern.matcher(operand);   
            		// REG LABEL
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			operand2 = operandMatcher.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            			
            			intermediateLine += String.format(" %s,%s",operand1,operand2);
            			lc+=1;
            		}
            		// REG LITERAL
            		else if(operandMatcher1.find()) {
            			operand1 = operandMatcher1.group(1);
            			operand2 = operandMatcher1.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            			
            			insertToLitTab(operand2);
            			intermediateLine += String.format(" %s,%s" , operand1,operand2);
            			lc+=1;
            		}
            		
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
            	// CONDITION
            	else if(instruction.equalsIgnoreCase("bc")) {
            		operandMatcher = bcPattern.matcher(line);
            		// CONDITION LABEL
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			operand2 = operandMatcher.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            			
            			lc+=1;
            		}
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
            	// ORIGIN
            	else if(instruction.equalsIgnoreCase("origin")) {
            		operandMatcher = numOperandPattern.matcher(operand);
            		operandMatcher1 = labelOffsetPattern.matcher(operand); 
            		// NUM OPERAND
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            			
            			lc = Integer.parseInt(operand1);
            			
            			intermediateLine += String.format(" %s",operand1);
            			
            		}
            		// LABEL operator OFFSET
            		else if(operandMatcher1.find()) {
            			operand1 = operandMatcher1.group(1);
            			operator = operandMatcher1.group(3);
            			operand2 = operandMatcher1.group(5);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operator    : " + operator);
            			System.out.println("operand2    : " + operand2);
            			
            			ArrayList ar = (ArrayList)symTab.get(operand1);
            			
            			if(operator.equals("+")) {
            				lc = (Integer)ar.get(0) + Integer.parseInt(operand2);
            			}
            			else if(operator.equals("-")) {
            				lc = (Integer)ar.get(0) - Integer.parseInt(operand2);
            			}
            			
            			intermediateLine += String.format(" %s%s%s",operand1,operator,operand2);
            		}
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
            	// EQU
            	else if(instruction.equalsIgnoreCase("equ")) {
            		operandMatcher = labelPattern.matcher(operand); 
            		operandMatcher1 = labelOffsetPattern.matcher(operand); 
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            			
            			Integer address = (Integer)((ArrayList)symTab.get(operand1)).get(0);
        				insertToSymtab(label,address,new Integer(1));
        				
        				intermediateLine += String.format(" %s",operand1);
            		}
            		// LABEL operator OFFSET
            		else if(operandMatcher1.find()) {
            			operand1 = operandMatcher1.group(1);
            			operator = operandMatcher1.group(3);
            			operand2 = operandMatcher1.group(5);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operator    : " + operator);
            			System.out.println("operand2    : " + operand2);
            			
            			if(operator.equals("+")) {
							Integer address = (Integer)((ArrayList)symTab.get(operand1)).get(0) + Integer.parseInt(operand2);
            				insertToSymtab(label,address,new Integer(1));
            			}
            			else if(operator.equals("-")) {
            				Integer address = (Integer)((ArrayList)symTab.get(operand1)).get(0) + Integer.parseInt(operand2);
            				insertToSymtab(label,address,new Integer(1));	
            			}
            			
            			intermediateLine += String.format(" %s%s%s",operand1,operator,operand2);
            		}
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
        		// DC
        		else if(instruction.equalsIgnoreCase("dc")) {
        			operandMatcher = constantPattern.matcher(operand);
        			//
        			if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            			
            			intermediateLine += String.format(" (c,%s)" , operand1);
            			lc+=1;
            		}
        			else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
        		}
        		// DS
        		else if(instruction.equalsIgnoreCase("ds")) {
        			operandMatcher = numOperandPattern.matcher(operand);
        			if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            			insertToSymtab(label , new Integer(lc), Integer.parseInt(operand1));
            			intermediateLine += String.format(" (c,%s)" , operand1);
            			lc += Integer.parseInt(operand1);
            		}
        			else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
        		}
            	//READ PRINT
                else if(instruction.equalsIgnoreCase("read")||instruction.equalsIgnoreCase("print")) {
                	intermediateLine += String.format(" %s",operand);
                	lc += 1;
                }
            }
            else {
            	System.out.println("Invalid Syntax line no : " + lineNO);
            	//System.exit(0);
            }           	
            
    		lineNO++;
    		bw.write(intermediateLine);
    		bw.newLine();
    		System.out.println("\n" + intermediateLine + "\n");
    		
	    }
	    
	    br.close();
	    bw.close();
	}

	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Assembler assembler = new Assembler();
		assembler.pass1();
		assembler.printTables();
	}

}
