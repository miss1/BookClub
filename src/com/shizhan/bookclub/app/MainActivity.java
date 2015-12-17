package com.shizhan.bookclub.app;

import com.shizhan.bookclub.app.fragment.MeFragment;
import com.shizhan.bookclub.app.fragment.NewsFragment;
import com.shizhan.bookclub.app.fragment.ReadFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
	
	private View readLayout;
	private View newsLayout;
	private View meLayout;
	
	private TextView readText;
	private TextView newsText;
	private TextView meText;
	
	private FragmentManager fragmentManager;   //���ڶ�Fragment���й���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//��ʼ������Ԫ��
		initViews();
		fragmentManager = getFragmentManager();
		//��һ������ʱѡ�е�0��Tab
		setTabSeclection(0);
	}

	/*��ȡÿ����Ҫ�õ��Ŀؼ�ʵ���������úñ�Ҫ�ĵ���¼�*/
	private void initViews() {
		readLayout = findViewById(R.id.read_layout);
		newsLayout = findViewById(R.id.news_layout);
		meLayout = findViewById(R.id.me_layout);
		readText = (TextView) findViewById(R.id.read_text);
		newsText = (TextView) findViewById(R.id.news_text);
		meText = (TextView) findViewById(R.id.me_text);
		readLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		meLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_layout:
			setTabSeclection(0);
			break;
		case R.id.news_layout:
			setTabSeclection(1);
			break;
		case R.id.me_layout:
			setTabSeclection(2);
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
		case 1:
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
		case 2:
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
	}
}
