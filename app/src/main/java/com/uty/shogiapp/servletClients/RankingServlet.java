package com.uty.shogiapp.servletClients;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.Ranking;
import com.uty.shogiapp.activities.RankingBean;
import android.os.AsyncTask;


public class RankingServlet extends AsyncTask<Ranking,Void,List<String>>{
	Ranking ranking;
	String rankingType;

	public RankingServlet(){
	}

	public RankingServlet(String rankingType){
		this.rankingType = rankingType;
	}

	@Override
	protected List<String> doInBackground(Ranking ... ranking) {
		this.ranking = ranking[0];
		List<String> rankingInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"RankingPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "id="+this.ranking.getUserId();
                parameter = (parameter + "&rankingType="+rankingType);

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
                    	rankingInfo.add(line);
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

        return rankingInfo;
	}


	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> rankingInfo) {
		Iterator<String> it = rankingInfo.iterator();

        //デバッグ用レスポンス内容全表示
		for(String str:rankingInfo)
			System.out.println("デバッグ:ランキングサーブレット:受け取った情報:"+str);

		//ランキングアクティビティのビューの書き換え
		//ユーザー名を更新
		ranking.getShowUserName().setText(it.next());

		//リストビューの項目を更新
		ranking.getRankingListView().clear();	//ランキングリストビューの初期化
		int rankingNum = 0;
		String rankerName = "ランカー名";
		String rankerTitleName = "ランカーの称号名";
		String rankerClassPosition = "ランカーの級";
		String rankerData = "ランキングデータ";

		int counter = Integer.parseInt(it.next());
		for(int i=0; i<counter; i++){
			rankingNum++;
			rankerName = it.next();
			rankerTitleName = it.next();
			rankerClassPosition = it.next();
			rankerData = it.next();

			ranking.getRankingListView().add(new RankingBean(Integer.toString(rankingNum),rankerName,rankerTitleName,rankerClassPosition,rankerData));
		}

		ranking.updateAdapter();

    }

}
