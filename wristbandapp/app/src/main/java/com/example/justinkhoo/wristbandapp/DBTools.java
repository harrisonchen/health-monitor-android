package com.example.justinkhoo.wristbandapp;

/**
 * Created by justinkhoo on 12/12/14.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class DBTools extends SQLiteOpenHelper {

    public DBTools(Context applicationContext) {

        super(applicationContext, "todo.db", null, 1);
    }

    public void onCreate(SQLiteDatabase database) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimeStamp = dateFormat.format(new Date());


        String databaseCreateQuery="CREATE TABLE datas(" +
                "query_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "time char(14)," +
                "temp INTEGER" +
                "heartbeat INTEGER";


        database.execSQL(databaseCreateQuery);
    }

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        String query = "DROP TABLE IF EXISTS todo";

        database.execSQL(query);

        onCreate(database);
    }

    public void addtodatas(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("time", queryValues.get("timestamp"));
        values.put("temp", queryValues.get("temperature"));
        values.put("heartbeat",queryValues.get("heartbeat"));

        database.insert("datas", null, values);

        database.close();
    }
    public void removedatas(SQLiteDatabase database){
        String removeQuery= "DROP TABLE datas";
        database.execSQL(removeQuery);
    }


    public void deleteTask(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE * FROM data";

        database.execSQL(deleteQuery);

        database.close();
    }


    public HashMap<String, String> getTask() {

        HashMap<String, String> dataMap = new HashMap<String, String>();

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM datas";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                dataMap.put("task_id", cursor.getString(0));
                dataMap.put("name", cursor.getString(2));
                dataMap.put("status", cursor.getString(3));
            } while (cursor.moveToNext());
        }

        database.close();

        return dataMap;
    }

    public String getNextMaxID(String table) {

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM SQLITE_SEQUENCE WHERE name='" + table + "'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        int max;

        if (cursor.moveToFirst()) {
            max = Integer.parseInt(cursor.getString(1));
            max = max + 1;
        }
        else {
            max = 1;
        }

        database.close();

        return String.valueOf(max);

    }

    public String getTaskStatus(String id) {

        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT status FROM task WHERE task_id='" + id + "'";

        Cursor cursor = database.rawQuery(selectQuery, null);

        String taskStatus = "";

        if (cursor.moveToFirst()) {

            taskStatus = cursor.getString(0);

        }

        database.close();

        return taskStatus;

    }

    /* LIST TABLE */

    public void addList(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("category", queryValues.get("category"));

        database.insert("list", null, values);

        database.close();
    }

    public void deletedatas(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM list WHERE list.list_id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }

    public String getListCategory(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String category = "";

        String selectQuery = "SELECT category FROM list WHERE list_id=" + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            category = cursor.getString(0);
        }

        database.close();

        return category;

    }



    public ArrayList<HashMap<String, String>> getAllLists() {

        ArrayList<HashMap<String, String>> listArrayList;

        listArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM list ORDER BY list_id DESC";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> listMap = new HashMap<String, String>();

                listMap.put("list_id", cursor.getString(0));
                listMap.put("category", cursor.getString(1));

                listArrayList.add(listMap);
            } while (cursor.moveToNext());
        }

        database.close();

        return listArrayList;
    }


}