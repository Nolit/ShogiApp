package com.uty.shogiapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.uty.shogiapp.R
import com.uty.shogiapp.functions.dispBoard
import com.uty.shogiapp.settings.ServerConfig
import com.uty.shogiapp.shogi.Board
import com.uty.shogiapp.shogi.BoardBuilder
import com.uty.shogiapp.websocketClients.BoardUpdater
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Problem : AppCompatActivity() {
    lateinit var board: Board
    //ここは無くなる予定
    var flag: Boolean? = false
    var beforeIndex: Int? = null

    val boardGrid: GridLayout by bindView(R.id.boardGrid)
    val myMochikomaGrid: GridLayout by bindView(R.id.myMochikomaGrid)
    val yourMochikomaGrid: GridLayout by bindView(R.id.yourMochikomaGrid)
    val myMochikomaSuGrid: GridLayout by bindView(R.id.myMochikomaSuGrid)
    val yourMochikomaSuGrid: GridLayout by bindView(R.id.yourMochikomaSuGrid)
    val resignButton: Button by bindView(R.id.resign)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problem)

        val caption = intent.getStringExtra("caption")
        fetchProblemByCaption(caption)

        resignButton.setOnClickListener {
            val intent = Intent(this, ProblemList::class.java)
            startActivity(intent)
        }

        //ここはAsyncTaskに変える
        while(true){
            if(flag != false){ break; }
        }
        gameUpdate()
    }

    //取得したJsonからboardを生成しフィールドに代入
    private fun fetchProblemByCaption(caption: String){
        thread {
            val url = URL(ServerConfig.URL + "ProblemPage")
            var connection: HttpURLConnection? = null
            connection = url.openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.requestMethod = "POST"

            val printWriter = PrintWriter(connection.outputStream)
            printWriter.print("caption=" + caption)
            printWriter.close()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
                val jsonString = reader.readLines().fold("") { s1, s2 -> "$s1$s2" }
                val builder = BoardBuilder()
                builder.json(jsonString)
                board = builder.boardInstance
                dispBoard(board)
                flag = true
            }
        }
    }

    private fun gameUpdate(){
        println("gameUpdate")
        val clazz = Class.forName("com.uty.shogiapp.R\$drawable")
        //ここから盤面GridLayoutの更新
        for (i in 0..8) {
            for (j in 0..8) {
                val ib = boardGrid.getChildAt(i * 9 + j) as ImageButton

                if (board.board[j][i] != null) {
                    //R.drawableの駒の画像に対応するフィールドにリフレクト
                    val f = clazz.getField(board.board[j][i].getImageId())
                    if (board.board[j][i].getOwner().getName() == "先手") {
                        //自駒の画像はそのまま表示
                        ib.setImageResource(f.getInt(null))
                    } else {
                        //相手駒の画像は左右反転して表示
                        val matrix = Matrix()
                        matrix.preScale(-1f, -1f)
                        var bm = BitmapFactory.decodeResource(resources, f.getInt(null))
                        bm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, false)
                        ib.setImageBitmap(bm)
                    }
                } else {
                    ib.setImageResource(0)
                    ib.setImageBitmap(null)
                }
            }
        }
        //ここまで盤面GridLayoutの更新

        //ここから持ち駒GridLayoutの更新
        //自分の持ち駒のビュー配置
        for (i in board.sente.mochiKoma.indices) {
            if (board.sente.mochiKoma[i] == null) {
                (myMochikomaGrid.getChildAt(i) as ImageButton).setImageResource(0)
                (myMochikomaSuGrid.getChildAt(i) as TextView).text = "  "
                break
            }
            val f = clazz.getField(board.sente.mochiKoma[i].getImageId())
            (myMochikomaGrid.getChildAt(i) as ImageButton).setImageResource(f.getInt(null))
            (myMochikomaSuGrid.getChildAt(i) as TextView).setText(board.sente.mochiKomaNum[i].toString())
        }
        //相手の持ち駒のビュー配置
        for (i in board.gote.mochiKoma.indices) {
            if (board.gote.mochiKoma[i] == null) {
                (yourMochikomaGrid.getChildAt(i) as ImageButton).setImageBitmap(null)
                (yourMochikomaSuGrid.getChildAt(i) as TextView).text = "  "
                break
            }
            val f = clazz.getField(board.gote.mochiKoma[i].getImageId())
            val matrix = Matrix()
            matrix.preScale(-1f, -1f)
            var bm = BitmapFactory.decodeResource(resources, f.getInt(null))
            bm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, false)
            (yourMochikomaGrid.getChildAt(i) as ImageButton).setImageBitmap(bm)
            (yourMochikomaSuGrid.getChildAt(i) as TextView).setText(board.gote.mochiKomaNum[i].toString())
        }
        //ここまで持ち駒GridLayoutの更新
        updateEventHandler()
    }

    //盤面の駒を持った時のイベントハンドラ生成
    private fun movableOnClickListenerCreate(zahyo: Int): View.OnClickListener {
        val index = zahyo
        return View.OnClickListener {
            println("move" + index)
            allButtonNotEnabled()
            beforeIndex = index
            println(index)
            val movable = board.moveCheck(index)
            for (i in movable.indices) {
                val x = movable[i] / 10 - 1
                val y = movable[i] % 10 - 1
                val ib = boardGrid.getChildAt(y * 9 + x) as ImageButton

                ib.isEnabled = true
                ib.setOnClickListener(moveOnClickListenerCreate(movable[i]))
                changeWhiteColorImageButton(ib)
            }
            val x = index / 10 - 1
            val y = index % 10 - 1
            val ib: ImageButton
            ib = boardGrid.getChildAt(y * 9 + x) as ImageButton

            ib.isEnabled = true
            ib.setOnClickListener(moveOnClickListenerCreate(index))
            changeWhiteColorImageButton(ib)
        }
    }

    //盤面から盤面へ駒を動かす時のイベントハンドラ生成
    private fun moveOnClickListenerCreate(zahyo: Int): View.OnClickListener {
        val afterIndex = zahyo

        return View.OnClickListener {
            var nariDialogFlag = false
            if (afterIndex == beforeIndex) {
                println("駒を戻します")
                updateEventHandler()
            } else {
                println("${beforeIndex}から${afterIndex}へmove")

                board.sente.move(beforeIndex!!, afterIndex)

                val afterRow = afterIndex / 10 - 1
                val afterCol = afterIndex % 10 - 1
                //ここから成れるかの確認
                val koma = board.board[afterRow][afterCol]
                if (koma.getName() == "歩" || koma.getName() == "香車" || koma.getName() == "桂馬" ||
                    koma.getName() == "銀" || koma.getName() == "飛車" || koma.getName() == "角") {
                    if (afterRow < 3 || afterRow < 3) {
                        if (!((koma.getName() == "歩" || koma.getName() == "香車") && afterRow == 0)) {
                            if (!(koma.getName() == "桂馬" && afterRow < 2)) {
                                //ダイアログで確認
                                nariDialogFlag = true
                                AlertDialog.Builder(this).setTitle("成りますか？").setPositiveButton("成る") { dialog, which ->
                                    board.setNariFlag(true);
                                    board.board[afterRow][afterCol].change()
                                    gameUpdate()
                                }.setNegativeButton("成らない") { dialog, which ->
//                                    board.setNariFlag(false);
//                                    gameUpdate()
                                }.show()
                            } else {
                                board.setNariFlag(true);
                                board.board[afterRow][afterCol].change()
                                gameUpdate()
                            }
                        } else {
                            board.setNariFlag(true);
                            board.board[afterRow][afterCol].change()
                            gameUpdate()
                        }
                    } else {
//                        board.setNariFlag(false);
//                        gameUpdate()
                    }
                    }
                //ここまで成れるかの確認
                allButtonNotEnabled()
            }
            changeDefaultColorBoardImageButton()
            gameUpdate()
        }
    }

    //手持ちの駒を持った時のイベントハンドラ生成
    private fun puttableOnClickListenerCreate(zahyo: Int): View.OnClickListener {
        val index = zahyo
        return View.OnClickListener {
            println("put" + index)
            allButtonNotEnabled()
            beforeIndex = index
            val searchKoma = board.sente.mochiKoma[index]
            val puttable = board.putCheck(searchKoma)
            for (i in puttable.indices) {
                val x = puttable[i] / 10 - 1
                val y = puttable[i] % 10 - 1
                val ib = boardGrid.getChildAt(y * 9 + x) as ImageButton

                changeWhiteColorImageButton(ib)
                ib.isEnabled = true
                ib.setOnClickListener(putOnClickListenerCreate(puttable[i]))
            }
            (myMochikomaGrid.getChildAt(index) as ImageButton).isEnabled = true
            (myMochikomaGrid.getChildAt(index) as ImageButton).setOnClickListener(putOnClickListenerCreate(index))
            changeWhiteColorImageButton((myMochikomaGrid.getChildAt(index) as ImageButton))
        }
    }

    //手持ちから盤面へ駒を置くときのイベントハンドラ生成
    private fun putOnClickListenerCreate(zahyo: Int): View.OnClickListener {
        val afterIndex = zahyo
        return View.OnClickListener {
            if (afterIndex == beforeIndex) {
                println("駒を戻します")
                updateEventHandler()
            } else {
                println("${beforeIndex}から${afterIndex}へput")
                board.sente.put(beforeIndex!!, afterIndex)
                //					BoardUpdater.this.bc.activity.setOrder(BoardUpdater.this.yourPlayer.getName());
                allButtonNotEnabled()
            }
            changeDefaultColorBoardImageButton()
            beforeIndex = 0
            gameUpdate()
        }
    }

    //イベントハンドラ更新
    private fun updateEventHandler() {
        println("updateEventHandler...")
        allButtonNotEnabled()
        //ここからイベントハンドラを付ける処理
        //盤面のイベントハンドラ
        for (i in 0..8) {
            for (j in 0..8) {
                val ib = boardGrid.getChildAt(i * 9 + j) as ImageButton

                ib.isEnabled = false
                if (board.board[j][i] != null && board.board[j][i].getOwner().getName() == "先手") {
                    println("enabled")
                    ib.isEnabled = true
                    ib.setOnClickListener(movableOnClickListenerCreate((j + 1) * 10 + (i + 1)))
                }
            }
        }
        //持ち駒のイベントハンドラ
        for (i in 0..7) {
            if (board.getSente().mochiKoma[i] == null) {
                break
            }
            (myMochikomaGrid.getChildAt(i) as ImageButton).isEnabled = true
            (myMochikomaGrid.getChildAt(i) as ImageButton).setOnClickListener(puttableOnClickListenerCreate(i))
        }
    }

    private fun changeWhiteColorImageButton(ib: ImageButton) {
        ib.setBackgroundResource(R.layout.white_color)
    }

    private fun changeDefaultColorBoardImageButton() {
        println("changeDefaultColorImageButton...")
        for (i in 0..8) {
            for (j in 0..8) {
                (boardGrid.getChildAt(i * 9 + j) as ImageButton).setBackgroundResource(R.drawable.border)
            }
        }

        for (i in 0..7) {
            (myMochikomaGrid.getChildAt(i) as ImageButton).setBackgroundResource(R.drawable.border)
        }
    }

    //全ボタンの無効化
    private fun allButtonNotEnabled() {
        for (i in 0..8) {
            for (j in 0..8) {
                (boardGrid.getChildAt(i * 9 + j) as ImageButton).isEnabled = false
            }
        }
        for (i in 0..7) {
            (myMochikomaGrid.getChildAt(i) as ImageButton).isEnabled = false
        }
    }
}
