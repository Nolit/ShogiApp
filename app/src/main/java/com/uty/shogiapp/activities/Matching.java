package com.uty.shogiapp.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.uty.shogiapp.R;
import com.uty.shogiapp.websocketClients.MatchingClient;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Matching extends Activity {
    private MatchingClient client;
    private Button cancelButton;
    private List<String> battleInfo = new ArrayList<String>();
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Matching_onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching);

        SharedPreferences pref = getSharedPreferences("tt4",Matching.MODE_PRIVATE);
        this.userId = pref.getString("id","guest");

        this.cancelButton = (Button)findViewById(R.id.btnDisp1);
        System.out.println("AsyncTaskを起動します");
        this.client = new MatchingClient();
        client.execute(this);
        cancelButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                client.cancel(true);
                preActivity();
            }
        });
        TextView text2 = (TextView) findViewById(R.id.textView5);
        Animation anim2 = AnimationUtils.loadAnimation(this,R.anim.matching);
        text2.startAnimation(anim2);
    }

    public void nextActivity() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException{
        Class<?> clazz = Class.forName("tt4.syogi.R$drawable");
        Field f;
        Intent intent = new Intent(Matching.this,Loading.class);
        Iterator<String> it = battleInfo.iterator();
        intent.putExtra("order",it.next());
        intent.putExtra("senteName",it.next());
        intent.putExtra("senteRate",it.next());
        f = clazz.getField(it.next());
        System.out.println("先手アバター:"+f.getInt(null));
        intent.putExtra("senteAvatar",String.valueOf(f.getInt(null)));
        intent.putExtra("senteTitle",it.next());
        intent.putExtra("senteClazz",it.next());
        intent.putExtra("goteName",it.next());
        intent.putExtra("goteRate",it.next());
        f = clazz.getField(it.next());
        System.out.println("後手アバター:"+f.getInt(null));
        intent.putExtra("goteAvatar",String.valueOf(f.getInt(null)));
        intent.putExtra("goteTitle",it.next());
        intent.putExtra("goteClazz",it.next());

        intent.putExtra("ticketNum",it.next());
        startActivity(intent);
        Matching.this.finish();

    }

    public void preActivity(){
        Intent intent = new Intent(Matching.this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * cancelButtonを取得します。
     * @return cancelButton
     */
    public Button getCancelButton() {
        return cancelButton;
    }
    /**
     * battleInfoを取得します。
     * @return battleInfo
     */
    public List<String> getBattleInfo() {
        return battleInfo;
    }
    /**
     * userIdを取得します。
     * @return userId
     */
    public String getUserId() {
        return userId;
    }
}