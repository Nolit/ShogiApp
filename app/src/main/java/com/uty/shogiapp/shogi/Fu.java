package com.uty.shogiapp.shogi;


import java.io.Serializable;

public class Fu extends KomaState implements Serializable{
	//コンストラクタ  状態を歩に
	public Fu(Board board){
            super(board);
	}

	//抽象メソッドの実装  成った後の駒インスタンスを返す
	public KomaState changeState(Koma koma,Player owner){
		if(owner.getName().equals("先手")){
			for(int i=0;i<9;i++){
				if(boardInstance.board[0][i] == koma){
                                    System.out.println("先手で成り");
					return new Tokin(boardInstance);
				}
			}
		}else{
			for(int i=0;i<9;i++){
				if(boardInstance.board[8][i] == koma){
                                    System.out.println("後手で成り");
					return new Tokin(boardInstance);
				}
			}
		}

		if(boardInstance.isNariFlag()){
			return new Tokin(boardInstance);
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
		return new Fu(boardInstance);
	}

	//抽象メソッドの実装　駒の名前を返す
	public String getNameState(){
		return "歩";
	}

	@Override
	public String getImageId(){
		return "fu";
	}

	//移動できる範囲を返す
	public int[][] getAfterIndexState(Player turn){
		int[][] move = new int[1][2];
		if(turn.getName().equals("先手")){	//先手
			//１マス前
			move[0][0] = -1;
			move[0][1] = 0;
			return move;
		}else{						//後手
			//１マス前
			move[0][0] = 1;
			move[0][1] = 0;

			return move;
		}
	}

}
