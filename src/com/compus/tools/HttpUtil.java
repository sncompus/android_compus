package com.compus.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.compus.Constants;
import com.compus.app.CompusApplication;
import com.compus.domain.UpdataInfo;

public class HttpUtil {

	CompusApplication appCookie = null;
	static String JSESSIONID = null;
	private static final int TIMEOUT = 3 * 1000;

	public static String sendPostStr(String url, CompusApplication appCookie) {
		String result = "";
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(),
						Constants.ENCODE);
			}
		} catch (Exception e) {
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return result;
	}

	public static String login(String url, List<NameValuePair> params,
			CompusApplication appCookie) {
		String ret = "none";
		url = Constants.URL_LOGIN;
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		HttpEntity httpEntity;
		HttpResponse httpResponse;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("UserName", "14514008"));
		pairs.add(new BasicNameValuePair("Password", "1"));
		try {
			httpPost = new HttpPost(url);
			httpEntity = new UrlEncodedFormEntity(pairs, Constants.ENCODE);
			httpPost.setEntity(httpEntity);
			// ��һ��һ���ǻ�δ����ֵ������ֵ��SessionId����������
			if (null != JSESSIONID) {
				httpPost.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
			}
			httpClient = new DefaultHttpClient();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				ret = EntityUtils.toString(entity);
				CookieStore mCookieStore = httpClient.getCookieStore();
				appCookie.setCookie(mCookieStore);
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					// �����Ƕ�ȡCookie['PHPSESSID']��ֵ���ھ�̬�����У���֤ÿ�ζ���ͬһ��ֵ
					if ("JSESSIONID".equals(cookies.get(i).getName())) {
						JSESSIONID = cookies.get(i).getValue();
						break;
					}
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String sendPostParamsStr(String url, List<NameValuePair> pairs) {
		String result = "";
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			HttpPost post = new HttpPost(url);
			if (pairs != null) {
				HttpEntity httpEntity;
				httpEntity = new UrlEncodedFormEntity(pairs, Constants.ENCODE);
				post.setEntity(httpEntity);
			}
			post.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(),
						Constants.ENCODE);
			}
		} catch (Exception e) {
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return result;
	}

	public void downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;
		int totalSize;
		int downloadCount = 0;
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			totalSize += readsize;

			if (downloadCount == 0
					|| (totalSize * 100 / totalSize - down_step) > downloadCount) {

			}

		}

	}

	public UpdataInfo getJsonVersion(String baseUrl) throws Exception {
		UpdataInfo info = new UpdataInfo();
		List<String> list = new ArrayList<String>();
		URL url = new URL(baseUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			byte[] data = outStream.toByteArray();
			String json = new String(data);
			/*
			 * ��������Ϊ200ʱ�������� �õ��������˷���json���ݣ���������
			 */

			Log.i("cat", ">>>>>>" + json);
			/**
			 * ������Ҫ�����������ش���json��ʽ���ݣ�
			 */
			JSONObject jsonObject = new JSONObject(json);
			// int total = Integer.parseInt(jsonObject.getString("0"));
			info.setDescription(jsonObject.getString("description"));
			info.setUrl(jsonObject.getString("url"));
			info.setVersionName(jsonObject.getString("versionName"));
			info.setVersionCode(Integer.parseInt(jsonObject
					.getString("versionCode")));

		}

		return info;
	}

}
