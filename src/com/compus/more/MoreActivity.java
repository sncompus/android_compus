package com.compus.more;

import com.baidu.android.pushservice.PushManager;
import com.compus.Constants;
import com.compus.MainService;
import com.compus.R;
import com.compus.account.LoginActivity;
import com.compus.app.CompusApplication;
import com.compus.domain.UpdataInfo;
import com.compus.more.SlideSwitch.OnSwitchChangedListener;
import com.compus.push.Utils;
import com.compus.tools.HttpUtil;
import com.compus.tools.SPUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity implements OnClickListener, OnSwitchChangedListener {

	TextView mTitleView;
	TableRow accountTR;
	TableRow versionTR;
	TableRow helpTR;
	TableRow aboutTR;
	SlideSwitch updateSwitch;
	SlideSwitch scheduleSwitch;
	SlideSwitch pushSwitch;
	private CompusApplication myApplication;
	
	SPUtil sp;

	// TextView accountView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		sp = new SPUtil(this);
		prepareView();
		mTitleView.setText(R.string.category_more);
	}

	class UpgradeTask extends AsyncTask<String, Void, UpdataInfo> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(UpdataInfo result) {
			if(result != null) {
				checkVersion(result);
			}else{
				Toast.makeText(MoreActivity.this, "连接失败", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected UpdataInfo doInBackground(String... params) {
			UpdataInfo info = null;
			try {
				info = new HttpUtil().getJsonVersion(params[0]);
			} catch (Exception e) {
				info = null;
				e.printStackTrace();
			}
			return info;
		}
		
	}
	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
		accountTR = (TableRow) findViewById(R.id.more_page_row1);
		versionTR = (TableRow) findViewById(R.id.more_page_row6);
		helpTR = (TableRow) findViewById(R.id.more_page_row5);
		aboutTR = (TableRow) findViewById(R.id.more_page_row7);
		updateSwitch = (SlideSwitch)findViewById(R.id.slideSwitch1);
		scheduleSwitch = (SlideSwitch)findViewById(R.id.slideSwitch2);
		pushSwitch = (SlideSwitch)findViewById(R.id.slideSwitch3);
		helpTR.setOnClickListener(this);
		accountTR.setOnClickListener(this);
		versionTR.setOnClickListener(this);
		aboutTR.setOnClickListener(this);
		updateSwitch.setOnSwitchChangedListener(this);
		scheduleSwitch.setOnSwitchChangedListener(this);
		pushSwitch.setOnSwitchChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_page_row1:
			Intent loginIntent = new Intent();
			loginIntent.setClass(MoreActivity.this, LoginActivity.class);
			startActivity(loginIntent);
			break;
		case R.id.more_page_row6:
			new UpgradeTask().execute(Constants.URL_VERSION);
			break;
		case R.id.more_page_row5:
			Intent helpIntent = new Intent();
			helpIntent.setClass(MoreActivity.this, FeedBackActivity.class);
			startActivity(helpIntent);
			break;
		case R.id.more_page_row7:
			Intent aboutIntent = new Intent();
			aboutIntent.setClass(MoreActivity.this, AboutActivity.class);
			startActivity(aboutIntent);
			break;
		default:
			break;
		}
	}

	// private void prepareIntent() {
	// Intent intent = new Intent(MoreActivity.this, LoginActivity.class);
	// startActivity(intent);
	// }
	
	public void checkVersion(UpdataInfo info) {
		myApplication =  (CompusApplication) getApplication();
		final String down_url = Constants.URL_IP+info.getUrl();
			if (myApplication.localVersion < info.getVersionCode()) {


				AlertDialog.Builder alert = new AlertDialog.Builder(MoreActivity.this);
				alert.setTitle("软件升级")//"发现新版本,建议立即更新使用.\n"+
						.setMessage(info.getDescription()+"\n版本号："+info.getVersionName())
						.setPositiveButton("更新",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 开启更新服务UpdateService
										// 这里为了把update更好模块化，可以传一些updateService依赖的值
										// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
										Intent updateIntent = new Intent(
												MoreActivity.this,
												UpdateService.class);
										updateIntent.putExtra(
												"app_name",
												getResources().getString(
														R.string.app_name));
										updateIntent.putExtra(
												"down_url",
												down_url);
										startService(updateIntent);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
				alert.create().show();

			}else {
				Toast.makeText(MoreActivity.this, "当前已是最新版本", Toast.LENGTH_LONG).show();
			}
		}

	@Override
	public void onSwitchChanged(SlideSwitch obj, int status) {
		Log.i(Constants.TAG, obj.getId()+"--"+R.id.slideSwitch3+"--"+status+PushManager.isPushEnabled(MoreActivity.this));
		switch (obj.getId()) 
		{
		case R.id.slideSwitch1:
			if(status==0)
				sp.setValue("updateinfo", false);
			if(status==1)
				sp.setValue("updateinfo", true);
			break;
		case R.id.slideSwitch2:
			if(status==0) {
				sp.setValue("schedule", false);
//				ActivityManager activityManager = (ActivityManager) this
//						.getSystemService(Context.ACTIVITY_SERVICE);
//				List<ActivityManager.RunningServiceInfo> serviceList = activityManager
//						.getRunningServices(1000);
//				if (serviceList.size() > 0) {
//					for (int i = 0; i < serviceList.size(); i++) {
//						if (serviceList.get(i).service.getClassName().equals(MainService.class.getName()) == true) {
//							stopService(new Intent().setComponent(serviceList.get(i).service));
//							break;
//						}
//					}
//				}
//								
//				Log.i(Constants.TAG, "stopService"+sp.getValue("schedule", true));
				
			}
			if(status==1) {
				sp.setValue("schedule", true);
				startService(new Intent(MoreActivity.this, MainService.class));
			}
			break;
		case R.id.slideSwitch3:
			if(PushManager.isPushEnabled(MoreActivity.this)) {
				Utils.setBind(MoreActivity.this, false);
				PushManager.stopWork(getApplicationContext());
			}
			else {
				Utils.setBind(MoreActivity.this, true);
				PushManager.resumeWork(getApplicationContext());
			}
				
			break;
		default:
			break;
		}
	}
	
}
