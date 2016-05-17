package com.uty.shogi.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.uty.shogi.R;
import com.uty.shogi.servletClients.MainActivityServlet;

public class MainActivity extends Activity {

	String id = null;
	String connectionLossNum = null;

	//コンポーネント
	TextView applicationTitleName;
	Button battleStartButton;
	Button myPageButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("MainActivity始め！");
		super.onCreate(savedInstanceState);

		//tt4プリファレンス生成
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);

		//デバッグ用処理:プリファレンスに異常な値が入った場合はここのコメントアウトを外す
		/* データベースに存在しないIDがプリファレンスに入った場合はエラーが起きる */
//		Editor editor = pref.edit();
//		editor.remove("id");
//		editor.commit();
//		System.out.println("重要！:MainActivity:デバッグ用処理:プリファレンスのIDを強制的に削除しました。IDを削除したくない場合はコメントアウトをしてください。");


		//プリファレンスからid取得
		String id = pref.getString("id","guest");
		System.out.println(id);

		//xmlの読み込み
		setContentView(R.layout.activity_main);

		//コンポーネントの取得
		battleStartButton = (Button)findViewById(R.id.battleStart);
		myPageButton = (Button)findViewById(R.id.intentMyPage);

		//ログイン状態とゲスト状態の分岐処理
		if(id.equals("guest")){
			/* ---------------------------------ゲストの処理--------------------------------- */
			System.out.println("プリファレンスにIDが保存されていません。id["+ id +"]で実行します。");

			//ビューの更新
			battleStartButton.setText("ゲストで対局");
			myPageButton.setText("ログイン");

			//リスナー
			//ゲストモードで対局開始
			battleStartButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
	               Intent intent = new Intent(MainActivity.this,Matching.class);
	               intent.putExtra("id", "guest");
	               startActivity(intent);
	           }
	        });

			//会員登録ページへ遷移
			myPageButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	               Intent intent = new Intent(MainActivity.this,Login.class);
	               startActivity(intent);
	           }
	        });


		}else{
			/* ---------------------------------会員の処理--------------------------------- */
			System.out.println("プリファレンスにIDが保存されていました。id["+ id +"]で実行します。");

			//接続切れ回数の更新
//			new MainActivityServlet(id).execute(this);

			//ビューの更新
			battleStartButton.setText("対局開始");
			myPageButton.setText("マイページ");

			//リスナー
			//対局開始
			battleStartButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					System.out.println("本日の接続切れ回数は"+connectionLossNum+"回です。");
					if(Integer.parseInt(connectionLossNum) <= 10){
						Intent intent = new Intent(MainActivity.this,Matching.class);
						startActivity(intent);
					}else{
						/* ここでアラート */
						AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
						alert.setTitle("ペナルティ");
						alert.setMessage("1日の接続切れ回数の限度を超えました。" + "\n" + "本日は対局出来ません。");
						alert.setPositiveButton("OK", null);
						alert.show();		//ダイアログ表示
					}
	           }
	        });

			//マイページへ遷移
			myPageButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(MainActivity.this,MyPage.class);
	            	startActivity(intent);
	           }
	        });

		}	//-----------終了:ログイン状態とゲスト状態の分岐処理

	}

	public String getId() {
		return id;
	}

	public void setConnectionLossNum(String connectionLossNum) {
		this.connectionLossNum = connectionLossNum;
	}

   @Override
   public boolean dispatchKeyEvent(KeyEvent event) {
       if (event.getAction()==KeyEvent.ACTION_DOWN) {
           switch (event.getKeyCode()) {
           case KeyEvent.KEYCODE_BACK:
               // ダイアログ表示など特定の処理を行いたい場合はここに記述
               // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
               return true;
           }
       }
       return super.dispatchKeyEvent(event);
   }

   @Override
   public void onStart(){
	   super.onStart();
	   System.out.println("MainActivity:onStart()...");
   }
   @Override
   public void onResume(){
	   super.onResume();
	   System.out.println("MainActivity:onResume()...");
   }
   @Override
   public void onStop(){
	   super.onStop();
	   System.out.println("MainActivity:onStop()...");
   }

}


