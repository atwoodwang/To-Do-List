package edu.osu.cse.todolist.to_dolist;

/**
 * Created by AtwoodWang on 15/10/25.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by AtwoodWang on 15/10/25.
 */
public class Task {
    private UUID mId;
    private String mTitle;
    private String mDetail;
    private Date mDate;
    private String mReminder;
    private boolean mIsFinished;
    private static String mFormats = "MM/dd/yy HH:mm E";

    public Task(){
        mId = UUID.randomUUID();
        mDate = new Date();
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

    public String getReminder() {
        return mReminder;
    }

    public void setReminder(String reminder) {
        mReminder = reminder;
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public void setIsFinished(boolean isFinished) {
        mIsFinished = isFinished;
    }

    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mFormats, Locale.US);
        return simpleDateFormat.format(date);
    }
}
