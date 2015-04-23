package com.rohit.restaurant;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* Resources class manages all the shared resources in between threads
 * and also provides synchronization in between threads
 */
public class Resources {
	int tables;										//Total number of tables available
	LinkedList<Integer> tableSet;					//Set of all table numbers
	int cooks;										//Total number of cooks available
	int diners;										//Total number of diners left
	int currentTime;								//Current time of the Restaurant
	boolean burgerMachineFree;						//Lock for burger machine
	boolean friesMachineFree;						//Lock for fries machine
	boolean sodaMachineFree;						//Lock for soda machine
	BlockingQueue<Order> pendingOrders;				//Queue of pending orders
	
	//Resources constructor
	public Resources(int t, int c, int d) {
		tables = t;
		tableSet = new LinkedList<>();
		for(int i=1; i<=tables; i++) {
			tableSet.add(i);
		}
		cooks = c;
		diners = d;
		currentTime = 0;
		burgerMachineFree = true;
		friesMachineFree = true;
		sodaMachineFree = true;
		pendingOrders = new LinkedBlockingQueue<Order>();
	}
	
	//Get lock on Table
	public synchronized int getTable() throws InterruptedException{
		while(tables < 1) {
			wait();
		}
		tables--;
		return tableSet.removeFirst();
	}
	
	//Release lock on Table
	public synchronized void leaveTable(int t) {
		tables++;
		tableSet.add(t);
		notifyAll();
	}
	
	//Check for last Diner
	public synchronized boolean lastDiner() {
		if(diners > 0) {
			return false;
		}
		return true;
	}
	
	//Update remaining Diners
	public synchronized void removeDiners() {
		diners--;
	}
	
	//Get lock on Cook
	public synchronized void getCook() throws InterruptedException {
		while(cooks < 1) {
			wait();
		}
	}
	
	//Release lock on Cook
	public synchronized void leaveCook() {
		cooks++;
		notifyAll();
	}
	
	//Place order in pending queue
	public synchronized void putOrder(Order o) {
		pendingOrders.offer(o);
	}

	//Take order from pending queue
	public Order takeOrder() throws InterruptedException {
		return pendingOrders.take();
	}
	
	//Get lock on Burger Machine
	public synchronized boolean getBurgerMachine() {
		if(burgerMachineFree) {
			burgerMachineFree = false;
			return true;
		}
		return false;
	}
	
	//Release lock on Burger Machine
	public synchronized void freeBurgerMachine() {
		burgerMachineFree = true;
	}
	
	//Get lock on Fries Machine
	public synchronized boolean getFriesMachine() {
		if(friesMachineFree) {
			friesMachineFree = false;
			return true;
		}
		return false;
	}
	
	//Release lock on Fries Machine
	public synchronized void freeFriesMachine() {
		friesMachineFree = true;
	}
	
	//Get lock on Soda Machine
	public synchronized boolean getSodaMachine() {
		if(sodaMachineFree) {
			sodaMachineFree = false;
			return true;
		}
		return false;
	}
	
	//Release lock on Soda Machine
	public synchronized void freeSodaMachine() {
		sodaMachineFree = true;
	}
}
