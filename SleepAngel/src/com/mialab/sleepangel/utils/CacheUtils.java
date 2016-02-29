package com.mialab.sleepangel.utils;

import android.content.Context;

/**
 * ���湤����
 * 
 * @author Wesly
 *
 */
public class CacheUtils {

	//���û���
	public static void setCache(Context ctx, String url, String json) {
		PrefUtils.setString(ctx, url, json);
	}

	//��ȡ����
	public static String getCache(Context ctx, String url, String defValue) {
		return PrefUtils.getString(ctx, url, defValue);
	}
}
