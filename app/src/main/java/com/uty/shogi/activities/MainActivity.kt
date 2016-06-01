package com.uty.shogi.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import butterknife.bindView
import com.uty.shogi.R
import com.uty.shogi.settings.ServerConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

class MainActivity : Activity() {

    //この変数の値が一定を超えると対局が出来ない
    var connectionLossCount: Int = 0

    private val battleStartButton: Button by bindView(R.id.battleStart)

    //マイページ又はログインページ遷移するボタン
    //***適切な名前に変更したいけど思いつかない***
    private val myPageButton: Button by bindView(R.id.intentMyPage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //会員を一意に識別するID
        //プリファレンスに保存したIDを参照するのはここに限られ、以降の処理ではIntentを使用してIDを渡す
        val id: String = getSharedPreferences("tt4", Context.MODE_PRIVATE).getString("id", "guest")

        //myPageButtonを押したときにstartするIntent
        var intent: Intent

        if (id == "guest") {
            battleStartButton.text = "ゲストで対局"
            this.myPageButton.text = "ログイン"
            intent = Intent(this@MainActivity, Login::class.java)
        } else {
            /* ---------------------------------会員の処理--------------------------------- */
            updateConnectionLossCount(id);

            battleStartButton.text = "対局開始"
            myPageButton.text = "マイページ"
            intent = Intent(this@MainActivity, MyPage::class.java)
        }

        this.myPageButton.setOnClickListener {
            startActivity(intent)
        }

        battleStartButton.setOnClickListener {
            println("本日の接続切れ回数は" + connectionLossCount + "回です。")
            if (connectionLossCount <= 10) {
                val intent = Intent(this@MainActivity, Matching::class.java).putExtra("id", id)
                startActivity(intent)
            } else {
                AlertDialog.Builder(this@MainActivity)
                            .setTitle("ペナルティ")
                            .setMessage("1日の接続切れ回数の限度を超えました。" + "\n" + "本日は対局出来ません。")
                            .setPositiveButton("OK", null)
                            .show()
            }
        }
    }

    //ユーザが会員時に限る、ゲスト時に呼び出してはいけない
    private fun updateConnectionLossCount(userId: String){
        thread{
            val url = URL(ServerConfig.URL + "MainActivityPage")

            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.requestMethod = "POST"

            val printWriter = PrintWriter(connection.outputStream)
            printWriter.print("id=" + userId)
            printWriter.close()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val isr = InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)
                val reader = BufferedReader(isr)
                this.connectionLossCount = Integer.parseInt(reader.readLine())
            }
        }
    }

    //オーバーライドしないと下記の例外発生(処理は続けられる)
    //Performing stop of activity that is not resumed
    override fun onResume(){
        super.onResume()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_BACK -> // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true
            }
        }
        return super.dispatchKeyEvent(event)
    }
}

