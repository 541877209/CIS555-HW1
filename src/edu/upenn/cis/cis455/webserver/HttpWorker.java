package edu.upenn.cis.cis455.webserver;

import org.apache.log4j.Logger;

public class HttpWorker extends Thread{

	static Logger log = Logger.getLogger(HttpWorker.class);
	
	private RequestQueue queue;
	private boolean isShutDown = false;
	
	public HttpWorker(RequestQueue queue) {
		this.queue = queue;
	}
	
	
	//TODO: shutdown
	
	public void shutdown(){
		
		this.isShutDown = true;
		interrupt();
		log.info("HttpWorker: shut down a worker");
		
	}
	
	public void run(){
		log.info("HttpWorker: running a worker");
		while(!isShutDown){
			
			try{
				
				HttpRequest r = queue.dequeue();
				if(r == null) continue;
				log.info("HttpWorker: dequeued a requestqueue");
				r.doRun();
				
				
				
			}catch(Exception e){
				e.printStackTrace();
				log.info("cannot run request in HttpWorker.");
				
			}
			
		}
		
		
		
		
	}
	

}
