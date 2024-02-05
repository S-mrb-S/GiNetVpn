//package com.gold.hamrahvpn.recyclerview;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.gold.hamrahvpn.R;
//import com.gold.hamrahvpn.ServerActivity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
//import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
//import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
//
///**
// * Created by Daichi Furiya / Wasabeef on 2020/08/26.
// */
//public class AnimatorSampleActivity extends AppCompatActivity {
//
//    enum Type {
//        ScaleInBottom(new ScaleInBottomAnimator());
//
//        BaseItemAnimator animator;
//
//        Type(BaseItemAnimator animator) {
//            this.animator = animator;
//        }
//    }
//
//    private MainAdapter adapter = new MainAdapter(this, new ArrayList<>(Arrays.asList(SampleData.LIST)));
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_animator_sample);
//
//        setSupportActionBar(findViewById(R.id.tool_bar));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
//
//        RecyclerView recyclerView = findViewById(R.id.list);
//        recyclerView.setItemAnimator(new SlideInLeftAnimator());
//        recyclerView.setAdapter(adapter);
//
//        boolean useGrid = getIntent().getBooleanExtra(ServerActivity.KEY_GRID, true);
//        RecyclerView.LayoutManager layoutManager = useGrid
//                ? new GridLayoutManager(this, 2)
//                : new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        Spinner spinner = findViewById(R.id.spinner);
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        for (Type type : Type.values()) {
//            spinnerAdapter.add(type.name());
//        }
//        spinner.setAdapter(spinnerAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                recyclerView.setItemAnimator(Type.values()[position].animator);
//                if (recyclerView.getItemAnimator() != null) {
//                    recyclerView.getItemAnimator().setAddDuration(500);
//                    recyclerView.getItemAnimator().setRemoveDuration(500);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // no-op
//            }
//        });
//
//        findViewById(R.id.add).setOnClickListener(v -> adapter.add("newly added item", 1));
////        findViewById(R.id.del).setOnClickListener(v -> adapter.remove(1));
//    }
//}
//
