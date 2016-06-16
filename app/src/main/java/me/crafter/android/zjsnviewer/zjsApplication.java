package me.crafter.android.zjsnviewer;

import android.app.Application;
import android.content.Intent;

import me.crafter.android.zjsnviewer.service.TimerService;

/**
 * @author traburiss
 * @date 2016/6/6
 * @info ZjsnViewer
 * @desc
 */

public class zjsApplication extends Application{

    private static zjsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        startService(new Intent(this, TimerService.class));
    }

    public static zjsApplication getInstance() {
        return instance;
    }
}
