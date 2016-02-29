package com.mialab.sleepangel.impl;

import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.impl.homedetail.HomeDetail;

import android.app.Activity;

/**
 * 首页实现
 * @author Wesly
 *
 */
public class HomePager extends BasePager {
	
	public HomePager(Activity activity) {
		super(activity);
		
	}

	@Override
	public void initData() {
		
		super.initData();
		
		System.out.println("home initdata...");
		mtvTitle.setText("首页");
		
		HomeDetail homeDetail = new HomeDetail(mainActivity);
		homeDetail.initData();
		
		

		// 向FrameLayout中动态添加布局
		flContent.addView(homeDetail.mRootView);
	}

}
