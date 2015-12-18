package com.shizhan.bookclub.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button login;
	private TextView regist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		login = (Button) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Intent intentm = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intentm);
			break;
		case R.id.regist:
			Intent intentr = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intentr);
			break;
		default:
			break;
		}
		
	}

}
