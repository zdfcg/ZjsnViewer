package me.crafter.android.zjsnviewer.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import me.crafter.android.zjsnviewer.ZjsnApplication;
import me.crafter.android.zjsnviewer.service.service.ProceedService;

/**
 * Created by paleneutron on 6/20/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
//        context = ZjsnApplication.getAppContext();
        Intent intent = new Intent(context, ProceedService.class);
        startWakefulService(context, intent);
    }

}