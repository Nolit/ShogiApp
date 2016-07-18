package com.uty.shogiapp.shogi;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable{

	public Koma board[][] = new Koma[9][9];			//盤上の駒の配置を示す配列
        private final BoardKeeper boardKeeper = new BoardKeeper();
        private Player sente;
        private Player gote;
        private Player turn;
        private boolean endFlag = false;
        private boolean senteWinFlag;
        private boolean nariFlag = false;
        private boolean connectionLossFlag = false;
        private boolean sennichiteFlag = false;
        private int beforeIndex;
        private int afterIndex;

	public Board(){
	}

	public Board(Player sente,Player gote){
        //先手と後手をセットして初期化
        this.sente = sente;
        this.gote = gote;
        sente.setBoard(this);
        gote.setBoard(this);
        turn = sente;

//		//test  -->王手チェックのテスト
//		board[5][4] = new Koma(sente,new Gyoku());
//		board[4][4] = new Koma(sente,new Kin());
//		board[8][2] = new Koma(sente,new Kyosha());
//		board[0][4] = new Koma(gote,new Kyosha());
//		board[1][1] = new Koma(gote,new Gyoku());
//		board[1][0] = new Koma(gote,new Ryu());
//		board[0][0] = new Koma(gote,new Kaku());




//		 //test  -->詰みチェックのテスト<a3>
//		board[3][3] = new Koma(sente,new Kin());
//		board[3][5] = new Koma(sente,new Kin());
//		board[4][3] = new Koma(sente,new Kin());
//		board[4][5] = new Koma(sente,new Kin());
//		board[4][4] = new Koma(sente,new Gyoku());
//		board[5][3] = new Koma(sente,new Kyosha());
//		board[5][5] = new Koma(sente,new Kyosha());
//		board[5][4] = new Koma(sente,new Gin());
//		board[8][8] = new Koma(sente,new Fu());
//		board[3][4] = new Koma(sente,new Fu());
//
//		board[1][4] = new Koma(gote,new Kyosha());
//		board[2][4] = new Koma(gote,new Kyosha());
//		board[0][4] = new Koma(gote,new Gyoku());
//		board[4][0] = new Koma(gote,new Hisha());
//		board[4][8] = new Koma(gote,new Hisha());
//		board[1][1] = new Koma(gote,new Kaku());
//		board[1][7] = new Koma(gote,new Kaku());

//		 //test  -->詰みチェックのテスト<a5>
//		board[3][3] = new Koma(sente,new Kin());
//		board[3][5] = new Koma(sente,new Kin());
//		board[4][3] = new Koma(sente,new Kin());
//		board[4][5] = new Koma(sente,new Kin());
//		board[5][4] = new Koma(sente,new Gin());
//		board[4][4] = new Koma(sente,new Gyoku());
//		board[3][4] = new Koma(sente,new Fu());
//		board[5][3] = new Koma(sente,new Keima());
//		board[5][5] = new Koma(sente,new Keima());
//		board[8][8] = new Koma(sente,new Fu());
//		board[8][0] = new Koma(sente,new Kyosha());
//
//
//		board[0][4] = new Koma(gote,new Kyosha());
//		board[1][4] = new Koma(gote,new Kyosha());
//		board[0][0] = new Koma(gote,new Gyoku());
//		board[4][0] = new Koma(gote,new Hisha());
//		board[4][8] = new Koma(gote,new Hisha());
//		board[2][2] = new Koma(gote,new Kaku());
//		board[2][6] = new Koma(gote,new Kaku());



//		 //test  -->actionableCheck
//		board[8][0] = new Koma(sente,new Kyosha());
//		board[8][8] = new Koma(sente,new Gyoku());
//		board[0][4] = new Koma(gote,new Gyoku());
//		board[0][0] = new Koma(gote,new Kin());
//		board[0][1] = new Koma(gote,new Kin());
//		board[6][5] = new Koma(gote,new Uma());
//		board[0][7] = new Koma(gote,new Ryu());
//		board[6][3] = new Koma(gote,new Ryu());
	}

	//boardkeeper
	public void setKomas(Koma[][] komas){
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				board[i][j] = komas[i][j];
			}
		}
	}

	//玉の座標を返す
	public int[] getGyokuIndex(Player turn){

		//初期設定
		int GyokuIndex[] = new int[2];

		//現在のターンの玉の座標を取得する
		loop_GyokuIndex:
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				//対応する座標に現在ターンの玉が存在すれば分岐
				if(board[i][j] != null && board[i][j].getName() == "玉" && board[i][j].getOwner() == turn){
					GyokuIndex[0] = i;
					GyokuIndex[1] = j;
					break loop_GyokuIndex;				//発見次第for文を抜ける
				}
			}
		}
		return GyokuIndex;

	}

	//現在のターンの玉が詰んでいるかを調べ、詰んでいる場合はtrueを返す
	public boolean tsumiCheck(Player turn){		//----->publicをprivateに直す

		//初期設定
		int gyokuIndex[] = getGyokuIndex(turn);		//玉の座標を取得


		//アルゴリズム<a1>--- 玉の移動可能先が存在しない(条件1)
		int[] moveCheckEnable = moveCheck((gyokuIndex[0] + 1) * 10 + gyokuIndex[1] + 1);

		if(moveCheckEnable.length > 0){		//moveCheckEnable[0]に値が入っていれば分岐
			System.out.println("アルゴリズム<a1>でfalseを返してます。玉の移動可能先が存在します");			//test
			return false;					//移動可能先があるので詰みではない-->falseを返す
		}
		//アルゴリズム<a1>--- 終了

		//アルゴリズム<a2>--- 二重王手ではない(条件1補足)
		if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[1] != 0){
			//outeCheckの返り値の配列[1]がtrueならば分岐	-->二重王手である
			System.out.println("アルゴリズム<a2>でtrueを返してます。玉の移動可能先がなく、二重王手なので詰みです");			//test
			return true;		//詰みなのでtrueを返す
		}
		//アルゴリズム<a2>--- 終了

		//アルゴリズム<a3>--- 王手をかけてきている駒を取れない(条件2)

		//王手をかけてきている駒を設定
		int outeKomaIndex1,outeKomaIndex2;		//王手をかけてきている駒の座標を格納する変数
		outeKomaIndex1 = outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0];			//座標の代入
		outeKomaIndex2 = outeKomaIndex1 % 10 - 1;			//2ケタ座標の分解
		outeKomaIndex1 = outeKomaIndex1 / 10 - 1;			//2ケタ座標の分解
		Koma outeKoma = board[outeKomaIndex1][outeKomaIndex2];			//王手をかけてきている駒を代入

		int[] outeKomaOute = outeCheck(outeKomaIndex1,outeKomaIndex2,outeKoma);			//王手駒に対して利きのある(現在ターンの)駒の座標が入る
		int outeKomaOuteIndex1,outeKomaOuteIndex2;			//outeKomaOuteに入ってる駒座標の分解用
		for(int i=0; i<outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]]).length; i++){		//王手駒に利きのある駒の回数分回す
			//王手駒の座標に利いている駒で王手駒を取り、その瞬間に王手がかかっていなかったら詰みではない

			outeKomaOuteIndex1 = outeKomaOute[i];
			outeKomaOuteIndex2 = outeKomaOuteIndex1 % 10 - 1;	//2ケタ座標の分解
			outeKomaOuteIndex1 = outeKomaOuteIndex1 / 10 - 1;	//2ケタ座標の分解


			//盤上の保存
			boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);

			try
			{
				//駒移動
				board[outeKomaIndex1][outeKomaIndex2] = board[outeKomaOuteIndex1][outeKomaOuteIndex2];	//王手駒の存在する座標に王手駒に王手をかけている駒を移動する
				board[outeKomaOuteIndex1][outeKomaOuteIndex2] = null;

				//王手チェック
				if(outeCheck(getGyokuIndex(turn)[0],getGyokuIndex(turn)[1],board[getGyokuIndex(turn)[0]][getGyokuIndex(turn)[1]])[0] != 0){			//王手駒を駒移動で取った瞬間に玉を王手チェック
					//王手である場合は他の駒で取れるかを確認するため、continue

					//盤上を元の状態に戻す
					board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
					//boardKeeperを元の状態に戻す
					boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
					continue;
				}else{
					//盤上を元の状態に戻す
					board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
					//boardKeeperを元の状態に戻す
					boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
					System.out.println("アルゴリズム<a3>で2回目のfalseを返してます。王手駒を取れます");			//test
					return false;		//王手でなければ、王手駒を取れるので詰みではない
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//boardKeeperを元の状態に戻す
				boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
			}

		}		//for文ブロック
		//アルゴリズム<a3>--- 終了

		//アルゴリズム<a4>--- 直接王手ではない(条件2補足)
		if(outeKoma.getName() != "桂馬"){
			if(gyokuIndex[0] - outeKomaIndex1 == 1 || gyokuIndex[0] - outeKomaIndex1 == -1){	//差が1マスならtrueを返す
				System.out.println("アルゴリズム<a4>でtrueを返してます(桂馬以外の駒によって王手されています)。");		//test
				return true;
			}
			if(gyokuIndex[1] - outeKomaIndex2 == 1 || gyokuIndex[1] - outeKomaIndex2 == -1){	//差が1マスならtrueを返す
				System.out.println("アルゴリズム<a4>でtrueを返してます(桂馬以外の駒によって王手されています)");			//test
				return true;
			}
		}else{
			System.out.println("アルゴリズム<a4>でtrueを返してます(桂馬によって王手されています)");			//test
			return true;		//分岐条件は getName() == "桂馬"  -->桂馬は必ず直接王手なのでtrueを返す
		}
		//アルゴリズム<a4>--- 終了

		//アルゴリズム<a5>--- 駒移動で間接王手を防げる(条件3)
		ArrayList<Integer> putDefense = new ArrayList<Integer>();		//アルゴリズム<a7>以降で使うリストを生成しながらチェックしていく
		int DefenseKomaCounter = 0;		//王手チェックの返り値配列の要素が0になる要素を判定するために使う
		int start=0,end=0;		//繰り返し回数判定に使う
		int[] DefenseKoma;							//間接王手から玉を守る駒移動が可能な駒の座標が入る
		int DefenseKomaRow,DefenseKomaColumn;		//DefenseKomaの座標分解用
		if(outeKoma.getName() == "飛車" || outeKoma.getName() == "龍" || outeKoma.getName() == "香車"){		//間接王手の向きが縦方向か横方向の時分岐

			if(gyokuIndex[0] == outeKomaIndex1){		//玉と王手駒の行が同じ場合分岐
				if(gyokuIndex[1] > outeKomaIndex2){
					start = outeKomaIndex2;
					end = gyokuIndex[1];
				}else{
					start = gyokuIndex[1];
					end = outeKomaIndex2;
				}


				for(int i=start+1; i<end; i++){		//間接王手駒から玉までのマス数分繰り返す
					DefenseKoma = outeCheck(gyokuIndex[0],i,outeKoma);	//王手駒と玉の中間にあるマスに王手チェック出来る駒の代入
					putDefense.add((gyokuIndex[0]+1) * 10 + i + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化
					while(DefenseKoma[DefenseKomaCounter] != 0 && DefenseKomaCounter < 6){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);
						try
						{

							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[gyokuIndex[0]][i] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する-->行が一定
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0] != 0){	//間接王手を防ぐために駒移動した瞬間に王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
								return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
						}
					}
				}

			}else{		//玉と王手駒の列が同じ場合分岐
				if(gyokuIndex[0] > outeKomaIndex1){
					start = outeKomaIndex1;
					end = gyokuIndex[0];
				}else{
					start = gyokuIndex[0];
					end = outeKomaIndex1;
				}


				for(int i=start+1; i<end; i++){		//間接王手駒から玉までのマス数分繰り返す
					DefenseKoma = outeCheck(i,gyokuIndex[1],outeKoma);
					putDefense.add((i+1) * 10 + gyokuIndex[1] + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化
					while(DefenseKoma[DefenseKomaCounter] != 0 && DefenseKomaCounter < 6){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);
						try
						{
							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[i][gyokuIndex[1]] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する-->列が一定
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0] != 0){			//王手駒を駒移動で取った瞬間に玉を王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
								return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
							DefenseKomaCounter++;
						}
					}
				}
			}

		}else if(outeKoma.getName() == "角" || outeKoma.getName() == "馬"){				//間接王手の方向が斜めの場合分岐


			if(gyokuIndex[0] > outeKomaIndex1 && gyokuIndex[1] > outeKomaIndex2){		//行-->[玉 > 王手駒]	列-->[玉 > 王手駒]
				//斜め左上方向
				start = outeKomaIndex1;		//開始は王手駒の存在する行
				end = gyokuIndex[0];		//終了は玉の存在する行

				int tempColumn = outeKomaIndex2;		//この変数はここでしか使わない、方向特有の処理で使う
				for(int i=start+1; i<end; i++){		//間接王手駒から玉までのマス数分繰り返す
					tempColumn++;		//方向特有の処理
					DefenseKoma = outeCheck(i,tempColumn,outeKoma);	//王手駒と玉の中間にあるマスに王手チェック出来る駒の代入
					putDefense.add((i+1) * 10 + i + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化
					while(DefenseKoma[DefenseKomaCounter] != 0 && DefenseKomaCounter < 6){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);
						try
						{

							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[i][tempColumn] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(getGyokuIndex(turn)[0],getGyokuIndex(turn)[1],board[getGyokuIndex(turn)[0]][getGyokuIndex(turn)[1]])[0] != 0){	//間接王手を防ぐために駒移動した瞬間に王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
								return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
						}
					}
				}
			}else if(gyokuIndex[0] > outeKomaIndex1 && gyokuIndex[1] < outeKomaIndex2){		//行-->[玉 > 王手駒]	列-->[玉 < 王手駒]
					//斜め右上方向
					start = outeKomaIndex1;		//開始は王手駒の存在する行
					end = gyokuIndex[0];		//終了は玉の存在する行

					int tempColumn = outeKomaIndex2;	//この変数はここでしか使わない、方向特有の処理で使う
				for(int i=start+1; i<end; i++){			//間接王手駒から玉までのマス数分繰り返す
					tempColumn--;		//方向特有の処理
					DefenseKoma = outeCheck(i,tempColumn,outeKoma);	//王手駒と玉の中間にあるマスに王手チェック出来る駒の代入
					putDefense.add((i+1) * 10 + outeKomaIndex2 - i + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化

					while(DefenseKoma[DefenseKomaCounter] != 0){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);

						try
						{

							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[i][tempColumn] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0] != 0){	//間接王手を防ぐために駒移動した瞬間に王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
								return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
						}
					}
				}
			}else if(gyokuIndex[0] < outeKomaIndex1 && gyokuIndex[1] < outeKomaIndex2){		//行-->[玉 < 王手駒]	列-->[玉 < 王手駒]
				//斜め右下方向
				start = gyokuIndex[0];		//開始は玉の存在する行
				end = outeKomaIndex1;		//終了は王手駒の存在する行

				int tempColumn = gyokuIndex[1];		//この変数はここでしか使わない、方向特有の処理で使う
				for(int i=start+1; i<end; i++){		//間接王手駒から玉までのマス数分繰り返す
					tempColumn++;		//方向特有の処理
					DefenseKoma = outeCheck(i,tempColumn,outeKoma);	//王手駒と玉の中間にあるマスに王手チェック出来る駒の代入
					putDefense.add((i+1) * 10 + i + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化
					while(DefenseKoma[DefenseKomaCounter] != 0 && DefenseKomaCounter < 6){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);

						try
						{

							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[i][tempColumn] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0] != 0){	//間接王手を防ぐために駒移動した瞬間に王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
								return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
						}
					}
				}
			}else if(gyokuIndex[0] < outeKomaIndex1 && gyokuIndex[1] > outeKomaIndex2){		//行-->[玉 < 王手駒]	列-->[玉 > 王手駒]
				//斜め左下方向
				start = gyokuIndex[0];		//開始は玉の存在する行
				end = outeKomaIndex1;		//終了は王手駒の存在する行

				int tempColumn = gyokuIndex[1];		//この変数はここでしか使わない、方向特有の処理で使う
				for(int i=start+1; i<end; i++){		//間接王手駒から玉までのマス数分繰り返す
					tempColumn--;		//方向特有の処理
					DefenseKoma = outeCheck(i,tempColumn,outeKoma);	//王手駒と玉の中間にあるマスに王手チェック出来る駒の代入
					putDefense.add((i+1) * 10 + gyokuIndex[1] - i + 1);			//アルゴリズム<a7>以降で使用する
					DefenseKomaCounter = 0;			//DefenseKomaCounterの初期化
					while(DefenseKoma[DefenseKomaCounter] != 0 && DefenseKomaCounter < 6){		//DefenseKomaの駒の数だけ繰り返す

						DefenseKomaRow = DefenseKoma[DefenseKomaCounter];
						DefenseKomaColumn = DefenseKomaRow % 10 -1;
						DefenseKomaRow = DefenseKomaRow / 10 - 1;

						//盤上の保存
						boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);

						try
						{

							if(board[DefenseKomaRow][DefenseKomaColumn].getName() == "玉"){		//玉の場合は判定しない
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}

							//駒移動
							board[i][tempColumn] = board[DefenseKomaRow][DefenseKomaColumn];	//王手駒と玉の中間のマスにDefenseKomaを移動する
							board[DefenseKomaRow][DefenseKomaColumn] = null;

							//王手チェック
							if(outeCheck(gyokuIndex[0],gyokuIndex[1],board[gyokuIndex[0]][gyokuIndex[1]])[0] != 0){	//間接王手を防ぐために駒移動した瞬間に王手チェック
								//王手である場合は他の駒で取れるかを確認する

								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								DefenseKomaCounter++;
								continue;
							}else{
								//盤上を元の状態に戻す
								board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
								//boardKeeperを元の状態に戻す
								boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
								System.out.println("アルゴリズム<a5>でfalseを返してます。この間接王手は"+board[DefenseKomaRow][DefenseKomaColumn].getName()+"によって防げます");			//test
							return false;		//王手でなければ、間接王手を防げるので詰みではない
							}
						}catch(ArrayIndexOutOfBoundsException e)
						{
							//boardKeeperを元の状態に戻す
							boardKeeper.getKifu().remove(boardKeeper.getKifu().size());
						}
					}
				}
			}
		}
		//アルゴリズム<a5>--- 終了

		//アルゴリズム<a6>--- a6.持ち駒がある(条件3補足)
		//現在ターンの持ち駒情報をチェック用に複製する
		ArrayList<Koma> tempMochiKoma = new ArrayList<Koma>();	//以降持ち駒関連のチェックに使う
		int counter = 0;		//ループ処理の繰り返し回数の判定に使う
		while(turn.mochiKoma[counter] != null){
			tempMochiKoma.add(turn.mochiKoma[counter]);			//持ち駒に存在する駒をtempMochiKomaに追加していく
			counter++;
		}

		if(tempMochiKoma.size() == 0){
			System.out.println("アルゴリズム<a6>でtrueを返してます。持ち駒がないので間接王手を防げません");			//test
			return true;		//持ち駒がなければ間接王手を防げないので詰み
		}
		//アルゴリズム<a6>--- 終了

		//アルゴリズム<a7>--- 持っている駒が歩のみ(条件4)
		//アルゴリズム<a8>--- 王手を防げるマス全てがputCheckにひっかかる(条件4)
		if(tempMochiKoma.size() == 1){
			if(turn.mochiKoma[0].getName() == "歩"){
				int[] putCheckEnableFu = putCheck(turn.mochiKoma[0]);		//玉を防ぐマスに歩を打てるかputCheckする
				for(int i=0; i<putDefense.size(); i++){					//間接王手を防げるマス数分繰り返す
					for(int j=0; j<putCheckEnableFu.length; j++){			//putCheckから返ってきた駒の打てるマス分繰り返す
						if(putDefense.get(i) == putCheckEnableFu[j]){
							//王手を防ぐマスに駒打ちが可能なので、falseを返す
							System.out.println("アルゴリズム<a8>でfalseを返してます。" +putCheckEnableFu[j]+ "に歩を打って王手を防げます");			//test
							return false;
						}
					}
				}
				//王手を防げるマスに歩を撃てないのでtrueを返す
				System.out.println("アルゴリズム<a8>でtrueを返してます。間接王手を防ぐ手段がありません。");			//test
				return true;
			}
		}
		//アルゴリズム<a7>--- 終了
		//アルゴリズム<a8>--- 終了

		//アルゴリズム<a9>--- 持っている駒が歩,香,桂のいずれかのみ(条件4)
		//アルゴリズム<a10>--- 王手を防ぐ駒打ちが盤外チェックにひっかかる(条件4)
		ArrayList<Integer> putCheckNum = new ArrayList<Integer>();	//putCheckの返り値がここに入る
		if(tempMochiKoma.size() > 0 && tempMochiKoma.size() < 4){		//持ち駒の種類が3個以下
			for(int i=0; i<tempMochiKoma.size(); i++){			//駒の種類分繰り返す
				 if(turn.mochiKoma[i].getName() != "歩" && turn.mochiKoma[i].getName() != "香車" && turn.mochiKoma[i].getName() != "桂馬"){		//歩、香車、桂馬以外の駒が格納されている場合分岐
					 //歩、香車、桂馬以外の駒で盤外チェックにひっかかることはないのでfalseを返す
					 System.out.println("アルゴリズム<a9>でfalseを返してます。"+turn.mochiKoma[i].getName()+"を打って間接王手を防げます");			//test
					 return false;
				}else{		//持ち駒が歩、香車、桂馬のいずれかのみの場合分岐
					//turn.mochiKoma[i]のputCheckの返り値をlistに代入していく処理
					for(int listMake=0; listMake<putCheck(turn.mochiKoma[i]).length; listMake++){
						putCheckNum.add(putCheck(turn.mochiKoma[i])[listMake]);
					}

					//玉を守れる座標に駒打ちが可能か判定する処理
					for(int j=0; j<putDefense.size(); j++){				//間接王手を防げるマス数分繰り返す\
						for(int k=0; k<putCheckNum.size(); k++){		//putCheckから返ってきた駒の打てるマス分繰り返す
							if(putDefense.get(i) == putCheckNum.get(k)){
								//王手を防ぐマスに駒打ちが可能なので、falseを返す
								System.out.println("アルゴリズム<a10>でfalseを返してます。" +putDefense.get(j)+ "に"+ turn.mochiKoma[i].getName()+"を打って王手を防げます");			//test
								return false;
							}
						}
					}
					putCheckNum.clear();			//次の種類の駒のputCheckの返り値を格納するために、putCheckNumを初期化する
				}
			}
			System.out.println("アルゴリズム<a10>でtrueを返してます。駒移動で王手を防げず、かつ持ち駒にある"+tempMochiKoma.size()+"つの駒では王手を防げません");			//test
			return true;	//
		}
		//アルゴリズム<a9>--- 終了
		//アルゴリズム<a10>--- 終了
			System.out.println("アルゴリズム外でtrueを返しています。詰みチェックで詰み判定ができませんでした。");			//debug
			return true;	//エラー消し用
		/*
		 * 詰みの判定
		 * 条件1:玉の移動可能先がない(二重王手の場合は条件1のみを満たせば詰み)
		 * 条件2:王手をかけてきている駒を取れない(直接王手の場合は条件1と条件2を満たせば詰み)
		 * 条件3:間接王手を駒移動で防げない(持ち駒がない場合は条件1,2,3を満たせば詰み)
		 * 条件4:間接王手を駒打ちで防げない(持ち駒が歩、香車、桂馬のみで、これらの駒を打つとルール上反則負けになる場合)
		 * 全ての条件、あるいは特定の条件を満たせば詰み
		 *
		 * 詰みアルゴリズム	true-->詰み  false-->詰みではない
		 * a1.玉の移動可能先が存在しない(条件1)						yes:-->a2	no:falseをreturn
		 * a2.二重王手ではない(条件1補足)							yes:-->a3	no:trueをreturn
		 * a3.王手をかけてきている駒を取れない(条件2)				yes:-->a4	no:falseをreturn	---->例外1:王手をかけてきている駒を取れる駒が別の間接王手から守っている駒だった場合-->処理(4)を参照
		 * a4.直接王手ではない(条件2補足)							yes:-->a5	no:trueをreturn
		 * a5.駒移動で間接王手を防げる(条件3)						yes:-->a6	no:falseをreturn
		 * a6.持ち駒がある(条件3補足)								yes:-->a7	no:trueをreturn
		 * a7.持っている駒が歩のみ(条件4)							yes:-->a8	no:a9
		 * a8.王手を防げるマス全てがputCheckにひっかかる(条件4)		yes:-->trueを返す	no:falseを返す
		 * a9.持っている駒が歩,香,桂のいずれかのみ(条件4)			yes:-->a10	no:falseを返す
		 * a10.王手を防ぐ駒打ちが盤外チェックにひっかかる(条件4)	yes:-->trueを返す	no:falseを返す
		 *
		 * 処理
		 * turn.Change()が終わった直後に毎回tsumiCheckを呼び出す
		 *
		 * TsumiCheck {
		 *
		 * (1)-->moveCheck()に現在のターンの玉のindexを渡し、返ってきた配列の0番目が0ならば<a1>のyesへ
		 * (2)-->(1)で返ってきた配列の1番目が0ならば<a2>のyesへ
		 * (3)-->(1)で返ってきた配列の0番目をouteCheck(int,int,Koma)にかけ、その駒が王手状態ならば処理(4)へ
		 * (4)-->王手をかけてきている駒(以下「王手駒」)を王手チェックし、王手駒に対し利きがある駒があるかを判定する
		 *    -->王手チェックの返り値配列が0であれば詰み
		 *    -->王手チェックの返り値配列[0]に座標が入っていれば、その座標の駒を王手駒の存在するマスに移動させてから、現在ターンの王手を王手チェックする
		 *    -->王手でなければ詰みではない
		 *    -->王手であれば返り値配列[1]を見る
		 *    -->返り値配列[1]がnullならば上記と同様に詰みである
		 *    -->返り値配列[1]に座標が入っていれば、その座標の駒を王手駒の存在するマスに移動させてから、現在ターンの王手を王手チェックする
		 *    -->王手でなければ詰みではない
		 *    -->王手であれば返り値配列[2]を見る
		 *    -->返り値配列[2]がnullであれば詰み
		 *    -->返り値配列[2]が0でなければ詰みではない
		 *    -->同様に配列[4]まで繰り返し、王手ではない結果が出た場合はfalseを返す
		 *    -->王手駒に利きのある駒が5個以上存在する場合は、王手駒を取っても王手にならない駒が必ず存在するので詰みではない
		 *    -->全て結果が王手の場合は<a3>のyesへ
		 * }
		 *
		 *
		 * 王手をされました(outeCheck)
 		 * 駒を動かしたり置いたりするたびに王へ利いてる駒がないかを調べる
 		 * あるならviewのouteFlagをtrueにして詰みかどうか調べる(tsumiCheck)
 		 * ～～詰みじゃない場合～～
 		 * outeFlagがtrueの状態で駒を掴むとouteCheck(beforeIndex)呼び出します
 		 * outeCheckの中ではbeforeIndexにある駒の利きを調べます
 		 * boardを保存しておいてbeforeIndexから動ける場所へ動かします
 		 * 動かした後王へ駒が利いていないか調べます
 		 * 利いていないなら王は動かせるのでbeforeIndexからreturn用配列に入れます
 		 * 利いているなら王が取られるのでretun用配列には入れません
 		 * 動かせる範囲全部を調べたらreturn用配列を返します
 		 *
		 * 例）王手されている状態で駒を置くしか防ぐ手がない時は盤上のどの駒を掴んでもouteCheckからは空の配列が返って動かせません
 		 * 	  王手されている状態で持ち駒を掴んだ時は置ける場所が多いのでouteCheckの中でめっちゃ調べます
 		 * 詰みチェックはがんばりましょう！
		 *
		 */

	}

	//komaのmove[][]から実際に動ける範囲を絞って配列で返す
	public int[] moveCheck(int beforeindex){

		//プロセス1:チェックするための初期設定

		//引数の座標に存在する駒の取得
		int intBefore1 = (beforeindex/10)-1;
		int intBefore2 = (beforeindex%10)-1;
		Koma koma = board[intBefore1][intBefore2];

		String komaName = koma.getName();
		Player komaOwner = koma.getOwner();

		//駒の移動可能範囲を全て move に格納する
		int move[][] = koma.getAfterIndex();

		//move[][]の動けない座標に対応する要素をfalseにして判定するための配列
		boolean[] moveEnable = new boolean[move.length];

		//moveEnableの初期化 → 初期値は全てtrue
		for(int i=0; i<move.length; i++){
			moveEnable[i] = true;
		}


		//プロセス2:移動先までの途中に駒が存在しないか確認する処理
		Player indexOwner;														//移動範囲内に駒が存在した場合、その駒のOwnerを格納する変数

		//飛車、龍、角、馬、香車の時だけ分岐する
		if(komaName == "飛車" || komaName == "龍" || komaName == "角" || komaName == "馬" || komaName == "香車"){
			boolean presenceCheck = false;			//走査中に駒が存在した場合trueにする

			for(int i=0; i<move.length; i++){
				//move[][]の行が8になったら方向が変わるので、presenceCheckをfalseに戻す
				if(i%8 == 0 && presenceCheck){
					presenceCheck = false;
				}

				//龍と馬はmove[]の要素が36個あるので、最後の4つの要素はpresenceCheckの処理を省く
				if(i > 31){
					presenceCheck = false;
				}

				//presenceCheckがtrueになってたら分岐
				if(presenceCheck){
					moveEnable[i] = false;
					continue;
				}

				//移動可能座標がnullじゃなかった場合分岐
				try
				{
					if(board[intBefore1+ move[i][0]][intBefore2 + move[i][1]] != null && presenceCheck == false){
						presenceCheck = true;
						indexOwner = board[intBefore1+ move[i][0]][intBefore2 + move[i][1]].getOwner();			//対象座標の駒のOwner取得

						if(indexOwner == komaOwner){		//ownerが同じ(自分の駒)
							moveEnable[i] = false;
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
						//何もしない
				}
			}
		}else{				//飛車、龍、角、馬、香車以外の時分岐

			for(int i=0; i<move.length; i++){
				try
				{
					//移動可能座標がnullじゃなかった場合分岐
					if(board[intBefore1+ move[i][0]][intBefore2 + move[i][1]] != null){
						indexOwner = board[intBefore1 + move[i][0]][intBefore2 + move[i][1]].getOwner();			//対象座標の駒のOwner取得
							if(indexOwner == komaOwner){		//ownerが同じ(自分の駒)
							moveEnable[i] = false;
						}
					}

				}catch(ArrayIndexOutOfBoundsException e){
					//何もしない
				}
			}
		}

		//プロセス4:王手チェック
		if(koma.getName() == "玉"){				//選択した駒が玉の時分岐
			//玉の移動可能範囲内に、敵駒の利きがあるマスがあればそのマスを移動できないようにする(玉の移動可能範囲をさらに絞る)

			//盤上の保存
			boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);


			for(int i=0; i<8; i++){		//玉のmove.length --> 8回繰り返す
				if(moveEnable[i] == true){		//移動可能先ならば分岐
					try
					{
						//駒移動
						board[intBefore1 + move[i][0]][intBefore2 + move[i][1]] = koma;
						board[intBefore1][intBefore2] = null;

						//王手チェック
						if(outeCheck(intBefore1 + move[i][0],intBefore2 + move[i][1],koma)[0] != 0){			//移動先の座標が王手ゾーンならば分岐
							moveEnable[i] = false;
						}

						//盤上を元の状態に戻す
						board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;

					}catch(ArrayIndexOutOfBoundsException e)
					{
						//何もしない
					}
				}
			}

			//boardKeeperを元の状態に戻す
			boardKeeper.getKifu().remove(boardKeeper.getKifu().size());


		}else{				//選択した駒が玉の以外の時分岐

			//王手チェック2 駒移動によって自分の玉が王手にならないかチェック

			//盤上の保存
			boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);

			//玉の座標の取得
			int gyokuIndex[] = getGyokuIndex(koma.getOwner());

			for(int i=0; i<move.length; i++){
				if(moveEnable[i] == true){		//移動可能先ならば分岐
					try
					{
						//駒移動
						board[intBefore1 + move[i][0]][intBefore2 + move[i][1]] = koma;
						board[intBefore1][intBefore2] = null;

						//王手チェック
						if(outeCheck(gyokuIndex[0],gyokuIndex[1],koma)[0] != 0){
							moveEnable[i] = false;
						}

						//盤上を元の状態に戻す
						board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;

					}catch(ArrayIndexOutOfBoundsException e)
					{
						//何もしない
					}
				}
			}

			//boardKeeperを元の状態に戻す
			boardKeeper.getKifu().remove(boardKeeper.getKifu().size());

		}



		//プロセス4:moveEnable配列のデータを参照し、盤外チェックをしながらListを生成していく
		ArrayList<Integer> completeCheckMoveList = new ArrayList<Integer>();
		int tempRow = 0;								//行の初期化
		int tempColumn = 0;								//列の初期化

		//Listを作成していく
		for(int i=0; i<move.length; i++){
			if(moveEnable[i] == true){
				//行
				tempRow = intBefore1 + move[i][0];
				if(tempRow >= 0 && tempRow <=8){					//行の値が0~8なら真
					tempRow += 1;
					//列
					tempColumn = intBefore2 + move[i][1];
					if(tempColumn >=0 && tempColumn <=8){			//列の値が0~8なら真
						tempRow *= 10;
						tempColumn +=1;
						completeCheckMoveList.add(tempRow + tempColumn);
					}
				}
			}
		}


		//プロセス5:完成したリストを配列に変換し、呼び出し元へ返す
		int[] completeCheckMoveArray = new int[completeCheckMoveList.size()];		//return用

		 //Listから配列に変換
		for(int i=0; i<completeCheckMoveArray.length; i++){
			completeCheckMoveArray[i] = completeCheckMoveList.get(i);
		}

		return completeCheckMoveArray;

	}

	//moveCheckメソッドで呼び出し対象の座標が王手ゾーンならばtrueを返す
	public int[] outeCheck(int row,int column,Koma koma){		//privateに直す
		Player owner = koma.getOwner();	//ownerは現在のターンと同じ
		int[] oute = {0,0,0,0,0,0,0};		//return用	王手なら王手をかけている駒の座標を代入する
		//要素が7つの理由
		//詰みチェック以外では要素は1つでいいが、詰みチェックの二重王手の判定と王手駒を他の駒で取れるか確認する時に使う
		//二重王手は、玉の座標を引数に王手チェックをした時、返り値配列の要素2つ目に王手駒の座標が入っているかどうかで判定する
		//王手駒を他の駒で取れない場合は、開き王手がそれに当たる
		//王手駒を取ろうと駒移動した瞬間開き王手がかかるという状況は同時に最高で5つまでしか存在しないため
		//王手駒に対する王手を調べるための要素は5つでいいが(要素の最後まで王手駒の座標が入っているかで判定できる)
		//詰みチェックの仕様上、王手駒に対する王手に玉も含まれてしまうため、その場合を例外処理として扱い、6つ目の要素で判定する
		//7つ目はエラー消し用
		int element = 0;				//oute配列の要素番号を判定するのに使う
		String targetKomaName;			//移動可能先のマスに隣接している駒の名前

		//玉の移動先を調べるので玉クラスの移動範囲を受け取る
		int[] gyokuIndex = getGyokuIndex(owner);
		Koma Gyoku = board[gyokuIndex[0]][gyokuIndex[1]];
		int[][] perimetry = new int[Gyoku.getAfterIndex().length][2];
		perimetry = Gyoku.getAfterIndex();
		int outeIndex;					//王手をかけてきている駒の座標を代入する


		//直接王手ゾーンを調べる


		for(int i=0; i<8; i++){			//8方向を調べるので8回繰り返す
			try
			{
				if(board[row + perimetry[i][0]][column + perimetry[i][1]] != null){					//対称座標に駒が存在すれば分岐
					if(board[row + perimetry[i][0]][column + perimetry[i][1]].getOwner() != owner){	//対称座標の駒がownerと一致しなければ(敵の駒ならば)分岐
						targetKomaName = board[row + perimetry[i][0]][column + perimetry[i][1]].getName();

						switch(i){
						case 0:		//対象マスの1マス前
							if(targetKomaName != "桂馬" && targetKomaName != "角"){		//1マス前に進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 1:		//対象マスの1マス右斜め前
							if(targetKomaName != "歩" && targetKomaName != "香車" && targetKomaName != "桂馬" && targetKomaName != "飛車"){	//1マス右斜め前に進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 2:		//対象マスの1マス右
							if(targetKomaName != "歩" && targetKomaName != "香車" && targetKomaName != "桂馬" && targetKomaName != "銀" && targetKomaName != "角"){	//1マス右に進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 3:		//対象マスの１マス右斜め後ろ
							if(targetKomaName == "銀" || targetKomaName == "玉" || targetKomaName == "龍" || targetKomaName == "角" || targetKomaName == "馬"){	//１マス右斜め後ろに進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 4:		//対象マスの１マス後ろ
							if(targetKomaName != "歩" && targetKomaName != "香車" && targetKomaName != "桂馬" && targetKomaName != "銀" && targetKomaName != "角"){	//1マス後ろに進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 5:		//対象マスの１マス左斜め後ろ
							if(targetKomaName == "銀" || targetKomaName == "玉" || targetKomaName == "龍" || targetKomaName == "角" || targetKomaName == "馬"){	//１マス左斜め後ろに進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 6:		//対象マスの１マス左
							if(targetKomaName != "歩" && targetKomaName != "香車" && targetKomaName != "桂馬" && targetKomaName != "銀" && targetKomaName != "角"){	//1マス左に進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						case 7:		//対象マスの１マス左斜め前
							if(targetKomaName != "歩" && targetKomaName != "香車" && targetKomaName != "桂馬" && targetKomaName != "飛車"){	//1マス左斜め前に進める駒なら分岐
								outeIndex = (row + perimetry[i][0] + 1) * 10 + (column + perimetry[i][1]) + 1;
								oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
								element++;
							}
							break;
						}
					}
				}
				if(element == 6){		//利きが6つになれば配列を返す
					return oute;
				}
			}catch(ArrayIndexOutOfBoundsException e){
				//何もしない
			}
		}


		//桂馬のチェック		(上記のfor文では桂馬の利きをチェックしていないので、ここでチェックする)
		if(owner == sente){
			//先手視点
			try{
				//右
				if(board[row - 2][column + 1].getName() == "桂馬" && board[row - 2][column + 1].getOwner()!= owner){
					//(右)対称座標に桂馬が存在しownerと一致しなければ(敵の駒ならば)分岐
					outeIndex = (row - 2 + 1) * 10 + (column + 1 + 1);
					oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
					element++;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//何もしない
			}catch(NullPointerException e){
				//何もしない
			}

			try{
				//左
				if(board[row - 2][column - 1].getName() == "桂馬" && board[row - 2][column - 1].getOwner() != owner){
					//(左)対称座標に桂馬が存在しownerと一致しなければ(敵の駒ならば)分岐
						outeIndex = (row - 2 + 1) * 10 + (column -1 + 1);
					oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
					element++;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//何もしない
			}catch(NullPointerException e){
				//何もしない
			}

		//後手視点
		}else{
			try
			{
				if(board[row + 2][column + 1].getName() == "桂馬" && board[row + 2][column + 1].getOwner()!= owner){
					//(左)対称座標に桂馬が存在しownerと一致しなければ(敵の駒ならば)分岐
					outeIndex = (row + 2 + 1) * 10 + (column + 1 + 1);
					oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
					element++;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//何もしない
			}catch(NullPointerException e){
				//何もしない
			}

			try{
				if(board[row + 2][column - 1].getName() == "桂馬" && board[row + 2][column - 1].getOwner() != owner){
					//(右)対称座標に桂馬が存在しownerと一致しなければ(敵の駒ならば)分岐
					outeIndex = (row + 2 + 1) * 10 + (column -1 + 1);
					oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
					element++;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{
				//何もしない
			}catch(NullPointerException e){
				//何もしない
			}
		}




		if(element == 7){		//利きが7つになれば配列を返す
			return oute;
		}

		//間接王手ゾーンを調べる
		for(int i=0; i<8; i++){			//向き --> 8方向

			switch(i){
			case 0:		//上方向

				for(int j=1; j<9; j++){		//距離-->8マス(隣接している場合は直接王手チェックでtrueを返すので、2マス目から8距離調べる)
					try
					{
						if(board[row - j][column] != null){		//対象のマスに駒が存在すれば分岐
							targetKomaName = board[row - j][column].getName();

							if(board[row - j][column].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								if(targetKomaName == "飛車" || targetKomaName == "龍"){		//対象の駒が縦方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row - j + 1) * 10 + (column + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else if(targetKomaName == "香車"){
									if(owner == sente){
										if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
											//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
											//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
											//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
											//何もせずチェック方向を変えるためにbreakする
											break;
										}else{
											outeIndex = (row - j + 1) * 10 + (column + 1);
											oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
											element++;
											break;
										}
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 1:		//右斜め上方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row - j][column + j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row - j][column + j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row - j][column + j].getName();
								if(targetKomaName == "角" || targetKomaName == "馬"){
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row - j + 1) * 10 + (column + j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 2:		//右方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row][column + j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row][column + j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row][column + j].getName();
								if(targetKomaName == "飛車" || targetKomaName == "龍"){		//対象の駒が横方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row + 1) * 10 + (column + j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 3:		//右斜め下方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row + j][column + j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row + j][column + j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row + j][column + j].getName();
								if(targetKomaName == "角" || targetKomaName == "馬"){		//対象の駒が斜め方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row + j + 1) * 10 + (column + j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 4:		//下方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row + j][column] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row + j][column].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row + j][column].getName();
								if(targetKomaName == "飛車" || targetKomaName == "龍"){		//対象の駒が縦方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row + j + 1) * 10 + (column + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else if(targetKomaName == "香車"){		//香車の場合は、後手ターンの時だけ分岐
									if(owner == gote){
										if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
											//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
											//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
											//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
											//何もせずチェック方向を変えるためにbreakする
											break;
										}else{
											outeIndex = (row + j + 1) * 10 + (column + 1);
											oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
											element++;
											break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
										}
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 5:		//左斜め下方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row + j][column - j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row + j][column - j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row + j][column - j].getName();
								if(targetKomaName == "角" || targetKomaName == "馬"){		//対象の駒が斜め方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row + j + 1) * 10 + (column - j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 6:		//左方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row][column - j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row][column- j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row][column - j].getName();
								if(targetKomaName == "飛車" || targetKomaName == "龍"){		//対象の駒が横方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{outeIndex = (row + 1) * 10 + (column - j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			case 7:		//左斜め上方向
				for(int j=1; j<9; j++){		//距離-->8マス
					try
					{
						if(board[row - j][column - j] != null){		//対象のマスに駒が存在すれば分岐
							if(board[row - j][column - j].getOwner() != owner){	//駒のownerがターン側のものでなければ分岐(敵の駒なら分岐)
								targetKomaName = board[row - j][column - j].getName();
								if(targetKomaName == "角" || targetKomaName == "馬"){		//対象の駒が斜め方向に2マス以上移動出来る駒の場合分岐
									if(j == 1 && gyokuIndex[0] == row && gyokuIndex[1] == column){
										//rowとcolumnの値が玉の存在する座標と同じ場合は、呼び出し元がTsumiCheckのときである
										//この場合、j==1の時はすでに直接チェックで返り値配列に香車の座標が代入されている
										//したがって、二重王手判定に不具合が起きるので、このブロックのif文の条件がtrueの時は
										//何もせずチェック方向を変えるためにbreakする
										break;
									}else{
										outeIndex = (row - j + 1) * 10 + (column - j + 1);
										oute[element] = outeIndex;		//王手ゾーンなので王手をかけてきている駒の座標を代入する
										element++;
										break;			//王手ゾーンとわかればそれ以上のチェックは必要ない
									}
								}else{
									break;			//分岐条件以外の駒が存在した場合は駒の効きがそこで止まるので、それ以上のチェックは必要ない
								}
							}else{
								break;				//最初に存在した駒が自分の駒の場合はそれ以上のチェックは必要ない
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){
						break;		//盤外への処理となるので、向きを変えるために距離for文を抜ける
					}
				}
				break;
			}


			if(element == 6){		//利きが6つになれば配列を返す
				return oute;
			}

		}
		return oute;

	}

	//駒の打てる位置をチェックして配列で返す
	public int[] putCheck(Koma koma){

		//初期設定
		ArrayList<Integer> completeCheckPutList = new ArrayList<Integer>();
		String komaName = koma.getName();
		Player turn = koma.getOwner();

		boolean[][] putEnable = new boolean[9][9];	//盤上と対応させて、コマが打てない座標をfalseにする
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				//初期値はすべてtrue
				putEnable[i][j] = true;
			}
		}

		//盤外チェック
		if(komaName == "歩" || komaName == "香車" || komaName == "桂馬"){			//選択された駒が「歩」と「香車」と「桂馬」のとき分岐
			//先手ターンの時分岐
			if(turn == sente){
				//1行目を全てfalseにする
				for(int i=0; i<9; i++){
					putEnable[0][i] = false;
				}

				//駒が桂馬の時だけ分岐
				if(komaName == "桂馬"){
					for(int i=0; i<9; i++){
						putEnable[1][i] = false;		//桂馬の場合は2行目も全てfalseにする
					}
				}
			}

			//後手ターンの時分岐
			if(turn == gote){
				//9行目を全てfalseにする
				for(int i=0; i<9; i++){
					putEnable[8][i] = false;
				}

				//駒が桂馬の時だけ分岐
				if(komaName == "桂馬"){
					for(int i=0; i<9; i++){
						putEnable[7][i] = false;		//桂馬の場合は8行目も全てfalseにする
					}
				}
			}

		}

		//歩チェック
		if(komaName == "歩"){		//選択された駒が「歩」のとき分岐
			//二歩チェック
			for(int i=0; i<9; i++){			//列		列ごとに歩があるかチェックしていく
				for(int j=0; j<9; j++){		//行
					if(board[j][i] != null){			//対称座標に駒が存在するとき分岐

						//対称座標の駒のownerがターンと同じで、且つその駒が歩のとき分岐
						if(board[j][i].getOwner() == koma.getOwner() && board[j][i].getName() == "歩"){

							for(int allRow=0; allRow<9; allRow++){			//j列の全ての行をfalseにする
								putEnable[allRow][i] = false;
							}
							continue;		//次の列へ
						}
					}
				}
			}

			//打ち歩詰めのチェック

		}

		//他全部 盤上のnullじゃない場所をfalseにしていく
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				//対応するマスに駒が存在した場合分岐
				if(board[i][j] != null){
					putEnable[i][j] = false;
				}
			}
		}

		//王手チェック
		//玉の座標の取得
		int gyokuIndex[] = getGyokuIndex(koma.getOwner());

		//盤上の保存
		boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(putEnable[i][j] == true){		//移動可能先ならば分岐
						//駒打ち
						board[i][j] = koma;

						//王手チェック
						if(outeCheck(gyokuIndex[0],gyokuIndex[1],koma)[0] != 0){			//移動先の座標が王手ゾーンならば分岐
							putEnable[i][j] = false;
						}

						//盤上を元の状態に戻す
						board = boardKeeper.getClone(boardKeeper.getKifu().size()).board;
				}
			}
		}
		//boardKeeperを元の状態に戻す
		boardKeeper.getKifu().remove(boardKeeper.getKifu().size());

		//Listを作成していく
		int row = 0;				//行
		int column = 0;				//列
		int index = 0;				//座標
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				//putEnableを参照し、対応するマスがtrueならばListに追加する
				if(putEnable[i][j] == true){
					row = (i + 1) * 10;
					column = j + 1;
					index = row + column;
					completeCheckPutList.add(index);
				}
			}
		}

		 //Listから配列に変換
		int[] completeCheckPutArray = new int [completeCheckPutList.size()];	//return用
		for(int i=0; i<completeCheckPutArray.length; i++){
			completeCheckPutArray[i] = completeCheckPutList.get(i);
		}

		return completeCheckPutArray;

	}

	public boolean actionableCheck(Player turn){		//次に指せる手がない場合にtrueを返す


		//プロセス1: 玉の移動可能先がない
		int gyokuIndex[] = getGyokuIndex(turn);		//玉の座標を取得
		if(moveCheck((gyokuIndex[0] + 1) * 10 + gyokuIndex[1] + 1).length > 0){	//玉の移動先がなければ分岐

			//test
			System.out.println("玉が移動可能です。詰みではありません。");
			return false;					//玉が移動可能
		}

		//プロセス2: 持ち駒がない
		ArrayList<Koma> tempMochiKoma = new ArrayList<Koma>();
		int counter = 0;
		while(turn.mochiKoma[counter] != null){
			tempMochiKoma.add(turn.mochiKoma[counter]);			//持ち駒に存在する駒をtempMochiKomaに追加していく
			counter++;
		}

		if(tempMochiKoma.size() >= 1){		//持ち駒が１つ以上あれば分岐

			//test
			System.out.println("持ち駒から駒を打てます。詰みではありません。");
			return false;	//持ち駒が1以上あるので詰みではない
		}


		//プロセス3: 盤上に玉以外の動ける自分の駒が存在しない
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(board[i][j] != null && board[i][j].getOwner() == turn){
					if(moveCheck((i + 1) * 10 + j + 1).length > 0){

						//test
						System.out.println(board[i][j].getName() + "が移動可能です。詰みではありません。");
						return false;		//移動可能な駒が存在するので詰みではない
					}
				}
			}
		}

		return true;	//次に指せる手がないのでtrueを返す

	}

	//千日手をチェックする
	//このメソッドは盤面を動かした後に呼び出されます
	public void sennichiCheck(){

		int counter = 0;	//同じ局面が現れたら +1 (4になったら千日手)
		//比較するボード1
		Board latestBoard = boardKeeper.getKifu().get(boardKeeper.getKifu().size());
		//比較するボード2
		Board compareBoard;
		for(int i=0; i<boardKeeper.getKifu().size(); i++){		//開始局面から現在の手数までを全て比較していく
			compareBoard = boardKeeper.getKifu().get(i);
//			if(i手目の盤面 == 現在の盤面){		//同じ局面の場合分岐
//				counter++;
//
//				if(counter == 4){
//					sennichiteFlag = true;	//同じ盤面が4回現れたので千日手
//					break;
//				}
//			}
		}
	}
	//ボードの状態をboardKeeperクラスにコピーする
	public void createClone(){
		//盤面を保存
		boardKeeper.addKifuClone(boardKeeper.getKifu().size()+1,this);
	}

	public void setBoardFromBoardKeeper(int index){
		this.board = boardKeeper.getClone(index).board;
		this.sente.setMochiKoma(boardKeeper.getKifu().get(index).getSente().mochiKoma);
		this.gote.setMochiKoma(boardKeeper.getKifu().get(index).getGote().mochiKoma);
	}
    public void initBoard(Player sente,Player gote){
        this.sente = sente;
        this.gote = gote;
        this.turn = sente;
    }
    /**
	 * turnを取得します。
	 * @return turn
	 */
	public Player getTurn() {
	    return turn;
	}

	/**
	 * turnを設定します。
	 * @param turn turn
	 */
	public void setTurn(Player turn) {
	    this.turn = turn;
	}

	public boolean isEndFlag() {
        return endFlag;
    }

    public boolean isSenteWinFlag() {
        return senteWinFlag;
    }

    public void setEndFlag(boolean endFlag) {
        this.endFlag = endFlag;
    }

    public void setSenteWinFlag(boolean senteWinFlag) {
        this.senteWinFlag = senteWinFlag;
    }

    public BoardKeeper getBoardKeeper() {
        return boardKeeper;
    }

    public Player getSente() {
        return sente;
    }

    public Player getGote() {
        return gote;
    }

	/**
	 * nariFlagを取得します。
	 * @return nariFlag
	 */
	public boolean isNariFlag() {
	    return nariFlag;
	}

	/**
	 * nariFlagを設定します。
	 * @param nariFlag nariFlag
	 */
	public void setNariFlag(boolean nariFlag) {
	    this.nariFlag = nariFlag;
	}

    /**
	 * sennichiteFlagを取得します。
	 * @return sennichiteFlag
	 */
	public boolean isSennichiteFlag() {
	    return sennichiteFlag;
	}
	/**
	 * sennichiteFlagを設定します。
	 * @param sennichiteFlag sennichiteFlag
	 */
	public void setSennichiteFlag(boolean sennichiteFlag) {
	    this.sennichiteFlag = sennichiteFlag;
	}
	/**
	 * beforeIndexを取得します。
	 * @return beforeIndex
	 */
	public int getBeforeIndex() {
	    return beforeIndex;
	}
	/**
	 * beforeIndexを設定します。
	 * @param beforeIndex beforeIndex
	 */
	public void setBeforeIndex(int beforeIndex) {
	    this.beforeIndex = beforeIndex;
	}
	/**
	 * afterIndexを取得します。
	 * @return afterIndex
	 */
	public int getAfterIndex() {
	    return afterIndex;
	}
	/**
	 * afterIndexを設定します。
	 * @param afterIndex afterIndex
	 */
	public void setAfterIndex(int afterIndex) {
	    this.afterIndex = afterIndex;
	}
	public boolean isOuteFlag() {
        return outeFlag;
    }

    public void setOuteFlag(boolean outeFlag) {
        this.outeFlag = outeFlag;
    }
        private boolean outeFlag = false;

    public boolean isConnectionLossFlag() {
        return connectionLossFlag;
    }

    public void setConnectionLossFlag(boolean connectionLossFlag) {
        this.connectionLossFlag = connectionLossFlag;
    }

}


