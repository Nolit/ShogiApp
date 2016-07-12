package com.uty.shogiapp.servletClients;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

import com.uty.shogiapp.settings.ServerConfig;

public class HavingAvatarUpdater extends AsyncTask<Void,Void,Void>{

	private String userId;
	private String pictPath;

	public HavingAvatarUpdater(String userId,String pictPath){
		this.userId = userId;
		this.pictPath = pictPath;
	}

	@Override
	protected Void doInBackground(Void... params) {
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(ServerConfig.URL+"HavingAvatarUpdate");
			connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
		}catch(IOException e){
			e.printStackTrace();
		}
		try {
	    //ユーザーIDとアバターの画像パスを送信して現在装着中のアバターを変える
	        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
	        printWriter.print("id="+userId);
	        printWriter.print("&");
	        printWriter.print("pictPath="+pictPath);
	        printWriter.close();
	        //リクエスト
	        connection.getResponseCode();
			System.out.println("デバッグ用:HavingAvatarUpdater.java:id="+userId+" pictPath="+pictPath);
	    } catch (IOException e) {
			e.printStackTrace();
		}finally {
	        if (connection != null) {
	            connection.disconnect();
	        }
		}

		return null;
	}
}
