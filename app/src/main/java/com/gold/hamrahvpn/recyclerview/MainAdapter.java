package com.gold.hamrahvpn.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gold.hamrahvpn.R;

import java.util.List;
//import com.squareup.picasso.Picasso

/**
 * Created by Daichi Furiya / Wasabeef on 2020/08/26.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private List<String> dataSet;

    public MainAdapter(Context context, List<String> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Picasso.get().load(R.drawable.chip).into(holder.image);
        holder.text.setText(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void remove(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String text, int position) {
        dataSet.add(position, text);
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
//      image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }
    }
}
