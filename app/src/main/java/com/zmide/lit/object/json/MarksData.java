package com.zmide.lit.object.json;

import java.util.ArrayList;

public class MarksData {
	public int code;
	public String msg;
	public Bean data;
	
	public static class Bean {
		public ArrayList<MarkBean> marks;
		public ArrayList<ParentBean> parents;
	}
}
