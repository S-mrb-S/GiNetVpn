package com.gold.hamrahvpn;

import static com.gold.hamrahvpn.Data.KB;
import static com.gold.hamrahvpn.Data.KEY_app_details;
import static com.gold.hamrahvpn.Data.MB;
import static com.gold.hamrahvpn.Data.NA;
import static com.gold.hamrahvpn.Data.PREFUSAGEStorage;
import static com.gold.hamrahvpn.Data.RobotoBold;
import static com.gold.hamrahvpn.Data.RobotoLight;
import static com.gold.hamrahvpn.Data.RobotoMedium;
import static com.gold.hamrahvpn.Data.RobotoRegular;
import static com.gold.hamrahvpn.Data.appValStorage;
import static com.gold.hamrahvpn.Data.connected_btn;
import static com.gold.hamrahvpn.Data.connected_catch_check_internet_txt;
import static com.gold.hamrahvpn.Data.connected_catch_txt;
import static com.gold.hamrahvpn.Data.connected_error_btn;
import static com.gold.hamrahvpn.Data.connected_error_danger_vpn_txt;
import static com.gold.hamrahvpn.Data.connected_error_long_txt;
import static com.gold.hamrahvpn.Data.connected_txt;
import static com.gold.hamrahvpn.Data.connecting_btn;
import static com.gold.hamrahvpn.Data.connecting_cancel_btn;
import static com.gold.hamrahvpn.Data.connecting_txt;
import static com.gold.hamrahvpn.Data.connectionStorage;
import static com.gold.hamrahvpn.Data.default_byte_txt;
import static com.gold.hamrahvpn.Data.default_ziro_txt;
import static com.gold.hamrahvpn.Data.disconnected_btn;
import static com.gold.hamrahvpn.Data.disconnected_txt;
import static com.gold.hamrahvpn.Data.disconnected_txt2;
import static com.gold.hamrahvpn.Data.isAppDetails;
import static com.gold.hamrahvpn.Data.isConnectionDetails;
import static com.gold.hamrahvpn.Data.settingsStorage;
import static com.gold.hamrahvpn.Data.update_count_txt;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.gold.hamrahvpn.util.CountryListManager;
import com.gold.hamrahvpn.util.LogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.blinkt.openvpn.LaunchVPN;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.App;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.IOpenVPNServiceInternal;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VpnStatus;

public class MainActivity extends AppCompatActivity implements VpnStatus.ByteCountListener, VpnStatus.StateListener {
    IOpenVPNServiceInternal mService;
    InputStream inputStream;
    BufferedReader bufferedReader;
    ConfigParser cp;
    VpnProfile vp;
    ProfileManager pm;
    Thread thread;

    // NEW
    ImageView iv_home, iv_servers, iv_data, iv_progress_bar;
    LinearLayout ll_text_bubble, ll_main_data, ll_main_today, ll_protocol;
    TextView tv_message_top_text, tv_message_bottom_text, tv_data_text, tv_data_name;
    TextView tv_data_today, tv_data_today_text, tv_data_today_name;

    Button btn_connection;
    LottieAnimationView la_animation;

    Animation fade_in_1000, fade_out_1000;

    boolean EnableConnectButton = false;

    int progress = 0;

    CountDownTimer ConnectionTimer;
    TextView tv_main_count_down;

    // new
    boolean hasFile = false;
    String FileID = "NULL", File = "NULL", City = "NULL", Image = "NULL", DarkMode = "false", TODAY;

    ConstraintLayout constLayoutMain;
    public static final EncryptData ENCRYPT_DATA = new EncryptData();

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = IOpenVPNServiceInternal.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
        }
    };


    @Override
    protected void onStop() {
        VpnStatus.removeStateListener(this);
        VpnStatus.removeByteCountListener(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // بازیابی مقدار مربوط به "dark_mode" از MMKV
        DarkMode = settingsStorage.getString("dark_mode", "false");
        constLayoutMain = findViewById(R.id.constraintLayoutMain);
        if (DarkMode.equals("true")) {
            constLayoutMain.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground));
            iv_home.setImageResource(R.drawable.ic_home_white);
            iv_servers.setImageResource(R.drawable.ic_go_forward_white);
        } else {
            constLayoutMain.setBackgroundColor(getResources().getColor(R.color.colorLightBackground));
            iv_home.setImageResource(R.drawable.ic_home);
            iv_servers.setImageResource(R.drawable.ic_go_forward);
        }

        if (!isAppDetails && !isConnectionDetails) {
            try {
                Intent Welcome = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(Welcome);
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA1" + e);
                LogManager.logEvent(params);
            }
        }

        // ایجاد یک نمونه از MMKV با نام "connection_data"
        // بازیابی مقادیر از MMKV و رمزگشایی آنها
        FileID = connectionStorage.getString("file_id", NA);
        File = ENCRYPT_DATA.decrypt(connectionStorage.getString("file", NA));
        City = connectionStorage.getString("city", NA);
        Image = connectionStorage.getString("image", NA);
        // بررسی وجود فایل
        hasFile = !FileID.isEmpty();

        try {
            VpnStatus.addStateListener(this);
            VpnStatus.addByteCountListener(this);
            Intent intent = new Intent(this, OpenVPNService.class);
            intent.setAction(OpenVPNService.START_SERVICE);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "MA2" + e);
            LogManager.logEvent(params);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date Today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        TODAY = df.format(Today);

        // Top level
        // Load
        LogManager.setAppContext(this);
        RobotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        RobotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        RobotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        RobotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        iv_home = findViewById(R.id.iv_home);
        iv_servers = findViewById(R.id.iv_servers);
        iv_data = findViewById(R.id.iv_data);
        ll_text_bubble = findViewById(R.id.ll_text_bubble);
        ll_protocol = findViewById(R.id.ll_protocol_main);
        ll_main_data = findViewById(R.id.ll_main_data);
        ll_main_today = findViewById(R.id.ll_main_today);
        tv_message_top_text = findViewById(R.id.tv_message_top_text);
        tv_message_bottom_text = findViewById(R.id.tv_message_bottom_text);
        tv_data_text = findViewById(R.id.tv_data_text);
        tv_data_name = findViewById(R.id.tv_data_name);
        btn_connection = findViewById(R.id.btn_connection);
        la_animation = findViewById(R.id.la_animation);
        tv_data_today = findViewById(R.id.tv_data_today);
        tv_data_today_text = findViewById(R.id.tv_data_today_text);
        tv_data_today_name = findViewById(R.id.tv_data_today_name);
        tv_main_count_down = findViewById(R.id.tv_main_count_down);

        ll_protocol.setOnClickListener(v -> {
//        String[] strArr = {getString(R.string.protocol_e_stable), getString(R.string.protocol_b_stable), getString(R.string.protocol_a_fast)};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.darkDialogs);
//        builder.setTitle("protocol_type");
//        builder.setCancelable(true);
//        builder.setSingleChoiceItems(strArr, AppData.CONNECTION_STEPS, new DialogInterface.OnClickListener() {
//            @Override // android.content.DialogInterface.OnClickListener
//            public void onClick(DialogInterface dialogInterface, int i) {
//                MainFragment.this.m38lambda$initializeAll$2$comamwerdvpnuiMainFragment(dialogInterface, i);
//            }
//        });
//        builder.create().show();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Write your message here.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        });

        fade_in_1000 = AnimationUtils.loadAnimation(this, R.anim.fade_in_1000);
        fade_out_1000 = AnimationUtils.loadAnimation(this, R.anim.fade_out_1000);

        ll_text_bubble.setAnimation(fade_in_1000);

        // set default fonts
        tv_message_top_text.setTypeface(RobotoMedium);
        tv_message_bottom_text.setTypeface(RobotoMedium);

        tv_main_count_down.setTypeface(RobotoBold);
        btn_connection.setTypeface(RobotoBold);

        tv_data_text.setTypeface(RobotoRegular);
        tv_data_name.setTypeface(RobotoMedium);

        tv_data_today.setTypeface(RobotoMedium);
        tv_data_today_text.setTypeface(RobotoRegular);
        tv_data_today_name.setTypeface(RobotoMedium);

        LinearLayout linearLayoutMainHome = findViewById(R.id.linearLayoutMainHome);
        linearLayoutMainHome.setOnClickListener(v -> {
            if (Data.isAppDetails) {
                Intent About = new Intent(MainActivity.this, UsageActivity.class);
                startActivity(About);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        LinearLayout linearLayoutMainServers = findViewById(R.id.linearLayoutMainServers);
        linearLayoutMainServers.setOnClickListener(v -> {
            if (Data.isConnectionDetails) {
                Intent Servers = new Intent(MainActivity.this, ServerActivity.class);
                startActivity(Servers);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        final Handler handlerToday = new Handler();
        handlerToday.postDelayed(() -> {
            startAnimation(MainActivity.this, R.id.linearLayoutMainHome, R.anim.anim_slide_down, true);
            startAnimation(MainActivity.this, R.id.linearLayoutMainServers, R.anim.anim_slide_down, true);
        }, 1000);

        btn_connection.setOnClickListener(view -> {
            connectToOpenVpn();
        });
        la_animation.setOnClickListener(view -> {
            connectToOpenVpn();
        });

        // ui refresh
        thread = new Thread() {
            boolean ShowData = true;
            boolean ShowAnimation = true;

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        // important
                        Thread.sleep(500); // don't delete

                        runOnUiThread(() -> {
                            // set country flag
                            if (App.abortConnection) {
                                App.abortConnection = false;

                                if (App.connection_status != 2) {
                                    App.CountDown = 1;
                                }

                                if (App.connection_status == 2) {
                                    try {
                                        stop_vpn();
                                        try {
                                            ConnectionTimer.cancel();
                                        } catch (Exception e) {
                                            Bundle params = new Bundle();
                                            params.putString("device_id", App.device_id);
                                            params.putString("exception", "MA7" + e);
                                            LogManager.logEvent(params);
                                        }

                                        iv_progress_bar.setVisibility(View.INVISIBLE);
                                        tv_main_count_down.setVisibility(View.INVISIBLE);

                                        final Handler handlerToday12 = new Handler();
                                        handlerToday12.postDelayed(() -> {
                                            startAnimation(MainActivity.this, R.id.ll_main_data, R.anim.slide_down_800, false);
                                            ll_main_data.setVisibility(View.INVISIBLE);
                                        }, 500);


                                        final Handler handlerData = new Handler();
                                        handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);

                                        startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                                        la_animation.cancelAnimation();
                                        la_animation.setAnimation(R.raw.ninjainsecure);
                                        la_animation.playAnimation();

                                        App.ShowDailyUsage = true;
                                    } catch (Exception e) {
                                        Bundle params = new Bundle();
                                        params.putString("device_id", App.device_id);
                                        params.putString("exception", "MA8" + e);
                                        LogManager.logEvent(params);
                                    }
                                    App.isStart = false;
                                }

                            }

                            CountryListManager.OpenVpnSetServerList(Image, iv_servers);

                            // set connection button
                            if (hasFile) {
                                if (App.connection_status == 0) {
                                    // disconnected
                                    btn_connection.setText(disconnected_btn);
                                    btn_connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_connect));

                                } else if (App.connection_status == 1) {
                                    // connecting
                                    if (EnableConnectButton) {
                                        btn_connection.setText(connecting_cancel_btn);
                                        btn_connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_retry));
                                    } else {
                                        btn_connection.setText(connecting_btn);
                                        btn_connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_retry));
                                    }
                                } else if (App.connection_status == 2) {
                                    // connected
                                    btn_connection.setText(connected_btn);
                                    btn_connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_disconnect));
                                } else if (App.connection_status == 3) {
                                    // connected
                                    btn_connection.setText(connected_error_btn);
                                    btn_connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_retry));
                                }
                            }

                            // set message text
                            if (hasFile) {
                                if (hasInternetConnection()) {
                                    if (App.connection_status == 0) {
                                        // disconnected
                                        tv_message_top_text.setText(disconnected_txt);
                                        tv_message_bottom_text.setText(disconnected_txt2);
                                    } else if (App.connection_status == 1) {
                                        // connecting
                                        tv_message_top_text.setText(connecting_txt + ' ' + City);
                                        tv_message_bottom_text.setText(VpnStatus.getLastCleanLogMessage(MainActivity.this));
                                    } else if (App.connection_status == 2) {
                                        // connected
                                        tv_message_top_text.setText(connected_txt + ' ' + City);
                                        tv_message_bottom_text.setText(Data.StringCountDown);
                                    } else if (App.connection_status == 3) {
                                        // connected
                                        tv_message_top_text.setText(connected_error_danger_vpn_txt);
                                        tv_message_bottom_text.setText(connected_error_long_txt);
                                    }
                                } else {
                                    tv_message_top_text.setText(connected_catch_txt);
                                    tv_message_bottom_text.setText(connected_catch_check_internet_txt);
                                }
                            }

                            // show data limit
                            if (ShowData) {
                                ShowData = false;
                                if (App.connection_status == 0) {
                                    final Handler handlerData = new Handler();
                                    handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);
                                } else if (App.connection_status == 1) {
                                    final Handler handlerData = new Handler();
                                    handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);
                                } else if (App.connection_status == 2) {
                                    final Handler handlerData = new Handler();
                                    handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_data, R.anim.slide_up_800, true), 1000);
                                } else if (App.connection_status == 3) {
                                    // connected
                                    final Handler handlerData = new Handler();
                                    handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);
                                }
                            }

                            // get daily usage
                            if (hasFile) {
                                if (App.connection_status == 0) {
                                    // disconnected
                                    if (App.ShowDailyUsage) {
                                        App.ShowDailyUsage = false;
                                        // بازیابی مقدار مربوط به کلید "today"
                                        long long_usage_today = PREFUSAGEStorage.getLong(TODAY, 0);

                                        if (long_usage_today < 1000) {
                                            tv_data_today_text.setText(default_ziro_txt + ' ' + KB);
                                        } else if (long_usage_today <= 1000_000) {
                                            tv_data_today_text.setText((long_usage_today / 1000) + KB);
                                        } else {
                                            tv_data_today_text.setText((long_usage_today / 1000_000) + MB);
                                        }
                                    }
                                }
                            }

                            // show animation
                            if (hasFile) {
                                if (ShowAnimation) {
                                    ShowAnimation = false;
                                    if (App.connection_status == 0) {
                                        // disconnected
                                        startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                                        la_animation.cancelAnimation();
                                        la_animation.setAnimation(R.raw.ninjainsecure);
                                        la_animation.playAnimation();
                                    } else if (App.connection_status == 1) {
                                        // connecting
                                        startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                                        la_animation.cancelAnimation();
                                        la_animation.setAnimation(R.raw.conneting);
                                        la_animation.playAnimation();
                                    } else if (App.connection_status == 3) {
                                        // connected
                                        startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                                        la_animation.cancelAnimation();
                                        la_animation.setAnimation(R.raw.ninjainsecure);
                                        la_animation.playAnimation();
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Bundle params = new Bundle();
                    params.putString("device_id", App.device_id);
                    params.putString("exception", "MA9" + e);
                    LogManager.logEvent(params);
                }
            }
        };
        thread.start();

    }

    private void connectToOpenVpn() {
        Runnable r = () -> {
            if (!App.isStart) {
                if (!hasFile) {
                    Intent Servers = new Intent(MainActivity.this, ServerActivity.class);
                    startActivity(Servers);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                } else {
                    if (hasInternetConnection()) {
                        try {
                            start_vpn(File);
                            final Handler handlerToday1 = new Handler();
                            handlerToday1.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_down_800, false), 500);

                            final Handler handlerData = new Handler();
                            handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_data, R.anim.slide_up_800, true), 1000);

                            startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                            la_animation.cancelAnimation();
                            la_animation.setAnimation(R.raw.conneting);
                            la_animation.playAnimation();

                            iv_progress_bar = findViewById(R.id.iv_progress_bar);
                            iv_progress_bar.getLayoutParams().width = 10;
                            progress = 10;
                            startAnimation(MainActivity.this, R.id.iv_progress_bar, R.anim.fade_in_1000, true);

                            tv_main_count_down.setVisibility(View.VISIBLE);
                            App.CountDown = 30;
                            try {
                                ConnectionTimer = new CountDownTimer(32_000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        App.CountDown = App.CountDown - 1;

                                        iv_progress_bar.getLayoutParams().width = progress;
                                        progress = progress + (int) getResources().getDimension(R.dimen.lo_10dpGrid);
                                        tv_main_count_down.setText(String.valueOf(App.CountDown));

                                        if (App.connection_status == 2) {
                                            ConnectionTimer.cancel();
                                            // ویرایش کردن مقدار "connection_time" در MMKV
                                            settingsStorage.putString("connection_time", String.valueOf(App.CountDown));
                                            // بررسی شرط
                                            if (App.CountDown >= 20) {
                                                // بازیابی مقدار "rate" از MMKV
                                                String rate = settingsStorage.getString("rate", "false");
                                                // بررسی شرط
                                                if (rate.equals("false")) {
                                                    // ایجاد یک Handler برای تاخیر
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(() -> {
                                                        // ایجاد Intent برای رفتن به ReviewActivity
                                                        Intent servers = new Intent(MainActivity.this, ReviewActivity.class);
                                                        startActivity(servers);
                                                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                                    }, 1000);
                                                }
                                            }

                                            startAnimation(MainActivity.this, R.id.tv_main_count_down, R.anim.fade_out_1000, false);
                                            startAnimation(MainActivity.this, R.id.iv_progress_bar, R.anim.fade_out_1000, false);
                                            startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_out_1000, false);
                                        }

                                        if (App.CountDown <= 20) {
                                            EnableConnectButton = true;
                                        }

                                        if (App.CountDown <= 1) {
                                            ConnectionTimer.cancel();
                                            startAnimation(MainActivity.this, R.id.tv_main_count_down, R.anim.fade_out_500, false);
                                            startAnimation(MainActivity.this, R.id.iv_progress_bar, R.anim.fade_out_500, false);
                                            startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_out_500, false);

                                            try {
                                                stop_vpn();

                                                final Handler handlerToday1 = new Handler();
                                                handlerToday1.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_data, R.anim.slide_down_800, false), 500);

                                                final Handler handlerData = new Handler();
                                                handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);

                                                startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                                                la_animation.cancelAnimation();
                                                la_animation.setAnimation(R.raw.ninjainsecure);
                                                la_animation.playAnimation();

                                                App.ShowDailyUsage = true;
                                            } catch (Exception e) {
                                                Bundle params = new Bundle();
                                                params.putString("device_id", App.device_id);
                                                params.putString("exception", "MA3" + e);
                                                LogManager.logEvent(params);
                                            }
                                            App.isStart = false;
                                        }

                                    }

                                    public void onFinish() {
                                    }

                                };
                            } catch (Exception e) {
                                Bundle params = new Bundle();
                                params.putString("device_id", App.device_id);
                                params.putString("exception", "MA4" + e);
                                LogManager.logEvent(params);
                            }
                            ConnectionTimer.start();

                            EnableConnectButton = false;
                            App.isStart = true;

                        } catch (Exception e) {
                            Bundle params = new Bundle();
                            params.putString("device_id", App.device_id);
                            params.putString("exception", "MA5" + e);
                            LogManager.logEvent(params);
                        }

                    }
                }
            } else {
                if (EnableConnectButton) {
                    try {
                        stop_vpn();
                        try {
                            ConnectionTimer.cancel();
                        } catch (Exception e) {
//                                new SyncFunctions(MainActivity.this, "MA6 " +  e.toString()).set_error_log();
                            Bundle params = new Bundle();
                            params.putString("device_id", App.device_id);
                            params.putString("exception", "MA6" + e);
                            LogManager.logEvent(params);
                        }

                        try {
                            iv_progress_bar.setVisibility(View.INVISIBLE);
                            tv_main_count_down.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            //new SyncFunctions(MainActivity.this, "MA7 " +  e.toString()).set_error_log();
                            Bundle params = new Bundle();
                            params.putString("device_id", App.device_id);
                            params.putString("exception", "MA7" + e);
                            LogManager.logEvent(params);
                        }

                        final Handler handlerToday1 = new Handler();
                        handlerToday1.postDelayed(() -> {
                            startAnimation(MainActivity.this, R.id.ll_main_data, R.anim.slide_down_800, false);
                            ll_main_data.setVisibility(View.INVISIBLE);
                        }, 500);

                        final Handler handlerData = new Handler();
                        handlerData.postDelayed(() -> startAnimation(MainActivity.this, R.id.ll_main_today, R.anim.slide_up_800, true), 1000);

                        startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                        la_animation.cancelAnimation();
                        la_animation.setAnimation(R.raw.ninjainsecure);
                        la_animation.playAnimation();

                        String ConnectionTime = settingsStorage.getString("connection_time", "0");
                        if (Long.parseLong(ConnectionTime) >= 20) {
                            settingsStorage.putString("connection_time", "0");
                            String rate = settingsStorage.getString("rate", "false");
                            if (rate.equals("false")) {
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    Intent servers = new Intent(MainActivity.this, ReviewActivity.class);
                                    startActivity(servers);
                                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                }, 500);
                            }
                        }

                        App.ShowDailyUsage = true;
                    } catch (Exception e) {
                        Bundle params = new Bundle();
                        params.putString("device_id", App.device_id);
                        params.putString("exception", "MA6" + e);
                        LogManager.logEvent(params);
                    }
                    App.isStart = false;
                }
            }
        };
        r.run();
    }

    private boolean hasInternetConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        } catch (Exception e) {
            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "MA10" + e);
            LogManager.logEvent(params);
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * 1 - convert SharedPreferences to MMKV
     * 2 - fix Daily Usage
     */
    private void start_vpn(String VPNFile) {
        // بازیابی مقادیر
        long connection_today = PREFUSAGEStorage.getLong(TODAY + "_connections", 0);
        long connection_total = PREFUSAGEStorage.getLong("total_connections", 0);

        // ویرایش کردن مقادیر
        PREFUSAGEStorage.putLong(TODAY + "_connections", connection_today + 1);
        PREFUSAGEStorage.putLong("total_connections", connection_total + 1);

        Bundle params = new Bundle();
        params.putString("device_id", App.device_id);
        params.putString("city", City);
        params.putString("params", "app_param_country");
        LogManager.logEvent(params);

        App.connection_status = 1;
        try {
            inputStream = null;
            bufferedReader = null;
            try {
                assert VPNFile != null;
                inputStream = new ByteArrayInputStream(VPNFile.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA11" + e);
                LogManager.logEvent(params);
            }

            try { // M8
                assert inputStream != null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream/*, Charset.forName("UTF-8")*/));
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA12" + e);
                LogManager.logEvent(params);
            }

            cp = new ConfigParser();
            try {
                cp.parseConfig(bufferedReader);
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA13" + e);
                LogManager.logEvent(params);
            }
            vp = cp.convertProfile();
            vp.mAllowedAppsVpnAreDisallowed = true;

            String AppValues = appValStorage.getString(KEY_app_details, NA);
            String AppDetailsValues = ENCRYPT_DATA.decrypt(AppValues);

            try {
                JSONObject json_response = new JSONObject(AppDetailsValues);
                JSONArray jsonArray = json_response.getJSONArray("blocked");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_object = jsonArray.getJSONObject(i);
                    vp.mAllowedAppsVpn.add(json_object.getString("app"));
                    Log.e("packages", json_object.getString("app"));
                }
            } catch (JSONException e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA14" + e);
                LogManager.logEvent(params);
            }

            try {
                vp.mName = Build.MODEL;
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA15" + e);
                LogManager.logEvent(params);
            }

            vp.mUsername = Data.FileUsername;
            vp.mPassword = Data.FilePassword;

            try {
                pm = ProfileManager.getInstance(MainActivity.this);
                pm.addProfile(vp);
                pm.saveProfileList(MainActivity.this);
                pm.saveProfile(MainActivity.this, vp);
                vp = pm.getProfileByName(Build.MODEL);
                Intent intent = new Intent(getApplicationContext(), LaunchVPN.class);
                intent.putExtra(LaunchVPN.EXTRA_KEY, vp.getUUID().toString());
                intent.setAction(Intent.ACTION_MAIN);
                startActivity(intent);
                App.isStart = false;
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA16" + e);
                LogManager.logEvent(params);
            }
        } catch (Exception e) {
            params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "MA17" + e);
            LogManager.logEvent(params);
        }
    }

    public void stop_vpn() {
        App.connection_status = 0;
        OpenVPNService.abortConnectionVPN = true;
        ProfileManager.setConntectedVpnProfileDisconnected(this);
        if (mService != null) {
            try {
                mService.stopVPN(false);
            } catch (RemoteException e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA18" + e);
                LogManager.logEvent(params);
            }
            try {
                pm = ProfileManager.getInstance(this);
                vp = pm.getProfileByName(Build.MODEL);
                pm.removeProfile(this, vp);
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "MA17" + e);
                LogManager.logEvent(params);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // does not need
    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void updateState(final String state, String logmessage, int localizedResId, ConnectionStatus level) {
        runOnUiThread(() -> {
            if (state.equals("CONNECTED")) {
                App.isStart = true;
                App.connection_status = 2;

                Handler handlerData = new Handler();
                handlerData.postDelayed(() -> {
                    startAnimation(MainActivity.this, R.id.la_animation, R.anim.fade_in_1000, true);
                    la_animation.cancelAnimation();
                    la_animation.setAnimation(R.raw.ninjasecure);
                    la_animation.playAnimation();
                }, 1000);

                EnableConnectButton = true;
            }
        });
    }

    @Override
    public void setConnectedVPN(String uuid) {

    }


    @Override
    public void updateByteCount(long ins, long outs, long diffIns, long diffOuts) {
        final long Total = ins + outs;
        runOnUiThread(() -> {
            // size
            if (Total < 1000) {
                tv_data_text.setText(default_byte_txt);
                tv_data_name.setText(update_count_txt);
            } else if (Total <= 1000_000) {
                tv_data_text.setText((Total / 1000) + KB);
                tv_data_name.setText(update_count_txt);
            } else {
                tv_data_text.setText((Total / 1000_000) + MB);
                tv_data_name.setText(update_count_txt);
            }
        });
    }

    public void startAnimation(Context ctx, int view, int animation, boolean show) {
        final View Element = findViewById(view);
        if (show) {
            Element.setVisibility(View.VISIBLE);
        } else {
            Element.setVisibility(View.INVISIBLE);
        }
        Animation anim = AnimationUtils.loadAnimation(ctx, animation);
        Element.startAnimation(anim);
    }


}


