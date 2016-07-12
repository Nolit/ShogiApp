package com.uty.shogiapp.shogi;

import java.io.Serializable;



class Hisha extends KomaState implements Serializable{


	//コンストラクタ  状態を飛車に
	Hisha(Board board){
            super(board);
	}

	//抽象メソッドの実装  成った後の駒インスタンスを返す
	public KomaState changeState(Koma koma,Player owner){
		if(boardInstance.isNariFlag()){
			return new Ryu(boardInstance);
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
		return new Hisha(boardInstance);
	}

	//抽象メソッドの実装　駒の名前を返す
	public String getNameState(){
		return "飛車";
	}

	@Override
	public String getImageId(){
		return "hisha";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[32][2];

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

			//１マス右
			move[8][0] = 0;
			move[8][1] = 1;

			//２マス右
			move[9][0] = 0;
			move[9][1] = 2;

			//３マス右
			move[10][0] = 0;
			move[10][1] = 3;

			//４マス右
			move[11][0] = 0;
			move[11][1] = 4;

			//５マス右
			move[12][0] = 0;
			move[12][1] = 5;

			//６マス右
			move[13][0] = 0;
			move[13][1] = 6;

			//７マス右
			move[14][0] = 0;
			move[14][1] = 7;

			//８マス右
			move[15][0] = 0;
			move[15][1] = 8;

			//１マス後ろ
			move[16][0] = 1;
			move[16][1] = 0;

			//２マス後ろ
			move[17][0] = 2;
			move[17][1] = 0;

			//３マス後ろ
			move[18][0] = 3;
			move[18][1] = 0;

			//４マス後ろ
			move[19][0] = 4;
			move[19][1] = 0;

			//５マス後ろ
			move[20][0] = 5;
			move[20][1] = 0;

			//６マス後ろ
			move[21][0] = 6;
			move[21][1] = 0;

			//７マス後ろ
			move[22][0] = 7;
			move[22][1] = 0;

			//８マス後ろ
			move[23][0] = 8;
			move[23][1] = 0;

			//１マス左
			move[24][0] = 0;
			move[24][1] = -1;

			//２マス左
			move[25][0] = 0;
			move[25][1] = -2;

			//３マス左
			move[26][0] = 0;
			move[26][1] = -3;

			//４マス左
			move[27][0] = 0;
			move[27][1] = -4;

			//５マス左
			move[28][0] = 0;
			move[28][1] = -5;

			//６マス左
			move[29][0] = 0;
			move[29][1] = -6;

			//７マス左
			move[30][0] = 0;
			move[30][1] = -7;

			//８マス左
			move[31][0] = 0;
			move[31][1] = -8;

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

			//１マス右
			move[8][0] = 0;
			move[8][1] = -1;

			//２マス右
			move[9][0] = 0;
			move[9][1] = -2;

			//３マス右
			move[10][0] = 0;
			move[10][1] = -3;

			//４マス右
			move[11][0] = 0;
			move[11][1] = -4;

			//５マス右
			move[12][0] = 0;
			move[12][1] = -5;

			//６マス右
			move[13][0] = 0;
			move[13][1] = -6;

			//７マス右
			move[14][0] = 0;
			move[14][1] = -7;

			//８マス右
			move[15][0] = 0;
			move[15][1] = -8;

			//１マス後ろ
			move[16][0] = -1;
			move[16][1] = 0;

			//２マス後ろ
			move[17][0] = -2;
			move[17][1] = 0;

			//３マス後ろ
			move[18][0] = -3;
			move[18][1] = 0;

			//４マス後ろ
			move[19][0] = -4;
			move[19][1] = 0;

			//５マス後ろ
			move[20][0] = -5;
			move[20][1] = 0;

			//６マス後ろ
			move[21][0] = -6;
			move[21][1] = 0;

			//７マス後ろ
			move[22][0] = -7;
			move[22][1] = 0;

			//８マス後ろ
			move[23][0] = -8;
			move[23][1] = 0;

			//１マス左
			move[24][0] = 0;
			move[24][1] = 1;

			//２マス左
			move[25][0] = 0;
			move[25][1] = 2;

			//３マス左
			move[26][0] = 0;
			move[26][1] = 3;

			//４マス左
			move[27][0] = 0;
			move[27][1] = 4;

			//５マス左
			move[28][0] = 0;
			move[28][1] = 5;

			//６マス左
			move[29][0] = 0;
			move[29][1] = 6;

			//７マス左
			move[30][0] = 0;
			move[30][1] = 7;

			//８マス左
			move[31][0] = 0;
			move[31][1] = 8;

			return move;

		}

	}

}
