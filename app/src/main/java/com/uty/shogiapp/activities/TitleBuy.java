package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.TitleBuyServlet;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TitleBuy extends ListActivity {

    private String userId;		//ユーザーID
    private String haveMoney;	//所持金

	private TextView showHaveMoney;

	private LayoutInflater mInflater;
	private TextView titleName;
	private TextView titleQualifaction;
	private TextView titlePrice;
	private Button purchaseButton;

    List<TitleBuyBean> titleListView = new ArrayList<TitleBuyBean>();		//リストビュー	称号名、条件文、値段、ボタン
    private ListAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//プリファレンスからIDの取得
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
        setContentView(R.layout.title_buy);

		//イベントハンドラを付けるコンポーネントの取得
		showHaveMoney = (TextView)findViewById(R.id.showHaveMoney2);		//所持金

		titleName = (TextView)findViewById(R.id.nameText);
		titleQualifaction = (TextView)findViewById(R.id.qualifactionText);
		titlePrice = (TextView)findViewById(R.id.priceText);
		this.purchaseButton = (Button)findViewById(R.id.purchaseButton);

        adapter = new ListAdapter(getApplicationContext(),titleListView);
        setListAdapter(adapter);

    }

	class ListAdapter extends ArrayAdapter<TitleBuyBean>{

		public ListAdapter(Context context, List<TitleBuyBean> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.title_buy_row, null);
			}
			final TitleBuyBean item = this.getItem(position);
			if(item != null){
				titleName = (TextView)convertView.findViewById(R.id.nameText);
				titleName.setText(item.getTitleName());
				titleQualifaction = (TextView)convertView.findViewById(R.id.qualifactionText);
				titleQualifaction.setText(item.getTitleQualifaction());
				titlePrice = (TextView)convertView.findViewById(R.id.priceText);
				titlePrice.setText(item.getTitlePrice());
				purchaseButton = (Button)convertView.findViewById(R.id.purchaseButton);
				purchaseButton.setText(item.getButtonState());
				purchaseButton.setEnabled(item.isButtonEnable());
				purchaseButton.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
						/* ここでアラート */
						AlertDialog.Builder alert = new AlertDialog.Builder(TitleBuy.this);
						alert.setTitle("購入確認");
						alert.setMessage(item.getTitleName() + "を購入しますか？");
						alert.setPositiveButton("はい", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
				                // はい ボタンクリック処理
								v.setEnabled(false);
								new TitleBuyServlet(item.getTitleName()).execute(TitleBuy.this);	//購入ボタンのリスナー処理
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
			}
			return convertView;

		}
	}

	@Override
	public void onResume(){
		super.onResume();
		System.out.println("称号購入ページ:onResume():サーブレットとの通信を開始します_____________________");
		new TitleBuyServlet().execute(this);
	}

	//ここからゲッター・セッター
	public String getUserId() {
		return userId;
	}

    public String getHaveMoney() {
		return haveMoney;
	}

	public TextView getShowHaveMoney() {
		return showHaveMoney;
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public TextView getTitleName() {
		return titleName;
	}

	public TextView getTitleQualifaction() {
		return titleQualifaction;
	}

	public TextView getTitlePrice() {
		return titlePrice;
	}

	public Button getPurchaseButton() {
		return purchaseButton;
	}

    public List<TitleBuyBean> getTitleListView() {
		return titleListView;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setHaveMoney(String haveMoney) {
		this.haveMoney = haveMoney;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	public void setTitleName(TextView titleName) {
		this.titleName = titleName;
	}

	public void setTitleQualifaction(TextView titleQualifaction) {
		this.titleQualifaction = titleQualifaction;
	}

	public void setTitlePrice(TextView titlePrice) {
		this.titlePrice = titlePrice;
	}

	public void setPurchaseButton(Button purchaseButton) {
		this.purchaseButton = purchaseButton;
	}

	public boolean checkPurchaseButton(String price) {
		if(Integer.parseInt(price) > Integer.parseInt(haveMoney)){
			return false;		//価格 > 所持金 の場合falseを返す
		}else{
			return true;
		}
	}

	public void updateAdapter() {
		adapter.notifyDataSetChanged();
	}
}