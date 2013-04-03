package com.perftest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReadWorker implements Worker {

	private static final Logger log = LoggerFactory.getLogger(FileReadWorker.class);
	@Inject
	private Blackboard blackboard;
	
	private int number;
	private Socket socket;
	private Random r;
	
	private void runImpl() throws IOException {
		while(true) {
			int firstDir = r.nextInt(1000);
			int secondDir = r.nextInt(1000);
			int thirdDir = r.nextInt(800);
			
			String file = blackboard.fetchNextFile(firstDir, secondDir, thirdDir);
			if(file == null)
				return; //we are done
			File f = new File(file);
			FileInputStream in = new FileInputStream(f);
			
			long fileLen = f.length();
			if(fileLen != 1024)
				throw new IllegalArgumentException("what, the file lenght is not 1024");
			byte[] data = new byte[(int) fileLen];
			
			in.read(data);
			
			OutputStream out = socket.getOutputStream();
			out.write(data);
		}
	}

	@Override
	public void run() {
		try {
			runImpl();
		} catch (IOException e) {
			log.warn("Exception running runnable", e);
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public void setNumber(int i) {
		this.number = i;
		r = new Random(System.currentTimeMillis()+10000*number);
	}
}
