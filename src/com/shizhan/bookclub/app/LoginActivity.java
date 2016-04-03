package com.shizhan.bookclub.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;

import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button login;
	private TextView regist;
	private EditText username;
	private EditText password;
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			progressBar.setVisibility(View.VISIBLE);
			String lusername = username.getText().toString();
			String lpassword = password.getText().toString();
			MyUsers use = new MyUsers();
			use.setUsername(lusername);
			use.setPassword(lpassword);
			use.login(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(LoginActivity.this, "µÇÂ½³É¹¦", Toast.LENGTH_SHORT).show();					
					Intent intentl = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intentl);
					progressBar.setVisibility(View.GONE);
					finish();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					progressBar.setVisibility(View.GONE);
					Toast.makeText(LoginActivity.this, "µÇÂ½Ê§°Ü" + arg1, Toast.LENGTH_SHORT).show();				
				}
			});
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
