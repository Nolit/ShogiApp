package com.uty.shogiapp.shogi;

import java.io.Serializable;



class Keima extends KomaState implements Serializable{

	//コンストラクタ  状態を桂馬に
	Keima(Board board){
            super(board);
	}

	//抽象メソッドの実装 成った後の駒インスタンスを返す
	public KomaState changeState(Koma koma,Player owner){
		if(owner.getName().equals("先手")){
			for(int i=0;i<9;i++){
				for(int j=0;j<2;j++){
					if(boardInstance.board[j][i] == koma){
						System.out.println("成ります");
						return new Narikei(boardInstance);
					}
				}
			}
		}else{
			for(int i=0;i<9;i++){
				for(int j=7;j<9;j++){
					if(boardInstance.board[j][i] == koma){
						System.out.println("成ります");
						return new Narikei(boardInstance);
					}
				}
			}
		}
		if(boardInstance.isNariFlag()){
			return new Narikei(boardInstance);
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
		return new Keima(boardInstance);
	}

	//抽象メソッドの実装 駒の名前を返す
	public String getNameState(){
		 return "桂馬";
	}

	@Override
	public String getImageId(){
		return "keima";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[2][2];

		if(turn.getName().equals("先手")){	//先手

			//２マス前　１マス左
			move[0][0] = -2;
			move[0][1] = -1;

			//２マス前　１マス右
			move[1][0] = -2;
			move[1][1] = 1;

			return move;

		}else{						//後手
			//２マス前　１マス左
			move[0][0] = 2;
			move[0][1] = 1;

			//２マス前　１マス右
			move[1][0] = 2;
			move[1][1] = -1;

			return move;

		}

	}

}
