package com.shizhan.bookclub.app.adapter;

import java.util.List;

import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.model.Information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchUserAdapter extends BaseAdapter {
	
	private Context scontext;
	private List<Information> sinfos;
	private LayoutInflater inflater;
	
	public SearchUserAdapter(Context context,List<Information> infos){
		scontext = context;
		sinfos = infos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sinfos.size();
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
		ViewHolder holder = null;
		if(convertView == null){
			inflater = LayoutInflater.from(scontext);
			convertView = inflater.inflate(R.layout.contactssearch_item, parent, false);
			holder = new ViewHolder();
			holder.searchitemImage = (ImageView) convertView.findViewById(R.id.contactsearch_itemImage);
			holder.searchitemAuthor = (TextView) convertView.findViewById(R.id.contactsearch_itemAuthor);
			holder.searchitemId = (TextView) convertView.findViewById(R.id.contactsearch_itemId);
			holder.searchitemgeqian = (TextView) convertView.findViewById(R.id.contactsearch_geqian);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.searchitemImage.setImageResource(R.drawable.head);
		holder.searchitemAuthor.setText(sinfos.get(position).getNicheng());
		holder.searchitemId.setText("("+sinfos.get(position).getZhanghao()+")");
		holder.searchitemgeqian.setText(sinfos.get(position).getGeqian());
		return convertView;
	}
	
	class ViewHolder{
		ImageView searchitemImage;
		TextView searchitemAuthor;
		TextView searchitemId;
		TextView searchitemgeqian;
	}

}
