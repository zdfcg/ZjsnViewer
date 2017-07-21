package me.crafter.android.zjsnviewer.ui.equipment.info;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import me.crafter.android.zjsnviewer.ui.time.TimeAdapter;
import me.crafter.android.zjsnviewer.util.JsonUtil;

public class EquipmentInfoActivity extends BaseFragmentActivity {

    @BindView(R.id.rv_second_test) RecyclerView rv_second_test;
    @BindView(R.id.iv_equipment_icon) ImageView iv_equipment_icon;
    @BindView(R.id.cl_toolbar) CollapsingToolbarLayout cl_toolbar;

    private HashMap info_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);
        ButterKnife.bind(this);
        initData();
        initView();
        initEven();
    }

    private void initData(){

        Serializable serializable = getIntent().getSerializableExtra("data");
        if (serializable instanceof HashMap){

            info_data = (HashMap) getIntent().getSerializableExtra("data");
        }
    }

    private void initView(){

        int level = Integer.valueOf(String.valueOf(info_data.get("equipment_level")));
        level = level < 1 || level > 6 ? 1 : level;
        cl_toolbar.setBackground(getResources().obtainTypedArray(R.array.level_background).getDrawable(level-1));

        String icon_url = String.valueOf(info_data.get("equipment_pic"));
        Glide.with(context).load(icon_url).error(R.drawable.pic_error).crossFade().into(iv_equipment_icon);

        String name = String.valueOf(info_data.get("equipment_name"));
        setToolbarTitle(name);
        cl_toolbar.setExpandedTitleColor(getResources().getColor(R.color.font_alpha));
        cl_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.font_white));

        String json = JsonUtil.getJsonFromRaw(context,R.raw.buildtime);

        ArrayList<Long> time_list = JsonUtil.TimesJsonGetTime(json);
        ArrayList<ArrayList<String>> names_list = JsonUtil.TimesJsonGetName(json);

        TimeAdapter adapter = new TimeAdapter(context,time_list,names_list);
        rv_second_test.setLayoutManager(new LinearLayoutManager(context));
        rv_second_test.setAdapter(adapter);
    }

    private void initEven(){


    }
}
