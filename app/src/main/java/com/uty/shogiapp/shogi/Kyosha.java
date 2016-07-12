package com.uty.shogiapp.shogi;

import java.io.Serializable;



public class Kyosha extends KomaState implements Serializable{

	//コンストラクタ  状態を香車に
	public Kyosha(Board board){
            super(board);
	}

	//抽象メソッドの実装 成った後の駒インスタンスを返す
        @Override
	public KomaState changeState(Koma koma,Player owner){
            System.out.println("changeState...owner:"+owner.getName());
		if(owner.getName().equals("先手")){
			for(int i=0;i<9;i++){
				if(boardInstance.board[0][i] == koma){
					return new Narikyo(boardInstance);
				}
			}
		}else{
			for(int i=0;i<9;i++){
				if(boardInstance.board[8][i] == koma){
					return new Narikyo(boardInstance);
				}
			}
		}
		if(boardInstance.isNariFlag()){
			return new Narikyo(boardInstance);
		}else{
			return this;
		}
	}

	//抽象メソッドの実装 成る前の駒を返す
	public KomaState backState(){
		return this;
	}

	//抽象メソッドの実装 新しい駒を返す
	public KomaState copyState(){
		return new Kyosha(boardInstance);
	}

	//抽象メソッドの実装 駒の名前を返す
	public String getNameState(){
		 return "香車";
	}

	@Override
	public String getImageId(){
		return "kyosha";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[8][2];

		if(turn.getName().equals("先手")){	//先手
			//１マス前
			move[0][0] = -1;
			move[0][1] = 0;

			//２マス前
			move[1][0] = -2;
			move[1][1] = 0;

			//３マス前
			move[2][0] = -3;
			move[2][1] = 0;

			//４マス前
			move[3][0] = -4;
			move[3][1] = 0;

			//５マス前
			move[4][0] = -5;
			move[4][1] = 0;

			//６マス前
			move[5][0] = -6;
			move[5][1] = 0;

			//７マス前
			move[6][0] = -7;
			move[6][1] = 0;

			//８マス前
			move[7][0] = -8;
			move[7][1] = 0;

			return move;

		}else{						//後手

			//１マス前
			move[0][0] = 1;
			move[0][1] = 0;

			//２マス前
			move[1][0] = 2;
			move[1][1] = 0;

			//３マス前
			move[2][0] = 3;
			move[2][1] = 0;

			//４マス前
			move[3][0] = 4;
			move[3][1] = 0;

			//５マス前
			move[4][0] = 5;
			move[4][1] = 0;

			//６マス前
			move[5][0] = 6;
			move[5][1] = 0;

			//７マス前
			move[6][0] = 7;
			move[6][1] = 0;

			//８マス前
			move[7][0] = 8;
			move[7][1] = 0;

			return move;

		}

	}

}
