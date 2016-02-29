package com.mialab.sleepangel.base;

import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.MainActivity;
import com.mialab.sleepangel.activity.SettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * MainActivityµÄViewPager»ùÀà
 * @author Wesly
 *
 */
public abstract class BasePager {

	public MainActivity mainActivity;
	public View mRootView;
	public TextView mtvTitle;
	public ImageButton mibSetting;
	public RelativeLayout mrlTitle;
	public FrameLayout flContent;
	
	public BasePager(Activity activity) {
		mainActivity = (MainActivity) activity;
		initViews();
	}
	
	public void initViews(){
		
		mRootView = View.inflate(mainActivity, R.layout.pager_base, null);
		mtvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		mrlTitle = (RelativeLayout) mRootView.findViewById(R.id.rl_title);
		mibSetting = (ImageButton) mRootView.findViewById(R.id.ib_setting);
	}
	
	public void initData(){
		
		mibSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.startActivity(new Intent(mainActivity,SettingActivity.class));
			}
		});
	}

	
}
