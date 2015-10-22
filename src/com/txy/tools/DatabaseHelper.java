package com.txy.tools;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//�ο���http://blog.csdn.net/liuhe688/article/details/6715983

@SuppressLint("NewApi")
public class DatabaseHelper extends SQLiteOpenHelper// �̳�SQLiteOpenHelper��
{

    // ���ݿ�汾��
    private static final int DATABASE_VERSION = 1;
    // ���ݿ���
    private static final String DATABASE_NAME = "TestDB.db";

    // ���ݱ�����һ�����ݿ��п����ж������Ȼ������ֻ������һ����
    public static final String TABLE_NAME = "PersonTable";

    // ���캯�������ø���SQLiteOpenHelper�Ĺ��캯��
    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
        // SQLiteOpenHelper�Ĺ��캯��������
        // context�������Ļ���
        // name�����ݿ�����
        // factory���α깤������ѡ��
        // version�����ݿ�ģ�Ͱ汾��
    }

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // ���ݿ�ʵ�ʱ���������getWritableDatabase()��getReadableDatabase()��������ʱ
        // CursorFactory����Ϊnull,ʹ��ϵͳĬ�ϵĹ�����
    }

    // �̳�SQLiteOpenHelper��,����Ҫ��д������������onCreate(),onUpgrade(),onOpen()
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // ����ʱ�䣺���ݿ��һ�δ���ʱonCreate()�����ᱻ����

        // onCreate������һ�� SQLiteDatabase������Ϊ������������Ҫ�������������ͳ�ʼ������
        // �����������Ҫ��ɴ������ݿ������ݿ�Ĳ���

        
        // �����������SQL��䣨���Դ�SQLite Expert���ߵ�DDLճ�������ӽ�StringBuffer�У�
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[name] TEXT,");
        sBuffer.append("[phonenum] TEXT,");
        sBuffer.append("[sortLetters] TEXT,");
        sBuffer.append("[email] TEXT,");
        sBuffer.append("[qq] TEXT,");
        sBuffer.append("[wechat] TEXT)");

        // ִ�д������SQL���
        db.execSQL(sBuffer.toString());

        // ��������޸��������У�ֻҪ���ݿ��Ѿ����������Ͳ����ٽ������onCreate����

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // ����ʱ�䣺���DATABASE_VERSIONֵ����Ϊ�����,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade

        // onUpgrade����������������һ�� SQLiteDatabase����һ���ɵİ汾�ź�һ���µİ汾��
        // �����Ϳ��԰�һ�����ݿ�Ӿɵ�ģ��ת�䵽�µ�ģ��
        // �����������Ҫ��ɸ������ݿ�汾�Ĳ���

        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        // ������������˵���ǣ�ͨ����鳣��ֵ��������Σ�����ʱɾ���ɱ�Ȼ�����onCreate�������±�
        // һ����ʵ����Ŀ���ǲ�����ô���ģ���ȷ���������ڸ������ݱ�ṹʱ����Ҫ�����û���������ݿ��е����ݲ���ʧ

    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        // ÿ�δ����ݿ�֮�����ȱ�ִ��
       
    }

}