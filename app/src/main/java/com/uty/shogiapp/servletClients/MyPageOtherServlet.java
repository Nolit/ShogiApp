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
import java.util.Iterator;
import java.util.List;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.MyPageOther;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;



//型パラメータは左から
//doInBackgroundの引数型,他のメソッドで使うらしい型,onPostExecuteの引数型
public class MyPageOtherServlet extends AsyncTask<MyPageOther,Void,List<String>>{
	MyPageOther myPageOther;
	private String userName = null;			//参照したい他人ページのユーザー名
	private String myId = null;				//アクティビティを操作するユーザー名
	private boolean requestButton = false;	//友達申請/解除ボタンが押されたかを判定する

	public MyPageOtherServlet(){
	}

	public MyPageOtherServlet(String myId, String userName){
		this.myId = myId;
		this.userName = userName;		//参照したい他人ページのユーザー名を代入する
		System.out.println("他人ページサーブレット:ユーザーID("+myId+")から("+userName+")様のページリクエスト");
	}

	public MyPageOtherServlet(String myId, String userName, boolean requestButton){
		this.myId = myId;
		this.userName = userName;		//参照したい他人ページのユーザー名を代入する
		this.requestButton = requestButton;
		System.out.println("他人ページサーブレット:ユーザーID("+myId+")から("+userName+")様へ友達申請/解除のリクエスト");
		/* 注意: myIdが[guest]の場合の処理が未指定 */
	}

	//execute()でこのメソッドが呼び出される
	@Override
	protected List<String> doInBackground(MyPageOther ... myPageOther) {
		this.myPageOther = myPageOther[0];
		List<String> userInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"MyPageOtherPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "myId="+myId+"&userName="+userName;
                if(requestButton){
                	//友達申請/解除ボタンが押されていた場合、パラメータを修正する
                	parameter = (parameter + "&requestButton=申請");
                }

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
        //userInfoを引数にonPostExecuteを呼び出す
        return userInfo;
	}

	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> userInfo) {
        Iterator<String> it = userInfo.iterator();

        //デバッグ用レスポンス内容全表示
		for(String str:userInfo)
			System.out.println("デバッグ:MyPageOtherServlet:受け取った情報:"+str);

		//アクティビティのビューの書き換え
		myPageOther.getUserName().setText(it.next());		//ユーザー名

		//アバター
		try {
			Class<?> clazz = Class.forName("tt4.syogi.R$drawable");
			Field f = clazz.getField(it.next());
			myPageOther.getAvatar().setImageBitmap(BitmapFactory.decodeResource(myPageOther.getResources(), Integer.valueOf(f.getInt(null))));
		} catch (Exception e) {
			e.printStackTrace();
		}

		myPageOther.getUserTitle().setText(it.next());		//称号
		myPageOther.getClassPosition().setText(it.next());	//級位
		myPageOther.getRate().setText(it.next());			//レート

		//対局回数、戦績、勝率
		String battleNum,win,lose,draw,winPercentage;
		win = it.next();
		lose = it.next();
		draw = it.next();
		battleNum = myPageOther.makeBattleNum(win,lose,draw);					//対局回数の作成
//		winPercentage = MyPageOther.makeWinPercentage(battleNum,win);			//勝率の作成

		myPageOther.getBattleNum().setText(battleNum+"戦");
		myPageOther.getBattleRecord().setText(win+"勝"+lose+"敗"+draw+"分");	//勝利数、敗北数、引き分け数
//		myPageOther.getWinPercentage().setText(winPercentage);


		myPageOther.getStreakNum().setText(it.next());				//ストリーク数
		myPageOther.getMaxStreakNum().setText(it.next()+"連勝");	//最高連続勝利数
		myPageOther.getDisconnectedNum().setText(it.next()+"回");	//接続切れ回数

		myPageOther.getFriendRequestButton().setText(it.next());

    }

}
