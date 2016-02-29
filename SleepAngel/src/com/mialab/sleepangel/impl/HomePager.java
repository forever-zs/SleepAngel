package com.mialab.sleepangel.impl;

import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.impl.homedetail.HomeDetail;

import android.app.Activity;

/**
 * ��ҳʵ��
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
		mtvTitle.setText("��ҳ");
		
		HomeDetail homeDetail = new HomeDetail(mainActivity);
		homeDetail.initData();
		
		

		// ��FrameLayout�ж�̬��Ӳ���
		flContent.addView(homeDetail.mRootView);
	}

}
