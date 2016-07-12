package com.uty.shogiapp.shogi;

import java.io.FileNotFoundException;
import java.io.IOException;

class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
            Player sente = new Player("先手");
            Player gote = new Player("後手");
            Board board = new Board(sente,gote);
            disp(board);
	}
        public static void disp(Board board){
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(board.board[i][j] == null){
                        System.out.print("○");
                    }else{
                        System.out.print(board.board[i][j].getName().charAt(0));
                    }
                }
                System.out.println();
            }
        }

}