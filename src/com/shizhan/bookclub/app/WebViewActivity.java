package com.shizhan.bookclub.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	
	private WebView webView;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		webView = (WebView) findViewById(R.id.webView);
		
		Intent intentr = getIntent();
		url = intentr.getStringExtra("url");
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(0);
		WebSettings websettings = webView.getSettings();
		websettings.setAllowFileAccess(true);
		websettings.setBuiltInZoomControls(true);
		webView.loadUrl(url);
		//加载数据  
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(newProgress == 100){
					WebViewActivity.this.setTitle("加载完成");
				}else{
					WebViewActivity.this.setTitle("加载中。。。");
				}
			}
		});
		//这个是当网页上的连接被点击的时候 
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
	}

	// goBack()表示返回webView的上一页面  
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK){
			webView.goBack();
			return true;
		}
		finish();
		return false;
	}
	
	public static void actionStart(Context context, String url){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
}
