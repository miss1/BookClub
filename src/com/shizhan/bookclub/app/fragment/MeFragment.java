package com.shizhan.bookclub.app.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.shizhan.bookclub.app.ChangePasswordActivity;
import com.shizhan.bookclub.app.InfoEditeActivity;
import com.shizhan.bookclub.app.LoginActivity;
import com.shizhan.bookclub.app.PersonPostActivity;
import com.shizhan.bookclub.app.PostCollectActivity;
import com.shizhan.bookclub.app.R;
import com.shizhan.bookclub.app.adapter.InfoShowAdapter;
import com.shizhan.bookclub.app.model.Information;
import com.shizhan.bookclub.app.model.InformationShow;
import com.shizhan.bookclub.app.model.MyUsers;
import com.shizhan.bookclub.app.mylistview.ReFlashListView;
import com.shizhan.bookclub.app.mylistview.ReFlashListView.IReflashListener;
import com.shizhan.bookclub.app.util.ImageHeade;
import com.shizhan.bookclub.app.util.MyProgressBar;

@SuppressLint("NewApi")
public class MeFragment extends Fragment implements OnClickListener,IReflashListener{
	
	private ImageView personEdite;
	private ImageView personHead;
	private TextView personTalk;
	private TextView personName;
	private ReFlashListView listInformation;      //自定义的下拉刷新ListView
	private ListView meEditeList;
	
	/*网络连接上时ListView加载的适配器及内容*/
	private InfoShowAdapter adapter;                    
	private List<InformationShow> infolist = new ArrayList<InformationShow>();
	
	/*下拉选择框*/
	private PopupWindow popupWindow;
	private LayoutInflater layoutInflater;
	private View view;
	private List<String> grouplist;
	private ArrayAdapter<String> arrAdapter;
	private WindowManager windowManager;
	
	private ImageHeade imageHeade;             //下载头像并显示
	
	private ProgressBar progressBar;
	private MyProgressBar myProgressBar;        //ProgressBar
	
	private long runDate;                     //连接服务器到服务器返回数据之间所隔得时间
	
	public static final int CHOOSE_PHOTO = 1;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		grouplist = new ArrayList<String>();
		grouplist.add("编辑资料");
		grouplist.add("我的收藏");
		grouplist.add("修改密码");
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
		personHead = (ImageView) meLayout.findViewById(R.id.person_head);
		listInformation = (ReFlashListView) meLayout.findViewById(R.id.list_information);
		
		myProgressBar = new MyProgressBar();
		progressBar = myProgressBar.createMyProgressBar(getActivity(), null);
		listInformation.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		adapter = new InfoShowAdapter(getActivity(), infolist);
		listInformation.setAdapter(adapter);
		initInfo();
		
		listInformation.setInterface(this);
		personEdite.setOnClickListener(this);
		personTalk.setOnClickListener(this);
		personHead.setOnClickListener(this);
		return meLayout;
	}

	//初始化信息
	private void initInfo() {
		Date startDate = new Date(System.currentTimeMillis());           //本段程序运行的起始时间
		
		MyUsers user = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
		
		if(user.getImageUrl() != null){
			imageHeade = new ImageHeade(user.getImageUrl(), personHead);         //下载服务器上的头像并显示
			imageHeade.setImageHead();
		}
		                                            
		BmobQuery<Information> query = new BmobQuery<Information>();
		query.addWhereEqualTo("user", user);
		query.findObjects(getActivity(), new FindListener<Information>() {
			
			@Override
			public void onSuccess(List<Information> arg0) {
				infolist.clear();
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
				adapter.notifyDataSetChanged();  //数据改变，动态更新列表
				listInformation.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				listInformation.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_LONG).show();
			}
		});
		
		Date endDate = new Date(System.currentTimeMillis());             //本段程序运行的结束时间
		runDate = endDate.getTime() - startDate.getTime();
	}

	//点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_edite:      //点击图标，显示下拉选择框
			showWindow(v);
			break;
		case R.id.person_talk:         //点击TextView，跳转到我的帖子界面
			PersonPostActivity.actionStart(getActivity(), BmobUser.getCurrentUser(getActivity(), MyUsers.class));
			break;
		case R.id.person_head:         //点击头像，获取本地图片更换头像并上传到服务器
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;
		default:
			break;
		}
		
	}
	
	/*下拉刷新*/
	@Override
	public void onReflash() {
		Handler handle = new Handler();
		if(runDate > 2000){
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					initInfo();
					listInformation.reflashComplete();
				}
			}, runDate);
		}else{
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					initInfo();
					listInformation.reflashComplete();
				}
			}, 2000);
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
		
		//popupWindow出现时其他地方变暗
		WindowManager.LayoutParams param = getActivity().getWindow().getAttributes();
		param.alpha=0.5f;
		getActivity().getWindow().setAttributes(param);
		
		//当popupWindow消失时，将变暗的地方变回正常的样子
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
				params.alpha=1f;
				getActivity().getWindow().setAttributes(params);
				
			}
		});
		
		//点击事件
		meEditeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:                     //跳转到编辑资料页面
					Intent intent = new Intent(getActivity(), InfoEditeActivity.class);
					startActivity(intent);
					popupWindow.dismiss();
					break;
				case 1:                     //跳转到我的收藏界面，显示我收藏的帖子
					PostCollectActivity.actionStart(getActivity());
					popupWindow.dismiss();
					break;
				case 2:                     //修改密码,跳转到修改密码界面
					ChangePasswordActivity.actionStart(getActivity());
					popupWindow.dismiss();
					break;
				case 3:                     //退出当前账号,跳转到登陆界面
					BmobUser.logOut(getActivity());
					Intent intentl = new Intent(getActivity(), LoginActivity.class);
					startActivity(intentl);
					popupWindow.dismiss();
					getActivity().finish();
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity();
		if(resultCode == Activity.RESULT_OK){
			if(Build.VERSION.SDK_INT >= 19){                           //判断手机系统版本号
				handleImageOnKitKat(data);                             //4.4及以上版本使用这个方法处理图片
			}else{
				handleImageBeforeKitKat(data);                         //4.4及以下版本使用这个方法处理图片
			}
		}
	}

	//4.4及以上版本使用这个方法处理图片
	private void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if(DocumentsContract.isDocumentUri(getActivity(), uri)){
			//如果是Document类型的Uri，则通过document id处理
			String docId = DocumentsContract.getDocumentId(uri);
			if("com.android.providers.media.documents".equals(uri.getAuthority())){
				String id = docId.split(":")[1];
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			}else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			}
		}else if("content".equalsIgnoreCase(uri.getScheme())){
			//如果不是Document类型的Uri，则使用普通方式处理
			imagePath = getImagePath(uri, null);
		}
		displayImage(imagePath);
		uploadImage(imagePath);
	}

	//4.4及以下版本使用这个方法处理图片
	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
		uploadImage(imagePath);
	}
	
	private String getImagePath(Uri uri, String selection) {
		String path = null;
		Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}
	
	ProgressDialog dialog = null;
	
	//将图片上传到服务器
	private void uploadImage(String imagePath){
		dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle("上传中");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final MyUsers user = BmobUser.getCurrentUser(getActivity(), MyUsers.class);
		final BmobFile bmobFile = new BmobFile(new File(imagePath));
		bmobFile.uploadblock(getActivity(), new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				user.setImageUrl(bmobFile.getFileUrl(getActivity()));
				user.update(getActivity(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						dialog.dismiss();
						Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();						
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						dialog.dismiss();
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();					
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				dialog.dismiss();
				Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();				
			}
		});
	}

	//将图片直接显示出来
	private void displayImage(String imagePath) {
		System.out.println("path:"+imagePath);
		if(imagePath != null){
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			personHead.setImageBitmap(bitmap);
		}else{
			Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
		}		
	}

}
