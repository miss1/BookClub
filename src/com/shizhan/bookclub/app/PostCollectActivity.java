package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.FindAllAdapter;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class PostCollectActivity extends BaseActivity implements OnClickListener{
	
	private ImageView infoTalkImh;
	private TextView infoTalkTv;
	private ListView infoTalkList;
	
	private FindAllAdapter adapter;
	private List<Post> cpost = new ArrayList<Post>();
	
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
		
		infoTalkTv.setText("我的收藏");
		infoTalkImh.setOnClickListener(this);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		progressBar.setVisibility(View.VISIBLE);
		
		// 查询当前用户收藏的所有帖子
		BmobQuery<Post> queryp = new BmobQuery<Post>();
		MyUsers userp = BmobUser.getCurrentUser(PostCollectActivity.this,
				MyUsers.class);
		queryp.addWhereRelatedTo("likes", new BmobPointer(userp));
		queryp.include("user");
		queryp.findObjects(PostCollectActivity.this, new FindListener<Post>() {

			@Override
			public void onSuccess(List<Post> arg0) {
				for(Post post : arg0){
					cpost.add(post);
				}
				adapter = new FindAllAdapter(PostCollectActivity.this, cpost);
				infoTalkList.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
				listItemClick();
			}

			@Override
			public void onError(int arg0, String arg1) {
				adapter = new FindAllAdapter(PostCollectActivity.this, cpost);
				Toast.makeText(PostCollectActivity.this, arg1, Toast.LENGTH_SHORT).show();
				infoTalkList.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		finish();
		
	}
	
	// ListView子项点击事件
	private void listItemClick() {
		infoTalkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (cpost.size() != 0) {
					Post post = cpost.get(position);
					PostCommentActivity.actionStart(PostCollectActivity.this,
							post);
				}

			}
		});
	}
	
	// 上一Activity调用这方法，跳转到本界面
	public static void actionStart(Context context){
		Intent intent = new Intent(context, PostCollectActivity.class);
		context.startActivity(intent);
	}

}
