package com.shizhan.bookclub.app.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.FindPostActivity;
import com.shizhan.bookclub.app.PostCommentActivity;
import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.WritePostActivity;
import com.shizhan.bookclub.app.adapter.NewsListAdapter;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class NewsFragment extends Fragment implements OnClickListener,IReflashListener{
	
	private ImageView newsFind;
	private ImageView newsEdit;
	private ReFlashListView newsListview;
	
	/*����������ʱListView���ص�������������*/
	private NewsListAdapter adapter;
	private List<Post> listp = new ArrayList<Post>();
	
	private MyProgressBar myProgressBar;
	private ProgressBar progressBar;
	
	private long runDate;                     //���ӷ���������������������֮��������ʱ��
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.news_layout, container, false);
		newsFind = (ImageView) newsLayout.findViewById(R.id.news_find);
		newsEdit = (ImageView) newsLayout.findViewById(R.id.news_edit);
		newsListview = (ReFlashListView) newsLayout.findViewById(R.id.news_listview);
		
		adapter = new NewsListAdapter(getActivity(), listp);
		newsListview.setAdapter(adapter);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(getActivity(), null);
		newsListview.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		initInfo();
		
		newsListview.setInterface(this);
		newsFind.setOnClickListener(this);
		newsEdit.setOnClickListener(this);
		
		return newsLayout;
	}

	//��ʼ����Ϣ
	private void initInfo() {
		Date startDate = new Date(System.currentTimeMillis());           //���γ������е���ʼʱ��
		
		BmobQuery<Post> query = new BmobQuery<Post>();                   //��ѯ�������ӣ�����ʾ��newsListview��
		query.include("user");
		query.order("-updatedAt");
		query.findObjects(getActivity(), new FindListener<Post>() {
			
			@Override
			public void onSuccess(List<Post> arg0) {
				listp.clear();
				for(Post post : arg0){
					listp.add(post);
				}
				adapter.notifyDataSetChanged();         //���ݸı䣬��̬�����б�
				newsListview.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				listItemClick();                                  //Item����¼�
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
				newsListview.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
			}
		});
		
		Date endDate = new Date(System.currentTimeMillis());             //���γ������еĽ���ʱ��
		runDate = endDate.getTime() - startDate.getTime();
		System.out.println("runDate:"+runDate);
	}

	//newsListview �������¼�
	private void listItemClick(){
		newsListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = listp.get(position-1);
				PostCommentActivity.actionStart(getActivity(), post);      //��ת�������۵����ӽ���NewsCommentActivity������post������Ϣ���ݵ��ý���
			}
		});
	}

	//���Һ�д���ӵĵ���¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_find:
			Intent intentf = new Intent(getActivity(), FindPostActivity.class);    //��ת�����ҽ���
			startActivity(intentf);
			break;
		case R.id.news_edit:
			Intent intentw = new Intent(getActivity(), WritePostActivity.class);    //��ת��д���ӽ���
			startActivity(intentw);
			break;
		default:
			break;
		}
		
	}

	//ListView����ˢ��
	@Override
	public void onReflash() {
		Handler handle = new Handler();
		if(runDate > 2000){
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					initInfo();
					newsListview.reflashComplete();
				}
			}, runDate);
		}else{
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					initInfo();
					newsListview.reflashComplete();
				}
			}, 2000);
		}
	}

}
