package me.crafter.android.zjsnviewer.ui.time;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.util.JsonUtil;
import rx.Observable;

/**
 * @author traburiss
 * @date 2016/6/17
 * @info ZjsnViewer
 * @desc
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{

    private Context context;
    private ArrayList<Long> parent;
    private ArrayList<ArrayList<String>> child;
    private ArrayList<HashMap<String, String>> list;

    private final String TIME = "time";
    private final String NAME = "name";

    private int position = 0;

    public TimeAdapter(Context context, ArrayList<Long> parent, ArrayList<ArrayList<String>> child){

        this.context = context;
        this.parent = parent;
        this.child = child;
        setList();
    }

    private void setList(){

        list = new ArrayList<>();

        Observable.from(parent)
                .map(JsonUtil::long2hms)
                .map(s -> getHahMap(TIME,s))
                .subscribe(stringStringHashMap -> {

                    list.add(stringStringHashMap);
                    Observable.from(child.get(position))
                            .map(s -> getHahMap(NAME, s))
                            .subscribe(stringStringHashMap1 -> list.add(stringStringHashMap1));
                    position++;
                });
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TimeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_time, parent, false));
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {

        HashMap<String, String> item = list.get(position);
        if (item.containsKey(TIME)){

            float font_size = context.getResources().getDimension(R.dimen.text_size_22);
            int padding_size = context.getResources().getDimensionPixelSize(R.dimen.dp_size_15);

            holder.tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, font_size);
            holder.tv_time.setBackgroundResource(R.color.background_white);
            holder.tv_time.setPadding(padding_size, padding_size, padding_size, padding_size);
            RxTextView.text(holder.tv_time).call(item.get(TIME));
        }else {

            float font_size = context.getResources().getDimension(R.dimen.text_size_22);
            int padding_size = context.getResources().getDimensionPixelSize(R.dimen.dp_size_15);
            int padding_left_size = context.getResources().getDimensionPixelSize(R.dimen.dp_size_25);

            holder.tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, font_size);
            holder.tv_time.setBackgroundResource(R.color.background_grey);
            holder.tv_time.setPadding(padding_left_size, padding_size, padding_size, padding_size);
            RxTextView.text(holder.tv_time).call(item.get(NAME));
        }
    }

    @Override
    public int getItemCount() {

        if (null == list) return 0;
        return list.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView tv_time;

        public TimeViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private HashMap<String, String> getHahMap(String key, String value){

        HashMap<String,String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
