package com.shizhan.bookclub.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.shizhan.bookclub.app.model.Comment;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.model.Post;

import android.app.Activity;
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
		post = (Post) intent.getSerializableExtra("post");    //接收传递过来的Post类对象
		
		//点击事件监听
		newsCommentImh.setOnClickListener(this);
		newsCommentImshoucang.setOnClickListener(this);
		newsCommentBuok.setOnClickListener(this);
		
		adapter = new CommentsListAdapter(this, post, list);
		newsCommentList.setAdapter(adapter);
		init();
		
		//ListView Item点击事件处理
		newsCommentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				//点击第一项时，删除帖子
				if(position == 0){                   
					//如果当前用户为帖子发布者，则弹出提示框是否删除帖子
					if(post.getUser().getObjectId().equals(BmobUser.getCurrentUser(NewsCommentActivity.this).getObjectId())){
						deletePostTip(position);
					}else{
						//如果当前用户不是帖子发布者,则弹出提示框查看作者信息
						checkAuthorInfoTip(position);
					}
				}else if(position==adapter.getCount()-1){              //点击最后一项Item时更新评论信息
					TextView comment_reflash_tip = (TextView) view.findViewById(R.id.comment_reflash_tip);
					ProgressBar comment_reflash_progressBar1 = (ProgressBar) view.findViewById(R.id.comment_reflash_progressBar1);
					comment_reflash_tip.setText("更新中...");
					comment_reflash_progressBar1.setVisibility(View.VISIBLE);
					init();
				}else{                  
					//如果当前用户为帖子发布者，则弹出单选提示框选择删除该评论还是查看作者信息
					if(post.getUser().getObjectId().equals(BmobUser.getCurrentUser(NewsCommentActivity.this).getObjectId())){
						dePostorChInfoTip(position);
					}else if(list.get(position-1).getUser().getObjectId().equals(BmobUser.getCurrentUser(NewsCommentActivity.this).getObjectId())){         
						//如果当前用户不是帖子发布者而是评论发布者,则弹出提示框删除该评论
						deleteCommentTip(position);
					}else{
						//如果当前用户既不是帖子发布者也不是评论发布者，则弹出提示框查看该评论作者信息
						checkAuthorInfoTip(position);
					}
				}
				
			}
		});
	}

	//单选提示框选择删除该评论或者查看作者信息
	private void dePostorChInfoTip(final int position) {
		new AlertDialog.Builder(NewsCommentActivity.this).setTitle("请选择")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setSingleChoiceItems(new String[]{"查看作者信息", "删除该条评论", "取消"}, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:                //查看作者信息
					checkAuthorInfoTip(position);
					break;
				case 1:               //删除该条评论
					deleteCommentTip(position);
					break;
				case 2:              //取消
					break;
				}
				dialog.dismiss();
			}
		}).show();
	}

	//提示框查看作者信息
	private void checkAuthorInfoTip(final int position) {
		new AlertDialog.Builder(NewsCommentActivity.this).setTitle("查看作者信息")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(position == 0){                //查看帖子作者信息
					PersonerInfoActivity.actionStart(NewsCommentActivity.this, post.getUser());
				}else{                        //查看评论作者信息
					PersonerInfoActivity.actionStart(NewsCommentActivity.this, list.get(position-1).getUser());
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}
	
	//提示框删除该评论
	private void deleteCommentTip(final int position) {
		new AlertDialog.Builder(NewsCommentActivity.this).setTitle("删除评论")
		.setMessage("确定删除该评论?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Comment dcomment = new Comment();
				dcomment.setObjectId(list.get(position-1).getObjectId());            //删除该评论
				dcomment.delete(NewsCommentActivity.this, new DeleteListener() {
						
					@Override
					public void onSuccess() {
						Toast.makeText(NewsCommentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					}
						
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
					
			}
		}).show();
			
	}

	//提示框是否删除帖子
	private void deletePostTip(int position) {
		new AlertDialog.Builder(NewsCommentActivity.this).setTitle("删除帖子")
		.setMessage("确定删除该帖子?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Post dpost = new Post();
				dpost.setObjectId(post.getObjectId());                    //删除帖子
				dpost.delete(NewsCommentActivity.this, new DeleteListener() {
					
					@Override
					public void onSuccess() {           //删除帖子成功后，将该帖子的所有评论也删除
						List<BmobObject> clist = new ArrayList<BmobObject>();
						for(int i=0;i<list.size();i++){         //得到该帖子的所有评论
							clist.add(list.get(i));
						}
						new BmobObject().deleteBatch(NewsCommentActivity.this, clist, new DeleteListener() {    //批量删除评论
							
							@Override
							public void onSuccess() {
								Toast.makeText(NewsCommentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
								finish();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();												
							}
						});
						
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();		
	}

	//上一Activity调用这方法，跳转到本界面并将数据传递到本界面
	public static void actionStart(Context context, Post post){
		Intent intent = new Intent(context, NewsCommentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("post", post);        //传递Post类对象
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	//点击事件处理
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newscomment_imh:      //返回
			finish();
			break;
		case R.id.newscomment_imshoucang:       //收藏
			MyUsers users = BmobUser.getCurrentUser(NewsCommentActivity.this, MyUsers.class);
			//将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
			BmobRelation relation = new BmobRelation();
			relation.add(users);
			post.setLikes(relation);
			post.update(NewsCommentActivity.this, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(NewsCommentActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();					
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();					
				}
			});
			break;
		case R.id.newscomment_buok:               //发表评论
			final String content = newsCommentEdcomment.getText().toString();
			if(!TextUtils.isEmpty(content)){             //发表的内容不为空，则接下来获取服务器时间
				Bmob.getServerTime(this, new GetServerTimeListener() {
					
					@Override
					public void onSuccess(long arg0) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String times = formatter.format(new Date(arg0 * 1000L));              
						//获取时间成功，往Comment表中添加一条内容
						MyUsers user = BmobUser.getCurrentUser(NewsCommentActivity.this, MyUsers.class);
						Comment comment = new Comment();
						comment.setContent(content);
						comment.setTime(times);
						comment.setUser(user);
						comment.setPost(post);
						
						BmobACL acl = new BmobACL();         //创建一个ACL对象,设置访问权限
						acl.setPublicReadAccess(true);         // 设置所有人可读的权限
						acl.setWriteAccess(BmobUser.getCurrentUser(NewsCommentActivity.this), true);      // 设置当前用户可写的权限
						acl.setWriteAccess(post.getUser(), true);          //设置帖子发布者可写的权限
						
						comment.setACL(acl);
						comment.save(NewsCommentActivity.this, new SaveListener() {
							
							@Override
							public void onSuccess() {
								newsCommentEdcomment.setText("");
								Toast.makeText(NewsCommentActivity.this, "评论发表成功", Toast.LENGTH_SHORT).show();							
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(NewsCommentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();						
					}
				});
			}else{
				Toast.makeText(NewsCommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		
	}
	
	//查询该帖子的所有评论
	private void init(){
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("post", new BmobPointer(post));
		//同时查询该评论的发布者的信息，以及该帖子的作者的信息
		query.include("user");
		query.findObjects(NewsCommentActivity.this, new FindListener<Comment>() {
			
			@Override
			public void onSuccess(List<Comment> arg0) {
				list.clear();
				for(Comment comment : arg0){
					list.add(comment);
				}
				adapter.notifyDataSetChanged();        //数据改变，动态更新列表
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(NewsCommentActivity.this, arg1, Toast.LENGTH_SHORT).show();				
			}
		});
	}

}
