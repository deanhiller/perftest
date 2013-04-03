package com.perftest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ProductionModule implements Module {

	private int numThreads;
	private boolean isClient;

	public ProductionModule(int numThreads, boolean isClient) {
		this.numThreads = numThreads;
		this.isClient = isClient;
	}

	@Override
	public void configure(Binder binder) {
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(1000);
		ThreadPoolExecutor exec = new ThreadPoolExecutor(numThreads, 10000, 0L, TimeUnit.MILLISECONDS,
                queue, new MyThreadFactory("server"), new MyRejectHandler());
		binder.bind(Executor.class).toInstance(exec);
		
		ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
		binder.bind(ScheduledExecutorService.class).toInstance(scheduled);
	
		if(isClient) 
			binder.bind(Worker.class).to(FileReadWorker.class);
		else
			binder.bind(Worker.class).to(FileWriteWorker.class);
	}

}
