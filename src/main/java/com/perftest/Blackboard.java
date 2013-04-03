package com.perftest;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blackboard {

	private static final Logger log = LoggerFactory.getLogger(Blackboard.class);
	private Set<String> filesInProcess = new HashSet<String>();
	private int total;
	private int numFilesComplete;

	public int getNumFilesComplete() {
		return numFilesComplete;
	}

	public void setTotal(int numReads) {
		this.total = numReads;
	}

	public int getTotal() {
		return total;
	}

	public synchronized String fetchNextFile(int firstDir, int secondDir, int thirdDir) {
		numFilesComplete++;
		String file = formFilename(firstDir, secondDir, thirdDir);
		if(filesInProcess.contains(file)) {
			log.info("conflict, someone already processing file="+file+" so increment until we find one");
			firstDir = 0;
			for(int i = 0; i < 1000; i++) {
				file = formFilename(i, secondDir, thirdDir);
				if(!filesInProcess.contains(file))
					return file;
				else
					log.info("what, conflict number "+(i+2)+" incrementing and trying again file="+file);
			}
		}
		return file;
	}

	private String formFilename(int firstDir, int secondDir, int thirdDir) {
		String file = firstDir+"/"+secondDir+"/"+thirdDir;
		return file;
	}

}
