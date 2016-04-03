package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import com.shizhan.bookclub.app.adapter.FindAllAdapter;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.util.MyProgressBar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PersonPostActivity extends Activity implements OnClickListener{
	
	private ImageView infoTalkImh;
	private TextView infoTalkTv;
	private ListView infoTalkList;
	
	private FindAllAdapter adapter;
	private List<Post> inpost = new ArrayList<Post>();
	
	private MyUsers user;
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infotalk);
		infoTalkImh = (ImageView) findViewById(R.id.infotalk_imh);
		infoTalkTv = (TextView) findViewById(R.id.infotalk_tv);
		infoTalkList = (ListView) findViewById(R.id.infotalk_list);
		
		Intent intent = getIntent();
		user = (MyUsers) intent.getSerializableExtra("user");
		
		infoTalkTv.setText(user.getUserId());
		infoTalkImh.setOnClickListener(this);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		progressBar.setVisibility(View.VISIBLE);
		
		BmobQuery<Post> query = new BmobQuery<Post>();
		query.addWhereEqualTo("user", user);         //查询当前用户的所有帖子
		query.order("-updatedAt");
		query.include("user");            // 希望在查询帖子信息的同时也把发布人的信息查询出来
		query.findObjects(PersonPostActivity.this, new FindListener<Post>() {
			
			@Override
			public void onSuccess(List<Post> arg0) {
				for(Post post:arg0){
					inpost.add(post);
				}
				adapter = new FindAllAdapter(PersonPostActivity.this, inpost);
				infoTalkList.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
				listItemClick();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				adapter = new FindAllAdapter(PersonPostActivity.this, inpost);
				Toast.makeText(PersonPostActivity.this, arg1, Toast.LENGTH_SHORT).show();
				infoTalkList.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
			}
		});
		
		
		
	}
	
	// 上一Activity调用这方法，跳转到本界面并将MyUsers数据传递到本界面
	public static void actionStart(Context context, MyUsers user) {
		Intent intent = new Intent(context, PersonPostActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
	
	//ListView子项点击事件
	private void listItemClick(){
		infoTalkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(inpost.size()!=0){
					Post post = inpost.get(position);
					PostCommentActivity.actionStart(PersonPostActivity.this, post);
				}
				
			}
		});
	}

}
