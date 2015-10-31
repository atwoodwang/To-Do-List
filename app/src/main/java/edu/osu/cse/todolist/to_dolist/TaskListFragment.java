package edu.osu.cse.todolist.to_dolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;


/**
 * Created by Zicong on 2015/10/27.
 */
public class TaskListFragment extends Fragment {
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mAdapter;
    private FloatingActionButton mFloatingAddTask;
    private final static int EMPTY_VIEW = 10;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container,Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.fragment_task_list,container,false);
        mTaskRecyclerView = (RecyclerView)view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mFloatingAddTask=(FloatingActionButton)view.findViewById(R.id.floating_action_add_task);
        mFloatingAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                TaskLab.get(getActivity()).addTask(task);
                Intent intent = TaskDetailActivity.newIntent(getActivity(), task.getId());
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        TaskLab taskLab = TaskLab.get(getActivity());
        List<Task> tasks = taskLab.getTasks();
        if (mAdapter == null){
        mAdapter = new TaskAdapter(tasks);
        mTaskRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mIsFinishedCheckBox;
        private Task mTask;

        public TaskHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_task_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_task_date_text_view);
            mIsFinishedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_task_is_finished_check_box);
        }


        @Override
        public void onClick(View v){
            Intent intent = TaskDetailActivity.newIntent(getActivity(), mTask.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        public void bindTask(Task task){
            mTask = task;
            mTitleTextView.setText(mTask.getTitle());
            mDateTextView.setText(Task.formatDate(mTask.getDate()));
            mIsFinishedCheckBox.setChecked(mTask.isFinished());
            mIsFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mTask.setIsFinished(isChecked);
                }
            });
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
