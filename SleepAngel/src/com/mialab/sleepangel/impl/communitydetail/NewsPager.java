package com.mialab.sleepangel.impl.communitydetail;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.NewsBodyActivity;
import com.mialab.sleepangel.base.CommunityBasePager;
import com.mialab.sleepangel.domain.NewsData;
import com.mialab.sleepangel.domain.NewsData.SimpleNewsData;
import com.mialab.sleepangel.domain.NewsData.TopNewsData;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.CacheUtils;
import com.mialab.sleepangel.utils.PrefUtils;
import com.mialab.sleepangel.view.RefreshListView;
import com.viewpagerindicator.PageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 新闻页面
 * @author Wesly
 *
 */
public class NewsPager extends CommunityBasePager {

	public View topNewsHeadView;
	public TextView mTopTitle;
	public ViewPager vpTopNews;
	public PageIndicator indicator;
	public RefreshListView mLvnews;

	String newsURL;
	public NewsData mNewsData;
	String mMoreURL;
	ArrayList<TopNewsData> mTopNewsList;
	ArrayList<SimpleNewsData> mSimpleNewsList;
	LvnewsAdapter listNewsAdapter;

	Handler mHandler;

	public NewsPager(Activity aty) {
		super(aty);
		newsURL = GlobalContants.NEWS_URL;

	}

	@Override
	public void initView() {
		mRootView = View.inflate(mainui, R.layout.pager_news, null);
		topNewsHeadView = View.inflate(mainui, R.layout.top_news_headview, null);

		mTopTitle = (TextView) topNewsHeadView.findViewById(R.id.tv_title);
		vpTopNews = (ViewPager) topNewsHeadView.findViewById(R.id.vp_topnews);
		indicator = (PageIndicator) topNewsHeadView.findViewById(R.id.indicator);
		mLvnews = (RefreshListView) mRootView.findViewById(R.id.lv_news);
		mLvnews.addHeaderView(topNewsHeadView);

	}

	@Override
	public void initData() {

		String cache = CacheUtils.getCache(mainui, newsURL, null);
		if(!TextUtils.isEmpty(cache)){
			parseData(cache, false);
		}
		
		getDataFromServer();

		mLvnews.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();

			}

			@Override
			public void onLoadMore() {
				if (mMoreURL != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mainui, "最后一页了", Toast.LENGTH_SHORT).show();
				}

				mLvnews.onRefreshComplete(false);

			}

		});
		
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mTopTitle.setText(mTopNewsList.get(arg0).title);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		/*vpTopNews.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					break;

				case MotionEvent.ACTION_UP:
					int id = vpTopNews.getCurrentItem();
					String bodyURL = mTopNewsList.get(id).BodyURL;
					System.out.println("CLICKED");
					// 跳转NewsBodyActivity
					Intent intent = new Intent();
					intent.setClass(mainui, NewsBodyActivity.class);
					intent.putExtra("BodyURL", bodyURL);
					mainui.startActivity(intent);
					break;

				case MotionEvent.ACTION_CANCEL:

					break;

				default:
					break;
				}
				return true;
			}
		});*/

		mLvnews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println(position);
				SimpleNewsData newsData = mSimpleNewsList.get(position - 2);

				// 在本地保存已经阅读过的新闻id
				int newsid = newsData.id;
				String ids = PrefUtils.getString(mainui, GlobalContants.NEWS_READED, "");
				if (!ids.contains(newsid + "")) {
					ids = ids + newsid;
				}
				PrefUtils.setString(mainui, GlobalContants.NEWS_READED, ids);
				// 切换颜色
				TextView tv = (TextView) view.findViewById(R.id.tv_title);
				tv.setTextColor(Color.GRAY);

				// 跳转NewsBodyActivity
				Bundle bundle = new Bundle();
				bundle.putString("BodyURL", newsData.BodyURL);
				bundle.putString("newsTitle", newsData.title);
				Intent intent = new Intent();
				intent.setClass(mainui, NewsBodyActivity.class);
				intent.putExtra("newsInf", bundle);
				mainui.startActivity(intent);

			}
		});
	}

	/**
	 * TopNews适配器
	 * 
	 * @author Wesly
	 *
	 */
	class vpTopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public vpTopNewsAdapter() {
			utils = new BitmapUtils(mainui);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopNewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			TopNewsData topNewsData = mTopNewsList.get(position);
			String imageURL = topNewsData.imageURL;

			ImageView ivTopNews = new ImageView(mainui);
			ivTopNews.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片

			utils.display(ivTopNews, imageURL);
			
			ivTopNews.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int id = vpTopNews.getCurrentItem();
					// 跳转NewsBodyActivity
					Bundle bundle = new Bundle();
					bundle.putString("BodyURL", mTopNewsList.get(id).BodyURL);
					bundle.putString("newsTitle", mTopNewsList.get(id).title);
					Intent intent = new Intent();
					intent.setClass(mainui, NewsBodyActivity.class);
					intent.putExtra("newsInf", bundle);
					mainui.startActivity(intent);
					
				}
			});

			container.addView(ivTopNews);
			return ivTopNews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**
	 * ListView适配器
	 * 
	 * @author Wesly
	 *
	 */
	class LvnewsAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public LvnewsAdapter() {
			super();
			utils = new BitmapUtils(mainui);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mSimpleNewsList.size();
		}

		@Override
		public SimpleNewsData getItem(int position) {

			return mSimpleNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();

			if (convertView == null) {
				convertView = View.inflate(mainui, R.layout.list_news_item, null);

				holder.newspic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.newsTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.newsTime = (TextView) convertView.findViewById(R.id.tv_time);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SimpleNewsData newsData = getItem(position);
			String imageURL = newsData.imageURL;

			utils.display(holder.newspic, imageURL);
			holder.newsTitle.setText(newsData.title);
			holder.newsTime.setText(newsData.pubTime);

			String ids = PrefUtils.getString(mainui, GlobalContants.NEWS_READED, "");
			if (ids.contains(getItem(position).id + "")) {
				holder.newsTitle.setTextColor(Color.GRAY);
			}

			return convertView;
		}

	}

	class ViewHolder {
		public ImageView newspic;
		public TextView newsTitle;
		public TextView newsTime;
	}

	/**
	 * 获得服务器数据
	 */
	private void getDataFromServer() {
		HttpUtils hUtils = new HttpUtils();
		hUtils.send(HttpMethod.GET, newsURL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				CacheUtils.setCache(mainui, newsURL, result);
				parseData(result, false);

				mLvnews.onRefreshComplete(true);

			}

			@Override
			public void onFailure(HttpException error, String msg) {

				mLvnews.onRefreshComplete(false);
				Toast.makeText(mainui, "加载失败", Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});

	}

	/**
	 * 获得ListView下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils hUtils = new HttpUtils();
		hUtils.send(HttpMethod.GET, mMoreURL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;

				parseData(result, true);

				mLvnews.onRefreshComplete(true);

			}

			@Override
			public void onFailure(HttpException error, String msg) {

				mLvnews.onRefreshComplete(false);
				Toast.makeText(mainui, "网络异常", Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
		});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param result
	 */
	private void parseData(String result, boolean loadMore) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);

		String more = mNewsData.moreURL;
		if (!TextUtils.isEmpty(more)) {
			mMoreURL = GlobalContants.SERVER_URL + more;
		} else {
			mMoreURL = null;
		}

		if (!loadMore) {
			mTopNewsList = mNewsData.topNews;
			mSimpleNewsList = mNewsData.simpleNews;
			if (mNewsData != null) {
				vpTopNews.setAdapter(new vpTopNewsAdapter());
				indicator.setViewPager(vpTopNews);
				indicator.setCurrentItem(0);
				mTopTitle.setText(mTopNewsList.get(0).title);

				// TopNews轮播显示
				if (mHandler == null) {
					mHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							int currentItem = vpTopNews.getCurrentItem();
							if (currentItem < mTopNewsList.size() - 1) {
								currentItem++;
							} else {
								currentItem = 0;
							}

							indicator.setCurrentItem(currentItem);
							mTopTitle.setText(mTopNewsList.get(currentItem).title);
							mHandler.sendEmptyMessageDelayed(0, 5000);
						}
					};

					mHandler.sendEmptyMessageDelayed(0, 3000);
				}

				listNewsAdapter = new LvnewsAdapter();
				mLvnews.setAdapter(listNewsAdapter);
			}

		} else {
			ArrayList<SimpleNewsData> NewsList = mNewsData.simpleNews;
			mSimpleNewsList.addAll(NewsList);
			listNewsAdapter.notifyDataSetChanged();

		}

	}

}
