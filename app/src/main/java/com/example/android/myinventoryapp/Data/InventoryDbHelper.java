package com.example.android.myinventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class InventoryDbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="stuff.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context){ super(context, DB_NAME, null, DATABASE_VERSION);};

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_STUFF_TABLE =  "CREATE TABLE " + InventoryContract.NewEntry.TABLE_NAME + " ("
                + InventoryContract.NewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.NewEntry.COLUMN_PNAME + " TEXT NOT NULL, "
                + InventoryContract.NewEntry.COLUMN_PRICE + " FLOAT NOT NULL, "
                + InventoryContract.NewEntry.COLUMN_QUANTITY + " INTEGER  DEFAULT 0, "
                + InventoryContract.NewEntry.COLUMN_SNAME + " TEXT NOT NULL , "
                + InventoryContract.NewEntry.COLUMN_PHONE+ " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STUFF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
