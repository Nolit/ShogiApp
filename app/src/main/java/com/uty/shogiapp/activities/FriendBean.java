package com.uty.shogiapp.activities;


public class FriendBean {

	private String friendName = "フレンド名";
	private String titleName = "称号名";
	private String classPosition = "級";
	private String rate = "レート";
	private String userNum = "フレンドユーザー番号";

	//コンストラクタ
	public FriendBean() {
	}

	public FriendBean(String friendName, String titleName,String classPosition, String rate, String userNum) {
		this.friendName = friendName;
		this.titleName = titleName;
		this.classPosition = classPosition;
		this.rate = rate;
		this.userNum = userNum;
	}

	//セッター・ゲッター
	public String getFriendName() {
		return friendName;
	}

	public String getTitleName() {
		return titleName;
	}

	public String getClassPosition() {
		return classPosition;
	}

	public String getRate() {
		return rate;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public void setClassPosition(String classPosition) {
		this.classPosition = classPosition;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

}
