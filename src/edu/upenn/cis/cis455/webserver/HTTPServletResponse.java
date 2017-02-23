package edu.upenn.cis.cis455.webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class HTTPServletResponse implements HttpServletResponse{
	static Logger log = Logger.getLogger(HTTPServletResponse.class);
	private HttpMetaData request_data;
	private HashMap<String, String> headers;
	private DataOutputStream out;
	private BufferedReader in;
	private StringBuffer buffer;
	private HTTPServletRequest request;
	private Locale locale = null;
	private boolean isCommitted = false;
	private Integer status = null;
	private String statusmessage = null;
	private String errormessage;
	private boolean isCalled = false;
	
	private String CHAR_ENCODING = "ISO-8859-1";
	
	public HTTPServletResponse(HttpMetaData request_data,HTTPServletRequest request) throws IOException {
		headers = new HashMap<>();
		this.request = request;
		this.request_data = request_data; 
		out = request_data.getOutputStream();
		in = request_data.getInStream();
		buffer = new StringBuffer();
		status = 200;
	}

	@Override
	public void flushBuffer() throws IOException {
		isCommitted = true;
		out.flush();
		log.info("HTTPServletResponse: flushBuffer");
		out.write(buffer.toString().getBytes());
		out.flush();
//		clearBuffer();
	}

	public void clearBuffer(){
		buffer.setLength(0);
		headers.clear();
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		isCalled = true;
		writeBuffer();
		log.info("HTTPServletResponse: after write buffer");
		flushBuffer();
		return new PrintWriter(out,true);		
	}

	public void setCookies(){
		Cookie[] cookies = request.getCookiesConstruct();
		for(Cookie c: cookies){
			addCookie(c);
		}		
	}
	
	
	
	
	/*************************************************************************************************************
	 * buffer.append(HttpConstant.CRLF) is really important, if append one more or loose one could result problem*
	 *************************************************************************************************************/
	
	public void writeBuffer(){  ////buffer.append(HttpConstant.CRLF) is really 
		if(status==200){
			buffer.append(HttpConstant.CRLF);
			buffer.append(HttpConstant.HTTP_ONE_DOT_ONE+" "+HttpConstant.HTTP_GOOD_REQUEST);
			buffer.append(HttpConstant.CRLF);
		}
		else{
			buffer.append(HttpConstant.HTTP_ONE_DOT_ONE + " "+statusmessage);
			buffer.append(HttpConstant.CRLF);
		}
//		buffer.append(HttpConstant.CRLF);
		Set<String> keys = headers.keySet();
		for(String key : keys){
			log.info("HTTPServletResponse: headers key "+ key + " "+headers.get(key));
			buffer.append(key+": "+headers.get(key));	
			buffer.append(HttpConstant.CRLF);
		}
		buffer.append(HttpConstant.CRLF);
				
	}
	
	@Override
	public void setStatus(int arg0) {
		status = arg0;
		if(arg0==200){
			statusmessage = HttpConstant.HTTP_GOOD_REQUEST;
		}else if(arg0 == 302){
			statusmessage = HttpConstant.HTTP_REDIRECT;
		}else if(arg0 == 500){
			statusmessage = HttpConstant.HTTP_INTERNAL_ERROR;
		}
	}
	

	@Override
	public void setContentType(String arg0) {
		headers.put(HttpConstant.CONTENT_LENGTH_TYPE, arg0);		
	}
	
	@Override
	public int getBufferSize() {
		
		return buffer.toString().length();
	}

	@Override
	public String getCharacterEncoding() {
		
		return CHAR_ENCODING;
	}

	@Override
	public String getContentType() {
		
		return headers.get("Content-Type");
		
	}

	@Override
	public Locale getLocale() {
		
		return locale;
	}

	/*
	 * (non-Javadoc)
	 *not required
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	
	@Override
	public boolean isCommitted() {
		
		return isCommitted;
	}

	@Override
	public void reset() {
		if(!isCommitted){
			buffer.setLength(0); 
			setContentType(HttpConstant.CONTENT_DEFAULT_TYPE);
			setContentLength(0);
		}else{
			log.info("HttpServletResponse: buffer is not committed");
		}
		
		
	}

	@Override
	public void resetBuffer() {
		if(isCommitted){
			log.info("HttpServletResponse: isCommitted, cannot reset-buffer");
		}else{
			buffer.setLength(0);  
		}
		
		
	}

	@Override
	public void setBufferSize(int arg0) {
		if(!isCommitted){
			buffer.setLength(arg0);
		}else{
			log.info("HttpServletResponse: isCommitted, cannot setBufferSize");
		}
		
		
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		CHAR_ENCODING = arg0;
		
	}

	@Override
	public void setContentLength(int arg0) {
		
		headers.put(HttpConstant.CONTENT_LENGTH_HEADER, Integer.toString(arg0));
		
	}


	@Override
	public void setLocale(Locale arg0) {
		locale = arg0;
		
	}

	@Override
	public void addCookie(Cookie arg0) {
		if(!headers.containsKey(HttpConstant.SET_COOKIE)){
			headers.put(HttpConstant.SET_COOKIE, arg0.getName()+"="+arg0.getValue());
		}else{
			String value = headers.get(HttpConstant.SET_COOKIE);
			value += "," + arg0.getName()+"="+arg0.getValue() ;
			headers.put(HttpConstant.SET_COOKIE, value);
		}
		
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		Date date = new Date(arg1);
		String res = HttpConstant.parseDate(date);
		headers.put(arg0, res);
		
	}

	@Override
	public void addHeader(String arg0, String arg1) {
		headers.put(arg0, arg1);
		
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		headers.put(arg0, Integer.toString(arg1));
		
	}

	@Override
	public boolean containsHeader(String arg0) {
		return headers.containsKey(arg0);
	}

	@Override
	public String encodeRedirectURL(String url) {
		String s = null;
		try{
			s = URLEncoder.encode(url,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			log.info("HttpServletResponse: cannot encode url");
		}
		
		return s;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		String s = null;
		try{
			s = URLEncoder.encode(url,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			log.info("HttpServletResponse: cannot encode url");
		}
		
		return s;
	}

	@Override
	public String encodeURL(String url) {
		String s = null;
		try{
			s = URLEncoder.encode(url,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			log.info("HttpServletResponse: cannot encode url");
		}
		
		return s;
	}

	@Override
	public String encodeUrl(String url) {
		String s = null;
		try{
			s = URLEncoder.encode(url,"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
			log.info("HttpServletResponse: cannot encode url");
		}
		
		return s;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendError(int errorcode, String errormessage) throws IOException {
		this.errormessage = errormessage;
		sendError(errorcode);
		
	}

	@Override
	public void sendRedirect(String url) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		Date date = new Date(arg1);
		String res = HttpConstant.parseDate(date);
		headers.put(arg0, res);
		
	}

	@Override
	public void setHeader(String arg0, String arg1) {
		headers.put(arg0, arg1);
		
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		headers.put(arg0, Integer.toString(arg1));
		
	}

	

	/*
	 * (non-Javadoc)
	 * not required;
	 */
	@Override
	public void setStatus(int arg0, String arg1) {
	}

}
