package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.InfoShowAdapter;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.InformationShow;
import com.shizhan.bookclub.app.model.MyUsers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonerInfoActivity extends Activity implements OnClickListener{
	
	private ImageView personInfoBack;
	private TextView personInfoTalk;
	private TextView personInfoName;
	private ListView personInfoList;
	private Button personInfoAddf;
	
	private MyUsers user;
	private InfoShowAdapter adapter;                    
	private List<InformationShow> infolist = new ArrayList<InformationShow>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personerinfo);
		
		Intent intent = getIntent();
		user = (MyUsers) intent.getSerializableExtra("user");
		
		personInfoBack = (ImageView) findViewById(R.id.personinfo_back);
		personInfoTalk = (TextView) findViewById(R.id.personinfo_talk);
		personInfoName = (TextView) findViewById(R.id.personinfo_name);
		personInfoList = (ListView) findViewById(R.id.personinfo_list);
		personInfoAddf = (Button) findViewById(R.id.personinfo_addf);
		
		personInfoBack.setOnClickListener(this);
		personInfoTalk.setOnClickListener(this);
		personInfoAddf.setOnClickListener(this);
		
		personInfoName.setText(user.getUserId());
		initInfo();
	}
	
	//上一Activity调用这方法，跳转到本界面并将MyUsers数据传递到本界面
	public static void actionStart(Context context, MyUsers user){
		Intent intent = new Intent(context, PersonerInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	//初始化信息
		private void initInfo() {
			BmobQuery<Information> query = new BmobQuery<Information>();
			query.addWhereEqualTo("user", user);
			query.findObjects(PersonerInfoActivity.this, new FindListener<Information>() {
				
				@Override
				public void onSuccess(List<Information> arg0) {
					InformationShow zhanghao = new InformationShow("账        号：", arg0.get(0).getZhanghao());
					infolist.add(zhanghao);
					InformationShow nicheng = new InformationShow("性        别：", arg0.get(0).getSex());
					infolist.add(nicheng);
					InformationShow age = new InformationShow("年        龄：", arg0.get(0).getAge());
					infolist.add(age);
					InformationShow city = new InformationShow("所在城市：", arg0.get(0).getCity());
					infolist.add(city);
					InformationShow geqian = new InformationShow("个性签名：", arg0.get(0).getGeqian());
					infolist.add(geqian);
					InformationShow lovebook = new InformationShow("喜欢的书：", arg0.get(0).getLovebook());
					infolist.add(lovebook);
					InformationShow loveauthor = new InformationShow("喜欢作家：", arg0.get(0).getLoveauthor());
					infolist.add(loveauthor);
					InformationShow bookstyle = new InformationShow("看书类型：", arg0.get(0).getBookstyle());
					infolist.add(bookstyle);
					adapter = new InfoShowAdapter(PersonerInfoActivity.this, infolist);
					personInfoList.setAdapter(adapter);
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					adapter = new InfoShowAdapter(PersonerInfoActivity.this, infolist);
					personInfoList.setAdapter(adapter);
					Toast.makeText(PersonerInfoActivity.this, arg1, Toast.LENGTH_LONG).show();
				}
			});
			
		}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personinfo_back:
			finish();
			break;
		case R.id.personinfo_talk:
			PersonPostActivity.actionStart(PersonerInfoActivity.this, user);
			break;
		case R.id.personinfo_addf:
			
			break;
		default:
			break;
		}
		
	}
}
