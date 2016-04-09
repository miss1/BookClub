package com.shizhan.bookclub.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.WebViewActivity;
import com.shizhan.bookclub.app.adapter.ReadingAdapter;
import com.shizhan.bookclub.app.model.Library;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class ReadingFragment extends Fragment implements OnClickListener{
	
	private ImageView readingSearchImsc;
	private ImageView readingSearchImde;
	private EditText readingSearchEd;
	private ListView readingList;
	
	private ReadingAdapter adapter;
	private List<Library> lists = new ArrayList<Library>();
	
	private ProgressBar progressBar;
	private MyProgressBar myProgressBar;        //ProgressBar
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View readingLayout = inflater.inflate(R.layout.reading_layout, container, false);
		readingSearchImsc = (ImageView) readingLayout.findViewById(R.id.reading_search_imsc);
		readingSearchImde = (ImageView) readingLayout.findViewById(R.id.reading_search_imde);
		readingSearchEd = (EditText) readingLayout.findViewById(R.id.reading_search_ed);
		readingList = (ListView) readingLayout.findViewById(R.id.reading_list);
		
		adapter = new ReadingAdapter(getActivity(), lists);
		readingList.setAdapter(adapter);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(getActivity(), null);
		
		queryAll();
		
		readingSearchImsc.setOnClickListener(this);
		readingSearchImde.setOnClickListener(this);
		return readingLayout;
	}

	//点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reading_search_imsc:
			String key = readingSearchEd.getText().toString();
			if(TextUtils.isEmpty(key)){
				Toast.makeText(getActivity(), "关键词不能为空", Toast.LENGTH_SHORT).show();
			}else{
				queryByKey(key);
				readingSearchImsc.setVisibility(View.GONE);
				readingSearchImde.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.reading_search_imde:
			readingSearchEd.setText("");
			queryAll();
			readingSearchImde.setVisibility(View.GONE);
			readingSearchImsc.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}
	
	//按关键字查询
	private void queryByKey(String key) {
		readingList.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		BmobQuery<Library> query = new BmobQuery<Library>();
		query.addWhereContains("name", key);
		query.findObjects(getActivity(), new FindListener<Library>() {
			
			@Override
			public void onSuccess(List<Library> arg0) {
				lists.clear();
				for(Library library : arg0){
					lists.add(library);
				}
				readingList.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				listItemClick();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				readingList.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}

	//查询所有数据
	private void queryAll() {
		
		readingList.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		BmobQuery<Library> query = new BmobQuery<Library>();
		query.order("-hot");
		query.findObjects(getActivity(), new FindListener<Library>() {
			
			@Override
			public void onSuccess(List<Library> arg0) {
				lists.clear();
				for(Library library : arg0){
					lists.add(library);
				}
				readingList.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				listItemClick();
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				readingList.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void listItemClick() {
		readingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(lists.size() == 0)
					return;
				String objectedId = lists.get(position).getObjectId();
				
				BmobQuery<Library> query = new BmobQuery<Library>();
				query.getObject(getActivity(), objectedId, new GetListener<Library>() {
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();					
					}
					
					@Override
					public void onSuccess(Library arg0) {
						//跳转到相应网页
						WebViewActivity.actionStart(getActivity(), arg0.getUrl());
						//访问量加1
						int hot = arg0.getHot().intValue() + 1;
						arg0.setHot(hot);
						arg0.update(getActivity());
						readingSearchEd.setText("");
						readingSearchImde.setVisibility(View.GONE);
						readingSearchImsc.setVisibility(View.VISIBLE);
					}
				});				
				queryAll();
			}
		});
		
		 //长按显示收藏对话框
		readingList.setOnItemLongClickListener(new OnItemLongClickListener() {      

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if(lists.size() == 0)
					return true;
				progressBar.setVisibility(View.VISIBLE);
				BmobQuery<Library> queryl = new BmobQuery<Library>();
				MyUsers user = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
				queryl.addWhereRelatedTo("likes", new BmobPointer(user));
				queryl.findObjects(getActivity(), new FindListener<Library>() {
					
					@Override
					public void onSuccess(List<Library> arg0) {
						progressBar.setVisibility(View.GONE);
						if(!arg0.contains(lists.get(position))){
							ColectWeb(lists.get(position));                 //收藏
						}else{
							DisColecWeb(lists.get(position));                //取消收藏
						}						
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						progressBar.setVisibility(View.GONE);
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();						
					}
				});
				
				return true;
			}
		});
	}

	//收藏对话框
	private void ColectWeb(final Library library) {
		new AlertDialog.Builder(getActivity()).setTitle("收藏").setMessage("收藏该站?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(final DialogInterface dialog, int which) {
				progressBar.setVisibility(View.VISIBLE);
				MyUsers users = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
				BmobRelation relation = new BmobRelation();
				relation.add(library);
				users.setLikes(relation);
				users.update(getActivity(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						progressBar.setVisibility(View.GONE);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
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
	
	//取消收藏对话框
	private void DisColecWeb(final Library library){
		new AlertDialog.Builder(getActivity()).setTitle("取消收藏").setMessage("取消收藏该站?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(final DialogInterface dialog, int which) {
				progressBar.setVisibility(View.VISIBLE);
				MyUsers users = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
				BmobRelation relation = new BmobRelation();
				relation.remove(library);
				users.setLikes(relation);
				users.update(getActivity(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						Toast.makeText(getActivity(), "取消收藏成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						progressBar.setVisibility(View.GONE);
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
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

}
