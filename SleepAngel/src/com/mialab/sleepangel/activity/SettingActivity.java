package com.mialab.sleepangel.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 设置界面
 * 
 * @author Wesly
 *
 */
public class SettingActivity extends Activity {

	ImageView mBack;
	ImageView mivQQ;
	ImageView mivWeChat;
	ImageView mivWeiBo;
	TextView mtvAuto;
	ToggleButton mtbWifi;
	TextView mtvCheckUpdate;
	ImageView mivNew;

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
		setContentView(R.layout.activity_setting);

		initView();
		initData();

	}

	private void initView() {

		mBack = (ImageView) findViewById(R.id.ib_back);
		mivQQ = (ImageView) findViewById(R.id.iv_feedback_qq);
		mivWeChat = (ImageView) findViewById(R.id.iv_feedback_wechat);
		mivWeiBo = (ImageView) findViewById(R.id.iv_feedback_weibo);
		mtvAuto = (TextView) findViewById(R.id.tv_wifi_auto);
		mtbWifi = (ToggleButton) findViewById(R.id.tb_wifi);
		mtvCheckUpdate = (TextView) findViewById(R.id.tv_check_for_update);
		mivNew = (ImageView) findViewById(R.id.ib_has_new);

	}

	private void initData() {

		boolean wifiAutoUpdate = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
		mtbWifi.setChecked(wifiAutoUpdate);

		boolean hasNew = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
		if (hasNew) {
			mivNew.setVisibility(View.VISIBLE);
		} else {
			mivNew.setVisibility(View.INVISIBLE);
		}

		mtvCheckUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkForUpdate();

			}
		});

		mtbWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, true);
				} else {
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
				}

			}
		});

		mtvAuto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean AutoUpdate = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
				if (AutoUpdate == false) {
					mtbWifi.setChecked(true);
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, true);
				} else {
					mtbWifi.setChecked(false);
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
				}
			}
		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mivQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url11 = "mqqwpa://im/chat?chat_type=wpa&uin=652048614&version=1";
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
				} catch (Exception e) {
					Toast.makeText(SettingActivity.this, "你的手机没有安装QQ,请使用微博反馈", Toast.LENGTH_SHORT).show();
				}

			}
		});

		mivWeChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(SettingActivity.this, "开发中", Toast.LENGTH_SHORT).show();

			}
		});

		mivWeiBo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://weibo.com/u/5104222748"));
				startActivity(intent);
			}
		});

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
					showUpdateDialog(SettingActivity.this, mVersionName, mDescription, mDownloadUrl);
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, true);
					mivNew.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(SettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(SettingActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
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

					PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
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
			Toast.makeText(SettingActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
		}

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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, true);
	}

}
