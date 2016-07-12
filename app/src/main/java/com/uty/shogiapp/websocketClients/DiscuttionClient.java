package com.uty.shogiapp.websocketClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.activities.Discuttion;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.uty.shogiapp.settings.ServerConfig;

public class DiscuttionClient extends AsyncTask<Discuttion,Void,Void>{
	//対局画面のアクティビティ
	Discuttion activity;
	private WebSocket ws;
	public boolean endFlag = false;
	private Board board;
	public DiscuttionClient() {
		System.out.println("DiscuttionClient_Constractor");
	}
	@Override
	protected Void doInBackground(Discuttion... params) {
		System.out.println("DiscuttionClient開始");
		this.activity = params[0];
		WebSocketFactory factory = new WebSocketFactory();

	    try {
	        ws = factory.createSocket(ServerConfig.URL2+"DiscuttionStartAccepter", 5000);
        	System.out.println("connect to DiscuttionStartAccepter");
			ws.connect();
	    } catch (IOException ex) {
	        // TODO 自動生成された catch ブロック
	        ex.printStackTrace();
	    } catch (WebSocketException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    this.activity.setWs(ws);
	    System.out.println("doInBackgroundを終了します");
	    return null;
	}
	@Override
	protected void onPostExecute(Void v){
		System.out.println("onPostExecute...");
	    this.activity.getExitButton().setText("exit");
	    this.activity.getChangeButton().setText("chat");
        ws.sendText(String.valueOf(this.activity.getTicketNum()));
        ws.addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String message)
                    throws Exception {
        		DiscuttionClient.this.activity.getTv().setText(DiscuttionClient.this.activity.getTv().getText()+message);
            }
            @Override
            public void onDisconnected(WebSocket websocket,WebSocketFrame serverCloseFrame,
                WebSocketFrame clientCloseFrame,boolean closedByServer){
            	new AlertDialog.Builder(DiscuttionClient.this.activity)
		        .setTitle("相手が退出しました")
		        .setPositiveButton("OK", null)
		        .show();
            	}
        	});
        this.activity.getSendButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = DiscuttionClient.this.activity.getMultiLine().getText().toString();
				System.out.println("onClick!"+str);
				if(!(str.equals(""))){
					System.out.println("文字列を送信します");
					DiscuttionClient.this.ws.sendText(str);
					DiscuttionClient.this.activity.getTv().setText(DiscuttionClient.this.activity.getTv().getText()+str+"\n");
					DiscuttionClient.this.activity.getMultiLine().setText(null);
				}else{
					System.out.println("テキストサイズが0です");
				}
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
