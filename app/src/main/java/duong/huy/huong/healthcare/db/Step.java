package duong.huy.huong.healthcare.db;

import android.os.Bundle;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Step {

    public static final String COL__ID = "_ID";
    public static final String COL_UID = "uid";
    public static final String COL_STEP = "step";
    public static final String COL_STEP_DATE = "step_date";

    private Integer m_ID;
    private String muid;
    private String mstep;
    private String mstep_date;

    public Step() {
    }

    public Step(Integer _ID, String uid, String step, String step_date) {
        this.m_ID = _ID;
        this.muid = uid;
        this.mstep = step;
        this.mstep_date = step_date;
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

    public String getstep() {
        return mstep;
    }

    public void setstep(String step) {
        this.mstep = step;
    }

    public String getstep_date() {
        return mstep_date;
    }

    public void setstep_date(String step_date) {
        this.mstep_date = step_date;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_UID, this.muid);
        b.putString(COL_STEP, this.mstep);
        b.putString(COL_STEP_DATE, this.mstep_date);
        return b;
    }

    public Step(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.muid = b.getString(COL_UID);
            this.mstep = b.getString(COL_STEP);
            this.mstep_date = b.getString(COL_STEP_DATE);
        }
    }

    @Override
    public String toString() {
        return "Step{" +
            " m_ID=" + m_ID +
            ", muid='" + muid + '\'' +
            ", mstep='" + mstep + '\'' +
            ", mstep_date='" + mstep_date + '\'' +
            '}';
    }


}
