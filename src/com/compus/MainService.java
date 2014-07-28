package com.compus;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.compus.db.DBHelper;
import com.compus.domain.Schedule;
import com.compus.tools.SPUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MainService extends Service {

	String kxStr = "2014-3-3";
	Date kxDate;
	String wuStr = "2014-5-1";
	Date wuDate;
	String shiStr = "2014-10-1";
	Date shiDate;
	SPUtil sp;
	int morningtime = 8*60;
	int afternoontime = 13*60+30;
	int wantime = 18*60+30;
	Handler qjHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			AudioManager audio = (AudioManager)MainService.this.getSystemService(Context.AUDIO_SERVICE);
			sp = new SPUtil(MainService.this);
			switch (msg.what) {
			case 1:
				//Toast.makeText(MainService.this, "1111111111111", 1).show();;
//				Log.i(Constants.TAG, 1+afternoontime
//						+ "-time--service");
				noRingAndVibrate(audio);
				
				super.handleMessage(msg);
				break;
			case 0:
				//Toast.makeText(MainService.this, "00000000000", 1).show();;
//				Log.i(Constants.TAG, afternoontime
//						+ "-time0--service---schedule--:"+sp.getValue("schedule", true));
				ringAndVibrate(audio);
				super.handleMessage(msg);
				break;
			case 2:
				//Toast.makeText(MainService.this, "00000000000", 1).show();;
//				Log.i(Constants.TAG,  "-stop--service");
				ringAndVibrate(audio);
				stopSelf();
				super.handleMessage(msg);
				break;

			default:
				break;
			}

		}

	};
	//无声无振动
	 void noRingAndVibrate(AudioManager audio) {
	        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
	                AudioManager.VIBRATE_SETTING_OFF);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
	                AudioManager.VIBRATE_SETTING_OFF);
	        //Toast.makeText(this, "设置成功！当前为无声无振动", Toast.LENGTH_LONG).show();
	    }
//只声音，无振动：
	    void ring(AudioManager audio) {
	        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
	                AudioManager.VIBRATE_SETTING_OFF);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
	                AudioManager.VIBRATE_SETTING_OFF);
	       // Toast.makeText(this, "设置成功！当前为铃声", Toast.LENGTH_LONG).show();
	    }
	    //即有声音也有振动
	    void ringAndVibrate(AudioManager audio) {
	        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
	                AudioManager.VIBRATE_SETTING_ON);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
	                AudioManager.VIBRATE_SETTING_ON);
	       // Toast.makeText(this, "设置成功！当前为铃声加振动", Toast.LENGTH_LONG).show();
	    }
	    //只能振动
	    void vibrate(AudioManager audio) {
	        audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
	                AudioManager.VIBRATE_SETTING_ON);
	        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
	                AudioManager.VIBRATE_SETTING_ON);
	        //Toast.makeText(this, "设置成功！当前为振动", Toast.LENGTH_LONG).show();
	    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sp = new SPUtil(this);//Log.i(Constants.TAG, "---schedule------"+sp.getValue("schedule", true));
		
		try {
			kxDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(kxStr
					+ " 00:00:00");
			wuDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(wuStr
					+ " 00:00:00");
			shiDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(shiStr
					+ " 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new QJThread()).start();
		return super.onStartCommand(intent, flags, startId);
	}

	public class QJThread implements Runnable {

		@Override
		public void run() {
           
            List<Schedule> list = listSchedule(null);
            while(true) {
            	if(!sp.getValue("schedule", true)) {
            		qjHandler.sendEmptyMessage(2);
            		break;
            	}
            	//取得夏秋两季的作息时间
            	if(System.currentTimeMillis()>wuDate.getTime() && System.currentTimeMillis()<shiDate.getTime()) {
            		morningtime = 8*60;
            		afternoontime = 14*60;
            		wantime = 19*60;
            	}else {
            		morningtime = 8*60;
            		afternoontime = 13*60+30;
            		wantime = 18*60+30;
            	}
    			long currentTime = (System.currentTimeMillis()-kxDate.getTime())/1000;
    			int currentWeek = (int) (currentTime/(3600*24*7)+1);
    			int currentDay = (int) ((currentTime/(3600*24)+1)%7);
    			int currentHour = (int)( (currentTime/3600)%24);
    			int currentMinute = (int) ((currentTime/60)%60);
    			if(currentDay==0) 	//若今天是星期天
    				currentDay=7;
    			//Log.i(Constants.TAG, currentMinute+"currentMinute"+currentWeek+ currentDay+"currentDay"+"currentWeek"+"-currentWeek--service"+currentHour+"-currentHour--service");

    			boolean isClassing = false;
            	for(int i=0; i<list.size(); i++){
            		if(list.get(i).getJc()!=null&&list.get(i).getJc().trim().length()>0) {
							int beginweeek = Integer.parseInt(list.get(i).getBeginweeek());
            			int endweek = Integer.parseInt(list.get(i).getEndweek());
            			
            			if(currentWeek>=beginweeek && currentWeek<=endweek){
            				
            				if("full".equals(list.get(i).getType())){
            					isClassing = isClassing(list.get(i), currentDay, currentHour, currentMinute);
            				}
            				else if(currentWeek%2 == 0 && "even".equals(list.get(i).getType())) {
            					isClassing = isClassing(list.get(i), currentDay, currentHour, currentMinute);
                			}
                			else if(currentWeek%2 == 1 && "odd".equals(list.get(i).getType())) {
                				isClassing = isClassing(list.get(i), currentDay, currentHour, currentMinute);
                			}
            			}
            			
            		}
            		if(isClassing) {
//            			Log.i(Constants.TAG, list.get(i).getJc()+list.get(i).getName()+sp.getValue("schedule", true)
//        						+ "-time0--service");
			            qjHandler.sendEmptyMessage(1);
			            break;
            		}
            	}
            	if(!isClassing) {
		            qjHandler.sendEmptyMessage(0);
        		}
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		}
		public boolean isClassing(Schedule schedule, int currentDay, int currentHour, int currentMinute) {
			
			int day = Integer.parseInt(schedule.getDay());
			if(currentDay!=day)
				return false;
			else{
//				Log.i(Constants.TAG,"--name  "+ schedule.getName()+ "--Jc  "+ schedule.getJc()+"Type   "+ schedule.getType()+
//						"afternoontime  "+ afternoontime+"current  "+ (currentHour*60+currentMinute));
				if ("一".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					if((currentHour*60+currentMinute)>=morningtime && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}

				if ("二".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					morningtime=morningtime+120;
					if((currentHour*60+currentMinute)>=(morningtime) && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}
//				Log.i(Constants.TAG, "---三"+ schedule.getJc()+"----");
				if ("三".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					morningtime = afternoontime;
//					Log.i(Constants.TAG, "Jc  "+ schedule.getJc()+"Type   "+ schedule.getType()+
//						"morningtime  "+ morningtime+"current  "+ (currentHour*60+currentMinute));
					if((currentHour*60+currentMinute)>=morningtime && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}

				if ("四".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					morningtime = afternoontime+120;
					if((currentHour*60+currentMinute)>=morningtime && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}

				if ("五".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					morningtime = wantime;
					if((currentHour*60+currentMinute)>=morningtime && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}
				
				if ("六".equals(schedule.getJc()) && sp.getValue(currentDay+schedule.getJc(), true)) {
					morningtime = wantime+110;
					if((currentHour*60+currentMinute)>=morningtime && (currentHour*60+currentMinute)<=(morningtime+100)){
						return true;
					}
				}

			}
			return false;
		}
	}
	
	public List<Schedule> listSchedule(String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Schedule> list = new ArrayList<Schedule>();
		String sql = "select * from " + DBHelper.TB_SCHEDULE;
		SQLiteDatabase database = null;
		// 实例化对象
		DBHelper DBhelp = new DBHelper(MainService.this);
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

}
