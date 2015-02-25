package com.mohamed.commercialdictionary.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mohamed on 06/02/15.
 */
public class ExternalDatabaseHelper extends SQLiteOpenHelper{
    private Context context ;
    private static String DB_NAME = "data.s3db";
    private static String DB_PATH ;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db ;



    public ExternalDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DATABASE_VERSION);
        this.context = context ;
        DB_PATH ="/data/data/com.mohamed.commercialdictionary/databases/" ;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.e("exist", "Data base Exist ");
        }else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            }catch (IOException e){
                throw new Error("Error Coping Data Base ");
            }
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH+DB_NAME);
        Log.e("2","Check Data Base Done ... ");
        return dbFile.exists();

    }

    private void copyDataBase() throws IOException {
        InputStream myInput=context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH +DB_NAME ;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte [] buffer = new byte[1024] ;
        int length ;
        while ((length=myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        Log.i("1","copy Data Base Done ... ") ;
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDB()  {
        String myPath = DB_PATH + DB_NAME ;
        db = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        Log.i("openDB ....", "DatBase is Open");
    }


    public Cursor getArabicData(String[] args){
        db = getReadableDatabase();
        String[] columns = {"en","ar","desc"};
        Cursor cursor = db.query("accounting",columns, "en = ?", args ,null,null,null);
        return cursor;
    }
    public Cursor getEnglishData(String[] args){
        db = getReadableDatabase();
        String[] columns = {"en","ar","desc"};
        Cursor cursor = db.query("accounting",columns, "ar = ?", args ,null,null,null);
        return cursor;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            Log.i("onUpgrade","onUpgrade is start xD");
            db.close();
            context.deleteDatabase(DB_NAME);
            Log.d("DeleteData","Database was Deleted xD");
            try {
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getAllEnglishColumn() {
        db = getReadableDatabase();
        String[] columns = {"en"};
        Cursor cursor = db.query("accounting",columns,null,null,null,null,null);
        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext())
            {
                str[i] = cursor.getString(cursor.getColumnIndex("en"));
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }

    public String[] getAllArabicColumn() {
        db = getReadableDatabase();
        String[] columns = {"ar"};
        Cursor cursor = db.query("accounting",columns,null,null,null,null,null);
        if(cursor.getCount() >0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext())
            {
                str[i] = cursor.getString(cursor.getColumnIndex("ar"));
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }










}


