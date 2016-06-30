package me.crafter.android.zjsnviewer.ui.info.infoactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v4.view.RxViewPager;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.config.Storage;
import me.crafter.android.zjsnviewer.service.task.UpdateTask;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import me.crafter.android.zjsnviewer.ui.info.infofragment.BuildFragment;
import me.crafter.android.zjsnviewer.ui.info.infofragment.MakeFragment;
import me.crafter.android.zjsnviewer.ui.info.infofragment.RepairFragment;
import me.crafter.android.zjsnviewer.ui.info.infofragment.TravelFragment;
import me.crafter.android.zjsnviewer.ui.preference.main.ZjsnViewer;
import me.crafter.android.zjsnviewer.ui.time.BuildTimeActivity;
import me.crafter.android.zjsnviewer.ui.time.MakeTimeActivity;
import me.crafter.android.zjsnviewer.ui.web.WebActivity;
import me.crafter.android.zjsnviewer.util.DockInfo;

public class InfoActivity extends BaseFragmentActivity {

    @BindView(R.id.dl_drawer)
    DrawerLayout dl_drawer;

//    @BindView(R.id.toolbar)
//    Toolbar bar;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_level)
    TextView tv_level;

    @BindView(R.id.tv_travel)
    TextView tv_travel;
    @BindView(R.id.tv_repair)
    TextView tv_repair;
    @BindView(R.id.tv_build)
    TextView tv_build;
    @BindView(R.id.tv_make)
    TextView tv_make;
    @BindView(R.id.vp_page)
    ViewPager vp_page;

    @BindView(R.id.ib_icon)
    ImageButton ib_icon;

    @BindView(R.id.tv_drawer_name)
    TextView tv_drawer_name;
    @BindView(R.id.tv_drawer_level)
    TextView tv_drawer_level;
    @BindView(R.id.tv_build_time)
    TextView tv_build_time;

    @BindView(R.id.sw_title_on)
    Switch sw_title_on;
    @BindView(R.id.sw_title_auto_run)
    Switch sw_title_auto_run;

    @BindView(R.id.tv_goto_build_time) TextView tv_goto_build_time;
    @BindView(R.id.tv_goto_make_time) TextView tv_goto_make_time;
    @BindView(R.id.tv_web) TextView tv_web;
    @BindView(R.id.tv_setting) TextView tv_setting;

    private Context context;

    private ArrayList<TextView> tabs;
    private TravelFragment travelFragment;
    private BuildFragment buildFragment;
    private MakeFragment makeFragment;
    private RepairFragment repairFragment;

    private Handler handler;
    private Runnable runnable;
    private int RUN_TIME = 60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        initData();
        initView();
        initEven();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean on = preferences.getBoolean("on", true);
        Boolean auto = preferences.getBoolean("auto_run", true);
        sw_title_on.setChecked(on);
        sw_title_auto_run.setChecked(auto);

        handler.postDelayed(runnable, 2 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void initData(){

        context = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString("username", "none");
        String password = prefs.getString("password", "none");
        if (username.contains("none") &&  password.contains("none")) Toast.makeText(context, R.string.username_hint, Toast.LENGTH_SHORT).show();
    }

    private void initView(){

        refreshView();
        initFragment();
    }

    private void initEven(){

        RxView.clicks(ib_icon).subscribe(aVoid -> {

            DockInfo.updateInterval = 0;
            final ProgressDialog progressDialog = ProgressDialog.show(context, "", getString(R.string.loading));
            progressDialog.setCancelable(true);
            UpdateTask task = new UpdateTask(context);
            task.setUpdateTaskStateChange(()-> {

                    progressDialog.dismiss();
                    refreshAllView();
                });
            task.execute();
        });

        RxViewPager.pageSelections(vp_page).subscribe(this::setTab);

        for(final TextView textView : tabs){

            View view = (View) textView.getParent();
            RxView.clicks(view).subscribe(aVoid -> {

                int position = (int) textView.getTag();
                vp_page.setCurrentItem(position);
                setTab(position);
            });
        }

        handler = new Handler();
        runnable = () -> {

            refreshAllView();
            handler.postDelayed(runnable,RUN_TIME);
        };

        sw_title_on.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (!preferences.edit().putBoolean("on", isChecked).commit()){

                sw_title_on.setChecked(!isChecked);
            }
        });

        sw_title_auto_run.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (!preferences.edit().putBoolean("auto_run", isChecked).commit()){

                sw_title_auto_run.setChecked(!isChecked);
            }
        });

        RxView.clicks(tv_goto_build_time).subscribe(aVoid -> startActivity(BuildTimeActivity.class));
        RxView.clicks(tv_goto_make_time).subscribe(aVoid -> startActivity(MakeTimeActivity.class));
        RxView.clicks(tv_web).subscribe(aVoid -> startActivity(WebActivity.class));
        RxView.clicks(tv_setting).subscribe(aVoid -> startActivity(ZjsnViewer.class));
    }

    private void initFragment(){

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(travelFragment = new TravelFragment());
        fragments.add(repairFragment = new RepairFragment());
        fragments.add(buildFragment = new BuildFragment());
        fragments.add(makeFragment = new MakeFragment());

        tabs = new ArrayList<>();
        tabs.add(setTabView(tv_travel, 0, true));
        tabs.add(setTabView(tv_repair, 1, false));
        tabs.add(setTabView(tv_build, 2, false));
        tabs.add(setTabView(tv_make, 3, false));

        vp_page.setOffscreenPageLimit(4);
        pageAdapter adapter = new pageAdapter(getSupportFragmentManager(), fragments);

        vp_page.setAdapter(adapter);
        vp_page.setCurrentItem(0);
    }

    private TextView setTabView(TextView textView, int position, boolean enable){

        textView.setTag(position);
        textView.setEnabled(enable);
        return textView;
    }

    private void setTab(int position) {

        if (position >= 0 && position < tabs.size()){

            for (int i = 0; i < tabs.size(); i++){

                if (i == position) tabs.get(i).setEnabled(true);
                else tabs.get(i).setEnabled(false);
            }
        }
    }


    private class pageAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragments;

        pageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void refreshAllView(){

        refreshView();

        buildFragment.refreshView();
        repairFragment.refreshView();
        makeFragment.refreshView();
        travelFragment.refreshView();
    }

    private void refreshView(){

        try {

            tv_name.setText(Storage.str_tiduName);
            tv_drawer_name.setText(Storage.str_tiduName);
            tv_level.setText("Level: " + DockInfo.level + " (" + DockInfo.exp + "/" + DockInfo.nextExp + ")");
            tv_drawer_level.setText("Level: " + DockInfo.level + " (" + DockInfo.exp + "/" + DockInfo.nextExp + ")");
            if (DockInfo.level.equals("150")){
                tv_level.setText(R.string.max);
                tv_drawer_level.setText(R.string.max);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
