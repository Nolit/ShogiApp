package com.uty.shogiapp.shogi;

import java.io.Serializable;

public class Player implements Serializable{
private static final String[] ORDERS = {"飛車","角","金","銀","桂馬","香車","歩"};
	private final String name;
    private Board boardInstance;
	public Koma[] mochiKoma = new Koma[8];
	public int[] mochiKomaNum = new int[8];

	public Player(String name){
		this.name = name;
	}
	//先手か後手かを返す
	public String getName(){
		return name;
	}

	//駒を動かす
	public void move(int beforeIndex,int afterIndex){
            System.out.println("move...");
		int beforeRow = ((beforeIndex/10)-1);
		int beforeCol = ((beforeIndex%10)-1);
		int afterRow = ((afterIndex/10)-1);
		int afterCol = ((afterIndex%10)-1);

		//ここから敵の駒を取った時の処理
		if(boardInstance.board[afterRow][afterCol] != null){
			this.addKoma(boardInstance.board[afterRow][afterCol]);
		}
		boardInstance.board[afterRow][afterCol] = boardInstance.board[beforeRow][beforeCol];
		boardInstance.board[beforeRow][beforeCol] = null;
		//ここまで敵の駒を取った時の処理
                System.out.println("afterRow:"+afterRow);
		System.out.println("afterCol:"+afterCol);
		//ここから成れるかの確認
		if(boardInstance.board[afterRow][afterCol].getOwner().getName().equals("先手")){

			if(afterRow < 3 || afterRow <3){
				boardInstance.board[afterRow][afterCol].change();
			}
		}else{
			if(afterRow > 5 || afterRow > 5){
				boardInstance.board[afterRow][afterCol].change();
			}
		}
		//ここまで成れるかの確認

		//一手前に動かした情報を保持させる
		boardInstance.setBeforeIndex(beforeIndex);
		boardInstance.setAfterIndex(afterIndex);

		//盤面を保存
        boardInstance.createClone();
	}

	//駒を置く
	public void put(int beforeIndex,int afterIndex){
		int afterRow = ((afterIndex/10)-1);
		int afterCol = ((afterIndex%10)-1);
		//置いた駒が持ち駒に一つしか無かった時の処理
		if(this.mochiKomaNum[beforeIndex] == 1){
			boardInstance.board[afterRow][afterCol] = this.mochiKoma[beforeIndex];
			for(int i=beforeIndex;i<this.mochiKoma.length-1;i++){
				this.mochiKoma[i] = this.mochiKoma[i+1];
				this.mochiKomaNum[i] = this.mochiKomaNum[i+1];
			}
		//置いた駒を複数持っていた時の処理
		}else{
			boardInstance.board[afterRow][afterCol] = this.mochiKoma[beforeIndex].copy();
			this.mochiKomaNum[beforeIndex]--;
		}

		//指した情報を保存しておく
		boardInstance.setBeforeIndex(beforeIndex);
		boardInstance.setAfterIndex(afterIndex);

		//盤面を保存
        boardInstance.createClone();

	}

	public void addKoma(Koma koma){
		koma.back();
		koma.setOwner(this);

		//ここから持ち駒を走査
		break_point:
		for(int i=0;i<this.mochiKoma.length;i++){

			//ここから取った駒より優先順位の高い駒が持ち駒になかった時の処理
			if(this.mochiKoma[i] == null){
				this.mochiKoma[i] = koma;
				this.mochiKomaNum[i] = 1;
				break break_point;
			}else{
				//ここまで取った駒より優先順位の高い駒が持ち駒になかった時の処理

				//ここから取った駒の優先順位に応じて持ち駒に加える
				for(String komaOrder : Player.ORDERS){
					if(komaOrder == this.mochiKoma[i].getName()){
						//取った駒を既に持っていた場合の処理
						if(this.mochiKoma[i].getName() == koma.getName()){
							mochiKomaNum[i]++;
							break break_point;
							//取った駒より優先順位の高い駒だったので次の持ち駒を探す
						}else{
							break;
						}
					}
					//ここから取った駒は持ち駒に無く、取った駒より優先順位の低い駒を持っていたため優先順位に沿った場所へ入れる処理
					if(komaOrder == koma.getName()){
						for(int j=this.mochiKoma.length-2;j>i+1;j--){
							System.out.println(j-1);
							this.mochiKoma[j-1] = this.mochiKoma[j-2];
							this.mochiKomaNum[j-1] = this.mochiKomaNum[j-2];
						}
						this.mochiKoma[i] = koma;
						this.mochiKomaNum[i] = 1;
						break break_point;
					}
					//ここまで取った駒は持ち駒に無く、取った駒より優先順位の低い駒を持っていたため優先順位に沿った場所へ入れる処理
				}
			}
		}
	}

    public void setBoard(Board board){
        this.boardInstance = board;
    }

	@Override
	public String toString(){
		return this.name;
	}
	/**
	 * mochiKomaを取得します。
	 * @return mochiKoma
	 */
	public Koma[] getMochiKoma() {
	    return mochiKoma;
	}
	/**
	 * mochiKomaを設定します。
	 * @param mochiKoma mochiKoma
	 */
	public void setMochiKoma(Koma[] mochiKoma) {
	    this.mochiKoma = mochiKoma;
	}
	/**
	 * mochiKomaNumを取得します。
	 * @return mochiKomaNum
	 */
	public int[] getMochiKomaNum() {
	    return mochiKomaNum;
	}
	/**
	 * mochiKomaNumを設定します。
	 * @param mochiKomaNum mochiKomaNum
	 */
	public void setMochiKomaNum(int[] mochiKomaNum) {
	    this.mochiKomaNum = mochiKomaNum;
	}

	public enum Type{
		SENTE,GOTE
	}
}
