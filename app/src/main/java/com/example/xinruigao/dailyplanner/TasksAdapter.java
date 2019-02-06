package com.example.xinruigao.dailyplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder>{
    private Context mContext;
    private List<Upload> mUploads;

    private OnItemClickListener mListener;

    public TasksAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.all_tasks, parent, false);
        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewTaskTitle.setText(uploadCurrent.getTitle());
        holder.textViewTaskDescription.setText(uploadCurrent.getDescription());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.drawable.planner_app_icon)
                .fit()
                .centerCrop()
                .into(holder.imageViewTaskUploaded);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewTaskTitle, textViewTaskDescription;
        public ImageView imageViewTaskUploaded;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTaskTitle = itemView.findViewById(R.id.text_view_task_title);
            textViewTaskDescription = itemView.findViewById(R.id.text_view_task_description);
            imageViewTaskUploaded = itemView.findViewById(R.id.image_view_uploaded_task);

            itemView.setOnClickListener(this);
            //long click
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do Whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhateverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhateverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}