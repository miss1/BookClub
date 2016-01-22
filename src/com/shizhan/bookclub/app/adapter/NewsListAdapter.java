package com.shizhan.bookclub.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Post;

public class NewsListAdapter extends BaseAdapter {
	
	private Context ncontext;
	private List<Post> nposts;
	private LayoutInflater inflater;
	private final int TYPE_1 = 0;      //������������ʱ����ʽ
	private final int TYPE_2 = 1;      //û����ʱview����ʽ

	public NewsListAdapter(Context context, List<Post> posts){
		ncontext = context;
		nposts = posts;
	}

	@Override
	public int getCount() {
		if(nposts.size()!=0){
			return nposts.size();
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
	
	// ÿ��convert view������ô˷�������õ�ǰ����Ҫ��view��ʽ
	@Override
	public int getItemViewType(int position) {
		if(getCount()==1){
			return TYPE_2;
		}else{
			return TYPE_1;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if(convertView == null){
			inflater = LayoutInflater.from(ncontext);
			// ����ǰ�������ʽ��ȷ��new�Ĳ���
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
				convertView = inflater.inflate(R.layout.listbody_layout, parent, false);
				holder2 = new ViewHolder2();
				holder2.im1 = (ImageView) convertView.findViewById(R.id.im1);
				holder2.tx1 = (TextView) convertView.findViewById(R.id.tx1);
				convertView.setTag(holder2);
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
			default:
				break;
			}
		}
		
		// ������Դ
		switch (type) {
		case TYPE_1:
			holder1.newsitemImage.setImageResource(R.drawable.head);
			holder1.newsitemAuthor.setText(nposts.get(position).getUser().getUserId());
			holder1.newsitemTime.setText(nposts.get(position).getTime());
			holder1.newsitemHead.setText(nposts.get(position).getTitle());
			holder1.newsitemContent.setText(nposts.get(position).getContent());
			break;
		case TYPE_2:
			holder2.im1.setImageResource(R.drawable.minion);
			holder2.tx1.setText("��Ŷ��û�����ˡ���");
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder1{
		ImageView newsitemImage;
		TextView newsitemAuthor;
		TextView newsitemTime;
		TextView newsitemHead;
		TextView newsitemContent;
	}
	
	class ViewHolder2{
		ImageView im1;
		TextView tx1;
	}
}
