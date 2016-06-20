package me.crafter.android.zjsnviewer.service.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
        appendLog("ProceedService start");
        Log.i(TAG, "Start");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Storage.language = Integer.parseInt(prefs.getString("language", "0"));
        if (prefs.getBoolean("auto_run", true)) {
            DockInfo.requestUpdate();
        }
        Log.i(TAG, "Stop");

        appendLog("ProceedService finished");

        AlarmReceiver.completeWakefulIntent(intent);
    }

    public static void appendLog(String text)
    {
        File logFile = new File("sdcard/ZjsnViewer.log");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss:");
            String date = sDateFormat.format(new java.util.Date());
            buf.append(date + text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
