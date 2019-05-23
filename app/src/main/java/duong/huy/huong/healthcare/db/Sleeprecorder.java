package duong.huy.huong.healthcare.db;

import android.os.Bundle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sleeprecorder {

    public static final String COL__ID = "_ID";
    public static final String COL_TIME = "time";
    public static final String COL_STATUS = "status";

    private Integer m_ID;
    private String mtime;
    private String mstatus;

    public Sleeprecorder() {
    }

    public Sleeprecorder(Integer _ID, String time, String status) {
        this.m_ID = _ID;
        this.mtime = time;
        this.mstatus = status;
    }

    public Integer get_ID() {
        return m_ID;
    }

    public void set_ID(Integer _ID) {
        this.m_ID = _ID;
    }

    public String gettime() {
        return mtime;
    }

    public void settime(String time) {
        this.mtime = time;
    }

    public String getstatus() {
        return mstatus;
    }

    public void setstatus(String status) {
        this.mstatus = status;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_TIME, this.mtime);
        b.putString(COL_STATUS, this.mstatus);
        return b;
    }

    public Sleeprecorder(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.mtime = b.getString(COL_TIME);
            this.mstatus = b.getString(COL_STATUS);
        }
    }

    @Override
    public String toString() {
        Timestamp stamp = new Timestamp(Long.valueOf(mtime));
        Date date = new Date(stamp.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String strDate = formatter.format(date);

        return strDate;
    }


}
