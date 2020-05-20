package com.example.waitinglist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.waitinglist.Data.WaitListContract.WaitListEntry;

public class WaitListDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "waitlist.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;

    public WaitListDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTableQuery = "create table " + WaitListEntry.TABLE_NAME + " ( " +
                WaitListEntry._ID + " integer primary key autoincrement, " +
                WaitListEntry.GUEST_NAME + " text not null," +
                WaitListEntry.PARTY_SIZE + " text not null," +
                WaitListEntry.TIME_STAMP + " timestamp default current_timestamp not null);";
        db.execSQL(createTableQuery);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String dropTableQuery = "drop table if exists " + WaitListEntry.TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }


    public boolean addGuest(String name, String size){
        sqLiteDatabase = this.getWritableDatabase();
        System.out.println("1");
        ContentValues contentValues = new ContentValues();
        contentValues.put(WaitListEntry.GUEST_NAME, name);
        contentValues.put(WaitListEntry.PARTY_SIZE, size);
        System.out.println("2");
        long d =  sqLiteDatabase.insert(WaitListEntry.TABLE_NAME,
                null,
                contentValues);
        System.out.println("3");
        if(d == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getGuests(){
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitListEntry.TIME_STAMP);
        return cursor;
    }

    public boolean remove(long id){
        return sqLiteDatabase.delete(WaitListEntry.TABLE_NAME, WaitListEntry._ID + "=" + id, null) > 0;
    }
}
