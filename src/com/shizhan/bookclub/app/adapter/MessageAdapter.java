package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Message;
import com.shizhan.bookclub.app.util.ImageHeade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	private Context context;
	private List<Message> messages;
	private LayoutInflater inflater;
	
	public MessageAdapter(Context context, List<Message> messages){
		this.context = context;
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
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
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.news_layout_item, parent, false);
			holder = new ViewHolder();
			holder.newsitemImage = (ImageView) convertView.findViewById(R.id.news_itemImage);
			holder.newsitemAuthor = (TextView) convertView.findViewById(R.id.news_itemAuthor);
			holder.newsitemTime = (TextView) convertView.findViewById(R.id.news_itemTime);
			holder.newsitemHead = (TextView) convertView.findViewById(R.id.news_itemHead);
			holder.newsitemContent = (TextView) convertView.findViewById(R.id.news_itemContent);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(messages.get(position).getFromuser().getImageUrl()!=null){
			imageHeade = new ImageHeade(messages.get(position).getFromuser().getImageUrl(), holder.newsitemImage);
			imageHeade.setImageHead();
		}else{
			holder.newsitemImage.setImageResource(R.drawable.head);
		}
		holder.newsitemAuthor.setText(messages.get(position).getFromuser().getUserId());
		holder.newsitemTime.setText(messages.get(position).getUpdatedAt());
		holder.newsitemHead.setText("∏¯ƒ„¡Ù—‘");
		holder.newsitemContent.setText(messages.get(position).getContent());
		return convertView;
	}
	
	class ViewHolder{
		ImageView newsitemImage;
		TextView newsitemAuthor;
		TextView newsitemTime;
		TextView newsitemHead;
		TextView newsitemContent;
	}

}
