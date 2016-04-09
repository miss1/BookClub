package com.shizhan.bookclub.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;

import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class WritePostActivity extends BaseActivity implements OnClickListener{
	
	private ImageView writeImageh;
	private Button writeButtonOk;
	private EditText writeEdHeade;
	private EditText wruteEdContent;
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_writefeeling);
		writeImageh = (ImageView) findViewById(R.id.write_imh);
		writeButtonOk = (Button) findViewById(R.id.write_buttonok);
		writeEdHeade = (EditText) findViewById(R.id.write_edhead);
		wruteEdContent = (EditText) findViewById(R.id.write_edcontent);
		writeImageh.setOnClickListener(this);
		writeButtonOk.setOnClickListener(this);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.write_imh:
			finish();
			break;
		case R.id.write_buttonok:
			final String title = writeEdHeade.getText().toString();
			final String content = wruteEdContent.getText().toString();
			if(!content.equals("")){                //发表的内容不为空，则接下来获取服务器时间
				progressBar.setVisibility(View.VISIBLE);
				Bmob.getServerTime(this, new GetServerTimeListener() {
					
					@Override
					public void onSuccess(long arg0) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String times = formatter.format(new Date(arg0 * 1000L));         //获取时间成功，往Post表中添加一条内容
						MyUsers user = BmobUser.getCurrentUser(WritePostActivity.this, MyUsers.class);
						//创建帖子信息
						Post post = new Post();
						if(!title.equals("")){
							post.setTitle(title);
						}
						post.setContent(content);
						post.setTime(times);
						post.setUser(user);
						
						post.save(WritePostActivity.this, new SaveListener() {
							
							@Override
							public void onSuccess() {
								Toast.makeText(WritePostActivity.this, "帖子发表成功", Toast.LENGTH_SHORT).show();
								progressBar.setVisibility(View.GONE);
								finish();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(WritePostActivity.this, arg1, Toast.LENGTH_SHORT).show();
								progressBar.setVisibility(View.GONE);
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(WritePostActivity.this, arg1, Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.GONE);
					}
				});
			}else{
				Toast.makeText(WritePostActivity.this, "帖子内容不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		
	}

}
