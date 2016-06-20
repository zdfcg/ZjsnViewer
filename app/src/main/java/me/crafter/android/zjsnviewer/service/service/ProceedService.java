package me.crafter.android.zjsnviewer.service.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import me.crafter.android.zjsnviewer.config.Storage;
import me.crafter.android.zjsnviewer.service.receiver.AlarmReceiver;
import me.crafter.android.zjsnviewer.util.DockInfo;

/**
 * Created by paleneutron on 6/20/2016.
 */
public class ProceedService extends IntentService{
    public static final String TAG = "ProceedService";
    public ProceedService(){
        super("ProceedService");
    }
    @Override
    protected void onHandleIntent(Intent intent){
        Log.i(TAG, "Start");
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Storage.language = Integer.parseInt(prefs.getString("language", "0"));
        if (prefs.getBoolean("auto_run", true)) {
            DockInfo.requestUpdate();
        }
        AlarmReceiver.completeWakefulIntent(intent);
        Log.i(TAG, "Stop");
    }
}
