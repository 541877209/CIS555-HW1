package edu.upenn.cis.cis455.webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import javafx.scene.shape.Path;

public class HttpMetaData {

	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;
	private String root;//get this from argument input
	private String version;
	private HashMap<String, ArrayList<String>> headersMap;
	private String method;
	private String filePath;
	private String pathInfo;
	private String queryString;
	private String servlet_path;
	private String messageBody;
	private boolean badMethod;
	private boolean shutDown;
	private boolean control;
	private boolean badVersion;
	private boolean badRequest;
	private boolean version1;
	
	//headers map
	public HashMap<String, ArrayList<String>> headerMaps; 
	
	
	public HttpMetaData(Socket socket, BufferedReader in, DataOutputStream out, String root) throws Exception {
		setSocket(socket);
		setInStream(in);
		setOutputStream(out);	
		setRoot(root);
		
		
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public void setSocket(Socket s){
		this.socket = s;
	}
	
	public String getMethod(){
		return method;
	}
	
	public void setMethod(String m){
		this.method = m;
	}
	
	public BufferedReader getInStream(){
		return in;
	}
	
	public void setInStream(BufferedReader re){ //input stream data is in socket
		this.in = re;
		
	}
	
	public DataOutputStream getOutputStream(){ //output stream data will be sent back
		return out;
	}
	
	public void setOutputStream(DataOutputStream ou){
		this.out = ou;
	}
	
	public String getRoot(){
		return root;
	}
	
	public void setRoot(String r){
		this.root = r;
	}
	
	public String getVersion(){
		return version;
	}
	
	public void setVersion(String v){
		this.version = v;
	}
	
	public String getMessageBody(){
		return messageBody;
	}
	
	public void setMessageBody(String s){
		this.messageBody = s;
	}
	
	
	public HashMap<String, ArrayList<String>> getHeadersMap(){
		return headersMap;
	}
	
	public void setHeadersMap(HashMap<String, ArrayList<String>> map){
		this.headersMap = map;		
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	public void setFilePath(String path){
		this.filePath = path;
	}
	
	public String getPathInfo(){
		return pathInfo;
	}
	
	public void setPathInfo(String s){
		pathInfo = s;
	}
	
	public String getQueryString(){
		return queryString;
	}
	
	public void setQueryString(String s){
		queryString = s;
	}
	
	public String getServletPath(){
		return servlet_path;
	}
	
	public void setServletPath(String s){
		servlet_path = s;
	}
	
	
	public boolean isBadMethod(){
		return badMethod;
	}
	
	public void setBadMethod(boolean isbad){
		this.badMethod = isbad;
	}
	
	public boolean isShutDown(){
		return shutDown;
	}
	
	public void setShutDown(boolean is){
		this.shutDown = is;
	}
	
	public boolean isControl(){
		return control;
	}
	
	public void setControl(boolean is){
		this.control = is;
	}
	
	public boolean isBadVersion(){
		return this.badVersion;
	}
	
	public void setBadVersion(boolean is){
		this.badVersion = is;
	}
	
	public boolean isBadRequest(){
		return this.badRequest;
	}
	
	public void setBadRequest(boolean is){
		this.badRequest = is;
	}

	public boolean isVersion1(){
		return version1;
	}
	
	public void setVersion1(boolean is){
		this.version1 = is;
	}
	
	
//	public static void main(String[] args) throws IOException, TikaException{
//		Thread th = new Thread();
//		th.start();
//		Set<Thread> set = Thread.getAllStackTraces().keySet();
//		for(Thread s : set){
//			System.out.println(s.getName());
//		}
//		
//
////		th.interrupt();
//
////		System.out.println("aa"+"\r\n"+"bb");
//		
//		
//	}
	
}
