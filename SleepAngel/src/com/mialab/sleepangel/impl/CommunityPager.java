package com.mialab.sleepangel.impl;

import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.impl.communitydetail.CommunityDetail;

import android.app.Activity;

/**
 * 社区实现
 * @author Wesly
 *
 */
public class CommunityPager extends BasePager {

	public CommunityPager(Activity activity) {
		super(activity);

	}

	@Override
	public void initData() {
		
		super.initData();
		
		System.out.println("community initdata...");
		
		CommunityDetail newsdetailpager = new CommunityDetail(mainActivity);
		newsdetailpager.initData();
		
		
		// 向FrameLayout中动态添加布局
		flContent.removeAllViews();
		flContent.addView(newsdetailpager.mRootView);
	}

}
