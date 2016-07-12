package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.MyPageOtherServlet;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPageOther extends Activity {

	private String myId;		//ユーザーID
	private String winNum,loseNum,drawNum;	//勝利数、敗北数、引き分け数

	//コンポーネント
	private TextView userName;			//ユーザー名
	private TextView userTitle;			//称号名
	private ImageView avatar;			//装着中のアバター

	private TextView classPosition;		//級位
	private TextView rate;				//レート
	private TextView battleNum;			//対局回数
	private TextView battleRecord;		//戦績
	private TextView winPercentage;		//勝率
	private TextView streakNum;			//連勝、連敗カウンター
	private TextView maxStreakNum;		//最高連続勝利数
	private TextView disconnectedNum;	//接続切れ回数

	//イベントハンドラを付けるコンポーネント
//	private Button friendButton;		//フレンドページ
//	private Button rankingButton;		//ランキングページ
//	private Button battleHistoryButton;	//対局履歴ページ
//	private Button teamButton;			//チームページ
	private Button friendRequestButton;	//友達申請ボタン
	private Button backTopButton;		//トップページに戻るボタン

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//プリファレンス
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		myId = pref.getString("id","guest");

		//xmlの読み込み
		setContentView(R.layout.mypage_other);

		//コンポーネントの取得
		//ユーザー情報
		//画面左側
		userName = 		(TextView)findViewById(R.id.nameText);			//ユーザー名
		avatar = 		(ImageView)findViewById(R.id.avatarImageView);	//装着中のアバター
		userTitle = 	(TextView)findViewById(R.id.titleText);			//称号名

		//画面右側
		classPosition = (TextView)findViewById(R.id.classPositionRow2);	//級位
		rate = 			(TextView)findViewById(R.id.rateRow2);			//レート
		battleNum = 	(TextView)findViewById(R.id.battleNumRow2);		//勝率
		battleRecord = 	(TextView)findViewById(R.id.battleRecordRow2);	//戦績
		streakNum =		(TextView)findViewById(R.id.streakRow2);		//ストリーク数
		maxStreakNum = 	(TextView)findViewById(R.id.maxStreakRow2);		//最高連続勝利数
		disconnectedNum = (TextView)findViewById(R.id.disconnectedRow2);//接続切れ回数

		//インテントボタン
//		friendButton = (Button)findViewById(R.id.friendButton);
//		rankingButton = (Button)findViewById(R.id.rankingButton);
//		battleHistoryButton = (Button)findViewById(R.id.battleHistoryButton);
		friendRequestButton = (Button)findViewById(R.id.friendRequestButton);
		backTopButton = (Button)findViewById(R.id.backTopButton);

		//リスナー
		//画面遷移用イベントハンドラ
//		friendButton.setOnClickListener(new OnClickListener(){
//			public void onClick(View v){
//				Intent intent = new Intent(MyPage.this,frend.class);
//				startActivity(intent);
//			}
//		});

//		rankingButton.setOnClickListener(new OnClickListener(){
//			public void onClick(View v){
//				Intent intent = new Intent(MyPage.this,ranking.class);
//				startActivity(intent);
//			}
//		});

//		battleHistoryButton.setOnClickListener(new OnClickListener(){
//			public void onClick(View v){
//				Intent intent = new Intent(MyPageOther.this,BattleHistory.class);
//				startActivity(intent);
//			}
//		});

		friendRequestButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//フレンドを申請する
				new MyPageOtherServlet(myId, userName.getText().toString(),true).execute(MyPageOther.this);
			}
		});

		backTopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//トップページに戻る
				Intent intent = new Intent(MyPageOther.this,MainActivity.class);
				startActivity(intent);
			}
		});

	}	//onCreate終了---------

	@Override
	public void onResume(){
		super.onResume();

		Intent i = getIntent();
		String userName = i.getStringExtra("userName");
		System.out.println("他人ページ:onResume():対局履歴ページから"+userName+"様のページへリクエスト");
		System.out.println("他人ページ:サーブレットとの通信を開始します_____________________");
		new MyPageOtherServlet(myId,userName).execute(this);
	}

	public String getMyId() {
		return myId;
	}

	public String getWinNum() {
		return winNum;
	}

	public String getLoseNum() {
		return loseNum;
	}

	public String getDrawNum() {
		return drawNum;
	}

	public TextView getUserName() {
		return userName;
	}

	public TextView getUserTitle() {
		return userTitle;
	}

	public TextView getClassPosition() {
		return classPosition;
	}

	public TextView getRate() {
		return rate;
	}

	public TextView getBattleNum() {
		return battleNum;
	}

	public TextView getBattleRecord() {
		return battleRecord;
	}

	public TextView getWinPercentage() {
		return winPercentage;
	}

	public ImageView getAvatar() {
		return avatar;
	}

	public TextView getStreakNum() {
		return streakNum;
	}

	public TextView getMaxStreakNum() {
		return maxStreakNum;
	}

	public TextView getDisconnectedNum() {
		return disconnectedNum;
	}

//	public Button getFriendButton() {
//		return friendButton;
//	}return titleButton;

//	public Button getRankingButton() {
//		return rankingButton;
//	}

//	public Button getBattleHistoryButton() {
//		return battleHistoryButton;
//	}

	public void setMyId(String myId) {
		this.myId = myId;
	}

	public void setWinNum(String winNum) {
		this.winNum = winNum;
	}

	public void setLoseNum(String loseNum) {
		this.loseNum = loseNum;
	}

	public void setDrawNum(String drawNum) {
		this.drawNum = drawNum;
	}

	public void setUserName(TextView userName) {
		this.userName = userName;
	}

	public void setClassPosition(TextView classPosition) {
		this.classPosition = classPosition;
	}

	public void setRate(TextView rate) {
		this.rate = rate;
	}

	public void setBattleNum(TextView battleNum) {
		this.battleNum = battleNum;
	}

	public void setBattleRecord(TextView battleRecord) {
		this.battleRecord = battleRecord;
	}

	public void setWinPercentage(TextView winPercentage) {
		this.winPercentage = winPercentage;
	}

	public void setAvatar(Bitmap bm) {
		this.avatar.setImageBitmap(bm);
	}

	public void setStreakNum(TextView streakNum) {
		this.streakNum = streakNum;
	}

	public void setMaxStreakNum(TextView maxStreakNum) {
		this.maxStreakNum = maxStreakNum;
	}

	public void setDisconnectedNum(TextView disconnectedNum) {
		this.disconnectedNum = disconnectedNum;
	}

//	public void setFriendButton(Button friendButton) {
//		this.friendButton = friendButton;
//	}

//	public void setRankingButton(Button rankingButton) {
//		this.rankingButton = rankingButton;
//	}

//	public void setBattleHistoryButton(Button battleHistoryButton) {
//		this.battleHistoryButton = battleHistoryButton;
//	}

	public Button getFriendRequestButton() {
		return friendRequestButton;
	}

	public String makeBattleNum(String win, String lose, String draw) {
		int battleNum;	//対戦回数
		battleNum = Integer.parseInt(win) + Integer.parseInt(lose) + Integer.parseInt(draw);

		return String.valueOf(battleNum);
	}

}


