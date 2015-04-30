package com.appleframework.commons.pool;

import com.appleframework.commons.pool.thread.CommonTaskManager;


public class PrintTest {
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 100000000; i++) {
			IPrint print = new PrintImpl();
			print.setMessage(i + "");
			
			PrintTask task = new PrintTask();
			task.setTask(print);
			task.setThreadCnt(5);
			CommonTaskManager.getInstance().addTask(task);
			
			DemoTask task1 = new DemoTask();
			task1.setTask(print);
			task1.setThreadCnt(5);
			CommonTaskManager.getInstance().addTask(task1);
		}
	}
}