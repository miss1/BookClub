package com.shizhan.bookclub.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.FindallActivity;
import com.shizhan.bookclub.app.NewsCommentActivity;
import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.WritefeelingActivity;
import com.shizhan.bookclub.app.adapter.NewsListAdapter;
import com.shizhan.bookclub.app.model.Post;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;

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
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsFragment extends Fragment implements OnClickListener,IReflashListener{
	
	private ImageView newsFind;
	private ImageView newsEdit;
	private ReFlashListView newsListview;
	
	/*����������ʱListView���ص�������������*/
	private NewsListAdapter adapter;
	private List<Post> listp = new ArrayList<Post>();
	
	/*����û��������ʱListView���ص�������������*/
	private SimpleAdapter adapterd;
	private List<HashMap<String, Object>> disconectList = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.news_layout, container, false);
		newsFind = (ImageView) newsLayout.findViewById(R.id.news_find);
		newsEdit = (ImageView) newsLayout.findViewById(R.id.news_edit);
		newsListview = (ReFlashListView) newsLayout.findViewById(R.id.news_listview);
		
		adapter = new NewsListAdapter(getActivity(), listp);
		newsListview.setAdapter(adapter);
		initInfo();
		
		newsListview.setInterface(this);
		newsFind.setOnClickListener(this);
		newsEdit.setOnClickListener(this);
		
		return newsLayout;
	}

	//��ʼ����Ϣ
	private void initInfo() {
		BmobQuery<Post> query = new BmobQuery<Post>();                   //��ѯ�������ӣ�����ʾ��newsListview��
		query.include("user");
		query.findObjects(getActivity(), new FindListener<Post>() {
			
			@Override
			public void onSuccess(List<Post> arg0) {
				listp.clear();
				for(Post post : arg0){
					listp.add(0,post);
				}
				adapter.notifyDataSetChanged();         //���ݸı䣬��̬�����б�
				listItemClick();                                  //Item����¼�
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				/*disconectList.clear();
				map.put("image", R.drawable.minion);
				map.put("text", "��Ŷ��û����ඡ�����");
				disconectList.add(map);
				adapterd = new SimpleAdapter(getActivity(), disconectList, R.layout.listbody_layout, new String[]{"image","text"}, new int[]{R.id.im1,R.id.tx1});
				newsListview.setAdapter(adapterd);*/
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}

	//newsListview �������¼�
	private void listItemClick(){
		newsListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = listp.get(position-1);
				NewsCommentActivity.actionStart(getActivity(), post);      //��ת�������۵����ӽ���NewsCommentActivity������post������Ϣ���ݵ��ý���
			}
		});
	}

	//���Һ�д���ӵĵ���¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news_find:
			Intent intentf = new Intent(getActivity(), FindallActivity.class);    //��ת�����ҽ���
			startActivity(intentf);
			break;
		case R.id.news_edit:
			Intent intentw = new Intent(getActivity(), WritefeelingActivity.class);    //��ת��д���ӽ���
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
		handle.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				initInfo();
				newsListview.reflashComplete();
			}
		}, 2000);
	}

}
