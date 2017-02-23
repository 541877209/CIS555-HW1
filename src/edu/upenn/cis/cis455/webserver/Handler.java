package edu.upenn.cis.cis455.webserver;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Handler extends DefaultHandler{
	static Logger log = Logger.getLogger(Handler.class);
	private int m_state=0;
	private String m_displayName = null;
	private String m_servletName;
	private String m_intParam;
	
	
	private HashMap<String,String> m_servletClassLoc = new HashMap<String,String>();
	private HashMap<String,String> m_servletExactUrl = new HashMap<String,String>(); //key is url,value is servletname
	private HashMap<String,String> m_servletPathUrl = new HashMap<String,String>();
	private HashMap<String,String> m_contextParams = new HashMap<String,String>();
	private HashMap<String,String> m_initParams = new HashMap<String,String>();
	
	private String tag;
	private String subtag;
	
	private String DISPLAY = "display-name";
	private String WEB_APP = "web-app";
	private String SERVLET = "servlet";
	private String SERVLET_DEFINITION = "servlet-definition";
	private String SERVLET_CLASS = "servlet-class";
	private String SERVLET_DEFINITION_NAME = "servlet-definition-name";
	private String SERVLET_DEFINITION_PARAM_NAME = "servlet-definition-param-name";	
	private String SERVLET_MAPPING = "servlet-mapping";
	private String SERVLET_MAPPING_NAME = "servlet-mapping-name";	
	private String SERVLET_NAME = "servlet-name";	
	private String PARAM_NAME = "param-name";
	private String PARAM_NAME_IN_DEFINITION = "param-name-in-definition";
	private String PARAM_NAME_IN_CONTEXT = "param-name-in-context";	
	private String PARAM_VALUE = "param-value";
	private String PARAM_VALUE_IN_DEFINITION = "param-value-in-definition";
	private String PARAM_VALUE_IN_CONTEXT = "param-value-in-context";	
	private String URL_PATTERN = "url-pattern";
	private String CONTEXT = "context";
	
	
	private String keyClass;
	private String urlmapvalue;
//	private String keyPathUrl;
	private String keyInitParam;
	private String keyConextParam;
	private String appname;
	private String display;
	private String url;
	private boolean isExact;
	
	
	
	public Handler() {
		// TODO Auto-generated constructor stub
	}

	
	public void startElement(String uri, String localName, String qName,Attributes attributes){
		if(qName.equals("display-name")){
			tag =  "display-name";
			subtag = "";
		}
		else if(qName.equals("web-app")){
			tag = "web-app";
			subtag = "";
		}
		else if(qName.equals("servlet")){
			tag = "servlet";
			subtag = "";
		}
		else if(qName.equals("servlet-mapping")){
			tag = "servlet-mapping";
			subtag = "";
		}
		else if(qName.equals("context")){
			tag = "context";
			subtag = "";
		}
		else if(qName.equals("url-pattern") && tag.equals("servlet-mapping")){
			subtag = "url-pattern";
			
		}
		else if(qName.equals("servlet-class")&& tag.equals("servlet")){
			subtag = "servlet-class";
			
		}
		else if(qName.equals("servlet-name") && tag.equals("servlet")){
			subtag = "servlet-definition-name";
		}
		else if(qName.equals("servlet-name") && tag.equals("servlet-mapping")){
			subtag = "servlet-mapping-name";
		}
//		else if(qName.equals(SERVLET_NAME) && tag.equals(SERVLET_MAPPING))
		
		else if(qName.equals("param-name") && tag.equals("servlet")){
			subtag = "param-name-in-definition";
		}
		else if(qName.equals("param-name") && tag.equals("context")){
			subtag = "param-name-in-context";
		}
		else if(qName.equals("param-value") && tag.equals("servlet")){
			subtag = "param-value-in-definition";
		}
		else if(qName.equals("param-value") && tag.equals("context")){
			subtag = "param-value-in-context";
		}
		
		
	}
	
	
	
	public void characters(char[] ch, int start, int length){
		String value = new String(ch,start,length);
		value = value.trim();
		if(tag.equals("servlet") && subtag.equals("servlet-definition-name")){
			if(value!=null && !value.equals("")){
				keyClass = value;
				log.info("Handler: get a keyClass "+keyClass);
			}
			
			
		}
		else if(tag.equals("servlet-mapping") && subtag.equals("servlet-mapping-name")){
			if(value!=null&& !value.equals("")){
				if(isExact){
					m_servletExactUrl.put(url, value);
				}else{
					m_servletPathUrl.put(url, value);
				}
			}
			
			
			
		}
		else if(tag.equals("servlet") && subtag.equals("param-name-in-definition")){
			if(value!=null&& !value.equals("")){
				keyInitParam = value;
			}
			
		}
		else if(tag.equals("context") && subtag.equals("param-name-in-context")){
			if(value!=null&& !value.equals("")){
				keyConextParam = value;
			}
		}
		else if(tag.equals("servlet") && subtag.equals("servlet-class")){
			if(value!=null&& !value.equals("")){
				m_servletClassLoc.put(keyClass, value);
				log.info("Handler: put a Class "+keyClass + " "+value);
			}
			
		}
		else if(tag.equals("servlet") && subtag.equals("param-value-in-definition")){
			if(value!=null&& !value.equals("")){
				m_initParams.put(keyInitParam, value);
			}
			
			
		}
		else if(tag.equals("context") && subtag.equals("param-value-in-context")){
			if(value!=null&& !value.equals("")){
				m_contextParams.put(keyConextParam, value);
			}
			
		}
		else if(tag.equals("servlet-mapping") && subtag.equals("url-pattern")){
			if(value!=null&& !value.equals("")){
				if(value.endsWith("/*")){
					isExact = false;
					url = value.substring(0, value.length()-2);
					log.info("Handler: put an exact path.");
				}else{
					isExact = true;
					url = value;
					log.info("Handler: put a path.");
				}
			}
		}
		else if(tag.equals("display-name") && subtag.equals("")){
			if(value!=null&& !value.equals("")){
				display = value;
			}
			
		}
		
		
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException{
		
	}
	
	public HashMap<String,String> getContextParams(){
		return m_contextParams;
	}
	
	public HashMap<String,String> getInitParams(){
		return m_initParams;
	}
	
	public HashMap<String,String> getServletClass(){
		return m_servletClassLoc;
	}
	
	public HashMap<String,String> getServletExactUrl(){
		return m_servletExactUrl;
	}
	
	public HashMap<String,String> getServletPathUrl(){
		return m_servletPathUrl;
	}
	
	
}
