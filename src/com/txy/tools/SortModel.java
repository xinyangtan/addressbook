package com.txy.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;

public class SortModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210830373423401142L;

	private int _id = -1;
    
	private String name;   //��ʾ������
	private String phonenum;
	private String sortLetters;  //��ʾ����ƴ��������ĸ
	
	private String[] phoneList;
	
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
		phoneList = phonenum.split("\\|");
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String[] getPhoneList() {
		return phoneList;
	}
}
