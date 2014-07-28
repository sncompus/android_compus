package com.compus.near;

import com.compus.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

	public class NearWebActivity extends Activity {
		private WebView webview;
		ProgressDialog progresDialog=null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			Intent intent = getIntent();
			String newsurl = intent.getStringExtra("nearurl");
			progresDialog = new ProgressDialog(this);
	        progresDialog.setMessage("页面请求.......");

	        progresDialog.show();

			// 实例化WebView对象
			webview = new WebView(this);
			WebSettings webSettings= webview.getSettings();
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			// 加载需要显示的网页
			//webview.loadUrl("http://www.51cto.com/");
			webview.loadUrl(newsurl);
			//webview.loadUrl("file:///android_asset/demo.html");
			// 设置Web视图
			webview.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if(url.indexOf("tel:") == 0) {
						//view.loadUrl(url);
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
	                    startActivity(intent);
									//Toast.makeText(MainActivity.this, url+" test", Toast.LENGTH_LONG).show();
	                    return true;
					}
					return super.shouldOverrideUrlLoading(view, url);
				}			
			});
			webview.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					//if(using.equals("webviewCache"))
					setTitle("页面加载中，请稍候..." + newProgress + "%");

			        setProgress(newProgress * 100);

			 

			        if (newProgress == 100) {

			            setTitle(R.string.app_name);

			        }


					if (newProgress == 100) { // 这里是设置activity的标题，
						// 也可以根据自己的需求做一些其他的操作

		                    progresDialog.setMessage("加载完成");

		                    progresDialog.cancel();

		                } else {

		                    progresDialog.setMessage("正在加载.......");

		                }
				}
				
			});
			//webview.getSettings().setSupportZoom(true);

			//webview.getSettings().setBuiltInZoomControls(true);
			
			
			setContentView(webview);
		}

		// 设置回退
		// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack() ) {
				
				webview.goBack(); // goBack()表示返回WebView的上一页面
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	
	}
