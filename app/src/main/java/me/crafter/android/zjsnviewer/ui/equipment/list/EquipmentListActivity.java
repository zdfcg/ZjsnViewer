package me.crafter.android.zjsnviewer.ui.equipment.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.config.ConfigFile;
import me.crafter.android.zjsnviewer.service.retrofit.epuimentlist.EquipmentListApi;
import me.crafter.android.zjsnviewer.service.retrofit.epuimentlist.EquipmentListRepo;
import me.crafter.android.zjsnviewer.ui.BaseFragmentActivity;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EquipmentListActivity extends BaseFragmentActivity {

    @BindView(R.id.sr_equipment) SwipeRefreshLayout sr_equipment;
    @BindView(R.id.rv_equipment) RecyclerView rv_equipment;

    private Retrofit retrofit;
    private EquipmentListApi equipmentListApi;
    private EquipmentAdapter adapter;
    private boolean need_load_more = true;
    private boolean isLoadingMore = false;
    private int page = 1;
    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_equipment);
        ButterKnife.bind(this);

        initView();
        initEven();
        initData();
    }

    private void initView(){

        setToolbarTitle(R.string.equipment);

        sr_equipment.setColorSchemeResources(R.color.load_blue, R.color.load_green, R.color.load_yellow);

        mLayoutManager = new LinearLayoutManager(context);
        rv_equipment.setLayoutManager(mLayoutManager);
        adapter = new EquipmentAdapter(context, new ArrayList<>());
        rv_equipment.setAdapter(adapter);
    }

    private void initEven(){

        sr_equipment.setOnRefreshListener(() -> {

            page = 1;
            LoadData(page);
        });
        rv_equipment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (need_load_more & !isLoadingMore){

                    int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                    int totalItemCount = adapter.getItemCount();
                    if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {

                        page ++;
                        LoadData(page);
                    }
                }
            }
        });
    }

    private void initData(){
        retrofit= new Retrofit.Builder()
                .baseUrl(ConfigFile.SERVICE_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        equipmentListApi = retrofit.create(EquipmentListApi.class);

        sr_equipment.setRefreshing(true);
        LoadData(page);
    }

    private void LoadData(int page_num){

        isLoadingMore = true;
        JSONObject send_params = new JSONObject();
        try {

            send_params.put("PAGE_SIZE", ConfigFile.PAGE_SIZE);
            send_params.put("PAGE_NUM", page_num);
            equipmentListApi.get_equipment_list(ConfigFile.EQUIPMENT_LIST_FUNCTION_CODE, send_params.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<EquipmentListRepo>() {
                        @Override
                        public void onCompleted() {
                            sr_equipment.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {

                            sr_equipment.setRefreshing(false);
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            need_load_more = false;
                            isLoadingMore = false;
                        }

                        @Override
                        public void onNext(EquipmentListRepo equipmentListRepo) {

                            isLoadingMore = false;
                            sr_equipment.setRefreshing(false);
                            if (equipmentListRepo.getError_info().equalsIgnoreCase(ConfigFile.SERVICE_SUCCESS)){

                                if (page_num == 1) adapter.setList(equipmentListRepo.getResult());
                                else adapter.addList(equipmentListRepo.getResult());

                                if (equipmentListRepo.getResult().size() < ConfigFile.PAGE_SIZE) need_load_more = false;
                                else need_load_more = true;
                            }else {

                                Toast.makeText(context, equipmentListRepo.error_info, Toast.LENGTH_SHORT).show();
                                need_load_more = false;
                            }
                        }
                    });
        }catch (Exception e){

            Toast.makeText(this,  R.string.service_error_load_error, Toast.LENGTH_SHORT).show();
        }
    }
}
