package com.uty.shogiapp.servletClients

import android.os.AsyncTask
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.uty.shogiapp.activities.ProblemList
import com.uty.shogiapp.settings.ServerConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ProblemListServlet : AsyncTask<ProblemList, Void, List<String>> {
    private lateinit var activity: ProblemList;

    constructor()

    override fun doInBackground(vararg arg: ProblemList): List<String> {
        this.activity = arg[0]
        val problemList = ArrayList<String>()

        val url = URL(ServerConfig.URL + "ProblemListPage")
        var connection: HttpURLConnection? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.requestMethod = "GET"
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val isr = InputStreamReader(connection.inputStream, "UTF-8")
                val reader = BufferedReader(isr)
                for (line in reader.readLines()) {
                    problemList.add(line)
                }
            }
        } finally {
            connection?.disconnect()
        }
        return problemList
    }

    override fun onPostExecute(userInfo: List<String>) {
        for (str in userInfo)
            println("デバッグ:ProblemListServlet:受け取った情報:" + str)

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1)
        for (str in userInfo) {
            adapter.add(str)
        }
        activity.problemListView.adapter = adapter

        activity.problemListView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val listView = parent as ListView
                var caption = listView.getItemAtPosition(position) as String?
                caption?.let { activity.selectProblem(caption) }
            }
        }
    }
}
