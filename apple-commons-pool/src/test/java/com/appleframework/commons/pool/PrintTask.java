package com.appleframework.commons.pool;

import com.appleframework.commons.pool.thread.CommonTask;

public class PrintTask implements CommonTask {

	private int threadCnt = 1;
	private IPrint task = null;

	public void doTask() {
		if (this.task != null) {
			this.task.doPrint();
		}
	}

	public int getThreadCnt() {
		return this.threadCnt;
	}

	public void setThreadCnt(int threadCnt) {
		this.threadCnt = threadCnt;
	}

	public IPrint getTask() {
		return task;
	}

	public void setTask(IPrint task) {
		this.task = task;
	}

}
