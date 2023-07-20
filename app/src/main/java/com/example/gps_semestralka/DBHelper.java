package com.example.gps_semestralka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Semestralka_PAA.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderData.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderData.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderData.FeedEntry.COLUMN_NAME_LAT + " TEXT," +
                    FeedReaderData.FeedEntry.COLUMN_NAME_LON + " TEXT," +
                    FeedReaderData.FeedEntry.COLUMN_NAME_ALT + " TEXT," +
                    FeedReaderData.FeedEntry.COLUMN_NAME_DESC + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderData.FeedEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL(SQL_DELETE_ENTRIES);
        onCreate(DB);
    }

    public boolean insertUserData(String lat, String lon, String alt, String description){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderData.FeedEntry.COLUMN_NAME_LAT, lat);
        contentValues.put(FeedReaderData.FeedEntry.COLUMN_NAME_LON, lon);
        contentValues.put(FeedReaderData.FeedEntry.COLUMN_NAME_ALT, alt);
        contentValues.put(FeedReaderData.FeedEntry.COLUMN_NAME_DESC, description);
        long result = DB.insert(FeedReaderData.FeedEntry.TABLE_NAME, null, contentValues);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteUserData(String sirka, String delka, String vyska, String description){
        SQLiteDatabase DB = this.getWritableDatabase();

        String selection = FeedReaderData.FeedEntry.COLUMN_NAME_LAT + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_LON + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_ALT + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_DESC + "=?";
        Log.v("Item", selection);
        String[] selectionArgs = { sirka, delka, vyska, description};
        int result = DB.delete(FeedReaderData.FeedEntry.TABLE_NAME, selection, selectionArgs);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateUserData(String sirka, String delka, String vyska, String description, String newDescription){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderData.FeedEntry.COLUMN_NAME_DESC,newDescription);
        String selection = FeedReaderData.FeedEntry.COLUMN_NAME_LAT + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_LON + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_ALT + "=? and " +
                FeedReaderData.FeedEntry.COLUMN_NAME_DESC + "=?";
        String[] selectionArgs = {sirka,delka,vyska,description};
        int result = DB.update(FeedReaderData.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getUserData(){
        SQLiteDatabase DB = this.getReadableDatabase();
        String[] projection = {
                FeedReaderData.FeedEntry._ID,
                FeedReaderData.FeedEntry.COLUMN_NAME_LAT,
                FeedReaderData.FeedEntry.COLUMN_NAME_LON,
                FeedReaderData.FeedEntry.COLUMN_NAME_ALT,
                FeedReaderData.FeedEntry.COLUMN_NAME_DESC
        };
        Cursor cursor = DB.query(FeedReaderData.FeedEntry.TABLE_NAME,
                projection,
                null,null,null,null,null);
        return cursor;
    }

}

