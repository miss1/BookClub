package com.shizhan.bookclub.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

public class RegistActivity extends Activity {
	
	private EditText rgzhanghao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
	}

}