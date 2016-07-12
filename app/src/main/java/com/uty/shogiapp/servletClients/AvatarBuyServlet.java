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

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.activities.AvatarBuy;
import android.os.AsyncTask;


public class AvatarBuyServlet extends AsyncTask<AvatarBuy,Void,List<String>>{
	AvatarBuy avatarBuy;
	private String purchaseAvatarNum = null;

	public AvatarBuyServlet(){
	}

	public AvatarBuyServlet(String purchaseAvatarNum){
		this.purchaseAvatarNum = purchaseAvatarNum;
	}

	@Override
	protected List<String> doInBackground(AvatarBuy ... avatarBuy) {
		this.avatarBuy = avatarBuy[0];
		List<String> avatarBuyInfo = new ArrayList<String>();
		System.out.println("===== HTTP POST Start =====");
        try {
        	URL url = new URL(ServerConfig.URL+"AvatarBuyPage");
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                //parameterの生成
                String parameter = "id="+this.avatarBuy.getUserId();
                if(purchaseAvatarNum != null){
                	parameter = (parameter + "&purchaseAvatarNum="+purchaseAvatarNum);
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
                        avatarBuyInfo.add(line);
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

        return avatarBuyInfo;
	}




	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(List<String> avatarBuyInfo) {

        //デバッグ用レスポンス内容全表示
		for(String str:avatarBuyInfo)
			System.out.println("デバッグ:AvatarBuyServlet:受け取った情報:"+str);

		//AvatarBuyアクティビティのビューの書き換え
		//所持マネー用テキストビューを更新
		avatarBuy.getShowHaveMoney().setText("$"+avatarBuyInfo.get(0));		//テキストビュー所持金の更新
		System.out.println("デバッグ:AvatarBuyServlet:所持金:"+avatarBuyInfo.get(0));
		avatarBuy.setHaveMoney(avatarBuyInfo.get(0));		//フィールドhaveMoneyを更新

		//すべての画像の値段を更新
		for(int i=1; i - 1<avatarBuy.getShowPictPriceList().size(); i++){
			System.out.println("デバッグ:AvatarBuyServlet:アバター値段:$"+avatarBuyInfo.get(i));
			avatarBuy.getShowPictPriceList().get(i-1).setText("$"+avatarBuyInfo.get(i));
		}

		//すべての画像に対応するボタンのステータスを更新
		String price = null;
		for(int i=9; i - 9<avatarBuy.getPurchaseButtonList().size(); i++){
			price = avatarBuyInfo.get(i-8);	//アバターの価格を代入
			if(avatarBuyInfo.get(i).equals("所持")){	//アバターを所持してる場合は真
				avatarBuy.getPurchaseButtonList().get(i-9).setEnabled(false);	//すでに所持しているアバターなのでボタンを無効化
				avatarBuy.getPurchaseButtonList().get(i-9).setText("所持");
				System.out.println("ボタンステータス更新処理で「所持」に分岐しました。");
			}else{
				avatarBuy.getPurchaseButtonList().get(i-9).setEnabled(avatarBuy.checkPurchaseButton(price));		//所持金が足りるかをチェック→足りなければボタンを無効化
				System.out.println("ボタンステータス更新処理で「未所持」に分岐しました。");
			}
		}


    }

}
