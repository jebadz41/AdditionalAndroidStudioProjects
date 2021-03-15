package com.example.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class ReminderDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DESCRPTION = "description";
    public static final String KEY_DATE = "_date";

    private final String DATABASE_NAME = "ReminderDB";
    private final String DATABASE_TABLE = "ReminderTable";
    private final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private  final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public ReminderDB(Context context)
    {
        ourContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper (Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            /*
            CREATE TABLE ContactsTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,
                person_name TEXT NOT NULL, _cell TEXT NOT NULL);

             */
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " ("+
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_DESCRPTION + " TEXT NOT NULL,"+
                    KEY_DATE + " TEXT NOT NULL);";

            db.execSQL(sqlCode);
        }
    }

    public ReminderDB open() throws SQLException
    {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();

        return this;
    }

    public void close()
    {
        ourHelper.close();
    }

    public long createEntry(String name, String cell)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DESCRPTION, name);
        cv.put(KEY_DATE, cell);

        return  ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public String getData()
    {
        String [] columns = new String [] {KEY_ROWID, KEY_DESCRPTION, KEY_DATE};

        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        String result = "";

        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iDes = c.getColumnIndex(KEY_DESCRPTION);
        int iDate = c.getColumnIndex(KEY_DATE);

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            result = result + c.getString(iRowID) + ": " + c.getString(iDes) + " " +
                    c.getString(iDate) + "\n";
        }

        c.close();

        return result;
    }

    public  long deleteEntry(String rowId)
    {
        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long updateEntry(String rowId, String name, String cell)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_DESCRPTION, name);
        cv.put(KEY_DATE, cell);

        return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=?", new String[]{rowId});
    }
}