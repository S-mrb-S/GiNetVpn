package com.gold.hamrahvpn.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.gold.hamrahvpn.R;
import com.gold.hamrahvpn.util.LogManager;

import de.blinkt.openvpn.core.App;


public class ReviewActivity extends Activity {

//    private FirebaseAnalytics LogManager;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

//        LogManager = FirebaseAnalytics.getInstance(this);

        Typeface RobotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        LinearLayout ll_about_back = findViewById(R.id.ll_about_back);
        LottieAnimationView la_review = findViewById(R.id.la_review);
        TextView tv_review_title = findViewById(R.id.tv_review_title);
        TextView tv_review_sup = findViewById(R.id.tv_review_sup);
        TextView tv_review_sub = findViewById(R.id.tv_review_sub);
        Button btn_review_submit = findViewById(R.id.btn_review_submit);

        startAnimation(ReviewActivity.this, R.id.la_review, R.anim.slide_up_800, true);
        startAnimation(ReviewActivity.this, R.id.ll_about_back, R.anim.anim_slide_down, true);
        startAnimation(ReviewActivity.this, R.id.tv_review_title, R.anim.slide_up_800, true);
        startAnimation(ReviewActivity.this, R.id.tv_review_sup, R.anim.slide_up_800, true);

        Handler handler = new Handler();
        handler.postDelayed(() -> startAnimation(ReviewActivity.this, R.id.btn_review_submit, R.anim.slide_up_800, true), 500);

        handler = new Handler();
        handler.postDelayed(() -> startAnimation(ReviewActivity.this, R.id.tv_review_sub, R.anim.slide_up_800, true), 1000);

        tv_review_title.setTypeface(RobotoBold);
        tv_review_sup.setTypeface(RobotoBold);
        tv_review_sub.setTypeface(RobotoBold);
        btn_review_submit.setTypeface(RobotoBold);

        ll_about_back.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        });

        la_review.setOnClickListener(view -> {
            SharedPreferences SharedAppDetails = getSharedPreferences("settings_data", 0);
            SharedPreferences.Editor Editor = SharedAppDetails.edit();
            Editor.putString("rate", "true");
            Editor.apply();

            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("click", "review-stars");
            params.putString("params", "app_param_click");
            LogManager.logEvent(params);


            finish();
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.gold.hamrahvpn"));
                startActivity(intent);
            } catch (ActivityNotFoundException activityNotFound) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "RA1" + activityNotFound.toString());
                LogManager.logEvent(params);

//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gold.hamrahvpn")));
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "RA2" + e);
                LogManager.logEvent(params);
            }

        });

        btn_review_submit.setOnClickListener(view -> {
            SharedPreferences SharedAppDetails = getSharedPreferences("settings_data", 0);
            SharedPreferences.Editor Editor = SharedAppDetails.edit();
            Editor.putString("rate", "true");
            Editor.apply();

            Bundle params = new Bundle();
            params.putString("device_id", App.device_id);
            params.putString("click", "review-button");
            params.putString("params", "app_param_click");
            LogManager.logEvent(params);

            finish();
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.gold.hamrahvpn"));
                startActivity(intent);
            } catch (ActivityNotFoundException activityNotFound) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "RA2" + activityNotFound.toString());
                LogManager.logEvent(params);
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gold.hamrahvpn")));
            } catch (Exception e) {
                params = new Bundle();
                params.putString("device_id", App.device_id);
                params.putString("exception", "RA3" + e);
                LogManager.logEvent(params);
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
