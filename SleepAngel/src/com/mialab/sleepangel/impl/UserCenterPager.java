package com.mialab.sleepangel.impl;

import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.impl.usercenterdetail.UserCenterDetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

/**
 * 用户中心实现
 * @author Wesly
 *
 */
public class UserCenterPager extends BasePager {

	public UserCenterPager(Activity activity) {
		super(activity);

	}

	@Override
	public void initData() {
		
		super.initData();
		
		System.out.println("usercenter initdata...");
		mtvTitle.setText("我的星空");

		
		UserCenterDetail userCenterDetail = new UserCenterDetail(mainActivity);
		userCenterDetail.initData();

		// 向FrameLayout中动态添加布局
		flContent.addView(userCenterDetail.mRootView);

	}

}
