package me.crafter.android.zjsnviewer.ui.equipment.info;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;

public class EquipmentInfoActivity extends BaseFragmentActivity {

    @BindView(R.id.rv_second_test) RecyclerView rv_second_test;
    @BindView(R.id.iv_equipment_icon) ImageView iv_equipment_icon;
    @BindView(R.id.cl_toolbar) CollapsingToolbarLayout cl_toolbar;

    private HashMap<String, Objects> info_data;

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

        info_data = (HashMap<String, Objects>) getIntent().getSerializableExtra("data");
    }

    private void initView(){

        String icon_url = String.valueOf(info_data.get("equipment_pic"));
        Glide.with(context).load(icon_url).error(R.drawable.pic_error).into(iv_equipment_icon);

        int level = Integer.valueOf(String.valueOf(info_data.get("equipment_level")));
        level = level < 1 || level > 6 ? 1 : level;
        iv_equipment_icon.setBackground(getResources().obtainTypedArray(R.array.level_background).getDrawable(level-1));

        String name = String.valueOf(info_data.get("equipment_name"));
        setToolbarTitle(name);
        cl_toolbar.setExpandedTitleColor(getResources().getColor(R.color.font_alpha));
        cl_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.font_white));

//        String json = JsonUtil.getJsonFromRaw(context,R.raw.buildtime);
//
//        ArrayList<Long> timelist = JsonUtil.TimesJsonGetTime(json);
//        ArrayList<ArrayList<String>> nameslist = JsonUtil.TimesJsonGetName(json);
//
//        TimeAdapter adapter = new TimeAdapter(context,timelist,nameslist);
//        rv_second_test.setLayoutManager(new LinearLayoutManager(context));
//        rv_second_test.setAdapter(adapter);
    }

    private void initEven(){


    }
}
