package com.mialab.sleepangel.impl.communitydetail;

import java.util.ArrayList;

import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.MainActivity;
import com.mialab.sleepangel.base.CommunityBasePager;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**
 * CommunityDetail的flcontent内容
 * @author Wesly
 *
 */
public class CommunityDetail implements OnPageChangeListener{
	
	public MainActivity mainui;
	public View mRootView;
	public ViewPager vpNewsDetail;
	public TabPageIndicator indicator;
	ArrayList<CommunityBasePager> pgList;
	
	public CommunityDetail(Activity activity) {
		mainui=(MainActivity) activity;
		initViews();
	}
	
	public void initViews(){
		mRootView = View.inflate(mainui,R.layout.detail_community, null);
		vpNewsDetail = (ViewPager) mRootView.findViewById(R.id.vp_news);
		indicator = (TabPageIndicator) mRootView.findViewById(R.id.indicator);
	}
	
	public void initData(){
		pgList= new ArrayList<CommunityBasePager>();
		pgList.add(new NewsPager(mainui));
		pgList.add(new GrandPage(mainui));
		
		vpNewsDetail.setAdapter(new vpNewsDetail());
		indicator.setViewPager(vpNewsDetail);
		indicator.setOnPageChangeListener(this);
		
	}
	
	class vpNewsDetail extends PagerAdapter{

		@Override
		public int getCount() {
			
			return pgList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
		
			return arg0==arg1;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			if(position==0){
				return "新闻";
			}
			else{
				return "广场";
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pgList.get(position).mRootView);
			pgList.get(position).initData();
			return pgList.get(position).mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
		
	}

	
	//indicator的OnPageChangeListener
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		//mainui.pagerArr.get(1)
		
	}
}
