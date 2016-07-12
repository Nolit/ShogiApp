package com.uty.shogiapp.shogi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoardKeeper  implements Serializable{
	private Map<Integer,Board> kifu = new HashMap<Integer,Board>();
	public BoardKeeper(){
	}

	public Map<Integer,Board> getKifu(){
		return kifu;
	}


	//クローンの追加
	public void addKifuClone(int turnNum,Board board){
		kifu.put(new Integer(turnNum),getBoardClone(board));
	}


	//クローンの取出し
	public Board getClone(int turnNum){
		return getBoardClone(kifu.get(turnNum));
	}

	//Koma[][]の全ての要素のclone()を呼び出す
	private Board getBoardClone(Board board){
		Board boardClone = new Board();
        boardClone.initBoard(new Player("先手"),new Player("後手"));
		//盤面をClone
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(board.board[i][j] != null){
					boardClone.board[i][j] = board.board[i][j].clone();
				}else{
					boardClone.board[i][j] = null;
				}
			}
		}
                //先手をClone
		boardClone.getSente().setMochiKoma(getKomasClone(board.getSente().getMochiKoma()));
		boardClone.getSente().setMochiKomaNum(numsClone(board.getSente().getMochiKomaNum()));
                //後手をClone
		boardClone.getGote().setMochiKoma(getKomasClone(board.getGote().getMochiKoma()));
		boardClone.getGote().setMochiKomaNum(numsClone(board.getGote().getMochiKomaNum()));
	return boardClone;
	}

	private Koma[] getKomasClone(Koma[] komas){
		Koma[] komasClone = new Koma[8];
		for(int i=0;i<8;i++){
			if(komas[i] != null){
				komasClone[i] = komas[i].clone();
			}else{
				komasClone[i] = null;
			}
		}
	return komasClone;
	}
	private int[] numsClone(int[] nums){
		int[] numsClone = new int[8];
		for(int i=0;i<8;i++){
			numsClone[i] = nums[i];
		}
	return numsClone;
	}

	public void removeAll(){
		kifu.clear();
	}

}
