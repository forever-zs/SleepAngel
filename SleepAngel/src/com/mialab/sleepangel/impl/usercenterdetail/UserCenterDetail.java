package com.mialab.sleepangel.impl.usercenterdetail;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.sleepangel.R;
import com.mialab.sleepangel.activity.LoginActivity;
import com.mialab.sleepangel.activity.MainActivity;
import com.mialab.sleepangel.activity.UserInfActivity;
import com.mialab.sleepangel.domain.UserCenterDetailInf;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UserCenterDetailµÄflcontentÄÚÈÝ
 * @author Wesly
 *
 */
public class UserCenterDetail {

	public View mRootView;
	MainActivity mainActivity;
	Button mUserName;
	Button mExit;
	String mToken;
	UserCenterDetailInf userCenterInf;
	TextView mMessage;
	TextView mAuthor;

	public UserCenterDetail(Activity aty) {
		mainActivity = (MainActivity) aty;
		initView();
	}

	private void initView() {
		mRootView = View.inflate(mainActivity, R.layout.detail_usercenter, null);
		mUserName = (Button) mRootView.findViewById(R.id.btn_username);
		mExit = (Button) mRootView.findViewById(R.id.btn_exit);
		mMessage = (TextView) mRootView.findViewById(R.id.tv_message);
		mAuthor = (TextView) mRootView.findViewById(R.id.tv_author);

	}

	public void initData() {

		mToken = PrefUtils.getString(mainActivity, GlobalContants.TOKEN, "");

		String cache = PrefUtils.getString(mainActivity, GlobalContants.USER_CENTER_DETAIL_URL, null);
		if (!TextUtils.isEmpty(cache)) {
			paraseData(cache);
		}

		getDataFromServer();

		if (mToken.equals("")) {
			mExit.setVisibility(View.INVISIBLE);
			mUserName.setText("µã»÷µÇÂ½");
		}

		mUserName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mToken.equals("")) {
					mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
				} else {
					mainActivity.startActivity(new Intent(mainActivity, UserInfActivity.class));
				}

			}
		});

		mExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PrefUtils.setString(mainActivity, GlobalContants.TOKEN, "");
				mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
				mainActivity.finish();

			}
		});
	}

	private void getDataFromServer() {

		HttpUtils utils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("UUID", mToken);

		utils.send(HttpMethod.POST, GlobalContants.USER_CENTER_DETAIL_URL, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String result = responseInfo.result;
				if (result != null) {
					PrefUtils.setString(mainActivity, GlobalContants.USER_CENTER_DETAIL_URL, result);
					paraseData(result);
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mainActivity, "Çë¼ì²éÍøÂç", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void paraseData(String result) {

		Gson gson = new Gson();
		userCenterInf = gson.fromJson(result, UserCenterDetailInf.class);

		if (userCenterInf != null) {
			String UserName = userCenterInf.getmUserName();
			if (userCenterInf.getmTips() != null) {
				String Message = userCenterInf.getmTips().getmMessage();
				String Author = userCenterInf.getmTips().getmAuthor();
				if (!TextUtils.isEmpty(UserName)) {
					mUserName.setText(UserName);
				}
				if (Message.length() > 10) {
					mMessage.setText("    "+Message);
				} else {
					mMessage.setText(Message);
				}

				mAuthor.setText("-" + Author);
			}

		}

	}

}
