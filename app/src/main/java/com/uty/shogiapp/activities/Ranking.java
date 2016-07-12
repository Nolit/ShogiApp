package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.RankingServlet;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class Ranking extends ListActivity {

	//ページレイアウト
	private LayoutInflater mInflater;		//リストビュー
    private String userId;					//自分のユーザーID
	private TextView showUserName;			//自分のユーザー名
	private TextView nowRankDataText;		//現在表示しているランキング
	private String rankingType = "レート"; 	//ランキングの種類"

	//タブ
	private TextView rateButton;			//レートランキング表示
	private TextView winNumButton;			//勝利数ランキング表示
	private TextView streakButton;			//連続勝利数ランキング表示

	//ランキング一覧
	private List<RankingBean> rankingListView = new ArrayList<RankingBean>();		//リストビュー	順位、ユーザー名、称号、級、データ
	private TextView rankingNum;			//順位
	private Button rankerName;				//ランカーのマイページに飛ぶボタン
	private TextView rankerTitleName;			//ランカーの称号名
	private TextView rankerClassPosition;		//ランカーの級位
	private TextView rankerData;				//ランカーのデータ
	private ListAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//プリファレンスからIDの取得
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
        setContentView(R.layout.ranking);

		//イベントハンドラを付けるコンポーネントの取得
		showUserName = (TextView)findViewById(R.id.showUserName);		//ユーザー名
    	nowRankDataText = (TextView)findViewById(R.id.nowRankDataText);	//現在表示しているランキング
		rateButton = (Button)findViewById(R.id.rateButton);				//レートランキングに変える
		winNumButton = (Button)findViewById(R.id.winNumButton);			//勝利数ランキングに変える
		streakButton = (Button)findViewById(R.id.streakButton);			//ストリークランキングに変える

		//ランキング項目
		rankingNum = (TextView)findViewById(R.id.rankingNumText);
		rankerName = (Button)findViewById(R.id.rankerNameButton);
		rankerTitleName = (TextView)findViewById(R.id.rankerTitleNameText);
		rankerClassPosition = (TextView)findViewById(R.id.rankerClassPositionText);
		rankerData = (Button)findViewById(R.id.rankerDataText);

        adapter = new ListAdapter(getApplicationContext(),rankingListView);
        setListAdapter(adapter);

		//リスナー
        rateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	nowRankDataText.setText("レートランキング");
            	new RankingServlet(rateButton.getText().toString()).execute(Ranking.this);
            	adapter.notifyDataSetChanged();
           }
        });

        winNumButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	nowRankDataText.setText("勝利数ランキング");
            	new RankingServlet(winNumButton.getText().toString()).execute(Ranking.this);
            	adapter.notifyDataSetChanged();
           }
        });

        streakButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	nowRankDataText.setText("最高ストリークランキング");
            	new RankingServlet(streakButton.getText().toString()).execute(Ranking.this);
            	adapter.notifyDataSetChanged();
           }
        });


    }

	class ListAdapter extends ArrayAdapter<RankingBean>{

		public ListAdapter(Context context, List<RankingBean> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.ranking_row, null);
			}
			final RankingBean item = this.getItem(position);
			if(item != null){

				rankingNum = (TextView)convertView.findViewById(R.id.rankingNumText);
				if(Integer.parseInt(item.getRankingNum()) < 10){
					rankingNum.setText("0"+item.getRankingNum()+"位");
				}else{
					rankingNum.setText(item.getRankingNum()+"位");
				}

				rankerName = (Button)convertView.findViewById(R.id.rankerNameButton);
				rankerName.setText(item.getRankerName());
				rankerName.setAllCaps(false);
				rankerName.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
						/* マッチングページへのインテント処理を追加して！ */
						System.out.println("ボタンに対応する、渡す値のユーザー番号:"+item.getRankerName());
					}
				});

				rankerTitleName = (TextView)convertView.findViewById(R.id.rankerTitleNameText);
				rankerTitleName.setText(item.getRankerTitleName());
				rankerClassPosition = (TextView)convertView.findViewById(R.id.rankerClassPositionText);
				rankerClassPosition.setText(item.getRankerClassPosition());
				rankerData = (TextView)convertView.findViewById(R.id.rankerDataText);
				rankerData.setText(item.getRankerData());

			}
			return convertView;

		}
	}

	@Override
	public void onResume(){
		super.onResume();
		System.out.println("ランキングページ:onResume()...:サーブレットとの通信を開始します_____________________");
		new RankingServlet(rankingType).execute(this);
	}

	//ここからセッター・ゲッター
    public String getUserId() {
		return userId;
	}

	public TextView getShowUserName() {
		return showUserName;
	}

	public List<RankingBean> getRankingListView() {
		return rankingListView;
	}

	public void updateAdapter() {
		adapter.notifyDataSetChanged();
	}


}