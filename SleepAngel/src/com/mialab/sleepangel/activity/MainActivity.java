package com.mialab.sleepangel.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.ActivityCollector;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.base.BasePager;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.impl.CommunityPager;
import com.mialab.sleepangel.impl.HomePager;
import com.mialab.sleepangel.impl.UserCenterPager;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 程序主界面
 * 
 * @author Wesly
 *
 */
public class MainActivity extends Activity {

	public ViewPager vpDetail;
	public RadioGroup rgButton;
	public RadioButton mrbHome;
	public RadioButton mrbCommunity;
	public RadioButton mrbUserCenter;
	public ArrayList<BasePager> pagerArr;
	public boolean mHomeFirstTime = true;
	public boolean mCommunityFirstTime = true;
	public boolean mUserCenterFirstTime = true;
	public int mHomeClickTimes = 0;
	public int mCommunityClickTimes = 0;
	public int mUserCenterClickTimes = 0;

	// 版本信息
	private int mVersionCode;
	private String mVersionName;
	private String mDescription;
	private String mDownloadUrl;
	private int mLocalVersionCode;
	ProgressDialog progressDialog;
	HttpHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ActivityCollector.addActivity(this);

		initView();
		initData();

	}

	private void initView() {
		vpDetail = (ViewPager) findViewById(R.id.vp_detail);
		rgButton = (RadioGroup) findViewById(R.id.rg_botton_tab);
		mrbHome = (RadioButton) findViewById(R.id.rb_home);
		mrbCommunity = (RadioButton) findViewById(R.id.rb_community);
		mrbUserCenter = (RadioButton) findViewById(R.id.rb_usercenter);

	}

	private void initData() {
		
		checkForUpdate();
		rgButton.check(R.id.rb_home);
		mHomeClickTimes = 1;
		pagerArr = new ArrayList<BasePager>();
		pagerArr.add(new HomePager(this));
		pagerArr.add(new CommunityPager(this));
		pagerArr.add(new UserCenterPager(this));

		mrbHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHomeClickTimes++;
				if (mHomeClickTimes >= 2) {
					pagerArr.get(0).initData();
				}

			}
		});

		mrbCommunity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCommunityClickTimes++;
				if (mCommunityClickTimes >= 2) {
					pagerArr.get(1).initData();
				}
			}
		});

		mrbUserCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUserCenterClickTimes++;
				if (mUserCenterClickTimes >= 2) {
					pagerArr.get(2).initData();
				}

			}
		});
		vpDetail.setAdapter(new vpDetailAdapter());
		vpDetail.setCurrentItem(0);
		pagerArr.get(0).initData();
		mHomeFirstTime = false;
		vpDetail.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0 && mHomeFirstTime) {
					pagerArr.get(0).initData();
					mHomeFirstTime = false;
				} else if (arg0 == 1 && mCommunityFirstTime) {
					pagerArr.get(1).initData();
					mCommunityFirstTime = false;
				} else if (arg0 == 2 && mUserCenterFirstTime) {
					pagerArr.get(2).initData();
					mUserCenterFirstTime = false;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		rgButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					vpDetail.setCurrentItem(0, false);

					mCommunityClickTimes = 0;
					mUserCenterClickTimes = 0;
					break;
				case R.id.rb_community:
					vpDetail.setCurrentItem(1, false);
					pagerArr.get(1).mrlTitle.setVisibility(View.GONE);

					mHomeClickTimes = 0;
					mUserCenterClickTimes = 0;
					break;
				case R.id.rb_usercenter:
					vpDetail.setCurrentItem(2, false);
					pagerArr.get(2).mibSetting.setVisibility(View.INVISIBLE);

					mHomeClickTimes = 0;
					mCommunityClickTimes = 0;
					break;
				}

			}
		});

	}

	class vpDetailAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return pagerArr.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager bp = pagerArr.get(position);
			container.addView((View) bp.mRootView);
			return bp.mRootView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	private void checkForUpdate() {
		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		utils.send(HttpMethod.GET, GlobalContants.CHECK_UPDATE_URL, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("result=" + result);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					mVersionCode = jsonObject.getInt("versionCode");
					mVersionName = jsonObject.getString("versionName");
					mDescription = jsonObject.getString("description");
					mDownloadUrl = jsonObject.getString("downloadUrl");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mLocalVersionCode = getVersionCode();

				if (mLocalVersionCode < mVersionCode) {
					showUpdateDialog(MainActivity.this, mVersionName, mDescription, mDownloadUrl);
					PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, true);
				} else {
					Toast.makeText(MainActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
					PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, false);
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(MainActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
			}

		});

	}

	private void showUpdateDialog(final Context ctx, String versionName, String description,
			final String mDownloadUrl) {

		AlertDialog alertDialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		alertDialog = builder.setTitle("版本升级提醒").setMessage("发现新版本：" + versionName + "\n" + "升级功能：\n" + description)
				.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startDownload(mDownloadUrl);

					}
				}).create();

		alertDialog.show();

	}

	private void startDownload(String downloadUrl) {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			HttpUtils utils = new HttpUtils();
			String path = Environment.getExternalStorageDirectory() + "/SleepAngel/update.apk";

			handler = utils.download(downloadUrl, path, new RequestCallBack<File>() {

				@Override
				public void onStart() {
					showProcessDialog(handler);
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					progressDialog.setProgress((int) (current * 100 / total));
					if (current == total) {
						progressDialog.dismiss();
					}
					System.out.println((float) current / total);
				}

				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {

					PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, false);
					// 跳转到系统安装界面
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");

					startActivityForResult(intent, 0);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			});
		} else {
			Toast.makeText(MainActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
		}

	}

	private int getVersionCode() {

		int versionCode = 1;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// 包名错误
			e.printStackTrace();
		}

		return versionCode;

	}

	private void showProcessDialog(final HttpHandler handler) {

		progressDialog = new ProgressDialog(this);

		progressDialog.setTitle("版本升级中");
		progressDialog.setIndeterminate(false);
		progressDialog.setButton("取消升级", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler.cancel();
			}
		});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				handler.cancel();
			}

		});
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);
		progressDialog.show();

	}

}
