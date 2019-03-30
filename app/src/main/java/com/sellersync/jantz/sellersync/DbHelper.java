package com.sellersync.jantz.sellersync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jantz on 6/5/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="SellerSync";
    private static final int DB_VER = 1;
    public static final String DB_TABLE ="Reminders";
    public static final String DB_COLUM ="ReminderName";
    public static final String DB_COLUM2 ="ReminderId";
    public static final String DB_COLUM3 ="IntentId";
    public static final String DB_TABLE2 ="Channels";
    public static final String DB_COLUM2_1 ="ChannelName";
    public static final String DB_COLUM2_2 ="ChannelId";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DB_TABLE + " (" +
                DB_COLUM + " TEXT, " +
                DB_COLUM2 + " NUMERIC, " +
                DB_COLUM3 + " NUMERIC " +
                ")";
        db.execSQL(query);
        String query2 = "CREATE TABLE " + DB_TABLE2 + " (" +
                DB_COLUM2_1 + " TEXT, " +
                DB_COLUM2_2 + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                ")";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s",DB_TABLE);
        db.execSQL(query);
        onCreate(db);

    }

    public void addColumn () {
        SQLiteDatabase  db = this.getWritableDatabase();
        String query = "ALTER TABLE Reminders ADD COLUMN IntentId NUMERIC;";                                                      //COLUMN ADD METHOD
        db.execSQL(query);
        db.close();
    }



    public void insertNewReminder (String reminder, Long alarmId, Integer intentId) {
        SQLiteDatabase  db = this.getWritableDatabase();
        String query = "INSERT INTO "+DB_TABLE+" VALUES ('"+reminder+"', "+alarmId+", "+intentId+" );";
        db.execSQL(query);
        db.close();
    }

    public Integer getIntentId (String reminder) {
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT IntentId FROM Reminders WHERE ReminderName = '"+reminder+"'", null);
        c.moveToFirst();
        int index = c.getColumnIndex("IntentId");
        Integer result = c.getInt(index);
        db.close();
        return result;
    }

    public String getReminderName (Long time) {
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ReminderName FROM Reminders WHERE ReminderId = "+time+"", null);
        c.moveToFirst();
        int index = c.getColumnIndex("ReminderName");
        String result = c.getString(index);
        db.close();
        return result;
    }

    public Long getTime (String reminder) {
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ReminderId FROM Reminders WHERE ReminderName = '"+reminder+"'", null);
        c.moveToFirst();
        int index = c.getColumnIndex("ReminderId");
        Long result = c.getLong(index);
        db.close();
        return result;
    }

    public Long getAlarmId (Long currentTime) {
        SQLiteDatabase  db = this.getWritableDatabase();
        Long time2 = currentTime;
        Long time1 = currentTime -240000;
        Cursor c = db.rawQuery("SELECT ReminderId FROM Reminders WHERE ReminderId BETWEEN "+time1+" AND "+time2+"", null);
        c.moveToFirst();
        int index = c.getColumnIndex("ReminderId");
        Long result = c.getLong(index);
        db.close();
        return result;
    }

    public Long getExactAlarmId (Long currentTime) {                                                                               //// FIXME: 6/19/2017
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ReminderId FROM Reminders WHERE ReminderId = "+currentTime+"", null);
        c.moveToFirst();
        int index = c.getColumnIndex("ReminderId");
        Long result = c.getLong(index);
        db.close();
        return result;
    }

    public ArrayList<Long> getReminderIdList(){
        ArrayList<Long>reminderIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUM2},null,null,null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUM2);
            reminderIdList.add(cursor.getLong(index));
        }
        cursor.close();
        db.close();
        return reminderIdList;
    }

    public void deleteReminder(String reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUM + " = ?",new String[]{String.valueOf(reminder)});
        db.close();
    }

    public void deleteAllReminder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE);
        db.close();
    }

    public ArrayList<String> getReminderList(){
        ArrayList<String>reminderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUM},null,null,null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUM);
            reminderList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return reminderList;
    }

    public void insertNewChannel (String channel) {
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUM2_1, channel);
        this.getWritableDatabase().insertOrThrow(DB_TABLE2,"",values);
        db.close();
    }

    public void deleteChannel(String channel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE2,DB_COLUM2_1 + " = ?",new String[]{String.valueOf(channel)});
        db.close();
    }

    public void deleteAllChannel() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ DB_TABLE2);
        db.close();
    }

    public ArrayList<String> getChannelList(){
        ArrayList<String>channelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE2,new String[]{DB_COLUM2_1},null,null,null,null,null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUM2_1);
            channelList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return channelList;
    }
}
