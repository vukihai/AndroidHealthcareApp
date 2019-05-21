package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class StepDao extends DbManager {
    private static final String TAG = "StepDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Step.allColumns;


    protected StepDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Step loadRecordById(int m_ID)  {
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Step.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Step step = new Step();
        step = cursorToStep(cursor);

        cursor.close();
        database_close();

        return step;
    }
    public static Step loadRecordByStep_Date(String date)  {
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Step.TABLE_NAME,allColumns,  "step_date = ?" , new String[] { String.valueOf(date) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount() <1) return null;
        Step step = new Step();
        step = cursorToStep(cursor);

        cursor.close();
        database_close();

        return step;
    }
    public static ArrayList<Step> loadAllRecords() {
        ArrayList<Step> stepList = new ArrayList<Step>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Step.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Step step = cursorToStep(cursor);
            stepList.add(step);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return stepList;
    }

    // Please always use the typed column names (Table_Step) when passing arguments.
    // Example: Table_Step.Column_Name
    public static ArrayList<Step> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Step> stepList = new ArrayList<Step>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Step.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Step step = cursorToStep(cursor);
            stepList.add(step);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return stepList;
    }

    public static long insertRecord(Step step) {
        ContentValues values = new ContentValues();
        values = getStepValues(step);
        database_open();
        long insertId = database.insert(DbSchema.Table_Step.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Step step) {
        ContentValues values = new ContentValues();
        values = getStepValues(step);
        database_open();
        String[] where = new String[] { String.valueOf(step.get_ID()) };
        int updatedId = database.update(DbSchema.Table_Step.TABLE_NAME , values, DbSchema.Table_Step.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Step step) {
        database_open();
        String[] where = new String[] { String.valueOf(step.get_ID()) };
        int deletedCount = database.delete(DbSchema.Table_Step.TABLE_NAME , DbSchema.Table_Step.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id };
        int deletedCount = database.delete(DbSchema.Table_Step.TABLE_NAME , DbSchema.Table_Step.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Step.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getStepValues(Step step) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Step.COL__ID, step.get_ID());
        values.put(DbSchema.Table_Step.COL_UID, step.getuid());
        values.put(DbSchema.Table_Step.COL_STEP, step.getstep());
        values.put(DbSchema.Table_Step.COL_STEP_DATE, step.getstep_date());

        return values;
    }

    protected static Step cursorToStep(Cursor cursor)  {
        Step step = new Step();

        step.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Step.COL__ID)));
        step.setuid(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step.COL_UID)));
        step.setstep(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step.COL_STEP)));
        step.setstep_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step.COL_STEP_DATE)));

        return step;
    }



}