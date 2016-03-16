package com.shizhan.bookclub.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import com.shizhan.bookclub.app.fragment.MessageFragment;
import com.shizhan.bookclub.app.fragment.MeFragment;
import com.shizhan.bookclub.app.fragment.NewsFragment;
import com.shizhan.bookclub.app.fragment.ReadFragment;
import com.shizhan.bookclub.app.model.MyUsers;

public class MainActivity extends Activity implements OnClickListener{

	private ReadFragment readFragment;
	private NewsFragment newsFragment;
	private MeFragment meFragment;
	private MessageFragment contactsFragment;
	
	private View readLayout;
	private View newsLayout;
	private View meLayout;
	private View messageLayout;
	
	private TextView readText;
	private TextView newsText;
	private TextView meText;
	private TextView messageText;
	private ImageView messageTips;
	
	private FragmentManager fragmentManager;   //用于对Fragment进行管理
	
	public static MainActivity instance;     //静态变量，初始化为this，方便其他Activity调用以finish()本个Activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//连接服务器
		MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
		BmobIM.connect(user.getObjectId(), new ConnectListener() {
			
			@Override
			public void done(String arg0, BmobException arg1) {
				if (arg1 == null) {
		            Log.i("bmobIm", "connect success");
		        } else {
		            Log.i("bmobIm",arg1.getErrorCode() + "/" + arg1.getMessage());
		        }				
			}
		});
		BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Toast.makeText(MainActivity.this, "" + status.getMsg(), Toast.LENGTH_SHORT);
            }
        });
		
		instance = this;
		//初始化布局元素
		initViews();
		fragmentManager = getFragmentManager();
		//第一次启动时选中第1个Tab
		setTabSeclection(1);
	}

	/*获取每个需要用到的控件实例，并设置好必要的点击事件*/
	private void initViews() {
		readLayout = findViewById(R.id.read_layout);
		newsLayout = findViewById(R.id.news_layout);
		meLayout = findViewById(R.id.me_layout);
		messageLayout = findViewById(R.id.message_layout);
		
		readText = (TextView) findViewById(R.id.read_text);
		newsText = (TextView) findViewById(R.id.news_text);
		meText = (TextView) findViewById(R.id.me_text);
		messageText = (TextView) findViewById(R.id.message_text);
		messageTips = (ImageView) findViewById(R.id.message_tips);
		
		readLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		meLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_layout:
			setTabSeclection(0);
			break;
		case R.id.read_layout:
			setTabSeclection(1);
			break;
		case R.id.news_layout:
			setTabSeclection(2);
			break;
		case R.id.me_layout:
			setTabSeclection(3);
			break;
		default:
			break;
		}
		
	}
	
	/*根据设置的index参数来设置选中的Tab页*/
	private void setTabSeclection(int index) {
		// 每次选中之前先清除掉上次的选中状态
		clearSeclection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			messageText.setTextColor(Color.GREEN);
			if(contactsFragment == null){
				// 如果messageFragment为空，则创建一个并添加到界面上
				contactsFragment = new MessageFragment();
				transaction.add(R.id.content, contactsFragment);
			}else{
				// 如果messageFragment不为空，则直接将它显示出来
				transaction.show(contactsFragment);
			}
			break;
		case 1:
			readText.setTextColor(Color.GREEN);
			if(readFragment == null){
				// 如果readFragment为空，则创建一个并添加到界面上
				readFragment = new ReadFragment();
				transaction.add(R.id.content, readFragment);
			}else{
				// 如果readFragment不为空，则直接将它显示出来
				transaction.show(readFragment);
			}
			break;
		case 2:
			newsText.setTextColor(Color.GREEN);
			if(newsFragment == null){
				// 如果newsFragment为空，则创建一个并添加到界面上
				newsFragment = new NewsFragment();
				transaction.add(R.id.content, newsFragment);
			}else{
				// 如果newsFragment不为空，则直接将它显示出来
				transaction.show(newsFragment);
			}
			break;
		case 3:
			meText.setTextColor(Color.GREEN);
			if(meFragment == null){
				// 如果meFragment为空，则创建一个并添加到界面上
				meFragment = new MeFragment();
				transaction.add(R.id.content, meFragment);
			}else{
				// 如果meFragment不为空，则直接将它显示出来
				transaction.show(meFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/*清除掉所有的选中状态*/
	private void clearSeclection() {
		readText.setTextColor(Color.parseColor("#3333FF"));
		newsText.setTextColor(Color.parseColor("#3333FF"));
		meText.setTextColor(Color.parseColor("#3333FF"));
		messageText.setTextColor(Color.parseColor("#3333FF"));
	}
	
	/*将所有的Fragment都设置为隐藏状态*/
	private void hideFragments(FragmentTransaction transaction) {
		if(readFragment != null){
			transaction.hide(readFragment);
		}
		if(newsFragment != null){
			transaction.hide(newsFragment);
		}
		if(meFragment !=null){
			transaction.hide(meFragment);
		}
		if(contactsFragment != null){
			transaction.hide(contactsFragment);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkRedPoint();
	}

	//注册消息接收事件
	public void onEventMainThread(MessageEvent event){
		checkRedPoint();
	}
	
	//注册离线消息接收事件
	public void onEventMainThread(OfflineMessageEvent event){
	    checkRedPoint();
	}
	
	private void checkRedPoint() {
		if(BmobIM.getInstance().getAllUnReadCount()>0){
			messageTips.setVisibility(View.VISIBLE);
		}else{
			messageTips.setVisibility(View.GONE);
		}		
	}

}
