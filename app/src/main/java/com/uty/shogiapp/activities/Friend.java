package com.uty.shogiapp.activities;

import java.util.ArrayList;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.servletClients.FriendServlet;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Friend extends ListActivity {

	//ページレイアウト
	private LayoutInflater mInflater;		//リストビュー
    private String userId;					//自分のユーザーID
	private TextView showUserName;			//自分のユーザー名

    //フレンド検索
	private EditText formSearch;			//入力フォーム
	private Button searchButton;			//検索ボタン
	private TextView searchFlag;			//検索結果
	private LinearLayout resultLayout;		//検索結果のレイアウト

	//検索結果
	private Button searchUserName;			//検索結果ユーザーの名前
	private TextView searchTitleName;		//検索結果ユーザーの称号名
	private TextView searchClassPosition;	//検索結果ユーザーの級位
	private TextView searchRate;		//検索結果ユーザーのレート

	//フレンド一覧
	private List<FriendBean> friendListView = new ArrayList<FriendBean>();		//リストビュー	フレンド名、称号、級、レート
	private Button friendName;				//フレンドのマイページに飛ぶボタン
	private TextView friendTitleName;		//フレンドの称号名
	private TextView friendClassPosition;	//フレンドの級位
	private TextView friendRate;			//フレンドのレート
	private Button matchingButton;			//フレンドのユーザー番号のみとマッチングを開始するボタン

	private ListAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//プリファレンスからIDの取得
		SharedPreferences pref = getSharedPreferences("tt4",MODE_PRIVATE);
		userId = pref.getString("id","");

		//xmlの読み込み
        setContentView(R.layout.friend);

		//イベントハンドラを付けるコンポーネントの取得
		showUserName = (TextView)findViewById(R.id.showUserName);		//ユーザー名
		searchFlag = (TextView)findViewById(R.id.searchFlagText);		//検索結果
		resultLayout = (LinearLayout)findViewById(R.id.resultLayout);

		//フレンド一覧
		friendName = (Button)findViewById(R.id.friendNameButton);
		friendTitleName = (TextView)findViewById(R.id.friendTitleNameText);
		friendClassPosition = (TextView)findViewById(R.id.friendClassPositionText);
		friendRate = (TextView)findViewById(R.id.friendRateText);
		matchingButton = (Button)findViewById(R.id.matchingButton);

        adapter = new ListAdapter(getApplicationContext(),friendListView);
        setListAdapter(adapter);

		//ユーザー検索
		formSearch = (EditText)findViewById(R.id.formSearch);
		searchButton = (Button)findViewById(R.id.searchButton);

		//検索結果
		searchUserName = (Button)findViewById(R.id.searchNameButton);				//検索結果のユーザー名
		searchTitleName = (TextView)findViewById(R.id.searchTitleNameText);			//検索結果ユーザーの称号名
		searchClassPosition = (TextView)findViewById(R.id.searchClassPositionText);	//検索結果ユーザーの級位
		searchRate = (TextView)findViewById(R.id.searchRateText);					//検索結果ユーザーのレート

		//リスナー
		searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	new FriendServlet(formSearch.getText().toString()).execute(Friend.this);
           }
        });

		searchUserName.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
	               Intent intent = new Intent(Friend.this,MyPageOther.class);
	               System.out.println("フレンド"+searchUserName.getText().toString()+"様のページへインテントします");
	               intent.putExtra("userName", searchUserName.getText().toString());
	               startActivity(intent);
           }
        });


    }

	class ListAdapter extends ArrayAdapter<FriendBean>{

		public ListAdapter(Context context, List<FriendBean> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.friend_row, null);
			}
			final FriendBean item = this.getItem(position);
			if(item != null){

				friendName = (Button)convertView.findViewById(R.id.friendNameButton);
				friendName.setText(item.getFriendName());
				friendName.setAllCaps(false);
				friendName.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
			               Intent intent = new Intent(Friend.this,MyPageOther.class);
			               System.out.println("フレンドページ"+item.getFriendName()+"様のページへインテントします");
			               intent.putExtra("userName", item.getFriendName());
			               startActivity(intent);
					}
				});

				friendTitleName = (TextView)convertView.findViewById(R.id.friendTitleNameText);
				friendTitleName.setText(item.getTitleName());
				friendClassPosition = (TextView)convertView.findViewById(R.id.friendClassPositionText);
				friendClassPosition.setText(item.getClassPosition());
				friendRate = (TextView)convertView.findViewById(R.id.friendRateText);
				friendRate.setText(item.getRate());

				matchingButton = (Button)convertView.findViewById(R.id.matchingButton);
				matchingButton.setOnClickListener(new OnClickListener() {
					public void onClick(final View v) {
						/* マッチングページへのインテント処理を追加して！ */
						System.out.println("ボタンに対応する、渡す値のユーザー番号:"+item.getUserNum());
					}
				});
			}
			return convertView;

		}
	}

	@Override
	public void onResume(){
		super.onResume();
		System.out.println("フレンドページ:onResume()...:サーブレットとの通信を開始します_____________________");
		new FriendServlet().execute(this);
	}

	//ここからセッター・ゲッター
	public String getUserId() {
		return userId;
	}

	public TextView getShowUserName() {
		return showUserName;
	}

	public List<FriendBean> getFriendListView() {
		return friendListView;
	}

	public TextView getSearchFlag() {
		return searchFlag;
	}

    public LinearLayout getResultLayout() {
		return resultLayout;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public Button getSearchUserName() {
		return searchUserName;
	}

	public TextView getSearchTitleName() {
		return searchTitleName;
	}

	public TextView getSearchClassPosition() {
		return searchClassPosition;
	}

	public TextView getSearchRate() {
		return searchRate;
	}

	public void updateAdapter() {
		adapter.notifyDataSetChanged();
	}
}