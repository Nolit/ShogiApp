package com.uty.shogiapp.servletClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.Avatar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


//型パラメータは左から
//doInBackgroundの引数型,他のメソッドで使うらしい型,onPostExecuteの引数型
public class AvatarServlet extends AsyncTask<Avatar,Void,List<String>>{
	Avatar avatar;

	//execute()でこのメソッドが呼び出される
	@Override
	protected List<String> doInBackground(Avatar ... avatar) {
		this.avatar = avatar[0];
		List<String> avatarInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"AvatarPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                //パラメータを付ける
                //サーブレットで使う名前=渡す値
                String parameter = "id="+this.avatar.getUserId();
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
                        avatarInfo.add(line);
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
        //avatarInfoを引数にonPostExecuteを呼び出す
        return avatarInfo;
	}

	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> avatarInfo) {

        //デバッグ用レスポンス内容全表示
		for(String str:avatarInfo)
			System.out.println("デバッグ:AvatarServlet:受け取った情報:"+str);

		//Avatarアクティビティのビューの書き換え
		try {
			Class<?> clazz = Class.forName("tt4.syogi.R$drawable");
			Field f,noImage;
			f = clazz.getField(avatarInfo.get(0));
			noImage = clazz.getField("res_noimage");

			//装着中のアバター更新
			Bitmap bm = BitmapFactory.decodeResource(avatar.getResources(), Integer.valueOf(f.getInt(null)));
			avatar.getIvHold().setImageBitmap(bm);

			//アバター一覧更新
			for(int i=1; i<avatarInfo.size(); i++){		//アバター一覧更新
				f = clazz.getField(avatarInfo.get(i));
				bm = BitmapFactory.decodeResource(avatar.getResources(), Integer.valueOf(f.getInt(null)));
				System.out.println("Reflect:"+f.getInt(null));
				avatar.getImageView().get(i-1).setImageBitmap(bm);

				if(noImage.getInt(null) == f.getInt(null)){		//noImage画像の場合分岐
					avatar.addNoImageList(true);	//noImage画像ならtrue
				}else{
					avatar.addNoImageList(false);	//アバター画像ならfalse
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


    }

}
