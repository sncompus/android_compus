package com.compus.db;

import com.compus.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "m.db";
	private static int VERSION = 1;
	public final static String TITLE = "title";
	public final static String TIME = "time";
	public final static String NEWSID = "newsid";
	public final static String TB_NEWS = "news";
	public final static String NEARID = "nearid";
	public final static String MINEID = "mineid";
	public final static String TOP = "top";
	public final static String TB_NEAR = "near";
	public final static String TB_MINE = "mine";
	public final static String TB_SCHEDULE = "schedule";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlnews = "create table "
				+ TB_NEWS
				+ " (newsid integer primary key autoincrement,title varchar(64),time varchar(64))";
		db.execSQL(sqlnews);
		String sqlnear = "create table "
				+ TB_NEAR
				+ " (nearid integer primary key autoincrement,title varchar(64),time varchar(64),top varchar(1))";
		db.execSQL(sqlnear);
		String sqlmine = "create table "
				+ TB_MINE
				+ " (mineid integer primary key autoincrement,title varchar(64),time varchar(64))";
		db.execSQL(sqlmine);
		String sqlschedule = "create table "
				+ TB_SCHEDULE
				+ " (_id integer primary key autoincrement,scheduleid varchar(64),name varchar(64),credit varchar(64),attrubute varchar(64),exam varchar(64),teacher varchar(64),beginweeek varchar(64),endweek varchar(64),type varchar(64),day varchar(64),jc varchar(64),compus varchar(64),building varchar(64),room varchar(64))";
		db.execSQL(sqlschedule);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.i(Constants.TAG, "update---schedule----");
	}

}
