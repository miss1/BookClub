package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindAllAdapter extends BaseAdapter {
	
	private Context fcontext;
	private List<Post> fposts;
	private LayoutInflater inflater;
	private final int TYPE_2 = 0;        //正常显示帖子时的
	private final int TYPE_3 = 1;        //查找不到时的样式
	
	public FindAllAdapter(Context context, List<Post> posts){
		fcontext = context;
		fposts = posts;
	}

	@Override
	public int getCount() {
		if(fposts.size()!=0){
			return fposts.size();
		}else{
			return 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	// 每个convert view都会调用此方法，获得当前所需要的view样式
	@Override
	public int getItemViewType(int position) {
		if(getCount()==1){
			return TYPE_3;
		}else{
			return TYPE_2;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;
		int type = getItemViewType(position);
		if(convertView == null){
			inflater = LayoutInflater.from(fcontext);
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case TYPE_2:
				convertView = inflater.inflate(R.layout.news_layout_item, parent, false);
				holder2 = new ViewHolder2();
				holder2.newsitemImage = (ImageView) convertView.findViewById(R.id.news_itemImage);
				holder2.newsitemAuthor = (TextView) convertView.findViewById(R.id.news_itemAuthor);
				holder2.newsitemTime = (TextView) convertView.findViewById(R.id.news_itemTime);
				holder2.newsitemHead = (TextView) convertView.findViewById(R.id.news_itemHead);
				holder2.newsitemContent = (TextView) convertView.findViewById(R.id.news_itemContent);
				convertView.setTag(holder2);
				break;
			case TYPE_3:
				convertView = inflater.inflate(R.layout.findall_failed_item, parent, false);
				holder3 = new ViewHolder3();
				holder3.findAllFailedIm = (ImageView) convertView.findViewById(R.id.findall_failed_im);
				holder3.findAllFailedTv = (TextView) convertView.findViewById(R.id.findall_failed_tv);
				convertView.setTag(holder3);
				break;
			default:
				break;
			}
			
		}else{
			switch (type) {
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
		case TYPE_2:
			holder2.newsitemImage.setImageResource(R.drawable.head);
			holder2.newsitemAuthor.setText(fposts.get(position).getUser().getUserId());
			holder2.newsitemHead.setText(fposts.get(position).getTitle());
			holder2.newsitemTime.setText(fposts.get(position).getTime());
			holder2.newsitemContent.setText(fposts.get(position).getContent());
			break;
		case TYPE_3:
			holder3.findAllFailedIm.setImageResource(R.drawable.miniong);
			holder3.findAllFailedTv.setText("暂无搜索结果");
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder2{
		ImageView newsitemImage;
		TextView newsitemAuthor;
		TextView newsitemTime;
		TextView newsitemHead;
		TextView newsitemContent;
	}

	class ViewHolder3{
		ImageView findAllFailedIm;
		TextView findAllFailedTv;
	}
}
