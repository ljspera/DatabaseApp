package com.example.databaseappex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class DatabaseControl {

    SQLiteDatabase database;
    DatabaseHelper helper;

    public DatabaseControl(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void open() {
        database = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    //Put as boolean to make it easier to debug (if this method is putting values into database)
    public boolean insert(String name, String city, String state){
        ContentValues values = new ContentValues();
        values.put("teamName", name);
        values.put("city", city);
        values.put("state", state);
        //^These values must match the attributes put in the helper
        return database.insert("contact", null, values) > 0;
    }

    public void delete(String n){
        database.delete("contact", "teamName=\""+n+"\"", null);
    }

    public String getCity(String name){
        String query = "select city from contact where teamName=\""+name+"\"";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        String city = cursor.getString(0);
        cursor.close();
        return city;
    }

    //Type in name and get the state back which corresponds with the specific name
    public String getState(String name){
        String query = "select state from contact where teamName=\""+name+"\"";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        String state = cursor.getString(0);
        cursor.close();
        return state;
    }

    public String[] getAllNamesArray() {
        String query = "select teamName from contact";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<String> list = new ArrayList<String>();
        while(!cursor.isAfterLast()) { //While cursor is not at end of table
            String name = cursor.getString(0);
            list.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    public ArrayList<String> getAllNames() {
        String query = "select name from contact";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<String> list = new ArrayList<String>();
        while(!cursor.isAfterLast()) { //While cursor is not at end of table
            String name = cursor.getString(0);
            list.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
