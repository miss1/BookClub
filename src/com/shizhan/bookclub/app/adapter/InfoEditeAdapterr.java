package com.shizhan.bookclub.app.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shizhan.bookclub.app.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class InfoEditeAdapterr extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;// 存储的EditText值
	public Map<String, String> editorValue = new HashMap<String, String>();

	public InfoEditeAdapterr(Context context, List<Map<String, Object>> data) {
		mData = data;
		mInflater = LayoutInflater.from(context);
		init();
	}

	// 初始化
	private void init() {
		editorValue.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
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

	private Integer index = -1;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_infoedite_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.infoedite_item_t);
			holder.value = (EditText) convertView.findViewById(R.id.infoedite_item_e);
			holder.value.setTag(position);
			holder.value.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_UP){
						index = (Integer) v.getTag();
					}
					return false;
				}
			});
			class MyTextWatcher implements TextWatcher {
				
				private  ViewHolder mHolder;
				
				public MyTextWatcher(ViewHolder holder){
					mHolder = holder;
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void afterTextChanged(Editable s) {
					if(s !=null && !"".equals(s.toString())){
						int position = (Integer) mHolder.value.getTag();
						mData.get(position).put("contents", s.toString());   // 当EditText数据发生改变的时候存到data变量中
					}
					
				}
				
			}
			holder.value.addTextChangedListener(new MyTextWatcher(holder));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			holder.value.setTag(position);
		}
		Object value = mData.get(position).get("head");
		if(value !=null){
			holder.title.setText((String)value);
		}
		value = mData.get(position).get("contents");
		if(value != null){
			holder.value.setText(value.toString());
		}		
		holder.value.clearFocus();
		if(index != -1 && index == position){
			holder.value.requestFocus();
		}
		return convertView;
	}
	
	public final class ViewHolder {
        public TextView title;
        public EditText value;// ListView中的输入
    }


}
