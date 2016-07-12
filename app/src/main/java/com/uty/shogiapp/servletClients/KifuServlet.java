package com.uty.shogiapp.servletClients;


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.uty.shogiapp.settings.ServerConfig;
import com.uty.shogiapp.shogi.Board;
import com.uty.shogiapp.shogi.Koma;
import com.uty.shogiapp.activities.Kifu;
import android.os.AsyncTask;


public class KifuServlet extends AsyncTask<Kifu,Void,Board>{
	Kifu activity;
	private String kifuNum;

	public KifuServlet(){
	}

	public KifuServlet(String kifuNum){
		this.kifuNum = kifuNum;
	}

	@Override
	protected Board doInBackground(Kifu...kifu) {
		this.activity = kifu[0];
		System.out.println("===== HTTP POST Start =====");
        Board board = null;
		try {
            URL url = new URL(ServerConfig.URL+"KifuPage");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                String parameter = "kifuNum="+kifuNum;
                PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
                printWriter.print(parameter);
                printWriter.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();

                    try {
                    	ObjectInputStream in = new ObjectInputStream(is);
                        board = (Board)in.readObject();
                    }catch(EOFException e){
                        e.printStackTrace();
                    }
                    disp(board);
                }
            } catch (ClassNotFoundException ex) {
            	ex.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
	}




	//doInBackground()メソッドの戻り値を引数に自動で呼び出されるメソッド
	@Override
    protected void onPostExecute(Board board) {
		activity.initView(board);
    }

	public void disp(Board board){
        Koma[][] komas;
        Map<Integer,Board> map = board.getBoardKeeper().getKifu();
        for(int k=1;k<map.size()+1;k++){
            komas = map.get(k).board;
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(komas[i][j] == null){
                        System.out.print("○");
                    }else{
                        System.out.print(komas[i][j].getName().charAt(0));
                    }
                }
                System.out.println();
            }
            System.out.println("________________________________");
            Koma[] mochiKoma = map.get(k).getSente().getMochiKoma();
            System.out.print("先手");
            for(int i=0;i<8;i++){
            	if(mochiKoma[i] == null){
                    System.out.print("○");
                }else{
                    System.out.print(mochiKoma[i].getName().charAt(0));
                }
            }
            System.out.println();
            System.out.println("________________________________");
        }
    }

}

