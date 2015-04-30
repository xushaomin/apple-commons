package com.appleframework.commons.pool.thread;

import java.util.*;

import org.apache.log4j.Logger;


public class CommonTaskManager {

	private static Logger logger = Logger.getLogger(CommonTaskManager.class);
	
	public static CommonTaskManager instance = null;
	private Map<String, CommonQueue> queueMap = new HashMap<String, CommonQueue>();

	class TaskCheck extends Thread {
		
		@Override
		public synchronized void run(){
			try{
				while( true ){					
					for (Map.Entry<String, CommonQueue> entry : queueMap.entrySet()) {
						String key = (String) entry.getKey();
						CommonQueue queue = (CommonQueue) queueMap.get(key);
						logger.info("===key:" + key + "  size:" + queue.getSize());
						if (queue.getSize() > 0) {
							// 啟動一个执行任務的线程
							logger.info(" queue size:" + queue.getSize());
							CommonThread thread = CommonThreadPool.getInstance().getThread(queue);
							if (thread != null) {
								// 激活线程
								logger.info("-------active thread to run------id:" + thread.getId());
								thread.doNotify();
							}
						}
					}
					logger.info("------task threard check run----");
					this.wait(1000);
				}
				
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
	private CommonTaskManager(){
		TaskCheck tCheck = new TaskCheck();
		tCheck.start();
	}
	
    public static synchronized CommonTaskManager getInstance(){
    	if( instance == null ){
    		instance = new CommonTaskManager();
    	}
    	return instance;
    }
    
	/**
	 * 接受委托过来的需要处理的任务
	 * @param task
	 */
	public synchronized void addTask(CommonTask task){
		String taskClassName = task.getClass().getName();
		logger.info("------add task class name: " + taskClassName);
		CommonQueue queue = (CommonQueue)queueMap.get(taskClassName);
		if(null == queue){
			queue = new CommonQueue();
			queue.setQueueName(taskClassName);
			queue.setThreadCnt(task.getThreadCnt());
			queueMap.put(taskClassName, queue);
		}
		queue.add( task );
	}
	/**
	 * 创建执行任务的线程
	 * @param task
	 */
    public void createTaskThread(Object task){
    	
    }
}
