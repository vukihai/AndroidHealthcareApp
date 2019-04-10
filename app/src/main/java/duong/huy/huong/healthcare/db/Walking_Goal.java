package duong.huy.huong.healthcare.db;

import android.os.Bundle;
import java.util.Date;

public class Walking_Goal {

    public static final String COL__ID = "_ID";
    public static final String COL_UID = "uid";
    public static final String COL_WALKING_GOAL = "walking_goal";
    public static final String COL_WALKING_GOAL_DATE = "walking_goal_date";

    private Integer m_ID;
    private String muid;
    private String mwalking_goal;
    private String mwalking_goal_date;

    public Walking_Goal() {
    }

    public Walking_Goal(Integer _ID, String uid, String walking_goal, String walking_goal_date) {
        this.m_ID = _ID;
        this.muid = uid;
        this.mwalking_goal = walking_goal;
        this.mwalking_goal_date = walking_goal_date;
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

    public String getwalking_goal() {
        return mwalking_goal;
    }

    public void setwalking_goal(String walking_goal) {
        this.mwalking_goal = walking_goal;
    }

    public String getwalking_goal_date() {
        return mwalking_goal_date;
    }

    public void setwalking_goal_date(String walking_goal_date) {
        this.mwalking_goal_date = walking_goal_date;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_UID, this.muid);
        b.putString(COL_WALKING_GOAL, this.mwalking_goal);
        b.putString(COL_WALKING_GOAL_DATE, this.mwalking_goal_date);
        return b;
    }

    public Walking_Goal(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.muid = b.getString(COL_UID);
            this.mwalking_goal = b.getString(COL_WALKING_GOAL);
            this.mwalking_goal_date = b.getString(COL_WALKING_GOAL_DATE);
        }
    }

    @Override
    public String toString() {
        return "Walking_Goal{" +
            " m_ID=" + m_ID +
            ", muid='" + muid + '\'' +
            ", mwalking_goal='" + mwalking_goal + '\'' +
            ", mwalking_goal_date='" + mwalking_goal_date + '\'' +
            '}';
    }


}
