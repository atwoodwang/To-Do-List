package edu.osu.cse.todolist.to_dolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zicong on 2015/10/27.
 */
public class TaskListFragment extends Fragment {
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container,Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.fragment_task_list,container,false);
        mTaskRecyclerView = (RecyclerView)view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasks = taskLab.getTasks();

        mAdapter = new TaskAdapter(tasks);
        mTaskRecyclerView.setAdapter(mAdapter);
    }

    private class TaskHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mRemindCheckBox;
        private Task mTask;

        public TaskHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_task_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_task_date_text_view);
            mRemindCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_task_remind_check_box);
        }

        public void bindTask(Task task){
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(Task.formatDate(mTask.getDate()));
            mRemindCheckBox.setChecked(mTask.isNeed());
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        private List<Task> mTasks;

        public TaskAdapter(List<Task> tasks){
            mTasks = tasks;
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_task,parent,false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position){
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

        @Override
        public int getItemCount(){
            return mTasks.size();
        }
    }
}
