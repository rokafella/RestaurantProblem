package com.rohit.restaurant;

public class Diner implements Runnable {
	
	Thread t;							//Diner thread
	int number;							//Diner number
	Resources resource;					//Shared resource object
	Order order;						//Order object of the diner
	
	//Constructor for Diner
	public Diner(int n, Order o, Resources r) {
		number = n;
		order = o;
		resource = r;
	}
	
	public void run() {
		int table;
		try {
			//Compete with other threads to get the table
			table = resource.getTable();
			
			System.out.println("Diner "+this.number+" is seated at Table "+table+" at time "+resource.currentTime);
		
			//Compete with other threads to get the Cook
			resource.getCook();
			
			//Place the order in queue
			resource.putOrder(order);
			
			//Wait for Cook to process the order
			order.isDone();
			
			//Release the Cook after getting order
			resource.leaveCook();
			
			System.out.println("Food is brought to Diner "+this.number+" at time "+resource.currentTime+", and Diner "+this.number+" is now eating");
			
			//Eat for for 30 minutes
			Thread.sleep(30000);
			
			System.out.println("Diner "+this.number+" is leaving at "+resource.currentTime);
			
			//Leave the table after eating food
			resource.leaveTable(table);
			
			//Exit the restaurant
			resource.removeDiners();
			
			//If this is the last diner then exit the simulation
			if(resource.lastDiner()) {
				System.out.println("Last Diner left the restaurant at "+resource.currentTime);
				System.exit(0);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	//Start the thread
	public void start() {
		if (t == null){
			t = new Thread (this);
			t.start ();
	    }
	}
}
