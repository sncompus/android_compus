package com.compus.account;


import java.util.ArrayList;



import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.compus.Constants;
import com.compus.R;
import com.compus.app.CompusApplication;
import com.compus.db.DBHelper;
import com.compus.domain.Mine;
import com.compus.tools.HttpUtil;
import com.compus.tools.JsonUtil;
import com.compus.tools.NetConnectDetectUtil;
import com.compus.tools.SPUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class AccountActivity extends Activity {

	TextView mTitleView;
	
	TextView accountView;
	
	private BaseListView listview = null;
	private MineAdapter adapter;
	private boolean is_divPage;
	private boolean is_upfPage;
	private boolean is_upfPaging = true;
	private List<Mine> totallist = new ArrayList<Mine>();
	private List<Mine> latestlist = new ArrayList<Mine>();
	private ProgressDialog dialog;
	private CompusApplication appCookie;
	String userinfo = null;
	SPUtil sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_activity);
		prepareView();
		mTitleView.setText(R.string.category_account);
		listview = (BaseListView) findViewById(R.id.listViewMine);
		appCookie = ((CompusApplication) getApplicationContext());
		sp = new SPUtil(this);
		dialog = new ProgressDialog(AccountActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("加载中......");
		adapter = new MineAdapter();
		prepareData();
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					new MineTask().execute(Constants.URL_MINE);
				} else if (is_upfPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (totallist.size() > 0 && latestlist.size() == 0) {
						latestlist.add(totallist.get(0));
					}
					is_upfPaging = true;
					new MineTask().execute(Constants.URL_MINE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
				is_upfPage = (firstVisibleItem == 0);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView title = (TextView) view.findViewById(R.id.mine_title);
				title.setTextColor(Color.GRAY);
				totallist.get(position).setIs_read(true);
				Intent intent = new Intent(AccountActivity.this,
						MineWebActivity.class);
				intent.putExtra("mineurl", Constants.URL_MINE_CONTENT
						+ "?mineid=" + totallist.get(position).getMineid());
				startActivity(intent);
			}
		});
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
//		accountView = (TextView) findViewById(R.id.textView_account);
//		accountView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				prepareIntent();
//			}
//		});
		
		
	}
	
	public void prepareData() {

		showMineByDB();
		if (NetConnectDetectUtil.isNetworkConnected(AccountActivity.this) && sp.getValue("updateinfo", true)) {
			new MineTask().execute(Constants.URL_MINE);
		}else if (totallist==null || totallist.size() == 0) {
			new MineTask().execute(Constants.URL_MINE);
		}

	}

	class MineTask extends AsyncTask<String, Void, List<Mine>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Mine> result) {
			super.onPostExecute(result);// Toast.makeText(AccountActivity.this,
										// "--->"+userinfo, 1).show();
			if (latestlist.size() > 0
					&& result.size() > 0
					&& latestlist.get(0).getMineid() == result.get(0)
							.getMineid()) {
				// transferMinelist(totallist, latestlist);
				latestlist.clear();
				is_upfPaging = false;
				Toast.makeText(AccountActivity.this, "已经是最新消息了......", 0).show();
				return;
			} else if (is_upfPaging && result.size() != 0) {
				is_upfPaging = false;
				Log.i(Constants.TAG, "-clear-- " + totallist.size() + " --- "
						+ latestlist.size());
				totallist.clear();
			}
			// Log.i(Constants.TAG, "--- " + latestlist.size() + " --- " +
			// result.size());
			if (result.size() == 0) {
				Toast.makeText(AccountActivity.this, "已无更多数据......", 0).show();
				return;
			}
			totallist.addAll(result);
			adapter.bindData(totallist);
			if (totallist.size() == result.size()) {
				listview.setAdapter(adapter);
				saveMine();
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		protected List<Mine> doInBackground(String... params) {
			// dialog.show();
			userinfo = HttpUtil.login(null, null, appCookie);
			// Log.i("userinfo", userinfo);System.out.print(userinfo);
			String jsonStr = null;
			if (totallist.size() > 0 && !is_upfPaging) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("time", totallist.get(
						totallist.size() - 1).getTime()));
				jsonStr = HttpUtil.sendPostParamsStr(params[0], pairs);
			} else {
				jsonStr = HttpUtil.sendPostStr(params[0], appCookie);
			}
			List<Mine> minelist = JsonUtil.parseMineJsonStr(jsonStr);
			// dialog.dismiss();
			return minelist;
		}

	}

	class MineAdapter extends BaseAdapter {

		List<Mine> list;
		private LayoutInflater mInflater = LayoutInflater
				.from(AccountActivity.this);

		public void bindData(List<Mine> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			TextView textView = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.list_item_mine, null);
				// holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView
						.findViewById(R.id.mine_title);
				convertView.setTag(holder);
				// textView = new TextView(AccountActivity.this);
			} else {
				holder = (ViewHolder) convertView.getTag();
				// textView = (TextView)convertView;
			}
			// holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
			holder.title.setTextSize(20);
			if (list.get(position).isIs_read())
				holder.title.setTextColor(Color.GRAY);
			else
				holder.title.setTextColor(Color.BLACK);
			Log.i(Constants.TAG,
					"--- " + totallist.size() + " --- " + list.size() + " --- "
							+ position);
			String timeTemp = list.get(position).getTime();
			if (timeTemp.length() > 10) {
				timeTemp = timeTemp.substring(5, 10);
			} else {
				timeTemp = "时间未知";
			}
			String titleTemp = list.get(position).getTitle();
			if (titleTemp == null || "null".equals(titleTemp)
					|| "".equals(titleTemp))
				titleTemp = "当前消息无标题！";
			if (titleTemp.length() > 20)
				titleTemp = titleTemp.substring(0, 18) + "...";
			holder.title.setText(timeTemp + "   " + titleTemp);
			// holder.info.setText((String)mData.get(position).get("info"));
			// textView.setTextSize(20);
			// textView.setText(list.get(position).getTitle());
			return convertView;
		}
	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView title;
		// public TextView info;
	}

	public void saveMine() {
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		// 获取写入数据对象
		SQLiteDatabase db = DBhelp.getWritableDatabase();
		// 实例化放置数据对象
		ContentValues values = new ContentValues();
		// 数据准备
		for (int i = 0; i < Constants.PAGESIZE && i < totallist.size(); i++) {
			values.put(DBHelper.MINEID, totallist.get(i).getMineid());
			values.put(DBHelper.TIME, totallist.get(i).getTime());
			values.put(DBHelper.TITLE, totallist.get(i).getTitle());
			// 插入数据
			// db.insert(DBHelper.TB_MINE, null, values);
			db.replace(DBHelper.TB_MINE, null, values);
		}

	}

	public List<Mine> listMine(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Mine> list = new ArrayList<Mine>();
		String sql = "select * from " + DBHelper.TB_MINE
				+ " order by time desc limit ? offset ?";
		SQLiteDatabase database = null;
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		try {
			database = DBhelp.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Mine mine = new Mine();
				String time = cursor.getString(cursor
						.getColumnIndex(DBHelper.TIME));
				if (time == null || "".equals(time) || "null".equals(time)) {
					time = "待定";
				}
				mine.setTime(time);
				String title = cursor.getString(cursor
						.getColumnIndex(DBHelper.TITLE));
				if (title == null || "".equals(title) || "null".equals(title)) {
					title = "待定";
				}
				mine.setTitle(title);
				int mineid = cursor.getInt(cursor
						.getColumnIndex(DBHelper.MINEID));
				mine.setMineid(mineid);
				// Log.i(Constants.TAG, title+"---"+time+"---"+mineid);
				list.add(mine);
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

	public void showMineByDB() {
		totallist = listMine(new String[] { "" + Constants.PAGESIZE, "0" });
		if (totallist.size() == 0) {
			Toast.makeText(AccountActivity.this, "当前未保存数据当前未保存数据，正在加载网络数据.....", 1)
					.show();
			return;
		}
		adapter.bindData(totallist);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void transferMinelist(List<Mine> d, List<Mine> s) {
		for (int i = 0; i < s.size(); i++) {
			d.add(s.get(i));
		}
		s.clear();
	}
	
	private void prepareIntent() {
		Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}
