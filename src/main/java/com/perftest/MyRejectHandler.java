package com.perftest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyRejectHandler implements RejectedExecutionHandler {

	private static final Logger log = Logger.getLogger(MyRejectHandler.class.getName());
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		BlockingQueue<Runnable> queue = executor.getQueue();
		
		long time = System.currentTimeMillis();
		try {
			queue.put(r);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		long total = System.currentTimeMillis() - time;

		if(total > 30000) {
			log.log(Level.WARNING, "Downstream not keeping up.  We waited to add to queue time="+total);
		}
	}

}
