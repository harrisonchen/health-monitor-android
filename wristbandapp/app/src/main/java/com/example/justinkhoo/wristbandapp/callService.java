package com.example.justinkhoo.wristbandapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.justinkhoo.wristbandapp.MyAsyncResponse;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by justinkhoo on 4/3/15.
 */
public class callService {

    private String longitude;
    private String latitude;
    MyLocationManager appLocationManager;

    callService(Context context) {
         appLocationManager = new MyLocationManager(context);

//        Log.d(latitude, "lati : ");
//        Log.d(longitude, "long : ");
    }
    public void printCoor(){
        latitude = appLocationManager.getLatitude();
        longitude = appLocationManager.getLongitude();
        Log.d(latitude, "lati : ");
        Log.d(longitude, "long : ");
    }

}