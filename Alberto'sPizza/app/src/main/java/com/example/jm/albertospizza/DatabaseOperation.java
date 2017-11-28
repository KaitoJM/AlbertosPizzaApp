package com.example.jm.albertospizza;

/**
 * Created by JM on 6/8/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseOperation extends SQLiteOpenHelper{
    public static final int database_version = 1;
    private SQLiteDatabase db;

    public String create_table_query = "CREATE TABLE tbl_settings (" +
            "id INT(11)" +
            ", name VARCHAR(50)" +
            ", email VARCHAR(50)" +
            ", phone VARCHAR(50)" +
            ", address TEXT);";

    public DatabaseOperation(Context context) {
        super(context, "db_crossfit", null, database_version);
        Log.d("Database operations", "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean putInformation(String name, String email, String phone, String address) {
        if(db == null){
            db = getWritableDatabase();
        }

        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("phone", phone);
        cv.put("address", address);

        long k = db.insert("tbl_settings", null, cv);

        if (k != -1) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getInformation() {
        if(db == null){
            db = getWritableDatabase();
        }
        String[] columns = {"id", "name", "email", "phone", "address"};
        Cursor CR = db.query("tbl_settings", columns, "id = 1", null, null, null, null);

        return CR;
    }

    public boolean updateInformation(String name, String email, String phone, String address) {
        if(db == null){
            db = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("address", address);

        int result = db.update("tbl_settings",values, null, null);

        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteInformation(String where, String[] args) {
        if(db == null){
            db = getWritableDatabase();
        }
        db.delete("tbl_settings", where, args);
    }
}
