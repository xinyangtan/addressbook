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
                        + " VALUES(null, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters() });
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
            person._id = c.getInt(c.getColumnIndex("_id"));
            person.setName(c.getString(c.getColumnIndex("name")), 
            		c.getString(c.getColumnIndex("sortLetters")));
            person.setPhonenum(c.getString(c.getColumnIndex("phonenum")));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    public void updatePerson(SortModel person) {
    	// 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
	        db.execSQL("UPDATE " + DatabaseHelper.TABLE_NAME + " SET"
	                + " name=?,phonenum=?,sortLetters=? WHERE _id = ?", new Object[] { person.getName(),
	                person.getPhonenum(), person.getSortLetters(), person._id});
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }
    
    public void deletePerson(SortModel person)
    {
        db.delete(DatabaseHelper.TABLE_NAME, "_id = ?",
                new String[] { String.valueOf(person._id) });
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