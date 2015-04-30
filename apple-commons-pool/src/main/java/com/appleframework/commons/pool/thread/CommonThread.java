package com.appleframework.commons.pool.thread;

import java.lang.Thread;

import org.apache.log4j.Logger;

public class CommonThread extends Thread {
	
	private static Logger logger = Logger.getLogger(CommonThread.class);
	
	private CommonQueue queue;

	@Override
	public void run() {
		while (true) {
			try {
				doWait();
				logger.info(" que size:" + queue.getSize() );
				while (queue.getSize() > 0) {
					logger.info("----------do task -----------");
					CommonTask task = (CommonTask) queue.removeFirst();
					if (null != task) {
						task.doTask();
					}
				}
				CommonThreadPool.getInstance().freeThread(this);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public synchronized void doWait() {
		try {
			this.wait(10*1000);
			logger.info("----------wake up -----------");
		} catch (IllegalMonitorStateException ie) {
			logger.error(ie.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public synchronized void doNotify() {
		try {
			this.notify();
		} catch (IllegalMonitorStateException ie) {
			logger.error(ie.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public CommonQueue getQueue() {
		return queue;
	}

	public void setQueue(CommonQueue queue) {
		this.queue = queue;
	}

}
