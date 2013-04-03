package com.perftest;

import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Server {

	private static final Logger log = LoggerFactory.getLogger(Server.class);
	@Inject
	private Executor executor;
	@Inject
	private Provider<Worker> workers;
	@Inject
	private ScheduledExecutorService scheduler;
	@Inject
	private LogStatus logStatus;
	@Inject
	private Blackboard blackboard;
	@Inject
	private ServerAcceptor server;
	
	public static void main(String[] args) throws UnknownHostException {
		if(args.length < 1)
			throw new IllegalArgumentException("Need to supply num threads as first argument");
		else if(args.length < 2)
			throw new IllegalArgumentException("Must run 'command <numThreads> <numReads>' but numReads is missing");
		int numThreads;
		try {
			numThreads = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("First argument must be integer of number of threads");
		}
		int numReads = 0;
		
		ProductionModule m = new ProductionModule(numThreads, true);
		Injector injector = Guice.createInjector(m);
		Server server = injector.getInstance(Server.class);
		server.start(numThreads, numReads);
	}

	public void start(int numThreads, int numReads) {
		blackboard.setTotal(numReads);
		scheduler.schedule(logStatus, 5, TimeUnit.SECONDS);

		executor.execute(server);

		log.info("we are done starting the server, now we wait");
	}
}
