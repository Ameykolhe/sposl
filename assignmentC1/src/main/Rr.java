package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Rr {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		
		System.out.print("Enter no of Processes : ");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int []pid = new int[n];		//process id
		int []ar = new int[n];		//arrival Time
		int []bt = new int[n];		//burst time
		int []baltime = new int[n];	//balance time
		int []wt = new int[n];		//wait time
		int []tt = new int[n];		//trun around Time
		int []ct = new int[n];		//Completion time
		
		int timeSlice = 4;
		
		//input from user
		for(int i = 0; i < n; i++) {
			pid[i] = i+1;
			System.out.print("Enter arrival time of process : ");
			ar[i] = sc.nextInt();
			System.out.print("Enter burst time of process : ");
			bt[i] = sc.nextInt();
			baltime[i] = bt[i];
		}
		
		
		//sort according to arrival time
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n-(i+1); j++) {
				if(ar[j]>ar[j+1]) {					//swaps
					int temp = ar[j];
					ar[j] = ar[j+1];
					ar[j+1] = temp;
					temp = bt[j];
					bt[j] = bt[j+1];
					bt[j+1] = temp;
					temp = pid[j];
					pid[j] = pid[j+1];
					pid[j+1] = temp;
					temp = baltime[j];
					baltime[j] = baltime[j+1];
					baltime[j+1] = temp;
				}
			}
		}
		
		ArrayList qq = new ArrayList();
		
		int counter = 0;		//clock time
		int index = 0;			//iterate through all processes
		boolean fl;
		
		while(true) {
			
			fl = true;
			
			if(index == n && qq.size() == 0) {
				break;
			}
			
			for(int i = 1; i <= timeSlice; i++) {
			
				System.out.println("\nCounter : " + (counter + 1));
				
				while(true) {
					if(index != n) {
						if(ar[index] == counter) {
							System.out.println("Inserting pid : " + (index + 1));
							qq.add(new Integer(index));
							index += 1;
						}
						else {
							break;
						}
					}
					else{
						break;
					}
				}
				
				
				for(int j = 0; j < qq.size(); j++) {
					int temp = (int)qq.get(j);
					if(j == 0) {
						baltime[temp] -= 1;
						System.out.println("Process ID : " + pid[temp] + "\tBalance TIme : " + baltime[temp]);
						if(baltime[temp] == 0) {
							ct[temp] = counter + 1;
							qq.remove(0);
							System.out.println("removed : " + pid[temp]);
							fl  = false;
						}
					}
					else {
						System.out.println("Process ID : " + pid[temp] + "\tBalance TIme : " + baltime[temp]);
					}
				
				}
				
				counter += 1;
				/*
				if(!fl) {
					break;
				}
				*/
			}
			
			
			if(!qq.isEmpty()) {
				qq.add(new Integer((int)qq.get(0)));
				qq.remove(0);
			}
			
			System.out.println("End Of TimeSlice\n");
		}
		
		
		for(int i = 0; i < n; i++) {
			tt[i] = ct[i] - ar[i];
			wt[i] = tt[i] - bt[i];
		}
		
		
		//display details
		System.out.println("pid\t ar\t bt\t wt\t tt\t ct");
		for(int i = 0; i < n; i++) { 
			System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d",pid[i],ar[i],bt[i],wt[i],tt[i],ct[i]));
		}
		
		int ttt = 0;
		int twt = 0;
		for(int i = 0; i < n; i++) {
			ttt += tt[i];
			twt += wt[i];
		}
		
		System.out.println("Average wait time : " + twt*1.0/n);
		System.out.println("Average turn Around time : " + ttt*1.0/n);
		
		
		sc.close();
	}

}


//Test Case :  6 0 5 1 6 2 3 3 1 4 5 6 4

