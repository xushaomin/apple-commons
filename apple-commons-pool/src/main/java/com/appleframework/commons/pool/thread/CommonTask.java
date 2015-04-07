package com.appleframework.commons.pool.thread;

public interface CommonTask {
	
	public void doTask();

	public int getThreadCnt();

	public void setThreadCnt(int threadCnt);
	
}
