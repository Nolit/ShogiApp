package com.uty.shogi.activities

import com.uty.shogi.R
import com.uty.shogi.servletClients.LoginServlet
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Login : Activity() {

    //ログイン失敗時の文字列を表示する
    var resultText: TextView
        internal set

    internal var formName: EditText
    internal var formPassword: EditText

    //新規登録ページ
    internal var intentRegistrationButton: Button

    internal var canselButton: Button

    internal var loginButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        resultText = findViewById(R.id.resultText) as TextView
        formName = findViewById(R.id.formName) as EditText
        formPassword = findViewById(R.id.formPassword) as EditText

        intentRegistrationButton = findViewById(R.id.intentRegistrationButton) as Button
        canselButton = findViewById(R.id.canselButton) as Button
        loginButton = findViewById(R.id.registrationButton) as Button

        //リスナー
        //新規登録ボタン
        intentRegistrationButton.setOnClickListener {
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        //キャンセルボタン
        canselButton.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        //ログインボタン
        loginButton.setOnClickListener {
            println(formName.text)
            println(formPassword.text)
            LoginServlet(formName.text.toString(), formPassword.text.toString()).execute(this@Login)
        }

    }

    public override fun onPause() {
        super.onPause()
        println("Login:onPause()...:ログインページを終了します。")
        finish()
    }

    public override fun onStop() {
        super.onStop()
        println("Login:onStop()...")
    }

}

