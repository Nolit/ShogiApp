package com.uty.shogiapp.activities;

import com.uty.shogiapp.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

class CustomDialog extends Dialog {

    private Handler mHandler = new Handler();   // ハンドラー
    private int mShowTime;
    private Runnable mRunDismiss;
    private Runnable mRunCountdown;
    private int mCount;
    private Button ok;
    private Button cancel;
    private TextView tv;

    // ////////////////////////////////////////////////////////////・
    // コンストラクタ一式
    public CustomDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.setContentView(R.layout.custom_dialog);
    }
    public CustomDialog(Context context, int theme) {
        super(context, theme);
        this.setContentView(R.layout.custom_dialog);
        ok = (Button)findViewById(R.id.signup);
        setCanceledOnTouchOutside(false);
//        ok.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				System.out.println("タッチされました");
//				CustomDialog.this.dismiss();
//				return false;
//			}
//		});
        cancel = (Button)findViewById(R.id.button2);
        this.tv = (TextView)findViewById(R.id.TextView1);
    }
    public CustomDialog(Context context) {
        super(context);
        this.setContentView(R.layout.custom_dialog);
    }

    // ////////////////////////////////////////////////////////////
    // カウントダウンしたい期間を設定する
    public void setShowTime(int time) {
        this.mShowTime = time;
        this.mCount = time / 1000;
    }

    // ////////////////////////////////////////////////////////////
    // ダイアログの表示開始
    @Override
    public void show() {
        final CustomDialog dlg = this;

        // ダイアログを削除するためのモノ
        this.mRunDismiss = new Runnable() {
            public void run() {
                dlg.dismiss();
            }
        };

        // 一定期間ごとに表示されるタイマーを更新するためのモノ
        this.mRunCountdown = new Runnable() {
            public void run() {
                dlg.updateCountdown();
                // 一定期間ごとにカウントダウン
                dlg.mHandler.postDelayed(dlg.mRunCountdown, 1000);
            }
        };
        // しばらくまってから実行
        this.mHandler.postDelayed(this.mRunDismiss, this.mShowTime + 1000);
        this.mHandler.postDelayed(this.mRunCountdown, 1000);

        super.show();
    }

    // ////////////////////////////////////////////////////////////
    // カウントダウンを更新する
    public void updateCountdown() {
        this.mCount--;
        EditText editText = (EditText)this.findViewById(R.id.editText1);
        editText.setText("" + this.mCount);
    }
	/**
	 * okを取得します。
	 * @return ok
	 */
	public Button getOk() {
	    return ok;
	}
	/**
	 * cancelを取得します。
	 * @return cancel
	 */
	public Button getCancel() {
	    return cancel;
	}

	public TextView getTv(){
		return tv;
	}
}