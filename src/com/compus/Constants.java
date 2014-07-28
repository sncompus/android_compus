package com.compus;

public interface Constants {

//	public final static String URL_IP = "http://10.17.13.19:8080/compus/";
	public final static String URL_IP = "http://192.168.1.105:8080/compus/";

	public final static int PAGESIZE = 12;

	public final static String URL_NEWS = URL_IP
			+ "servlet/ListNewsServlet?pageSize=" + PAGESIZE;

	public final static String URL_NEWS_CONTENT = URL_IP
			+ "servlet/ShowNewsServlet";

	public final static String URL_NEAR = URL_IP
			+ "servlet/ListNearServlet?pageSize=" + PAGESIZE;

	public final static String URL_NEAR_CONTENT = URL_IP
			+ "servlet/ShowNearServlet";
	
	public final static String URL_MINE = URL_IP
			+ "servlet/ListMineServlet?pageSize=" + PAGESIZE;

	public final static String URL_MINE_CONTENT = URL_IP
			+ "servlet/ShowMineServlet";

	public final static String URL_LOGIN = URL_IP + "servlet/LoginServet";

	public final static String URL_VERSION = URL_IP + "android/detail.txt";
	
	public final static String URL_SCHEDULE = URL_IP + "schedule.jsp";

	public final static String ENCODE = "utf-8";

	public final static String TAG = "compus";

	public final static String APP_NAME = "��ũ�ƶ�У԰";
}
