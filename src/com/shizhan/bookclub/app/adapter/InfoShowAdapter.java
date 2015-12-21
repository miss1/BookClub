package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.InformationShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InfoShowAdapter extends ArrayAdapter<InformationShow> {
	
	private int resourceId;

	public InfoShowAdapter(Context context, int resource, List<InformationShow> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InformationShow info = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.infoitemh = (TextView) view.findViewById(R.id.info_itemh);
			viewHolder.infoitemc = (TextView) view.findViewById(R.id.info_itemc);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.infoitemh.setText(info.getInfoHead());
		viewHolder.infoitemc.setText(info.getInfoContent());
		return view;
	}
	
	class ViewHolder{
		TextView infoitemh;
		TextView infoitemc;
	}

}
