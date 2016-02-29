package com.mialab.sleepangel.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mialab.sleepangel.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新，加载更多的ListView
 * 
 * @author Wesly
 *
 */
public class RefreshListView extends ListView {

	
	public static final int MAXPADDING = 20;
	public boolean mIsFirstVisibleItem;//标记是否滑动到ListView顶端
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新
	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
	private int startY = -1;// 滑动起点的y坐标

	private boolean isLoadingMore;

	public View mHeaderView;
	public TextView mAction;
	public TextView mLastRefreshTime;
	public ImageView ivArr;
	public ProgressBar pbProgress;
	int mHeadViewHeight;
	private Animation animUp;
	private RotateAnimation animDown;

	private View mFootView;
	private int mFootViewHeight;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeadView();
		initFootView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadView();
		initFootView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeadView();
		initFootView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mIsFirstVisibleItem) {
				startY = (int) ev.getRawY();
			}
			break;
		case MotionEvent.ACTION_MOVE:

			if (startY == -1&&mIsFirstVisibleItem) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;// 偏移量

			if (dy > 0 && mIsFirstVisibleItem) {// 只有下拉并且当前是第一个item,才允许下拉
				int padding = dy - mHeadViewHeight;// 计算padding
				if(padding<1){
					mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding
				}else{
					mHeaderView.setPadding(0, (int) Math.pow(padding, 0.8), 0, 0);// 设置当前padding
					System.out.println((int) Math.pow(padding, 0.8));
				}
				

				if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
					mCurrrentState = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
					mCurrrentState = STATE_PULL_REFRESH;
					refreshState();
				}

				//return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置

			if (mCurrrentState == STATE_RELEASE_REFRESH) {
				mCurrrentState = STATE_REFRESHING;// 正在刷新
				mHeaderView.setPadding(0, 0, 0, 0);// 显示
				refreshState();
			} else if (mCurrrentState == STATE_PULL_REFRESH) {
				mHeaderView.setPadding(0, -mHeadViewHeight, 0, 0);// 隐藏
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);

	}

	private void refreshState() {
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			mAction.setText("下拉刷新");
			ivArr.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArr.startAnimation(animDown);
			break;
		case STATE_RELEASE_REFRESH:
			mAction.setText("松开刷新");
			ivArr.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArr.startAnimation(animUp);
			break;
		case STATE_REFRESHING:
			mAction.setText("正在刷新...");
			ivArr.clearAnimation();// 必须先清除动画,才能隐藏
			ivArr.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);

			if (mListener != null) {
				mListener.onRefresh();
			}
			break;

		default:
			break;
		}

	}

	private void initHeadView() {

		mHeaderView = View.inflate(getContext(), R.layout.refresh_head, null);
		this.addHeaderView(mHeaderView);

		mAction = (TextView) mHeaderView.findViewById(R.id.tv_action);
		mLastRefreshTime = (TextView) mHeaderView.findViewById(R.id.tv_last_refresh_time);
		ivArr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

		mHeaderView.measure(0, 0);
		mHeadViewHeight = mHeaderView.getMeasuredHeight();
		mHeaderView.setPadding(0, -mHeadViewHeight, 0, 0);

		mLastRefreshTime.setText("最后刷新时间" + getCurrentTime());
		initArrAnim();

	}

	private void initFootView() {
		mFootView = View.inflate(getContext(), R.layout.refresh_foot, null);
		this.addFooterView(mFootView);

		mFootView.measure(0, 0);
		mFootViewHeight = mFootView.getMeasuredHeight();

		mFootView.setPadding(0, -mFootViewHeight, 0, 0);// 隐藏

		this.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {

					if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
						mFootView.setPadding(0, 0, 0, 0);// 显示
						setSelection(getCount() - 1);// 改变listview显示位置

						isLoadingMore = true;

						if (mListener != null) {
							mListener.onLoadMore();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem==0){
					mIsFirstVisibleItem=true;
				}else{
					mIsFirstVisibleItem=false;
				}
			}
		});
	}

	private void initArrAnim() {
		// 箭头向上动画
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		// 箭头向下动画
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}

	private String getCurrentTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return sDateFormat.format(new Date());
	}

	OnRefreshListener mListener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();// 加载下一页数据
	}

	public void onRefreshComplete(boolean success) {

		if (isLoadingMore) {// 正在加载更多...
			mFootView.setPadding(0, -mFootViewHeight, 0, 0);// 隐藏脚布局
			isLoadingMore = false;
		} else {

			mCurrrentState = STATE_PULL_REFRESH;
			mAction.setText("下拉刷新");
			ivArr.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mHeaderView.setPadding(0, -mHeadViewHeight, 0, 0);// 隐藏

			if (success) {
				mLastRefreshTime.setText("最后刷新时间:" + getCurrentTime());
			}
		}
	}
}
