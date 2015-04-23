package com.rohit.restaurant;

public class Cook implements Runnable {
	
	Thread t;							//Cook thread
	Resources resource;					//Shared resource object
	int number;							//Cook number
	
	//Constructor for Cook
	public Cook(int n, Resources r) {
		number = n;
		resource = r;
	}
	
	public void run() {
		while(true) {
			try {
				//Wait for an Order and take Order from queue when available
				Order o = resource.takeOrder();
				
				System.out.println("Cook "+number+" is now serving Diner "+o.number);
				
				//Set busy flag as true and now compete for machines
				boolean busy = true;
				while(busy) {
					
					//If there are burger left in the order
					if(o.burgers > 0) {
						//Check if the machine is available
						if(resource.getBurgerMachine()) {
							System.out.println("Burger machine in use for Diner "+o.number+" at "+ resource.currentTime);
							//Make burger using the machine
							Thread.sleep(o.burgers*5000);
							//Free the machine
							resource.freeBurgerMachine();
							//Change the burger left in order to 0
							o.burgers = 0;
						}
					}
					
					//If there are fries left in the order
					if(o.fries > 0) {
						//Check if the machine is available
						if(resource.getFriesMachine()) {
							System.out.println("Fries machine in use for Diner "+o.number+" at "+ resource.currentTime);
							//Make fries using the machine
							Thread.sleep(o.fries*3000);
							//Free the machine
							resource.freeFriesMachine();
							//Change the fries left in order to 0
							o.fries = 0;
						}
					}
					
					//If there is coke left in the order
					if(o.coke > 0) {
						//Check if the machine is available
						if(resource.getSodaMachine()) {
							System.out.println("Coke machine in use for Diner "+o.number+" at "+ resource.currentTime);
							//Fill coke using the machine
							Thread.sleep(o.coke*1000);
							//Free the machine
							resource.freeSodaMachine();
							//Change the coke left in order to 0
							o.coke = 0;
						}
					}
					
					//If nothing is left in the order mark the order as done and make Cook free
					if((o.burgers==0)&&(o.fries==0)&&(o.coke==0)) {
						o.markDone();
						busy = false;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
