package com.shizhan.bookclub.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shizhan.bookclub.app.R;

import cn.bmob.newim.bean.BmobIMConversation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private Context mcontext;
	private List<BmobIMConversation> conversation = new ArrayList<BmobIMConversation>();
	private LayoutInflater inflater;
	
	public MessageAdapter (Context context){
		mcontext = context;
	}
	
	public void bindDatas(List<BmobIMConversation> list){
		conversation.clear();
		if(null != list){
			conversation.addAll(list);
		}
	}
	
	//移除会话
	public void remove(int position){
		conversation.remove(position);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return conversation.size();
	}

	//获取会话
	@Override
	public BmobIMConversation getItem(int position) {
		return conversation.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			inflater = LayoutInflater.from(mcontext);
			convertView = inflater.inflate(R.layout.message_item, parent, false);
			holder = new ViewHolder();
			holder.messageItemImage = (ImageView) convertView.findViewById(R.id.message_itemImage);
			holder.messageItemAuthor = (TextView) convertView.findViewById(R.id.message_itemAuthor);
			holder.messageItemTime = (TextView) convertView.findViewById(R.id.message_itemTime);
			holder.messageItemMessage = (TextView) convertView.findViewById(R.id.message_itemmessage);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.messageItemImage.setImageResource(R.drawable.head);
		holder.messageItemAuthor.setText(conversation.get(position).getConversationTitle());
		if(conversation.get(position).getMessages().size()>0){
			holder.messageItemMessage.setText(conversation.get(position).getMessages().get(0).getContent());
		}else{
			holder.messageItemMessage.setText("");
		}		
		holder.messageItemTime.setText(""+conversation.get(position).getUpdateTime());
		return convertView;
	}
	
	class ViewHolder{
		ImageView messageItemImage;
		TextView messageItemAuthor;
		TextView messageItemTime;
		TextView messageItemMessage;
	}

}
