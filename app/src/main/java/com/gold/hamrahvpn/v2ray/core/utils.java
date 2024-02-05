package com.gold.hamrahvpn.v2ray.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class utils {
    static final boolean $assertionsDisabled = false;
    public static final String APPLICATION_NAME = "V2ray Lib";
    public static ArrayList<String> BLOCKED_APPS = null;
    public static final int FLAG_VPN_START = 3198;
    public static final int FLAG_VPN_STOP = 3149;
    public static final int LOCAL_SOCKS5_PORT = 10808;
    public static final int V2RAY_TUN_CONNECTED = 3813;
    public static final int V2RAY_TUN_CONNECTING = 4309;
    public static final int V2RAY_TUN_DISCONNECTED = 4329;

    public static void CopyFiles(InputStream inputStream, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static String getUserAssetsPath(Context context) {
        File externalFilesDir = context.getExternalFilesDir("assets");
        if (externalFilesDir == null) {
            return "";
        }
        if (!externalFilesDir.exists()) {
            return context.getDir("assets", 0).getAbsolutePath();
        }
        return externalFilesDir.getAbsolutePath();
    }

    public static String getDeviceIdForXUDPBaseKey() {
        String androidId = Settings.Secure.ANDROID_ID;
        byte[] androidIdBytes = androidId.getBytes(StandardCharsets.UTF_8);
        byte[] truncatedAndroidId = Arrays.copyOf(androidIdBytes, 32);
        return Base64.encodeToString(truncatedAndroidId, Base64.NO_PADDING | Base64.URL_SAFE);
    }

    public static void copyAssets(Context context) {
        String[] list;
        String userAssetsPath = getUserAssetsPath(context);
        try {
            for (String str : Objects.requireNonNull(context.getAssets().list(""))) {
                if ("geosite.dat,geoip.dat".contains(str)) {
                    CopyFiles(context.getAssets().open(str), new File(userAssetsPath, str));
                }
            }
            Log.e("COPY_ASSETS", "SUCCESS");
        } catch (Exception e) {
            Log.e("COPY_ASSETS", "FAILED=>", e);
        }
    }

    public static Notification getVpnServiceNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String packageName = context.getPackageName();
            NotificationChannel notificationChannel = new NotificationChannel(packageName, "V2ray Lib Background Service", NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            return new NotificationCompat.Builder(context, packageName).setOngoing(true).setContentTitle("Location : USA 7").setContentText("16.6 kbit/s 779.8 kB 2.2 kbit/s 259.2 kB").setPriority(1).setCategory("service").build();
        }

        return null;
    }

    public static String convertTwoDigit(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }
}
