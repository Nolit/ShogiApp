package com.uty.shogi.servletClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.uty.shogi.activities.Login;
import com.uty.shogi.activities.MainActivity;
import com.uty.shogi.settings.ServerConfig;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class LoginServlet extends AsyncTask<Login,Void,List<String>>{

	Login login;


	private String userName;
	private String password;

	public LoginServlet(){
	}

	public LoginServlet(String userName, String password){
		this.userName = userName;
		this.password = password;

		System.out.println("userName="+userName+"とpassword="+password+"が送られてきました。");
	}

	@Override
	protected List<String> doInBackground(Login ... login) {
		this.login = login[0];
		List<String> userInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"LoginPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //パラメータの作成
                String parameter = ("userName="+userName+"&password="+password);
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
        //デバッグ用レスポンス内容全表示
		for(String str:userInfo)
			System.out.println("デバッグ:LoginServlet:受け取ったID:"+str);

		//アクティビティのビューの書き換え
		login.getResultText().setText(userInfo.get(0));

   		//ログインに成功した場合はTopページに自動で遷移する
		if(login.getResultText().getText().equals("ログイン成功！")){

			//ログイン成功時にプリファレンスにユーザーIDを書き込む
			SharedPreferences pref = login.getSharedPreferences("tt4",Activity.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putString("id",userInfo.get(1));
			editor.commit();
			System.out.println("プリファレンスにid["+userInfo.get(1)+"]を書き込みました。");

			System.out.println("デバッグ:Login:MainActivity.javaにインテントします");
            Intent intent = new Intent(login,MainActivity.class);
            login.startActivity(intent);

		}

    }


}
