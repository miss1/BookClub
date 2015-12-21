package com.shizhan.bookclub.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.shizhan.bookclub.app.InfoEditeActivity;
import com.shizhan.bookclub.app.InfoTalkActivity;
import com.shizhan.bookclub.app.LoginActivity;
import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.adapter.InfoShowAdapter;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.InformationShow;
import com.shizhan.bookclub.app.model.MyUsers;

public class MeFragment extends Fragment implements OnClickListener{
	
	private ImageView personEdite;
	private TextView personTalk;
	private TextView personName;
	private ListView listInformation;
	private ListView meEditeList;
	
	private InfoShowAdapter adapter;
	private List<InformationShow> infolist = new ArrayList<InformationShow>();
	
	private PopupWindow popupWindow;
	private LayoutInflater layoutInflater;
	private View view;
	private List<String> grouplist;
	private ArrayAdapter<String> arrAdapter;
	private WindowManager windowManager;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		initInfo();          //初始化个人信息
		grouplist = new ArrayList<String>();
		grouplist.add("编辑资料");
		grouplist.add("退       出");
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		arrAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, grouplist);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View meLayout = inflater.inflate(R.layout.me_layout, container, false);
		personEdite = (ImageView) meLayout.findViewById(R.id.person_edite);
		personTalk = (TextView) meLayout.findViewById(R.id.person_talk);
		personName = (TextView) meLayout.findViewById(R.id.person_name);
		listInformation = (ListView) meLayout.findViewById(R.id.list_information);
		personEdite.setOnClickListener(this);
		personTalk.setOnClickListener(this);
		return meLayout;
	}

	//初始化信息
	private void initInfo() {
		MyUsers user = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
		BmobQuery<Information> query = new BmobQuery<Information>();
		query.addWhereEqualTo("user", user);
		query.findObjects(getActivity(), new FindListener<Information>() {
			
			@Override
			public void onSuccess(List<Information> arg0) {
				InformationShow zhanghao = new InformationShow("账        号：", arg0.get(0).getZhanghao());
				infolist.add(zhanghao);
				InformationShow nicheng = new InformationShow("性        别：", arg0.get(0).getSex());
				infolist.add(nicheng);
				InformationShow age = new InformationShow("年        龄：", arg0.get(0).getAge());
				infolist.add(age);
				InformationShow city = new InformationShow("所在城市：", arg0.get(0).getCity());
				infolist.add(city);
				InformationShow geqian = new InformationShow("个性签名：", arg0.get(0).getGeqian());
				infolist.add(geqian);
				InformationShow lovebook = new InformationShow("喜欢的书：", arg0.get(0).getLovebook());
				infolist.add(lovebook);
				InformationShow loveauthor = new InformationShow("喜欢作家：", arg0.get(0).getLoveauthor());
				infolist.add(loveauthor);
				InformationShow bookstyle = new InformationShow("看书类型：", arg0.get(0).getBookstyle());
				infolist.add(bookstyle);
				personName.setText(arg0.get(0).getNicheng());
				adapter = new InfoShowAdapter(getActivity(), R.layout.me_layout_item, infolist);
				listInformation.setAdapter(adapter);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_edite:
			showWindow(v);
			break;
		case R.id.person_talk:
			Intent intentt = new Intent(getActivity(), InfoTalkActivity.class);
			startActivity(intentt);
			break;
		default:
			break;
		}
		
	}

	//显示PopupWindow
	private void showWindow(View v) {
		if(popupWindow == null){
			 view = layoutInflater.inflate(R.layout.me_layout_editelist, null);
			 meEditeList = (ListView) view.findViewById(R.id.me_editelist);
			 meEditeList.setAdapter(arrAdapter);
			 //创建一个PopupWindow对象
			 popupWindow=new PopupWindow(view, 200, 250);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int xPos = windowManager.getDefaultDisplay().getWidth()/2-popupWindow.getWidth()/2;
		popupWindow.showAsDropDown(v, xPos, 0);
		
		//点击事件
		meEditeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:                     //跳转到编辑资料页面
					Intent intent = new Intent(getActivity(), InfoEditeActivity.class);
					startActivity(intent);
					break;
				case 1:                     //退出当前账号,跳转到登陆界面
					BmobUser.logOut(getActivity());
					Intent intentl = new Intent(getActivity(), LoginActivity.class);
					startActivity(intentl);
					getActivity().finish();
					break;
				default:
					break;
				}
				
			}
		});
	}

}
