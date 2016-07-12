package com.uty.shogiapp.websocketClients;

import java.io.IOException;

import com.uty.shogiapp.activities.Matching;
import com.uty.shogiapp.settings.ServerConfig;
import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

public class MatchingClient extends AsyncTask<Matching,Void,Void>{

	public MatchingClient(){
		System.out.println("MatchingClient_Constractor...");
	}
	//対局画面のアクティビティ
	Matching activity;
	WebSocket ws;
	@Override
	protected Void doInBackground(Matching... params) {
		this.activity = params[0];

		WebSocketFactory factory = new WebSocketFactory();
	    try {
	    	System.out.println("connectします");
	        ws = factory.createSocket(ServerConfig.URL2+"MatchingStartAccepter", 5000);
	        ws.connect();
	        ws.sendText(this.activity.getUserId());
	        ws.addListener(new WebSocketAdapter() {
	            @Override
	            public void onTextMessage(WebSocket websocket, String message)
	                    throws Exception {
	            	System.out.println("受信:"+message);
	                // テキスト・メッセージを受信
	            	if(message.equals("ready")){
	            		//対戦相手が見つかった時の処理
//            			activity.getCancelButton().setEnabled(false);
	            		websocket.sendText("ok");
	            	}else if(message.equals("start")){
	            		//対局画面へ移動
	            		ws.disconnect();
	            		activity.nextActivity();
	            	}else if(message.equals("retry")){
	            		//もう一度マッチングする時の処理
	            		activity.getCancelButton().setEnabled(true);
	            	}else{
	            		activity.getBattleInfo().add(message);
	            	}
	            }
	            @Override
	            public void onDisconnected(WebSocket websocket,WebSocketFrame serverCloseFrame,
	                WebSocketFrame clientCloseFrame,boolean closedByServer){
	                    System.out.println("接続が切れました");
		            	cancel(true);
	            }
	        });
	    } catch (IOException ex) {
	        // TODO 自動生成された catch ブロック
	        ex.printStackTrace();
	    } catch (WebSocketException we) {
	        we.printStackTrace();
	    }
	    while(!isCancelled()){

	    }
	    System.out.println("nullを返します");
	    return null;
	}
	@Override
	protected void onCancelled(){
		System.out.println("MatchingClient_onCancelled...");
		if(ws != null){
			ws.disconnect();
		}
	}
}
