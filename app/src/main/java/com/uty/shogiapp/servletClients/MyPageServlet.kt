package com.uty.shogiapp.servletClients

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

import com.uty.shogiapp.R
import com.uty.shogiapp.activities.MyPage
import com.uty.shogiapp.settings.ServerConfig
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter


//型パラメータは左から
//doInBackgroundの引数型,他のメソッドで使うらしい型,onPostExecuteの引数型
class MyPageServlet : AsyncTask<MyPage, Void, List<String>> {
    private lateinit var myPage: MyPage;
    private var guestFlag: Boolean? = null;        //ゲスト対局の許可・拒否設定

    constructor() {
    }

    constructor(guestFlag: Boolean) {
        this.guestFlag = guestFlag
    }

    //execute()でこのメソッドが呼び出される
    override fun doInBackground(vararg myPage: MyPage): List<String> {
        this.myPage = myPage[0]
        val userInfo = ArrayList<String>()
        println("===== HTTP POST Start =====")
        try {

            val url = URL(ServerConfig.URL + "MyPage")
            var connection: HttpURLConnection? = null

            try {
                connection = url.openConnection() as HttpURLConnection
                connection.doOutput = true
                connection.requestMethod = "POST"

                //parameterの生成
                var parameter = "id=" + this.myPage.userId
                guestFlag?.let {  parameter += "&guestFlag=" + guestFlag }

                val printWriter = PrintWriter(connection.outputStream)
                printWriter.print(parameter)
                printWriter.close()

                //getResponseCode()実行時にリクエストを行う戻り値はHTTPステータスコード
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val isr = InputStreamReader(connection.inputStream, "UTF-8")
                    val reader = BufferedReader(isr)
                    for(line in reader.readLines()){
                        userInfo.add(line)
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return userInfo
    }

    override fun onPostExecute(userInfo: List<String>) {
        //最後の称号群が6番目以降から取得のためiterator作成
        val it = userInfo.iterator()

        //デバッグ用レスポンス内容全表示
        for (str in userInfo)
            println("デバッグ:MyPageServlet:受け取った情報:" + str)

        //アクティビティのビューの書き換え
        myPage.userName.text = it.next()        //ユーザー名

        //アバター
        try {
            val clazz = Class.forName("com.uty.shogiapp.R\$drawable")
            val f = clazz.getField(it.next())
            myPage.avatar.setImageBitmap(BitmapFactory.decodeResource(myPage.resources, f.getInt(null)))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        myPage.classPosition.text = it.next()    //級位
        myPage.rate.text = it.next()            //レート

        //対局回数、戦績、勝率
        val win = it.next()
        val lose = it.next()
        val draw = it.next()
        val battleNum = makeBattleNum(win, lose, draw)

        myPage.battleNum.text = battleNum + "戦"
        myPage.battleRecord.text = win + "勝" + lose + "敗" + draw + "分"        //勝利数、敗北数、引き分け数

        myPage.streakNum.text = it.next()                //ストリーク数
        myPage.maxStreakNum.text = it.next() + "連勝"        //最高連続勝利数
        myPage.disconnectedNum.text = it.next() + "回"    //接続切れ回数
        myPage.haveMoney.text = "$" + it.next()            //所持金

        //アクティビティのビューの書き換え(称号スピナー)
        val adapter = ArrayAdapter<String>(myPage, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        //現在付けている称号からの取得
        while (it.hasNext()) {
            adapter.add(it.next())
        }
        //取得した称号名リストをスピナーに入れる
        myPage.titles.adapter = adapter

        //リファクタリング対象
        //称号が変更される時のイベントハンドラ
        myPage.titles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,position: Int, id: Long) {
                // 選択された称号名の取得
                val titleName = parent.selectedItem as String
                //選択した称号名からDBの現在付けている称号を変えるスレッド
                println("デバッグ:MypageServlet:" + titleName + "をexecuteします")
                HavingTitleUpdater(myPage.userId, titleName).execute()
            }
            override fun onNothingSelected(arg0: AdapterView<*>) {
            }
        }
    }

    fun makeBattleNum(win: String, lose: String, draw: String): String {
        val battleCount = win.toInt() + lose.toInt() + draw.toInt()
        return battleCount.toString()
    }
}
