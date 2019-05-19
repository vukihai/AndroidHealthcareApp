package duong.huy.huong.healthcare.db;

import android.os.Bundle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Heart_Rate {

    public static final String COL__ID = "_ID";
    public static final String COL_UID = "uid";
    public static final String COL_HEART_RATE = "heart_rate";
    public static final String COL_HR_DATE = "hr_date";

    private Integer m_ID;
    private String muid;
    private String mheart_rate;
    private String mhr_date;

    public Heart_Rate() {
    }

    public Heart_Rate(Integer _ID, String uid, String heart_rate, String hr_date) {
        this.m_ID = _ID;
        this.muid = uid;
        this.mheart_rate = heart_rate;
        this.mhr_date = hr_date;
    }

    public Integer get_ID() {
        return m_ID;
    }

    public void set_ID(Integer _ID) {
        this.m_ID = _ID;
    }

    public String getuid() {
        return muid;
    }

    public void setuid(String uid) {
        this.muid = uid;
    }

    public String getheart_rate() {
        return mheart_rate;
    }

    public void setheart_rate(String heart_rate) {
        this.mheart_rate = heart_rate;
    }

    public String gethr_date() {
        return mhr_date;
    }

    public void sethr_date(String hr_date) {
        this.mhr_date = hr_date;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_UID, this.muid);
        b.putString(COL_HEART_RATE, this.mheart_rate);
        b.putString(COL_HR_DATE, this.mhr_date);
        return b;
    }

    public Heart_Rate(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.muid = b.getString(COL_UID);
            this.mheart_rate = b.getString(COL_HEART_RATE);
            this.mhr_date = b.getString(COL_HR_DATE);
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return " nhịp tim: " + mheart_rate + " đo vào ngày: " + dt.format(new Date(Long.valueOf(mhr_date)));
    }


}
