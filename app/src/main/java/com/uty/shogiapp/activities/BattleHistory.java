package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.BattleHistoryServlet;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BattleHistory extends ListActivity {

    private String userId;				//ユーザーID
	private TextView showUserName;		//ユーザー名

	private LayoutInflater mInflater;
	private Button opponentName;		//対局相手のマイページに飛ぶボタン
	private TextView opponentRate;		//対局相手のレート
	private TextView battleResult;		//勝敗
	private TextView battleDate;		//対局日時
	private Button kifuButton;			//棋譜番号に対応する棋譜ページに飛ぶボタン

    List<BattleHistoryBean> battleHistoryListView = new ArrayList<BattleHistoryBean>();		//リストビュー	対局相手の名前、レート、勝敗、日時
    private ListAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//プリファレンスからIDの取得
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
        setContentView(R.layout.battle_history);

		//イベントハンドラを付けるコンポーネントの取得
		showUserName = (TextView)findViewById(R.id.showUserName);		//ユーザー名

		opponentName = (Button)findViewById(R.id.opponentNameButton);
		opponentRate = (TextView)findViewById(R.id.opponentRateText);
		battleResult = (TextView)findViewById(R.id.resultText);
		battleDate = (TextView)findViewById(R.id.dateText);
		this.kifuButton = (Button)findViewById(R.id.kifuButton);

        adapter = new ListAdapter(getApplicationContext(),battleHistoryListView);
        setListAdapter(adapter);

        //サーブレットに接続し画面を更新する
        new BattleHistoryServlet().execute(this);

    }

	class ListAdapter extends ArrayAdapter<BattleHistoryBean>{

		public ListAdapter(Context context, List<BattleHistoryBean> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.battle_history_row, null);
			}
			final BattleHistoryBean item = this.getItem(position);
			if(item != null){

				opponentName = (Button)convertView.findViewById(R.id.opponentNameButton);
				opponentName.setText(item.getOpponentName());
				opponentName.setAllCaps(false);
				opponentName.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
			               Intent intent = new Intent(BattleHistory.this,MyPageOther.class);
			               System.out.println("対局履歴ページ"+item.getOpponentName()+"様のページへインテントします");
			               intent.putExtra("userName", item.getOpponentName());
			               startActivity(intent);
					}
				});

				opponentRate = (TextView)convertView.findViewById(R.id.opponentRateText);
				opponentRate.setText(item.getRate());
				battleResult = (TextView)convertView.findViewById(R.id.resultText);
				battleResult.setText(item.getBattleResult());
				battleDate = (TextView)convertView.findViewById(R.id.dateText);
				battleDate.setText(item.getBattleDate());

				kifuButton = (Button)convertView.findViewById(R.id.kifuButton);
				kifuButton.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
						System.out.println("インテント処理:棋譜番号:"+item.getBattleNum());
						System.out.println(item.getSenteName()+","+item.getGoteName()+","+item.getSenteClass()+","
										+item.getGoteClass()+","+item.getSenteAvatarName()+","+item.getGoteAvatarName()+"を渡します");
			            Intent intent = new Intent(BattleHistory.this,Kifu.class);
			            intent.putExtra("battleNum", item.getBattleNum());				//棋譜番号
			            intent.putExtra("SenteName", item.getSenteName());				//先手の名前
			            intent.putExtra("GoteName", item.getGoteName());				//後手の名前
			            intent.putExtra("SenteClass", item.getSenteClass());			//先手の級
			            intent.putExtra("GoteClass", item.getGoteClass());				//後手の級
			            intent.putExtra("SenteAvatarName", item.getSenteAvatarName());	//先手のアバター名
			            intent.putExtra("GoteAvatarName", item.getGoteAvatarName());	//後手のアバター名
			            startActivity(intent);
					}
				});
			}
			return convertView;

		}
	}

	//ここからセッター・ゲッター
	public String getUserId() {
		return userId;
	}

	public TextView getShowUserName() {
		return showUserName;
	}

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public Button getOpponentName() {
		return opponentName;
	}

	public TextView getOpponentRate() {
		return opponentRate;
	}

	public TextView getBattleResult() {
		return battleResult;
	}

	public TextView getBattleDate() {
		return battleDate;
	}

	public Button getKifuButton() {
		return kifuButton;
	}

	public List<BattleHistoryBean> getBattleHistoryListView() {
		return battleHistoryListView;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setShowUserName(TextView showUserName) {
		this.showUserName = showUserName;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	public void setOpponentName(Button opponentName) {
		this.opponentName = opponentName;
	}

	public void setOpponentRate(TextView opponentRate) {
		this.opponentRate = opponentRate;
	}

	public void setBattleResult(TextView battleResult) {
		this.battleResult = battleResult;
	}

	public void setBattleDate(TextView battleDate) {
		this.battleDate = battleDate;
	}

	public void setKifuButton(Button kifuButton) {
		this.kifuButton = kifuButton;
	}

	public void setBattleHistoryListView(
			List<BattleHistoryBean> battleHistoryListView) {
		this.battleHistoryListView = battleHistoryListView;
	}

	public void updateAdapter() {
		adapter.notifyDataSetChanged();
	}
}