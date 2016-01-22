package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.FindAllAdapter;
import com.shizhan.bookclub.app.model.Post;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class FindallActivity extends Activity implements OnClickListener{
	
	private ImageView findAllImh;
	private EditText findAllEdfind;
	private Button findAllButtonok;
	private ListView findAllList;
	
	private FindAllAdapter adapter;
	private List<Post> listpo = new ArrayList<Post>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_findall);
		findAllImh = (ImageView) findViewById(R.id.findall_imh);
		findAllEdfind = (EditText) findViewById(R.id.findall_edfind);
		findAllButtonok = (Button) findViewById(R.id.findall_buttonok);
		findAllList = (ListView) findViewById(R.id.findall_list);
		
		findAllImh.setOnClickListener(this);
		findAllButtonok.setOnClickListener(this);
		
		adapter = new FindAllAdapter(FindallActivity.this, listpo);
		findAllList.setAdapter(adapter);
	}

	//点击事件处理
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findall_imh:               //返回
			finish();
			break;
		case R.id.findall_buttonok:         //确定搜索
			String key = findAllEdfind.getText().toString();
			if(TextUtils.isEmpty(key)){
				Toast.makeText(FindallActivity.this, "关键字不能为空", Toast.LENGTH_SHORT).show();
			}else{
				search(key);
			}
			break;
		default:
			break;
		}
		
	}
	
	//按关键字查找帖子
	private void search(String key){
		BmobQuery<Post> query = new BmobQuery<Post>();
		query.addWhereContains("content", key);
		query.include("user");
		query.findObjects(FindallActivity.this, new FindListener<Post>() {
			
			@Override
			public void onSuccess(List<Post> arg0) {
				listpo.clear();
				for(Post post:arg0){
					listpo.add(0,post);
				}
				adapter.notifyDataSetChanged();         //数据改变，动态更新列表
				listItemClick();                    //Item点击事件
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				listpo.clear();
				adapter.notifyDataSetChanged();
				Toast.makeText(FindallActivity.this, arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//findAllList子项点击事件
	private void listItemClick(){
		findAllList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = listpo.get(position);
				NewsCommentActivity.actionStart(FindallActivity.this, post);     //跳转到带评论的帖子界面NewsCommentActivity，并将post帖子信息传递到该界面
			}
		});
	}
	
}
