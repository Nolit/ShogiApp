package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class name extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name);
		Button btnDisp = (Button)findViewById(R.id.button1);
		Button btnDisp2 = (Button)findViewById(R.id.button2);


		btnDisp.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(name.this,MyPage.class);
				startActivity(intent);
}
		});		
		btnDisp2.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(name.this,MyPage.class);
				startActivity(intent);
			}
			
		});
	}
}