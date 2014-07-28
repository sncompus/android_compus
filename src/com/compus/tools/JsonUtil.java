package com.compus.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.util.Log;

import com.compus.Constants;
import com.compus.domain.Mine;
import com.compus.domain.Near;
import com.compus.domain.News;
import com.compus.domain.Schedule;

public class JsonUtil {

	public static List<News> parseNewsJsonStr(String jsonStr) {
		List<News> list = new ArrayList<News>();
		try {
			JSONArray ja = new JSONArray(jsonStr);
			for(int i=0; i<ja.length(); i++) {
				News news = new News();
				news.setTitle(ja.getJSONObject(i).getString("title"));
				news.setNewsid(ja.getJSONObject(i).getInt("newsid"));
				news.setTime(ja.getJSONObject(i).getString("time"));
				news.setUsername(ja.getJSONObject(i).getString("name"));
				news.setUserid(ja.getJSONObject(i).getString("userid"));
				list.add(news);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static List<Near> parseNearJsonStr(String jsonStr) {
		List<Near> list = new ArrayList<Near>();
		try {
			JSONArray ja = new JSONArray(jsonStr);
			for(int i=0; i<ja.length(); i++) {
				Near near = new Near();
				near.setTitle(ja.getJSONObject(i).getString("title"));
				near.setNearid(ja.getJSONObject(i).getInt("nearid"));
				near.setTime(ja.getJSONObject(i).getString("time"));
				near.setUsername(ja.getJSONObject(i).getString("name"));
				near.setUserid(ja.getJSONObject(i).getString("userid"));
				near.setTop(ja.getJSONObject(i).getString("top"));
				list.add(near);
			}
		} catch (Exception e) {
			Log.i(Constants.TAG, "½âÎöÊ§°Ü£¬nearlist--- "+list.size());
		}
		return list;
	}
	
	public static List<Mine> parseMineJsonStr(String jsonStr) {
		List<Mine> list = new ArrayList<Mine>();
		try {
			JSONArray ja = new JSONArray(jsonStr);
			for(int i=0; i<ja.length(); i++) {
				Mine mine = new Mine();
				mine.setTitle(ja.getJSONObject(i).getString("title"));
				mine.setMineid(ja.getJSONObject(i).getInt("mineid"));
				mine.setTime(ja.getJSONObject(i).getString("time"));
				list.add(mine);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static List<Schedule> parseScheduleJsonStr(String jsonStr) {
		List<Schedule> list = new ArrayList<Schedule>();
		try {
			JSONArray ja = new JSONArray(jsonStr);
			for(int i=0; i<ja.length(); i++) {
				Schedule schedule = new Schedule();
				schedule.setAttrubute(ja.getJSONObject(i).getString("attrubute"));
				schedule.setBeginweeek(ja.getJSONObject(i).getString("beginweeek"));
				schedule.setBuilding(ja.getJSONObject(i).getString("building"));
				schedule.setCompus(ja.getJSONObject(i).getString("compus"));
				schedule.setCredit(ja.getJSONObject(i).getString("credit"));
				schedule.setDay(ja.getJSONObject(i).getString("day"));
				schedule.setEndweek(ja.getJSONObject(i).getString("endweek"));
				schedule.setExam(ja.getJSONObject(i).getString("exam"));
				schedule.setJc(ja.getJSONObject(i).getString("jc"));
				schedule.setName(ja.getJSONObject(i).getString("name"));
				schedule.setRoom(ja.getJSONObject(i).getString("room"));
				schedule.setScheduleid(ja.getJSONObject(i).getString("scheduleid"));
				schedule.setTeacher(ja.getJSONObject(i).getString("teacher"));
				schedule.setType(ja.getJSONObject(i).getString("type"));
				list.add(schedule);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
}
