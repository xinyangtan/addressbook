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
     * add person
     * 
     * @param person
     */
    public void add(SortModel person)
    {
        // ����������ȷ������������
        db.beginTransaction(); // ��ʼ����
        try
        {
            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters(), person.getEmail(), person.getQq(), person.getWechat() });
                // ������������execSQL()����������ռλ�����������Ѳ���ֵ���ں��棬˳���Ӧ
                // һ��������execSQL()�����У��û����������ַ�ʱ��Ҫת��
                // ʹ��ռλ����Ч�������������
            db.setTransactionSuccessful(); // ��������ɹ����
        }
        finally
        {
            db.endTransaction(); // ��������
        }
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
                        + " VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[] { person.getName(),
                        person.getPhonenum(), person.getSortLetters(), person.getEmail(), person.getQq(), person.getWechat() });
                
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
        // �ͷ����ݿ���Դ
        db.close();
    }

}