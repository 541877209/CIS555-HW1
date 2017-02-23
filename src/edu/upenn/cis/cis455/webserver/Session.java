package edu.upenn.cis.cis455.webserver;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class Session implements HttpSession{

	private HashMap<String, Object> attributes;
	private long creationTime;
	private long lastAccessedTime; 
	private static long sessionIncrement=0; 
	private long id = 0L;
	private int maxInterval=-1;
	private boolean valid = true;
	private boolean isnew = true;
	private ServletContainer container;
	
	
	public Session(ServletContainer container) {
		this.container = container;
		attributes = new HashMap<String, Object>();
		Date date = new Date();
		creationTime = date.getTime();
		lastAccessedTime = date.getTime();
		id = sessionIncrement++;
	}

	
	public ServletContainer getContainer(){
		return container;
	}
	
	
	@Override
	public Object getAttribute(String arg0) {
		
		return attributes.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		Set<String> set = attributes.keySet();
		Vector<String> v = new Vector<>(set);
		return v.elements();
	}

	/*
	 * Returns the time when this session was created, measured in milliseconds
	 */
	@Override
	public long getCreationTime() {		
		return creationTime;
	}

	@Override
	public String getId() {
		
		return Long.toString(id);
	}

	@Override
	public long getLastAccessedTime() {
		
		return lastAccessedTime;
	}

	/*
	 * (non-Javadoc)
	 * an integer specifying the number of seconds this session remains open between client requests
	 */
	@Override
	public int getMaxInactiveInterval() {
		
		return maxInterval;
	}

	/*
	 * Returns the ServletContext to which this session belongs.
	 */
	@Override
	public ServletContext getServletContext() {
		ServletContext sc = container.getContext();
		return sc;
	}

	/*
	 * not required
	 */
	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		
		return attributes.get(arg0);
	}
	
	/*
	 * not required
	 */
	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		valid = false;
		
	}

	@Override
	public boolean isNew() {
		
		return isnew;
	}

	/*
	 * (non-Javadoc)
	 *not required
	 */
	@Override
	public void putValue(String arg0, Object arg1) {
		
	}

	@Override
	public void removeAttribute(String arg0) {
		attributes.remove(arg0);
		
	}
	/*
	 * not required
	 */
	@Override
	public void removeValue(String arg0) {
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0,arg1);		
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		maxInterval = arg0;
	}

}
