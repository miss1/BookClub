package com.shizhan.bookclub.app.fragment;

import com.shizhan.bookclub.app.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mapLayout = inflater.inflate(R.layout.map_layout, container, false);
		return mapLayout;
	}

}
