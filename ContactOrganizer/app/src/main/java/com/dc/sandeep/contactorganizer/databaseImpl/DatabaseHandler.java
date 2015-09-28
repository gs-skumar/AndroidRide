package com.dc.sandeep.contactorganizer.databaseImpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SandeepK on 28-09-2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactManager",
    TABLE_CONTACT = "Contact",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_PHONE = "phone",
    KEY_ADDRESS = "address",
    KEY_IMAGE = "imageUri",
    KEY_EMAIL = "email";

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_CONTACT +" ( "+ KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_NAME +" TEXT, "+ KEY_PHONE +" TEXT,"+ KEY_ADDRESS +" TEXT," + KEY_EMAIL + "TEXT,"+ KEY_IMAGE + "TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_CONTACT);
        onCreate(db);
    }
}
