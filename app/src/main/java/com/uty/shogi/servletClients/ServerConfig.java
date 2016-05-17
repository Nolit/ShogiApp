package com.uty.shogi.servletClients;

public class ServerConfig {
	//学校用URL
	public static final String URL = "http://172.24.46.105:8080/Server/";	//HTTP
	public static final String URL2 = "ws://172.24.46.105:8080/Server/";	//WebSocket

	//ローカル用URL
//	public static final String URL = "http://10.0.2.2:8080/Server/";
//	public static final String URL2 = "ws://10.0.2.2:8080/Server/";	//WebSocket

//	//俺のMAC
//	public static final String URL = "http://169.254.118.73:8080/Server/";	//HTTP
//	public static final String URL2 = "ws://169.254.118.73:8080/Server/";	//WebSocket
	private ServerConfig(){
	}
}
