package com.rohit.restaurant;

public class Order {
	int number;						//Order number
	int burgers;					//Number of burger
	int fries;						//Number of fries
	int coke;						//Number of coke
	boolean isComplete;				//Order status
	
	//Order Constructor
	public Order(int n, int b, int f, int c) {
		number = n;
		burgers = b;
		fries = f;
		coke = c;
		isComplete = false;
	}
	
	//Checking the status of order
	public synchronized void isDone() throws InterruptedException {
		while(!isComplete) {
			wait();
		}
	}
	
	//Marking order as complete
	public synchronized void markDone() {
		isComplete = true;
		notify();
	}
}
