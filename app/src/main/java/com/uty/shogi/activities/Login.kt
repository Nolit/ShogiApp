package com.uty.shogi.activities

import com.uty.shogi.R
import com.uty.shogi.servletClients.LoginServlet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.bindView

class Login : Activity() {

    //ログイン失敗時の文字列を表示する
    val resultText: TextView by bindView(R.id.resultText)

    val formName: EditText by bindView(R.id.formName)
    val formPassword: EditText by bindView(R.id.formPassword)

    //新規登録ページに遷移するボタン
    internal val intentRegistrationButton: Button by bindView(R.id.intentRegistrationButton)

    internal val cancelButton: Button by bindView(R.id.canselButton)

    internal val loginButton: Button by bindView(R.id.registrationButton)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        intentRegistrationButton.setOnClickListener {
            val intent = Intent(this@Login, Registration::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            LoginServlet(formName.text.toString(), formPassword.text.toString()).execute(this@Login)
        }

    }
}

