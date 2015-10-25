package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import java.util.Date;

public class TaskDetailFragment extends Fragment {
    private Task mTask;
    private Date mDate;
    private Button mDateButton;
    private EditText mTitleField;
    private EditText mDetailField;
    private CheckBox mNeedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mTask = TaskLab.get(getActivity()).getTasks().get(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_detail, container, false);

        mTitleField = (EditText)v.findViewById(R.id.task_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDetailField = (EditText)v.findViewById(R.id.task_detail);
        mDetailField.setText(mTask.getDetail());
        mDetailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDate = new Date();
        mDateButton = (Button)v.findViewById(R.id.task_time);
        mDateButton.setText(mDate.toString());


        mNeedCheckBox = (CheckBox)v.findViewById(R.id.needCheckBox);
        mNeedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setNeed(isChecked);
            }
        });

        return v;
    }
}

