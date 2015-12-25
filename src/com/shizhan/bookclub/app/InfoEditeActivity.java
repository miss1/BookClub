package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.shizhan.bookclub.app.adapter.InfoEditeAdapterr;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.MyUsers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoEditeActivity extends Activity implements OnClickListener{
	
	private TextView infoediteCancel;
	private TextView infoediteOk;
	
	private ListView infoEditeList;
	
	private InfoEditeAdapterr adapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infoedite);
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

	//������Ϣ
	private void updateInfo() {
		MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
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
							Toast.makeText(InfoEditeActivity.this, "���³ɹ�", Toast.LENGTH_SHORT).show();
							finish();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(InfoEditeActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();							
						}
					});
				}
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(InfoEditeActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();				
			}
		});
	}

	//���س�ʼ��Ϣ
	private void initInfo() {
		MyUsers user = BmobUser.getCurrentUser(this, MyUsers.class);
		BmobQuery<Information> query = new BmobQuery<Information>();
		query.addWhereEqualTo("user", user);
		query.findObjects(InfoEditeActivity.this, new FindListener<Information>() {
			
			@Override
			public void onSuccess(List<Information> arg0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("head", "��        �ƣ�");
				map.put("contents", arg0.get(0).getNicheng());
				list.add(map);
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("head", "��        ��");
				map1.put("contents", arg0.get(0).getSex());
				list.add(map1);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("head", "��        �䣺");
				map2.put("contents", arg0.get(0).getAge());
				list.add(map2);
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("head", "���ڳ��У�");
				map3.put("contents", arg0.get(0).getCity());
				list.add(map3);
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("head", "����ǩ����");
				map4.put("contents", arg0.get(0).getGeqian());
				list.add(map4);
				Map<String, Object> map5 = new HashMap<String, Object>();
				map5.put("head", "ϲ�����飺");
				map5.put("contents", arg0.get(0).getLovebook());
				list.add(map5);
				Map<String, Object> map6 = new HashMap<String, Object>();
				map6.put("head", "ϲ�����ң�");
				map6.put("contents", arg0.get(0).getLoveauthor());
				list.add(map6);
				Map<String, Object> map7 = new HashMap<String, Object>();
				map7.put("head", "�������ͣ�");
				map7.put("contents", arg0.get(0).getBookstyle());
				list.add(map7);
				adapter = new InfoEditeAdapterr(InfoEditeActivity.this, list);
				infoEditeList.setAdapter(adapter);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(InfoEditeActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
				
			}
		});
			
	}

}