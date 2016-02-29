package com.mialab.sleepangel;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * ActivityÊÕ¼¯Æ÷
 * @author Wesly
 *
 */
public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}