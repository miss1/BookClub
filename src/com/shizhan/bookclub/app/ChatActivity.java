package com.shizhan.bookclub.app;

import java.util.List;

import com.shizhan.bookclub.app.adapter.ChatAdapter;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener,IReflashListener{
	
	private ImageView chatImh;
	private EditText chatEdChat;
	private Button chatBuok;
	private TextView chatUser;
	
	private ReFlashListView chatList;
	private ChatAdapter adapter;
	
	private BmobIMConversation c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		c=BmobIMConversation.obtain(BmobIMClient.getInstance(),(BmobIMConversation)getIntent().getSerializableExtra("c"));
		
		chatUser = (TextView) findViewById(R.id.chat_user);
		chatImh = (ImageView) findViewById(R.id.chat_imh);
		chatEdChat = (EditText) findViewById(R.id.chat_edchat);
		chatBuok = (Button) findViewById(R.id.chat_buok);
		chatList = (ReFlashListView) findViewById(R.id.chat_list);
		
		chatUser.setText(c.getConversationTitle());
		
		chatList.setInterface(this);
		chatImh.setOnClickListener(this);
		chatBuok.setOnClickListener(this);
		
		adapter = new ChatAdapter(this);
		chatList.setAdapter(adapter);
		
		EventBus.getDefault().register(this);
		queryMessage(null);
	}

	//����¼�����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_imh:
			finish();
			break;
		case R.id.chat_buok:
			sendMessage();
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
				BmobIMMessage msg = adapter.getFirstMessage();
				queryMessage(msg);
				chatList.reflashComplete();
			}
		}, 2000);
		
	}

	//������Ϣ���״μ��أ�������msgΪnull������ˢ�µ�ʱ�򣬿�����Ϣ��ĵ�һ��msg��Ϊˢ�µ���ʼʱ��㣬Ĭ�ϰ�����Ϣʱ��Ľ������У�limit�ɿ����߿���
	private void queryMessage(BmobIMMessage msg) {
		c.queryMessages(msg, 10, new MessagesQueryListener() {
			
			@Override
			public void done(List<BmobIMMessage> arg0, BmobException arg1) {
				if(arg1 == null){
					if(arg0 != null && arg0.size()>0){
						adapter.addMessage(arg0);
						adapter.notifyDataSetChanged();
					}
				}else{
					Toast.makeText(ChatActivity.this, arg1.getMessage()+"("+arg1.getErrorCode()+")", Toast.LENGTH_SHORT).show();
				}				
			}
		});
		
	}

	//������Ϣ
	private void sendMessage() {
		String content = chatEdChat.getText().toString();
		if(TextUtils.isEmpty(content)){
			Toast.makeText(ChatActivity.this, "����������", Toast.LENGTH_SHORT).show();
			return;
		}
		BmobIMTextMessage msg = new BmobIMTextMessage();
		msg.setContent(content);
		c.sendMessage(msg, new MessageSendListener() {
			
			@Override
			public void onStart(BmobIMMessage arg0) {
				super.onStart(arg0);
				adapter.addMessage(arg0);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void done(BmobIMMessage arg0, BmobException arg1) {
				if(arg1 == null){
					Log.i("messageSend", "send success");
					adapter.notifyDataSetChanged();
					chatEdChat.setText("");
				}else{
					Log.e("messageSend", arg1.toString());
				}				
			}
		});
	}
	
	//����������Ϣ
	public void onEventMainThread(MessageEvent event){
        if(c!=null && event!=null && c.getConversationId().equals(event.getConversation().getConversationId())){//����ǵ�ǰ�Ự����Ϣ
            BmobIMMessage msg =event.getMessage();
            adapter.addMessage(msg);
            adapter.notifyDataSetChanged();
            //���¸ûỰ������Ѷ�״̬
            c.updateReceiveStatus(msg);
        }
    }
	
	//���´˻Ự��������ϢΪ�Ѷ�״̬
	@Override
	protected void onDestroy() {
		c.updateLocalCache();
		EventBus.getDefault().unregister(this);
        super.onDestroy();       
	}

}
