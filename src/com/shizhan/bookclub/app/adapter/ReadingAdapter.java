package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadingAdapter extends BaseAdapter {
	
	private List<Library> listsl;
	private Context contextl;
	private LayoutInflater inflater;
	private final int TYPE_1 = 0;
	private final int TYPE_2 = 1;
	
	public ReadingAdapter(Context context,List<Library> lists){
		listsl = lists;
		contextl = context;
	}

	@Override
	public int getCount() {
		if(listsl.size() != 0){
			return listsl.size();
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
		if(listsl.size() == 0){
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
			inflater = LayoutInflater.from(contextl);
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.reading_layout_item, parent, false);
				holder1 = new ViewHolder1();
				holder1.readingItemName = (TextView) convertView.findViewById(R.id.reading_item_name);
				holder1.readingItemhotCount = (TextView) convertView.findViewById(R.id.reading_item_hotcount);
				holder1.readingItemhotIm = (ImageView) convertView.findViewById(R.id.reading_item_hotim);
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
		
		switch (type) {
		case TYPE_1:
			holder1.readingItemName.setText(listsl.get(position).getName());
			holder1.readingItemhotCount.setText(listsl.get(position).getHot());
			holder1.readingItemhotIm.setImageResource(R.drawable.hot);
			break;
		case TYPE_2:
			holder2.im1.setImageResource(R.drawable.miniong);
			holder2.tx1.setText("暂未收录该图书馆。。");
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder1{
		TextView readingItemName;
		TextView readingItemhotCount;
		ImageView readingItemhotIm;
	}
	
	class ViewHolder2{
		ImageView im1;
		TextView tx1;
	}

}
