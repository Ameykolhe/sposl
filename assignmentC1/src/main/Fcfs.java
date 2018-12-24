package main;

import java.util.Scanner;

public class Fcfs {

	public static void main(String[] args) {
		System.out.print("Enter no of Processes : ");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int []pid = new int[n];		//process id
		int []ar = new int[n];		//arrival Time
		int []bt = new int[n];		//burst time
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
		}
		
		
		//sort according to arrival time
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n-(i+1); j++) {
				if(ar[j]>ar[j+1]) {
					int temp = ar[j];
					ar[j] = ar[j+1];
					ar[j+1] = temp;
					temp = bt[j];
					bt[j] = bt[j+1];
					bt[j+1] = temp;
					temp = pid[j];
					pid[j] = pid[j+1];
					pid[j+1] = temp;
				}
			}
		}
		
		
		//calculate wait time, turn around time and completion time
		ct[0] = ar[0] + bt[0];
		tt[0] = ct[0] - ar[0];
		for(int i = 1; i < n; i++) {
			if(ar[i]<ct[i-1]) {
				wt[i] = ct[i-1] - ar[i];
				ct[i] = wt[i] + ar[i] + bt[i];
				tt[i] = ct[i] - ar[i];
			}
			else {
				ct[i] = ar[i] + bt[i];
				tt[i] = bt[i];
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



/*
Enter no of Processes : 3
Enter arrival time of process : 3
Enter burst time of process : 4
Enter arrival time of process : 2
Enter burst time of process : 3
Enter arrival time of process : 1
Enter burst time of process : 2
pid	 ar	 bt	 wt	 tt	 ct
3	1	2	0	2	3
2	2	3	1	4	6
1	3	4	3	7	10
Average wait time : 4.333333333333333
Average turn Around time : 1.3333333333333333
*/
