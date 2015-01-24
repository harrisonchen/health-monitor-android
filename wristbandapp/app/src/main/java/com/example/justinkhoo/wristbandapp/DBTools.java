package com.example.justinkhoo.wristbandapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DBTools extends SQLiteOpenHelper {

    public DBTools(Context applicationContext) {

        super(applicationContext, "h2j-health.db", null, 1);
    }

    public void onCreate(SQLiteDatabase database) {

        String accelerometerCreateQuery =
                "CREATE TABLE acceleromter(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "x INTEGER DEFAULT 0, y INTEGER DEFAULT 0, z INTEGER DEFAULT 0)";

        database.execSQL(accelerometerCreateQuery);
    }

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        String query = "DROP TABLE IF EXISTS h2j-health";

        database.execSQL(query);

        onCreate(database);
    }

    public void addAccel(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("x", queryValues.get("x"));
        values.put("y", queryValues.get("y"));
        values.put("z", queryValues.get("z"));

        database.insert("accelerometer", null, values);

        database.close();
    }


    public void deleteAccel(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM accelerometer WHERE id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }

    public ArrayList<HashMap<String, String>> getAccel() {

        ArrayList<HashMap<String, String>> accelArrayList;

        accelArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM accelerometer ORDER BY task_id DESC LIMIT 50";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> accelMap = new HashMap<String, String>();

                accelMap.put("id", cursor.getString(0));
                accelMap.put("x", cursor.getString(2));
                accelMap.put("y", cursor.getString(3));
                accelMap.put("z", cursor.getString(4));

                accelArrayList.add(accelMap);
            } while (cursor.moveToNext());
        }

        database.close();

        return accelArrayList;
    }
}
