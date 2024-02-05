package com.gold.hamrahvpn.v2ray.core;

import android.os.Handler;

import com.gold.hamrahvpn.v2ray.interfaces.V2rayStateListener;
import com.gold.hamrahvpn.v2ray.services.V2rayVPNService;


public class V2rayStateUtil {
    private final V2rayStateListener v2rayStateListener;
    private final Runnable v2ray_checker;
    private int v2ray_last_state = 424344;
    public final int check_delay_time = 1000;

    public V2rayStateUtil(V2rayStateListener v2rayStateListener) {
        Runnable runnable = new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                if ((V2rayVPNService.V2RAY_TUN_STATE == 3813 || V2rayVPNService.V2RAY_TUN_STATE == 4309 || V2rayVPNService.V2RAY_TUN_STATE == 4329) && V2rayVPNService.V2RAY_TUN_STATE != V2rayStateUtil.this.v2ray_last_state) {
                    V2rayStateUtil.this.v2rayStateListener.onV2rayStateChange(V2rayVPNService.V2RAY_TUN_STATE);
                    V2rayStateUtil.this.v2ray_last_state = V2rayVPNService.V2RAY_TUN_STATE;
                }
                new Handler().postDelayed(V2rayStateUtil.this.v2ray_checker, 1000L);
            }
        };
        this.v2ray_checker = runnable;
        this.v2rayStateListener = v2rayStateListener;
        new Handler().post(runnable);
    }
}
