package com.appleframework.commons.pool.thread;


import java.util.*;

import org.apache.log4j.Logger;


public class CommonThreadPool {
	
	private static Logger logger = Logger.getLogger(CommonThreadPool.class);

	//thread pool instance
	private static CommonThreadPool threadPool = null;

	//the mapping of proccessor to maxcount of processor instance
	private static List<CommonThread> freeList = new LinkedList<CommonThread>();

	//the mapping of proccessor name to proccessor map
	private static Map<String, Map<String, CommonThread>> busyMap = new Hashtable<String, Map<String, CommonThread>>();

	//constructor
	private CommonThreadPool() {
	}

	

	/**
	 * return the instance
	 * @return  thread pool instance
	 */
	public static synchronized CommonThreadPool getInstance() {
		if (threadPool == null) {
			threadPool = new CommonThreadPool();
		}
		return threadPool;
	}



	/**
	 * free thread
	 * @param thread which will be free
	 */
	public void freeThread(CommonThread thread) {
		try {
			String processName = thread.getName();
			String threadId = String.valueOf(thread.getId());
			//find the thread from busy map then free
			Map<String, CommonThread> threadMap = busyMap.get(processName);
			if (threadMap != null) {
				threadMap.remove(threadId);
				logger.info("ThreadId:" + threadId + " free a busy thread!");
			}
			thread.setName("");
			freeList.add( thread );
		} catch (Exception e) {
			logger.error("free thread exception : ", e);
		}
	}

	public void freeThreds(Map<String, CommonThread> threadMap){
		for (Map.Entry<String, CommonThread> entry : threadMap.entrySet()) {
			CommonThread thread = threadMap.get(entry.getKey());
			logger.info("thread :" + thread.getName() + " status is " + thread.isAlive());
			if (!thread.isAlive()) {
				freeThread(thread);
				thread.doNotify();
			}
		}  
    }
	
	/**
	 * get a thread from thread pool,if pool is null then create a new CommonThread
	 * @param ProcessName proccessor class name
	 * @return a CommonThread
	 * @throws Exception
	 */
	public CommonThread getThread(CommonQueue queue) throws Exception {
		try {
			logger.info("----------get thread begin-------------");
			Map<String, CommonThread> threadMap = busyMap.get( queue.getQueueName() );
			if( threadMap == null ){
				threadMap = new HashMap<String, CommonThread>();
				busyMap.put(queue.getQueueName(), threadMap);
			}
			if( threadMap.size() >= queue.getThreadCnt() ){
				logger.info("----------get thread end null-------------q:" 
						+ queue.getClass().getName() + " qname:" + queue.getQueueName() + " qsize:" 
						+ threadMap.size());
				freeThreds(threadMap);
				return null;
			}
			else{
				CommonThread thread = null;
				if( freeList.size() > 0 ){
					logger.info("----------get thread from free-------------");
					thread = (CommonThread)freeList.remove(0);
					//thread = (CommonThread)freeList.get(0);
				}else{
					logger.info("----------get thread from new-------------");
				    thread = new CommonThread();
				    thread.start();
				}
				thread.setName(queue.getQueueName());
				threadMap.put(String.valueOf(thread.getId()), thread);
				logger.info("----------get thread end-------------threadid:"+ thread.getId());
				thread.setQueue(queue);
				return thread;
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
			logger.error(e);
			return null;
		}

	}
}
