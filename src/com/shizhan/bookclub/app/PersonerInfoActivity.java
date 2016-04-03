package com.shizhan.bookclub.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.adapter.InfoShowAdapter;
import com.shizhan.bookclub.app.event.ChatEvent;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.InformationShow;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.ImageHeade;

public class PersonerInfoActivity extends Activity implements OnClickListener{
	
	private ImageView personInfoHead;
	private ImageView personInfoBack;
	private TextView personInfoTalk;
	private TextView personInfoName;
	private ListView personInfoList;
	private Button personInfosendM;
	
	private MyUsers user;
	private InfoShowAdapter adapter;                    
	private List<InformationShow> infolist = new ArrayList<InformationShow>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personerinfo);
		
		Intent intent = getIntent();
		user = (MyUsers) intent.getSerializableExtra("user");
		
		personInfoHead = (ImageView) findViewById(R.id.personinfo_head);
		personInfoBack = (ImageView) findViewById(R.id.personinfo_back);
		personInfoTalk = (TextView) findViewById(R.id.personinfo_talk);
		personInfoName = (TextView) findViewById(R.id.personinfo_name);
		personInfoList = (ListView) findViewById(R.id.personinfo_list);
		personInfosendM = (Button) findViewById(R.id.personinfo_sendM);
		
		personInfoBack.setOnClickListener(this);
		personInfoTalk.setOnClickListener(this);
		personInfosendM.setOnClickListener(this);
		
		personInfoName.setText(user.getUserId());
		
		if(user.getImageUrl() != null){
			ImageHeade imageHead = new ImageHeade(user.getImageUrl(), personInfoHead);
			imageHead.setImageHead();
		}else{
			personInfoHead.setImageResource(R.drawable.head);
		}
		initInfo();
	}
	
	//��һActivity�����ⷽ������ת�������沢��MyUsers���ݴ��ݵ�������
	public static void actionStart(Context context, MyUsers user){
		Intent intent = new Intent(context, PersonerInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	//��ʼ����Ϣ
		private void initInfo() {
			BmobQuery<Information> query = new BmobQuery<Information>();
			query.addWhereEqualTo("user", user);
			query.findObjects(PersonerInfoActivity.this, new FindListener<Information>() {
				
				@Override
				public void onSuccess(List<Information> arg0) {
					InformationShow zhanghao = new InformationShow("��        �ţ�", arg0.get(0).getZhanghao());
					infolist.add(zhanghao);
					InformationShow nicheng = new InformationShow("��        ��", arg0.get(0).getSex());
					infolist.add(nicheng);
					InformationShow age = new InformationShow("��        �䣺", arg0.get(0).getAge());
					infolist.add(age);
					InformationShow city = new InformationShow("���ڳ��У�", arg0.get(0).getCity());
					infolist.add(city);
					InformationShow geqian = new InformationShow("����ǩ����", arg0.get(0).getGeqian());
					infolist.add(geqian);
					InformationShow lovebook = new InformationShow("ϲ�����飺", arg0.get(0).getLovebook());
					infolist.add(lovebook);
					InformationShow loveauthor = new InformationShow("ϲ�����ң�", arg0.get(0).getLoveauthor());
					infolist.add(loveauthor);
					InformationShow bookstyle = new InformationShow("�������ͣ�", arg0.get(0).getBookstyle());
					infolist.add(bookstyle);
					adapter = new InfoShowAdapter(PersonerInfoActivity.this, infolist);
					personInfoList.setAdapter(adapter);
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					adapter = new InfoShowAdapter(PersonerInfoActivity.this, infolist);
					personInfoList.setAdapter(adapter);
					Toast.makeText(PersonerInfoActivity.this, arg1, Toast.LENGTH_LONG).show();
				}
			});
			
		}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personinfo_back:
			finish();
			break;
		case R.id.personinfo_talk:
			PersonPostActivity.actionStart(PersonerInfoActivity.this, user);
			break;
		case R.id.personinfo_sendM:
			sendMessage();
			break;
		default:
			break;
		}
		
	}

	private void sendMessage() {
		BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUserId(), null);
		ChatEvent event = new ChatEvent(info);
		onEvent(event);
	}

	public void onEvent(ChatEvent event){
        //�����Ҫ�����û����ϣ�������ֻ��Ҫ���µ�info��ȥ�Ϳ�����
        BmobIM.getInstance().startPrivateConversation(event.info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if(e==null){
                	Intent intent = new Intent(PersonerInfoActivity.this, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(PersonerInfoActivity.this, e.getMessage()+"("+e.getErrorCode()+")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
