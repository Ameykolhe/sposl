package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Sjf {
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		
		while(true) {
			if(index == n && qq.size() == 0) {
				break;
			}
			if(index != n) {
				if(ar[index] == counter) {
					if(qq.isEmpty()) {
						qq.add(new Integer(index));
					}
					else {
						int i = 0;
						for( ; i < qq.size(); i++) {
							int temp = (Integer)qq.get(i);
							//***
							if(baltime[index] <= baltime[temp]) {
								break;
							}
						}
						qq.add(i, new Integer(index));
					}
					index += 1;
				}
			}
			
			for(int i=0; i < qq.size(); i++) {	//loop to update baltime / waittime of processes
				int temp = (Integer)qq.get(i);
				//***
				if(i == 0) {					//process to be serviced
					baltime[temp] -= 1;
					if(baltime[temp] == 0) {
						ct[temp] = counter + 1;
						qq.remove(0);
					}
				}
				else {
					wt[temp] += 1;				
				}
				
			}
			
			counter += 1;
			//***
		}
		
		
		for(int i = 0; i < n; i++) {
			tt[i] = ct[i] - ar[i];
			if(wt[i] != 0) {
				wt[i] += 1;
			}
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