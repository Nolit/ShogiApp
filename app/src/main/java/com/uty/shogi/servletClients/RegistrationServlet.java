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

import com.uty.shogi.activities.MainActivity;
import com.uty.shogi.activities.Registration;
import com.uty.shogi.settings.ServerConfig;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class RegistrationServlet extends AsyncTask<Registration,Void,List<String>>{
	Registration registration;

	private String userName;
	private String mailAddress;
	private String password;
	private String confirmPassword;

	public RegistrationServlet(){
	}

	public RegistrationServlet(String userName, String mailAddress, String password, String confirmPassword){
		this.userName = userName;
		this.mailAddress = mailAddress;
		this.password = password;
		this.confirmPassword = confirmPassword;

		System.out.println("userName=["+userName+"],mailAddress=["+ mailAddress +"],password=["+password+"],confirmPassword=["+ confirmPassword +"]が送られてきました。");
	}

	@Override
	protected List<String> doInBackground(Registration ... registration) {
		this.registration = registration[0];
		List<String> userInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"RegistrationPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //パラメータの作成
                String parameter = ("userName="+userName
                					+"&mailAddress="+mailAddress
                					+"&password="+password
                					+"&confirmPassword="+confirmPassword);
                System.out.println("parameterの内容"+parameter);
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
			System.out.println("デバッグ:RegistrationServlet:受け取った情報:"+str);

		//アクティビティのビューの書き換え
		registration.getResultText().setText(userInfo.get(0));

   		//会員登録に成功した場合はTopページに自動で遷移する
		if(registration.getResultText().getText().equals("登録成功！")){

			//会員登録成功時にプリファレンスに新しいユーザーIDを書き込む
			SharedPreferences pref = registration.getSharedPreferences("tt4",Activity.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putString("id",userInfo.get(1));
			editor.commit();
			System.out.println("プリファレンスにid["+userInfo.get(1)+"]を書き込みました。");

			System.out.println("デバッグ:Registration:MainActivity.javaにインテントします");
            Intent intent = new Intent(registration,MainActivity.class);
            registration.startActivity(intent);

		}

    }

}
