package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

public class ResultView extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		System.out.println("ResultView_onCreate...");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultview);
		Intent i = getIntent();
		TextView tv = (TextView)findViewById(R.id.displayText);
		tv.setText(i.getStringExtra("message"));
		Handler hdl = new Handler();
        // ��Q�����Ő؂�ւ��b��(�~���b)���w��A�����7�b
       hdl.postDelayed(new SplashHandler(i), 5000);

    }
   class SplashHandler implements Runnable {
	   private Intent nextIntent;
	   public SplashHandler(Intent nextIntent) {
		   this.nextIntent = nextIntent;
	   }
        public void run() {
            nextIntent.setClass(ResultView.this,Result.class);
            startActivity(nextIntent);
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
}
