package edu.upenn.cis.cis455.webserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ServletContainer {
	static Logger log = Logger.getLogger(ServletContainer.class);
	private HashMap<Long, Session> sessionsmap;
	private HashMap<String, HttpServlet> servletsmap;
	private Context context;
//	private Config config;
	private Handler handler;
	
	
	public ServletContainer(Handler handler) {
		sessionsmap = new HashMap<Long, Session>();
		servletsmap = new HashMap<String, HttpServlet>();
		this.handler = handler;
		
	//parse xml to handler
		try{
			handler = parseXML("conf/web.xml");
		}catch(Exception e){
			e.printStackTrace();
			log.info("ServletContainer: cannot parse xml to handler");
		}
		
	//create servletsmap;
		servletsmap = createServletMaps();
	
	//create sessionsmap
		
		
		
	}
	
	
	public Handler parseXML(String xmlPath) throws ParserConfigurationException, SAXException, IOException{
		File file = new File(xmlPath);
		log.info("ServletContainer: is file "+ file.isFile());
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		log.info("ServletContainer: start parsing xml");
		parser.parse(file, handler);
		
		return handler;
		
	}
	
	
	public Context createContext(){
		context = new Context();
		HashMap<String,String> map = handler.getContextParams();
		Set<String> set = map.keySet();
		
		for(String key : set){
			context.setInitParameters(key, map.get(key));			
		}
		
		return context;
		
	}
	
	public Context getContext(){
		return context;
	}
	
	
	public Handler getHandler(){
		return handler;
	}
	
	
	/*
	 * create a servlet when we know servletname and context through handler.
	 * a servlet is a wrap, inside is config
	 */
	public HttpServlet createServlet(String servletname, Context con){
		
		HttpServlet servlet = null;
		Config config = new Config(servletname,con);
		HashMap<String,String> servletClassMap = handler.getServletClass();
		
	//get servlet class name through servletClass map
		
		String classname = servletClassMap.get(servletname);
		
	//get the exact servlet when we know servlet name
		Class servletEmptyClass = null;
		try{
			log.info("ServletContainer: classname is "+classname);
			
			servletEmptyClass = Class.forName("edu.upenn.cis.cis455.webserver."+classname);  // must contain the path in class
			
			servlet = (HttpServlet) servletEmptyClass.newInstance();
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.info("ServletContainer: cannot create a servlet");
		}
	
	//set init data in config
		HashMap<String, String> initParamsConfigMap = handler.getInitParams();
		Set<String> set = initParamsConfigMap.keySet();
		for(String key : set){
			String value = initParamsConfigMap.get(key);
			config.setInitParameter(key, value);			
		}
		
	// init servlet	
		try{
			servlet.init(config);
			
		}catch(Exception e){
			e.printStackTrace();
			log.info("ServletContainer: cannot init servlet");
		}
		
		return servlet;
			
	}
	
	
	
	public HashMap<String, HttpServlet> createServletMaps(){
		
	// first need to know all servlet name;
		HashMap<String,String> servletClass = handler.getServletClass();
		Set<String> allServletName = servletClass.keySet();
		Context con = createContext();
		
		for(String servletname: allServletName){
			log.info("ServletContainer: servletname is "+servletname);
			HttpServlet servlet = createServlet(servletname,con);
			
			servletsmap.put(servletname, servlet);			
		}
		
		return servletsmap;		
		
	}
	
	
	public HttpServlet servletNameMatching(HttpMetaData requestdata){
		String path = requestdata.getFilePath();
		log.info("ServletContainer: requestdata.getFilePath() = "+path);
		
		HashMap<String,String> mapexact = handler.getServletExactUrl();
		log.info("ServletContainer: mapexact is null "+mapexact.isEmpty());
		log.info("ServletContainer: TestServlet in mapexact "+mapexact.containsKey("/test"));
		
		HashMap<String,String> mappath = handler.getServletPathUrl();
		log.info("ServletContainer: mappath is null "+mappath.isEmpty());
		log.info("ServletContainer: TestServlet in mappath "+mappath.containsKey("/test"));
		
		String servletname = null;
		boolean pathMatchSearch = true;
		
		if(mapexact.containsKey(path)){
			HttpServlet servlet = servletsmap.get(mapexact.get(path));
			if(servlet == null){
				Context con = createContext();
				servlet = createServlet(mapexact.get(path),con);
			}
			return servlet;
		}
				
		
		while(pathMatchSearch){
			
			if(mappath.containsKey(path)){
				HttpServlet servlet = servletsmap.get(mappath.get(path));
				if(servlet == null){
					Context con = createContext();
					servlet = createServlet(mappath.get(path),con);
				}
				return servlet;
			}
			
			if(path !=null && path.length()>0){
				int index = path.lastIndexOf("/");
				if(index>0){
					path = path.substring(0, index);
				}else{
					pathMatchSearch = false;
				}
				
			}else{
				pathMatchSearch = false;
			}
		
			
		}
		
		return null;
		
		
	}
	
	
	
	public Session getSession(Long id){
		return sessionsmap.get(id);
	}
	
		
	public void addSession(Session s){
		String id = s.getId();
		Long key = Long.parseLong(id);
		sessionsmap.put(key, s);
	}
	
	public HashMap<Long, Session> getSessionsMap(){
		return sessionsmap;
	}
	
	public HashMap<String, HttpServlet> getServletsMap(){
		return servletsmap;
	}
	
	

}
