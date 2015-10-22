package com.txy.tools;

import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class PhonenumLocation {
	public SQLiteDatabase sqliteDB;
	public DatabaseDAO dao;
	public Context c;
	
	public PhonenumLocation(Context c) {
		this.c = c;
	}
	
	private void initDB() {
		AssetsDatabaseManager.initManager(this.c);
		AssetsDatabaseManager mg = AssetsDatabaseManager.getAssetsDatabaseManager();
		sqliteDB = mg.getDatabase("number_location.zip");
		dao = new DatabaseDAO(sqliteDB);
	}
	
	@SuppressLint("NewApi") public String getLocation(String phoneNumber) {
		try {
			initDB();
			String prefix, center;
			Map<String,String> map = null;
			
			if (isZeroStarted(phoneNumber) && getNumLength(phoneNumber) > 2){
				prefix = getAreaCodePrefix(phoneNumber);
				map = dao.queryAeraCode(prefix);
				
			}else if (!isZeroStarted(phoneNumber) && getNumLength(phoneNumber) > 6){
				prefix = getMobilePrefix(phoneNumber);
				center = getCenterNumber(phoneNumber);
				map = dao.queryNumber(prefix, center);
			}
			String province = map.get("province");
			String city = map.get("city");
			String result = "";
			if (province == null || city == null || province.isEmpty() || city.isEmpty()){
				
			}else if ( province.equals(city))
				result = province;
			else
				result = province + "  " + city;
			return result;
		}
			catch (Exception e) {
				// TODO: handle exception
				return "";
			}
	}

	/**得到输入区号中的前三位数字或前四位数字去掉首位为零后的数字。*/
	public String getAreaCodePrefix(String number){
		if (number.charAt(1) == '1' || number.charAt(1) == '2')
			return number.substring(1,3);
		return number.substring(1,4);
	}
	
	/**得到输入手机号码的前三位数字。*/
	public String getMobilePrefix(String number){
		return number.substring(0,3);
	}
	
	/**得到输入号码的中间四位号码，用来判断手机号码归属地。*/
	public String getCenterNumber(String number){
		return number.substring(3,7);
	}
	
	/**判断号码是否以零开头*/
	@SuppressLint("NewApi") public boolean isZeroStarted(String number){
		if (number == null || number.isEmpty()){
			return false;
		}
		return number.charAt(0) == '0';
	}
	
	/**得到号码的长度*/
	@SuppressLint("NewApi") public int getNumLength(String number){
		if (number == null || number.isEmpty()  )
			return 0;
		return number.length();
	}
}
