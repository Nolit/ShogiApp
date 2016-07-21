package com.uty.shogiapp.shogi

data class ProblemJson(
        val boardJsonList: Array<BoardKomaJson>,
        val senteJsonList: Array<MochikomaJson>,
        val goteJsonList: Array<MochikomaJson>,
        val rightAction: Array<RightAction>
)

data class BoardKomaJson(
        val index: Int,
        val name: String,
        val player: String
)

data class MochikomaJson(
        val name: String,
        val amount: Int
)

data class RightAction(
        val done: Action,
        val next: Action,
        val rightAction: Array<RightAction>
)

data class Action(
        val before: Int?,
        val after: Int,
        val change: Boolean,
        val put: Boolean,
        val name: String?
)
