package com.uty.shogiapp.shogi

import java.net.URL
import com.orangesignal.csv.Csv
import com.orangesignal.csv.CsvConfig
import com.orangesignal.csv.handlers.StringArrayListHandler
import java.io.File

class BoardBuilder{
    private val sente:Player = (Player("先手"))
    private val gote:Player = (Player("後手"))
    val boardInstance:Board = Board(sente, gote)

    //通常対局時のデフォルトの配置
    fun default(){
        boardInstance.turn = sente

        for (i in 0..8) {
            boardInstance.board[6][i] = Koma(sente, Fu(boardInstance))
        }

        for (i in 0..8) {
            boardInstance.board[2][i] = Koma(gote, Fu(boardInstance))
        }

        boardInstance.board[8][0] = Koma(sente, Kyosha(boardInstance))
        boardInstance.board[8][8] = Koma(sente, Kyosha(boardInstance))
        boardInstance.board[0][0] = Koma(gote, Kyosha(boardInstance))
        boardInstance.board[0][8] = Koma(gote, Kyosha(boardInstance))

        boardInstance.board[8][1] = Koma(sente, Keima(boardInstance))
        boardInstance.board[8][7] = Koma(sente, Keima(boardInstance))
        boardInstance.board[0][1] = Koma(gote, Keima(boardInstance))
        boardInstance.board[0][7] = Koma(gote, Keima(boardInstance))

        boardInstance.board[8][2] = Koma(sente, Gin(boardInstance))
        boardInstance.board[8][6] = Koma(sente, Gin(boardInstance))
        boardInstance.board[0][2] = Koma(gote, Gin(boardInstance))
        boardInstance.board[0][6] = Koma(gote, Gin(boardInstance))

        boardInstance.board[8][3] = Koma(sente, Kin(boardInstance))
        boardInstance.board[8][5] = Koma(sente, Kin(boardInstance))
        boardInstance.board[0][3] = Koma(gote, Kin(boardInstance))
        boardInstance.board[0][5] = Koma(gote, Kin(boardInstance))

        boardInstance.board[7][7] = Koma(sente, Hisha(boardInstance))
        boardInstance.board[1][1] = Koma(gote, Hisha(boardInstance))

        boardInstance.board[7][1] = Koma(sente, Kaku(boardInstance))
        boardInstance.board[1][7] = Koma(gote, Kaku(boardInstance))

        boardInstance.board[8][4] = Koma(sente, Gyoku(boardInstance))
        boardInstance.board[0][4] = Koma(gote, Gyoku(boardInstance))

        //初期の盤面保存
        boardInstance.createClone()
    }
    
    fun csv(url: URL){
        val csv:List<Array<String>> = Csv.load(File("C:\\Users\\karin757\\Desktop\\shogi.csv"), CsvConfig(), StringArrayListHandler())

    }

    fun ki2(url: URL){
        //ファイルを読み込んでBoardを変えていく
    }

    fun kif(url: URL){
        //ファイルを読み込んでBoardを変えていく
    }
}
