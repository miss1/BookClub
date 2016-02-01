package com.shizhan.bookclub.app;

import com.shizhan.bookclub.app.fragment.ContactsFragment;
import com.shizhan.bookclub.app.fragment.MeFragment;
import com.shizhan.bookclub.app.fragment.MessageFragment;
import com.shizhan.bookclub.app.fragment.NewsFragment;
import com.shizhan.bookclub.app.fragment.ReadFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private ReadFragment readFragment;
	private NewsFragment newsFragment;
	private MeFragment meFragment;
	private ContactsFragment contactsFragment;
	private MessageFragment messageFragment;
	
	private View readLayout;
	private View newsLayout;
	private View meLayout;
	private View contactsLayout;
	private View messageLayout;
	
	private TextView readText;
	private TextView newsText;
	private TextView meText;
	private TextView contactsText;
	private TextView messageText;
	
	private FragmentManager fragmentManager;   //���ڶ�Fragment���й���
	
	public static MainActivity instance;     //��̬��������ʼ��Ϊthis����������Activity������finish()����Activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		instance = this;
		//��ʼ������Ԫ��
		initViews();
		fragmentManager = getFragmentManager();
		//��һ������ʱѡ�е�2��Tab
		setTabSeclection(2);
	}

	/*��ȡÿ����Ҫ�õ��Ŀؼ�ʵ���������úñ�Ҫ�ĵ���¼�*/
	private void initViews() {
		readLayout = findViewById(R.id.read_layout);
		newsLayout = findViewById(R.id.news_layout);
		meLayout = findViewById(R.id.me_layout);
		contactsLayout = findViewById(R.id.contacts_layout);
		messageLayout = findViewById(R.id.message_layout);
		
		readText = (TextView) findViewById(R.id.read_text);
		newsText = (TextView) findViewById(R.id.news_text);
		meText = (TextView) findViewById(R.id.me_text);
		contactsText = (TextView) findViewById(R.id.contacts_text);
		messageText = (TextView) findViewById(R.id.message_text);
		
		readLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		meLayout.setOnClickListener(this);
		contactsLayout.setOnClickListener(this);
		messageLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_layout:
			setTabSeclection(0);
			break;
		case R.id.contacts_layout:
			setTabSeclection(1);
			break;
		case R.id.read_layout:
			setTabSeclection(2);
			break;
		case R.id.news_layout:
			setTabSeclection(3);
			break;
		case R.id.me_layout:
			setTabSeclection(4);
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
			messageText.setTextColor(Color.WHITE);
			if(messageFragment == null){
				// ���messageFragmentΪ�գ��򴴽�һ������ӵ�������
				messageFragment = new MessageFragment();
				transaction.add(R.id.content, messageFragment);
			}else{
				// ���messageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(messageFragment);
			}
			break;
		case 1:
			contactsText.setTextColor(Color.WHITE);
			if(contactsFragment == null){
				// ���contactsFragmentΪ�գ��򴴽�һ������ӵ�������
				contactsFragment = new ContactsFragment();
				transaction.add(R.id.content, contactsFragment);
			}else{
				// ���contactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(contactsFragment);
			}
			break;
		case 2:
			readText.setTextColor(Color.WHITE);
			if(readFragment == null){
				// ���readFragmentΪ�գ��򴴽�һ������ӵ�������
				readFragment = new ReadFragment();
				transaction.add(R.id.content, readFragment);
			}else{
				// ���readFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(readFragment);
			}
			break;
		case 3:
			newsText.setTextColor(Color.WHITE);
			if(newsFragment == null){
				// ���newsFragmentΪ�գ��򴴽�һ������ӵ�������
				newsFragment = new NewsFragment();
				transaction.add(R.id.content, newsFragment);
			}else{
				// ���newsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(newsFragment);
			}
			break;
		case 4:
			meText.setTextColor(Color.WHITE);
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
		contactsText.setTextColor(Color.parseColor("#3333FF"));
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
		if(messageFragment != null){
			transaction.hide(messageFragment);
		}
	}

}
