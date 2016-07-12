package com.uty.shogiapp.servletClients;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.Friend;
import com.uty.shogiapp.activities.FriendBean;
import android.os.AsyncTask;
import android.view.View;


public class FriendServlet extends AsyncTask<Friend,Void,List<String>>{
	Friend friend;
	String searchUser = null;

	public FriendServlet(){
	}

	public FriendServlet(String formSearch){
		this.searchUser = formSearch;
		System.out.println("FriendServlet:検索ボタンが押されました。検索ユーザーは:"+searchUser);
	}

	@Override
	protected List<String> doInBackground(Friend ... friend) {
		this.friend = friend[0];
		List<String> friendInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"FriendPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "id="+this.friend.getUserId();
                if(searchUser != null){
                	parameter = (parameter + "&searchUser=" + searchUser);
                }

                PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                printWriter.print(parameter);
                printWriter.close();

                //getResponseCode()実行時にリクエストを行う戻り値はHTTPステータスコード
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                	InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF_8");
                    BufferedReader reader = new BufferedReader(isr);
                    String line;
                    //レスポンスを文字列で受け取る
                    while ((line = reader.readLine()) != null) {
                    	friendInfo.add(line);
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

        return friendInfo;
	}


	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> friendInfo) {
		Iterator<String> it = friendInfo.iterator();

        //デバッグ用レスポンス内容全表示
		for(String str:friendInfo)
			System.out.println("デバッグ:FriendServlet:受け取った情報:"+str);

		//Friendアクティビティのビューの書き換え
		//ユーザー名を更新
		friend.getShowUserName().setText(it.next());

		/* ユーザー検索の更新 */
		String searchFlag = it.next();
		if(searchFlag.equals("検索成功")){
			friend.getResultLayout().setVisibility(View.VISIBLE);
			friend.getSearchFlag().setVisibility(View.GONE);
			friend.getSearchUserName().setText(it.next());
			friend.getSearchTitleName().setText(it.next());
			friend.getSearchClassPosition().setText(it.next());
			friend.getSearchRate().setText(it.next());
		}else if(searchFlag.equals("ユーザー名が存在しません。")){
			friend.getSearchFlag().setText(searchFlag);
			friend.getSearchFlag().setVisibility(View.VISIBLE);
		}else if(searchFlag.equals("自分のアカウントは検索できません。")){
			friend.getSearchFlag().setText(searchFlag);
			friend.getSearchFlag().setVisibility(View.VISIBLE);
		}else{
			// searchFlag = 未検索
			friend.getSearchFlag().setVisibility(View.GONE);
			friend.getResultLayout().setVisibility(View.GONE);
		}

		/* フレンドリストの更新 */
		int counter = Integer.parseInt(it.next());
		System.out.println("フレンド人数:"+counter);

		//リストビューの項目を更新
		friend.getFriendListView().clear();	//フレンドリストビューの初期化
		String friendName = "フレンド名";
		String titleName = "称号名";
		String classPosition = "級";
		String rate = "レート";
		String userNum = "フレンドユーザー番号";

		for(int i=0; i<counter; i++){
			friendName = it.next();
			titleName = it.next();
			classPosition = it.next();
			rate = it.next();
			userNum = it.next();

			System.out.println("作成するBean:"+friendName+","+titleName+","+classPosition+","+rate+","+userNum);
			friend.getFriendListView().add(new FriendBean(friendName,titleName,classPosition,rate,userNum));
		}

		friend.updateAdapter();
    }

}
