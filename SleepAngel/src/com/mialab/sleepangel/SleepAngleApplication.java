package com.mialab.sleepangel;

import android.app.Application;
import cn.smssdk.SMSSDK;

/**
 * 重写Application，初始化环境
 * @author Wesly
 *
 */
public class SleepAngleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SMSSDK.initSDK(this, "fb622d09b37e", "9f434c5b4b46222552114698138c2671");
		
	}
}
