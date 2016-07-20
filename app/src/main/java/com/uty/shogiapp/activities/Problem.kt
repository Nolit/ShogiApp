package com.uty.shogiapp.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import com.uty.shogiapp.R
import com.uty.shogiapp.functions.dispBoard
import com.uty.shogiapp.settings.ServerConfig
import com.uty.shogiapp.shogi.Board
import com.uty.shogiapp.shogi.BoardBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Problem : AppCompatActivity() {
    lateinit var board: Board
    var flag: Boolean? = false;
    val boardGrid: GridLayout by bindView(R.id.boardGrid)
    val myMochikomaGrid: GridLayout by bindView(R.id.myMochikomaGrid)
    val yourMochikomaGrid: GridLayout by bindView(R.id.yourMochikomaGrid)
    val myMochikomaSuGrid: GridLayout by bindView(R.id.myMochikomaSuGrid)
    val yourMochikomaSuGrid: GridLayout by bindView(R.id.yourMochikomaSuGrid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problem)

        val caption = intent.getStringExtra("caption")
        fetchProblemByCaption(caption)

        while(true){
            if(flag != false){ break; }
        }
        gameUpdate()
    }

    //取得したJsonからboardを生成しフィールドに代入
    private fun fetchProblemByCaption(caption: String){
        thread{
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
        println(board.sente.mochiKoma.size)
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
    }
}
