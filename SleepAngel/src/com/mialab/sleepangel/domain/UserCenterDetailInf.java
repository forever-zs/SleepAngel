package com.mialab.sleepangel.domain;

/**
 * UserCenterDetailÊý¾Ýjavabean
 * @author Wesly
 *
 */
public class UserCenterDetailInf {
	private String mUserName;
	private Tip mTip;

	public String getmUserName() {
		return mUserName;
	}

	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public Tip getmTips() {
		return mTip;
	}

	public void setmTips(Tip mTips) {
		this.mTip = mTips;
	}

	public class Tip {
		private int id;
		private String mMessage;
		private String mAuthor;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getmMessage() {
			return mMessage;
		}

		public void setmMessage(String mMessage) {
			this.mMessage = mMessage;
		}

		public String getmAuthor() {
			return mAuthor;
		}

		public void setmAuthor(String mAuthor) {
			this.mAuthor = mAuthor;
		}

	}
}
