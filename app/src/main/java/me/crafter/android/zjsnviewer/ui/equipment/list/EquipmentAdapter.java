package me.crafter.android.zjsnviewer.ui.equipment.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.crafter.android.zjsnviewer.R;
import me.crafter.android.zjsnviewer.ui.equipment.info.EquipmentInfoActivity;

/**
 * @author traburiss
 * @date 2016/7/22
 * @info ZjsnViewer
 * @desc
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentHolder>{

    private Context context;
    private ArrayList<HashMap<String, Object>> list;

    public EquipmentAdapter(Context context, ArrayList<HashMap<String, Object>> list){

        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<HashMap<String, Object>> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<HashMap<String, Object>> list){

        if (null != this.list) this.list.addAll(list);
        else setList(list);
        notifyDataSetChanged();
    }

    @Override
    public EquipmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new EquipmentHolder(LayoutInflater.from(context).inflate(R.layout.item_equipment, parent, false));
    }

    @Override
    public void onBindViewHolder(EquipmentHolder holder, int position) {

        HashMap<String, Object> item = list.get(position);
        String icon_url = item.get("equipment_pic").toString();
        String name = item.get("equipment_name").toString();
        String type = item.get("equipment_type").toString();
        String id = item.get("equipment_id").toString();
        String power = getPower(item);
        String time = context.getString(R.string.equipment_time) + ":" + item.get("equipment_make_time").toString();
        String threshold = context.getString(R.string.equipment_threshold) + ":" + item.get("equipment_make_threshold").toString();
        String from = context.getString(R.string.equipment_from) + ":" + item.get("equipment_from").toString();

        Glide.with(context).load(icon_url).error(R.drawable.pic_error).into(holder.iv_equipment_icon);

        int level = Integer.valueOf(item.get("equipment_level").toString());
        level = level < 1 || level > 6 ? 1 : level;
        int color = context.getResources().obtainTypedArray(R.array.level_color).getColor(level-1,context.getResources().getColor(R.color.range_1_color));
        holder.tv_equipment_name.setTextColor(color);
        holder.tv_equipment_name.setText(name);

        holder.tv_equipment_type.setText(type);

        holder.tv_equipment_id.setText(id);

        holder.tv_equipment_power.setText(power);

        holder.tv_equipment_time.setText(time);

        holder.tv_equipment_threshold.setText(threshold);

        holder.tv_equipment_from.setText(from);

        holder.itemView.setOnClickListener((view)-> {
            Intent intent = new Intent(context, EquipmentInfoActivity.class);
            intent.putExtra("data", item);
            context.startActivity(intent);
        });
    }

    private String getPower(HashMap item){

        StringBuilder power = new StringBuilder();
        String equipment_range = item.get("equipment_range").toString();
        if (!equipment_range.isEmpty()) power.append(context.getString(R.string.equipment_range)).append(":").append(equipment_range);

        int equipment_fire = Integer.valueOf(item.get("equipment_fire").toString());
        if (equipment_fire != 0) power.append("|").append(context.getString(R.string.equipment_fire)).append(":").append(equipment_fire);

        int equipment_torpedo = Integer.valueOf(item.get("equipment_torpedo").toString());
        if (equipment_torpedo != 0) power.append("|").append(context.getString(R.string.equipment_torpedo)).append(":").append(equipment_torpedo);

        int equipment_antisubmarine = Integer.valueOf(item.get("equipment_antisubmarine").toString());
        if (equipment_antisubmarine != 0) power.append("|").append(context.getString(R.string.equipment_antisubmarine)).append(":").append(equipment_antisubmarine);

        int equipment_boom = Integer.valueOf(item.get("equipment_boom").toString());
        if (equipment_boom != 0) power.append("|").append(context.getString(R.string.equipment_boom)).append(":").append(equipment_boom);

        int equipment_antiair = Integer.valueOf(item.get("equipment_antiair").toString());
        if (equipment_antiair != 0) power.append("|").append(context.getString(R.string.equipment_antiair)).append(":").append(equipment_antiair);

        int equipment_armor = Integer.valueOf(item.get("equipment_armor").toString());
        if (equipment_armor != 0) power.append("|").append(context.getString(R.string.equipment_armor)).append(":").append(equipment_armor);

        int equipment_hit = Integer.valueOf(item.get("equipment_hit").toString());
        if (equipment_hit != 0) power.append("|").append(context.getString(R.string.equipment_hit)).append(":").append(equipment_hit);

        int equipment_flee = Integer.valueOf(item.get("equipment_flee").toString());
        if (equipment_flee != 0) power.append("|").append(context.getString(R.string.equipment_flee)).append(":").append(equipment_flee);

        int equipment_tracking = Integer.valueOf(item.get("equipment_tracking").toString());
        if (equipment_tracking != 0) power.append("|").append(context.getString(R.string.equipment_tracking)).append(":").append(equipment_tracking);

        int equipment_lucky = Integer.valueOf(item.get("equipment_lucky").toString());
        if (equipment_lucky != 0) power.append("|").append(context.getString(R.string.equipment_lucky)).append(":").append(equipment_lucky);

//        float equipment_antiair_correct = Float.valueOf(item.get("equipment_antiair_correct").toString());
//        NumberFormat percent_format = NumberFormat.getPercentInstance();
//        if (equipment_antiair_correct != 0)
//            power.append("|").append(context.getString(R.string.equipment_antiair_correct)).append(":").append(percent_format.format(equipment_antiair_correct));

        String equipment_special_effect = item.get("equipment_special_effect").toString();
        if (!equipment_special_effect.isEmpty()) power.append("|").append(context.getString(R.string.equipment_special_effect)).append(":").append(equipment_special_effect);

        return power.toString();
    }

    @Override
    public int getItemCount() {

        return null == this.list? 0: this.list.size();
    }

    public class EquipmentHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_equipment_icon) ImageView iv_equipment_icon;
        @BindView(R.id.tv_equipment_name) TextView tv_equipment_name;
        @BindView(R.id.tv_equipment_type) TextView tv_equipment_type;
        @BindView(R.id.tv_equipment_id) TextView tv_equipment_id;
        @BindView(R.id.tv_equipment_power) TextView tv_equipment_power;
        @BindView(R.id.tv_equipment_time) TextView tv_equipment_time;
        @BindView(R.id.tv_equipment_threshold) TextView tv_equipment_threshold;
        @BindView(R.id.tv_equipment_from) TextView tv_equipment_from;

        public EquipmentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
