package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.AvatarBuyServlet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AvatarBuy extends Activity{

	//ページで使うデータ
	private String userId;			//ユーザーID
	private String pictPrice,haveMoney;		//画像の値段、所持金額

	//コンポーネント
	private TextView showHaveMoney;		//所持金の表示
	private List<TextView> showPictPriceList = new ArrayList<TextView>();	//アバター価格の表示

	//イベントハンドラを付けるコンポーネント
	private List<Button> purchaseButtonList = new ArrayList<Button>();		//購入ボタン
	private Button backTopButton;		//トップページに戻るボタン



	public void onCreate(Bundle savedInstanceState){
		System.out.println("AvatarBuyStart!!");
		super.onCreate(savedInstanceState);

		//購入確認用アラート
		final AlertDialog.Builder alert = new AlertDialog.Builder(AvatarBuy.this);
		alert.setTitle("購入確認");
		alert.setMessage("選択したアバターを購入しますか？");

		//プリファレンス
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
		setContentView(R.layout.avatar_buy);

		//イベントハンドラを付けるコンポーネントの取得
		showHaveMoney = (TextView)findViewById(R.id.textView2);

		showPictPriceList.add((TextView)findViewById(R.id.textView3));
		showPictPriceList.add((TextView)findViewById(R.id.textView4));
		showPictPriceList.add((TextView)findViewById(R.id.textView5));
		showPictPriceList.add((TextView)findViewById(R.id.textView6));
		showPictPriceList.add((TextView)findViewById(R.id.textView7));
		showPictPriceList.add((TextView)findViewById(R.id.textView8));
		showPictPriceList.add((TextView)findViewById(R.id.textView9));
		showPictPriceList.add((TextView)findViewById(R.id.textView10));

		purchaseButtonList.add((Button)findViewById(R.id.Button1));
		purchaseButtonList.add((Button)findViewById(R.id.Button2));
		purchaseButtonList.add((Button)findViewById(R.id.Button3));
		purchaseButtonList.add((Button)findViewById(R.id.Button4));
		purchaseButtonList.add((Button)findViewById(R.id.Button5));
		purchaseButtonList.add((Button)findViewById(R.id.Button6));
		purchaseButtonList.add((Button)findViewById(R.id.Button7));
		purchaseButtonList.add((Button)findViewById(R.id.Button8));

		backTopButton = (Button)findViewById(R.id.backTopButton);

		//サーブレットに接続し画面を更新する
		new AvatarBuyServlet().execute(this);


		//リスナー
		//購入ボタンを押された時の処理
		purchaseButtonList.get(0).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(0).setEnabled(false);
						new AvatarBuyServlet("1").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(1).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(1).setEnabled(false);
						new AvatarBuyServlet("2").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(2).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(2).setEnabled(false);
						new AvatarBuyServlet("3").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(3).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(3).setEnabled(false);
						new AvatarBuyServlet("4").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(4).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(4).setEnabled(false);
						new AvatarBuyServlet("5").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(5).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(5).setEnabled(false);
						new AvatarBuyServlet("6").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(6).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(6).setEnabled(false);
						new AvatarBuyServlet("7").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		purchaseButtonList.get(7).setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				/* ここでアラート */
				alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
		                // はい ボタンクリック処理
						purchaseButtonList.get(7).setEnabled(false);
						new AvatarBuyServlet("8").execute(AvatarBuy.this);
		            }
		        });
				alert.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    // いいえ ボタンクリック処理
	                	/* なにもしない */
	                }
	            });
				alert.create().show();
			}
		});

		backTopButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//トップページに戻る
				Intent intent = new Intent(AvatarBuy.this,MainActivity.class);
				startActivity(intent);
			}
		});

	}

	public String getUserId() {
		return userId;
	}
	public void setHaveMoney(String haveMoney){
		this.haveMoney = haveMoney;
	}

	public TextView getShowHaveMoney() {
		return showHaveMoney;
	}

	public List<TextView> getShowPictPriceList() {
		return showPictPriceList;
	}

	public List<Button> getPurchaseButtonList() {
		return purchaseButtonList;
	}

	public boolean checkPurchaseButton(String price) {
		if(Integer.parseInt(price) > Integer.parseInt(haveMoney)){
			return false;		//価格 > 所持金 の場合falseを返す
		}else{
			return true;
		}
	}



}
