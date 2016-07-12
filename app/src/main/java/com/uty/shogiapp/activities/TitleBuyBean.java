package com.uty.shogiapp.activities;


public class TitleBuyBean {

	private String titleName = "称号名";			//称号名
	private String titleQualifaction = "取得条件";	//取得条件
	private String titlePrice = "値段";				//値段
	private String buttonState = "所持";			//ボタンの状態
	private boolean buttonEnable = true;			//ボタンのEnable

	public TitleBuyBean() {
	}

	public TitleBuyBean(String titleName, String titleQualifaction, String titlePrice, String buttonState, boolean enable) {
		this.titleName = titleName;
		this.titleQualifaction = titleQualifaction;
		this.titlePrice = titlePrice;
		this.buttonState = buttonState;
		this.buttonEnable = enable;
	}

	public boolean isButtonEnable() {
		return buttonEnable;
	}

	public void setButtonEnable(boolean buttonEnable) {
		this.buttonEnable = buttonEnable;
	}

	public String getButtonState() {
		return buttonState;
	}

	public void setButtonState(String buttonState) {
		this.buttonState = buttonState;
	}



	public String getTitleName() {
		return titleName;
	}

	public String getTitleQualifaction() {
		return titleQualifaction;
	}

	public String getTitlePrice() {
		return titlePrice;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public void setTitleQualifaction(String titleQualifaction) {
		this.titleQualifaction = titleQualifaction;
	}

	public void setTitlePrice(String titlePrice) {
		this.titlePrice = titlePrice;
	}

}
