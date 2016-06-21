package me.crafter.android.zjsnviewer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;

import me.crafter.android.zjsnviewer.service.receiver.AlarmReceiver;
import me.crafter.android.zjsnviewer.service.service.ProceedService;
import me.crafter.android.zjsnviewer.service.service.TimerService;

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

        startService(new Intent(this, ProceedService.class));
        startService(new Intent(this, TimerService.class));
    }
    public static ZjsnApplication getInstance() {
        return instance;
    }
    public static Context getAppContext() {
        return ZjsnApplication.context;
    }
}
