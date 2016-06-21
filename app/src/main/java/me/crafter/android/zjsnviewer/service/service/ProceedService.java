package me.crafter.android.zjsnviewer.service.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import me.crafter.android.zjsnviewer.ZjsnApplication;
import me.crafter.android.zjsnviewer.config.Storage;
import me.crafter.android.zjsnviewer.service.receiver.AlarmReceiver;
import me.crafter.android.zjsnviewer.util.DockInfo;
import me.crafter.android.zjsnviewer.util.NetworkManager;

/**
 * Created by paleneutron on 6/20/2016.
 */
public class ProceedService extends IntentService{
    public static final String TAG = "ProceedService";
    public ProceedService(){
        super("ProceedService");
    }

    static void scheduleAlarms(int period) {
        Context ctxt = ZjsnApplication.getAppContext();
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctxt, 0, i, 0);
        mgr.cancel(pi);
        if (Build.VERSION.SDK_INT >= 19) {
            mgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + period, pi);
        } else {
            mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + period, pi);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent){
        appendLog("ProceedService start");
        Log.i(TAG, "Start");
        scheduleAlarms(2*60*1000);
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Storage.language = Integer.parseInt(prefs.getString("language", "0"));
        boolean success = false;
        if (prefs.getBoolean("auto_run", true)) {
//            success = DockInfo.requestUpdate();
            success = NetworkManager.updateDockInfo();
        }
        Log.i(TAG, "success stop:"+String.valueOf(success));

        appendLog("ProceedService finished");

//        AlarmReceiver.completeWakefulIntent(intent);
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
