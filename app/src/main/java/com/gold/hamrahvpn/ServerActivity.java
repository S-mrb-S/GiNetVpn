package com.gold.hamrahvpn;

import static com.gold.hamrahvpn.util.SafeParcelable.NULL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gold.hamrahvpn.openvpn.EncryptData;
import com.gold.hamrahvpn.recyclerview.MainAdapter;
import com.gold.hamrahvpn.recyclerview.Server;
import com.gold.hamrahvpn.util.FinishActivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.blinkt.openvpn.core.ProfileManager;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

//import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ServerActivity extends Activity implements FinishActivityListener {
    RecyclerView listView_light, listView_dark; // list
    ProfileManager pm;
    ImageView iv_server_refresh;
    String AppDetails = "NULL", FileDetails = "NULL";

    String[][] ServerArray = new String[40][8];
    public static String[][] FileArray = new String[40][2];

    public static String DarkMode = "false";

    public static final String KEY_GRID = "GRID";

    // 100
    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);

        pm = ProfileManager.getInstance(ServerActivity.this);

        iv_server_refresh = findViewById(R.id.iv_server_refresh);

        EncryptData En = new EncryptData();
        SharedPreferences AppValues = getSharedPreferences("app_values", 0);
        String AppDetails = En.decrypt(AppValues.getString("app_details", NULL));

        if (AppDetails.isEmpty()) {
            getConnectionString getConnectionString = new getConnectionString();
            getConnectionString.GetAppDetails();
        } else {
            ServersList Servers = new ServersList();
            Servers.Load();
        }

        Typeface RobotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        TextView tv_servers_title = findViewById(R.id.tv_servers_title);
        tv_servers_title.setTypeface(RobotoMedium);

        LinearLayout ll_server_back = findViewById(R.id.ll_server_back);
        ll_server_back.setOnClickListener(v -> finishActivity());

        LinearLayout ll_server_retry = findViewById(R.id.ll_server_refresh);
        ll_server_retry.setOnClickListener(v -> {
            getConnectionString getConnectionString = new getConnectionString();
            getConnectionString.GetAppDetails();
        });


        RelativeLayout linearLayoutServers = findViewById(R.id.constraintLayoutServers);
        ImageView iv_servers_go_back = findViewById(R.id.iv_servers_go_back);
        SharedPreferences SettingsDetails = getSharedPreferences("settings_data", 0);
        DarkMode = SettingsDetails.getString("dark_mode", "false");
        if (DarkMode.equals("true")) {
            linearLayoutServers.setBackgroundColor(getResources().getColor(R.color.colorDarkBackground));
            tv_servers_title.setTextColor(getResources().getColor(R.color.colorDarkText));
            iv_servers_go_back.setImageResource(R.drawable.ic_go_back_white);
        } else {
            linearLayoutServers.setBackgroundColor(getResources().getColor(R.color.colorLightBackground));
            tv_servers_title.setTextColor(getResources().getColor(R.color.colorLightText));
            iv_servers_go_back.setImageResource(R.drawable.ic_go_back);
        }

    }

    private class getConnectionString {
        private void GetAppDetails() {
            iv_server_refresh.setBackground(getDrawable(R.drawable.ic_servers_process));
            RequestQueue queue = Volley.newRequestQueue(ServerActivity.this);
            queue.getCache().clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://raw.githubusercontent.com/gayanvoice/gayankuruppu.github.io/source-json/appdetails.json",
                    Response -> AppDetails = Response, error -> {
    //                    Bundle params = new Bundle();
    //                    params.putString("device_id", App.device_id);
    //                    params.putString("exception", "SA1" + error.toString());
    //                    mFirebaseAnalytics.logEvent("app_param_error", params);
                    });
            queue.add(stringRequest);
            queue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    iv_server_refresh.setBackground(getDrawable(R.drawable.ic_servers_cloud));
                    GetFileDetails();
                }, 2000);
                iv_server_refresh.setBackground(getDrawable(R.drawable.ic_servers_cloud));
                GetFileDetails();
            });
        }

        private void GetFileDetails() {
            iv_server_refresh.setBackground(getDrawable(R.drawable.ic_servers_process));
            RequestQueue queue = Volley.newRequestQueue(ServerActivity.this);
            queue.getCache().clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://raw.githubusercontent.com/gayanvoice/gayankuruppu.github.io/source-json/filedetails.json",
                    Response -> FileDetails = Response, error -> {
    //                    Bundle params = new Bundle();
    //                    params.putString("device_id", App.device_id);
    //                    params.putString("exception", "SA2" + error.toString());
    //                    mFirebaseAnalytics.logEvent("app_param_error", params);
                    });
            queue.add(stringRequest);
            queue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {
                EncryptData En = new EncryptData();
                try {
                    SharedPreferences SharedAppDetails = getSharedPreferences("app_values", 0);
                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
                    Editor.putString("app_details", En.encrypt(AppDetails));
                    Editor.putString("file_details", En.encrypt(FileDetails));
                    Editor.apply();
                } catch (Exception e) {
//                        Bundle params = new Bundle();
//                        params.putString("device_id", App.device_id);
//                        params.putString("exception", "SA3" + e.toString());
//                        mFirebaseAnalytics.logEvent("app_param_error", params);
                }

                iv_server_refresh.setBackground(getDrawable(R.drawable.ic_servers_cloud));
                ServersList Servers = new ServersList();
                Servers.Load();
            });
        }
    }

    class ServersList {
        private MainAdapter adapter;

        ServersList() {
        }

        void Load() {
            EncryptData En = new EncryptData();
            SharedPreferences ConnectionDetails = getSharedPreferences("app_values", 0);
            AppDetails = En.decrypt(ConnectionDetails.getString("app_details", NULL));
            FileDetails = En.decrypt(ConnectionDetails.getString("file_details", NULL));
            int NumServers = 0;
            try {
                JSONObject json_response = new JSONObject(AppDetails);
                JSONArray jsonArray = json_response.getJSONArray("free");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_object = jsonArray.getJSONObject(i);
                    ServerArray[i][0] = json_object.getString("id");
                    ServerArray[i][1] = json_object.getString("file");
                    ServerArray[i][2] = json_object.getString("city");
                    ServerArray[i][3] = json_object.getString("country");
                    ServerArray[i][4] = json_object.getString("image");
                    ServerArray[i][5] = json_object.getString("ip");
                    ServerArray[i][6] = json_object.getString("active");
                    ServerArray[i][7] = json_object.getString("signal");
                    NumServers = NumServers + 1;
                }

            } catch (JSONException e) {
//                Bundle params = new Bundle();
//                params.putString("device_id", App.device_id);
//                params.putString("exception", "SA4" + e.toString());
//                mFirebaseAnalytics.logEvent("app_param_error", params);
                TextView showBool = findViewById(R.id.boolShowListServer);
                showBool.setVisibility(View.VISIBLE);
            }

            try {
                JSONObject jsonResponse = new JSONObject(FileDetails);
                JSONArray jsonArray = jsonResponse.getJSONArray("ovpn_file");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FileArray[i][0] = jsonObject.getString("id");
                    FileArray[i][1] = jsonObject.getString("file");
                }

            } catch (JSONException e) {
//                Bundle params = new Bundle();
//                params.putString("device_id", App.device_id);
//                params.putString("exception", "SA5" + e.toString());
//                mFirebaseAnalytics.logEvent("app_param_error", params);
                TextView showBool = findViewById(R.id.boolShowListServer);
                showBool.setVisibility(View.VISIBLE);
            }

            List<Server> ServerList = new ArrayList<>();
            listView_light = findViewById(R.id.ls_servers_list_light);
            listView_dark = findViewById(R.id.ls_servers_list_dark);

            for (int x = 0; x < NumServers; x++) {
                Server Server = new Server();
                Server.SetID(ServerArray[x][0]);
                Server.SetFileID(ServerArray[x][1]);
                Server.SetCity(ServerArray[x][2]);
                Server.SetCountry(ServerArray[x][3]);
                Server.SetImage(ServerArray[x][4]);
                Server.SetIP(ServerArray[x][5]);
                Server.SetActive(ServerArray[x][6]);
                Server.SetSignal(ServerArray[x][7]);
                ServerList.add(Server);
            }

            SharedPreferences SettingsDetails = getSharedPreferences("settings_data", 0);
            String DarkMode = SettingsDetails.getString("dark_mode", "false");
            adapter = new MainAdapter(ServerActivity.this, ServerList, ServerActivity.this);
            RecyclerView recyclerView;
            if (DarkMode.equals("true")) {
//                listView_dark.setAdapter(adapter);
                recyclerView = listView_dark;
                listView_dark.setVisibility(View.VISIBLE);
                listView_light.setVisibility(View.GONE);
            } else {
//                listView_light.setAdapter(adapter);
                recyclerView = listView_light;
                listView_light.setVisibility(View.VISIBLE);
                listView_dark.setVisibility(View.GONE);
            }

            // new adapter
            recyclerView.setLayoutManager(getLayoutManager());
            recyclerView.setItemAnimator(new FadeInAnimator());

            AnimationAdapter defaultAdapter = new AlphaInAnimationAdapter(adapter);
            defaultAdapter.setFirstOnly(true);
            defaultAdapter.setDuration(500);
            defaultAdapter.setInterpolator(new OvershootInterpolator(0.5f));
            recyclerView.setAdapter(defaultAdapter);
        }
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        boolean useGrid = getIntent().getBooleanExtra(KEY_GRID, true);
        return useGrid
                ? new GridLayoutManager(this, 1) // 2
                : new LinearLayoutManager(this);
    }

    @Override
    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

}
