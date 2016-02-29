package com.mialab.sleepangel.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * �Զ���TopNewsViewPager
 * @author Wesly
 *
 */
public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public TopNewsViewPager(Context context) {
		super(context);
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);

			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();

			break;

		case MotionEvent.ACTION_MOVE:

			endX = (int) ev.getRawX();
			endY = (int) ev.getRawY();

			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// ���һ���
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {// ���»���

				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
