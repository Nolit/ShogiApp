package com.uty.shogiapp.servletClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.MainActivity;


//型パラメータは左から
//doInBackgroundの引数型,他のメソッドで使うらしい型,onPostExecuteの引数型
public class MainActivityServlet extends AsyncTask<MainActivity,Void,List<String>>{
	MainActivity mainActivity;
	String userId = null;

	public MainActivityServlet(String userId){
		this.userId = userId;
	}

	//execute()でこのメソッドが呼び出される
	@Override
	protected List<String> doInBackground(MainActivity ... mainActivity) {
		this.mainActivity = mainActivity[0];
		List<String> userInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"MainActivityPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                //パラメータを付ける
                //サーブレットで使う名前=渡す値
                String parameter = "id="+userId;
                System.out.println("parameter→"+parameter);
                PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                printWriter.print(parameter);
                printWriter.close();
                //getResponseCode()実行時にリクエストを行う戻り値はHTTPステータスコード

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                	InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    String line;
                    //レスポンスを文字列で受け取る
                    while ((line = reader.readLine()) != null) {
                        userInfo.add(line);
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
	}

	@Override
    protected void onPostExecute(List<String> userInfo) {
		mainActivity.setConnectionLossCount(Integer.parseInt(userInfo.get(0)));		//ユーザー名
    }

}
