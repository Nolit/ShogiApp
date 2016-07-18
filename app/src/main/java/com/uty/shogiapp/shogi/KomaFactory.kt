package com.uty.shogiapp.shogi

class KomaFactory(val sente:Player, val gote:Player, val boardInstance:Board){

    fun createKoma(komaType: Koma.Type, playerType: Player.Type): Koma{
        val player:Player = when(playerType){
            Player.Type.SENTE -> sente
            Player.Type.GOTE -> gote
        }

        return when(komaType) {
            Koma.Type.FU -> Koma(player, Fu(boardInstance))
            Koma.Type.KYOSHA -> Koma(player, Kyosha(boardInstance))
            Koma.Type.KEIMA -> Koma(player, Keima(boardInstance))
            Koma.Type.GIN -> Koma(player, Gin(boardInstance))
            Koma.Type.KIN -> Koma(player, Kin(boardInstance))
            Koma.Type.KAKU -> Koma(player, Kaku(boardInstance))
            Koma.Type.HISHA -> Koma(player, Hisha(boardInstance))
            Koma.Type.GYOKU -> Koma(player, Gyoku(boardInstance))
            Koma.Type.TOKIN -> Koma(player, Tokin(boardInstance))
            Koma.Type.NARIKYO -> Koma(player, Narikyo(boardInstance))
            Koma.Type.NARIKEI -> Koma(player, Narikei(boardInstance))
            Koma.Type.NARIGIN -> Koma(player, Narigin(boardInstance))
            Koma.Type.UMA -> Koma(player, Uma(boardInstance))
            Koma.Type.RYU -> Koma(player, Ryu(boardInstance))
        }
    }
}
