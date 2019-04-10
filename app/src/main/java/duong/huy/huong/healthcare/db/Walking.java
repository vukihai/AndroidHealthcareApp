package duong.huy.huong.healthcare.db;

import android.os.Bundle;
import java.util.Date;

public class Walking {

    public static final String COL__ID = "_ID";
    public static final String COL_UID = "uid";
    public static final String COL_ROAD = "road";
    public static final String COL_TIME_BEGIN = "time_begin";
    public static final String COL_TIME_END = "time_end";
    public static final String COL_DISTANCE = "distance";

    private Integer m_ID;
    private String muid;
    private String mroad;
    private String mtime_begin;
    private String mtime_end;
    private String mdistance;

    public Walking() {
    }

    public Walking(Integer _ID, String uid, String road, String time_begin, String time_end, String distance) {
        this.m_ID = _ID;
        this.muid = uid;
        this.mroad = road;
        this.mtime_begin = time_begin;
        this.mtime_end = time_end;
        this.mdistance = distance;
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

    public String getroad() {
        return mroad;
    }

    public void setroad(String road) {
        this.mroad = road;
    }

    public String gettime_begin() {
        return mtime_begin;
    }

    public void settime_begin(String time_begin) {
        this.mtime_begin = time_begin;
    }

    public String gettime_end() {
        return mtime_end;
    }

    public void settime_end(String time_end) {
        this.mtime_end = time_end;
    }

    public String getdistance() {
        return mdistance;
    }

    public void setdistance(String distance) {
        this.mdistance = distance;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_UID, this.muid);
        b.putString(COL_ROAD, this.mroad);
        b.putString(COL_TIME_BEGIN, this.mtime_begin);
        b.putString(COL_TIME_END, this.mtime_end);
        b.putString(COL_DISTANCE, this.mdistance);
        return b;
    }

    public Walking(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.muid = b.getString(COL_UID);
            this.mroad = b.getString(COL_ROAD);
            this.mtime_begin = b.getString(COL_TIME_BEGIN);
            this.mtime_end = b.getString(COL_TIME_END);
            this.mdistance = b.getString(COL_DISTANCE);
        }
    }

    @Override
    public String toString() {
        return "Walking{" +
            " m_ID=" + m_ID +
            ", muid='" + muid + '\'' +
            ", mroad='" + mroad + '\'' +
            ", mtime_begin='" + mtime_begin + '\'' +
            ", mtime_end='" + mtime_end + '\'' +
            ", mdistance='" + mdistance + '\'' +
            '}';
    }


}
