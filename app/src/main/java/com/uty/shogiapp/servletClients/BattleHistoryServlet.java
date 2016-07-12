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
import com.uty.shogiapp.activities.BattleHistory;
import com.uty.shogiapp.activities.BattleHistoryBean;
import android.os.AsyncTask;


public class BattleHistoryServlet extends AsyncTask<BattleHistory,Void,List<String>>{
	BattleHistory battleHistory;

	public BattleHistoryServlet(){
	}

	@Override
	protected List<String> doInBackground(BattleHistory ... battleHistory) {
		this.battleHistory = battleHistory[0];
		List<String> battleHistoryInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"BattleHistoryPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "id="+this.battleHistory.getUserId();

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
                    	battleHistoryInfo.add(line);
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

        return battleHistoryInfo;
	}


	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> battleHistoryInfo) {
		Iterator<String> it = battleHistoryInfo.iterator();

        //デバッグ用レスポンス内容全表示
		for(String str:battleHistoryInfo)
			System.out.println("デバッグ:battleHistoryServlet:受け取った情報:"+str);

		//battleHistoryアクティビティのビューの書き換え
		//ユーザー名を更新
		battleHistory.getShowUserName().setText(it.next());

		//リストビューの項目を更新
		battleHistory.getBattleHistoryListView().clear();	//対局履歴リストビューの初期化
		String userId = "ユーザーID";
		String opponentName = "対局相手名";
		String rate = "レート";
		String battleResult = "勝敗";
		String battleDate = "日時";
		String battleNum = "棋譜番号";

		//Intent用
	    String senteName = "先手の名前";
	    String goteName = "後手の名前";
	    String senteClass = "先手の級";
	    String goteClass = "後手の級";
	    String senteAvatarName = "先手のアバター名";
	    String goteAvatarName = "後手のアバター名";

		int counter = Integer.parseInt(it.next());
		for(int i=0; i<counter; i++){
			opponentName = it.next();
			rate = it.next();
			battleResult = it.next();
			battleDate = it.next();
			battleNum = it.next();

			senteName = it.next();
			goteName = it.next();
			senteClass = it.next();
			goteClass = it.next();
			senteAvatarName = it.next();
			goteAvatarName = it.next();

			battleHistory.getBattleHistoryListView().add(new BattleHistoryBean(userId,battleDate,opponentName,battleResult,rate,battleNum,senteName,goteName,senteClass,goteClass,senteAvatarName,goteAvatarName));
		}

		battleHistory.updateAdapter();
    }

}
