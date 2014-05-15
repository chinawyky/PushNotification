package com.telenav.cserver.resource.manager;

import java.util.concurrent.ArrayBlockingQueue;

public class UserDataManager {
	
	//several threads loop and get one item by each thread.
	//each thread will return util the item has been successfully processed.
	private volatile static ArrayBlockingQueue<String[]> userDataArrayOrigin;

	//Buffer between UMS and push service
	//after previous UMS thread returns, one item with userid <--> registration_id will be inserted into this array
	private volatile static ArrayBlockingQueue<String[]> userDataArray;
	
	
	public static ArrayBlockingQueue<String[]> getUserDataArray() {
		return userDataArray;
	}
	public static void setUserDataArray(ArrayBlockingQueue<String[]> userDataArray) {
		UserDataManager.userDataArray = userDataArray;
	}
	public static void setUserDataArrayOrigin(ArrayBlockingQueue<String[]> userDataArrayOrigin) {
		UserDataManager.userDataArrayOrigin = userDataArrayOrigin;
	}	
	public static ArrayBlockingQueue<String[]> getUserDataArrayOrigin()
	{
		return userDataArrayOrigin;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
