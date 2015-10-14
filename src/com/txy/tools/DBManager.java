package com.txy.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//�ο���http://blog.csdn.net/liuhe688/article/details/6715983
public class DBManager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DatabaseHelper(context);
        // ��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // ����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     * 
     * @param persons
     */
    public void add(List<SortModel> persons)
    {
        // ����������ȷ������������
        db.beginTransaction(); // ��ʼ����
        try
        {
            for (SortModel person : persons)
            {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters() });
                // ������������execSQL()����������ռλ�����������Ѳ���ֵ���ں��棬˳���Ӧ
                // һ��������execSQL()�����У��û����������ַ�ʱ��Ҫת��
                // ʹ��ռλ����Ч�������������
            }
            db.setTransactionSuccessful(); // ��������ɹ����
        }
        finally
        {
            db.endTransaction(); // ��������
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
    	// ����������ȷ������������
        db.beginTransaction(); // ��ʼ����
        try
        {
	        db.execSQL("UPDATE " + DatabaseHelper.TABLE_NAME + " SET"
	                + " name=?,phonenum=?,sortLetters=? WHERE _id = ?", new Object[] { person.getName(),
	                person.getPhonenum(), person.getSortLetters(), person._id});
        }
        finally
        {
            db.endTransaction(); // ��������
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
        // �ͷ����ݿ���Դ
        db.close();
    }

}