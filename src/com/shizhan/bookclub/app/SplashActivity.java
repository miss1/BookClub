package com.shizhan.bookclub.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import cn.bmob.v3.BmobUser;

public class SplashActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if(bmobUser!=null){
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}, 3000);
		}
		
	}

}
