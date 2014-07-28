package com.compus.domain;


public class Near {

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private int nearid; 
	
	private String userid;
	
	private String username;

	private String title;
	
	private String content;
	
	private String time;
	
	private boolean is_read = false;
	
	private String top = "0";
	
	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public boolean isIs_read() {
		return is_read;
	}

	public void setIs_read(boolean is_read) {
		this.is_read = is_read;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNearid() {
		return nearid;
	}

	public void setNearid(int nearid) {
		this.nearid = nearid;
	}

}
