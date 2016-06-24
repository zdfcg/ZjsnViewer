package me.crafter.android.zjsnviewer.service.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import me.crafter.android.zjsnviewer.ZjsnApplication;
import me.crafter.android.zjsnviewer.service.service.ProceedService;

/**
 * Created by paleneutron on 6/20/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
//        context = ZjsnApplication.getAppContext();
        ProceedService.appendLog("alarm start");
        Log.i("AlarmReceiver", "alarm start");
        Intent intent = new Intent(context, ProceedService.class);
//        scheduleAlarms(1000*60*10);
        startWakefulService(context, intent);
    }
}

//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent arg1) {
////        context = ZjsnApplication.getAppContext();
//        Intent intent = new Intent(context, ProceedService.class);
//        context.startService(intent);
//    }
//}