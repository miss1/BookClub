package com.shizhan.bookclub.app.fragment;

import com.shizhan.bookclub.app.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReadFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View readLayout = inflater.inflate(R.layout.read_layout, container, false);
		return readLayout;
	}

}
