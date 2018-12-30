package main;

import java.util.ArrayList;
import java.util.Scanner;
public class Priority {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		System.out.print("Enter no of Processes : ");
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int []pid = new int[n];		//process id
		int []ar = new int[n];		//arrival Time
		int []bt = new int[n];		//burst time
		int []baltime = new int[n];	//balance Time
		int []wt = new int[n];		//wait time
		int []tt = new int[n];		//trun around Time
		int []ct = new int[n];		//Completion time
		int []pri = new int[n];
		
		
		//input from user
		for(int i = 0; i < n; i++) {
			pid[i] = i+1;
			System.out.print("Enter arrival time of process : ");
			ar[i] = sc.nextInt();
			System.out.print("Enter burst time of process : ");
			bt[i] = sc.nextInt();
			baltime[i] = bt[i];
			System.out.print("Enter priority  : ");
			pri[i] = sc.nextInt();
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
					temp = pri[j];
					pri[j] = pri[j+1];
					pri[j+1] = temp;
					temp = baltime[j];
					baltime[j] = baltime[j+1];
					baltime[j+1] = temp;
				}
			}
		}
		
		
		//display details
		System.out.println("pid\t ar\t bt\t pr\t wt\t tt\t ct");
		for(int i = 0; i < n; i++) { 
			System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d\t%d",pid[i],ar[i],bt[i],pri[i],wt[i],tt[i],ct[i]));
		}
		
		
		ArrayList qq = new ArrayList();
		int index = 0;
		int counter = 0;
		
		while(true) {
			if(index == n && qq.isEmpty()) {
				break;
			}
			while(true) {
				if(index != n) {
					if(ar[index] == counter) {
						if(qq.isEmpty()) {
							qq.add(new Integer(index));
							index += 1;
						}
						else {
							int i = 0;
							for( ; i < qq.size(); i++) {
								int temp = (Integer)qq.get(i);
								if(pri[index] <= pri[temp]) {
									break;
								}
							}
							if(qq.size() == i + 1){
								qq.add(i, new Integer(index));
							}
							else {
								qq.add(i, new Integer(index));
							}
							index += 1;
						}
					}
					else {
						break;
					}
				}
				else {
					break;
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
			System.out.println("counter   : " + counter);
			System.out.println("Index     : " + index);
			System.out.println("QueueSize : " + qq.size());
			
		}
		
		
		for(int i = 0; i < n; i++) {
			tt[i] = ct[i] - ar[i];
			if(wt[i] != 0) {
				wt[i] += 1;
			}
		}
		
		
		//display details
		System.out.println("pid\t ar\t bt\t pr\t wt\t tt\t ct");
		for(int i = 0; i < n; i++) { 
			System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d\t%d",pid[i],ar[i],bt[i],pri[i],wt[i],tt[i],ct[i]));
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

//test case : 7 0 3 2 2 5 6 1 4 3 4 2 5 6 9 7 5 4 4 7 10 10

Process Id	Priority	Arrival Time	Burst Time	Completion Time	Turnaround Time	Waiting Time	Response Time
  1		2	  0         	3	  3     	3	       0	0
2	6	2	5	18	16	11	13
3	3	1	4	7	6	2	3
4	5	4	2	13	9	7	11
5	7	6	9	27	21	12	18
6	4	5	4	11	6	2	7
7	10	7	10	37	30	18	27


*/