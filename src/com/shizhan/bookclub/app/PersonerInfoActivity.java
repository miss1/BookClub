package com.shizhan.bookclub.app;

import com.shizhan.bookclub.app.model.MyUsers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class PersonerInfoActivity extends Activity {
	
	private MyUsers user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personerinfo);
		
		Intent intent = getIntent();
		user = (MyUsers) intent.getSerializableExtra("user");
		
		System.out.println("user:" + user.getUserId());
	}
	
	//上一Activity调用这方法，跳转到本界面并将MyUsers数据传递到本界面
	public static void actionStart(Context context, MyUsers user){
		Intent intent = new Intent(context, PersonerInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
