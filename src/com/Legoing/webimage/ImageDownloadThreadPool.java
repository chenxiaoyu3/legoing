package com.Legoing.webimage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//TODO Some Parameter tunes
public class ImageDownloadThreadPool {
	private static final int CORE_POOL_SIZE = 2;
	private static final int MAX_POOL_SIZE = 5;
	private static final int KEEP_ALIVE_TIME = 2;//SECONDS
	private static final int QUEUE_SIZE = 200;
	private static final ThreadPoolExecutor mExcutor = 
			new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, 
					new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),  
					Executors.defaultThreadFactory(),
					new ThreadPoolExecutor.DiscardOldestPolicy());
	
	public static void execute(Runnable runnabe){
		mExcutor.execute(runnabe);
	}
}
