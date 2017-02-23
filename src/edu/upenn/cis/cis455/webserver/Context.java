package edu.upenn.cis.cis455.webserver;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Context implements ServletContext{

	private HashMap<String,Object> attributes;
	private HashMap<String,String> params;
	
	public Context() {
		attributes = new HashMap<String,Object>();
		params = new HashMap<String,String>();
		
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

	@Override
	public ServletContext getContext(String arg0) {		
		return this;
	}

	@Override
	public String getInitParameter(String arg0) {
		
		return params.get(arg0);
	}

	@Override
	public Enumeration getInitParameterNames() {
		Set<String> set = params.keySet();
		Vector<String> v = new Vector<>(set);
		return v.elements();
	}
	
	public void setInitParameters(String key, String value){
		params.put(key, value);
	}

	@Override
	public int getMajorVersion() {		
		return 2;
	}

	/*
	 * not required
	 */	
	@Override
	public String getMimeType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMinorVersion() {
		return 4;
	}

	/*
	 * not required
	 */
	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		
		return "";
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	//not required
	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		
		return null;
	}

	//not required
	@Override
	public InputStream getResourceAsStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//not required
	@Override
	public Set getResourcePaths(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerInfo() {
		return HttpConstant.DEFAULT_SERVER_NAME;
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	//not required
	@Override
	public String getServletContextName() {
		// TODO Auto-generated method stub
		return null;
	}

	//not required
	@Override
	public Enumeration getServletNames() {
		// TODO Auto-generated method stub
		return null;
	}

	//not required
	@Override
	public Enumeration getServlets() {
		// TODO Auto-generated method stub
		return null;
	}

	//not required
	@Override
	public void log(String arg0) {
		// TODO Auto-generated method stub
		
	}

	//not required
	@Override
	public void log(Exception arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	//not required
	@Override
	public void log(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String arg0) {
		attributes.remove(arg0);
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0, arg1);
		
	}

}
