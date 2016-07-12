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

import java.nio.charset.StandardCharsets;
import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.TitleBuy;
import com.uty.shogiapp.activities.TitleBuyBean;
import android.os.AsyncTask;


public class TitleBuyServlet extends AsyncTask<TitleBuy,Void,List<String>>{
	TitleBuy titleBuy;
	private String purchaseTitleName = null;

	public TitleBuyServlet(){
	}

	public TitleBuyServlet(String purchaseTitleName){
		this.purchaseTitleName = purchaseTitleName;		//購入ボタンに対応する称号名を代入する
	}

	@Override
	protected List<String> doInBackground(TitleBuy ... titleBuy) {
		this.titleBuy = titleBuy[0];
		List<String> titleBuyInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {

        	URL url = new URL(ServerConfig.URL+"TitleBuyPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "id="+this.titleBuy.getUserId();
                if(purchaseTitleName != null){
                	parameter = (parameter + "&purchaseTitleNum="+purchaseTitleName);
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
                    	titleBuyInfo.add(line);
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

        return titleBuyInfo;
	}


	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> titleBuyInfo) {
		Iterator<String> it = titleBuyInfo.iterator();

        //デバッグ用レスポンス内容全表示
		for(String str:titleBuyInfo)
			System.out.println("デバッグ:TitleBuyServlet:受け取った情報:"+str);

		//TitleBuyアクティビティのビューの書き換え
		//所持マネー用テキストビューを更新
		titleBuy.setHaveMoney(it.next());
		titleBuy.getShowHaveMoney().setText("$"+titleBuy.getHaveMoney());		//テキストビュー所持金の更新


		//リストビューの項目を更新
		titleBuy.getTitleListView().clear();					//称号リストビューの初期化
		int titleNumCounter = Integer.parseInt(it.next());		//繰り返し回数を代入する
		String name = null;		//称号名
		String summary = null;	//条件
		String price = null;	//価格
		String buttonState = null;	//ボタンテキスト
		boolean buttonEnable = true;//ボタンEnable
		String satisfyQualifaction = null;	//購入可能条件を満たしているか
		for(int i=0; i<titleNumCounter; i++){
			name = it.next();
			summary = it.next();
			price = it.next();
			buttonState = it.next();
			satisfyQualifaction = it.next();

			//ボタンのEnableを更新する処理
			if(buttonState.equals("所持")){
				buttonState = "所持";
				buttonEnable = false;
				System.out.println("分岐1:buttonState→所持,buttonEnable→false");
			}else{
				if(titleBuy.checkPurchaseButton(price)){
					buttonState = "購入";
					buttonEnable = true;
					System.out.println("分岐2:buttonState→購入,buttonEnable→true");
					System.out.println(satisfyQualifaction);
					if(satisfyQualifaction.equals("NoSatisfy")){
						buttonState = "購入";
						buttonEnable = false;
						System.out.println("分岐3:buttonState→購入,buttonEnable→false");
					}
				}else{
					buttonState = "購入";
					buttonEnable = false;
					System.out.println("分岐4:buttonState→購入,buttonEnable→false");
				}
			}
			titleBuy.getTitleListView().add(new TitleBuyBean(name,summary,"$"+price,buttonState,buttonEnable));
		}

		titleBuy.updateAdapter();
    }

}
