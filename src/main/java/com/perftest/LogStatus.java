package com.perftest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogStatus implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(LogStatus.class);
	@Inject
	private Blackboard blackboard;
	
	@Override
	public void run() {
		int numComplete = blackboard.getNumFilesComplete();
		log.info("num files complete="+numComplete+" of total="+blackboard.getTotal()+" percent="+(100*numComplete/blackboard.getTotal()));
	}

}
