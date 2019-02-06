package com.example.xinruigao.dailyplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowAllDatesActivity extends AppCompatActivity implements DateAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private DateAdapter mDateAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private ValueEventListener mDBlistener;

    private List<String> mAllDates;

    private String mSelectedKeyDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_dates);

        mRecyclerView = findViewById(R.id.recycler_view_all_dates);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mAllDates = new ArrayList<>();

        mDateAdapter = new DateAdapter(ShowAllDatesActivity.this, mAllDates);
        mDateAdapter.setOnItemClickListener(ShowAllDatesActivity.this);

        mRecyclerView.setAdapter(mDateAdapter);

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDBlistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear list before filling it up to avoid duplicate
                mAllDates.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //.getKey shows the name of the package
                    String dateValue = postSnapshot.getKey();
                    mAllDates.add(dateValue);
                    System.out.println("DATE EQUALS = :  " + dateValue);
                }
                mDateAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowAllDatesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onItemClick(int position) {
        String selectedDate = mAllDates.get(position);
        mSelectedKeyDate = selectedDate;
        openShowAllTasksActivity();
        Toast.makeText(this, "Normal Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhateverClick(int position) {
        Toast.makeText(this, "On Whatever Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        String selectedDate = mAllDates.get(position);
        String selectedKey = selectedDate;
        mDatabaseRef.child(selectedKey).removeValue();
        Toast.makeText(this, mSelectedKeyDate + " deleted", Toast.LENGTH_SHORT).show();

    }

    private void openShowAllTasksActivity(){
        Intent intent = new Intent(this, ShowAllTasksActivity.class);
        intent.putExtra("SELECTED_KEY_DATE", mSelectedKeyDate);
        startActivity(intent);
    }
    //prevent stacking up of mDBListener everytime the activity is restarted
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }
}
