package com.shizhan.bookclub.app.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.util.ImageHeade;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	
	private Context ccontext;
	private BmobIMConversation c;
	private List<BmobIMMessage> msgs = new ArrayList<BmobIMMessage>();
	private LayoutInflater inflater;
	
	private String currentUid = "";
	private String currentUImage = "";

	public ChatAdapter(Context context, BmobIMConversation c){
		this.c = c;
		ccontext = context;
		try {
            currentUid = BmobUser.getCurrentUser(context).getObjectId();
            currentUImage = BmobUser.getCurrentUser(context, MyUsers.class).getImageUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void addMessage(List<BmobIMMessage> messages){
		msgs.addAll(0, messages);
	}
	
	public void addMessage(BmobIMMessage message){
		msgs.addAll(Arrays.asList(message));
	}
	
	public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }
	
	@Override
	public int getCount() {
		return msgs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageHeade imageHeade;
		ViewHolder holder = null;
		if(convertView == null){
			inflater = LayoutInflater.from(ccontext);
			convertView = inflater.inflate(R.layout.chat_item, parent, false);
			holder = new ViewHolder();
			holder.chatLeft = (LinearLayout) convertView.findViewById(R.id.chat_left);
			holder.chatRiht = (RelativeLayout) convertView.findViewById(R.id.chat_riht);
			holder.chatLeftIm = (ImageView) convertView.findViewById(R.id.chat_left_im);
			holder.chatRightIm = (ImageView) convertView.findViewById(R.id.chat_right_im);
			holder.chatLeftMsg = (TextView) convertView.findViewById(R.id.chat_left_msg);
			holder.chatRightMsg = (TextView) convertView.findViewById(R.id.chat_right_msg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(msgs.get(position).getFromId().equals(currentUid)){
			holder.chatRiht.setVisibility(View.VISIBLE);
			holder.chatLeft.setVisibility(View.GONE);
			holder.chatRightMsg.setText(msgs.get(position).getContent());
			imageHeade = new ImageHeade(currentUImage, holder.chatRightIm);
			imageHeade.setImageHead();
		}else{
			holder.chatRiht.setVisibility(View.GONE);
			holder.chatLeft.setVisibility(View.VISIBLE);
			holder.chatLeftMsg.setText(msgs.get(position).getContent());
			imageHeade = new ImageHeade(c.getConversationIcon(), holder.chatLeftIm);
			imageHeade.setImageHead();
		}
		return convertView;
	}
	
	class ViewHolder{
		LinearLayout chatLeft;
		RelativeLayout chatRiht;
		ImageView chatLeftIm;
		ImageView chatRightIm;
		TextView chatLeftMsg;
		TextView chatRightMsg;
	}

}
