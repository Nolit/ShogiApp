package com.uty.shogiapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import butterknife.bindView
import com.uty.shogiapp.R
import com.uty.shogiapp.servletClients.ProblemListServlet

class ProblemList : AppCompatActivity() {
    val problemListView: ListView by bindView(R.id.problemList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.problem_list)
        ProblemListServlet().execute(this)
    }

    fun selectProblem(caption: String){
        val intent = Intent(this, Problem::class.java).putExtra("caption", caption)
        startActivity(intent)
    }
}
