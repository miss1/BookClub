package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.adapter.InfoEditeAdapterr;
import com.shizhan.bookclub.app.base.BaseActivity;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.MyProgressBar;

public class InfoEditeActivity extends BaseActivity implements OnClickListener{
	
	private TextView infoediteCancel;
	private TextView infoediteOk;
	
	private ListView infoEditeList;
	
	/*网络连接上时ListView加载的适配器及内容*/
	private InfoEditeAdapterr adapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
	
	/*网络没有连接上时ListView加载的适配器及内容*/
	private SimpleAdapter adapterd;
	private List<HashMap<String, Object>> disconectList = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	private ProgressBar progressBar;
	private MyProgressBar myProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infoedite);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(this, null);
		
		initInfo();
		
		infoEditeList = (ListView) findViewById(R.id.infoedite_list);
		infoediteCancel = (TextView) findViewById(R.id.infoedite_cancel);
		infoediteOk = (TextView) findViewById(R.id.infoedite_ok);
		infoediteCancel.setOnClickListener(this);
		infoediteOk.setOnClickListener(this);
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.infoedite_cancel:
			finish();
			break;
		case R.id.infoedite_ok:
			updateInfo();
			break;
		default:
			break;
		}
		
	}
	
	//更新信息
	private void updateInfo() {
		progressBar.setVisibility(View.VISIBLE);
		final MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
		BmobQuery<Information> query = new BmobQuery<Information>();
		query.addWhereEqualTo("user", user);
		query.findObjects(InfoEditeActivity.this, new FindListener<Information>() {
			
			@Override
			public void onSuccess(List<Information> arg0) {
				for(Information info : arg0){
					info.setNicheng(list.get(0).get("contents").toString());
					info.setSex(list.get(1).get("contents").toString());
					info.setAge(list.get(2).get("contents").toString());
					info.setCity(list.get(3).get("contents").toString());
					info.setGeqian(list.get(4).get("contents").toString());
					info.setLovebook(list.get(5).get("contents").toString());
					info.setLoveauthor(list.get(6).get("contents").toString());
					info.setBookstyle(list.get(7).get("contents").toString());
					info.update(InfoEditeActivity.this, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							progressBar.setVisibility(View.GONE);
							Toast.makeText(InfoEditeActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
							finish();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							progressBar.setVisibility(View.GONE);
							Toast.makeText(InfoEditeActivity.this, "更新失败", Toast.LENGTH_SHORT).show();							
						}
					});
				}
				user.setUserId(list.get(0).get("contents").toString());
				user.update(InfoEditeActivity.this);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				progressBar.setVisibility(View.GONE);
				Toast.makeText(InfoEditeActivity.this, "查找失败", Toast.LENGTH_SHORT).show();				
			}
		});
	}

	//加载初始信息
	private void initInfo() {
		progressBar.setVisibility(View.VISIBLE);
		MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
		BmobQuery<Information> query = new BmobQuery<Information>();
		query.addWhereEqualTo("user", user);
		query.findObjects(InfoEditeActivity.this, new FindListener<Information>() {
			
			@Override
			public void onSuccess(List<Information> arg0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("head", "昵        称：");
				map.put("contents", arg0.get(0).getNicheng());
				list.add(map);
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("head", "性        别：");
				map1.put("contents", arg0.get(0).getSex());
				list.add(map1);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("head", "年        龄：");
				map2.put("contents", arg0.get(0).getAge());
				list.add(map2);
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("head", "所在城市：");
				map3.put("contents", arg0.get(0).getCity());
				list.add(map3);
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("head", "个性签名：");
				map4.put("contents", arg0.get(0).getGeqian());
				list.add(map4);
				Map<String, Object> map5 = new HashMap<String, Object>();
				map5.put("head", "喜欢的书：");
				map5.put("contents", arg0.get(0).getLovebook());
				list.add(map5);
				Map<String, Object> map6 = new HashMap<String, Object>();
				map6.put("head", "喜欢作家：");
				map6.put("contents", arg0.get(0).getLoveauthor());
				list.add(map6);
				Map<String, Object> map7 = new HashMap<String, Object>();
				map7.put("head", "看书类型：");
				map7.put("contents", arg0.get(0).getBookstyle());
				list.add(map7);
				adapter = new InfoEditeAdapterr(InfoEditeActivity.this, list);
				infoEditeList.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				disconectList.clear();
				map.put("image", R.drawable.minion);
				map.put("text", "啊哦。没有网喽。。。");
				disconectList.add(map);
				adapterd = new SimpleAdapter(InfoEditeActivity.this, disconectList, R.layout.listbody_layout, new String[]{"image","text"}, new int[]{R.id.im1,R.id.tx1});
				infoEditeList.setAdapter(adapterd);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(InfoEditeActivity.this, arg1, Toast.LENGTH_LONG).show();
			}
		});
			
	}

}
