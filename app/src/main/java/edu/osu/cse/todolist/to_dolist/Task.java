package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */
import java.util.Date;
import java.util.UUID;

/**
 * Created by AtwoodWang on 15/10/25.
 */
public class Task {
    private UUID mId;
    private String mTitle;
    private String mDetail;
    private Date mDate;
    private boolean mNeed;

    public Task(){
        mId = UUID.randomUUID();
    }

    public UUID getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isNeed() {
        return mNeed;
    }

    public void setNeed(boolean need) {
        mNeed = need;
    }
}
