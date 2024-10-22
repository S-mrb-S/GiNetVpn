package com.gold.hamrahvpn.util;

import android.widget.ImageView;

import com.gold.hamrahvpn.R;

public class CountryListManager {

    public static void OpenVpnSetServerList(String boolSwitch, ImageView holderIv){
        switch (boolSwitch) {
            case "japan":
                holderIv.setImageResource(R.drawable.ic_flag_japan);
                break;
            case "russia":
                holderIv.setImageResource(R.drawable.ic_flag_russia);
                break;
            case "southkorea":
                holderIv.setImageResource(R.drawable.ic_flag_south_korea);
                break;
            case "thailand":
                holderIv.setImageResource(R.drawable.ic_flag_thailand);
                break;
            case "vietnam":
                holderIv.setImageResource(R.drawable.ic_flag_vietnam);
                break;
            case "unitedstates":
                holderIv.setImageResource(R.drawable.ic_flag_united_states);
                break;
            case "unitedkingdom":
                holderIv.setImageResource(R.drawable.ic_flag_united_kingdom);
                break;
            case "singapore":
                holderIv.setImageResource(R.drawable.ic_flag_singapore);
                break;
            case "france":
                holderIv.setImageResource(R.drawable.ic_flag_france);
                break;
            case "germany":
                holderIv.setImageResource(R.drawable.ic_flag_germany);
                break;
            case "canada":
                holderIv.setImageResource(R.drawable.ic_flag_canada);
                break;
            case "luxemburg":
                holderIv.setImageResource(R.drawable.ic_flag_luxemburg);
                break;
            case "netherlands":
                holderIv.setImageResource(R.drawable.ic_flag_netherlands);
                break;
            case "spain":
                holderIv.setImageResource(R.drawable.ic_flag_spain);
                break;
            case "finland":
                holderIv.setImageResource(R.drawable.ic_flag_finland);
                break;
            case "poland":
                holderIv.setImageResource(R.drawable.ic_flag_poland);
                break;
            case "australia":
                holderIv.setImageResource(R.drawable.ic_flag_australia);
                break;
            case "italy":
                holderIv.setImageResource(R.drawable.ic_flag_italy);
                break;
            default:
                holderIv.setImageResource(R.drawable.ic_flag_unknown_mali);
                break;
        }
    }
}
