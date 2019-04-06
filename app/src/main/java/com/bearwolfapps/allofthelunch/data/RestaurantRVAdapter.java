package com.bearwolfapps.allofthelunch.data;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class RestaurantRVAdapter extends RecyclerView.Adapter<RestaurantRVAdapter.RestaurantViewHolder> {
    List<LunchItems.Restaurant> items;
    public float textStorlek;

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        return new RestaurantViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder rvh, int i) {
        rvh.durr(items.get(i));
    }

    public RestaurantRVAdapter(List<LunchItems.Restaurant> restaurants){
        items = restaurants;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;



        public RestaurantViewHolder(@NonNull LinearLayout itemView) {
            super(itemView);
            layout = itemView;

        }

        public void durr(LunchItems.Restaurant restaurant){
            layout.removeAllViewsInLayout();

            TextView tv = new TextView(layout.getContext());
            tv.setText(restaurant.name);
            tv.setTextColor(Color.BLACK);
            //tv.setText(restaurant.name.toUpperCase());
            tv.setTextSize(24);
            layout.addView(tv);
            StringBuilder sb = new StringBuilder();
            for(LunchItems.MenuItem mi : restaurant.menu){
                sb.append(mi.toString() + "\n");
            }
            tv = new TextView(layout.getContext());
            tv.setTextColor(Color.BLACK);
            tv.setText(sb.toString());
            layout.addView(tv);
        }
    }
}
