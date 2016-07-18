package com.uty.shogiapp.shogi

data class BoardJson(
    val boardJsonList:Array<BoardKomaJson>,
    val senteJsonList:Array<MochikomaJson>,
    val goteJsonList:Array<MochikomaJson>
)

data class BoardKomaJson(
        val index:Int,
        val name:String,
        val player:String
)

data class MochikomaJson(
        val name:String,
        val amount:Int
)