package com.gold.hamrahvpn.recyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.hamrahvpn.R;
import com.gold.hamrahvpn.ServerActivity;
import com.gold.hamrahvpn.openvpn.EncryptData;
import com.gold.hamrahvpn.util.FinishActivityListener;

import java.util.List;

import de.blinkt.openvpn.core.App;
//import com.squareup.picasso.Picasso

/**
 * Created by Daichi Furiya / Wasabeef on 2020/08/26.
 */

//public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
//
//    private Context context;
//    private List<String> dataSet;
//
//    public MainAdapter(Context context, List<String> dataSet) {
//        this.context = context;
//        this.dataSet = dataSet;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.server_list_item, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
////        Picasso.get().load(R.drawable.chip).into(holder.image);
//        holder.text.setText(dataSet.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataSet.size();
//    }
//
//    public void remove(int position) {
//        dataSet.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void add(String text, int position) {
//        dataSet.add(position, text);
//        notifyItemInserted(position);
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
//        TextView text;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
////      image = itemView.findViewById(R.id.image);
//            text = itemView.findViewById(R.id.text);
//        }
//    }
//}
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Server> dataSet;
    private Context context;
    private FinishActivityListener finishActivityListener;

    public MainAdapter(Context context, List<Server> dataSet, FinishActivityListener finishActivityListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.finishActivityListener = finishActivityListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.server_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Picasso.get().load(R.drawable.chip).into(holder.image);
//            holder.text.setText(dataSet.get(position));

        final Server Server = dataSet.get(position);

        if (Server != null) {

            holder.tv_country.setText(Server.GetCity());
            holder.tv_country.setTypeface(holder.RobotoRegular);

            if (ServerActivity.DarkMode.equals("true")) {
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorDarkText));
            } else {
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorLightText));

            }

            switch (Server.GetImage()) {
                case "japan":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_japan);
                    break;
                case "russia":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_russia);
                    break;
                case "southkorea":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_south_korea);
                    break;
                case "thailand":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_thailand);
                    break;
                case "vietnam":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_vietnam);
                    break;
                case "unitedstates":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_united_states);
                    break;
                case "unitedkingdom":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_united_kingdom);
                    break;
                case "singapore":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_singapore);
                    break;
                case "france":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_france);
                    break;
                case "germany":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_germany);
                    break;
                case "canada":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_canada);
                    break;
                case "luxemburg":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_luxemburg);
                    break;
                case "netherlands":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_netherlands);
                    break;
                case "spain":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_spain);
                    break;
                case "finland":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_finland);
                    break;
                case "poland":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_poland);
                    break;
                case "australia":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_australia);
                    break;
                case "italy":
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_italy);
                    break;
                default:
                    holder.iv_flag.setImageResource(R.drawable.ic_flag_unknown_mali);
                    break;
            }

            holder.ll_item.setOnClickListener(v -> {
                holder.ll_item.setBackgroundColor(context.getResources().getColor(R.color.colorSelectItem));
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorDarkText));
                EncryptData En = new EncryptData();
                try {
                    SharedPreferences SharedAppDetails = context.getSharedPreferences("connection_data", 0);
                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
                    Editor.putString("id", Server.GetID());
                    Editor.putString("file_id", Server.GetFileID());
                    Editor.putString("file", En.encrypt(ServerActivity.FileArray[Integer.valueOf(Server.GetFileID())][1]));
                    Editor.putString("city", Server.GetCity());
                    Editor.putString("country", Server.GetCountry());
                    Editor.putString("image", Server.GetImage());
                    Editor.putString("ip", Server.GetIP());
                    Editor.putString("active", Server.GetActive());
                    Editor.putString("signal", Server.GetSignal());
                    Editor.apply();
                    App.hasFile = true;
                    App.abortConnection = true;
                } catch (Exception e) {
//                            Bundle params = new Bundle();
//                            params.putString("device_id", App.device_id);
//                            params.putString("exception", "SA6" + e.toString());
//                            mFirebaseAnalytics.logEvent("app_param_error", params);
                }
                finishActivityListener.finishActivity();
            });


            switch (Server.GetSignal()) {
                case "a":
                    holder.iv_signal_strength.setBackgroundResource(R.drawable.ic_signal_full);
                    break;
                case "b":
                    holder.iv_signal_strength.setBackgroundResource(R.drawable.ic_signal_normal);
                    break;
                case "c":
                    holder.iv_signal_strength.setBackgroundResource(R.drawable.ic_signal_medium);
                    break;
//                case "d":
//                    holder.iv_signal_strength.setBackgroundResource(R.drawable.ic_signal_low);
//                    break;
                default:
                    holder.iv_signal_strength.setBackgroundResource(R.drawable.ic_signal_low);
                    break;
            }

        } else {
            holder.showBool.setVisibility(View.VISIBLE);
        }

        // bottom
        SharedPreferences ConnectionDetails = context.getSharedPreferences("connection_data", 0);
        int ID = Integer.parseInt(ConnectionDetails.getString("id", "1"));

        if (position == ID) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorSelectItem));
        } else {
            if (ServerActivity.DarkMode.equals("true")) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

//    public void remove(int position) {
//        dataSet.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void add(String text, int position) {
////            dataSet.add(position, text);
//        notifyItemInserted(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_country;
        View vitem;
        Typeface RobotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

        TextView showBool = itemView.findViewById(R.id.boolShowListServer);
        ImageView iv_flag;
        ImageView iv_signal_strength;
        final LinearLayout ll_item = itemView.findViewById(R.id.ll_item);

        public ViewHolder(View itemView) {
            super(itemView);
//      image = itemView.findViewById(R.id.image);
//                text = itemView.findViewById(R.id.text);
            vitem = itemView;
            tv_country = itemView.findViewById(R.id.tv_country);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            iv_signal_strength = itemView.findViewById(R.id.iv_signal_strength);

        }
    }
}

