package com.compus.function;


import com.compus.R;
import com.compus.function.schedule.ScheduleShow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FunctionActivity extends Activity implements OnClickListener {

	TextView mTitleView;
	ImageView schedule_IV;
	
	Intent scheduleIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_activity);
		prepareView();
		mTitleView.setText(R.string.category_channel);
		prepareIntent();
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
		schedule_IV = (ImageView) findViewById(R.id.channel_schedule);
		schedule_IV.setOnClickListener(this);
	}

	private void prepareIntent() {
		scheduleIntent = new Intent(this, ScheduleShow.class);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.channel_schedule:
			startActivity(scheduleIntent);
			break;
		default:
			break;
		}
	}
	
}
