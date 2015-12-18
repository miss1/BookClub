package com.shizhan.bookclub.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class RegistActivity extends Activity implements OnClickListener{
	
	private EditText rgnicheng;
	private EditText rgmima;
	private EditText rgmimas;
	
	private Button rgok;
	private Button rgback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		inits();
	}

	//获取各控件的实例对象及设置事件监听
	private void inits() {
		rgnicheng = (EditText) findViewById(R.id.rg_nicheng);
		rgmima = (EditText) findViewById(R.id.rg_mima);
		rgmimas = (EditText) findViewById(R.id.rg_mimas);
		rgok = (Button) findViewById(R.id.rg_ok);
		rgback = (Button) findViewById(R.id.rg_back);
		rgok.setOnClickListener(this);
		rgback.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rg_ok:
			String nicheng = rgnicheng.getText().toString();
			String mima = rgmima.getText().toString();
			String mimas = rgmimas.getText().toString();
			BmobUser user = new BmobUser();
            if(!(mima.equals(mimas))){
				Toast.makeText(RegistActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
				rgmima.setText("");
				rgmimas.setText("");
				return;
			}else{
				user.setUsername(nicheng);
				user.setPassword(mima);
				user.signUp(RegistActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RegistActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(RegistActivity.this, "注册失败" + arg1, Toast.LENGTH_SHORT).show();						
					}
				});
			}
			break;
		case R.id.rg_back:
			finish();
			break;
		default:
			break;
		}
		
	}

}
