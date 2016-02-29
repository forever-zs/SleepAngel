package com.mialab.sleepangel.activity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.ActivityCollector;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆界面
 * @author Wesly
 *
 */
public class LoginActivity extends Activity {

	Button mRegister;
	TextView mForgetPass;
	EditText mTel;
	EditText mPassword;
	Button mLogin;
	ProgressBar mPBLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ActivityCollector.addActivity(this);

		initView();

		initData();
	}

	private void initView() {
		mRegister = (Button) findViewById(R.id.btn_register);
		mForgetPass = (TextView) findViewById(R.id.tv_forget_password);
		mTel = (EditText) findViewById(R.id.et_tel);
		mPassword = (EditText) findViewById(R.id.et_password);
		mLogin = (Button) findViewById(R.id.btn_login);
		mPBLogin = (ProgressBar) findViewById(R.id.pb_login);
	}

	public void initData() {
		mForgetPass.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mForgetPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.baidu.com"));
				startActivity(intent);
				
			}
		});

		mRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, CheckPhoneNumActivity.class));

			}
		});

		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isLegal(mTel.getText().toString(), mPassword.getText().toString())) {

					mPBLogin.setVisibility(View.VISIBLE);
					HttpUtils utils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("PhoneNum", mTel.getText().toString());
					params.addBodyParameter("Password", mPassword.getText().toString());

					utils.send(HttpMethod.POST, GlobalContants.LOGIN_URL, params, new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							mPBLogin.setVisibility(View.INVISIBLE);
							String back = responseInfo.result;

							if (back.equals("userName_Password_not_match")) {
								Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
							} else {
								System.out.println(back);
								PrefUtils.setString(getApplicationContext(), GlobalContants.TOKEN, back);
								startActivity(new Intent(LoginActivity.this, MainActivity.class));
								ActivityCollector.finishAll();
							}

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							mPBLogin.setVisibility(View.INVISIBLE);
							Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();

						}
					});
				} else {
					Toast.makeText(getApplicationContext(), "账号或密码不合法", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private boolean isLegal(String userName, String password) {

		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			if (password.matches("[a-zA-Z0-9_]{6,20}$")) {
				return true;
			}
			return false;
		} else {
			return false;
		}

	}
}
