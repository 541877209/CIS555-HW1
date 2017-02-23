package edu.upenn.cis.cis455.webserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class HTTPServletRequest implements HttpServletRequest{
	
	static Logger log = Logger.getLogger(HTTPServletRequest.class);
	private HttpMetaData request_data;
	private ServletContainer container;
	private Session session;
	
	
	
	private HashMap<String,Object> attributes;
	private String CHAR_ENCODING = "ISO-8859-1"; 
	private Locale locale = null;
	private HashMap<String,ArrayList<String>> params;
	private HashMap<String, String[]> new_map;
	private Cookie[] cookiecookie;
	
	public HTTPServletRequest(HttpMetaData request_data, ServletContainer container) {
	
	//initialize
		this.container = container;
		this.request_data = request_data;
		this.attributes = new HashMap<String,Object>();
		params = new HashMap<String,ArrayList<String>>();
		new_map = new HashMap<String, String[]>();         // for parameters num=1&num1=3
		
	//put queries in params
		
		if(!request_data.getQueryString().equals("")){
			String[] queries = request_data.getQueryString().split("&");
			for(String query:queries){
				String[] pnv = query.split("=");
				if(!params.containsKey(pnv[0])){
					params.put(pnv[0], new ArrayList<String>());
				}
				params.get(pnv[0]).add(pnv[1]);
				
			}			
		}
	
	//
	//find session id in cookies, and get session of this request, if not exist in container then create one and add.
	//
		
		cookiecookie = getCookies() ;
		if(cookiecookie !=null){
			HashMap<Long,Session> sessionsmap = container.getSessionsMap();
			for(Cookie c : cookiecookie){
				if(c.getName().equals(HttpConstant.COOKIE_JSESSIONID)){
					Long id = Long.parseLong(c.getValue());
					try{
						session = getSession(id);	//if this session exist in container then get it, if not, then create 
					}catch(Exception e){
						e.printStackTrace();
						log.info("HTTPServletRequest: cannot get session in constructor");
					}
								
				}
				
				
			}
		}
		
	}
	
	public Cookie[] getCookiesConstruct(){
		return cookiecookie;
	}
	
	@Override
	public HttpSession getSession() {

		return session;
	}

	

	
	public Session getSession(Long id){
		HashMap<Long,Session> map = container.getSessionsMap();
		if(!map.containsKey(id)){
			Session se = getSession(true);
			return se;
		}else{
			return map.get(id);
		}
				
	}
	
	public Session getSession(boolean arg0){
		if(arg0){
			Session se = new Session(container);
			container.addSession(se);
			return se;
		}else{
			return null;
		}
		
	}
	
	@Override
	public Cookie[] getCookies() {
		
		ArrayList<String> list = request_data.getHeadersMap().get("Cookie");
		ArrayList<Cookie> ll = new ArrayList<>();
		if(list!=null){
			for(String s : list){
				String[] ss = s.split("=");
				Cookie cookie = new Cookie(ss[0],ss[1]);
				ll.add(cookie);			
			}
			Cookie[] cookies = (Cookie[])ll.toArray(new Cookie[list.size()]);
			return cookies;
		}else{
			return null;
		}
		
		
	}
	
	
	@Override
	public Object getAttribute(String arg0) {
		return attributes.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		Set<String> set = attributes.keySet();
		Vector<String> v = new Vector(set);
		return v.elements();
	}

	@Override
	public String getCharacterEncoding() {
		log.info("HTTPServletRequest: getCharacterEncoding()");
		return CHAR_ENCODING;
	}

	@Override
	public int getContentLength() {
		log.info("HTTPServletRequest: getContentLength()");
		ArrayList<String> list = request_data.getHeadersMap().get("Content-Length");
		String length = list.get(0);
		if(length!=null && length.length()>0){
			try{
				return Integer.parseInt(list.get(0));
			}catch(Exception e){
				return -1;
			}
			
		}else{
			return -1;
		}
		
	}

	@Override
	public String getContentType() {
		log.info("HTTPServletRequest: getContentType()");
		ArrayList<String> list = request_data.getHeadersMap().get("Content-Type");
		String contenttype = list.get(0);
		if(contenttype!=null && contenttype.length()>0){
			
			return contenttype;			
		}else{
			return null;
		}
		
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		
		return null;
	}

	@Override
	public String getLocalAddr() {
		log.info("HTTPServletRequest: getLocalAddr()");
		String s = request_data.getSocket().getLocalAddress().getHostAddress();
		return s;
	}

	@Override
	public String getLocalName() {
		log.info("HTTPServletRequest: getLocalName()");
		String s = request_data.getSocket().getLocalAddress().getCanonicalHostName();
		return s;
	}

	@Override
	public int getLocalPort() {
		log.info("HTTPServletRequest: getLocalPort()");
		return request_data.getSocket().getLocalPort();
	}

	@Override
	public Locale getLocale() {
		
		return locale;
	}

	/*
	 * not required
	 * (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	@Override
	public Enumeration getLocales() {
		
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		log.info("HTTPServletRequest: getParameter(String arg0)");
		ArrayList<String> ll = params.get(arg0);
		if(ll!=null && !ll.isEmpty()){
			return ll.get(0);
		}else{
			return null;
		}
		
		
	}

	@Override
	public Map getParameterMap() {		
		return params;
	}

	@Override
	public Enumeration getParameterNames() {
		Set<String> set = params.keySet();
		Vector<String> v = new Vector<>(set);
		return v.elements();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		if(params.containsKey(arg0)){
			ArrayList<String> ll = params.get(arg0);
			String[] res = ll.toArray(new String[ll.size()]);
			return res;
		}
		return null;
	}

	@Override
	public String getProtocol() {
		log.info("HTTPServletRequest: getProtocol()");
		if(!request_data.isBadVersion() && request_data.isVersion1()){
			log.info("HTTPServletRequest: getProtocol()  HTTP/1.1");
			return "HTTP/1.1";
		}else if(!request_data.isBadVersion() && !request_data.isVersion1()){
			log.info("HTTPServletRequest: getProtocol()  HTTP/1.0");
			return "HTTP/1.0";
		}else{
			return null;
		}
		
	}

	/*
	 * Retrieves the body of the request as character data using a BufferedReader. 
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		log.info("HTTPServletRequest: getReader()");
		BufferedReader br = new BufferedReader(new StringReader(request_data.getMessageBody()));
		
		return br;
	}

	/*
	 * not required
	 * 
	 */
	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * Returns the Internet Protocol (IP) address of the client
	 */
	@Override
	public String getRemoteAddr() {
		log.info("HTTPServletRequest: getRemoteAddr()");
		return request_data.getSocket().getInetAddress().getHostAddress();
	}

	@Override
	public String getRemoteHost() {
		log.info("HTTPServletRequest: getRemoteHost()");
		return request_data.getSocket().getInetAddress().getHostName();
	}

	
	/*
	 * (non-Javadoc)
	 * Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request.
	 */
	@Override
	public int getRemotePort() {
		log.info("HTTPServletRequest: getRemotePort()");
		return request_data.getSocket().getPort();
	}

	/*
	 * (non-Javadoc)
	 *  not required
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * Returns the name of the scheme used to make this request, for example, http, https, or ftp.
	 */
	@Override
	public String getScheme() {
		
		return "http";
	}

	/*
	 * (non-Javadoc)
	 * Returns the host name of the server to which the request was sent. It is the value of the part before ":" in the Host header value
	 */
	@Override
	public String getServerName() {
		log.info("HTTPServletRequest: getServerName()");
		return request_data.getSocket().getLocalAddress().getCanonicalHostName();
	}

	@Override
	public int getServerPort() {
		log.info("HTTPServletRequest: getServerPort()");
		return request_data.getSocket().getLocalPort();
	}

	/*
	 * (non-Javadoc)
	 * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS
	 */
	@Override
	public boolean isSecure() {
		
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		attributes.remove(arg0);		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0,arg1);
		
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		CHAR_ENCODING = arg0;
		
	}

	@Override
	public String getAuthType() {
		
		return BASIC_AUTH;
	}

	@Override
	public String getContextPath() {
		return "";
	}

	

	/*
	 * The date is returned as the number of milliseconds since January 1, 1970 GMT. @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	@Override
	public long getDateHeader(String arg0) {
		log.info("HTTPServletRequest: getDateHeader(String arg0)");
		if(request_data.getHeadersMap().containsKey(arg0)){
			String time = request_data.getHeadersMap().get("arg0").get(0);
			Date date = HttpConstant.parseStringToDate(time);
			if(date!=null){
				long l = date.getTime();
				return l;
			}
			
		}
		return -1;
		
	}

	@Override
	public String getHeader(String arg0) {
		log.info("HTTPServletRequest: getHeader(String arg0)");
		if(request_data.getHeadersMap().containsKey(arg0)){
			return request_data.getHeadersMap().get(arg0).get(0);
		}else{
			return null;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * Returns an enumeration of all the header names this request contains
	 */
	@Override
	public Enumeration getHeaderNames() {
		log.info("HTTPServletRequest: getHeaderNames()");
		Set<String> set = request_data.getHeadersMap().keySet();
		Vector<String> vector = new Vector<>(set);
		return vector.elements();
	}

	/*
	 * Returns all the values of the specified request header as an Enumeration of String objects.
	 */
	@Override
	public Enumeration getHeaders(String arg0) {
		ArrayList<String> list = request_data.getHeadersMap().get(arg0);
		return Collections.enumeration(list);
	}

	@Override
	public int getIntHeader(String arg0) {
		HashMap<String,ArrayList<String>> map = request_data.getHeadersMap();
		if(!map.containsKey(arg0)){
			return -1;
		}else{
			String s = map.get(arg0).get(0);
			try{
				int res = Integer.parseInt(s);
				return res;
			}catch(NumberFormatException e){
				e.printStackTrace();
				log.info("HTTPServletRequest: cannot parse integer");
			}
		}
		return -1;
		
	}

	
	
	/*
	 * (non-Javadoc)
	 * Gets the method of the request
	 */
	@Override
	public String getMethod() {
		log.info("HTTPServletRequest: get method");
		return request_data.getMethod();
	}

	@Override
	public String getPathInfo() {
		
		return request_data.getPathInfo();
	}
	/*
	 * (non-Javadoc)
	 * not required
	 */
	@Override
	public String getPathTranslated() {
		
		return null;
	}

	@Override
	public String getQueryString() {
		return request_data.getQueryString();
	}

	/*
	 * (non-Javadoc)
	 * not required;
	 */
	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		log.info("HTTPServletRequest: getRequestURI()");
		return request_data.getFilePath();
	}

	@Override
	public StringBuffer getRequestURL() {
		log.info("HTTPServletRequest: getRequestURL()");
		StringBuffer sb = new StringBuffer();
		sb.append(getScheme());
		sb.append("://");
		sb.append(getServerName());
		sb.append(getLocalPort());
		sb.append(request_data.getFilePath());
		return sb;
	}

	@Override
	public String getRequestedSessionId() {
		if(session!=null){
			return session.getId();
		}
		return null;
	}

	@Override
	public String getServletPath() {
		log.info("HTTPServletRequest: getServletPath()");
		return request_data.getServletPath();
	}

	/*
	 * not required
	 */
	@Override
	public Principal getUserPrincipal() {
		
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		log.info("HTTPServletRequest: isRequestedSessionIdFromCookie()");
		if (session != null)
			return true;
		else
			return false;
	}

	/*
	 * always false
	 */
	
	@Override
	public boolean isRequestedSessionIdFromURL() {
		
		return false;
	}

	/*
	 * Not required
	 */
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		log.info("HTTPServletRequest: isRequestedSessionIdValid()");
		if(session!=null){
			return true;
		}
		return false;
	}

	/*
	 * Not required
	 */
	@Override
	public boolean isUserInRole(String arg0) {
		
		return false;
	}

}
