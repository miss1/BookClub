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
	
	private FragmentManager fragmentManager;   //���ڶ�Fragment���й���
	
	public static MainActivity instance;     //��̬��������ʼ��Ϊthis����������Activity������finish()����Activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//���ӷ�����
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
		//��ʼ������Ԫ��
		initViews();
		fragmentManager = getFragmentManager();
		//��һ������ʱѡ�е�1��Tab
		setTabSeclection(1);
	}

	/*��ȡÿ����Ҫ�õ��Ŀؼ�ʵ���������úñ�Ҫ�ĵ���¼�*/
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
	
	/*�������õ�index����������ѡ�е�Tabҳ*/
	private void setTabSeclection(int index) {
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		clearSeclection();
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		hideFragments(transaction);
		switch (index) {
		case 0:
			messageText.setTextColor(Color.GREEN);
			if(contactsFragment == null){
				// ���messageFragmentΪ�գ��򴴽�һ������ӵ�������
				contactsFragment = new MessageFragment();
				transaction.add(R.id.content, contactsFragment);
			}else{
				// ���messageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(contactsFragment);
			}
			break;
		case 1:
			readText.setTextColor(Color.GREEN);
			if(readFragment == null){
				// ���readFragmentΪ�գ��򴴽�һ������ӵ�������
				readFragment = new ReadFragment();
				transaction.add(R.id.content, readFragment);
			}else{
				// ���readFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(readFragment);
			}
			break;
		case 2:
			newsText.setTextColor(Color.GREEN);
			if(newsFragment == null){
				// ���newsFragmentΪ�գ��򴴽�һ������ӵ�������
				newsFragment = new NewsFragment();
				transaction.add(R.id.content, newsFragment);
			}else{
				// ���newsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(newsFragment);
			}
			break;
		case 3:
			meText.setTextColor(Color.GREEN);
			if(meFragment == null){
				// ���meFragmentΪ�գ��򴴽�һ������ӵ�������
				meFragment = new MeFragment();
				transaction.add(R.id.content, meFragment);
			}else{
				// ���meFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(meFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/*��������е�ѡ��״̬*/
	private void clearSeclection() {
		readText.setTextColor(Color.parseColor("#3333FF"));
		newsText.setTextColor(Color.parseColor("#3333FF"));
		meText.setTextColor(Color.parseColor("#3333FF"));
		messageText.setTextColor(Color.parseColor("#3333FF"));
	}
	
	/*�����е�Fragment������Ϊ����״̬*/
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

	//ע����Ϣ�����¼�
	public void onEventMainThread(MessageEvent event){
		checkRedPoint();
	}
	
	//ע��������Ϣ�����¼�
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
