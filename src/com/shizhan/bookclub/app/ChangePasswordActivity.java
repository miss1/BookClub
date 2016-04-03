package com.shizhan.bookclub.app;

import com.shizhan.bookclub.app.util.MyProgressBar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener{
	
	private ImageView chgPasswordImh;
	private EditText passwordOld;
	private EditText passwordNew1;
	private EditText passwordNew2;
	private Button chgPasswordOk;
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_changepassword);
		chgPasswordImh = (ImageView) findViewById(R.id.chgpassword_imh);
		chgPasswordOk = (Button) findViewById(R.id.chgpassword_ok);
		passwordOld = (EditText) findViewById(R.id.password_old);
		passwordNew1 = (EditText) findViewById(R.id.password_new1);
		passwordNew2 = (EditText) findViewById(R.id.password_new2);
		
		chgPasswordImh.setOnClickListener(this);
		chgPasswordOk.setOnClickListener(this);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chgpassword_imh:    //点击返回图标，返回上一界面
			finish();
			break;
		case R.id.chgpassword_ok:       //点击确定按钮，修改密码，退出重新回到登陆界面			
			String oldPwd = passwordOld.getText().toString();
			String newPwd = passwordNew1.getText().toString();
			String newPwdr = passwordNew2.getText().toString();
			if(newPwd.equals(newPwdr)){
				progressBar.setVisibility(View.VISIBLE);
				BmobUser.updateCurrentUserPassword(ChangePasswordActivity.this, oldPwd, newPwd, new UpdateListener() {
					
					@Override
					public void onSuccess() {       //密码修改成功，回到登录界面
						BmobUser.logOut(ChangePasswordActivity.this);
						Intent intentl = new Intent(ChangePasswordActivity.this, LoginActivity.class);
						startActivity(intentl);	
						finish();
						MainActivity.instance.finish();    //销毁主界面
						progressBar.setVisibility(View.GONE);
						Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(ChangePasswordActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}else{
				passwordNew1.setText("");
				passwordNew2.setText("");
				Toast.makeText(ChangePasswordActivity.this, "两次输入的新密码不一样", Toast.LENGTH_SHORT).show();
			}			
			break;
		default:
			break;
		}
		
	}
	
	// 上一Activity调用这方法，跳转到本界面
	public static void actionStart(Context context) {
		Intent intent = new Intent(context, ChangePasswordActivity.class);
		context.startActivity(intent);
	}

}
