package com.uty.shogiapp.activities;


public class RankingBean {
	private String rankingNum = "順位";
	private String rankerName = "ランカー名";
	private String rankerTitleName = "ランカーの称号名";
	private String rankerClassPosition = "ランカーの級";
	private String rankerData = "ランカーのランキングデータ";

	//コンストラクタ
	public RankingBean() {
	}

	public RankingBean(String rankingNum, String rankerName, String rankerTitleName,String rankerClassPosition, String rankerData) {
		this.rankingNum = rankingNum;
		this.rankerName = rankerName;
		this.rankerTitleName = rankerTitleName;
		this.rankerClassPosition = rankerClassPosition;
		this.rankerData = rankerData;
	}

	//セッター・ゲッター
	public String getRankingNum() {
		return rankingNum;
	}

	public String getRankerName() {
		return rankerName;
	}

	public String getRankerTitleName() {
		return rankerTitleName;
	}

	public String getRankerClassPosition() {
		return rankerClassPosition;
	}

	public String getRankerData() {
		return rankerData;
	}

	public void setRankingNum(String rankingNum) {
		this.rankingNum = rankingNum;
	}

	public void setRankerName(String rankerName) {
		this.rankerName = rankerName;
	}

	public void setRankerTitleName(String rankerTitleName) {
		this.rankerTitleName = rankerTitleName;
	}

	public void setRankerClassPosition(String rankerClassPosition) {
		this.rankerClassPosition = rankerClassPosition;
	}

	public void setRankerData(String rankerData) {
		this.rankerData = rankerData;
	}

}
