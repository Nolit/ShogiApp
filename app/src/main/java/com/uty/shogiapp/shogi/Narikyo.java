package com.uty.shogiapp.shogi;

import java.io.Serializable;




class Narikyo extends KomaState implements Serializable{


	//コンストラクタ  状態を成香に
	Narikyo(Board board){
            super(board);
	}

	//抽象メソッドの実装 成る前の駒を返す
	public KomaState changeState(Koma koma,Player owner){
		return this;
	}

	//抽象メソッドの実装 成る前の駒を返す
	public KomaState backState(){
		return new Kyosha(boardInstance);
	}

	//抽象メソッドの実装 新しい駒を返す
	public KomaState copyState(){
		return backState();
	}

	//抽象メソッドの実装 駒の名前を返す
	public String getNameState(){
		return "成香";
	}

	@Override
	public String getImageId(){
		return "narikyo";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[6][2];

		if(turn.getName().equals("先手")){	//先手

			//１マス前
			move[0][0] = -1;
			move[0][1] = 0;

			//１マス右斜め前
			move[1][0] = -1;
			move[1][1] = 1;

			//１マス右
			move[2][0] = 0;
			move[2][1] = 1;

			//１マス後ろ
			move[3][0] = 1;
			move[3][1] = 0;

			//１マス左
			move[4][0] = 0;
			move[4][1] = -1;

			//１マス左斜め前
			move[5][0] = -1;
			move[5][1] = -1;

			return move;

		}else{						//後手

			//１マス前
			move[0][0] = 1;
			move[0][1] = 0;

			//１マス右斜め前
			move[1][0] = 1;
			move[1][1] = -1;

			//１マス右
			move[2][0] = 0;
			move[2][1] = -1;

			//１マス後ろ
			move[3][0] = -1;
			move[3][1] = 0;

			//１マス左
			move[4][0] = 0;
			move[4][1] = 1;

			//１マス左斜め前
			move[5][0] = 1;
			move[5][1] = 1;

			return move;

		}

	}

}
