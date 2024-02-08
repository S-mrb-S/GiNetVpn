package com.gold.hamrahvpn;

import static com.gold.hamrahvpn.Data.RobotoBold;
import static com.gold.hamrahvpn.Data.RobotoMedium;
import static com.gold.hamrahvpn.Data.disconnected;
import static com.gold.hamrahvpn.Data.get_details_from_file;
import static com.gold.hamrahvpn.Data.get_info_from_app;
import static com.gold.hamrahvpn.Data.isAppDetails;
import static com.gold.hamrahvpn.Data.isConnectionDetails;
import static com.gold.hamrahvpn.MainActivity.ENCRYPT_DATA;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gold.hamrahvpn.util.LogManager;
import com.gold.hamrahvpn.util.MmkvManager;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import de.blinkt.openvpn.core.App;


public class WelcomeActivity extends AppCompatActivity {
    TextView tv_welcome_status, tv_welcome_app;
    String StringGetAppURL, StringGetConnectionURL, AppDetails, FileDetails;
    MMKV appAppDetailsStorage = MmkvManager.getADStorage();
    MMKV connectionStorage = MmkvManager.getConnectionStorage();
    MMKV appValStorage = MmkvManager.getAppValStorage();
//    TextView tv_welcome_title, tv_welcome_description, tv_welcome_size, tv_welcome_version;

    int Random;
//    private FirebaseAnalytics LogManager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        LogManager = FirebaseAnalytics.getInstance(this);

        StringGetAppURL = "https://raw.githubusercontent.com/gayanvoice/android-vpn-client-ics-openvpn/images/appdetails.json";
        StringGetConnectionURL = "https://raw.githubusercontent.com/gayanvoice/android-vpn-client-ics-openvpn/images/filedetails.json";
        //StringGetConnectionURL = "https://gayankuruppu.github.io/buzz/connection.html";

        tv_welcome_status = findViewById(R.id.tv_welcome_status);
        tv_welcome_app = findViewById(R.id.tv_welcome_app);
//        tv_welcome_title = findViewById(R.id.tv_welcome_title);
//        tv_welcome_description = findViewById(R.id.tv_welcome_description);
//        tv_welcome_size = findViewById(R.id.tv_welcome_size);
//        tv_welcome_version = findViewById(R.id.tv_welcome_version);

//        tv_welcome_title.setTypeface(RobotoMedium);
//        tv_welcome_description.setTypeface(RobotoRegular);
//        tv_welcome_size.setTypeface(RobotoMedium);
//        tv_welcome_version.setTypeface(RobotoMedium);

        startAnimation(WelcomeActivity.this, R.id.ll_welcome_loading, R.anim.slide_up_800, true);
        Handler handler = new Handler();
        handler.postDelayed(() -> startAnimation(WelcomeActivity.this, R.id.ll_welcome_details, R.anim.slide_up_800, true), 1000);

        tv_welcome_status.setTypeface(RobotoMedium);
        tv_welcome_app.setTypeface(RobotoBold);

//        Button btn_welcome_update = findViewById(R.id.btn_welcome_update);
        Button btn_welcome_later = findViewById(R.id.btn_welcome_later);

//        btn_welcome_update.setTypeface(RobotoMedium);
        btn_welcome_later.setTypeface(RobotoMedium);

//        btn_welcome_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gayanvoice/android-vpn-client-ics-openvpn")));
//                    /*
//                    The following lines of code load the PlayStore
//
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("click", "play");
//                    LogManager.logEvent("app_param_click", params);
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("market://details?id=com.gold.hamrahvpn"));
//                    startActivity(intent);
//                    */
//                } catch (ActivityNotFoundException activityNotFound) {
//                    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gold.hamrahvpn")));
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gayanvoice/android-vpn-client-ics-openvpn")));
//
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA1" + e);
////                    LogManager.logEvent(params);
//                }
//            }
//
//        });

        btn_welcome_later.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

        if (!Data.isConnectionDetails) {
            if (!Data.isAppDetails) {
                handler.postDelayed(this::getAppDetails, 2000);
            }
        }

    }

    void getAppDetails() {
        tv_welcome_status.setText(get_info_from_app);
        RequestQueue queue = Volley.newRequestQueue(WelcomeActivity.this);
        queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, StringGetAppURL,
                Response -> {
                    Log.e("Response", Response);
                    AppDetails = Response;
                    Data.isAppDetails = true;
                }, error -> {
            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "WA2" + error.toString());
            LogManager.logEvent(params);

            Data.isAppDetails = false;
        });

        queue.add(stringRequest);
        queue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {
            if (isAppDetails) {
                getFileDetails();
            } else {
                tv_welcome_status.setText(disconnected);
            }
        });
    }

    void getFileDetails() {
        tv_welcome_status.setText(get_details_from_file);
        RequestQueue queue = Volley.newRequestQueue(WelcomeActivity.this);
        queue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, StringGetConnectionURL,
                Response -> {
                    FileDetails = Response;
                    Data.isConnectionDetails = true;
                }, error -> {
            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("exception", "WA3" + error.toString());
            LogManager.logEvent(params);

            Data.isConnectionDetails = false;
        });
        queue.add(stringRequest);
        queue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {

            final int min = 0;
            final int max = 4;
            Random = new Random().nextInt((max - min) + 1) + min;

            String Ads = "NULL", cuVersion = "NULL", upVersion = "NULL", upTitle = "NULL", upDescription = "NULL", upSize = "NULL";
            String ID = "NULL", FileID = "NULL", File = "NULL", City = "NULL", Country = "NULL", Image = "NULL",
                    IP = "NULL", Active = "NULL", Signal = "NULL";
//                String BlockedApps = "NULL";

            try {
                JSONObject jsonResponse = new JSONObject(AppDetails);
                Ads = jsonResponse.getString("ads");
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA4" + e);
                LogManager.logEvent(params);
            }

            try {
                JSONObject jsonResponse = new JSONObject(AppDetails);
                JSONArray jsonArray = jsonResponse.getJSONArray("update");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                upVersion = jsonObject.getString("version");
                upTitle = jsonObject.getString("title");
                upDescription = jsonObject.getString("description");
                upSize = jsonObject.getString("size");
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA5" + e);
                LogManager.logEvent(params);
            }

            try {
                JSONObject json_response = new JSONObject(AppDetails);
                JSONArray jsonArray = json_response.getJSONArray("free");
                JSONObject json_object = jsonArray.getJSONObject(Random);
                ID = json_object.getString("id");
                FileID = json_object.getString("file");
                City = json_object.getString("city");
                Country = json_object.getString("country");
                Image = json_object.getString("image");
                IP = json_object.getString("ip");
                Active = json_object.getString("active");
                Signal = json_object.getString("signal");
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA5" + e);
                LogManager.logEvent(params);
            }

            try {
                JSONObject json_response = new JSONObject(FileDetails);
                JSONArray jsonArray = json_response.getJSONArray("ovpn_file");
                JSONObject json_object = jsonArray.getJSONObject(Integer.parseInt(FileID));
                FileID = json_object.getString("id");
                File = json_object.getString("file");
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA6" + e);
                LogManager.logEvent(params);
            }

            // save details
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                cuVersion = pInfo.versionName;
                if (cuVersion.isEmpty()) {
                    cuVersion = "0.0.0";
                }
                appAppDetailsStorage.putString("ads", Ads);
                appAppDetailsStorage.putString("up_title", upTitle);
                appAppDetailsStorage.putString("up_description", upDescription);
                appAppDetailsStorage.putString("up_size", upSize);
                appAppDetailsStorage.putString("up_version", upVersion);
                appAppDetailsStorage.putString("cu_version", cuVersion);
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA7" + e);
                LogManager.logEvent(params);
            }
            try {
                connectionStorage.putString("id", ID);
                connectionStorage.putString("file_id", FileID);
                connectionStorage.putString("file", ENCRYPT_DATA.encrypt(File));
                connectionStorage.putString("city", City);
                connectionStorage.putString("country", Country);
                connectionStorage.putString("image", Image);
                connectionStorage.putString("ip", IP);
                connectionStorage.putString("active", Active);
                connectionStorage.putString("signal", Signal);
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA8" + e);
                LogManager.logEvent(params);
            }

            try {
                appValStorage.putString("app_details", ENCRYPT_DATA.encrypt(AppDetails));
                appValStorage.putString("file_details", ENCRYPT_DATA.encrypt(FileDetails));
            } catch (Exception e) {
                Bundle params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "WA9" + e);
                LogManager.logEvent(params);
            }

//                tv_welcome_title.setText(upTitle);
//                tv_welcome_description.setText(upDescription);
//                tv_welcome_size.setText(upSize);
//                tv_welcome_version.setText(upVersion);

            if (isConnectionDetails) {
                if (cuVersion.equals(upVersion)) {
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                } else {
                    startAnimation(WelcomeActivity.this, R.id.ll_welcome_loading, R.anim.fade_out_500, false);
                    Handler handlerData = new Handler();
                    handlerData.postDelayed(() -> startAnimation(WelcomeActivity.this, R.id.ll_update_details, R.anim.slide_up_800, true), 1000);
                }
            } else {
                tv_welcome_status.setText(disconnected);
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
