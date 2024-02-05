package com.gold.hamrahvpn.v2ray.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.VpnService;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.system.OsConstants;
import android.util.Log;

import com.gold.hamrahvpn.v2ray.core.V2rayCore;
import com.gold.hamrahvpn.v2ray.core.utils;
import com.gold.hamrahvpn.v2ray.interfaces.FBProtectListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class V2rayVPNService extends VpnService implements FBProtectListener, Runnable {
    public static String SERVICE_DURATION = "00:00:00";
    public static int V2RAY_TUN_STATE = 34298472;
    private CountDownTimer countDownTimer;
    private int hours;
    private ParcelFileDescriptor mInterface;
    private Thread mThread;
    private int minutes;
    private Process process;
    private int seconds;

    static void access008(V2rayVPNService v2rayVPNService) {
        int i = v2rayVPNService.seconds;
        v2rayVPNService.seconds = i + 1;
    }

    static void access108(V2rayVPNService v2rayVPNService) {
        int i = v2rayVPNService.minutes;
        v2rayVPNService.minutes = i + 1;
    }

    static void access208(V2rayVPNService v2rayVPNService) {
        int i = v2rayVPNService.hours;
        v2rayVPNService.hours = i + 1;
    }


    public void makeDurationTimer() {
        this.countDownTimer = new CountDownTimer(300000000L, 1000L) {
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                V2rayVPNService.access008(V2rayVPNService.this);
                if (V2rayVPNService.this.seconds == 59) {
                    V2rayVPNService.access108(V2rayVPNService.this);
                    V2rayVPNService.this.seconds = 0;
                }
                if (V2rayVPNService.this.minutes == 59) {
                    V2rayVPNService.this.minutes = 0;
                    V2rayVPNService.access208(V2rayVPNService.this);
                }
                if (V2rayVPNService.this.hours == 23) {
                    V2rayVPNService.this.hours = 0;
                }
                V2rayVPNService.SERVICE_DURATION = utils.convertTwoDigit(V2rayVPNService.this.hours) + ":" + utils.convertTwoDigit(V2rayVPNService.this.minutes) + ":" + utils.convertTwoDigit(V2rayVPNService.this.seconds);
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                V2rayVPNService.this.countDownTimer.cancel();
                V2rayVPNService.this.makeDurationTimer();
            }
        }.start();
    }

    @Override // dev.dev7.lib.v2ray.interfaces.FBProtectListener
    public boolean onProtect(int i) {
        return protect(i);
    }

    @Override // java.lang.Runnable
    public void run() {
        V2rayCore.fbProtectListener = this;
        configure();
    }

    @SuppressLint("ForegroundServiceType")
    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        SERVICE_DURATION = "00:00:00";
        makeDurationTimer();
        if (Build.VERSION.SDK_INT > 26) {
            startForeground(2, utils.getVpnServiceNotification(this));
        } else {
            startForeground(1, new Notification());
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        V2RAY_TUN_STATE = utils.V2RAY_TUN_CONNECTING;
        int intExtra = intent.getIntExtra("ACTION", 0);
        String stringExtra = intent.getStringExtra("V2RAY_CONFIG");
        if (stringExtra == null) {
            onDestroy();
        }
        if (intExtra != 3198) {
            if (intExtra == 3149) {
                onDestroy();
                stopSelf();
                return Service.START_NOT_STICKY;
            }
            return Service.START_NOT_STICKY;
        }
        if (V2rayCore.isV2rayRunning()) {
            V2rayCore.stop();
        }
        if (!V2rayCore.start(getApplicationContext(), stringExtra)) {
            onDestroy();
            return Service.START_NOT_STICKY;
        }
        Thread thread = this.mThread;
        if (thread != null) {
            thread.interrupt();
        }
        Thread thread2 = new Thread(this, "VPN_THREAD");
        this.mThread = thread2;
        thread2.start();
        return Service.START_NOT_STICKY;
    }

    @Override // android.app.Service
    public void onDestroy() {
        V2rayCore.stop();
        Thread thread = this.mThread;
        if (thread != null) {
            thread.interrupt();
        }
        Process process = this.process;
        if (process != null) {
            process.destroy();
        }
        ParcelFileDescriptor parcelFileDescriptor = this.mInterface;
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
                this.mInterface = null;
            } catch (IOException unused) {
            }
        }
        V2RAY_TUN_STATE = utils.V2RAY_TUN_DISCONNECTED;
    }

    @Override // android.net.VpnService
    public void onRevoke() {
        onDestroy();
        V2rayCore.stop();
        V2RAY_TUN_STATE = utils.V2RAY_TUN_DISCONNECTED;
    }

    private void configure() {
        if (this.mInterface != null) {
            return;
        }
        try {
            VpnService.Builder builder = new VpnService.Builder();
            builder.setSession(utils.APPLICATION_NAME);
            builder.setMtu(1500);
            builder.addAddress("26.26.26.1", 24);
            builder.addRoute("0.0.0.0", 0);
            if (utils.BLOCKED_APPS != null) {
                for (int i = 0; i < utils.BLOCKED_APPS.size(); i++) {
                    builder.addDisallowedApplication(utils.BLOCKED_APPS.get(i));
                }
            }
            builder.allowFamily(OsConstants.AF_INET6);
            ParcelFileDescriptor establish = builder.establish();
            this.mInterface = establish;
            if (establish == null) {
                onDestroy();
            } else {
                runTun2socks();
            }
        } catch (Exception e) {
            Log.e("VPN_SERVICE", "FAILED=>", e);
            onDestroy();
        }
    }

    private void runTun2socks() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(new ArrayList<>(Arrays.asList(new File(getApplicationInfo().nativeLibraryDir, "libtun2socks.so").getAbsolutePath(), "--netif-ipaddr", "26.26.26.2", "--netif-netmask", "255.255.255.252", "--socks-server-addr", "127.0.0.1:10808", "--tunmtu", "1500", "--sock-path", "sock_path", "--enable-udprelay", "--loglevel", "error")));
            processBuilder.redirectErrorStream(true);
            this.process = processBuilder.directory(getApplicationContext().getFilesDir()).start();
            sendFd();
        } catch (Exception e) {
            Log.e("VPN_SERVICE", "FAILED=>", e);
            onDestroy();
        }
    }

    private void sendFd() {
        String absolutePath = new File(getApplicationContext().getFilesDir(), "sock_path").getAbsolutePath();
        LocalSocket localSocket = new LocalSocket();
        FileDescriptor fileDescriptor = this.mInterface.getFileDescriptor();
        try {
            Thread.sleep(1000L);
            localSocket.connect(new LocalSocketAddress(absolutePath, LocalSocketAddress.Namespace.FILESYSTEM));
            if (!localSocket.isConnected()) {
                Log.e("SOCK_FILE", "Unable to connect to localSocksFile [" + absolutePath + "]");
            }
            OutputStream outputStream = localSocket.getOutputStream();
            localSocket.setFileDescriptorsForSend(new FileDescriptor[]{fileDescriptor});
            outputStream.write(32);
            localSocket.setFileDescriptorsForSend(null);
            localSocket.shutdownOutput();
            localSocket.close();
            if (V2rayCore.isV2rayRunning()) {
                V2RAY_TUN_STATE = utils.V2RAY_TUN_CONNECTED;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SOCK_FILE", "Unable to connect to localSocksFile [" + absolutePath + "]");
            onDestroy();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
            Log.e("SOCK_FILE", "FAILED=>", e2);
            onDestroy();
        }
    }
}
