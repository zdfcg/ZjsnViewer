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
import android.os.PowerManager;
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
import me.crafter.android.zjsnviewer.ui.widget.Widget_Build;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Main;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Make;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Repair;
import me.crafter.android.zjsnviewer.ui.widget.Widget_Travel;
import me.crafter.android.zjsnviewer.util.DockInfo;
import me.crafter.android.zjsnviewer.util.NetworkManager;
import me.crafter.android.zjsnviewer.util.NotificationSender;

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
        appendLog(TAG+" start ProceedService");
        Context context = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Storage.language = Integer.parseInt(prefs.getString("language", "0"));
        boolean upadated = false;
        if (prefs.getBoolean("auto_run", true)) {
            upadated = DockInfo.requestUpdate();
        }
        appendLog(TAG+" "+String.valueOf(upadated));
        //notification checker
        if (DockInfo.shouldNotify()){

            String msj_name = prefs.getString("notification_msj_name","");
            if (msj_name.isEmpty()){

                NotificationSender.notify(context, Storage.str_reportTitle[Storage.language], DockInfo.getStatusReportAllFull());
            }else {

                NotificationSender.notify(context, msj_name + Storage.str_msjreportTitle[Storage.language], DockInfo.getStatusReportAllFull());
            }
        }
        //check if screen is on
        //if screen not on, widget should not update
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenon;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH){
            screenon = pm.isInteractive();
        } else {
            screenon = pm.isScreenOn();
        }
        if (!screenon){
            //Log.i("TimerService", "run() - Screen is off, ignores update.");
        } else {
            int currentUnix = DockInfo.currentUnix();
            if (currentUnix - TimerService.lastWidgetUpdate >= Integer.parseInt(prefs.getString("refresh", "60"))) {

                TimerService.lastWidgetUpdate = currentUnix;
                Widget_Main.updateWidget(context);
                Widget_Travel.updateWidget(context);
                Widget_Repair.updateWidget(context);
                Widget_Build.updateWidget(context);
                Widget_Make.updateWidget(context);
            } else {
                //not time yet, ignore widget update
            }
        }
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
