package com.txy.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//参考：http://blog.csdn.net/liuhe688/article/details/6715983
public class DBManager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add person
     * 
     * @param person
     */
    public void add(SortModel person)
    {
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters(), person.getEmail(), person.getQq(), person.getWechat() });
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * add persons
     * 
     * @param persons
     */
    public void add(List<SortModel> persons)
    {
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (SortModel person : persons)
            {
            	db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters(), person.getEmail(), person.getQq(), person.getWechat() });
                
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * query all persons, return list
     * 
     * @return List<SortModel>
     */
    public List<SortModel> query()
    {
        ArrayList<SortModel> persons = new ArrayList<SortModel>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
        	SortModel person = new SortModel();
            person.set_id(c.getInt(c.getColumnIndex("id")));
            person.setName(c.getString(c.getColumnIndex("name")), 
            		c.getString(c.getColumnIndex("sortLetters")));
            person.setPhonenum(c.getString(c.getColumnIndex("phonenum")));

            person.setEmail(c.getString(c.getColumnIndex("email")));
            person.setQq(c.getString(c.getColumnIndex("qq")));
            person.setWechat(c.getString(c.getColumnIndex("wechat")));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    public int updatePerson(SortModel person) {
    	ContentValues cv = new ContentValues();
        cv.put("name", person.getName());
        cv.put("phonenum", person.getPhonenum());
        cv.put("sortLetters", person.getSortLetters());
        cv.put("email", person.getEmail());
        cv.put("qq", person.getQq());
        cv.put("wechat", person.getWechat());
        String[] args = {String.valueOf(person.get_id())};
        return db.update(DatabaseHelper.TABLE_NAME, cv, "id=?",args);   
//	        db.execSQL("UPDATE " + DatabaseHelper.TABLE_NAME + " t SET"
//	                + " t.name=?,t.phonenum=?,t.sortLetters=? WHERE t._id = ?", new Object[] { person.getName(),
//	                person.getPhonenum(), person.getSortLetters(),person.get_id()});

    }
    
    public void deleteAll() {
    	db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_NAME);
        String sql = "update sqlite_sequence set seq=0 where name='"+DatabaseHelper.TABLE_NAME+"'";
    	db.execSQL(sql);
    }
    
    public void deletePerson(SortModel person)
    {
        db.delete(DatabaseHelper.TABLE_NAME, "id = ?",
                new String[] { String.valueOf(person.get_id()) });
    }
    
    /**
     * query all persons, return cursor
     * 
     * @return Cursor
     */
     public Cursor queryTheCursor()
    {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        // 释放数据库资源
        db.close();
    }

}