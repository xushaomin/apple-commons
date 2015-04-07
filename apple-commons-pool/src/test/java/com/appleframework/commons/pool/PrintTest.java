package com.appleframework.commons.pool;

import com.appleframework.commons.pool.thread.CommonTaskManager;


public class PrintTest {
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 10000; i++) {
			PrintTask task = new PrintTask();			
			IPrint print = new PrintImpl();
			print.setMessage(i + "");
			task.setTask(print);
			CommonTaskManager.getInstance().addTask(task);
			
		}
	}
}