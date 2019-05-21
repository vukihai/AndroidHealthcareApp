package duong.huy.huong.healthcare.db;

import android.os.Bundle;
import java.util.Date;

public class User_Info {

    public static final String COL__ID = "_ID";
    public static final String COL_NAME = "name";
    public static final String COL_SEX = "sex";
    public static final String COL_DATE_OF_BIRTH = "date_of_birth";
    public static final String COL_WEIGHT = "weight";
    public static final String COL_HEIGHT = "height";

    private Integer m_ID;
    private String mname;
    private String msex;
    private String mdate_of_birth;
    private String mweight;
    private String mheight;

    public User_Info() {
    }

    public User_Info(Integer _ID, String name, String sex, String date_of_birth, String weight, String height) {
        this.m_ID = _ID;
        this.mname = name;
        this.msex = sex;
        this.mdate_of_birth = date_of_birth;
        this.mweight = weight;
        this.mheight = height;
    }

    public Integer get_ID() {
        return m_ID;
    }

    public void set_ID(Integer _ID) {
        this.m_ID = _ID;
    }

    public String getname() {
        return mname;
    }

    public void setname(String name) {
        this.mname = name;
    }

    public String getsex() {
        return msex;
    }

    public void setsex(String sex) {
        this.msex = sex;
    }

    public String getdate_of_birth() {
        return mdate_of_birth;
    }

    public void setdate_of_birth(String date_of_birth) {
        this.mdate_of_birth = date_of_birth;
    }

    public String getweight() {
        return mweight;
    }

    public void setweight(String weight) {
        this.mweight = weight;
    }

    public String getheight() {
        return mheight;
    }

    public void setheight(String height) {
        this.mheight = height;
    }


    public Bundle toBundle() { 
        Bundle b = new Bundle();
        b.putInt(COL__ID, this.m_ID);
        b.putString(COL_NAME, this.mname);
        b.putString(COL_SEX, this.msex);
        b.putString(COL_DATE_OF_BIRTH, this.mdate_of_birth);
        b.putString(COL_WEIGHT, this.mweight);
        b.putString(COL_HEIGHT, this.mheight);
        return b;
    }

    public User_Info(Bundle b) {
        if (b != null) {
            this.m_ID = b.getInt(COL__ID);
            this.mname = b.getString(COL_NAME);
            this.msex = b.getString(COL_SEX);
            this.mdate_of_birth = b.getString(COL_DATE_OF_BIRTH);
            this.mweight = b.getString(COL_WEIGHT);
            this.mheight = b.getString(COL_HEIGHT);
        }
    }

    @Override
    public String toString() {
        return "User_Info{" +
            " m_ID=" + m_ID +
            ", mname='" + mname + '\'' +
            ", msex='" + msex + '\'' +
            ", mdate_of_birth='" + mdate_of_birth + '\'' +
            ", mweight='" + mweight + '\'' +
            ", mheight='" + mheight + '\'' +
            '}';
    }


}
