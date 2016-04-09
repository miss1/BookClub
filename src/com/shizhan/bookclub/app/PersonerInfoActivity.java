package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.shizhan.bookclub.app.adapter.InfoShowAdapter;
import com.shizhan.bookclub.app.base.ActivityCollector;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.InformationShow;
import com.shizhan.bookclub.app.model.Message;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.ImageHeade;
import com.shizhan.bookclub.app.util.MyDialog;
import com.shizhan.bookclub.app.util.MyProgressBar;
import com.shizhan.bookclub.app.util.MyDialog.Dialogcallback;

public class PersonerInfoActivity extends BaseActivity implements OnClickListener{
	
	private ImageView personInfoHead;
	private ImageView personInfoBack;
	private TextView personInfoTalk;
	private TextView personInfoName;
	private ListView personInfoList;
	private Button personInfosendM;
	
	private MyUsers user;
	private InfoShowAdapter adapter;                    
	private List<InformationShow> infolist = new ArrayList<InformationShow>();
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personerinfo);
		
		Intent intent = getIntent();
		user = (MyUsers) intent.getSerializableExtra("user");
		
		personInfoHead = (ImageView) findViewById(R.id.personinfo_head);
		personInfoBack = (ImageView) findViewById(R.id.personinfo_back);
		personInfoTalk = (TextView) findViewById(R.id.personinfo_talk);
		personInfoName = (TextView) findViewById(R.id.personinfo_name);
		personInfoList = (ListView) findViewById(R.id.personinfo_list);
		personInfosendM = (Button) findViewById(R.id.personinfo_sendM);
		
		personInfoBack.setOnClickListener(this);
		personInfoTalk.setOnClickListener(this);
		personInfosendM.setOnClickListener(this);
		
		personInfoName.setText(user.getUserId());
		
		if(user.getImageUrl() != null){
			ImageHeade imageHead = new ImageHeade(user.getImageUrl(), personInfoHead);
			imageHead.setImageHead();
		}else{
			personInfoHead.setImageResource(R.drawable.head);
		}
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		
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
			progressBar.setVisibility(View.VISIBLE);
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
					progressBar.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					adapter = new InfoShowAdapter(PersonerInfoActivity.this, infolist);
					personInfoList.setAdapter(adapter);
					Toast.makeText(PersonerInfoActivity.this, arg1, Toast.LENGTH_LONG).show();
					progressBar.setVisibility(View.GONE);
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
		case R.id.personinfo_sendM:
			sendMessage();
			break;
		default:
			break;
		}
		
	}

	private void sendMessage() {
		MyDialog myDialog = new MyDialog(PersonerInfoActivity.this);
		myDialog.setDialogCallback(dialogcallback);
		myDialog.show();
	}
	
	//设置mydialog需要处理的事情 
	Dialogcallback dialogcallback = new Dialogcallback() {
		
		@Override
		public void dialogdo(String string) {
			if(TextUtils.isEmpty(string)){
				Toast.makeText(PersonerInfoActivity.this, "留言内容不能为空", Toast.LENGTH_SHORT).show();
			}else{
				progressBar.setVisibility(View.VISIBLE);
				MyUsers fuser = BmobUser.getCurrentUser(PersonerInfoActivity.this, MyUsers.class);
				Message message = new Message();
				message.setFromuser(fuser);
				message.setContent(string);
				message.setTouser(user);
				message.save(PersonerInfoActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(PersonerInfoActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
						ActivityCollector.finishAll();
						progressBar.setVisibility(View.GONE);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(PersonerInfoActivity.this, arg1, Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.GONE);
					}
				});
			}
		}
	};
}
