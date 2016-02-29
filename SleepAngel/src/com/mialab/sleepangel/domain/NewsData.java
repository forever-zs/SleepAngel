package com.mialab.sleepangel.domain;

import java.util.ArrayList;

/**
 * 新闻数据javabean
 * @author Wesly
 *
 */
public class NewsData {
	
	public String moreURL;
	public ArrayList<TopNewsData> topNews;
	public ArrayList<SimpleNewsData> simpleNews;
	
	public class TopNewsData{
		public int id;
		public String title;
		public String pubTime;
		public String BodyURL;
		public String imageURL;
	}
	
	public class SimpleNewsData{
		public int id;
		public String title;
		public String pubTime;
		public String BodyURL;
		public String imageURL;
	}
}
