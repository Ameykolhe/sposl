package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {
	
	HashMap<String, String> optab;
	LinkedHashMap<String,ArrayList<Integer>> symTab;
	LinkedHashMap<Integer,Integer> litTab;
	ArrayList<Integer> poolTab;
	
	int lc;
	
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
		
		
		symTab = new LinkedHashMap<String,ArrayList<Integer>>();
		litTab = new LinkedHashMap<Integer,Integer>();
		poolTab = new ArrayList<Integer>();
		
		lc = 0;
		
	}
	
	
	void insertToSymtab(String symbol, Integer address, Integer length){
			if(symTab.containsKey(symbol)){
				//System.out.println("Symbol : " + symbol + " already defined");
				if(length!=1){
					ArrayList<Integer> ar = (ArrayList<Integer>)symTab.get(symbol);
					ar.remove(1);
					ar.add(length);
				}
			}
			else{
				ArrayList<Integer> ar = new ArrayList<Integer>();
				ar.add(address);
				ar.add(length);
			}
	}
	
	
	@SuppressWarnings("unused")
	void pass1() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("sample.asm")); 
		BufferedWriter bw = new BufferedWriter(new FileWriter("Sample.intermediate"));
	    String line;
	    
	    Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*)([\\s]+)([a-zA-Z]+)([\\s]*)(.*)$");									//Group1 : Label			Group3 : Instruction	Group5 : operand
/**/	Pattern regLabelPattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");					//Group1 : op1(Register)	Group4 : op2(Label)
		Pattern regLiteralPattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)='([0-9]+)'$");						//Group1 : op1(Register)	Group4 : op2(Literal)
		Pattern bcPattern = Pattern.compile("^(LT|LTE|EQ|GT|GTE|ANY)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");							//Group1 : condition		Group4 : Label
	    Pattern labelOffsetPattern = Pattern.compile("^([a-zA-Z0-9]+)([\\s]*)([+|-])([\\s]*)([0-9]+)$");						//Group1 : Label			Group4 : Offset(int)
	    Pattern numOperandPattern = Pattern.compile("^([0-9]+)$");																//Group1 : Number(int)
	    Pattern constantPattern = Pattern.compile("^'([0-9]+)'$");																//Group1 : constant(int)
	    Pattern literralPattern = Pattern.compile("^='([0-9]+)'$");																//Group1 : Literal(int)
	    
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
            	
            	System.out.println("Line no     : " + lineNO);
            	
            	label = matcher.group(1);
            	instruction = matcher.group(3);
            	operand = matcher.group(5);
            	
            	if(!label.equalsIgnoreCase("")) {
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
            		}
            	}
            	// MOVEM MOVER ADD SUB MULT DIV
            	else if(instruction.equalsIgnoreCase("movem")||instruction.equalsIgnoreCase("mover")||instruction.equalsIgnoreCase("add")||instruction.equalsIgnoreCase("sub")||instruction.equalsIgnoreCase("mult")||instruction.equalsIgnoreCase("div")) {
            		
            		operandMatcher = regLabelPattern.matcher(operand);
            		operandMatcher1 = regLiteralPattern.matcher(operand);   
            		// REG LABEL
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			operand2 = operandMatcher.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            		}
            		// REG LITERAL
            		else if(operandMatcher1.find()) {
            			operand1 = operandMatcher1.group(1);
            			operand2 = operandMatcher1.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            		}
            		
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
            	// CONDITION
            	else if(instruction.equalsIgnoreCase("literralPatternbc")) {
            		operandMatcher = bcPattern.matcher(line);
            		// CONDITION LABEL
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			operand2 = operandMatcher.group(4);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operand2    : " + operand2);
            		}
            		else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
            	}
            	// ORIGIN EQU
            	else if(instruction.equalsIgnoreCase("origin")||instruction.equalsIgnoreCase("equ")) {
            		operandMatcher = numOperandPattern.matcher(operand);
            		operandMatcher1 = labelOffsetPattern.matcher(operand); 
            		// NUM OPERAND
            		if(operandMatcher.find()) {
            			operand1 = operandMatcher.group(1);
            			System.out.println("operand1    : " + operand1);
            		}
            		// LABEL operator OFFSET
            		else if(operandMatcher1.find()) {
            			operand1 = operandMatcher1.group(1);
            			operator = operandMatcher1.group(3);
            			operand2 = operandMatcher1.group(5);
            			System.out.println("operand1    : " + operand1);
            			System.out.println("operator    : " + operator);
            			System.out.println("operand2    : " + operand2);
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
            		}
        			else {
            			System.out.println("Error line  : " + lineNO + " INVALID OPERANDS");
            		}
        		}
            }
            else {
            	System.out.println("Invalid Syntax line no : " + lineNO);
            	//System.exit(0);
            }           	
            
    		lineNO++;
	    
    		System.out.println("\n");
    		
	    }
	    
	    br.close();
	}

	
	void showDetails() {
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Assembler assembler = new Assembler();
		assembler.pass1();
		
	}

}
