package com.example.xinruigao.dailyplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ShowAllDatesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;

    private List<String> mAllDates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_dates);
    }
}
