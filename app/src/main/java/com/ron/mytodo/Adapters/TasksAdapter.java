package com.ron.mytodo.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ron.mytodo.model.Task;
import com.ron.mytodo.uis.UpdateTaskActivity;

import net.simplifiedcoding.mytodo.R;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<Task> mtaskList;

    public TasksAdapter(List<Task> taskList) {
        mtaskList = taskList;
    }

    //private Context mCtx;
    /*public TasksAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.mtaskList = taskList;
    }*/

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){return 0;}


    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = mtaskList.get(position);
        holder.textViewTask.setText(t.getTask());
        holder.textViewDesc.setText(t.getDescription());



        if (t.isFinished())
            holder.textViewStatus.setText("Completed");
        else
            holder.textViewStatus.setText("Not Completed");
    }

    @Override
    public int getItemCount() {
        return mtaskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTask, textViewDesc, textViewFinishBy, locationTextView;

        private int mCurrentPosition;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);


            itemView.setOnClickListener(this);
        }

        void clear() {

        }

        public void onBind(int position) {
            mCurrentPosition = position;
            clear();
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

        @Override
        public void onClick(View view) {
            Task task = mtaskList.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(), UpdateTaskActivity.class);
            intent.putExtra("task", task);
            view.getContext().startActivity(intent);
        }
    }
}