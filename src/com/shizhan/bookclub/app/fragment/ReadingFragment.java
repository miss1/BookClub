package com.shizhan.bookclub.app.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.WebViewActivity;
import com.shizhan.bookclub.app.adapter.ReadingAdapter;
import com.shizhan.bookclub.app.model.Library;
import com.shizhan.bookclub.app.util.MyProgressBar;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
				Library library = lists.get(position);
				
				//跳转到相应网页
				WebViewActivity.actionStart(getActivity(), library.getUrl());
				
				//访问量加1
				int hot = Integer.parseInt(library.getHot())+1;
				library.setHot(String.valueOf(hot));
				library.update(getActivity());
				queryAll();
			}
		});
		
	}

}
