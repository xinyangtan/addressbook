package com.txy.tools;

import java.io.Serializable;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;

public class SortModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210830373423401142L;

	public int _id;
    
	private String name;   //显示的数据
	private String phonenum;
	private String sortLetters;  //显示数据拼音的首字母
	
	public String getName() {
		return name;
	}
	public void setName(String name, String sortLetters) {
		this.name = name;
		this.sortLetters = sortLetters;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getSortLetters() {
		return sortLetters;
	}
}
