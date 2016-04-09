package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.MessageAdapter;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.Message;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class MessageActivity extends BaseActivity implements OnClickListener, IReflashListener{
	
	private ImageView messageimh;
	private ReFlashListView messageList;
	private RelativeLayout messageNoInternet;
	private TextView messageTips;
	
	private MessageAdapter adapter;
	private List<Message> lists = new ArrayList<Message>();
	
	private ProgressBar progressBar;
	private MyProgressBar myProgressBar;        //ProgressBar
	
	private long runDate;                     //连接服务器到服务器返回数据之间所隔得时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		
		messageimh = (ImageView) findViewById(R.id.message_imh);
		messageList = (ReFlashListView) findViewById(R.id.message_list);
		messageNoInternet = (RelativeLayout) findViewById(R.id.message_nointernet);
		messageTips = (TextView) findViewById(R.id.message_tipss);
		
		adapter = new MessageAdapter(MessageActivity.this, lists);
		messageList.setAdapter(adapter);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		
		messageNoInternet.setVisibility(View.GONE);
		messageList.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		initInfo();
		
		messageimh.setOnClickListener(this);
		messageList.setInterface(this);
	}

	private void initInfo() {
		Date startDate = new Date(System.currentTimeMillis());           //本段程序运行的起始时间
		
		MyUsers touser = BmobUser.getCurrentUser(MessageActivity.this, MyUsers.class);
		BmobQuery<Message> query = new BmobQuery<Message>();
		query.addWhereEqualTo("touser", touser);
		query.include("fromuser");
		query.order("-updatedAt");
		query.findObjects(MessageActivity.this, new FindListener<Message>() {
			
			@Override
			public void onSuccess(List<Message> arg0) {
				lists.clear();
				for(Message message : arg0){
					lists.add(message);
				}
				adapter.notifyDataSetChanged();
				if(lists.size()==0){
					messageList.setVisibility(View.GONE);
					messageNoInternet.setVisibility(View.VISIBLE);
					messageTips.setText("暂时还没有留言哦");
				}else{
					messageNoInternet.setVisibility(View.GONE);
					messageList.setVisibility(View.VISIBLE);
					listItemClick();
				}
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(MessageActivity.this, arg1, Toast.LENGTH_SHORT).show();
				messageList.setVisibility(View.GONE);
				messageNoInternet.setVisibility(View.VISIBLE);
				messageTips.setText("网络君调皮去了");
				progressBar.setVisibility(View.GONE);
			}
		});
		
		Date endDate = new Date(System.currentTimeMillis());             //本段程序运行的结束时间
		runDate = endDate.getTime() - startDate.getTime();
	}

	private void listItemClick() {
		messageList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				deleteMessage(lists.get(position-1));				
			}
		});
		
	}

	private void deleteMessage(final Message message) {
		new AlertDialog.Builder(MessageActivity.this).setTitle("删除").setMessage("删除该留言?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressBar.setVisibility(View.VISIBLE);
				message.delete(MessageActivity.this, new DeleteListener() {
					
					@Override
					public void onSuccess() {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(MessageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
						initInfo();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(MessageActivity.this, arg1, Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.GONE);
					}
				});
				
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();			
			}
		}).show();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_imh:
			finish();
			break;
		default:
			break;
		}		
	}

	@Override
	public void onReflash() {
		Handler handle = new Handler();
		if(runDate > 2000){
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					initInfo();
					messageList.reflashComplete();
				}
			}, runDate);
		}else{
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					initInfo();
					messageList.reflashComplete();
				}
			}, 2000);
		}		
	}

}
