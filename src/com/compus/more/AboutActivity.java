package com.compus.more;


import com.compus.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//AdManager.init(this, "6caa2fadf4fcb4ff", "c9e2d6acd01eab5b", 30, false);
		setContentView(R.layout.app_about);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//������µ��Ƿ��ؼ�������û���ظ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}
}
