package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import com.uty.shogiapp.websocketClients.BattleClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;

public class Battle extends Activity {
	private GridLayout boardGrid;
	private GridLayout myMochikomaGrid;
	private GridLayout yourMochikomaGrid;
	private GridLayout myMochikomaSuGrid;
	private GridLayout yourMochikomaSuGrid;
	private BattleClient client;
	private String order;
	private Button cancelButton;
	private WebSocket ws;
	private Intent nextIntent;
	private int ticketNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		System.out.println("Battleがはじまります");
		System.out.println(findViewById(R.id.resign));
		cancelButton = (Button)findViewById(R.id.resign);
		nextIntent = getIntent();
		order = nextIntent.getStringExtra("order");
		ticketNum = Integer.valueOf(nextIntent.getStringExtra("ticketNum"));
		if(order.equals("先手")){
			System.out.println("あなたは先手");
			//自分の名前
			TextView myName = (TextView) findViewById(R.id.myNameText);
			myName.setText(nextIntent.getStringExtra("senteName"));
			//自分のアバター
			ImageView myAvatar = (ImageView) findViewById(R.id.myAvatarImage);
			myAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("senteAvatar"))));
			//自分の級
			TextView myClazz = (TextView) findViewById(R.id.myClazzText);
			myClazz.setText(nextIntent.getStringExtra("senteClazz"));

			//相手の名前
			TextView yourName = (TextView) findViewById(R.id.yourNameText);
			yourName.setText(nextIntent.getStringExtra("goteName"));
			//相手のアバター
			ImageView yourAvatar = (ImageView) findViewById(R.id.yourAvatarImage);
			yourAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("goteAvatar"))));
			//相手の級
			TextView yourClazz = (TextView) findViewById(R.id.yourClazzText);
			yourClazz.setText(nextIntent.getStringExtra("goteClazz"));
		}else if(order.equals("後手")){
			System.out.println("あなたは後手");
			//自分の名前
			TextView myName = (TextView) findViewById(R.id.myNameText);
			myName.setText(nextIntent.getStringExtra("goteName"));
			//自分のアバター
			ImageView myAvatar = (ImageView) findViewById(R.id.myAvatarImage);
			myAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("goteAvatar"))));
			//自分の級
			TextView myClazz = (TextView) findViewById(R.id.myClazzText);
			myClazz.setText(nextIntent.getStringExtra("goteClazz"));

			//相手の名前
			TextView yourName = (TextView) findViewById(R.id.yourNameText);
			yourName.setText(nextIntent.getStringExtra("senteName"));
			//相手のアバター
			ImageView yourAvatar = (ImageView) findViewById(R.id.yourAvatarImage);
			yourAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(nextIntent.getStringExtra("senteAvatar"))));
			//相手の級
			TextView yourClazz = (TextView) findViewById(R.id.yourClazzText);
			yourClazz.setText(nextIntent.getStringExtra("senteClazz"));
		}
		boardGrid = (GridLayout)findViewById(R.id.boardGrid);
		myMochikomaGrid = (GridLayout)findViewById(R.id.myMochikomaGrid);
		yourMochikomaGrid = (GridLayout)findViewById(R.id.yourMochikomaGrid);
		myMochikomaSuGrid = (GridLayout)findViewById(R.id.myMochikomaSuGrid);
		yourMochikomaSuGrid = (GridLayout)findViewById(R.id.yourMochikomaSuGrid);
		client = new BattleClient();
		System.out.println("AsynkTaskをexecute");
		client.execute(this);
		System.out.println("onCreate終了");
	}

	public void showDisconecctedDialog(){
    	new AlertDialog.Builder(this)
        .setTitle("接続が切れました")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(Battle.this, MainActivity.class);
            	startActivity(intent);
            }
        })
        .show();
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

	/**
	 * boardGridを取得します。
	 * @return boardGrid
	 */
	public GridLayout getBoardGrid() {
	    return boardGrid;
	}

	/**
	 * myMochikomaGridを取得します。
	 * @return myMochikomaGrid
	 */
	public GridLayout getMyMochikomaGrid() {
	    return myMochikomaGrid;
	}

	/**
	 * yourMochikomaGridを取得します。
	 * @return yourMochikomaGrid
	 */
	public GridLayout getYourMochikomaGrid() {
	    return yourMochikomaGrid;
	}


	/**
	 * myMochikomaSuGridを取得します。
	 * @return myMochikomaSuGrid
	 */
	public GridLayout getMyMochikomaSuGrid() {
	    return myMochikomaSuGrid;
	}

	/**
	 * yourMochikomaSuGridを取得します。
	 * @return yourMochikomaSuGrid
	 */
	public GridLayout getYourMochikomaSuGrid() {
	    return yourMochikomaSuGrid;
	}

	/**
	 * orderを取得します。
	 * @return order
	 */
	public String getOrder() {
	    return order;
	}

	/**
	 * cancelButtonを取得します。
	 * @return cancelButton
	 */
	public Button getCancelButton() {
	    return cancelButton;
	}

	/**
	 * wsを取得します。
	 * @return ws
	 */
	public WebSocket getWs() {
		return ws;
	}

	/**
	 * wsを設定します。
	 * @param ws ws
	 */
	public void setWs(WebSocket ws) {
		this.ws = ws;
	}

	/**
	 * nextIntentを取得します。
	 * @return nextIntent
	 */
	public Intent getNextIntent() {
		return nextIntent;
	}

	/**
	 * ticketNumを取得します。
	 * @return ticketNum
	 */
	public int getTicketNum() {
	    return ticketNum;
	}

	/**
	 * ticketNumを設定します。
	 * @param ticketNum ticketNum
	 */
	public void setTicketNum(int ticketNum) {
	    this.ticketNum = ticketNum;
	}
}
