package com.uty.shogiapp.websocketClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.activities.Discuttion;
import com.uty.shogiapp.activities.Result;

import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

public class DiscuttionSelectClient extends AsyncTask<Result,Void,Void>{
	//対局画面のアクティビティ
	Result activity;
	private WebSocket ws;
	public boolean endFlag = false;
	private Board board;
	public DiscuttionSelectClient() {
		System.out.println("DiscuttionClient_Constractor");
	}
	@Override
	protected Void doInBackground(Result... params) {
		System.out.println("DiscuttionSelectClient開始");
		this.activity = params[0];
		WebSocketFactory factory = new WebSocketFactory();

	    try {
	        ws = factory.createSocket(ServerConfig.URL2+"DiscuttionSelectStartAccepter", 5000);
        	System.out.println("connect to DiscuttionSelectStartAccepter");
			ws.connect();
	    } catch (IOException ex) {
	        // TODO 自動生成された catch ブロック
	        ex.printStackTrace();
	    } catch (WebSocketException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    System.out.println("doInBackgroundを終了します");
	    return null;
	}
	@Override
	protected void onPostExecute(Void v){
		System.out.println("onPostExecute...");
        ws.sendText(String.valueOf(this.activity.getTicketNum()));
        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String message)
                    throws Exception {
            	System.out.println("onTextMessage"+message);
                // テキスト・メッセージを受信
            	if(message.equals("cancel")){
            		System.out.println("相手が断りました");

            	}else{
            		System.out.println("相手が来た時の処理");
            		activity.getNextIntent().putExtra("ticketNum",message);
            		activity.getNextIntent().setClass(activity,Discuttion.class);
                    activity.startActivity(activity.getNextIntent());
                    System.out.println("startActivity");
            	}
            }
            @Override
            public void onDisconnected(WebSocket websocket,WebSocketFrame serverCloseFrame,
                WebSocketFrame clientCloseFrame,boolean closedByServer){
            	System.out.println("接続が切れました");
            	}
        	});
        System.out.println("DiscuttionClient_onPostExecuteを終了します");
	}

	private void disp(Board board) {
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
