package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.AvatarServlet;
import com.uty.shogiapp.servletClients.HavingAvatarUpdater;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Avatar extends Activity{

	private String userId;
	private String pictPath;		//試着中の画像パスを代入する→変更ボタンでServletに送る変数
	private BitmapDrawable bd;				//imageViewのbitmapを取得するために使う
	private List<Boolean> noImageList = new ArrayList<Boolean>();			//変更ボタンのEnableをチェックするために使う

	//イベントハンドラを付けるコンポーネントL
	private Button changeButton,intentButton1;	//変更ボタン,アバターを買いに行くボタン
	private ImageView ivHold;	//装着中
	private ImageView ivTry;	//試着中
	private List<ImageView> imageView = new ArrayList<ImageView>();

	public void onCreate(Bundle savedInstanceState){
		System.out.println("AvatarStart!!");
		super.onCreate(savedInstanceState);

		//プリファレンス
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
		setContentView(R.layout.avatar);

		//イベントハンドラを付けるコンポーネントの取得
		changeButton = (Button)findViewById(R.id.button1);	//変更
		intentButton1 = (Button)findViewById(R.id.button2);	//アバターを買いに行く

		ivHold = (ImageView)findViewById(R.id.imageView1);
		ivTry = (ImageView)findViewById(R.id.imageView2);

		imageView.add((ImageView)findViewById(R.id.imageView3));
		imageView.add((ImageView)findViewById(R.id.ImageView4));
		imageView.add((ImageView)findViewById(R.id.ImageView5));
		imageView.add((ImageView)findViewById(R.id.ImageView6));
		imageView.add((ImageView)findViewById(R.id.ImageView7));
		imageView.add((ImageView)findViewById(R.id.ImageView8));
		imageView.add((ImageView)findViewById(R.id.ImageView9));
		imageView.add((ImageView)findViewById(R.id.ImageView10));

		changeButton.setEnabled(false);	//変更ボタンは、最初は押せない状態にしておく

		//アバター試着イベントハンドラ ivTryを更新する
		imageView.get(0).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				pictPath = "res_avatar1";
				bd = (BitmapDrawable)imageView.get(0).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(0));
			}
		});

		imageView.get(1).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(1).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar2";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(1));
			}
		});

		imageView.get(2).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(2).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar3";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(2));
			}
		});

		imageView.get(3).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(3).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar4";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(3));
			}
		});

		imageView.get(4).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(4).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar5";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(4));
			}
		});

		imageView.get(5).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(5).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar6";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(5));
			}
		});

		imageView.get(6).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(6).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar7";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(6));
			}
		});

		imageView.get(7).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				bd = (BitmapDrawable)imageView.get(7).getDrawable();
				ivTry.setImageBitmap(bd.getBitmap());
				pictPath = "res_avatar8";

				//変更ボタンの更新
				changeButtonEnableCheck(noImageList.get(7));
			}
		});



		//アバター変更イベントハンドラ
		changeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//装着中のアバターを変更
				new HavingAvatarUpdater(userId,pictPath).execute();
				System.out.println("デバッグ用:Avatar.java:変更ボタンリスナーをexecuteしました。userId="+userId);

				//ImageView ivHoldの更新
				ivHold.setImageBitmap(bd.getBitmap());
			}
		});

		//画面遷移用イベントハンドラ	アバター購入ページに移動する
		intentButton1.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(Avatar.this,AvatarBuy.class);
				startActivity(intent);
			}

		});

	}

	@Override
	public void onResume(){
		super.onResume();
		System.out.println("アバターページ:onResume():サーブレットとの通信を開始します_____________________");
		new AvatarServlet().execute(this);
	}

	//ボタンのEnableを判定し有効、無効化する処理
	private void changeButtonEnableCheck(boolean bol) {
		if(bol){
			//真ならボタンをoffに
			changeButton.setEnabled(false);
		}else{
			//偽ならボタンをonに
			changeButton.setEnabled(true);
		}
	}

	//noImageListを返す
	public List<Boolean> getNoImageList(){
		return noImageList;
	}

	//noImageListに要素を追加する
	public void addNoImageList(boolean bol){
		noImageList.add(bol);
	}

	//ユーザーIDを返す
	public String getUserId() {
	    return userId;
	}

	//装着中アバターのImageViewを返す
	public ImageView getIvHold() {
	    return ivHold;
	}

	//保持アバターのImageViewを返す
	public List<ImageView> getImageView() {
	    return imageView;
	}

	//変更ボタンを返す
	public Button getChangeButton(){
		return changeButton;
	}

}