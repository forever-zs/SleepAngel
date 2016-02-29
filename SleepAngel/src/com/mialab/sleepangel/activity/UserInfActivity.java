package com.mialab.sleepangel.activity;

import com.mialab.sleepangel.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 用户资料页
 * @author Wesly
 *
 */
public class UserInfActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_inf);
	}
}
