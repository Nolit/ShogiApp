package com.uty.shogiapp.shogi;

import java.io.Serializable;

abstract class KomaState implements Cloneable,Serializable{
        protected Board boardInstance;
	//駒に対応する画像のパスを返す
	abstract public String getImageId();
	//駒の移動可能範囲を返す
	abstract public int[][] getAfterIndexState(Player turn);
	//駒の名前を返す
	abstract public String getNameState();
	//成った後の駒インスタンスを返す
	abstract public KomaState changeState(Koma koma,Player owner);
	//成る前の駒インスタンスを返す
	abstract public KomaState backState();
	//新しい駒インスタンスを返す
	abstract public KomaState copyState();

        KomaState(){

        }
        KomaState(Board board){
            this.boardInstance = board;
        }

	@Override
	public KomaState clone(){
		KomaState komaStateClone = null;
		try {
			komaStateClone = (KomaState)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return komaStateClone;
	}

	public Board getBoard(){
		return boardInstance;
	}
}
