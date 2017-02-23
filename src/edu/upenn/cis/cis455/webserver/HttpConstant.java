package edu.upenn.cis.cis455.webserver;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.log4j.Logger;



public class HttpConstant {

	static Logger log = Logger.getLogger(HttpConstant.class);
	
	public static final String HTTP_HEADER_COOKIE = "Cookie";
	public static final String CONTENT_DEFAULT_TYPE = "text/html";
	
	public static final String CRLF = "\n\r";
	public static final String HTTP_ONE_DOT_ONE = "HTTP/1.1";
	public static final String HTTP_ONE_DOT_ZERO = "HTTP/1.0";
	public static final String HTTP_CONTINUE = "100 Continue";
	public static final String HTTP_SWITCH_PROTOCOL = "101 Switching Protocols";
	
	public static final String HTTP_GOOD_REQUEST = "200 OK";
	public static final String HTTP_CREATED = "201 Created";
	public static final String HTTP_ACCEPTED = "202 Accepted";
	public static final String HTTP_NON_AUTHORITATIVE = "203 Non-Authoritative Information";
	public static final String HTTP_NO_CONTENT = "204 No Content";
	public static final String HTTP_RESET_CONTENT = "205 Reset Content";
	public static final String HTTP_PARTIAL_CONTENT = "206 Partial Content";
	public static final String HTTP_MULTIPLE_CHOICES = "300 Multiple Choices";
	public static final String HTTP_MOVED_PERMANENTLY = "301 Moved Permanently";
	public static final String HTTP_REDIRECT = "302 Redirect";
	public static final String HTTP_SEE_OTHER = "303 See Other";
	public static final String HTTP_NOT_MODIFIED = "304 Not Modified";
	public static final String HTTP_USE_PROXY = "305 Use Proxy";
	public static final String HTTP_TEMP_REDIRECT = "307 Temporary Redirect";	
	
	public static final String HTTP_BAD_REQUEST = "400 Bad Request";
	public static final String HTTP_UNAUTHORIZED = "401 Unauthorized";
	public static final String HTTP_PAYMENT_REQUIRED = "402 Payment Required";
	public static final String HTTP_FORBIDDEN = "403 Forbidden";
	public static final String HTTP_NOT_FOUND = "404 Not Found";
	public static final String HTTP_METHOD_NOT_ALLOWED = "405 Method Not Allowed";
	public static final String HTTP_NOT_ACCEPTABLE = "406 Not Acceptable";
	public static final String HTTP_PROXY_REQUIRED = "407 Proxy Authentication Required";
	public static final String HTTP_REQUEST_TIMEOUT = "408 Request Time-out";
	public static final String HTTP_CONFLICT = "409 Conflict";
	public static final String HTTP_GONE = "410 Gone";
	public static final String HTTP_LENGTH_REQUIRED = "411 Length Required";
	public static final String HTTP_REQUEST_ENTITY_LARGE = "413 Request Entity Too Large";
	public static final String HTTP_REQUEST_URI_LARGE = "414 Request-URI Too Large";
	public static final String HTTP_UNSUPPORTED_MEDIA = "415 Unsipported Media Type";
	public static final String HTTP_REQUESTED_RANGE_BAD = "416 Requested range not satisfiable";
	public static final String HTTP_EXPECTATION_FAILED = "417 Expectation Failed";
	
	public static final String HTTP_INTERNAL_ERROR = "500 Internal Server Error";
	public static final String HTTP_METHOD_NOT_IMPLEMENTED = "501 Method Not Implemented";
	public static final String HTTP_BAD_GATEWAY = "502 Bad Gateway";
	public static final String HTTP_SERVICE_UNAVAILABLE = "503 Service Unavailable";
	public static final String HTTP_GATEWAY_TIMEOUT = "504 Gateway Time-out";
	public static final String HTTP_BAD_VERSION = "505 HTTP Version not supported";
	
	//Headers
	public static final String DATE_HEADER = "Date";
	public static final String SERVER_HEADER = "Server";
	public static final String CONTENT_LENGTH_TYPE = "Content-Type";
	public static final String CONTENT_LENGTH_HEADER = "Content-Length";
	public static final String COOKIE = "cookie";
	public static final String SET_COOKIE = "Set-Cookie";
	public static final String COOKIE_JSESSIONID = "JSESSIONID";
	public static final String LAST_MODIFIED_HEADER = "Last-Modified";
	public static final String MODIFIED_SINCE_HEADER = "if-modified-since";
	public static final String UNMODIFIED_SINCE_HEADER = "if-unmodified-since";
	
	//Reponse
	public static final String HTTP_CLEAR_CACHE="<script type='text/javascript'>setTimeout(function() { location.reload(true); }, 5000);</script>";
	public static final String HTTP_RESPONSE_START = "<!DOCTYPE html>" 
																										+"<html>"
																										+"<head>"
																										+"<title>Server Message</title>"
																										+"</head>"
																										+"<body>";
	
	public static final String HTTP_RESPONSE_END = "</body></html>";
	public static final String HTTP_BAD_REQUEST_BODY= HTTP_RESPONSE_START
																										+"<h1>"+HTTP_BAD_REQUEST+"</h1>"
																										+"<p>The HTTP Request does not fit HTTP standard</p>"
																										+HTTP_RESPONSE_END;
	
	public static final String HTTP_BAD_METHOD_BODY = HTTP_RESPONSE_START
																										+"<h1>"+HTTP_METHOD_NOT_ALLOWED+"</h1>"
																										+"<p>Method is not correct</p>"
																										+HTTP_RESPONSE_END;
	
	public static final String HTTP_BAD_VERSION_BODY = HTTP_RESPONSE_START
																										+"<h1>"+HTTP_BAD_VERSION+"</h1>"
																										+"<p>Http version is not correct</p>"
																										+HTTP_RESPONSE_END;
	
	public static final String HTTP_SHUTDOWN = HTTP_RESPONSE_START
																										+"<h1>The server is shutting down</h1>"
																										+HTTP_RESPONSE_END;
	
	public static final String HTTP_CONTROL = "<!DOCTYPE html>"
																										+"<html>"
																										+"<head>"
																										+"<title>Control Panel</title>"
																										+"</head>"
																										+"<body>"
																										+"<h1>Zeyin Wu</h1>"
																										+"<table>"
																										+"<tr>"
																										+"<th>Thread</th>"
																										+"<th>Status</th>"
																										+"</tr>";
	
	//Date type
	public static final String STANDARD_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
	public static final String ALTERNATE_DATE_FORMAT = "EEEE, d-MMM-yy HH:mm:ss z";
	public static final String ALTERNATE_DATE_FORMAT2 = "EEE MMM d HH:mm:ss yyyy";
	public static final String STANDARD_TIMEZONE = "GMT";
	
	public static final String DEFAULT_CONTENT_TYPE = "text/html";
	public static final String DEFAULT_SERVER_NAME = "ZEYINWU-CIS555-HW1";
	
	
	public static String parseDate(java.util.Date date){ //Use it in response
		SimpleDateFormat sdf1 = new SimpleDateFormat(STANDARD_DATE_FORMAT);		
		sdf1.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		SimpleDateFormat sdf2 = new SimpleDateFormat(ALTERNATE_DATE_FORMAT);
		sdf2.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		SimpleDateFormat sdf3 = new SimpleDateFormat(ALTERNATE_DATE_FORMAT2);
		sdf3.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		String d = "";
		
		try{			
			d = sdf1.format(date);
		}catch(Exception e1){
			try{
				d = sdf2.format(date);
			}catch(Exception e2){
				try{
					d = sdf3.format(date);
				}catch(Exception e3){
					log.info("Wrong format of date.");
				}
			}			
		}
		return d;
	}
	
	public static Date parseStringToDate(String s){
		SimpleDateFormat sdf1 = new SimpleDateFormat(STANDARD_DATE_FORMAT);		
		sdf1.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		SimpleDateFormat sdf2 = new SimpleDateFormat(ALTERNATE_DATE_FORMAT);
		sdf2.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		SimpleDateFormat sdf3 = new SimpleDateFormat(ALTERNATE_DATE_FORMAT2);
		sdf3.setTimeZone(TimeZone.getTimeZone(STANDARD_TIMEZONE));
		Date date = null;
		try{
			date = (Date) sdf1.parse(s);
		}catch(Exception e0){
			try{
				date = (Date) sdf2.parse(s);
			}catch(Exception e1){
				try{
					date = (Date) sdf3.parse(s);
				}catch(Exception e2){
					e2.printStackTrace();
					log.info("Wrong format of String to Date");
				}
			}
		}
		
		return date;
	}
	
}
