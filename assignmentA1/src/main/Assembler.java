package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {
	
	Hashtable<String,ArrayList<Integer>> symTab;
	LinkedHashMap<Integer,Integer> litTab;
	ArrayList<Integer> poolTab;
	
	int lc;
	
	Assembler(){
		symTab = new Hashtable<String,ArrayList<Integer>>();
		litTab = new LinkedHashMap<Integer,Integer>();
		poolTab = new ArrayList<Integer>();
		
		lc = 0;
		
	}
	
	
	void pass1() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("sample.asm")); 
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
    	
	    while ((line = br.readLine()) != null) {
	    	
            matcher = pattern.matcher(line);
            if (matcher.find()) {
            	
            	System.out.println("Line no     : " + lineNO);
            	label = matcher.group(1);
            	instruction = matcher.group(3);
            	operand = matcher.group(5);
            	if(!label.equalsIgnoreCase("")) {
            		System.out.println("Symbol      : " + label);
            	}
            	
            	System.out.println("Instruction : " + instruction);
            	
            	// MOVEM MOVER ADD SUB MULT DIV
            	if(instruction.equalsIgnoreCase("movem")||instruction.equalsIgnoreCase("mover")||instruction.equalsIgnoreCase("add")||instruction.equalsIgnoreCase("sub")||instruction.equalsIgnoreCase("mult")||instruction.equalsIgnoreCase("div")) {
            		
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
            	else if(instruction.equalsIgnoreCase("bc")) {
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
