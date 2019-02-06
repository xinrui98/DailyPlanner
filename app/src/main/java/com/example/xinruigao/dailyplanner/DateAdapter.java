package com.example.xinruigao.dailyplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private Context mContext;
    private List<String> mAllDates;

    public DateAdapter(Context mContext, List<String> mAllDates) {
        this.mContext = mContext;
        this.mAllDates = mAllDates;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.all_dates, parent, false);
        return new DateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder dateViewHolder, int postion) {
        String dateCurrent = mAllDates.get(postion);
        dateViewHolder.textViewDatePackage.setText(dateCurrent);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewDatePackage;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDatePackage = itemView.findViewById(R.id.text_view_date_package);
        }
    }


}
