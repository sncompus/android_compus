package com.compus.function.schedule;

import java.util.ArrayList;

import java.util.List;

import com.compus.Constants;
import com.compus.MainService;
import com.compus.R;
import com.compus.app.CompusApplication;
import com.compus.db.DBHelper;
import com.compus.domain.Schedule;
import com.compus.tools.HttpUtil;
import com.compus.tools.JsonUtil;
import com.compus.tools.NetConnectDetectUtil;
import com.compus.tools.SPUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleShow extends Activity {

	// ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
	// 包名为 android-support-v4.jar
	private ViewPager mPager;// 页卡内容,即主要显示内容的画面
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private CompusApplication appCookie;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
	private String WEEK = "0";
	SPUtil sp;
	protected MenuItem refreshItem;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_schedule_show);
		appCookie = ((CompusApplication) getApplicationContext());
		sp = new SPUtil(this);
		dialog = new ProgressDialog(ScheduleShow.this);
		dialog.setTitle("提示");
		dialog.setMessage("加载中......");
		InitTextView();

		InitImageView();
		InitViewPager();
		prepareData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			if (NetConnectDetectUtil.isNetworkConnected(this)) {
				sp.setValue("hascome", false);
				prepareData();
			} 
			else {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("设置网络");
				builder.setMessage("网络错误请设置网络");
				builder.setPositiveButton("设置网络", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClassName("com.android.settings",
								"com.android.settings.WirelessSettings");
						if (android.os.Build.VERSION.SDK_INT > 10) {
							startActivity(new Intent(
									android.provider.Settings.ACTION_SETTINGS));
						} else {
							startActivity(new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						}
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish();
					}
				});
				builder.create().show();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);
		tv4 = (TextView) findViewById(R.id.text4);
		tv5 = (TextView) findViewById(R.id.text5);
		tv6 = (TextView) findViewById(R.id.text6);
		tv7 = (TextView) findViewById(R.id.text7);

		tv1.setOnClickListener(new MyOnClickListener(0));
		tv2.setOnClickListener(new MyOnClickListener(1));
		tv3.setOnClickListener(new MyOnClickListener(2));
		tv4.setOnClickListener(new MyOnClickListener(3));
		tv5.setOnClickListener(new MyOnClickListener(4));
		tv6.setOnClickListener(new MyOnClickListener(5));
		tv7.setOnClickListener(new MyOnClickListener(6));

	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();

		GetSchedule getSchedule = new GetSchedule(this);

		View monView = getSchedule.getScheduleView(1);// 1为星期一
		View tueView = getSchedule.getScheduleView(2);
		View wedView = getSchedule.getScheduleView(3);
		View thuView = getSchedule.getScheduleView(4);
		View friView = getSchedule.getScheduleView(5);
		View satView = getSchedule.getScheduleView(6);
		View sunView = getSchedule.getScheduleView(7);

		listViews.add(monView);
		listViews.add(tueView);
		listViews.add(wedView);
		listViews.add(thuView);
		listViews.add(friView);
		listViews.add(satView);
		listViews.add(sunView);

		Intent intent = getIntent();
		WEEK = intent.getIntExtra("POSITION", 1) + "";// 1为星期一
		// Log.i("intent.getIntExtra", WEEK);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		// currIndex=Integer.parseInt(WEEK)-1;
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 为mPage设置了另一个监听
		mPager.setCurrentItem(Integer.parseInt(WEEK) - 1);

		// Log.i("WEEK_int", Integer.parseInt(WEEK)-1+"");

	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a_small)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 7 - bmpW) / 2;// 计算偏移量
		// int startoffset=offset*2*currIndex;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;
		int four = one * 4;
		int five = one * 5;
		int six = one * 6;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {// arg0为目的选项卡

			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, 0, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, one, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, two, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, three, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, three, 0, 0);
				}
				break;
			case 4:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, four, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, four, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, four, 0, 0);
				}
				break;
			case 5:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, five, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, five, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, five, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, five, 0, 0);
				} else if (currIndex == 6) {
					animation = new TranslateAnimation(six, five, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, five, 0, 0);
				}
				break;
			case 6:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, six, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, six, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, six, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, six, 0, 0);
				} else if (currIndex == 5) {
					animation = new TranslateAnimation(five, six, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, six, 0, 0);
				}
				break;
			default:
				arg0 = currIndex;
				animation = new TranslateAnimation(offset, one * currIndex, 0,
						0);
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void prepareData() {
		if (sp.getValue("hascome", false)) {
			showFromDB();
		} else {
			new ScheduleTask().execute(Constants.URL_SCHEDULE);
			sp.setValue("hascome", true);
		}

	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(1000);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
		//		Log.i("arg0", "---isRunning--..>>>>" + isRunning + serviceList.get(i).service.getClassName());
				break;
			}
//			Log.i("arg0",
//					"-----..>>>>" + serviceList.get(i).service.getClassName());
		}
//		Log.i("arg0", "---className--..>>>>" + className);
		return isRunning;
	}

	class ScheduleTask extends AsyncTask<String, Void, List<Schedule>> {

		@Override
		protected List<Schedule> doInBackground(String... params) {
			String jsonStr = null;

			jsonStr = HttpUtil.sendPostStr(params[0], appCookie);
			List<Schedule> schedulelist = JsonUtil
					.parseScheduleJsonStr(jsonStr);
			// Log.i("arg0",jsonStr+schedulelist.size());
			return schedulelist;
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Schedule> result) {
			loadPageView(result);
			saveSchedule(result);
			dialog.dismiss();
			super.onPostExecute(result);
		}

	}

	public void saveSchedule(List<Schedule> result) {
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		// 获取写入数据对象
		SQLiteDatabase db = DBhelp.getWritableDatabase();
		// 实例化放置数据对象
		ContentValues values = new ContentValues();
		// 数据准备
		db.delete(DBHelper.TB_SCHEDULE, null, null);
		for (int i = 0; i < result.size(); i++) {
			// ", result.get(i));values.put("
			values.put("scheduleid", result.get(i).getScheduleid());
			values.put("name", result.get(i).getName());
			values.put("credit", result.get(i).getCredit());
			values.put("attrubute", result.get(i).getAttrubute());
			values.put("exam", result.get(i).getExam());
			values.put("teacher", result.get(i).getTeacher());
			values.put("beginweeek", result.get(i).getBeginweeek());
			values.put("endweek", result.get(i).getEndweek());
			values.put("type", result.get(i).getType());
			values.put("day", result.get(i).getDay());
			values.put("jc", result.get(i).getJc());
			values.put("compus", result.get(i).getCompus());
			values.put("building", result.get(i).getBuilding());
			values.put("room", result.get(i).getRoom());
			// 插入数据
			// db.insert(DBHelper.TB_NEWS, null, values);
			db.insert(DBHelper.TB_SCHEDULE, null, values);
		}

	}

	public void showFromDB() {
		List<Schedule> result = listSchedule(null);
		loadPageView(result);
	}

	public List<Schedule> listSchedule(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select * from " + DBHelper.TB_SCHEDULE;
		SQLiteDatabase database = null;
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		try {
			database = DBhelp.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				Schedule schedule = new Schedule();
				// ")));schedule.setScheduleid(cursor.getString(cursor.getColumnIndex("
				schedule.setScheduleid(cursor.getString(cursor
						.getColumnIndex("scheduleid")));
				schedule.setName(cursor.getString(cursor.getColumnIndex("name")));
				schedule.setCredit(cursor.getString(cursor
						.getColumnIndex("credit")));
				schedule.setAttrubute(cursor.getString(cursor
						.getColumnIndex("attrubute")));
				schedule.setExam(cursor.getString(cursor.getColumnIndex("exam")));
				schedule.setTeacher(cursor.getString(cursor
						.getColumnIndex("teacher")));
				schedule.setBeginweeek(cursor.getString(cursor
						.getColumnIndex("beginweeek")));
				schedule.setEndweek(cursor.getString(cursor
						.getColumnIndex("endweek")));
				schedule.setType(cursor.getString(cursor.getColumnIndex("type")));
				schedule.setDay(cursor.getString(cursor.getColumnIndex("day")));
				schedule.setJc(cursor.getString(cursor.getColumnIndex("jc")));
				schedule.setCompus(cursor.getString(cursor
						.getColumnIndex("compus")));
				schedule.setBuilding(cursor.getString(cursor
						.getColumnIndex("building")));
				schedule.setRoom(cursor.getString(cursor.getColumnIndex("room")));

				list.add(schedule);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public void loadPageView(List<Schedule> result) {
		List<List<Schedule>> list = new ArrayList<List<Schedule>>();
		for (int i = 0; i < 7; i++) {
			List<Schedule> s = new ArrayList<Schedule>();
			list.add(s);
		}
		for (int i = 0; i < result.size(); i++) {
			if ("1".equals(result.get(i).getDay())) {
				list.get(0).add(result.get(i));
			}
			if ("2".equals(result.get(i).getDay())) {
				list.get(1).add(result.get(i));
			}
			if ("3".equals(result.get(i).getDay())) {
				list.get(2).add(result.get(i));
			}
			if ("4".equals(result.get(i).getDay())) {
				list.get(3).add(result.get(i));
			}
			if ("5".equals(result.get(i).getDay())) {
				list.get(4).add(result.get(i));
			}
			if ("6".equals(result.get(i).getDay())) {
				list.get(5).add(result.get(i));
			}
			if ("7".equals(result.get(i).getDay())) {
				list.get(6).add(result.get(i));
			}
//			Log.i(Constants.TAG, result.get(i).getName()
//					+ result.get(i).getDay() + result.get(i).getJc());
		}
		GetSchedule getSchedule = new GetSchedule(ScheduleShow.this);
		for (int i = 0; i < listViews.size(); i++) {
			getSchedule.loadDataFromNet(listViews.get(i), i + 1, list.get(i));
		}
		Intent intent = getIntent();
		WEEK = intent.getIntExtra("POSITION", 1) + "";// 1为星期一
		// Log.i("intent.getIntExtra", WEEK);
		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		// currIndex=Integer.parseInt(WEEK)-1;
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 为mPage设置了另一个监听
		mPager.setCurrentItem(Integer.parseInt(WEEK) - 1);
		if (!isServiceRunning(ScheduleShow.this, MainService.class.getName())) {
			startService(new Intent(ScheduleShow.this, MainService.class));
		}
	}	
}
