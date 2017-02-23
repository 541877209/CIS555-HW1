package edu.upenn.cis.cis455.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import java.io.DataOutputStream;
import org.apache.log4j.Logger;



public class HttpResponse {
	static Logger log = Logger.getLogger(HttpResponse.class);
	public HttpMetaData requestdata;
	public HttpResponse(HttpMetaData requestdata) {
		this.requestdata = requestdata;		
	}
	
	
	
	
	public void generateResponse(HttpMetaData requestdata) throws IOException, TikaException{
		DataOutputStream out = requestdata.getOutputStream();
		BufferedReader in = requestdata.getInStream();
		
		//create StringBuilder
		StringBuilder response_first = new StringBuilder();
		StringBuilder response_head = new StringBuilder();
		StringBuilder response_body = new StringBuilder();
		
		// start append first line
		if(requestdata.isVersion1()){
			response_first.append(HttpConstant.HTTP_ONE_DOT_ONE);
		}else{
			response_first.append(HttpConstant.HTTP_ONE_DOT_ZERO);
		}
		
		//start set headers
		HashMap<String,String> headers = new HashMap<>();
		Date date = new Date();
		String datestring = HttpConstant.parseDate((java.util.Date) date); // get date of response, and set GMT as time zone;
		headers.put(HttpConstant.DATE_HEADER,datestring);
		
		//set content type
		String content_type = null;
		
		//set file
		File file = null;
		
		if(requestdata.isBadRequest()){
			
			response_first.append(" " + HttpConstant.HTTP_BAD_REQUEST);
			response_body.append(HttpConstant.HTTP_BAD_REQUEST_BODY);				
			content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
			
		}else if(requestdata.isBadMethod()){
			
			response_first.append(" " + HttpConstant.HTTP_METHOD_NOT_ALLOWED);
			response_body.append(HttpConstant.HTTP_BAD_METHOD_BODY);			
			content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
			
		}else if(requestdata.isBadVersion()){
			
			response_first.append(" " +HttpConstant.HTTP_BAD_VERSION);
			response_body.append(HttpConstant.HTTP_BAD_VERSION_BODY);			
			content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
			
		}else if(requestdata.isShutDown()){
			
			//TODO: SHUTDOWN server
			
			response_first.append(" " +HttpConstant.HTTP_GOOD_REQUEST);
			response_body.append(HttpConstant.HTTP_SHUTDOWN);			
			content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
			
		}else if(requestdata.isControl()){
			//TODO:
			HashMap<String, String> threadmap = ThreadPool.getThreadStatus();
			
			
			
			response_first.append(" " +HttpConstant.HTTP_GOOD_REQUEST);
			response_body.append(HttpConstant.HTTP_CONTROL);
			content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
			Set<String> threadset = threadmap.keySet(); // get all
			for(String t : threadset){
				
				response_body.append("<tr>"+
															"<th>"+t+"</th>"
															+"<th>"+threadmap.get(t)+"</th>"
															+"</tr>"
															+"</body>"
															+HttpConstant.HTTP_RESPONSE_END);
				
				
			}
			
		}else{ // get file
					response_first.append(" " +HttpConstant.HTTP_GOOD_REQUEST);
					
					
					
					
					String filepath = requestdata.getFilePath();
					
					////######################
					log.info("HttpResponse: finding path");
					file = new File(filepath);
					
					
					String canonical_dir = file.getCanonicalPath();
					if(canonical_dir.equals("/")){
						log.info("HttpResponse: finding index page");
						canonical_dir = "www/index.html";					
					}
					file = new File(canonical_dir); 
					log.info(file.getCanonicalPath());
					
					if(file.isFile()){
						Tika tika = new Tika();						
						try{
							content_type = tika.detect(file);
						}catch(IOException e){
							content_type = HttpConstant.DEFAULT_CONTENT_TYPE;
							log.info("HttpResponse: Use DEFAULT_CONTENT_TYPE");
						}
												
						//file will be sent back in binary not in string.
						
					}
			
				
		}
		//put content type to headers
		headers.put(HttpConstant.CONTENT_LENGTH_TYPE, content_type);
//		headers.put(HttpConstant.CONTENT_LENGTH_HEADER, response_body.length()+"");
		
		///		
		//get request method
		String method = requestdata.getMethod();
		
		switch(method){
			case "GET":
				
					if(file!=null){
						if(file.isFile()){
							log.info("HttpResponse: file isFile");
							FileInputStream fis = new FileInputStream(file); //file
							log.info("HttpResponse: file FileInputStream");
							sendResponseBegin(response_first, headers, out); //send back line and headers							
							sendResponseGeneralBody(out,fis); //send back file
							
//							StringBuilder ssb = new StringBuilder();
//							ssb.append(HttpConstant.HTTP_RESPONSE_START +HttpConstant.HTTP_RESPONSE_END +HttpConstant.HTTP_CLEAR_CACHE);
//							sendResponseSpecialBody(out,ssb);///
							if(requestdata.isShutDown()){
								log.info("HttpResponse: shutting down server");
								ThreadPool.shutdown();
							}
						
						
						}
						else if(file.isDirectory()){
								log.info("HttpResponse: file is directory");
								String path = file.getPath();
								StringBuilder newbody = new StringBuilder();
								newbody.append("<html>"
										+"<head>Cannot find file</head>"
										+"<body>"+ path+"</body>"
										+ "</html>");
									
								sendResponseBegin(response_first, headers, out);
								sendResponseSpecialBody(out,newbody);
								
						}
					}else{
						log.info("HttpResponse: file is null");
						sendResponseBegin(response_first, headers, out);
						sendResponseSpecialBody(out,response_body);
						if(requestdata.isShutDown()){
							log.info("HttpResponse: shutting down server");
							ThreadPool.shutdown();
						}
					}
					
					
				
			
			case "HEAD":
				try{
					
					sendResponseBegin(response_first, headers, out);
					
				}catch(IOException e){
					log.info("HttpResponse: do not send header");
				}
								
			
		}
	}
		
	
	/*
	 * send first line and headers
	 */
	public void sendResponseBegin(StringBuilder response_first, HashMap<String,String> response_header,DataOutputStream out) throws IOException{
		StringBuilder response = new StringBuilder();
		response.append(HttpConstant.CRLF);
		response.append(response_first+HttpConstant.CRLF);
		Set<String> set = response_header.keySet();
		for(String key : set){
			response.append(key+": ");
			response.append(response_header.get(key));
			response.append(HttpConstant.CRLF);		
		}
		response.append(HttpConstant.CRLF);	
		out.write(response.toString().getBytes());
		
	}	
	
	
	/*
	 * send body with files
	 */
	public void sendResponseGeneralBody(DataOutputStream out, FileInputStream fis) throws IOException{
		byte rw_buffer[] = new byte[1024];
		int num_bytes = 0;
				
		//read until num_bytes = -1, meaning end of file
		while ((num_bytes = fis.read(rw_buffer)) != -1) {
			out.write(rw_buffer, 0, num_bytes);
		}
		
		fis.close();
		
//		int content;
//		while((content = fis.read())!=-1){			
//			out.write(content);
//		}
//		fis.close();
		
	}
	
	/*
	 * send body without file
	 */
	public void sendResponseSpecialBody(DataOutputStream out, StringBuilder response_body) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(HttpConstant.CRLF);
		sb.append(response_body);
		out.write(sb.toString().getBytes());		
	}
	
	
	/*
	 * TODO:  
	 */
	public static void sendError(){
		
				
	}
	
	/*
	 * TODO:
	 */
	public static void sendForbidden(){
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
