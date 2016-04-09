package com.shizhan.bookclub.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.adapter.CommentsListAdapter;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.Comment;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class PostCommentActivity extends BaseActivity implements OnClickListener{
	
	private Post post;
	
	private ImageView newsCommentImh;
	private ImageView newsCommentImshoucang;
	private EditText newsCommentEdcomment;
	private Button newsCommentBuok;
	
	private ListView newsCommentList;
	private CommentsListAdapter adapter;
	private List<Comment> list = new ArrayList<Comment>();
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	private Boolean isShoucang = false;
	
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
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		
		init();
		
		//��ѯ��ǰ�û��ղص���������,�жϵ�ǰ�����Ƿ��ղ�
		BmobQuery<Post> queryp = new BmobQuery<Post>();
		MyUsers userp = BmobUser.getCurrentUser(PostCommentActivity.this, MyUsers.class);
		queryp.addWhereRelatedTo("likes", new BmobPointer(userp));
		queryp.findObjects(PostCommentActivity.this, new FindListener<Post>() {
			
			@Override
			public void onSuccess(List<Post> arg0) {
				if(!arg0.contains(post)){             //����û��ղص������в�������ǰ���ӣ����ղ�ͼ���Ϊ���ĵ�����
					newsCommentImshoucang.setBackgroundResource(R.drawable.startn);
					isShoucang = false;
				}else{                                  //����û��ղص������а�����ǰ���ӣ����ղ�ͼ���Ϊʵ�ĵ�����
					newsCommentImshoucang.setBackgroundResource(R.drawable.start);
					isShoucang = true;
				}				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//ListView Item����¼�����
		newsCommentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//�����һ��ʱ��ɾ������
				if(position == 0){                   
					//�����ǰ�û�Ϊ���ӷ����ߣ��򵯳���ʾ���Ƿ�ɾ������
					if(post.getUser().getObjectId().equals(BmobUser.getCurrentUser(PostCommentActivity.this).getObjectId())){
						deletePostTip(position);
					}else{
						//�����ǰ�û��������ӷ�����,�򵯳���ʾ��鿴������Ϣ
						checkAuthorInfoTip(position);
					}
				}else if(position==adapter.getCount()-1){              //������һ��Itemʱ����������Ϣ
					TextView comment_reflash_tip = (TextView) view.findViewById(R.id.comment_reflash_tip);
					ProgressBar comment_reflash_progressBar1 = (ProgressBar) view.findViewById(R.id.comment_reflash_progressBar1);
					comment_reflash_tip.setText("������...");
					comment_reflash_progressBar1.setVisibility(View.VISIBLE);
					init();
				}else{                  
					//�����ǰ�û�Ϊ���ӷ����ߣ��������۷�����,�򵯳���ʾ��ɾ��������
					if(post.getUser().getObjectId().equals(BmobUser.getCurrentUser(PostCommentActivity.this).getObjectId())&&list.get(position-1).getUser().getObjectId().equals(BmobUser.getCurrentUser(PostCommentActivity.this).getObjectId())){
						deleteCommentTip(position);
					}else if(post.getUser().getObjectId().equals(BmobUser.getCurrentUser(PostCommentActivity.this).getObjectId())){
						//�����ǰ�û�Ϊ���ӷ�����,�������۷�����,�򵯳���ѡ��ʾ��ѡ��ɾ�������ۻ��ǲ鿴������Ϣ
						dePostorChInfoTip(position);
					}else if(list.get(position-1).getUser().getObjectId().equals(BmobUser.getCurrentUser(PostCommentActivity.this).getObjectId())){         
						//�����ǰ�û��������ӷ����߶������۷�����,�򵯳���ʾ��ɾ��������
						deleteCommentTip(position);
					}else{		
						//�����ǰ�û��Ȳ������ӷ�����Ҳ�������۷����ߣ��򵯳���ʾ��鿴������������Ϣ
						checkAuthorInfoTip(position);
					}
				}
				
			}
		});
	}

	//��ѡ��ʾ��ѡ��ɾ�������ۻ��߲鿴������Ϣ
	private void dePostorChInfoTip(final int position) {
		new AlertDialog.Builder(PostCommentActivity.this).setTitle("��ѡ��")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setSingleChoiceItems(new String[]{"�鿴������Ϣ", "ɾ����������", "ȡ��"}, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:                //�鿴������Ϣ
					checkAuthorInfoTip(position);
					break;
				case 1:               //ɾ����������
					deleteCommentTip(position);
					break;
				case 2:              //ȡ��
					break;
				}
				dialog.dismiss();
			}
		}).show();
	}

	//��ʾ��鿴������Ϣ
	private void checkAuthorInfoTip(final int position) {
		new AlertDialog.Builder(PostCommentActivity.this).setTitle("�鿴������Ϣ")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(position == 0){                //�鿴����������Ϣ
					PersonerInfoActivity.actionStart(PostCommentActivity.this, post.getUser());
				}else{                        //�鿴����������Ϣ
					PersonerInfoActivity.actionStart(PostCommentActivity.this, list.get(position-1).getUser());
				}
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}
	
	//��ʾ��ɾ��������
	private void deleteCommentTip(final int position) {
		new AlertDialog.Builder(PostCommentActivity.this).setTitle("ɾ������")
		.setMessage("ȷ��ɾ��������?")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressBar.setVisibility(View.VISIBLE);
				Comment dcomment = new Comment();
				dcomment.setObjectId(list.get(position-1).getObjectId());            //ɾ��������
				dcomment.delete(PostCommentActivity.this, new DeleteListener() {
						
					@Override
					public void onSuccess() {
						progressBar.setVisibility(View.GONE);
						init();
						Toast.makeText(PostCommentActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
					}
						
					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
					
			}
		}).show();
			
	}

	//��ʾ���Ƿ�ɾ������
	private void deletePostTip(int position) {
		new AlertDialog.Builder(PostCommentActivity.this).setTitle("ɾ������")
		.setMessage("ȷ��ɾ��������?")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressBar.setVisibility(View.VISIBLE);
				Post dpost = new Post();
				dpost.setObjectId(post.getObjectId());                    //ɾ������
				dpost.delete(PostCommentActivity.this, new DeleteListener() {
					
					@Override
					public void onSuccess() {           //ɾ�����ӳɹ��󣬽������ӵ���������Ҳɾ��
						List<BmobObject> clist = new ArrayList<BmobObject>();
						for(int i=0;i<list.size();i++){         //�õ������ӵ���������
							clist.add(list.get(i));
						}
						new BmobObject().deleteBatch(PostCommentActivity.this, clist, new DeleteListener() {    //����ɾ������
							
							@Override
							public void onSuccess() {
								progressBar.setVisibility(View.GONE);
								Toast.makeText(PostCommentActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
								finish();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								progressBar.setVisibility(View.GONE);
								Toast.makeText(PostCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();												
							}
						});
						
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();		
	}

	//��һActivity�����ⷽ������ת�������沢�����ݴ��ݵ�������
	public static void actionStart(Context context, Post post){
		Intent intent = new Intent(context, PostCommentActivity.class);
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
			progressBar.setVisibility(View.VISIBLE);
			MyUsers users = BmobUser.getCurrentUser(PostCommentActivity.this, MyUsers.class);
			BmobRelation relation = new BmobRelation();
			if(isShoucang){         //�����Ѿ��ղ��ˣ����ȡ���ղ�
				relation.remove(post);
				users.setLikes(relation);
				users.update(PostCommentActivity.this, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						newsCommentImshoucang.setBackgroundResource(R.drawable.startn);
						isShoucang = false;
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, "ȡ���ղ�",
								Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, arg1,
								Toast.LENGTH_SHORT).show();						
					}
				});
			}else{
				// ����δ���ղأ�����ǰpost��ӵ�users���е�likes�ֶ�ֵ�У�������ǰ�û�ϲ��������
				relation.add(post);
				users.setLikes(relation);
				users.update(PostCommentActivity.this, new UpdateListener() {

					@Override
					public void onSuccess() {
						newsCommentImshoucang.setBackgroundResource(R.drawable.start);
						isShoucang = true;
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, "�ղسɹ�",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, arg1,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		case R.id.newscomment_buok:               //��������
			final String content = newsCommentEdcomment.getText().toString();
			if(!TextUtils.isEmpty(content)){             //��������ݲ�Ϊ�գ����������ȡ������ʱ��
				progressBar.setVisibility(View.VISIBLE);
				Bmob.getServerTime(this, new GetServerTimeListener() {
					
					@Override
					public void onSuccess(long arg0) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String times = formatter.format(new Date(arg0 * 1000L));              
						//��ȡʱ��ɹ�����Comment�������һ������
						MyUsers user = BmobUser.getCurrentUser(PostCommentActivity.this, MyUsers.class);
						final Comment comment = new Comment();
						comment.setContent(content);
						comment.setTime(times);
						comment.setUser(user);
						comment.setPost(post);
						
						BmobACL acl = new BmobACL();         //����һ��ACL����,���÷���Ȩ��
						acl.setPublicReadAccess(true);         // ���������˿ɶ���Ȩ��
						acl.setWriteAccess(BmobUser.getCurrentUser(PostCommentActivity.this), true);      // ���õ�ǰ�û���д��Ȩ��
						acl.setWriteAccess(post.getUser(), true);          //�������ӷ����߿�д��Ȩ��
						
						comment.setACL(acl);
						comment.save(PostCommentActivity.this, new SaveListener() {
							
							@Override
							public void onSuccess() {
								newsCommentEdcomment.setText("");
								progressBar.setVisibility(View.GONE);
								init();
								post.update(PostCommentActivity.this);         //��������
								Toast.makeText(PostCommentActivity.this, "���۷���ɹ�", Toast.LENGTH_SHORT).show();							
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								progressBar.setVisibility(View.GONE);
								Toast.makeText(PostCommentActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(PostCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();						
					}
				});
			}else{
				Toast.makeText(PostCommentActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
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
		query.findObjects(PostCommentActivity.this, new FindListener<Comment>() {
			
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
				Toast.makeText(PostCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();				
			}
		});
	}

}
