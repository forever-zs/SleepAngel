package com.mialab.sleepangel.base;

import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.MainActivity;

import android.app.Activity;
import android.view.View;

/**
 * CommunityPagerµÄviewPager»ùÀà
 * @author Wesly
 *
 */
public abstract class CommunityBasePager {
	
	public MainActivity mainui;
	public View mRootView;
	
	public CommunityBasePager(Activity aty){
		mainui=(MainActivity) aty;
		initView();
	}

	public abstract void initView();
	
	public void initData(){
		
		
	}
}
