package com.shizhan.bookclub.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.adapter.CommentsListAdapter;
import com.shizhan.bookclub.app.model.Comment;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewsCommentActivity extends Activity implements OnClickListener{
	
	private Post post;
	
	private ImageView newsCommentImh;
	private ImageView newsCommentImshoucang;
	private EditText newsCommentEdcomment;
	private Button newsCommentBuok;
	
	private ListView newsCommentList;
	private CommentsListAdapter adapter;
	private List<Comment> list = new ArrayList<Comment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_newscomment);
		newsCommentImh = (ImageView) findViewById(R.id.newscomment_imh);
		newsCommentImshoucang = (ImageView) findViewById(R.id.newscomment_imshoucang);
		newsCommentEdcomment = (EditText) findViewById(R.id.newscomment_edcomment);
		newsCommentBuok = (Button) findViewById(R.id.newscomment_buok);
		newsCommentList = (ListView) findViewById(R.id.newscomment_list);
		
		Intent intent = getIntent();
		post = (Post) intent.getSerializableExtra("post");    //���մ��ݹ�����Post�����
		
		//����¼�����
		newsCommentImh.setOnClickListener(this);
		newsCommentImshoucang.setOnClickListener(this);
		newsCommentBuok.setOnClickListener(this);
		
		adapter = new CommentsListAdapter(this, post, list);
		newsCommentList.setAdapter(adapter);
		init();
		
		newsCommentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==adapter.getCount()-1){              //������һ��Itemʱ����������Ϣ
					TextView comment_reflash_tip = (TextView) view.findViewById(R.id.comment_reflash_tip);
					ProgressBar comment_reflash_progressBar1 = (ProgressBar) view.findViewById(R.id.comment_reflash_progressBar1);
					comment_reflash_tip.setText("������...");
					comment_reflash_progressBar1.setVisibility(View.VISIBLE);
					init();
				}
				
			}
		});
	}
	
	//��һActivity�����ⷽ���������ݴ��ݵ�������
	public static void actionStart(Context context, Post post){
		Intent intent = new Intent(context, NewsCommentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("post", post);        //����Post�����
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	//����¼�����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newscomment_imh:      //����
			finish();
			break;
		case R.id.newscomment_imshoucang:       //�ղ�
			MyUsers users = BmobUser.getCurrentUser(NewsCommentActivity.this, MyUsers.class);
			//����ǰ�û���ӵ�Post���е�likes�ֶ�ֵ�У�������ǰ�û�ϲ��������
			BmobRelation relation = new BmobRelation();
			relation.add(users);
			post.setLikes(relation);
			post.update(NewsCommentActivity.this, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(NewsCommentActivity.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();					
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();					
				}
			});
			break;
		case R.id.newscomment_buok:               //��������
			final String content = newsCommentEdcomment.getText().toString();
			if(!TextUtils.isEmpty(content)){             //��������ݲ�Ϊ�գ����������ȡ������ʱ��
				Bmob.getServerTime(this, new GetServerTimeListener() {
					
					@Override
					public void onSuccess(long arg0) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String times = formatter.format(new Date(arg0 * 1000L));              
						//��ȡʱ��ɹ�����Comment�������һ������
						MyUsers user = BmobUser.getCurrentUser(NewsCommentActivity.this, MyUsers.class);
						Comment comment = new Comment();
						comment.setContent(content);
						comment.setTime(times);
						comment.setUser(user);
						comment.setPost(post);
						comment.save(NewsCommentActivity.this, new SaveListener() {
							
							@Override
							public void onSuccess() {
								newsCommentEdcomment.setText("");
								Toast.makeText(NewsCommentActivity.this, "���۷���ɹ�", Toast.LENGTH_SHORT).show();							
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(NewsCommentActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();						
					}
				});
			}else{
				Toast.makeText(NewsCommentActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		
	}
	
	//��ѯ�����ӵ���������
	private void init(){
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("post", new BmobPointer(post));
		//ͬʱ��ѯ�����۵ķ����ߵ���Ϣ���Լ������ӵ����ߵ���Ϣ
		query.include("user");
		query.findObjects(NewsCommentActivity.this, new FindListener<Comment>() {
			
			@Override
			public void onSuccess(List<Comment> arg0) {
				list.clear();
				for(Comment comment : arg0){
					list.add(comment);
				}
				adapter.notifyDataSetChanged();        //���ݸı䣬��̬�����б�
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();				
			}
		});
	}

}
