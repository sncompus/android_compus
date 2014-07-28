package com.compus.app;

import org.apache.http.client.CookieStore;

import com.baidu.frontia.FrontiaApplication;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class CompusApplication extends FrontiaApplication {

	private CookieStore cookies;   
	public int localVersion = 0;// 本地安装版本

	public static String downloadDir = "compus";
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.getInstance().init(this);
		try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}
    public CookieStore getCookie(){    
        return cookies;
    }
    public void setCookie(CookieStore cks){
        cookies = cks;
    }
    
}
