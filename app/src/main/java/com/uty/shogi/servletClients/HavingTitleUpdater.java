package com.uty.shogi.servletClients;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import com.uty.shogi.settings.ServerConfig;

//型パラメータは左から
//doInBackgroundの引数型,他のメソッドで使うらしい型,onPostExecuteの引数型
public class HavingTitleUpdater extends AsyncTask<Void,Void,Void>{
	private String userId;
	private String titleName;
	public HavingTitleUpdater(String userId,String titleName) {
		this.userId = userId;
		this.titleName = titleName;
	}

	//execute()でこのメソッドが呼び出される
	@Override
	protected Void doInBackground(Void... params) {
		URL url;
	    HttpURLConnection connection = null;
		try {
			url = new URL(ServerConfig.URL+"HavingTitleUpdate");
			connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
		}catch(IOException e){
			e.printStackTrace();
		}
	    try {
	    //userIdをタイトル番号を送信して現在付けているタイトルを変える
	        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
	        //パラメータが複数ある場合は&で繋ぐ
	        printWriter.print("id="+userId);
	        printWriter.print("&");
	        printWriter.print("titleName="+titleName);
	        printWriter.close();
	        //リクエスト
	        connection.getResponseCode();
	    } catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally {
	        if (connection != null) {
	            connection.disconnect();
	        }
	    }
	    return null;
	}
}