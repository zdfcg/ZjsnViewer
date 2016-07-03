package me.crafter.android.zjsnviewer.ui.time;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import me.crafter.android.zjsnviewer.util.JsonUtil;

/**
 * @author traburiss
 * @date 2016/6/17
 * @info ZjsnViewer
 * @desc
 */

public class MakeTimeActivity extends BaseFragmentActivity {

    private final String TAG = "BuildTimeActivity";

    @BindView(R.id.rv_time) RecyclerView rv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_time);
        setToolbarTitle(R.string.make_time_info);
        ButterKnife.bind(this);

        String json = JsonUtil.getJsonFromRaw(context,R.raw.equipment);

        ArrayList<Long> timelist = JsonUtil.TimesJsonGetTime(json);
        ArrayList<ArrayList<String>> nameslist = JsonUtil.TimesJsonGetName(json);

        TimeAdapter adapter = new TimeAdapter(context,timelist,nameslist);
        rv_time.setLayoutManager(new LinearLayoutManager(context));
        rv_time.setAdapter(adapter);
    }
}
