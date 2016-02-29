package com.mialab.sleepangel.impl;

import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.impl.communitydetail.CommunityDetail;

import android.app.Activity;

/**
 * ����ʵ��
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
		
		
		// ��FrameLayout�ж�̬��Ӳ���
		flContent.removeAllViews();
		flContent.addView(newsdetailpager.mRootView);
	}

}
