package com.mialab.sleepangel.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.ActivityCollector;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.domain.UserInf;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * �ύ�û�ע����Ϣ
 * @author Wesly
 *
 */
public class PostUserInfActivity extends Activity {

	String mPhoneNum;
	ImageButton mBack;
	EditText mNickNmae;
	EditText mPassword;
	Button mComplete;
	ProgressBar mPbCommit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_user_inf);

		initView();
		initData();
	}

	private void initView() {
		mBack = (ImageButton) findViewById(R.id.ib_back);
		mNickNmae = (EditText) findViewById(R.id.et_username);
		mPassword = (EditText) findViewById(R.id.et_password);
		mComplete = (Button) findViewById(R.id.btn_complete);
		mPbCommit = (ProgressBar) findViewById(R.id.pb_commit);

	}

	private void initData() {

		Intent intent = getIntent();
		mPhoneNum = intent.getStringExtra("PhoneNum");
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		mComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				commitUserInf(mNickNmae.getText().toString(), mPassword.getText().toString());

			}

		});

	}

	private void commitUserInf(String userName, String password) {
		if (isLegal(userName,password)) {
			mPbCommit.setVisibility(View.VISIBLE);
			HttpUtils utils = new HttpUtils();
			RequestParams params = new RequestParams();

			UserInf user = new UserInf();
			user.setmUserName(userName);
			user.setmPassword(password); 
			user.setmPhoneNum(mPhoneNum);

			Gson gson = new Gson();
			String jsonStr = gson.toJson(user);

			params.addBodyParameter("jsonStr", jsonStr);

			utils.send(HttpMethod.POST, GlobalContants.COMMIT_USER_INF_URL, params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					
					mPbCommit.setVisibility(View.INVISIBLE);
					
					String token = responseInfo.result;
					System.out.println(token);
					
					PrefUtils.setString(getApplicationContext(), GlobalContants.TOKEN, token);	
					
					Intent intent = new Intent(PostUserInfActivity.this, MainActivity.class);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_SHORT).show();
					ActivityCollector.finishAll();
					finish();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					mPbCommit.setVisibility(View.INVISIBLE);
					Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
				}

			});
		} else {
			Toast.makeText(PostUserInfActivity.this, "�ǳƻ������ʽ���Ϸ�", Toast.LENGTH_SHORT).show();
		}

	}

	private boolean isLegal(String userName,String password) {

		if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(password)){
			if(password.matches("[a-zA-Z0-9_]{6,20}$")){
				return true;
			}
			return false;
		}else{
			return false;
		}
		

	}

}
