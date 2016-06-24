package me.crafter.android.zjsnviewer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import java.util.Calendar;

import me.crafter.android.zjsnviewer.service.receiver.AlarmReceiver;
import me.crafter.android.zjsnviewer.service.service.ProceedService;
import me.crafter.android.zjsnviewer.service.service.TimerService;
import me.crafter.android.zjsnviewer.util.WakeLocker;

/**
 * @author traburiss
 * @date 2016/6/6
 * @info ZjsnViewer
 * @desc
 */

public class ZjsnApplication extends Application{

    private static ZjsnApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();

//        startService(new Intent(this, ProceedService.class));
        startService(new Intent(this, TimerService.class));
        startAlarm();
    }

    public static void startAlarm() {
        Context ctxt = ZjsnApplication.getAppContext();
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctxt, 0, i, 0);

        Calendar cld = Calendar.getInstance();
        cld.setTimeInMillis(System.currentTimeMillis());
        cld.add(Calendar.MINUTE,1);
        mgr.setRepeating(AlarmManager.RTC_WAKEUP,cld.getTimeInMillis(),1000*60*10,pi);
    }
    public static ZjsnApplication getInstance() {
        return instance;
    }
    public static Context getAppContext() {
        return ZjsnApplication.context;
    }
}
