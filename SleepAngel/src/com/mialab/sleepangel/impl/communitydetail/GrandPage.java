package com.mialab.sleepangel.impl.communitydetail;

import com.mialab.sleepangel.R;
import com.mialab.sleepangel.base.CommunityBasePager;

import android.app.Activity;
import android.view.View;

/**
 * �û���̬
 * @author Wesly
 *
 */
public class GrandPage extends CommunityBasePager {

	public GrandPage(Activity aty) {
		super(aty);
	
	}
	
	@Override
	public void initView() {
		
		mRootView = View.inflate(mainui, R.layout.pager_grand, null);
		
		
	}
	
	@Override
	public void initData() {
		
	}
	
	
}
