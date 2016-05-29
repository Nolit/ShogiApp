package com.uty.shogi.activities


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button

import com.uty.shogi.R
import com.uty.shogi.servletClients.MainActivityServlet

class MainActivity : Activity() {

    private val id: String = getSharedPreferences("tt4", Context.MODE_PRIVATE).getString("id", "guest")
    var connectionLossNum: String? = null
    private val battleStartButton: Button = findViewById(R.id.battleStart) as Button
    private val myPageButton: Button = findViewById(R.id.intentMyPage) as Button

	public override fun onCreate(savedInstanceState: Bundle?) {
        println("MainActivity始め！")
        super.onCreate(savedInstanceState)

        //デバッグ用処理:プリファレンスに異常な値が入った場合はここのコメントアウトを外す
        /* データベースに存在しないIDがプリファレンスに入った場合はエラーが起きる */
        //		Editor editor = pref.edit();
        //		editor.remove("id");
        //		editor.commit();
        //		System.out.println("重要！:MainActivity:デバッグ用処理:プリファレンスのIDを強制的に削除しました。IDを削除したくない場合はコメントアウトをしてください。");

        //xmlの読み込み
        setContentView(R.layout.activity_main)

        //ログイン状態とゲスト状態の分岐処理
        if (id == "guest") {
            /* ---------------------------------ゲストの処理--------------------------------- */
            println("プリファレンスにIDが保存されていません。id[$id]で実行します。")

            //ビューの更新
            battleStartButton.text = "ゲストで対局"
            myPageButton.text = "ログイン"

            //リスナー
            //ゲストモードで対局開始
            battleStartButton.setOnClickListener {
                val intent = Intent(this@MainActivity, Matching::class.java)
                intent.putExtra("id", "guest")
                startActivity(intent)
            }

            //会員登録ページへ遷移
            myPageButton.setOnClickListener {
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
            }


        } else {
            /* ---------------------------------会員の処理--------------------------------- */
            println("プリファレンスにIDが保存されていました。id[$id]で実行します。")

            //接続切れ回数の更新
//            			new MainActivityServlet(id).execute(this);

            //ビューの更新
            battleStartButton.text = "対局開始"
            myPageButton.text = "マイページ"

            //リスナー
            //対局開始
            battleStartButton.setOnClickListener {
                println("本日の接続切れ回数は" + connectionLossNum + "回です。")
                if (Integer.parseInt(connectionLossNum) <= 10) {
                    val intent = Intent(this@MainActivity, Matching::class.java)
                    startActivity(intent)
                } else {
                    /* ここでアラート */
                    AlertDialog.Builder(this@MainActivity)
                               .setTitle("ペナルティ")
                               .setMessage("1日の接続切れ回数の限度を超えました。" + "\n" + "本日は対局出来ません。")
                               .setPositiveButton("OK", null)
                               .show()
                }
            }

            //マイページへ遷移
            myPageButton.setOnClickListener {
                val intent = Intent(this@MainActivity, MyPage::class.java)
                startActivity(intent)
            }

        }    //-----------終了:ログイン状態とゲスト状態の分岐処理

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

    public override fun onStart() {
        super.onStart()
        println("MainActivity:onStart()...")
    }

    public override fun onResume() {
        super.onResume()
        println("MainActivity:onResume()...")
    }

    public override fun onStop() {
        super.onStop()
        println("MainActivity:onStop()...")
    }

}


