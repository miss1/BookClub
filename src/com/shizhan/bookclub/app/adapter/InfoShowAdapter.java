package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.InformationShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoShowAdapter extends BaseAdapter {
	
	private List<InformationShow> infolista;
	private Context contexta;
	private LayoutInflater inflater;
	private final int TYPE_1 = 0;
	private final int TYPE_2 = 1;
	
	public InfoShowAdapter(Context context, List<InformationShow> infolists){
		infolista = infolists;
		contexta = context;
	}

	@Override
	public int getCount() {
		if(infolista.size()!=0){
			return infolista.size();
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
			inflater = LayoutInflater.from(contexta);
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.me_layout_item, parent, false);
				holder1 = new ViewHolder1();
				holder1.infoitemc = (TextView) convertView.findViewById(R.id.info_itemc);
				holder1.infoitemh = (TextView) convertView.findViewById(R.id.info_itemh);
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
		
		// 设置资源
		switch (type) {
		case TYPE_1:
			holder1.infoitemc.setText(infolista.get(position).getInfoContent());
			holder1.infoitemh.setText(infolista.get(position).getInfoHead());
			break;
		case TYPE_2:
			holder2.im1.setImageResource(R.drawable.minion);
			holder2.tx1.setText("啊哦。没有网了。。");
			break;
		default:
			break;
		}
		
		return convertView;
	}

	class ViewHolder1{
		TextView infoitemh;
		TextView infoitemc;
	}
	
	class ViewHolder2{
		ImageView im1;
		TextView tx1;
	}
	
}
