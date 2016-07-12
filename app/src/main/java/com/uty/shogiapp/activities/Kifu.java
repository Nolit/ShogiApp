package com.uty.shogiapp.activities;


import java.lang.reflect.Field;

import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.shogi.Player;
import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.KifuServlet;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Kifu extends Activity {
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
	private ScrollView scroll;
	private Button nextButton;
	private Button backButton;
	private Button reverseButton;
	private Board board;
	private Player myPlayer;
	private Player yourPlayer;
	int kifuIndex = 1;
	private String focusPlayer = "先手";

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.kifu);
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
	boardGrid = (GridLayout)findViewById(R.id.boardGrid);
	myMochikomaGrid = (GridLayout)findViewById(R.id.myMochikomaGrid);
	yourMochikomaGrid = (GridLayout)findViewById(R.id.yourMochikomaGrid);
	myMochikomaSuGrid = (GridLayout)findViewById(R.id.myMochikomaSuGrid);
	yourMochikomaSuGrid = (GridLayout)findViewById(R.id.yourMochikomaSuGrid);
	scroll = (ScrollView)findViewById(R.id.kifuNum);

	Intent i = getIntent();
    try {
		Class<?> clazz = Class.forName("tt4.syogi.R$drawable");
		Field f = clazz.getField(i.getStringExtra("SenteAvatarName"));
		int senteAvatarName = f.getInt(null);
		f = clazz.getField(i.getStringExtra("GoteAvatarName"));
		int goteAvatarName = f.getInt(null);
		//先手のコンポーネントを更新
		myName.setText(i.getStringExtra("SenteName"));
		myClazz.setText(i.getStringExtra("SenteClass"));
		myAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), senteAvatarName));
		//後手のコンポーネントを更新
		yourName.setText(i.getStringExtra("GoteName"));
		yourClazz.setText(i.getStringExtra("GoteClass"));
		yourAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), goteAvatarName));
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	} catch (NoSuchFieldException e) {
		e.printStackTrace();
	}

	String battleNum = i.getStringExtra("battleNum");
	new KifuServlet(battleNum).execute(this);




	nextButton = (Button)findViewById(R.id.nextButton);
	nextButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			kifuIndex += 1;
			updateView();
		}
	});
	backButton = (Button)findViewById(R.id.prevButton);
	backButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			kifuIndex -= 1;
			updateView();
		}
	});
	reverseButton = (Button)findViewById(R.id.reverseButton);
	reverseButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(focusPlayer.equals("先手")){
				focusPlayer = "後手";
			}else{
				focusPlayer = "先手";
			}
			//プレイヤー交換
			Player taihi = Kifu.this.myPlayer;
			myPlayer = Kifu.this.yourPlayer;
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

}
public void initView(Board board){
	this.board = board;
	this.myPlayer = board.getSente();
	this.yourPlayer = board.getGote();
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
			Kifu.this.kifuIndex = zahyo;
			updateView();
		}
	};
}
/**
 * scrollを取得します。
 * @return scroll
 */
public ScrollView getScroll() {
    return scroll;
}

/**
 * boardを設定します。
 * @param board board
 */
public void setBoard(Board board) {
    this.board = board;
}

/**
 * kifuIndexを取得します。
 * @return kifuIndex
 */
public int getKifuIndex() {
    return kifuIndex;
}

/**
 * kifuIndexを設定します。
 * @param kifuIndex kifuIndex
 */
public void setKifuIndex(int kifuIndex) {
    this.kifuIndex = kifuIndex;
}



}