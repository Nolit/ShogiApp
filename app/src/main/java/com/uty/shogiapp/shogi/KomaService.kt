package com.uty.shogiapp.shogi

fun getKomaName(koma: Koma): String{
    return when(koma.state) {
        is Fu -> "Fu"
        is Kyosha -> "KYOSHA"
        is Keima -> "KEIMA"
        is Gin -> "GIN"
        is Kin -> "KIN"
        is Hisha -> "HISHA"
        is Kaku -> "KAKU"
        else -> "成駒？"
    }
}
