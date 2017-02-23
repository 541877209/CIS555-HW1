package edu.upenn.cis.cis455.webserver;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class ThreadPool {

	static Logger log = Logger.getLogger(ThreadPool.class);

	private static ArrayList<HttpWorker> threadpool = new ArrayList<HttpWorker>();
	private RequestQueue request;
	private int numberofthread;
	private int queuebound;
	private static boolean isShutDown;
	
	
	
	public ThreadPool(int queuebound,int numberofthread) {
		this.request = new RequestQueue(queuebound);
		this.queuebound = queuebound;
		this.numberofthread = numberofthread;
		
		int i=0;
		while(i < this.numberofthread){
			HttpWorker thread = new HttpWorker(this.request);
			threadpool.add(thread);	
			i++;
		}
		
//		log.info(threadpool.size());
		
		for(HttpWorker worker : threadpool){
			worker.start();
		}
		log.info("ThreadPool: finish start worker");		
	}
	
	
	public static void shutdown(){
		
		for(HttpWorker worker : threadpool){
			try{
				isShutDown = true;
				worker.shutdown();
				log.info("ThreadPool: shut down");
			}catch(Exception e){
				log.info("Fail to shut down.");
			}
			
			//TODO: set metadata as shutdown
			//TODO: connection: close; 
		}
		
	}
	
	
	public static HashMap<String,String> getThreadStatus(){
		HashMap<String,String> threadmap = new HashMap<String,String>();
		for(Thread thread : threadpool){
			
			threadmap.put(thread.getName(), thread.getState().toString());
			
			
		}
		
		return threadmap;
		
	}
	
	
	public void addRequest(HttpRequest re){
		try{
			log.info("ThreadPool: is adding request");
			this.request.enqueue(re);
		}catch(Exception e){
			
			log.info("Fail to enqueue request.");
		}
		
		
	}
	

}
