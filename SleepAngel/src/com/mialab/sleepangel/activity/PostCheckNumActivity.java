package com.mialab.sleepangel.activity;

import com.mialab.sleepangel.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 提交验证码
 * 
 * @author Wesly
 *
 */
public class PostCheckNumActivity extends Activity {

	String mPhoneNum;
	TextView mINF;
	EditText mCheckNum;
	ImageButton mBack;
	Button mReCheck;
	Button mCommit;
	int i;
	private static final int UPDATE_TIME = 1;
	private static final int RECHECK = 2;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == UPDATE_TIME) {
				mReCheck.setText("重新发送(" + i + ")");
			} else if (msg.what == RECHECK) {
				mReCheck.setText("重新发送");
				mReCheck.setEnabled(true);
			} else {
				int event = msg.arg1;
				int result = msg.arg2;

				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					if (result == SMSSDK.RESULT_COMPLETE) {
						Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(PostCheckNumActivity.this, PostUserInfActivity.class);
						intent.putExtra("PhoneNum", mPhoneNum);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
					}

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {// 正在发送验证码
					// Toast.makeText(getApplicationContext(), "正在发送验证码",
					// Toast.LENGTH_SHORT).show();
				}

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_check_num);

		initView();
		initData();

	}

	private void initView() {
		mINF = (TextView) findViewById(R.id.tv_inf);
		mCheckNum = (EditText) findViewById(R.id.et_checknum);
		mReCheck = (Button) findViewById(R.id.btn_recheck);
		mCommit = (Button) findViewById(R.id.btn_commit);
		mBack = (ImageButton) findViewById(R.id.ib_back);
	}

	private void initData() {
		Intent intent = getIntent();
		mPhoneNum = intent.getStringExtra("PhoneNum");
		mINF.setText("已向手机" + mPhoneNum + "发送验证码");
		mReCheck.setText("重新发送60");

		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				mHandler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		SMSSDK.getVerificationCode("86", mPhoneNum);

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (i = 60; i > 0; i--) {
					mHandler.sendEmptyMessage(UPDATE_TIME);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mHandler.sendEmptyMessage(RECHECK);

			}
		}).start();

		mReCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCheckNum.setText("");
				SMSSDK.getVerificationCode("86", mPhoneNum);
				mReCheck.setEnabled(false);
				new Thread(new Runnable() {

					@Override
					public void run() {
						for (i = 60; i > 0; i--) {
							mHandler.sendEmptyMessage(UPDATE_TIME);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						mHandler.sendEmptyMessage(RECHECK);

					}
				}).start();
			}
		});

		mCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SMSSDK.submitVerificationCode("86", mPhoneNum, mCheckNum.getText().toString());
			}
		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
}
