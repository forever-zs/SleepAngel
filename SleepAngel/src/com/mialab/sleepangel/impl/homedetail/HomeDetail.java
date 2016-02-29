package com.mialab.sleepangel.impl.homedetail;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.MainActivity;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * HomeDetailµÄflcontentÄÚÈÝ
 * @author Wesly
 *
 */
public class HomeDetail {

	public View mRootView;
	MainActivity mainActivity;

	public HomeDetail(Activity aty) {
		mainActivity = (MainActivity) aty;
		initView();
		initData();
	}

	private void initView() {
		mRootView = View.inflate(mainActivity, R.layout.detail_home, null);
		

	}

	public void initData() {
		
	}

}
