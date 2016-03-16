package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.SearchUserAdapter;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.MyUsers;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchUserActivity extends Activity implements OnClickListener{
	
	private EditText searchContact;
	private TextView searchSearch;
	private ListView searchList;
	
	private SearchUserAdapter adapter;
	private List<Information> infolist = new ArrayList<Information>();
	
	private MyUsers currenrUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contactssearch);
		searchContact = (EditText) findViewById(R.id.contactsearch_contact);
		searchSearch = (TextView) findViewById(R.id.contactsearch_search);
		searchList = (ListView) findViewById(R.id.contactsearch_list);
		
		adapter = new SearchUserAdapter(this, infolist);
		searchList.setAdapter(adapter);
		
		currenrUser = BmobUser.getCurrentUser(SearchUserActivity.this, MyUsers.class);
		
		searchSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String key = searchContact.getText().toString();
		if(TextUtils.isEmpty(key)){
			Toast.makeText(SearchUserActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
		}else{
			//�ؼ��ʲ�Ϊ��ʱ����ѯ�ǳƻ��˺���ؼ�����ͬ���û���Ϣ������ʾ��ListView��
			BmobQuery<Information> query = new BmobQuery<Information>();
			BmobQuery<Information> eq1 = new BmobQuery<Information>();
			eq1.addWhereEqualTo("nicheng", key);
			BmobQuery<Information> eq2 = new BmobQuery<Information>();
			eq2.addWhereEqualTo("zhanghao", key);
			List<BmobQuery<Information>> queries = new ArrayList<BmobQuery<Information>>();
			queries.add(eq1);
			queries.add(eq2);
			query.include("user");
			query.or(queries);
			query.findObjects(SearchUserActivity.this, new FindListener<Information>() {
				
				@Override
				public void onSuccess(List<Information> arg0) {
					infolist.clear();
					if(arg0 != null && arg0.size()>0){
						for(Information info : arg0){
							infolist.add(info);
						}
						adapter.notifyDataSetChanged();
						listItemClick();
					}else{
						Toast.makeText(SearchUserActivity.this, "��û�������Ŷ", Toast.LENGTH_SHORT).show();
						adapter.notifyDataSetChanged();
					}
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					Toast.makeText(SearchUserActivity.this, arg1, Toast.LENGTH_SHORT).show();					
				}
			});
		}
		
	}

	//ListView����¼����������user��ϸ��Ϣҳ��
	private void listItemClick() {
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Information info = infolist.get(position);
				MyUsers user = info.getUser();
				if(!user.getObjectId().equals(currenrUser.getObjectId())){       //�����ѯ���û����ǵ�ǰ�û����������ϸ��Ϣҳ��
					PersonerInfoActivity.actionStart(SearchUserActivity.this, user);
				}else{
					Toast.makeText(SearchUserActivity.this, "�����ҵ��û�Ϊ��ǰ��¼�˺�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

}
