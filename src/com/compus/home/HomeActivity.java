package com.compus.home;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.compus.Constants;
import com.compus.R;
import com.compus.app.CompusApplication;
import com.compus.db.DBHelper;
import com.compus.domain.News;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private BaseListView listview = null;
	private NewsAdapter adapter;
	private boolean is_divPage;
	private boolean is_upfPage;
	private boolean is_upfPaging = true;
	private List<News> totallist = new ArrayList<News>();
	private List<News> latestlist = new ArrayList<News>();
	private ProgressDialog dialog;
	private CompusApplication appCookie;
	String userinfo = null;
	SPUtil sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		listview = (BaseListView) findViewById(R.id.listViewNews);
		appCookie = ((CompusApplication) getApplicationContext());
		sp = new SPUtil(this);
		// adapter = new NewsAdapter();
		// adapter.bindData(getData());
		// listview.setAdapter(adapter);
		dialog = new ProgressDialog(HomeActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("加载中......");
		adapter = new NewsAdapter();
		prepareData();
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (is_divPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					new NewsTask().execute(Constants.URL_NEWS);
					// Toast.makeText(HomeActivity.this, "achive data",
					// 0).show();
					// if(totallist.size() > 0) {
					// String tt = totallist.get(totallist.size()-1).getTime();
					// new
					// NewsTask().execute(Constants.URL_NEWS+"&type=m&date="+tt.s);
					// System.currentTimeMillis()
					// new
					// NewsTask().execute("http://localhost:8080/compus/servlet/ListNewsServlet?pageSize=8&time=2014-04-04 15:53:24.0");
					// Toast.makeText(HomeActivity.this,
					// "---->"+totallist.get(totallist.size()-1).getTime(),
					// 0).show();
					// }=
				} else if (is_upfPage
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (totallist.size() > 0 && latestlist.size() == 0) {
						latestlist.add(totallist.get(0));
					}
					// transferNewslist(latestlist, totallist);
					//Log.i(Constants.TAG, "--ss- " + latestlist.size());
					// totallist.clear();
					is_upfPaging = true;
					new NewsTask().execute(Constants.URL_NEWS);
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
				TextView title = (TextView) view.findViewById(R.id.news_title);
				title.setTextColor(Color.GRAY);
				totallist.get(position).setIs_read(true);
				Intent intent = new Intent(HomeActivity.this,
						NewsWebActivity.class);
				intent.putExtra("newsurl", Constants.URL_NEWS_CONTENT
						+ "?newsid=" + totallist.get(position).getNewsid());
				startActivity(intent);
			}
		});
	}

	public void prepareData() {

		showNewsByDB();
		if (NetConnectDetectUtil.isNetworkConnected(HomeActivity.this) && sp.getValue("updateinfo", true)) {
			new NewsTask().execute(Constants.URL_NEWS);
		}else if (totallist==null || totallist.size() == 0) {
			new NewsTask().execute(Constants.URL_NEWS);
		}

	}

	class NewsTask extends AsyncTask<String, Void, List<News>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<News> result) {
			super.onPostExecute(result);// Toast.makeText(HomeActivity.this,
										// "--->"+userinfo, 1).show();
			if (latestlist.size() > 0
					&& result.size() > 0
					&& latestlist.get(0).getNewsid() == result.get(0)
							.getNewsid()) {
				// transferNewslist(totallist, latestlist);
				latestlist.clear();
				is_upfPaging = false;
				Toast.makeText(HomeActivity.this, "已经是最新消息了......", Toast.LENGTH_SHORT).show();
				return;
			} else if (is_upfPaging && result.size() != 0) {
				is_upfPaging = false;
				//Log.i(Constants.TAG, "-clear-- " + totallist.size() + " --- "
				//		+ latestlist.size());
				totallist.clear();
			}
			// Log.i(Constants.TAG, "--- " + latestlist.size() + " --- " +
			// result.size());
			if (result.size() == 0) {
				Toast.makeText(HomeActivity.this, "已无更多数据......", Toast.LENGTH_SHORT).show();
				return;
			}
			totallist.addAll(result);
			adapter.bindData(totallist);
			if (totallist.size() == result.size()) {
				listview.setAdapter(adapter);
				saveNews();
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		protected List<News> doInBackground(String... params) {
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
			List<News> newslist = JsonUtil.parseNewsJsonStr(jsonStr);
			// dialog.dismiss();
			return newslist;
		}

	}

	class NewsAdapter extends BaseAdapter {

		List<News> list;
		private LayoutInflater mInflater = LayoutInflater
				.from(HomeActivity.this);

		public void bindData(List<News> list) {
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
//			TextView textView = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.list_item_news, null);
				// holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView
						.findViewById(R.id.news_title);
				convertView.setTag(holder);
				// textView = new TextView(HomeActivity.this);
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
			//Log.i(Constants.TAG,
			//		"--- " + totallist.size() + " --- " + list.size() + " --- "
			//				+ position);
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

	public void saveNews() {
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		// 获取写入数据对象
		SQLiteDatabase db = DBhelp.getWritableDatabase();
		// 实例化放置数据对象
		ContentValues values = new ContentValues();
		// 数据准备
		for (int i = 0; i < Constants.PAGESIZE && i < totallist.size(); i++) {
			values.put(DBHelper.NEWSID, totallist.get(i).getNewsid());
			values.put(DBHelper.TIME, totallist.get(i).getTime());
			values.put(DBHelper.TITLE, totallist.get(i).getTitle());
			// 插入数据
			// db.insert(DBHelper.TB_NEWS, null, values);
			db.replace(DBHelper.TB_NEWS, null, values);
		}

	}

	public List<News> listNews(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<News> list = new ArrayList<News>();
		String sql = "select * from " + DBHelper.TB_NEWS
				+ " order by time desc limit ? offset ?";
		SQLiteDatabase database = null;
		// 实例化对象
		DBHelper DBhelp = new DBHelper(this);
		try {
			database = DBhelp.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				News news = new News();
				String time = cursor.getString(cursor
						.getColumnIndex(DBHelper.TIME));
				if (time == null || "".equals(time) || "null".equals(time)) {
					time = "待定";
				}
				news.setTime(time);
				String title = cursor.getString(cursor
						.getColumnIndex(DBHelper.TITLE));
				if (title == null || "".equals(title) || "null".equals(title)) {
					title = "待定";
				}
				news.setTitle(title);
				int newsid = cursor.getInt(cursor
						.getColumnIndex(DBHelper.NEWSID));
				news.setNewsid(newsid);
				// Log.i(Constants.TAG, title+"---"+time+"---"+newsid);
				list.add(news);
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

	public void showNewsByDB() {
		totallist = listNews(new String[] { "" + Constants.PAGESIZE, "0" });
		if (totallist.size() == 0) {
			Toast.makeText(HomeActivity.this, "当前未保存数据当前未保存数据，正在加载网络数据.....", Toast.LENGTH_LONG)
					.show();
			return;
		}
		adapter.bindData(totallist);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void transferNewslist(List<News> d, List<News> s) {
		for (int i = 0; i < s.size(); i++) {
			d.add(s.get(i));
		}
		s.clear();
	}
}
