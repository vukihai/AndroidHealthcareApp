package duong.huy.huong.healthcare.db;

import android.os.Bundle;
import java.util.Date;

public class Step_Goal {

    public static final String COL__ID = "_ID";
    public static final String COL_UID = "uid";
    public static final String COL_STEP_GOAL = "step_goal";
    public static final String COL_STEP_GOAL_DATE = "step_goal_date";

    private Integer m_ID;
    private String muid;
    private String mstep_goal;
    private String mstep_goal_date;

    public Step_Goal() {
    }

    public Step_Goal(Integer _ID, String uid, String step_goal, String step_goal_date) {
        this.m_ID = _ID;
        this.muid = uid;
        this.mstep_goal = step_goal;
        this.mstep_goal_date = step_goal_date;
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

    public String getstep_goal() {
        return mstep_goal;
    }

    public void setstep_goal(String step_goal) {
        this.mstep_goal = step_goal;
    }

    public String getstep_goal_date() {
        return mstep_goal_date;
    }

    public void setstep_goal_date(String step_goal_date) {
        this.mstep_goal_date = step_goal_date;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_UID, this.muid);
        b.putString(COL_STEP_GOAL, this.mstep_goal);
        b.putString(COL_STEP_GOAL_DATE, this.mstep_goal_date);
        return b;
    }

    public Step_Goal(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.muid = b.getString(COL_UID);
            this.mstep_goal = b.getString(COL_STEP_GOAL);
            this.mstep_goal_date = b.getString(COL_STEP_GOAL_DATE);
        }
    }

    @Override
    public String toString() {
        return "Step_Goal{" +
            " m_ID=" + m_ID +
            ", muid='" + muid + '\'' +
            ", mstep_goal='" + mstep_goal + '\'' +
            ", mstep_goal_date='" + mstep_goal_date + '\'' +
            '}';
    }


}
