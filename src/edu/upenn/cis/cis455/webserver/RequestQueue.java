package edu.upenn.cis.cis455.webserver;

import java.util.LinkedList;

import org.apache.log4j.Logger;

public class RequestQueue {
	
	static Logger log = Logger.getLogger(RequestQueue.class);
	private LinkedList<HttpRequest> requestqueue; 
	private int limit;
	
	public RequestQueue(int limit) {
		this.limit = limit;
		this.requestqueue = new LinkedList<HttpRequest>();
	}
	
	public synchronized void enqueue(HttpRequest request){
		log.info("RequestQueue: start adding request");
		log.info("RequestQueue: queue size is "+requestqueue.size());
		log.info("RequestQueue: queue limit is "+limit);
		if(requestqueue.size()==limit){  
			try{
				wait();
			}catch(InterruptedException ee){
				log.info("requestqueue is full, cannot wait in 'enqueue'. ");
			}
			
		}
		
		if(requestqueue.isEmpty()){
			notifyAll();
		}
		log.info("RequestQueue: is adding request");
		this.requestqueue.add(request);
		
	}
	
	public synchronized HttpRequest dequeue(){
		if(requestqueue.size()==limit){
			notifyAll();
		}
		
		if(requestqueue.isEmpty()){
			try{
				wait();
			}catch(InterruptedException e){
				log.info("requestqueue is empty, cannot wait in 'dequeue'.");
			}
			
			
		}
		log.info("RequestQueue: start dequeue");
		HttpRequest res = requestqueue.poll();  /// one mistake: i used 'get' not 'remove' method to dequeue
		return res;
		
	}
	
	
	

}
