package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;



public class Loading extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("Loading_onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		Intent i = getIntent();

		//先手名前
		TextView senteName = (TextView) findViewById(R.id.senteName);
		senteName.setText(i.getStringExtra("senteName"));
		//先手アバター
		ImageView senteAvatar = (ImageView) findViewById(R.id.ImageView02);
		System.out.println(i.getStringExtra("senteAvatar"));
		senteAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("senteAvatar"))));
		//先手称号
		TextView senteTitle = (TextView) findViewById(R.id.textView4);
		senteTitle.setText(i.getStringExtra("senteTitle"));
		//先手レート
		TextView senteRate = (TextView) findViewById(R.id.senteRateText);
		senteRate.setText("Rate:"+i.getStringExtra("senteRate"));
		//先手アニメーション設定
		Animation animSente = AnimationUtils.loadAnimation(this, R.anim.loading1);
		senteName.startAnimation(animSente);
		senteAvatar.startAnimation(animSente);
		senteTitle.startAnimation(animSente);
		senteRate.startAnimation(animSente);

		//後手名前
		TextView goteName = (TextView) findViewById(R.id.textView2);
		goteName.setText(i.getStringExtra("goteName"));
		//後手アバター
		ImageView goteAvatar = (ImageView) findViewById(R.id.ImageView01);
		goteAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), Integer.valueOf(i.getStringExtra("goteAvatar"))));
		//後手称号
		TextView goteTitle = (TextView) findViewById(R.id.textView5);
		goteTitle.setText(i.getStringExtra("goteTitle"));
		//後手レート
		TextView goteRate = (TextView) findViewById(R.id.goteRateText);
		goteRate.setText("Rate:"+i.getStringExtra("goteRate"));
		//後手アニメーション設定
		Animation animGote = AnimationUtils.loadAnimation(this, R.anim.loading1);
		goteName.startAnimation(animGote);
		goteAvatar.startAnimation(animGote);
		goteTitle.startAnimation(animGote);
		goteRate.startAnimation(animGote);

		Handler hdl = new Handler();
        // ��Q�����Ő؂�ւ��b��(�~���b)���w��A�����7�b
       hdl.postDelayed(new SplashHandler(i), 7000);

    }
    // splashHandler�N���X
   class SplashHandler implements Runnable {
	   private Intent nextIntent;
	   public SplashHandler(Intent nextIntent) {
		   this.nextIntent = nextIntent;
	   }
        public void run() {
            nextIntent.setClass(Loading.this,Battle.class);
            startActivity(nextIntent);
            Loading.this.finish();

        }
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

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}
}