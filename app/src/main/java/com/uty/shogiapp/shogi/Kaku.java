package com.uty.shogiapp.shogi;

import java.io.Serializable;



class Kaku extends KomaState implements Serializable{


	//コンストラクタ  状態を角に
	Kaku(Board board){
            super(board);
	}

	//抽象メソッドの実装  成った後の駒インスタンスを返す
	public KomaState changeState(Koma koma,Player owner){
		if(boardInstance.isNariFlag()){
			return new Uma(boardInstance);
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
		return new Kaku(boardInstance);
	}

	//抽象メソッドの実装　駒の名前を返す
	public String getNameState(){
		return "角";
	}

	@Override
	public String getImageId(){
		return "kaku";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[32][2];

		if(turn.getName().equals("先手")){	//先手

			//１マス右斜め前
			move[0][0] = -1;
			move[0][1] = 1;

			//２マス右斜め前
			move[1][0] = -2;
			move[1][1] = 2;

			//３マス右斜め前
			move[2][0] = -3;
			move[2][1] = 3;

			//４マス右斜め前
			move[3][0] = -4;
			move[3][1] = 4;

			//５マス右斜め前
			move[4][0] = -5;
			move[4][1] = 5;

			//６マス右斜め前
			move[5][0] = -6;
			move[5][1] = 6;

			//７マス右斜め前
			move[6][0] = -7;
			move[6][1] = 7;

			//８マス右斜め前
			move[7][0] = -8;
			move[7][1] = 8;

			//１マス右斜め後ろ
			move[8][0] = 1;
			move[8][1] = 1;

			//２マス右斜め後ろ
			move[9][0] = 2;
			move[9][1] = 2;

			//３マス右斜め後ろ
			move[10][0] = 3;
			move[10][1] = 3;

			//４マス右斜め後ろ
			move[11][0] = 4;
			move[11][1] = 4;

			//５マス右斜め後ろ
			move[12][0] = 5;
			move[12][1] = 5;

			//６マス右斜め後ろ
			move[13][0] = 6;
			move[13][1] = 6;

			//７マス右斜め後ろ
			move[14][0] = 7;
			move[14][1] = 7;

			//８マス右斜め後ろ
			move[15][0] = 8;
			move[15][1] = 8;

			//１マス左斜め後ろ
			move[16][0] = 1;
			move[16][1] = -1;

			//２マス左斜め後ろ
			move[17][0] = 2;
			move[17][1] = -2;

			//３マス左斜め後ろ
			move[18][0] = 3;
			move[18][1] = -3;

			//４マス左斜め後ろ
			move[19][0] = 4;
			move[19][1] = -4;

			//５マス左斜め後ろ
			move[20][0] = 5;
			move[20][1] = -5;

			//６マス左斜め後ろ
			move[21][0] = 6;
			move[21][1] = -6;

			//７マス左斜め後ろ
			move[22][0] = 7;
			move[22][1] = -7;

			//８マス左斜め後ろ
			move[23][0] = 8;
			move[23][1] = -8;

			//１マス左斜め前
			move[24][0] = -1;
			move[24][1] = -1;

			//２マス左斜め前
			move[25][0] = -2;
			move[25][1] = -2;

			//３マス左斜め前
			move[26][0] = -3;
			move[26][1] = -3;

			//４マス左斜め前
			move[27][0] = -4;
			move[27][1] = -4;

			//５マス左斜め前
			move[28][0] = -5;
			move[28][1] = -5;

			//６マス左斜め前
			move[29][0] = -6;
			move[29][1] = -6;

			//７マス左斜め前
			move[30][0] = -7;
			move[30][1] = -7;

			//８マス左斜め前
			move[31][0] = -8;
			move[31][1] = -8;

			return move;

		}else{					//後手

			//１マス右斜め前
			move[0][0] = 1;
			move[0][1] = -1;

			//２マス右斜め前
			move[1][0] = 2;
			move[1][1] = -2;

			//３マス右斜め前
			move[2][0] = 3;
			move[2][1] = -3;

			//４マス右斜め前
			move[3][0] = 4;
			move[3][1] = -4;

			//５マス右斜め前
			move[4][0] = 5;
			move[4][1] = -5;

			//６マス右斜め前
			move[5][0] = 6;
			move[5][1] = -6;

			//７マス右斜め前
			move[6][0] = 7;
			move[6][1] = -7;

			//８マス右斜め前
			move[7][0] = -8;
			move[7][1] = -8;

			//１マス右斜め後ろ
			move[8][0] = -1;
			move[8][1] = -1;

			//２マス右斜め後ろ
			move[9][0] = -2;
			move[9][1] = -2;

			//３マス右斜め後ろ
			move[10][0] = -3;
			move[10][1] = -3;

			//４マス右斜め後ろ
			move[11][0] = -4;
			move[11][1] = -4;

			//５マス右斜め後ろ
			move[12][0] = -5;
			move[12][1] = -5;

			//６マス右斜め後ろ
			move[13][0] = -6;
			move[13][1] = -6;

			//７マス右斜め後ろ
			move[14][0] = -7;
			move[14][1] = -7;

			//８マス右斜め後ろ
			move[15][0] = -8;
			move[15][1] = -8;

			//１マス左斜め後ろ
			move[16][0] = -1;
			move[16][1] = 1;

			//２マス左斜め後ろ
			move[17][0] = -2;
			move[17][1] = 2;

			//３マス左斜め後ろ
			move[18][0] = -3;
			move[18][1] = 3;

			//４マス左斜め後ろ
			move[19][0] = -4;
			move[19][1] = 4;

			//５マス左斜め後ろ
			move[20][0] = -5;
			move[20][1] = 5;

			//６マス左斜め後ろ
			move[21][0] = -6;
			move[21][1] = 6;

			//７マス左斜め後ろ
			move[22][0] = -7;
			move[22][1] = 7;

			//８マス左斜め後ろ
			move[23][0] = -8;
			move[23][1] = 8;

			//１マス左斜め前
			move[24][0] = 1;
			move[24][1] = 1;

			//２マス左斜め前
			move[25][0] = 2;
			move[25][1] = 2;

			//３マス左斜め前
			move[26][0] = 3;
			move[26][1] = 3;

			//４マス左斜め前
			move[27][0] = 4;
			move[27][1] = 4;

			//５マス左斜め前
			move[28][0] = 5;
			move[28][1] = 5;

			//６マス左斜め前
			move[29][0] = 6;
			move[29][1] = 6;

			//７マス左斜め前
			move[30][0] = 7;
			move[30][1] = 7;

			//８マス左斜め前
			move[31][0] = 8;
			move[31][1] = 8;

			return move;

		}
	}
}
