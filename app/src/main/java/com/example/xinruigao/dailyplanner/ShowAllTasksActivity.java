package com.example.xinruigao.dailyplanner;

import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ShowAllTasksActivity extends AppCompatActivity implements TasksAdapter.OnItemClickListener {
    private String mSelectedDateKey;

    private RecyclerView mRecyclerView;
    private TasksAdapter mTaskAdapter;

    private FirebaseStorage mStorage;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageReference mStorageTaskRef;
    private DatabaseReference mDatabaseTaskRef;

    private ValueEventListener mDBlistener;

    private List<Upload> mUploads;

    private int mUpdatePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_tasks);

        mSelectedDateKey = getIntent().getStringExtra("SELECTED_KEY_DATE");
        System.out.println("SELECTED KEY DATE =" + mSelectedDateKey);

        mRecyclerView = findViewById(R.id.recycler_view_all_tasks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        mTaskAdapter = new TasksAdapter(ShowAllTasksActivity.this, mUploads);

        mTaskAdapter.setOnItemClickListener(ShowAllTasksActivity.this);

        mRecyclerView.setAdapter(mTaskAdapter);

        mStorage = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        //individual date (child) database and storage ref package
        mDatabaseTaskRef = mDatabaseRef.getRef().child(mSelectedDateKey);
        mStorageTaskRef = mStorageRef.child(mSelectedDateKey);

        mDBlistener = mDatabaseTaskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear list before filling it up to avoid duplicate
                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowAllTasksActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showUpdateDialog(String title, String description){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View view = inflater.inflate(R.layout.update_dialog, null);

        builder.setView(view);

        final EditText editTextUpdateTitle = view.findViewById(R.id.edit_text_update_title);
        final EditText editTextUpdateDescription = view.findViewById(R.id.edit_text_update_description);
        final ImageButton buttonUpdateTask = view.findViewById(R.id.button_update_task);

        editTextUpdateTitle.setText(title);
        editTextUpdateDescription.setText(description);

        builder.setTitle("Updating Task Title : "+ title);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextUpdateTitle.getText().toString().trim();
                String description = editTextUpdateDescription.getText().toString().trim();

                updateTask(title,description, mUpdatePosition);

                alertDialog.dismiss();
            }
        });




    }

    private void updateTask(String title, String description, int position){
        final Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKkey();

        String imageUri = selectedItem.getImageUrl();

        DatabaseReference updateDatabaseRef = mDatabaseTaskRef.child(selectedKey);

        Upload upload = new Upload(title, description, imageUri);

        updateDatabaseRef.setValue(upload);

        Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateClick(int position) {
        mUpdatePosition = position;
        Upload uploadUpdate = mUploads.get(position);
        showUpdateDialog(uploadUpdate.getTitle(), uploadUpdate.getDescription());
    }

    @Override
    public void onDeleteClick(int position) {
        final Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKkey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseTaskRef.child(selectedKey).removeValue();
                Toast.makeText(ShowAllTasksActivity.this, selectedItem.getTitle() + " deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseTaskRef.removeEventListener(mDBlistener);
    }
}

