package com.uty.shogiapp.activities

import com.uty.shogiapp.R
import com.uty.shogiapp.servletClients.RegistrationServlet
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.bindView

class Registration : Activity() {

    //会員登録失敗時のメッセージ
    val resultText: TextView by bindView(R.id.resultText)

    internal val formName: EditText by bindView(R.id.formName)
    internal val formMail: EditText by bindView(R.id.formMail)
    internal val formPassword: EditText by bindView(R.id.formPassword)
    internal val formConfirmPassword: EditText by bindView(R.id.formConfirmPassword)

    internal val intentLoginButton: Button by bindView(R.id.intentLoginButton)
    internal val cancelButton: Button by bindView(R.id.canselButton)
    internal val registrationButton: Button by bindView(R.id.registrationButton)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.registration)

        intentLoginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        registrationButton.setOnClickListener {
            RegistrationServlet(formName.text.toString(),
                                formMail.text.toString(),
                                formPassword.text.toString(),
                                formConfirmPassword.text.toString()
                                ).execute(this)
        }

    }
}

