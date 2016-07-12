package com.uty.shogiapp.websocketClients;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class OuteColorChanger extends AsyncTask<TextView,Void,Void>{
	TextView outeText;
	@Override
	protected Void doInBackground(TextView... params) {
		outeText = params[0];
		return null;
	}

	@Override
	protected void onPostExecute(Void param){
		outeText.setTextColor(Color.RED);
    	AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(2000);
        outeText.startAnimation(animation);
        outeText.setTextColor(0x00000000);
	}
}
