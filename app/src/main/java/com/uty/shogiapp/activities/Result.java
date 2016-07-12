package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import com.uty.shogiapp.websocketClients.DiscuttionSelectClient;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends Activity{
	//コンポーネント(レイアウト配置順)
	//先手
	private TextView senteClazz;	//級
	private TextView senteName;		//ユーザー名
	private ImageView senteAvatar;	//アバター番号
	private TextView senteTitle;	//称号名
	private TextView senteResult;	//対局結果

	//後手
	private TextView goteClazz;		//級
	private TextView goteName;		//ユーザー名
	private ImageView goteAvatar;	//アバター番号
	private TextView goteTitle;		//称号名
	private TextView goteResult;	//対局結果

	//ボタン
	private Button backButton;

	//感想戦ダイアログ
	private CustomDialog dlg;

	//ウェブソケットクライアント
	DiscuttionSelectClient client;

	private int ticketNum;

	private Intent nextIntent;


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//インテントの取得
		nextIntent = getIntent();

		//xmlの読み込み
		setContentView(R.layout.result);

		ticketNum = Integer.valueOf(nextIntent.getStringExtra("ticketNum"));

		//コンポーネントの取得
		//先手
		senteClazz = (TextView)findViewById(R.id.classText1);
		senteName = (TextView)findViewById(R.id.nameText1);
		senteAvatar = (ImageView)findViewById(R.id.avatarImage1);
		senteTitle = (TextView)findViewById(R.id.titleText1);
		senteResult = (TextView)findViewById(R.id.winText);

		//後手
		goteClazz = (TextView)findViewById(R.id.classText2);
		goteName = (TextView)findViewById(R.id.nameText2);
		goteAvatar = (ImageView)findViewById(R.id.avatarImage2);
		goteTitle = (TextView)findViewById(R.id.titleText2);
		goteResult = (TextView)findViewById(R.id.loseText);

		//ボタン
		backButton = (Button)findViewById(R.id.backButton);

		//リスナー
		backButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Result.this,MainActivity.class);
				startActivity(intent);
			}
		});

		//ビューの更新
		//先手
		senteClazz.setText(nextIntent.getStringExtra("senteClazz"));
		senteName.setText(nextIntent.getStringExtra("senteName"));
		senteAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("senteAvatar"))));
		senteTitle.setText(nextIntent.getStringExtra("senteTitle"));

		//後手
		goteClazz.setText(nextIntent.getStringExtra("goteClazz"));
		goteName.setText(nextIntent.getStringExtra("goteName"));
		goteAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("goteAvatar"))));
		goteTitle.setText(nextIntent.getStringExtra("goteTitle"));

		//対局結果
		if(nextIntent.getBooleanExtra("isSenteWin", true)){		//勝者が先手の時にtrue、キーがなければtrue
			senteResult.setText("勝利");
			goteResult.setText("敗北");
		}else{
			senteResult.setText("敗北");
			goteResult.setText("勝利");
		}

		if(!(nextIntent.getStringExtra("senteName").equals("guest") || nextIntent.getStringExtra("goteName").equals("guest"))){
			dlg = new CustomDialog(this, R.style.Theme_Dialog);
			dlg.getOk().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.getOk().setEnabled(false);
					dlg.getCancel().setEnabled(false);
					client = new DiscuttionSelectClient();
					client.execute(Result.this);
					dlg.getTv().setText("対戦相手を待っています");
				}
			});
			dlg.getCancel().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
				}
			});
	        dlg.setShowTime(15000);
	        dlg.show();
		}
	    System.out.println("Result_onCreate()終了");
	}	//--------------終了:onCreate

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

	/**
	 * dlgを取得します。
	 * @return dlg
	 */
	public CustomDialog getDlg() {
	    return dlg;
	}

	/**
	 * ticketNumを取得します。
	 * @return ticketNum
	 */
	public int getTicketNum() {
	    return ticketNum;
	}

	/**
	 * nextIntentを取得します。
	 * @return nextIntent
	 */
	public Intent getNextIntent() {
	    return nextIntent;
	}

}
