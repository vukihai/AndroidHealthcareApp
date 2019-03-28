package duong.huy.huong.healthcare.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import duong.huy.huong.healthcare.MainActivity;

public class Step {
    int _id;
    int date;
    int SumStep;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getSumStep() {
        return SumStep;
    }

    public void setSumStep(int sumStep) {
        SumStep = sumStep;
    }
}
