package edu.upenn.cis.cis455.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketServer;

public class HttpServer {

	/**
	 * Logger for this particular class
	 */
	static Logger log = Logger.getLogger(HttpServer.class);

	private static boolean isShutDown;
	private static ThreadPool threadpool;
	private static int port;
	private static String root;
	private static ServerSocket listenSocket;
	private static Socket echoSocket;
	private static Context context;
	private static Handler handler;
	private static ServletContainer container;
	
	public static void main(String args[]) throws Exception
	{
		log.info("Start of Http Server");
		port = Integer.parseInt(args[0]);
		root = args[1];
		threadpool = new ThreadPool(10,10);
		
		handler = new Handler();
		container = new ServletContainer(handler);
		
		
		listenSocket = new ServerSocket(port);
		
		while(!isShutDown){ ///use'while', then server will always be running.
			
			try{
				
				HttpRequest re = new HttpRequest(listenSocket.accept(),root,container);
				try{
					log.info("HttpServer: Adding request");
					threadpool.addRequest(re);

				}catch(Exception e){
					e.printStackTrace();
					log.info("HttpServer: Could not add request.");
				}
				
				
				
			}catch(IOException io){
				 log.info("Could not read from socket. Either IOError or shutdown request.");
				
				
				
			}
			
			
			
			
			
		}
		
		
		
		
		
		log.info("Http Server terminating");
	}
	
	
	
	
	public void shutdownServer(){
		this.isShutDown = true;
		this.threadpool.shutdown();
				
	}
	
	public static Context getContext(){
		return context;
	}

}
