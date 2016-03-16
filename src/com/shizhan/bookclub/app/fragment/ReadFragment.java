package com.shizhan.bookclub.app.fragment;

import com.shizhan.bookclub.app.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ReadFragment extends Fragment implements OnClickListener{
	
	private Button readOnlineRead;
	private Button readMap;
	
	private ReadingFragment readingFragment;
	private MapFragment mapFragment;
	
	private FragmentManager fragmentManager;       //���ڶ�Fragment���й���
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View readLayout = inflater.inflate(R.layout.read_layout, container, false);
		readOnlineRead = (Button) readLayout.findViewById(R.id.read_onlineread);
		readMap = (Button) readLayout.findViewById(R.id.read_map);
		
		readOnlineRead.setOnClickListener(this);
		readMap.setOnClickListener(this);
		
		fragmentManager = getFragmentManager();
		
		//��һ������ʱѡ�е�0��Tab
		setTabSeclection(0);
		return readLayout;
	}

	//����¼��������ÿؼ�ѡ��״̬
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.read_onlineread:
			readOnlineRead.setBackgroundResource(R.drawable.a_shap_button_onclick);
			readOnlineRead.setTextColor(Color.parseColor("#0099FF"));
			readMap.setBackgroundResource(R.drawable.a_shape_bluebutton_normal);
			readMap.setTextColor(Color.parseColor("#FFFFFF"));
			setTabSeclection(0);
			break;
		case R.id.read_map:
			readOnlineRead.setBackgroundResource(R.drawable.a_shape_bluebutton_normal);
			readOnlineRead.setTextColor(Color.parseColor("#FFFFFF"));
			readMap.setBackgroundResource(R.drawable.a_shap_button_onclick);
			readMap.setTextColor(Color.parseColor("#0099FF"));
			setTabSeclection(1);		
			break;
		default:
			break;
		}
		
	}
	
	/*�������õ�i����������ѡ�е�Tabҳ*/
	private void setTabSeclection(int i) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();	
		hideFragment(transaction);
		switch (i) {
		case 0:
			if(readingFragment == null){
				readingFragment = new ReadingFragment();
				transaction.add(R.id.read_content, readingFragment);
			}else{
				transaction.show(readingFragment);
			}
			break;
		case 1:
			if(mapFragment == null){
				mapFragment = new MapFragment();
				transaction.add(R.id.read_content, mapFragment);
			}else{
				transaction.show(mapFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/*�����е�Fragment������Ϊ����״̬*/
	private void hideFragment(FragmentTransaction transaction) {
		if(readingFragment != null){
			transaction.hide(readingFragment);
		}
		if(mapFragment != null){
			transaction.hide(mapFragment);
		}		
	}

}
