package com.shizhan.bookclub.app.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.ChatActivity;
import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.SearchUserActivity;
import com.shizhan.bookclub.app.adapter.MessageAdapter;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;

import de.greenrobot.event.EventBus;

public class MessageFragment extends Fragment implements OnClickListener, IReflashListener{
	
	private TextView contactsAddFreand;
	private TextView contactsAddGroupc;
	private RelativeLayout contactsSearchFreand;
	private LinearLayout contactsNoFrend;
	private ReFlashListView contactsList;
	
	private MessageAdapter adapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contactsLayout = inflater.inflate(R.layout.contacts_layout, container, false);
		contactsAddFreand = (TextView) contactsLayout.findViewById(R.id.contacts_addfreand);
		contactsAddGroupc = (TextView) contactsLayout.findViewById(R.id.contacts_addgroupc);
		contactsSearchFreand = (RelativeLayout) contactsLayout.findViewById(R.id.contacts_searchfreand);
		contactsNoFrend = (LinearLayout) contactsLayout.findViewById(R.id.contacts_nofrend);
		contactsList = (ReFlashListView) contactsLayout.findViewById(R.id.contacts_list);
		
		adapter = new MessageAdapter(getActivity());
		contactsList.setAdapter(adapter);
		
		contactsAddFreand.setOnClickListener(this);
		contactsAddGroupc.setOnClickListener(this);
		contactsSearchFreand.setOnClickListener(this);
		contactsList.setInterface(this);
		
		//单击Item，进入聊天界面
		contactsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", adapter.getItem(position-1));
                intent.putExtras(bundle);
                startActivity(intent);				
			}
		});
		
		//长按Item删除会话
		contactsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getActivity()).setTitle("删除会话")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobIM.getInstance().deleteConversation(adapter.getItem(position-1));
						adapter.remove(position-1);
						dialog.dismiss();
						query();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();						
					}
				}).show();
				return true;
			}
		});
		
		return contactsLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
		query();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contacts_addfreand:
			Intent intent1 = new Intent(getActivity(), SearchUserActivity.class);
			startActivity(intent1);
			break;
		case R.id.contacts_addgroupc:
			break;
		case R.id.contacts_searchfreand:
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onReflash() {
		Handler handle = new Handler();
		handle.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				query();
				contactsList.reflashComplete();
			}
		}, 2000);
		
	}
	
	//查询全部会话
	private void query() {
		if(BmobIM.getInstance().loadAllConversation().size()==0){
			contactsNoFrend.setVisibility(View.VISIBLE);
		}else{
			contactsNoFrend.setVisibility(View.GONE);
		}
		adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
		adapter.notifyDataSetChanged();
	}
	
	//注册离线消息接收事件
	public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        adapter.notifyDataSetChanged();
    }
	
	//注册消息接收事件
	public void onEventMainThread(MessageEvent event){
		BmobIMMessage msg = event.getMessage();
		final BmobIMConversation conversation=event.getConversation();
        String username =msg.getBmobIMUserInfo().getUserId();
        String title =conversation.getConversationTitle();
        if(!username.equals(title)){
        	BmobQuery<MyUsers> query = new BmobQuery<MyUsers>();
        	query.addWhereEqualTo("objectId", msg.getFromId());
        	query.findObjects(getActivity(), new FindListener<MyUsers>() {
				
				@Override
				public void onSuccess(List<MyUsers> arg0) {
					conversation.setConversationIcon(null);
					conversation.setConversationTitle(arg0.get(0).getUserId());
					BmobIM.getInstance().updateConversation(conversation);
					adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
			        adapter.notifyDataSetChanged();
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();					
				}
								
			});
        }else{
        	adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
            adapter.notifyDataSetChanged();
        }
		
	}
	
}
