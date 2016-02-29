package com.mialab.sleepangel.activity;


import com.mialab.sleepangel.R;
import com.mialab.sleepangel.R.id;
import com.mialab.sleepangel.R.layout;
import com.mialab.sleepangel.global.GlobalContants;
import com.mialab.sleepangel.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * …¡∆¡“≥
 * @author Wesly
 *
 */
public class SplashActivity extends Activity {

	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);

		startAnim();
	}

	private void startAnim() {
		AnimationSet set = new AnimationSet(false);

		ScaleAnimation scale = new ScaleAnimation((float) 0.95, 1, (float) 0.95, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(500);
		scale.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation((float) 0.5, 1);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);

		set.addAnimation(scale);
		set.addAnimation(alpha);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				jumpToNextPage();
				finish();
			}

		});
		
		rl_root.startAnimation(set);
		
	}

	private void jumpToNextPage() {
		boolean userGuide = PrefUtils.getBoolean(this, GlobalContants.USER_GUIDE_PREF, false);
		Intent intent = new Intent();
		if(!userGuide){
			intent.setClass(SplashActivity.this,UserGuideActivity.class);
		}else{
			intent.setClass(SplashActivity.this, MainActivity.class);
		}
		
		startActivity(intent);
	}
}
