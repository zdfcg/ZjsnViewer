package me.crafter.android.zjsnviewer.ui.web;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;

public class WebActivity extends BaseFragmentActivity{

    @BindView(R.id.rv_web)
    RecyclerView rv_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        ButterKnife.bind(this);

        setToolbarTitle(R.string.web_site);

        WebAdapter adapter = new WebAdapter(context, getList());
        rv_web.setLayoutManager(new LinearLayoutManager(context));
        rv_web.setAdapter(adapter);
    }


    public ArrayList<HashMap<String, String>> getList() {

        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.web_site_names);
        String[] urls = getResources().getStringArray(R.array.web_site_urls);
        String[] icons = getResources().getStringArray(R.array.web_site_icons);

        for (int i = 0; i< names.length; i++){

            list.add(changeNameAndUrlAndIcon(names[i], urls[i], icons[i]));
        }
        return list;
    }

    private HashMap<String,String> changeNameAndUrlAndIcon(String name, String Url, String Icon_Url){

        HashMap<String,String> item = new HashMap<>();
        item.put("WEB_NAME", name);
        item.put("WEB_URL", Url);
        item.put("WEB_ICON", Icon_Url);

        return item;
    }
}
