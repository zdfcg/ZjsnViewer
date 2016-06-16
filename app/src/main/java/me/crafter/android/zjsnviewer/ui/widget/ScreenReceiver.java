package me.crafter.android.zjsnviewer.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Widget_Main.updateWidget(context);
            Widget_Travel.updateWidget(context);
            Widget_Repair.updateWidget(context);
            Widget_Build.updateWidget(context);
            Widget_Make.updateWidget(context);
        }
    }


}
