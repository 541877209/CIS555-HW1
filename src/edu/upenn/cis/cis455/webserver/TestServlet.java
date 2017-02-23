package edu.upenn.cis.cis455.webserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class TestServlet extends HttpServlet{
	static Logger log = Logger.getLogger(TestServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		
		log.info("TestServlet: doGet");
		try{
		
			response.setContentType(HttpConstant.DEFAULT_CONTENT_TYPE); // put content type in response header			
			log.info("TestServlet: "+response.getContentType());
			Cookie cok = new Cookie("cookie1","12345");
			log.info("TestServlet: cookie "+cok.getName()+" "+cok.getValue());
			response.addCookie(cok);
//			log.info("TestServlet: cookie "+" "+cok.getValue());
			
			
			PrintWriter out = response.getWriter();
			
			String ss = "<html>"
					+ "<head>"
					+ "<title>Hellow WWW</title>"
					+ "</head>"
					+ "<body>"
					+ "<p>Hello WWW test servlet</p>"
					+ "</body>"
					+ "</html>";
			
			out.println(ss);
		
		}catch(Exception e){
			e.printStackTrace();
			log.info("TestServlet: cannot doGet");
		}
		
	}
	
}
