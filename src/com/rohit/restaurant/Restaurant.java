package com.rohit.restaurant;

import java.util.Scanner;

/* Main class from where all the threads are run
 * and global time is maintained
 */
public class Restaurant {
	
	public static void main(String[] args) {
		//Handling input from the user
		Scanner in = new Scanner(System.in);
		int totalDiners = in.nextInt();
		int tables = in.nextInt();
		int totalCooks = in.nextInt();
		int[][] orders = new int[totalDiners][4];
		for(int i=0; i<totalDiners; i++) {
			for(int j=0; j<4; j++) {
				orders[i][j] = in.nextInt();
			}
		}
		in.close();
		
		//Creating a single resource object which will be shared among all the threads
		Resources res = new Resources(tables, totalCooks, totalDiners);
		
		//Diner objects for Diner threads
		Diner[] diners = new Diner[totalDiners];
		
		for(int i=0; i<totalDiners; i++) {
			diners[i] = new Diner((i+1),new Order((i+1),orders[i][1],orders[i][2],orders[i][3]),res);
		}
		
		//Cook objects for Cook threads
		Cook[] cooks = new Cook[totalCooks];
		
		for(int i=0; i<totalCooks; i++) {
			cooks[i] = new Cook((i+1),res);
			cooks[i].start();
		}
		
		int nextDiner = 0;
		
		//Updating global clock every 1s
		for(int time=0; ;time++){
			res.currentTime = time;
			if(time == orders[nextDiner][0]){
				diners[nextDiner].start();			//If the arrival time of Diner is equal to current time, then start thread
				if(nextDiner<totalDiners-1){
					nextDiner++;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}