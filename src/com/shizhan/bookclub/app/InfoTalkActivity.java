package com.shizhan.bookclub.app;

import com.shizhan.bookclub.app.model.MyUsers;

import cn.bmob.v3.BmobUser;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InfoTalkActivity extends Activity implements OnClickListener{
	
	private ImageView infoTalkImh;
	private TextView infoTalkTv;
	private ListView infoTalkList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infotalk);
		infoTalkImh = (ImageView) findViewById(R.id.infotalk_imh);
		infoTalkTv = (TextView) findViewById(R.id.infotalk_tv);
		infoTalkList = (ListView) findViewById(R.id.infotalk_list);
		
		infoTalkTv.setText(BmobUser.getCurrentUser(InfoTalkActivity.this,MyUsers.class).getUserId());
		infoTalkImh.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
