package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends ArrayAdapter<Post> {
	
	private int resourceId;

	public NewsListAdapter(Context context, int resource, List<Post> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Post post = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.newsitemImage = (ImageView) view.findViewById(R.id.news_itemImage);
			viewHolder.newsitemAuthor = (TextView) view.findViewById(R.id.news_itemAuthor);
			viewHolder.newsitemTime = (TextView) view.findViewById(R.id.news_itemTime);
			viewHolder.newsitemHead = (TextView) view.findViewById(R.id.news_itemHead);
			viewHolder.newsitemContent = (TextView) view.findViewById(R.id.news_itemContent);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.newsitemImage.setImageResource(R.drawable.head);
		viewHolder.newsitemAuthor.setText(post.getUser().getUserId());
		viewHolder.newsitemTime.setText(post.getTime());
		viewHolder.newsitemHead.setText(post.getTitle());
		viewHolder.newsitemContent.setText(post.getContent());
		return view;
	}

	class ViewHolder{
		ImageView newsitemImage;
		TextView newsitemAuthor;
		TextView newsitemTime;
		TextView newsitemHead;
		TextView newsitemContent;
	}
}
