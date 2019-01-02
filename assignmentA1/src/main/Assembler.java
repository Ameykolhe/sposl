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
	    
	    Pattern pattern = Pattern.compile("^([a-zA-Z0-9]*)([\\s]+)([a-zA-Z]+)([\\s]*)(.*)$");												//Group1 : Label	Group3 : Instruction	Group5 : operand
/**/	Pattern regLabelOrLiteralPattern = Pattern.compile("^(AREG|BREG|CREG|DREG)([\\s]*),([\\s]*)([a-zA-Z0-9]+|='([0-9]+)')$");			//Group1 : op1(Register)	Group4 : op2(Label)	Group5: op2(literal)
	    Pattern bcPattern = Pattern.compile("^(LT|LTE|EQ|GT|GTE|ANY)([\\s]*),([\\s]*)([a-zA-Z0-9]+)$");										//Group1 : condition	Group4 : Label
	    Pattern labelOffsetPattern = Pattern.compile("^([a-zA-Z0-9]+)([\\s]*)[+|-]([\\s]*)([0-9]+)$");										//Group1 : Label	Group4 : Offset(int)
	    Pattern numOperand = Pattern.compile("^([0-9]+)$");																					//Group1 : Number(int)
	    Pattern constantPattern = Pattern.compile("^'([0-9]+)'$");
	    Pattern literralPattern = Pattern.compile("^='([0-9]+)'$");
	    
	    Matcher matcher;
	    Matcher operandMatcher;
	    
	    int lineNO = 1;
	    
	    String label;
	    String instruction;
	    String operand;
	    
	    String operand1,operand2;
    	
	    while ((line = br.readLine()) != null) {
	    	
            matcher = pattern.matcher(line);
            if (matcher.find())
            {
            	
            	System.out.println("Line no     : " + lineNO);
            	label = matcher.group(1);
            	instruction = matcher.group(3);
            	operand = matcher.group(5);
            	if(!label.equalsIgnoreCase("")) {
            		System.out.println("Symbol      : " + label);
            	}
            	
            	System.out.println("Instruction : " + instruction);
            	
            	if(instruction.equalsIgnoreCase("movem")||instruction.equalsIgnoreCase("mover")||instruction.equalsIgnoreCase("add")||instruction.equalsIgnoreCase("sub")||instruction.equalsIgnoreCase("mult")||instruction.equalsIgnoreCase("div")) {
            		operandMatcher = regLabelOrLiteralPattern.matcher(operand);
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
            	else {
            		System.out.println("Operand     : " + operand);
            	}
            	
            	
            	System.out.println("\n");
            	
            }
            else {
            	System.out.println("Invalid Syntax line no : " + lineNO);
            	//System.exit(0);
            }
    		
    		lineNO++;
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
