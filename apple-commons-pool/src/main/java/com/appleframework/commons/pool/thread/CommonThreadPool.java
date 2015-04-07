package com.appleframework.commons.pool.thread;


import java.util.*;

import org.apache.log4j.Logger;


@SuppressWarnings("rawtypes")
public class CommonThreadPool {
	
	private static Logger logger = Logger.getLogger(CommonThreadPool.class);

	//thread pool instance
	private static CommonThreadPool threadPool = null;

	//the mapping of proccessor to maxcount of processor instance
	private static List<CommonThread> freeList = new LinkedList<CommonThread>();

	//the mapping of proccessor name to proccessor map
	private static Map<String, Map> busyMap = new Hashtable<String, Map>();

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
			String threadId = "" + thread.getId();
			//find the thread from busy map then free
			Map threadMap = (Map) busyMap.get(processName);
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
    	/*Iterator<?> it = threadMap.keySet().iterator();
    	logger.info("Threadmap:free a busy thread group!");
		while (it.hasNext()) {
    		CommonThread thread = threadMap.get(it.next());
    		logger.info("--------thread---t status:" + thread.isAlive());
    		if( !thread.isAlive() ){
    			freeThread(thread);
    			thread.doNotify();
    		}
    	}*/
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
	 * get a thread from thread pool,if pool is null then create a new gwthread
	 * @param ProcessName proccessor class name
	 * @return a gwthread
	 * @throws GwException
	 */
	@SuppressWarnings("unchecked")
	public CommonThread getThread(CommonQueue q) throws Exception {
		try {
			logger.info("----------get thread begin-------------");
			Map threadMap = (Map) busyMap.get( q.getQueueName() );
			if( threadMap == null ){
				threadMap = new HashMap();
				busyMap.put(q.getQueueName(), threadMap);
			}
			if( threadMap.size() >= q.getThreadCnt() ){
				logger.info("----------get thread end null-------------q:" + q.getClass().getName() + " qname:" + q.getQueueName() + " qsize:" + threadMap.size()  );
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
				thread.setName(q.getQueueName());
				threadMap.put(""+thread.getId(), thread );
				logger.info("----------get thread end-------------threadid:"+ thread.getId());
				thread.setQueue(q);
				return thread;
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
			logger.error(e);
			return null;
		}

	}
}
