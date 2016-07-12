package com.uty.shogiapp.activities;


public class BattleHistoryBean {

	private String userId = "guest";
	private String opponentName = "対局相手";
	private String Rate = "相手のレート";
	private String battleResult = "勝敗";
	private String battleDate = "日時";
	private String battleNum = "棋譜番号";
    private String senteName = "先手の名前";
    private String goteName = "後手の名前";
    private String senteClass = "先手の級";
    private String goteClass = "後手の級";
    private String senteAvatarName = "先手のアバター名";
    private String goteAvatarName = "後手のアバター名";

	//コンストラクタ
	public BattleHistoryBean() {
	}

	public BattleHistoryBean(String userId, String battleDate, String opponentName,String battleResult, String rate, String battleNum,
			String senteName,String goteName,String senteClass,String goteClass,String senteAvatarName,String goteAvatarName) {
		//Bean
		this.userId = userId;
		this.battleDate = battleDate;
		this.opponentName = opponentName;
		this.battleResult = battleResult;
		this.Rate = rate;
		this.battleNum = battleNum;

		//Intent
		this.senteName = senteName;
		this.goteName = goteName;
		this.senteClass = senteClass;
		this.goteClass = goteClass;
		this.senteAvatarName = senteAvatarName;
		this.goteAvatarName = goteAvatarName;

	}

	//セッター・ゲッター
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public void setRate(String rate) {
		Rate = rate;
	}

	public void setBattleResult(String battleResult) {
		this.battleResult = battleResult;
	}

	public void setBattleDate(String battleDate) {
		this.battleDate = battleDate;
	}

	public void setBattleNum(String battleNum) {
		this.battleNum = battleNum;
	}

	public String getUserId() {
		return userId;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public String getRate() {
		return Rate;
	}

	public String getBattleResult() {
		return battleResult;
	}

	public String getBattleDate() {
		return battleDate;
	}

	public String getBattleNum() {
		return battleNum;
	}

	public String getSenteName() {
		return senteName;
	}

	public String getGoteName() {
		return goteName;
	}

	public String getSenteClass() {
		return senteClass;
	}

	public String getGoteClass() {
		return goteClass;
	}

	public String getSenteAvatarName() {
		return senteAvatarName;
	}

	public String getGoteAvatarName() {
		return goteAvatarName;
	}

	public void setSenteName(String senteName) {
		this.senteName = senteName;
	}

	public void setGoteName(String goteName) {
		this.goteName = goteName;
	}

	public void setSenteClass(String senteClass) {
		this.senteClass = senteClass;
	}

	public void setGoteClass(String goteClass) {
		this.goteClass = goteClass;
	}

	public void setSenteAvatarName(String senteAvatarName) {
		this.senteAvatarName = senteAvatarName;
	}

	public void setGoteAvatarName(String goteAvatarName) {
		this.goteAvatarName = goteAvatarName;
	}

}
