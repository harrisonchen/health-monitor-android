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


}