package edu.upenn.cis.cis455.webserver;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class Config implements ServletConfig{

	private String servletName;
	private Context context;
	private HashMap<String,String> initParams;
	
	
	public Config(String servletName, Context context) {
		this.servletName = servletName;
		this.context = context;
		this.initParams = new HashMap<String,String>();
		
	}

	@Override
	public String getInitParameter(String arg0) {
		
		return initParams.get(arg0);
	}
	
	public void setInitParameter(String key, String value){
		initParams.put(key, value);
	}
	

	@Override
	public Enumeration getInitParameterNames() {
		Set<String> set = initParams.keySet();
		Vector<String> v = new Vector(set);
		return v.elements();
	}

	@Override
	public ServletContext getServletContext() {
		
		return context;
	}

	@Override
	public String getServletName() {
		
		return servletName;
	}

}
