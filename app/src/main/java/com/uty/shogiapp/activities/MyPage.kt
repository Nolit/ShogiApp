package com.uty.shogiapp.activities

import com.uty.shogiapp.R
import com.uty.shogiapp.servletClients.MyPageServlet
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.bindView

class MyPage : Activity() {

    lateinit var userId: String

    //画面左側
    val userName:  TextView  by bindView(R.id.nameText)          //ユーザ名
    val titles:    Spinner   by bindView(R.id.titleSpinner)      //称号スピナー
    val avatar:    ImageView by bindView(R.id.avatarImageView)  //装着中のアバター
    val haveMoney: TextView  by bindView(R.id.haveMoneyRow2)    //所持金

    //画面右側
    val classPosition:   TextView by bindView(R.id.classPositionRow2)    //級位
    val rate:             TextView by bindView(R.id.rateRow2)            //レート
    val battleNum:       TextView by bindView(R.id.battleNumRow2)      //対局回数
    val battleRecord:    TextView by bindView(R.id.battleRecordRow2)  //戦績
    val streakNum:       TextView by bindView(R.id.streakRow2)        //連勝、連敗カウンター
    val maxStreakNum:    TextView by bindView(R.id.maxStreakRow2)     //最高連続勝利数
    val disconnectedNum: TextView by bindView(R.id.disconnectedRow2) //接続切れ回数

    private val friendButton:        Button by bindView(R.id.friendButton);		  //フレンドページ
    private val avatarButton:        Button by bindView(R.id.avatarButton);		  //アバターページ
    private val titleButton:         Button by bindView(R.id.titleButton);		  //称号購入ページ
    private val rankingButton:       Button by bindView(R.id.rankingButton);		  //ランキングページ
    private val battleHistoryButton: Button by bindView(R.id.battleHistoryButton);//対局履歴ページ
    private val logoutButton:        Button by bindView(R.id.logoutButton);		  //ログアウトしてTopに戻る
    private val guestFlagSwitch:     Switch by bindView(R.id.guestFlagSwitch);	   //ゲスト対局の許可・拒否設定

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage)
        userId = getSharedPreferences("tt4", Context.MODE_PRIVATE).getString("id", null)

        val transitionListener : (View) -> Unit = {
            v ->  val nextIntent = when (v.id) {
                        R.id.friendButton -> Intent(this, Friend::class.java)
                        R.id.avatarButton -> Intent(this, Avatar::class.java)
                        R.id.titleButton -> Intent(this, TitleBuy::class.java)
                        R.id.rankingButton -> Intent(this, Ranking::class.java)
                        R.id.battleHistoryButton -> Intent(this, BattleHistory::class.java)
                        else -> Intent(this, MainActivity::class.java)
                    }
                    startActivity(nextIntent)
        }
        friendButton.setOnClickListener(transitionListener)
        avatarButton.setOnClickListener(transitionListener)
        titleButton.setOnClickListener(transitionListener)
        rankingButton.setOnClickListener(transitionListener)
        battleHistoryButton.setOnClickListener(transitionListener)
        logoutButton.setOnClickListener({
            v -> getSharedPreferences("tt4", Context.MODE_PRIVATE).edit().remove("id").commit()
                println("ログアウトしました。")
                startActivity(Intent(this, MainActivity::class.java))
        })
        guestFlagSwitch.setOnCheckedChangeListener { b, isChecked -> MyPageServlet(isChecked).execute(this) }
    }

    public override fun onResume() {
        super.onResume()
        MyPageServlet().execute(this)
    }
}

