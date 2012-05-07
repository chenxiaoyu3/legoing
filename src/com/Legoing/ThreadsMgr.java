package com.Legoing;

import java.util.LinkedList;

import android.os.Message;
import android.text.format.Time;
import android.util.Log;

class ControlledThread extends Thread
{
	Runnable toRun;
	Time begTime;
	public ControlledThread(Runnable toRun) {
		// TODO , Cxy, 2011-11-4 ����4:59:38
		this.toRun = toRun;
	}
	@Override
	public void run() {
		// TODO , Chen Xiaoyu Cxy, 2011-11-4 ����5:01:27
		ThreadsMgr.addThread(this);
		begTime = new Time();
		begTime.setToNow();
		try {
			toRun.run();
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("Thread", "Thread aborted with Exception: " + e.getStackTrace()[0].getMethodName());
			Message msg = new Message();
			msg.what = StaticOverall.MSG_THREAD_ABORT;
			msg.obj = e;
			StaticOverall.getHandlerOverall().sendMessage(msg);
		}
		
		ThreadsMgr.removeThread(this);
		
	}
	public Time getBegTime() {
		return begTime;
	}
}
public class ThreadsMgr 
{
	static LinkedList<Thread> allThreads = new LinkedList<Thread>();
	public static Thread getThreadToRun(Runnable toRun,String name)
	{
		ControlledThread thread = new ControlledThread(toRun);
		thread.setName(name);
		return thread;
	}
	
	public static void addThread(Thread thread)
	{
		allThreads.add(thread);
	}
	public static boolean removeThread(Thread thread)
	{
		return allThreads.remove(thread);
	}
	/**
	 * ?? how to clear? cxy 2012/1/3
	 */
	public static void abortAllThreads()
	{
		for (Thread thread : allThreads) {
			if (thread.isAlive()) {
				thread = null;
			}
		}
	}

}
