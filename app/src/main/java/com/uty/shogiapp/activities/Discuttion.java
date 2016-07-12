package com.uty.shogiapp.activities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;

import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.shogi.Player;
import com.uty.shogiapp.R;
import com.uty.shogiapp.websocketClients.DiscuttionClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.neovisionaries.ws.client.WebSocket;

public class Discuttion extends Activity {
	private TextView myName;
	private ImageView myAvatar;
	private TextView myClazz;
	private TextView yourName;
	private ImageView yourAvatar;
	private TextView yourClazz;
	private GridLayout boardGrid;
	private GridLayout myMochikomaGrid;
	private GridLayout yourMochikomaGrid;
	private GridLayout myMochikomaSuGrid;
	private GridLayout yourMochikomaSuGrid;
	private DiscuttionClient client;
	private String order;
	private WebSocket ws;
	private Intent i;
	private int ticketNum;
	private ScrollView scroll;
	private Button nextButton;
	private Button backButton;
	private Button reverseButton;
	private Board board;
	private Player myPlayer;
	private Player yourPlayer;
	private Button changeButton;
	private Button exitButton;
	private ViewFlipper flipper;
	private TextView tv;
	private EditText multiLine;
	private Button sendButton;
	int kifuIndex = 1;
	private String focusPlayer = "先手";
	private boolean viewFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discuttion);
		System.out.println("感想戦がはじまります");
		flipper = (ViewFlipper)findViewById(R.id.flipper);
		changeButton = (Button) findViewById(R.id.changeButton);
		exitButton = (Button)findViewById(R.id.exitButton);
		tv = (TextView)findViewById(R.id.textView1);
		multiLine = (EditText)findViewById(R.id.multiLine);
		sendButton = (Button)findViewById(R.id.send);
		changeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button btn = (Button)v;
				if(viewFlag){
					btn.setText("board");
					flipper.showNext();
					viewFlag = false;
				}else{
					btn.setText("chat");
					flipper.showPrevious();
					viewFlag = true;
				}
			}
		});
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(Discuttion.this)
		        .setTitle("感想戦を終了しますか？")
		        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	if(ws.isOpen()){
		            		ws.disconnect();
		            	}
		            	i.setClass(Discuttion.this,MainActivity.class);
		            	startActivity(i);
		            }
		        })
		        .setNegativeButton("いいえ",null)
		        .show();
			}
		});
		i = getIntent();
		order = i.getStringExtra("order");
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(i.getByteArrayExtra("kifu")));
	        board = (Board)in.readObject();
	        in.close();
		} catch (StreamCorruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		if(i.getStringExtra("order").equals("先手")){
			this.myPlayer = board.getSente();
			this.yourPlayer = board.getGote();
		}else{
			this.myPlayer = board.getGote();
			this.yourPlayer = board.getSente();
		}
		ticketNum = Integer.valueOf(i.getStringExtra("ticketNum"));
		//自分の名前
		myName = (TextView) findViewById(R.id.myNameText);
		//自分のアバター
		myAvatar = (ImageView) findViewById(R.id.myAvatarImage);
		//自分の級
		myClazz = (TextView) findViewById(R.id.myClazzText);

		//相手の名前
		yourName = (TextView) findViewById(R.id.yourNameText);
		//相手のアバター
		yourAvatar = (ImageView) findViewById(R.id.yourAvatarImage);
		//相手の級
		yourClazz = (TextView) findViewById(R.id.yourClazzText);
		yourClazz.setText("9級");
		yourAvatar.setImageResource(R.drawable.kin);
		if(order.equals("先手")){
			System.out.println("あなたは先手");
			//自分の名前
			TextView myName = (TextView) findViewById(R.id.myNameText);
			myName.setText(i.getStringExtra("senteName"));
			//自分のアバター
			ImageView myAvatar = (ImageView) findViewById(R.id.myAvatarImage);
			myAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("senteAvatar"))));
			//自分の級
			TextView myClazz = (TextView) findViewById(R.id.myClazzText);
			myClazz.setText(i.getStringExtra("senteClazz"));

			//相手の名前
			TextView yourName = (TextView) findViewById(R.id.yourNameText);
			yourName.setText(i.getStringExtra("goteName"));
			//相手のアバター
			ImageView yourAvatar = (ImageView) findViewById(R.id.yourAvatarImage);
			yourAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("goteAvatar"))));
			//相手の級
			TextView yourClazz = (TextView) findViewById(R.id.yourClazzText);
			yourClazz.setText(i.getStringExtra("goteClazz"));
		}else if(order.equals("後手")){
			System.out.println("あなたは後手");
			//自分の名前
			TextView myName = (TextView) findViewById(R.id.myNameText);
			myName.setText(i.getStringExtra("goteName"));
			//自分のアバター
			ImageView myAvatar = (ImageView) findViewById(R.id.myAvatarImage);
			myAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("goteAvatar"))));
			//自分の級
			TextView myClazz = (TextView) findViewById(R.id.myClazzText);
			myClazz.setText(i.getStringExtra("goteClazz"));

			//相手の名前
			TextView yourName = (TextView) findViewById(R.id.yourNameText);
			yourName.setText(i.getStringExtra("senteName"));
			//相手のアバター
			ImageView yourAvatar = (ImageView) findViewById(R.id.yourAvatarImage);
			yourAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("senteAvatar"))));
			//相手の級
			TextView yourClazz = (TextView) findViewById(R.id.yourClazzText);
			yourClazz.setText(i.getStringExtra("senteClazz"));
		}
		boardGrid = (GridLayout)findViewById(R.id.boardGrid);
		myMochikomaGrid = (GridLayout)findViewById(R.id.myMochikomaGrid);
		yourMochikomaGrid = (GridLayout)findViewById(R.id.yourMochikomaGrid);
		myMochikomaSuGrid = (GridLayout)findViewById(R.id.myMochikomaSuGrid);
		yourMochikomaSuGrid = (GridLayout)findViewById(R.id.yourMochikomaSuGrid);
		scroll = (ScrollView)findViewById(R.id.kifuNum);
		nextButton = (Button)findViewById(R.id.nextButton);
		backButton = (Button)findViewById(R.id.prevButton);
		reverseButton = (Button)findViewById(R.id.reverseButton);

		initView(board);

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				kifuIndex += 1;
				updateView();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				kifuIndex -= 1;
				updateView();
			}
		});
		reverseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(focusPlayer.equals("先手")){
					focusPlayer = "後手";
				}else{
					focusPlayer = "先手";
				}
				//プレイヤー交換
				Player taihi = Discuttion.this.myPlayer;
				myPlayer = Discuttion.this.yourPlayer;
				yourPlayer = taihi;
				//ユーザ名交換
				CharSequence taihiStr = myName.getText();
				myName.setText(yourName.getText());
				yourName.setText(taihiStr);
				updateView();
				//級交換
				taihiStr = myClazz.getText();
				myClazz.setText(yourClazz.getText());
				yourClazz.setText(taihiStr);
				//アバター交換
				Drawable taihiDraw = myAvatar.getDrawable();
				myAvatar.setImageDrawable(yourAvatar.getDrawable());
				yourAvatar.setImageDrawable(taihiDraw);
				updateView();
			}
		});
		this.client = new DiscuttionClient();
		this.client.execute(this);
		System.out.println("onCreate終了");
	}

	public void initView(Board board){
		this.board = board;
		LinearLayout ll = (LinearLayout)scroll.getChildAt(0);
		for(int k=1;k<board.getBoardKeeper().getKifu().size()+1;k++){
			TextView tv = new TextView(this);
			tv.setText(String.valueOf(k));
			tv.setOnClickListener(createListener(k));
			ll.addView(tv);
		}
		updateView();
	}
	public void updateView(){
		//先頭で戻るボタン無効化
		if(kifuIndex == 1){
			backButton.setEnabled(false);
		}else{
			backButton.setEnabled(true);
		}
		//末尾で次へボタン無効化
		if(kifuIndex == board.getBoardKeeper().getKifu().size()){
			nextButton.setEnabled(false);
		}else{
			nextButton.setEnabled(true);
		}

		this.board.setBoardFromBoardKeeper(kifuIndex);
		Class<?> clazz=null;
		try {
			clazz = Class.forName("tt4.syogi.R$drawable");
			for(int i=0;i<9;i++){
		    	for(int j=0;j<9;j++){
		    		ImageButton ib;
		    		if(focusPlayer.equals("先手")){
		    			ib = (ImageButton)boardGrid.getChildAt(i*9+j);
		    		}else{
		    			ib = (ImageButton)boardGrid.getChildAt((8-i)*9+(8-j));
		    		}
		    		if(board.board[j][i] !=null){
		    			//R.drawableの駒の画像に対応するフィールドにリフレクト
						Field f=null;
						try {
							f = clazz.getField(board.board[j][i].getImageId());
						} catch (NoSuchFieldException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
		        		if(board.board[j][i].getOwner().getName().equals(focusPlayer)){
		        			//自駒の画像はそのまま表示
							ib.setImageResource(f.getInt(null));
		        		}else{
		        			//相手駒の画像は左右反転して表示
							Matrix matrix = new Matrix();
							matrix.preScale(-1,-1);
							Bitmap bm = BitmapFactory.decodeResource(this.getResources(),f.getInt(null));
							bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
							ib.setImageBitmap(bm);
		        		}
		    		}else{
		    			ib.setImageResource(0);
		    			ib.setImageBitmap(null);
		    		}
		    	}
			}
		} catch (ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//ここまで盤面GridLayoutの更新
		//ここから持ち駒GridLayoutの更新
		//自分の持ち駒のビュー配置
		for(int i=0;i<myPlayer.mochiKoma.length;i++){
			ImageButton ib = (ImageButton)myMochikomaGrid.getChildAt(i);
			TextView tv = (TextView)myMochikomaSuGrid.getChildAt(i);
			if(myPlayer.mochiKoma[i] == null){
				ib.setImageBitmap(null);
				ib.setImageResource(0);
				tv.setText("  ");
			}else{
				Field f;
				try {
					f = clazz.getField(myPlayer.mochiKoma[i].getImageId());
					ib.setImageResource(f.getInt(null));
				} catch (NoSuchFieldException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				tv.setText(String.valueOf(myPlayer.mochiKomaNum[i]));
			}
		}
		//相手の持ち駒のビュー配置
		for(int i=0;i<yourPlayer.mochiKoma.length;i++){
			ImageButton ib = (ImageButton)yourMochikomaGrid.getChildAt(i);
			TextView tv = (TextView)yourMochikomaSuGrid.getChildAt(i);
			if(yourPlayer.mochiKoma[i] == null){
				ib.setImageBitmap(null);
				ib.setImageResource(0);
				tv.setText("  ");
			}else{
				try {
					Field f;
					f = clazz.getField(yourPlayer.mochiKoma[i].getImageId());
					Matrix matrix = new Matrix();
					matrix.preScale(-1,-1);
					Bitmap bm = BitmapFactory.decodeResource(getResources(),f.getInt(null));
					bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
					ib.setImageBitmap(bm);
					tv.setText(String.valueOf(yourPlayer.mochiKomaNum[i]));
				} catch (NoSuchFieldException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		//ここまで持ち駒GridLayoutの更新
	}

	private OnClickListener createListener(int index){
		final int zahyo = index;
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("kifuIndex:"+zahyo);
				Discuttion.this.kifuIndex = zahyo;
				updateView();
			}
		};
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
		return i;
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

	/**
	 * exitButtonを取得します。
	 * @return exitButton
	 */
	public Button getExitButton() {
	    return exitButton;
	}

	/**
	 * tvを取得します。
	 * @return tv
	 */
	public TextView getTv() {
	    return tv;
	}

	/**
	 * multiLineを取得します。
	 * @return multiLine
	 */
	public EditText getMultiLine() {
	    return multiLine;
	}

	/**
	 * sendButtonを取得します。
	 * @return sendButton
	 */
	public Button getSendButton() {
	    return sendButton;
	}
	public Button getChangeButton(){
		return changeButton;
	}
}
