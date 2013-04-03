package com.perftest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerAcceptor implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ServerAcceptor.class);
	@Inject
	private Provider<FileReadWorker> workers;
	@Inject
	private Executor executor;
	private int count = 0;
	
	public void runImpl() throws IOException {
		ServerSocket s = new ServerSocket(8080);
		while(true) {
			Socket socket = s.accept();
			FileReadWorker w = workers.get();
			w.setNumber(count++);
			w.setSocket(socket);
			executor.execute(w);
		}
	}

	@Override
	public void run() {
		try {
			runImpl();
		} catch (IOException e) {
			log.warn("Exception in server accepting", e);
		}
	}

}
