package com.gold.hamrahvpn.v2ray;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Keep;

import com.gold.hamrahvpn.v2ray.core.V2rayCore;
import com.gold.hamrahvpn.v2ray.core.V2rayStateUtil;
import com.gold.hamrahvpn.v2ray.core.utils;
import com.gold.hamrahvpn.v2ray.interfaces.V2rayStateListener;
import com.gold.hamrahvpn.v2ray.services.V2rayVPNService;
import java.util.ArrayList;

/**
 * by Mehrab
 * Keep all
 */
@Keep
public class V2rayController {
    public static void init(Context context) {
        utils.copyAssets(context);
    }

    public static void StartV2ray(Context context, String str, ArrayList<String> arrayList) {
        utils.BLOCKED_APPS = arrayList;
        Intent intent = new Intent(context, V2rayVPNService.class);
        intent.putExtra("ACTION", utils.FLAG_VPN_START);
        intent.putExtra("V2RAY_CONFIG", str);
        context.startService(intent);
    }

    public static void StopV2ray(Context context) {
        utils.BLOCKED_APPS = null;
        Intent intent = new Intent(context, V2rayVPNService.class);
        intent.putExtra("ACTION", utils.FLAG_VPN_STOP);
        context.startService(intent);
    }

    public static void registerStateChangeListener(V2rayStateListener v2rayStateListener) {
        new V2rayStateUtil(v2rayStateListener);
    }

    public static boolean isRunning() {
        return V2rayCore.isV2rayRunning();
    }

    public static String getV2rayDuration() {
        return V2rayVPNService.SERVICE_DURATION;
    }
}
