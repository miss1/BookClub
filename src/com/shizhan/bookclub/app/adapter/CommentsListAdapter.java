package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Comment;
import com.shizhan.bookclub.app.model.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CommentsListAdapter extends BaseAdapter {
	
	private List<Comment> commentList;
	private Post mpost;
	private Context mcontext;
	private LayoutInflater inflater;
	private final int TYPE_1 = 0;         //帖子主题样式
	private final int TYPE_2 = 1;         //帖子评论样式
	private final int TYPE_3 = 2;         //暂无更多刷新样式
	

	public CommentsListAdapter(Context context, Post post, List<Comment> comments){
		commentList = comments;
		mpost = post;
		mcontext = context;
	}
	
	@Override
	public int getCount() {
		if(commentList.size()!=0){
			return commentList.size()+2;
		}else{
			return 2;
		}
	}

	@Override
	public Object getItem(int position) {
		if(position==0){
			return mpost;
		}else if(position == getCount()-1){
			return null;
		}else{
			return commentList.get(position-1);
		}
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	// 每个convert view都会调用此方法，获得当前所需要的view样式 
	@Override
	public int getItemViewType(int position) {
		int p = position;
		if(p == 0){
			return TYPE_1;
		}else if(p == getCount()-1){
			return TYPE_3;
		}else{
			return TYPE_2;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;
		int type = getItemViewType(position);
		if(convertView == null){
			inflater = LayoutInflater.from(mcontext);
			// 按当前所需的样式，确定new的布局 
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.news_layout_item, parent, false);
				holder1 = new ViewHolder1();
				holder1.newsitemImage = (ImageView) convertView.findViewById(R.id.news_itemImage);
				holder1.newsitemAuthor = (TextView) convertView.findViewById(R.id.news_itemAuthor);
				holder1.newsitemTime = (TextView) convertView.findViewById(R.id.news_itemTime);
				holder1.newsitemHead = (TextView) convertView.findViewById(R.id.news_itemHead);
				holder1.newsitemContent = (TextView) convertView.findViewById(R.id.news_itemContent);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = inflater.inflate(R.layout.comments_item, parent, false);
				holder2 = new ViewHolder2();
				holder2.commentsitemImage = (ImageView) convertView.findViewById(R.id.comments_itemImage);
				holder2.commentsitemAuthor = (TextView) convertView.findViewById(R.id.comments_itemAuthor);
				holder2.commentscount = (TextView) convertView.findViewById(R.id.comments_count);
				holder2.commentsitemTime = (TextView) convertView.findViewById(R.id.comments_itemTime);
				holder2.commentsitemContent = (TextView) convertView.findViewById(R.id.comments_itemContent);
				convertView.setTag(holder2);
				break;
			case TYPE_3:
				convertView = inflater.inflate(R.layout.comments_reflash_item, parent, false);
				holder3 = new ViewHolder3();
				holder3.comment_reflash_tip = (TextView) convertView.findViewById(R.id.comment_reflash_tip);
				holder3.comment_reflash_progressBar1 = (ProgressBar) convertView.findViewById(R.id.comment_reflash_progressBar1);
				convertView.setTag(holder3);
				break;
			default:
				break;
			}
			
		}else{
			switch (type) {
			case TYPE_1:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (ViewHolder2) convertView.getTag();
				break;
			case TYPE_3:
				holder3 = (ViewHolder3) convertView.getTag();
				break;
			default:
				break;
			}
		}
		
		// 设置资源
		switch (type) {
		case TYPE_1:
			holder1.newsitemImage.setImageResource(R.drawable.head);
			holder1.newsitemAuthor.setText(mpost.getUser().getUserId());
			holder1.newsitemTime.setText(mpost.getTime());
			holder1.newsitemHead.setText(mpost.getTitle());
			holder1.newsitemContent.setText(mpost.getContent());
			holder1.newsitemContent.setMaxLines(50);
			break;
		case TYPE_2:
			holder2.commentsitemImage.setImageResource(R.drawable.head);
			holder2.commentsitemAuthor.setText(commentList.get(position-1).getUser().getUserId());
			holder2.commentscount.setText("第" + (position+1) + "楼");
			holder2.commentsitemTime.setText(commentList.get(position-1).getTime());
			holder2.commentsitemContent.setText(commentList.get(position-1).getContent());
			break;
		case TYPE_3:
			holder3.comment_reflash_tip.setText("暂无更多");
			holder3.comment_reflash_progressBar1.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		return convertView;
	}
	
	public class ViewHolder1{
		ImageView newsitemImage;
		TextView newsitemAuthor;
		TextView newsitemTime;
		TextView newsitemHead;
		TextView newsitemContent;
	}
	
	public class ViewHolder2{
		ImageView commentsitemImage;
		TextView commentsitemAuthor;
		TextView commentscount;
		TextView commentsitemTime;
		TextView commentsitemContent;
	}
	
	public class ViewHolder3{
		TextView comment_reflash_tip;
		ProgressBar comment_reflash_progressBar1;
	}

}
