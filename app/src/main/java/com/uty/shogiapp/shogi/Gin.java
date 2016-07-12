package com.uty.shogiapp.shogi;

import java.io.Serializable;



class Gin extends KomaState implements Serializable{


	//コンストラクタ  状態を銀に
	Gin(Board board){
            super(board);
	}

	//抽象メソッドの実装 成った後の駒インスタンスを返す
	public KomaState changeState(Koma koma,Player owner){
		if(boardInstance.isNariFlag()){
			return new Narigin(boardInstance);
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
		return new Gin(boardInstance);
	}

	//抽象メソッドの実装 駒の名前を返す
	public String getNameState(){
		return "銀";
	}

	@Override
	public String getImageId(){
		return "gin";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[5][2];

		if(turn.getName().equals("先手")){			//先手
			//１マス前
			move[0][0] = -1;
			move[0][1] = 0;

			//１マス右斜め前
			move[1][0] = -1;
			move[1][1] = 1;

			//１マス右斜め後ろ
			move[2][0] = 1;
			move[2][1] = 1;

			//１マス左斜め後ろ
			move[3][0] = 1;
			move[3][1] = -1;

			//１マス左斜め前
			move[4][0] = -1;
			move[4][1] = -1;

			return move;

		}else{
			//後手
			//１マス前
			move[0][0] = 1;
			move[0][1] = 0;

			//１マス右斜め前
			move[1][0] = 1;
			move[1][1] = -1;

			//１マス右斜め後ろ
			move[2][0] = -1;
			move[2][1] = -1;

			//１マス左斜め後ろ
			move[3][0] = -1;
			move[3][1] = 1;

			//１マス左斜め前
			move[4][0] = 1;
			move[4][1] = 1;

			return move;

		}

	}

}
