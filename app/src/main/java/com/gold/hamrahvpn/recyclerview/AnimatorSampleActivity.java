package com.gold.hamrahvpn.recyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.hamrahvpn.R;
import com.gold.hamrahvpn.ServerActivity;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInRightYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Daichi Furiya / Wasabeef on 2020/08/26.
 */
public class AnimatorSampleActivity extends AppCompatActivity {

    enum Type {
        FadeIn(new FadeInAnimator()),
        FadeInDown(new FadeInDownAnimator()),
        FadeInUp(new FadeInUpAnimator()),
        FadeInLeft(new FadeInLeftAnimator()),
        FadeInRight(new FadeInRightAnimator()),
        Landing(new LandingAnimator()),
        ScaleIn(new ScaleInAnimator()),
        ScaleInTop(new ScaleInTopAnimator()),
        ScaleInBottom(new ScaleInBottomAnimator()),
        ScaleInLeft(new ScaleInLeftAnimator()),
        ScaleInRight(new ScaleInRightAnimator()),
        FlipInTopX(new FlipInTopXAnimator()),
        FlipInBottomX(new FlipInBottomXAnimator()),
        FlipInLeftY(new FlipInLeftYAnimator()),
        FlipInRightY(new FlipInRightYAnimator()),
        SlideInLeft(new SlideInLeftAnimator()),
        SlideInRight(new SlideInRightAnimator()),
        SlideInDown(new SlideInDownAnimator()),
        SlideInUp(new SlideInUpAnimator()),
        OvershootInRight(new OvershootInRightAnimator(1.0f)),
        OvershootInLeft(new OvershootInLeftAnimator(1.0f));

        BaseItemAnimator animator;

        Type(BaseItemAnimator animator) {
            this.animator = animator;
        }
    }

    private MainAdapter adapter = new MainAdapter(this, new ArrayList<>(Arrays.asList(SampleData.LIST)));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_sample);

        setSupportActionBar(findViewById(R.id.tool_bar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setAdapter(adapter);

        boolean useGrid = getIntent().getBooleanExtra(ServerActivity.KEY_GRID, true);
        RecyclerView.LayoutManager layoutManager = useGrid
                ? new GridLayoutManager(this, 2)
                : new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Type type : Type.values()) {
            spinnerAdapter.add(type.name());
        }
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setItemAnimator(Type.values()[position].animator);
                if (recyclerView.getItemAnimator() != null) {
                    recyclerView.getItemAnimator().setAddDuration(500);
                    recyclerView.getItemAnimator().setRemoveDuration(500);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        findViewById(R.id.add).setOnClickListener(v -> adapter.add("newly added item", 1));
        findViewById(R.id.del).setOnClickListener(v -> adapter.remove(1));
    }
}

