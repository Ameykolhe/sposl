package main;


import java.util.ArrayList;
import java.util.Scanner;


public class BankersAlgorithm {
	ArrayList available;
	int max[][], allocated[][], need[][];
	
	private void print(Object obj){ System.out.println(obj);}
	
	public void printTables(){
		// Print available
		for(Object obj: available){
			print(obj);
		}
		
		// Print max
		System.out.println("\n\nMax Table");
		for(int i = 0;i < max.length;i ++){
			for(int j = 0; j < max[0].length; j++){
				System.out.print(max[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		//Print allocated
		System.out.println("\n\nallocated Table");
		for(int i = 0;i < allocated.length;i ++){
			for(int j = 0; j < allocated[0].length; j++){
				System.out.print(allocated[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		// Print need
		System.out.println("\n\nNeed Table");
		for(int i = 0;i < need.length;i ++){
			for(int j = 0; j < need[0].length; j++){
				System.out.print(need[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	private int[] add(int a[], int b[]) throws Exception{
		int sum[] = new int[a.length];
		
		try{
			if(a.length != b.length){
				throw new Exception("Size error");
			}
		}
		finally{
			
		}
		
		for(int i = 0;i < a.length;i ++){
			sum[i] = a[i] + b[i];
		}
		
		return sum;
	}
	
	private int[][] subtract(int a[][], int b[][]) throws Exception{
		
		int difference[][] = new int[a.length][a[0].length];
		
		try{
			if(a.length != b.length || a[0].length != b[0].length){
				throw new Exception("Size invalid");
			}
		}
		finally{
			
		}
		
		for(int i = 0;i < a.length;i ++){
			for(int j = 0;j < a[0].length; j++){
				difference[i][j] = a[i][j] - b[i][j];
			}
		}
		return difference;
	}
	
	public BankersAlgorithm(int processes, int resources){
		
		available = new ArrayList();
		
		for(int iter=0;iter < resources;iter++){
			available.add(new Integer(resources%5));
		}
		
		max = new int[processes][resources];
		allocated = new int[processes][resources];
		need = new int[processes][resources];
		
	}
	
	public void fillMatrices() throws Exception{
		Scanner sc = new Scanner(System.in);
		
		// Input all the max matrix elements
		print("Enter max matrix(row major):");
		
		for(int i = 0;i < max.length;i ++){
			for(int j = 0;j < max[0].length; j++){
				max[i][j] = sc.nextInt();
			}
		}
		
		//Input all the allocated elements
		print("Enter allocated matrix(row major):");
		
		for(int i = 0;i < allocated.length;i ++){
			for(int j = 0;j < allocated[i].length; j++){
				allocated[i][j] = sc.nextInt();
			}
		}
		
		// Fill the need matrix;
		need = subtract(max, allocated);
	}
	
	// Find if need is less than or equal
	private boolean needLeWork(int need[], int work[]){
		for(int i = 0;i < need.length;i ++){
			if(need[i] <= work[i]){
				return true;
			}
		}
		return false;
	}
	
	public boolean safetyAlgorithm() throws Exception{
		
		// Create work and finish array
		int work[] = new int[available.size()];
		for(int i = 0;i < work.length;i ++){
			work[i] = (Integer) available.get(i);
		}
		int finish[] = new int[max.length];
		
		// Find i for which finish is false and for all resources
		// need[i] <= work
		// if you find such i then add work to it
		
		for(int i = 0;i < finish.length; i++){
			
			if(finish[i] == 0 && needLeWork(need[i], work)){
				work = add(work, allocated[i]);
				finish[i] = 1;
			}
		}
		
		// Finally check finish
		for(int i = 0;i < finish.length;i ++){
			if(finish[i]==0){
				return false;
			}
		}
		return true;
	}
	
	// TODO: Request resource algorithm left
	
	public static void main(String args[]) throws Exception{
		BankersAlgorithm a = new BankersAlgorithm(5,3);
		a.fillMatrices();
		a.printTables();
		boolean status = a.safetyAlgorithm();
		if(status){
			System.out.println("Safe");
		}
		else{
			System.out.println("Not Safe");
		}
		
	}
	
}


//test case : 7 5 3 3 2 2 9 0 2 2 2 2 2 4 3 3 0 1 0 2 0 0 3 0 2 2 1 1 0 0 2