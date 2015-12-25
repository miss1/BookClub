package com.shizhan.bookclub.app.mylistview;

import com.shizhan.bookclub.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReFlashListView extends ListView implements OnScrollListener {
	
	View header;// ���������ļ���
	int headerHeight;// ���������ļ��ĸ߶ȣ�
	int firstVisibleItem;// ��ǰ��һ���ɼ���item��λ�ã�
	int scrollState;// ListView ��ǰ����״̬��
	boolean isRemark;// ��ǣ���ǰ����ListView������µģ�
	int startY;// ����ʱ��Yֵ��

	int state;// ��ǰ��״̬��
	final int NONE = 0;// ����״̬��
	final int PULL = 1;// ��ʾ����״̬��
	final int RELESE = 2;// ��ʾ�ͷ�״̬��
	final int REFLASHING = 3;// ˢ��״̬��
	IReflashListener iReflashListener;//ˢ�����ݵĽӿ�
	
	public ReFlashListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public ReFlashListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ReFlashListView(Context context) {
		super(context);
		initView(context);
	}
	
	/*��ʼ�����棬��Ӷ��������ļ��� ListView*/
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.listheader_layout, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.setOnScrollListener(this);
	}

	/*֪ͨ�����֣�ռ�õĿ���*/
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if(p == null){
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if(tempHeight > 0){
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
		}else{
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	
	/*����header ���� �ϱ߾�*/
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(firstVisibleItem == 0){
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if(state == RELESE){
				state = REFLASHING;
				// �����������ݣ�
				reflashViewByState();
				iReflashListener.onReflash();
			}else if(state == PULL){
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/*�ж��ƶ����̲���*/
	private void onMove(MotionEvent ev) {
		if(!isRemark){
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - headerHeight;
		switch (state) {
		case NONE:
			if(space > 0){
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL:
			topPadding(topPadding);
			if (space > headerHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewByState();
			}
			break;
		case RELESE:
			topPadding(topPadding);
			if (space < headerHeight + 30) {
				state = PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		default:
			break;
		}
	}
	
	/*���ݵ�ǰ״̬���ı������ʾ*/
	private void reflashViewByState() {
		TextView tip = (TextView) header.findViewById(R.id.tips);
		ImageView arrow = (ImageView) findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			arrow.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("��������ˢ�£�");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("�ɿ�����ˢ�£�");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case REFLASHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("����ˢ��...");
			arrow.clearAnimation();
			break;
		}
	}
	
	/*��ȡ������*/
	public void reflashComplete(){
		state = NONE;
		isRemark = false;
		reflashViewByState();
	}
	
	public void setInterface(IReflashListener iReflashListener){
		this.iReflashListener = iReflashListener;
	}

	/*ˢ�����ݽӿ�*/
	public interface IReflashListener{
		public void onReflash();
	}

}
