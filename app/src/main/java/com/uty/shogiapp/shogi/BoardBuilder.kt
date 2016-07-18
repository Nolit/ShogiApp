package com.uty.shogiapp.shogi

import java.net.URL
import com.squareup.moshi.Moshi
import java.io.File

class BoardBuilder{
    private val sente:Player = (Player("先手"))
    private val gote:Player = (Player("後手"))
    val boardInstance:Board = Board(sente, gote)

    //通常対局時のデフォルトの配置
    fun default(){
        boardInstance.turn = sente

        val factory:KomaFactory = KomaFactory(sente, gote, boardInstance)

        for (i in 0..8) {
            boardInstance.board[6][i] = factory.createKoma(Koma.Type.FU, Player.Type.SENTE)
        }

        for (i in 0..8) {
            boardInstance.board[2][i] = factory.createKoma(Koma.Type.FU, Player.Type.GOTE)
        }

        boardInstance.board[8][0] = factory.createKoma(Koma.Type.KYOSHA, Player.Type.SENTE)
        boardInstance.board[8][8] = factory.createKoma(Koma.Type.KYOSHA, Player.Type.SENTE)
        boardInstance.board[0][0] = factory.createKoma(Koma.Type.KYOSHA, Player.Type.GOTE)
        boardInstance.board[0][8] = factory.createKoma(Koma.Type.KYOSHA, Player.Type.GOTE)

        boardInstance.board[8][1] = factory.createKoma(Koma.Type.KEIMA, Player.Type.SENTE)
        boardInstance.board[8][7] = factory.createKoma(Koma.Type.KEIMA, Player.Type.SENTE)
        boardInstance.board[0][1] = factory.createKoma(Koma.Type.KEIMA, Player.Type.GOTE)
        boardInstance.board[0][7] = factory.createKoma(Koma.Type.KEIMA, Player.Type.GOTE)

        boardInstance.board[8][2] = factory.createKoma(Koma.Type.GIN, Player.Type.SENTE)
        boardInstance.board[8][6] = factory.createKoma(Koma.Type.GIN, Player.Type.SENTE)
        boardInstance.board[0][2] = factory.createKoma(Koma.Type.GIN, Player.Type.GOTE)
        boardInstance.board[0][6] = factory.createKoma(Koma.Type.GIN, Player.Type.GOTE)

        boardInstance.board[8][3] = factory.createKoma(Koma.Type.KIN, Player.Type.SENTE)
        boardInstance.board[8][5] = factory.createKoma(Koma.Type.KIN, Player.Type.SENTE)
        boardInstance.board[0][3] = factory.createKoma(Koma.Type.KIN, Player.Type.GOTE)
        boardInstance.board[0][5] = factory.createKoma(Koma.Type.KIN, Player.Type.GOTE)

        boardInstance.board[7][7] = factory.createKoma(Koma.Type.HISHA, Player.Type.SENTE)
        boardInstance.board[1][1] = factory.createKoma(Koma.Type.HISHA, Player.Type.GOTE)

        boardInstance.board[7][1] = factory.createKoma(Koma.Type.KAKU, Player.Type.SENTE)
        boardInstance.board[1][7] = factory.createKoma(Koma.Type.KAKU, Player.Type.GOTE)

        boardInstance.board[8][4] = factory.createKoma(Koma.Type.GYOKU, Player.Type.SENTE)
        boardInstance.board[0][4] = factory.createKoma(Koma.Type.GYOKU, Player.Type.GOTE)

        //初期の盤面保存
        boardInstance.createClone()
    }
    
    fun json(url: String){
        val factory: KomaFactory = KomaFactory(sente, gote, boardInstance)

        val jsonAdapter = Moshi.Builder().build().adapter(BoardJson::class.java)
        val jsonData = File(url).readLines().fold("") { s1, s2 -> "$s1$s2" }
        val boardJson:BoardJson = jsonAdapter.fromJson(jsonData)

        for (komaJson in boardJson.boardJsonList) {
            val komaType = Koma.Type.valueOf(komaJson.name)
            val playerType = Player.Type.valueOf(komaJson.player)
            val x = parseX(komaJson.index)
            val y = parseY(komaJson.index)
            boardInstance.board[y][x] = factory.createKoma(komaType, playerType)
        }

        for (komaJson in boardJson.senteJsonList) {
            val komaType = Koma.Type.valueOf(komaJson.name)
            for(i in 1..komaJson.amount){
                sente.addKoma(factory.createKoma(komaType, Player.Type.SENTE))
            }
        }

        for (komaJson in boardJson.goteJsonList) {
            val komaType = Koma.Type.valueOf(komaJson.name)
            for(i in 1..komaJson.amount){
                gote.addKoma(factory.createKoma(komaType, Player.Type.GOTE))
            }
        }
    }

    fun ki2(url: URL){
        //ki2ファイルを読み込んでBoardを変えていく
    }

    fun kif(url: URL){
        //kifファイルを読み込んでBoardを変えていく
    }

    private fun parseX(x: Int): Int{
        return 9 - (x/10)
    }
    private fun parseY(y: Int): Int{
        return (y%10) - 1
    }
}
