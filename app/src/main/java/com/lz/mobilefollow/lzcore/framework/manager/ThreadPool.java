package com.lz.mobilefollow.lzcore.framework.manager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * 线程池 <br>
 * 管理thread开启和销毁
 * @see Executors
 * @author lkk
 */
public class ThreadPool {

	private static ExecutorService service = Executors.newCachedThreadPool();
	
	/**
	 * 执行 异步任务
	 * @param task
	 */
	public static void execute(Runnable task){
		service.execute(task);
	}
	
	/**
	 * ִCallable
	 * @param <T>
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submit(Callable<T> task){
		return (Future<T>) service.submit(task);
	}
	
	/**
	 * 
	 */
	public static void shutDownNow(){
		service.shutdownNow();
	}
	
}