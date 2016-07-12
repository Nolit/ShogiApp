package com.uty.shogiapp.websocketClients;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.activities.Battle;
import com.uty.shogiapp.activities.ResultView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.uty.shogiapp.settings.ServerConfig;

public class BattleClient extends AsyncTask<Battle,Void,Void>{
	//対局画面のアクティビティ
	Battle activity;
	private WebSocket ws;
	public boolean endFlag = false;
	private Board board;
	public BattleClient() {
		System.out.println("BattleClient_Constractor");
	}
	@Override
	protected Void doInBackground(Battle... params) {
		System.out.println("BattleClient開始");
		this.activity = params[0];
		WebSocketFactory factory = new WebSocketFactory();

	    try {
	        ws = factory.createSocket(ServerConfig.URL2+"BattleStartAccepter", 5000);
	        activity.setWs(ws);
        	System.out.println("connect to BattleStartAccepter");
			ws.connect();
	    } catch (IOException ex) {
	        // TODO 自動生成された catch ブロック
	        ex.printStackTrace();
	    } catch (WebSocketException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
//	    new BoardUpdateChecker().execute(this);

	    System.out.println("doInBackgroundを終了します");
	    return null;
	}
	@Override
	protected void onPostExecute(Void v){
		System.out.println("onPostExecute...");
		System.out.println(this.activity.getTicketNum());
        ws.sendText(String.valueOf(this.activity.getTicketNum()));
        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String message)
                    throws Exception {
                // テキスト・メッセージを受信
            	if(message.equals("disconnected")){
            		//接続切れ勝ちの処理
            		System.out.println("接続切れ勝ちの処理");
            		Intent intent = activity.getNextIntent();
            		if(activity.getOrder().equals("先手")){
            			intent.putExtra("isSenteWin",true);
            		}else{
            			intent.putExtra("isSenteWin",false);
            		}
            		intent.putExtra("message","接続切れ勝ち");
            		intent.setClass(activity,ResultView.class);
                    activity.startActivity(intent);
            	}
            }

            @Override
            public void onBinaryMessage(WebSocket websocket, byte[] binary) {
                //棋譜取得の処理
                System.out.println("onBinaryMessage...");
                try {
                	ObjectInputStream in = new BattleClient.NonHeaderObjectInputStream(new ByteArrayInputStream(binary));
                    BattleClient.this.board = (Board)in.readObject();
                    in.close();
                    disp(BattleClient.this.board);
                	new BoardUpdater().execute(BattleClient.this);
                    if(BattleClient.this.board.isEndFlag()){
                    	BattleClient.this.endFlag = true;
                    	//詰み・投了による試合の終了
                    	System.out.println("試合終了");
                    	ws.clearHeaders();
                    	ws = null;
                    	Intent intent = activity.getNextIntent();
                    	ByteArrayOutputStream baos= new ByteArrayOutputStream();
                    	ObjectOutputStream oos = new ObjectOutputStream(baos);
                    	oos.writeObject(board);
                    	byte[] bytes = baos.toByteArray();
                    	intent.putExtra("kifu",bytes);
                    	baos.close();
                    	oos.close();
            			if(BattleClient.this.board.isSenteWinFlag()){
                			intent.putExtra("isSenteWin",true);
                			if(activity.getOrder().equals("先手")){
                				intent.putExtra("message","勝利");
                			}else{
                				intent.putExtra("message","敗北");
                			}
                		}else{
                			intent.putExtra("isSenteWin",false);
                			if(activity.getOrder().equals("後手")){
                				intent.putExtra("message","勝利");
                			}else{
                				intent.putExtra("message","敗北");
                			}
                		}
                		intent.setClass(activity,ResultView.class);
                        activity.startActivity(intent);
                    }else if(BattleClient.this.board.isOuteFlag()){
                    	System.out.println("王手です");
                    	//王手
//                    	new OuteColorChanger().execute(activity.outeText);
                    }
                    //対局終了
                    //senteWInFlagよりどっちが勝ったかを調べる
                } catch (EOFException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                //詰みフラグ・投了フラグ・時間切れフラグの判断
                //手番を判断してボタンの無効化有効化を行う
                //自分の番なら時間を測る
                //動かした情報・使用時間等を送信
            }
            @Override
            public void onDisconnected(WebSocket websocket,WebSocketFrame serverCloseFrame,
                WebSocketFrame clientCloseFrame,boolean closedByServer){
            	System.out.println("接続が切れました");
            	}
        	});
		activity.getCancelButton().setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				new AlertDialog.Builder(activity)
		        .setTitle("投了しますか？")
		        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	//二つ目の要素をtrueにして投了であることを知らせる
		            	ws.sendText("false,true");
		            }
		        })
		        .setNegativeButton("いいえ", null)
		        .show();
			}
		});

        System.out.println("BattleClient_onPostExecuteを終了します");
	}
	public static void disp(Board board) {
		System.out.println("棋譜を表示します");
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

    }
	static class NonHeaderObjectInputStream extends ObjectInputStream {
        @Override
	protected void readStreamHeader() {
	}

	NonHeaderObjectInputStream(InputStream in) throws IOException{
		super(in);
	}
    }
	/**
	 * wsを取得します。
	 * @return ws
	 */
	public WebSocket getWs() {
	    return ws;
	}
	/**
	 * boardを取得します。
	 * @return board
	 */
	public Board getBoard() {
	    return board;
	}
}
