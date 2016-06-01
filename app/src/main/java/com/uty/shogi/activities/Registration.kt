package com.uty.shogi.activities

import com.uty.shogi.R
import com.uty.shogi.servletClients.RegistrationServlet
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Registration : Activity() {

    var resultText: TextView
        internal set    //ログイン失敗時の文字列を表示する

    internal var formName: EditText
    internal var formMail: EditText
    internal var formPassword: EditText
    internal var formConfirmPassword: EditText

    internal var intentLoginButton: Button
    internal var canselButton: Button
    internal var registrationButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //xmlの読み込み
        setContentView(R.layout.registration)

        //コンポーネントの取得
        resultText = findViewById(R.id.resultText) as TextView

        formName = findViewById(R.id.formName) as EditText
        formMail = findViewById(R.id.formMail) as EditText
        formPassword = findViewById(R.id.formPassword) as EditText
        formConfirmPassword = findViewById(R.id.formConfirmPassword) as EditText

        intentLoginButton = findViewById(R.id.intentLoginButton) as Button
        canselButton = findViewById(R.id.canselButton) as Button
        registrationButton = findViewById(R.id.registrationButton) as Button

        //リスナー
        //ログインボタン
        intentLoginButton.setOnClickListener {
            val intent = Intent(this@Registration, Login::class.java)
            startActivity(intent)
        }

        //キャンセルボタン
        canselButton.setOnClickListener {
            val intent = Intent(this@Registration, MainActivity::class.java)
            startActivity(intent)
        }

        //新規登録ボタン
        registrationButton.setOnClickListener {
            println(formName.text)
            println(formMail.text)
            println(formPassword.text)
            println(formConfirmPassword.text)

            RegistrationServlet(formName.text.toString(),
                    formMail.text.toString(),
                    formPassword.text.toString(),
                    formConfirmPassword.text.toString()).execute(this@Registration)
        }

    }

    public override fun onPause() {
        super.onPause()
        println("Registration:onPause()...:会員登録ページを終了します。")
        finish()
    }

    public override fun onStop() {
        super.onStop()
        println("Registration:onStop()...")
    }

}

