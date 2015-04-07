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
					/*
					Iterator<?> it = queueMap.keySet().iterator();
					while( it.hasNext() ){
						String key = (String)it.next();
						
						CommonQueue q = (CommonQueue) queueMap.get( key );
						logger.info("===key:" + key + "  size:" + q.getSize());
						if( q.getSize() > 0 ){
							//啟動一个执行任務的线程
							logger.info(" que size:" + q.getSize() );
							CommonThread thread = CommonThreadPool.getInstance().getThread( q );
							if( thread != null ){
								//激活线程
								logger.info("-------active thread to run------id:" + thread.getId());
								thread.doNotify();
							}
						}
					}
					logger.info("------task threard check run----");
					this.wait(1000);
					*/
					
					for (Map.Entry<String, CommonQueue> entry : queueMap.entrySet()) {
						String key = (String) entry.getKey();
						CommonQueue q = (CommonQueue) queueMap.get(key);
						logger.info("===key:" + key + "  size:" + q.getSize());
						if (q.getSize() > 0) {
							// 啟動一个执行任務的线程
							logger.info(" que size:" + q.getSize());
							CommonThread thread = CommonThreadPool.getInstance().getThread(q);
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
		CommonQueue q = (CommonQueue)queueMap.get( task.getClass().getName() );
		if( q == null ){
			q = new CommonQueue();
			q.setQueueName(task.getClass().getName());
			q.setThreadCnt( task.getThreadCnt() );
			queueMap.put(task.getClass().getName(), q);
		}
		q.add( task );
		/*
		//如果取到线程就立即执行，否则扔到队列
		try{
			SheThread thread = ThreadPool.getInstance().getThread( task );
			if( thread == null){
				logger.info("--------get thread null ----");
				q.add( task );
			}
			else{
				logger.info("--------get thread ok ----");
				thread.setTask( task );
				thread.setQueue(q);
				//thread.start();
				//thread.notify();
			}
		}
		catch( Exception e ){
			q.add( task );
		}
		*/
	}
	/**
	 * 创建执行任务的线程
	 * @param task
	 */
    public void createTaskThread(Object task){
    	
    }
}
