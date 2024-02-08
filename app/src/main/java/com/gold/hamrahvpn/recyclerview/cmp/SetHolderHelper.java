package com.gold.hamrahvpn.recyclerview.cmp;

import static com.gold.hamrahvpn.ServerActivity.DarkMode;
import static com.gold.hamrahvpn.ServerActivity.FileArray;
import static com.gold.hamrahvpn.recyclerview.MainAdapter.context;
import static com.gold.hamrahvpn.recyclerview.MainAdapter.finishActivityListener;
import static com.gold.hamrahvpn.Data.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.gold.hamrahvpn.EncryptData;
import com.gold.hamrahvpn.R;
import com.gold.hamrahvpn.recyclerview.MainAdapter;
import com.gold.hamrahvpn.util.CountryListManager;
import com.gold.hamrahvpn.util.LogManager;

import de.blinkt.openvpn.core.App;

/**
 * Dev by MehraB832 --> github
 * @MehraB832
 */
public class SetHolderHelper {

    public static void setItemHolder(OpenVpnServerList OpenVpnServerList, @NonNull MainAdapter.ViewHolder holder) {
        if (OpenVpnServerList != null) {

            holder.tv_country.setText(OpenVpnServerList.GetCity());
            holder.tv_country.setTypeface(holder.RobotoRegular);

            if (DarkMode.equals("true")) {
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorDarkText));
            } else {
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorLightText));

            }

            CountryListManager.OpenVpnSetServerList(OpenVpnServerList.GetImage(), holder.iv_flag);

            holder.ll_item.setOnClickListener(v -> {
                holder.ll_item.setBackgroundColor(context.getResources().getColor(R.color.colorSelectItem));
                holder.tv_country.setTextColor(context.getResources().getColor(R.color.colorDarkText));
                EncryptData En = new EncryptData();
                try {
//                    SharedPreferences SharedAppDetails = context.getSharedPreferences("connection_data", 0);
//                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
                    connectionStorage.putString("id", OpenVpnServerList.GetID());
                    connectionStorage.putString("file_id", OpenVpnServerList.GetFileID());
                    connectionStorage.putString("file", En.encrypt(FileArray[Integer.parseInt(OpenVpnServerList.GetFileID())][1]));
                    connectionStorage.putString("city", OpenVpnServerList.GetCity());
                    connectionStorage.putString("country", OpenVpnServerList.GetCountry());
                    connectionStorage.putString("image", OpenVpnServerList.GetImage());
                    connectionStorage.putString("ip", OpenVpnServerList.GetIP());
                    connectionStorage.putString("active", OpenVpnServerList.GetActive());
                    connectionStorage.putString("signal", OpenVpnServerList.GetSignal());
//                    connectionStorage.apply();
                    App.hasFile = true;
                    App.abortConnection = true;
                } catch (Exception e) {
                    Bundle params = new Bundle();
                    params.putString("device_id", App.device_id);
                    params.putString("exception", "SA6" + e);
                    LogManager.logEvent(params);
                }
                finishActivityListener.finishActivity();
            });


            switch (OpenVpnServerList.GetSignal()) {
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
    }

    public static void setBackgroundHolder(View itemView, int position) {
        SharedPreferences ConnectionDetails = context.getSharedPreferences("connection_data", 0);
        int ID = Integer.parseInt(ConnectionDetails.getString("id", "1"));

        if (position == ID) {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorSelectItem));
        } else {
            if (DarkMode.equals("true")) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkBackground));
            } else {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.colorLightBackground));

            }
        }
    }
}
