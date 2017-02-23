package edu.upenn.cis.cis455.webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.google.common.primitives.Bytes;

public class HttpRequest{
	
	static Logger log = Logger.getLogger(HttpRequest.class);
	private Socket acceptSocket;
	private String root;
	private HttpMetaData requestdata;
	private HashMap<String, ArrayList<String>> headmaps;
	private HttpResponse httpResponse;
	private BufferedReader in;
	private DataOutputStream out;
	private ServletContainer container;
	private HttpServlet servlet = null;
	
//	private static HttpResponse response;
	public HttpRequest(Socket acceptSocket,String root,ServletContainer container) throws Exception {
			this.acceptSocket = acceptSocket;
			this.root = root;
			this.container = container;
	}
	
	
	/*
	 * process request by using this method;
	 */
	public void doRun() throws Exception{
		this.in = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream(),"UTF8"));
		
		this.out = new DataOutputStream(this.acceptSocket.getOutputStream()); 
		this.requestdata = new HttpMetaData(acceptSocket,in,out,this.root);
		this.headmaps= new HashMap<String,ArrayList<String>>();
	
		
		
	
		try{
//			log.info("HttpRequest: start parse request");
			parseRequestFirstline(this.in);
			
			
			parseHearders(in);
			log.info("HttpRequest: processing headers ");
			
			parseMessageBody(in);
			
//			
//			try{
//				
//				servlet = container.servletNameMatching(requestdata);
//				log.info("HttpRequest: get servlet "+ servlet.getServletName());
//			}catch(Exception e){
//				e.printStackTrace();
//				log.info("HttpRequest:  cannot get servlet");
//			}
			
			
			
			if(!requestdata.getFilePath().equals("/favicon.ico")){
				if((servlet= container.servletNameMatching(requestdata))!=null){
					
					HTTPServletRequest servletrequest = new HTTPServletRequest(requestdata,container);
					HTTPServletResponse servletresponse = new HTTPServletResponse(requestdata,servletrequest);
					try{
						log.info("HttpRequest: processing a servlet request ");
						try{
							
							servlet.service(servletrequest, servletresponse);
							
						}catch(Exception e){
							e.printStackTrace();
							log.info("HttpRequest: cannot service servlet");
						}
						
						if(servletresponse.isCommitted()){
							servletresponse.clearBuffer();
							servletresponse.getWriter().close();
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
				}else {
					log.info("HttpRequest: processing a general request ");
					this.httpResponse = new HttpResponse(this.requestdata);
					this.httpResponse.generateResponse(requestdata);
					
				}			
			}
			in.close();
			out.close();
			acceptSocket.close();
//			in.close();
		}catch(Exception e){
			e.printStackTrace();
			log.info("HttpRequest: Cannot generateResponse");
		}
		
		
		
	}

	/*
	 * read the first line of socket info, which is the request from client
	 */
	public void parseRequestFirstline(BufferedReader in) throws Exception{
			log.info("HttpRequest: start read");
			String line = in.readLine();
			log.info("HttpRequest: request line is "+line);
			while(line.length()==0){
					line = in.readLine();
			}
			
			String[] list = line.split("\\s+");
			if(list.length!=3){
				log.info("HttpRequest: request line is not equal 3");
				requestdata.setBadRequest(true);
				return;
			}
			
			
			requestdata.setMethod(list[0]);
			String pathandquery = list[1].trim();
			String[] pnq = pathandquery.split("\\?");
		//set query string
			if(pnq.length>1){
				requestdata.setQueryString(pnq[1].trim());
			}else{
				requestdata.setQueryString("");
			}
			
		//set method
			if(list[0].trim().equals("GET") ||list[0].trim().equals("HEAD")||list[0].trim().equals("POST")){
				log.info("HttpRequest: set badversion good");
				requestdata.setBadMethod(false);
				requestdata.setMethod(list[0].trim());
				if(list[0].trim().equals("GET") && pnq[0].trim().equals("/shutdown")){
					log.info("HttpRequest: setting shutdown");
					requestdata.setShutDown(true);
					requestdata.setFilePath("/shutdown");
					
				}
				
				else if(list[0].trim().equals("GET") && pnq[0].trim().equals("/control")){
					log.info("HttpRequest: setting control");
					requestdata.setControl(true);
					requestdata.setFilePath("/control");
					
				}
				
				else if(list[0].trim().equals("GET") && pnq[0].trim().equals("/")){
					log.info("HttpResponse: setting index page");
					requestdata.setFilePath("/");
					
				}
				else{
					requestdata.setFilePath(pnq[0].trim());
					requestdata.setBadRequest(false);
				}
				
			}else{
				requestdata.setBadMethod(true);
				
			}
		
			if(list[2].trim().equals("HTTP/1.1") || list[2].trim().equals("HTTP/1.0")){
				log.info("HttpRequest: setting good version");
				requestdata.setBadVersion(false);
				requestdata.setVersion(list[2].trim()); // HTTP/1.1
				if(list[2].trim().equals("HTTP/1.1")){
					log.info("HttpRequest: setting version 1.1");
					requestdata.setVersion1(true);      // is HTTP/1.1
				}else{
					requestdata.setVersion1(false);
				}
			}else{
				requestdata.setBadVersion(true);
				
			}
		
		
				
	}
	
	public void parseHearders(BufferedReader in) throws IOException{
		String line = "";
		log.info("HttpRequest: start parsing headers");
		
		boolean flag = false;
		
		while(!flag){
//			try{
			line = in.readLine();
//			}catch(Exception e){
//				e.printStackTrace();
//				log.info("...........");
//			}
			
			log.info("HttpRequest: "+ line);
			String[] list = line.split(":\\s*");
			if(list.length>1){
				log.info("HttpRequest: in if ");
				String va0 = list[0].trim();
				String va1 = list[1].trim();
				if(!headmaps.containsKey(va0)){
					headmaps.put(va0, new ArrayList<String>());
				}
				headmaps.get(va0).add(va1);
			}else{
				flag = true;
//				System.out.println(line);
			}
			
			requestdata.setHeadersMap(headmaps);
						
		}
		log.info("HttpRequest: out of loop");
		if(requestdata.isVersion1()){
			if(!headmaps.keySet().contains("Host")){
				log.info("HttpRequest: set badrequest for host");
				requestdata.setBadRequest(true);
			}
		}
						
	}
	
	
	public void parseMessageBody(BufferedReader in) throws IOException{ //read message body in bytes
		log.info("HttpRequest: parsing body");
		if(requestdata.getHeadersMap().get("Content-Length")!=null){
			int numberOfBytes =Integer.parseInt(requestdata.getHeadersMap().get("Content-Length").get(0));
			char[] bytes = new char[numberOfBytes];
			int bytesread = in.read(bytes, 0, numberOfBytes);
			if(numberOfBytes != bytesread){
				log.info("Didn't read number of bytes specified by Content Length");
			}
			requestdata.setMessageBody(new String(bytes));
		}
		
		
	}
	
	
	
}
