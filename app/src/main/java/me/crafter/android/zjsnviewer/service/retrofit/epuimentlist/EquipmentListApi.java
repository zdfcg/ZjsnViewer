package me.crafter.android.zjsnviewer.service.retrofit.epuimentlist;

import me.crafter.android.zjsnviewer.config.ConfigFile;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author traburiss
 * @date 2016/7/25
 * @info ZjsnViewer
 * @desc
 */

public interface EquipmentListApi {

    @GET(ConfigFile.SERVICE_NAME)
    Observable<EquipmentListRepo> get_equipment_list(@Query("function_code") String function_code, @Query("send_params") String send_params);
}
