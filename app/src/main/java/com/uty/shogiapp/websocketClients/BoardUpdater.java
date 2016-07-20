package com.uty.shogiapp.websocketClients;

import java.lang.reflect.Field;

import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.shogi.Koma;
import com.uty.shogiapp.shogi.Player;
import com.uty.shogiapp.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BoardUpdater extends AsyncTask<BattleClient,Void,Void>{
	private BattleClient bc;
	private int beforeIndex;
	private Player myPlayer;
	private Player yourPlayer;
	@Override
	protected Void doInBackground(BattleClient... params) {
		bc = params[0];
		System.out.println("BoardUpdater_doInBackground...");
		//自分と相手のPlayerインスタンスを保存
		if(bc.activity.getOrder().equals("先手")){
			System.out.println("あなたは先手");
			myPlayer = bc.getBoard().getSente();
			yourPlayer = bc.getBoard().getGote();
		}else{
			System.out.println("あなたは後手");
			myPlayer = bc.getBoard().getGote();
			yourPlayer = bc.getBoard().getSente();
		}
		beforeIndex = 0;
		return null;
	}
	@Override
	protected void onPostExecute(Void v){
		System.out.println("BoardUpdater_onPostExecute...");
		try {
			Class<?> clazz = Class.forName("tt4.syogi.R$drawable");
			//ここから盤面GridLayoutの更新
			for(int i=0;i<9;i++){
	        	for(int j=0;j<9;j++){
	        		ImageButton ib;
	        		if(myPlayer.getName().equals("先手")){
	        			ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(i*9+j);
	        		}else{
	        			ib = (ImageButton)bc.activity.getBoardGrid().getChildAt((8-i)*9+(8-j));
	        		}
	        		if(bc.getBoard().board[j][i] !=null){
            			//R.drawableの駒の画像に対応するフィールドにリフレクト
						Field f = clazz.getField(bc.getBoard().board[j][i].getImageId());
                		if(bc.getBoard().board[j][i].getOwner().getName().equals(bc.activity.getOrder())){
                			//自駒の画像はそのまま表示
							ib.setImageResource(f.getInt(null));
                		}else{
                			//相手駒の画像は左右反転して表示
							Matrix matrix = new Matrix();
							matrix.preScale(-1,-1);
							Bitmap bm = BitmapFactory.decodeResource(bc.activity.getResources(),f.getInt(null));
							bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
							ib.setImageBitmap(bm);
                		}
	        		}else{
	        			ib.setImageResource(0);
	        			ib.setImageBitmap(null);
	        		}
	        	}
			}
			//ここまで盤面GridLayoutの更新
			//ここから持ち駒GridLayoutの更新
			//自分の持ち駒のビュー配置
			System.out.println(myPlayer.mochiKoma.length);
			for(int i=0;i<myPlayer.mochiKoma.length;i++){
				ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(i);
				TextView tv = (TextView)bc.activity.getMyMochikomaSuGrid().getChildAt(i);
				if(myPlayer.mochiKoma[i] == null){
					ib.setImageResource(0);
					tv.setText("  ");
					break;
				}
				Field f = clazz.getField(myPlayer.mochiKoma[i].getImageId());
				ib.setImageResource(f.getInt(null));
				tv.setText(String.valueOf(myPlayer.mochiKomaNum[i]));
			}
			//相手の持ち駒のビュー配置
			for(int i=0;i<yourPlayer.mochiKoma.length;i++){
				ImageButton ib = (ImageButton)bc.activity.getYourMochikomaGrid().getChildAt(i);
				TextView tv = (TextView)bc.activity.getYourMochikomaSuGrid().getChildAt(i);
				if(yourPlayer.mochiKoma[i] == null){
					ib.setImageBitmap(null);
					tv.setText("  ");
					break;
				}
				Field f = clazz.getField(yourPlayer.mochiKoma[i].getImageId());
				Matrix matrix = new Matrix();
				matrix.preScale(-1,-1);
				Bitmap bm = BitmapFactory.decodeResource(bc.activity.getResources(),f.getInt(null));
				bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
				ib.setImageBitmap(bm);
				tv.setText(String.valueOf(yourPlayer.mochiKomaNum[i]));
			}
			//ここまで持ち駒GridLayoutの更新


			//Boardに応じてイベントハンドラを更新する
			updateEventHandler();
		} catch (NoSuchFieldException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		bc.activity.getCancelButton().setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(bc.activity)
		        .setTitle("投了しますか？")
		        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	//二つ目の要素をtrueにして投了であることを知らせる
		            	bc.activity.getWs().sendText("false,true");
		            }
		        })
		        .setNegativeButton("いいえ", null)
		        .show();
			}
		});
        System.out.println("BoardUpdater_onPostExecuteが終了します");
	}
	//盤面の駒を持った時のイベントハンドラ生成
	private OnClickListener movableOnClickListenerCreate(int zahyo){
		final int index =zahyo;
		return new OnClickListener(){
			public void onClick(View v){
				System.out.println("move"+index);
				allButtonNotEnabled();
				beforeIndex = index;
				int[] movable = bc.getBoard().moveCheck(index);
				for(int i=0;i<movable.length;i++){
					int x = (movable[i]/10)-1;
					int y = (movable[i]%10)-1;
					ImageButton ib;
					if(myPlayer.getName().equals("先手")){
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(y*9+x);
					}else{
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt((8-y)*9+(8-x));
					}
	        		ib.setEnabled(true);
	        		ib.setOnClickListener(moveOnClickListenerCreate(movable[i]));
	        		changeWhiteColorImageButton(ib);
				}
				int x = (index/10)-1;
				int y = (index%10)-1;
				ImageButton ib;
				if(myPlayer.getName().equals("先手")){
					ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(y*9+x);
				}else{
					ib = (ImageButton)bc.activity.getBoardGrid().getChildAt((8-y)*9+(8-x));
				}
        		ib.setEnabled(true);
        		ib.setOnClickListener(moveOnClickListenerCreate(index));
        		changeWhiteColorImageButton(ib);
			}
		};
	}
	//盤面から盤面へ駒を動かす時のイベントハンドラ生成
	private OnClickListener moveOnClickListenerCreate(int zahyo){
		final int afterIndex = zahyo;
		return new OnClickListener(){
			public void onClick(View v){
				boolean nariDialogFlag=false;
				if(afterIndex == beforeIndex){
					System.out.println("駒を戻します");
					updateEventHandler();
				}else{
					System.out.println(beforeIndex+"から"+afterIndex+"へmove");
					myPlayer.move(beforeIndex, afterIndex);
//					BoardUpdater.this.bc.activity.setOrder(BoardUpdater.this.yourPlayer.getName());

					int afterRow = ((afterIndex/10)-1);
					int afterCol = ((afterIndex%10)-1);
					//ここから成れるかの確認
					Koma koma = bc.getBoard().board[afterRow][afterCol];
					if(koma.getName().equals("歩") || koma.getName().equals("香車") || koma.getName().equals("桂馬") ||
							koma.getName().equals("銀") || koma.getName().equals("飛車") || koma.getName().equals("角")){
						if(koma.getOwner().getName().equals("先手")){
							if(afterRow < 3 || afterRow <3){
								if(!((koma.getName().equals("歩") || koma.getName().equals("香車")) && afterRow == 0)){
									if(!(koma.getName().equals("桂馬") && afterRow < 2)){
										//ダイアログで確認
										nariDialogFlag = true;
										new AlertDialog.Builder(bc.activity)
								        .setTitle("成りますか？")
								        .setPositiveButton("成る", new DialogInterface.OnClickListener() {
								            @Override
								            public void onClick(DialogInterface dialog, int which) {
								                // OK button pressed
								            	bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
								            	//成らずに動いてboard受信後成る
//								            	BoardUpdater.this.bc.getBoard().setNariFlag(true);
//								            	new BoardUpdater().execute(BoardUpdater.this.bc);
								            }
								        })
								        .setNegativeButton("成らない", new DialogInterface.OnClickListener() {
								            @Override
								            public void onClick(DialogInterface dialog, int which) {
								                // OK button pressed
								            	bc.getWs().sendText("false,false,false,"+beforeIndex+","+afterIndex);
								            	BoardUpdater.this.bc.getBoard().setNariFlag(false);
								            	new BoardUpdater().execute(BoardUpdater.this.bc);
								            }
								        })
								        .show();
									}else{
										//強制的に成る
										bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
									}
								}else{
									//強制的に成る
									bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
								}
							}else{
								//成れない
								bc.getWs().sendText("false,false,false,"+beforeIndex+","+afterIndex);
							}
						}else{
							if(afterRow > 5 || afterRow > 5){
								if(!((koma.getName().equals("歩") || koma.getName().equals("香車")) && afterRow == 8)){
									if(!(koma.getName().equals("桂馬") && afterRow > 6)){
										//ダイアログで確認
										nariDialogFlag = true;
										new AlertDialog.Builder(bc.activity)
								        .setTitle("成りますか？")
								        .setPositiveButton("成る", new DialogInterface.OnClickListener() {
								            @Override
								            public void onClick(DialogInterface dialog, int which) {
								                // OK button pressed
								            	bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
								            	//成らずに動いてboard受信後成る
//								            	BoardUpdater.this.bc.getBoard().setNariFlag(true);
//								            	new BoardUpdater().execute(BoardUpdater.this.bc);
								            }
								        })
								        .setNegativeButton("成らない", new DialogInterface.OnClickListener() {
								            @Override
								            public void onClick(DialogInterface dialog, int which) {
								                // OK button pressed
								            	bc.getWs().sendText("false,false,false,"+beforeIndex+","+afterIndex);
								            	BoardUpdater.this.bc.getBoard().setNariFlag(false);
								            	new BoardUpdater().execute(BoardUpdater.this.bc);
								            }
								        })
								        .show();
									}else{
										//強制的に成る
										bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
									}
								}else{
									//強制的に成る
									bc.getWs().sendText("true,false,false,"+beforeIndex+","+afterIndex);
								}
							}else{
								//成れない
								bc.getWs().sendText("false,false,false,"+beforeIndex+","+afterIndex);
							}
						}
					}else{
						//成れない
						bc.getWs().sendText("false,false,false,"+beforeIndex+","+afterIndex);
					}
					//ここまで成れるかの確認
					allButtonNotEnabled();
				}
				changeDefaultColorBoardImageButton();
				if(!nariDialogFlag){
					new BoardUpdater().execute(BoardUpdater.this.bc);
				}
			}
		};
	}
	//手持ちの駒を持った時のイベントハンドラ生成
	private OnClickListener puttableOnClickListenerCreate(int zahyo){
		final int index =zahyo;
		return new OnClickListener(){
			public void onClick(View v){
				System.out.println("put"+index);
				allButtonNotEnabled();
				beforeIndex = index;
				Koma searchKoma = myPlayer.mochiKoma[index];
				int[] puttable = bc.getBoard().putCheck(searchKoma);
				for(int i=0;i<puttable.length;i++){
					int x = (puttable[i]/10)-1;
					int y = (puttable[i]%10)-1;
					ImageButton ib;
					if(myPlayer.getName().equals("先手")){
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(y*9+x);
					}else{
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt((8-y)*9+(8-x));
					}
					changeWhiteColorImageButton(ib);
	        		ib.setEnabled(true);
	        		ib.setOnClickListener(putOnClickListenerCreate(puttable[i]));
				}
				ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(index);
        		ib.setEnabled(true);
        		ib.setOnClickListener(putOnClickListenerCreate(index));
        		changeWhiteColorImageButton(ib);
			}
		};
	}
	//手持ちから盤面へ駒を置くときのイベントハンドラ生成
	private OnClickListener putOnClickListenerCreate(int zahyo){
		final int afterIndex =zahyo;
		return new OnClickListener(){
			public void onClick(View v){
				if(afterIndex == beforeIndex){
					System.out.println("駒を戻します");
					updateEventHandler();
				}else{
					System.out.println(beforeIndex+"から"+afterIndex+"へput");
					bc.getWs().sendText("false,false,true,"+beforeIndex+","+afterIndex);
					myPlayer.put(beforeIndex, afterIndex);
//					BoardUpdater.this.bc.activity.setOrder(BoardUpdater.this.yourPlayer.getName());
					new BoardUpdater().execute(BoardUpdater.this.bc);
					allButtonNotEnabled();
				}
				changeDefaultColorBoardImageButton();
				beforeIndex = 0;
			}
		};
	}

	//全ボタンの無効化
	private void allButtonNotEnabled(){
		for(int i=0;i<9;i++){
        	for(int j=0;j<9;j++){
        		ImageButton ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(i*9+j);
        		ib.setEnabled(false);
        	}
		}
		for(int i=0;i<8;i++){
			ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(i);
			ib.setEnabled(false);
		}
	}

	//イベントハンドラ更新
	private void updateEventHandler(){
		System.out.println("updateEventHandler...");
		allButtonNotEnabled();
		//ここからイベントハンドラを付ける処理
		if(bc.activity.getOrder().equals(bc.getBoard().getTurn().getName())){
			//盤面のイベントハンドラ
			for(int i=0;i<9;i++){
	        	for(int j=0;j<9;j++){
	        		ImageButton ib;
	        		if(myPlayer.getName().equals("先手")){
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(i*9+j);
					}else{
						ib = (ImageButton)bc.activity.getBoardGrid().getChildAt((8-i)*9+(8-j));
					}
	        		ib.setEnabled(false);
	        		if(bc.getBoard().board[j][i] !=null && bc.getBoard().board[j][i].getOwner().getName().equals(bc.activity.getOrder())){
	            		ib.setEnabled(true);
	            		ib.setOnClickListener(movableOnClickListenerCreate((j+1)*10+(i+1)));
	        		}
	        	}
			}
			//持ち駒のイベントハンドラ
			if(bc.activity.getOrder().equals("先手")){
				for(int i=0;i<8;i++){
					if(bc.getBoard().getSente().mochiKoma[i] == null){
						break;
					}
					ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(i);
					ib.setEnabled(true);
					ib.setOnClickListener(puttableOnClickListenerCreate(i));
				}
			}else{
				for(int i=0;i<8;i++){
					if(bc.getBoard().getGote().mochiKoma[i] == null){
						break;
					}
					ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(i);
					ib.setEnabled(true);
					ib.setOnClickListener(puttableOnClickListenerCreate(i));
				}
			}
		}
	}
	private void changeWhiteColorImageButton(ImageButton ib){
		ib.setBackgroundResource(R.layout.white_color);
	}
	private void changeDefaultColorBoardImageButton(){
		System.out.println("changeDefaultColorImageButton...");
		for(int i=0;i<9;i++){
        	for(int j=0;j<9;j++){
        		ImageButton ib = (ImageButton)bc.activity.getBoardGrid().getChildAt(i*9+j);
        		ib.setBackgroundResource(R.drawable.border);
        	}
		}

		for(int i=0;i<8;i++){
			ImageButton ib = (ImageButton)bc.activity.getMyMochikomaGrid().getChildAt(i);
    		ib.setBackgroundResource(R.drawable.border);
		}
	}


	public static void disp(Board board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.board[i][j] == null) {
                    System.out.print("○");
                } else {
                    System.out.print(board.board[i][j].getName().charAt(0));
                }
            }
            System.out.println();
        }
        System.out.println("________________________________");
    }

}
