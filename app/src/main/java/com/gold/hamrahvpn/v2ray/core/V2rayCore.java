package com.gold.hamrahvpn.v2ray.core;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.gold.hamrahvpn.v2ray.interfaces.FBProtectListener;

import org.json.JSONObject;

import libv2ray.Libv2ray;
import libv2ray.V2RayPoint;
import libv2ray.V2RayVPNServiceSupportsSet;


/**
 * by Mehrab
 */
public class V2rayCore {
    public static FBProtectListener fbProtectListener;
    private static final V2RayPoint v2RayPoint;

    static {
        v2RayPoint = Libv2ray.newV2RayPoint(new V2RayVPNServiceSupportsSet() {
            public long onEmitStatus(long j, String str) {
                return 0L;
            }

            public long prepare() {
                return 0L;
            }

            public long setup(String str) {
                return 0L;
            }

            public long shutdown() {
                return 0L;
            }

            public boolean protect(long j) {
                if (V2rayCore.fbProtectListener != null) {
                    return V2rayCore.fbProtectListener.onProtect((int) j);
                }
                return true;
            }
        }, Build.VERSION.SDK_INT >= 25);
    }

    public static boolean isV2rayRunning() {
        V2RayPoint v2RayPoint2 = v2RayPoint;
        if (v2RayPoint2 == null) {
            return false;
        }
        return v2RayPoint2.getIsRunning();
    }

    public static boolean start(Context context, String str) {
        String string;
        String string2;
        Libv2ray.initV2Env(utils.getUserAssetsPath(context), utils.getDeviceIdForXUDPBaseKey());
        try {
            JSONObject jSONObject = new JSONObject(str);
            try {
                string = jSONObject.getJSONArray("outbounds").getJSONObject(0).getJSONObject("settings").getJSONArray("vnext").getJSONObject(0).getString("address");
                string2 = jSONObject.getJSONArray("outbounds").getJSONObject(0).getJSONObject("settings").getJSONArray("vnext").getJSONObject(0).getString("port");
            } catch (Exception unused) {
                string = jSONObject.getJSONArray("outbounds").getJSONObject(0).getJSONObject("settings").getJSONArray("servers").getJSONObject(0).getString("address");
                string2 = jSONObject.getJSONArray("outbounds").getJSONObject(0).getJSONObject("settings").getJSONArray("servers").getJSONObject(0).getString("port");
            }
            V2RayPoint v2RayPoint2 = v2RayPoint;
            v2RayPoint2.setConfigureFileContent(str);
            v2RayPoint2.setDomainName(string + ":" + string2);
            v2RayPoint2.runLoop(false);
            return true;
        } catch (Exception e) {
            Log.e("V2RAY_CORE_START", "FAILED=>", e);
            return false;
        }
    }

    public static void stop() {
        try {
            v2RayPoint.stopLoop();
        } catch (Exception e) {
            Log.e("V2RAY_CORE_STOP", "FAILED=>", e);
        }
    }
}
