package com.uty.shogiapp.shogi;

import java.io.Serializable;

public class Koma implements Cloneable,Serializable{
	private Player owner;
	private KomaState state;

	public Koma(Player owner,KomaState state){
		this.owner = owner;
		this.state = state;
	}

	public void setOwner(Player player){
		this.owner = player;
	}

	public Player getOwner(){
		return this.owner;
	}

	public String getName(){
		return state.getNameState();
	}

	public void change(){
		this.state = state.changeState(this,this.owner);
	}

	public void back(){
		this.state = state.backState();
	}

	public Koma copy(){
		return new Koma(this.owner,state.copyState());
	}

	public String getImageId(){
		return state.getImageId();
	}

	public int[][] getAfterIndex(){
		return state.getAfterIndexState(owner);		//確認	7/3 owner入れる意味あるかどうか
	}

	@Override
	public Koma clone(){
		Koma komaClone = null;
		try {
			komaClone = (Koma) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		komaClone.state = (KomaState)komaClone.state.clone();
		return komaClone;
	}

	public Board getBoard(){
		return state.getBoard();
	}

	public enum Type{
		FU,KYOSHA,KEIMA,GIN,KIN,KAKU,HISHA,GYOKU
	}
}
