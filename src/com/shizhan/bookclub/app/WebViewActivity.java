package com.shizhan.bookclub.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class WebViewActivity extends BaseActivity {
	
	private WebView webView;
	private String url;
	
	private ImageView back;
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		webView = (WebView) findViewById(R.id.webView);
		back = (ImageView) findViewById(R.id.webview_back);
		
		Intent intentr = getIntent();
		url = intentr.getStringExtra("url");
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		progressBar.setVisibility(View.VISIBLE);
		
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
					progressBar.setVisibility(View.GONE);
				}else{
					progressBar.setVisibility(View.VISIBLE);
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
		
		//点击返回主界面
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();			
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
