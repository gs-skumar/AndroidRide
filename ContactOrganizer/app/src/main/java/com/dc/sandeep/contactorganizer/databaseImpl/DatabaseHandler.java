package com.dc.sandeep.contactorganizer.databaseImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.dc.sandeep.contactorganizer.com.dc.beans.contact.ContactDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CONTACT + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NAME + " TEXT , " + KEY_PHONE + " TEXT ," + KEY_ADDRESS + " TEXT ," + KEY_EMAIL + " TEXT ," + KEY_IMAGE + " TEXT )";
        System.out.println("SQL :: "+sql);
        Log.e("SQL ", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_CONTACT);
        onCreate(db);
    }

    public void insertContact(ContactDetail contactDetail){

        SQLiteDatabase db = getWritableDatabase();
        /*try{
           // db.execSQL("DROP TABLE IF EXIST "+TABLE_CONTACT);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }
*/

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,contactDetail.getName());
        values.put(KEY_PHONE,contactDetail.getPhoneNo());
        values.put(KEY_ADDRESS,contactDetail.getAddress());
        values.put(KEY_EMAIL,contactDetail.getEmail());
        values.put(KEY_IMAGE,contactDetail.getImageuri().toString());

        db.insert(TABLE_CONTACT, null, values);
        db.close();
    }

    public ContactDetail getContact(String name){

        ContactDetail contactDetail = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT,new String[]{KEY_ID,KEY_NAME,KEY_PHONE,KEY_EMAIL,KEY_ADDRESS,KEY_IMAGE},KEY_NAME+" =? ",new String[]{name},null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            contactDetail = new ContactDetail();
            cursor.moveToFirst();
            contactDetail.setId(cursor.getInt(0));
            contactDetail.setName(cursor.getString(1));
            contactDetail.setPhoneNo(cursor.getString(2));
            contactDetail.setEmail(cursor.getString(3));
            contactDetail.setAddress(cursor.getString(4));
            contactDetail.setImageuri(Uri.parse(cursor.getString(5)));
        }
            cursor.close();
            db.close();
        return contactDetail;
    }

    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM " + TABLE_CONTACT, null);
       int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int updateContact(ContactDetail contactDetail){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,contactDetail.getName());
        values.put(KEY_PHONE,contactDetail.getPhoneNo());
        values.put(KEY_ADDRESS,contactDetail.getAddress());
        values.put(KEY_EMAIL,contactDetail.getEmail());
        values.put(KEY_IMAGE, contactDetail.getImageuri().toString());

        return db.update(TABLE_CONTACT, values, KEY_ID + " =?", new String[]{String.valueOf(contactDetail.getId())});
    }

    public List<ContactDetail> getAllContacts(){

        List<ContactDetail> contactDetailList = new ArrayList<>();
        ContactDetail contactDetail ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
       try{
           cursor = db.rawQuery("SELECT * FROM "+TABLE_CONTACT,null);
           if(cursor.moveToFirst()){
               do {
                   contactDetail = new ContactDetail();
                   contactDetail.setId(cursor.getInt(0));
                   contactDetail.setName(cursor.getString(1));
                   contactDetail.setPhoneNo(cursor.getString(2));
                   contactDetail.setEmail(cursor.getString(3));
                   contactDetail.setAddress(cursor.getString(4));
                   contactDetail.setImageuri(Uri.parse(cursor.getString(5)));
                   contactDetailList.add(contactDetail);
               }while (cursor.moveToNext());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
            cursor.close();
            db.close();
        return contactDetailList;
    }
}
