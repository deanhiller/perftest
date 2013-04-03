package com.perftest;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {

	private String name;
	private int count = 0;
	public MyThreadFactory(String string) {
		this.name= string;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setName(name+nextCount());
		t.setDaemon(true);
		return t;
	}

	private int nextCount() {
		return count++;
	}

}
