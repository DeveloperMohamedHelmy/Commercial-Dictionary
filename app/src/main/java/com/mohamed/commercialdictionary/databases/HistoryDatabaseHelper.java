package com.mohamed.commercialdictionary.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by mohamed on 06/02/15.
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "historydatabase";
    public static final String TABLE_NAME = "history";
    private static final String KEY_ID = "_id";
    public static final String KEY_FROM = "_english";
    public static final String KEY_TO = "_arabic";
    public static final String KEY_DESC = "_description";

    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FROM + " TEXT,"
                + KEY_TO + " TEXT," + KEY_DESC + " TEXT" + ");";
        db.execSQL(CREATE_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Adding new single History Raw
    public void addHistoryRaw(String enValue, String arValue, String descValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FROM, enValue);
        values.put(KEY_TO, arValue);
        values.put(KEY_DESC, descValue);
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /*public void deletedRawIfIsFound(String[] descArgs) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID, KEY_FROM};
        Cursor cursor = db.query(TABLE_NAME, columns, KEY_DESC + " = ?", descArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // Deleting record if found
            db.delete(TABLE_NAME, KEY_DESC +" = ?",descArgs);
            db.close();
        }
    }*/

    public void deleteRaw(String[] FromArgs){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID,KEY_FROM};
        Cursor cursor = db.query(TABLE_NAME, columns, KEY_FROM + " = ?", FromArgs , null ,null, null);
        if (cursor.moveToFirst()){
            db.delete(TABLE_NAME , KEY_FROM + " = ?" , FromArgs);
            db.close();
        }
    }

    // Getting single History Raw
    public Cursor getData() {
        SQLiteDatabase db = getReadableDatabase();
        Log.i("dat", "Get Data Cursor ") ;
        //Cursor c =db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
       Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " DESC", new String[]{});
        Log.i("dat","Get Data Done done done  ") ;
        return c ;
    }




}
