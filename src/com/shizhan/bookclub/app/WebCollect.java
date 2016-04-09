package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.adapter.ReadingAdapter;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.Library;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class WebCollect extends BaseActivity implements OnClickListener{
	
	private TextView webcTv;
	private ImageView webcimh;
	private ListView webcList;
	
	private ReadingAdapter adapter;
	private List<Library> lists = new ArrayList<Library>();
	
	private ProgressBar progressBar;
	private MyProgressBar myProgressBar;        //ProgressBar
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infotalk);
		webcTv = (TextView) findViewById(R.id.infotalk_tv);
		webcimh = (ImageView) findViewById(R.id.infotalk_imh);
		webcList = (ListView) findViewById(R.id.infotalk_list);
		
		webcTv.setText("我的收藏");
		
		adapter = new ReadingAdapter(this, lists);
		webcList.setAdapter(adapter);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		
		querryAllCollect();
		
		webcimh.setOnClickListener(this);
	}

	private void querryAllCollect() {
		progressBar.setVisibility(View.VISIBLE);
		BmobQuery<Library> queryl = new BmobQuery<Library>();
		MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
		queryl.addWhereRelatedTo("likes", new BmobPointer(user));
		queryl.findObjects(this, new FindListener<Library>() {
			
			@Override
			public void onSuccess(List<Library> arg0) {
				lists.clear();
				for(Library library : arg0){
					lists.add(library);
				}
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.GONE);
				if(lists.size()!=0){
					listItemClick();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(WebCollect.this, arg1, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}
		});
	}

	//点击取消收藏
	private void listItemClick() {
		webcList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DisColecWeb(lists.get(position));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.infotalk_imh:
			finish();
			break;
		default:
			break;
		}		
	}
	
	// 取消收藏对话框
	private void DisColecWeb(final Library library) {
		new AlertDialog.Builder(this)
				.setTitle("取消收藏")
				.setMessage("取消收藏该站?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog, int which) {
						progressBar.setVisibility(View.VISIBLE);
						MyUsers users = BmobUser.getCurrentUser(WebCollect.this, MyUsers.class);
						BmobRelation relation = new BmobRelation();
						relation.remove(library);
						users.setLikes(relation);
						users.update(WebCollect.this, new UpdateListener() {

							@Override
							public void onSuccess() {
								Toast.makeText(WebCollect.this, "取消收藏成功",Toast.LENGTH_SHORT).show();
								querryAllCollect();
								dialog.dismiss();
								progressBar.setVisibility(View.GONE);
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(WebCollect.this, arg1,Toast.LENGTH_SHORT).show();
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
