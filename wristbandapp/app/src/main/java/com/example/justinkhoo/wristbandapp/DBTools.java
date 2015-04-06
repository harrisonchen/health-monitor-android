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

        super(applicationContext, "oneband.db", null, 1);
    }

    public void onCreate(SQLiteDatabase database) {

        String motionCreateQuery =
                "CREATE TABLE motion(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                       "x REAL DEFAULT 0, " +
                                        "y REAL DEFAULT 0, " +
                                        "z REAL DEFAULT 0)";

        String stepsCreateQuery =
                "CREATE TABLE steps(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "count INTEGER DEFAULT 0)";

        String heartbeatCreateQuery =
                "CREATE TABLE heartbeat(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        "beats_per_minute INTEGER DEFAULT 0, " +
                                        "beats_per_second INTEGER DEFAULT 0)";

        String temperatureCreateQuery =
                "CREATE TABLE temperature(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                           "fahrenheit REAL DEFAULT 0.0)";

        String emergencyContacts =
                "CREATE TABLE contact(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "phone TEXT)";
        database.execSQL(motionCreateQuery);
        database.execSQL(stepsCreateQuery);
        database.execSQL(heartbeatCreateQuery);
        database.execSQL(temperatureCreateQuery);
        database.execSQL(emergencyContacts);
    }

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        String query = "DROP TABLE IF EXISTS oneband";

        database.execSQL(query);

        onCreate(database);
    }

    /* Emergency Contacts */

    public void addEmergencyContacts(HashMap<String, String> contacts){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", contacts.get("name"));
        values.put("phone", contacts.get("phone"));

        database.insert("contact", null, values);
        database.close();
    }
    public void deleteEmergencyContacts(String name) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM contact WHERE name='" + name + "'";

        database.execSQL(deleteQuery);

        database.close();
    }
    public ArrayList<HashMap<String, String>> getEmergencyContacts() {

        ArrayList<HashMap<String, String>> contactsArrayList;

        contactsArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM contact ORDER BY id DESC LIMIT 50";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> contactsMap = new HashMap<String, String>();

                contactsMap.put("name", cursor.getString(1));
                contactsMap.put("phone", cursor.getString(2));

                contactsArrayList.add(contactsMap);
            } while (cursor.moveToNext());
        }

        database.close();

        return contactsArrayList;
    }

    /* Motion Functions */
    public void addMotion(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("x", queryValues.get("x"));
        values.put("y", queryValues.get("y"));
        values.put("z", queryValues.get("z"));

        database.insert("motion", null, values);

        database.close();
    }
    public void deleteMotion(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM motion WHERE id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }
    public ArrayList<HashMap<String, String>> getMotion() {

        ArrayList<HashMap<String, String>> motionArrayList;

        motionArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM motion ORDER BY id DESC LIMIT 50";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> motionMap = new HashMap<String, String>();

                motionMap.put("id", cursor.getString(0));
                motionMap.put("x", cursor.getString(1));
                motionMap.put("y", cursor.getString(2));
                motionMap.put("z", cursor.getString(3));

                motionArrayList.add(motionMap);
            } while (cursor.moveToNext());
        }

        database.close();

        return motionArrayList;
    }

    /* Steps Functions */
    public void addSteps(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("count", queryValues.get("step_count"));

        database.insert("steps", null, values);

        database.close();
    }
    public void deleteSteps(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM steps WHERE id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }
    public ArrayList<HashMap<String, String>> getSteps() {

        ArrayList<HashMap<String, String>> arrayList;

        arrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * " +
                "FROM (SELECT * FROM steps ORDER BY id DESC LIMIT 10)" + //just wanna read 10
                "ORDER BY id ASC";          //get the last 10 entry of the database

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", cursor.getString(0));
                map.put("step_count", cursor.getString(1));

                arrayList.add(map);
            } while (cursor.moveToNext());
        }

        database.close();

        return arrayList;
    }

    /* Heartbeat Functions */
    public void addHeartbeat(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("beats_per_minute", queryValues.get("beats_per_minute"));

        database.insert("heartbeat", null, values);

        database.close();
    }
    public void deleteHeartbeat(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM heartbeat WHERE id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }
    public ArrayList<HashMap<String, String>> getHeartbeat() {

        ArrayList<HashMap<String, String>> arrayList;

        arrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * " +
                                    "FROM (SELECT * FROM heartbeat ORDER BY id DESC LIMIT 10)" +
                                    "ORDER BY id ASC";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", cursor.getString(0));
                map.put("beats_per_minute", cursor.getString(1));

                arrayList.add(map);
            } while (cursor.moveToNext());
        }

        database.close();

        return arrayList;
    }

    /* Temperature Functions */
    public void addTemperature(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("fahrenheit", queryValues.get("fahrenheit"));

        database.insert("temperature", null, values);

        database.close();
    }
    public void deleteTemperature(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM temperature WHERE id='" + id + "'";

        database.execSQL(deleteQuery);

        database.close();
    }
    public ArrayList<HashMap<String, String>> getTemperature() {

        ArrayList<HashMap<String, String>> arrayList;

        arrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * " +
                            "FROM (SELECT * FROM temperature ORDER BY id DESC LIMIT 10)" +
                            "ORDER BY id ASC";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", cursor.getString(0));
                map.put("fahrenheit", cursor.getString(1));

                arrayList.add(map);
            } while (cursor.moveToNext());
        }

        database.close();

        return arrayList;
    }
}
