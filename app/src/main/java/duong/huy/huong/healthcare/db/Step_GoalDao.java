package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class Step_GoalDao extends DbManager {
    private static final String TAG = "Step_GoalDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Step_Goal.allColumns;


    protected Step_GoalDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Step_Goal loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Step_Goal.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Step_Goal step_goal = new Step_Goal();
        step_goal = cursorToStep_Goal(cursor);

        cursor.close();
        database_close();

        return step_goal;
    }

    public static ArrayList<Step_Goal> loadAllRecords() {
        ArrayList<Step_Goal> step_goalList = new ArrayList<Step_Goal>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Step_Goal.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Step_Goal step_goal = cursorToStep_Goal(cursor);
            step_goalList.add(step_goal);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return step_goalList;
    }

    // Please always use the typed column names (Table_Step_Goal) when passing arguments.
    // Example: Table_Step_Goal.Column_Name
    public static ArrayList<Step_Goal> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Step_Goal> step_goalList = new ArrayList<Step_Goal>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Step_Goal.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Step_Goal step_goal = cursorToStep_Goal(cursor);
            step_goalList.add(step_goal);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return step_goalList;
    }

    public static long insertRecord(Step_Goal step_goal) {
        ContentValues values = new ContentValues();
        values = getStep_GoalValues(step_goal);
        database_open();
        long insertId = database.insert(DbSchema.Table_Step_Goal.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Step_Goal step_goal) { 
        ContentValues values = new ContentValues();
        values = getStep_GoalValues(step_goal);
        database_open();
        String[] where = new String[] { String.valueOf(step_goal.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_Step_Goal.TABLE_NAME , values, DbSchema.Table_Step_Goal.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Step_Goal step_goal) { 
        database_open();
        String[] where = new String[] { String.valueOf(step_goal.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_Step_Goal.TABLE_NAME , DbSchema.Table_Step_Goal.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_Step_Goal.TABLE_NAME , DbSchema.Table_Step_Goal.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Step_Goal.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getStep_GoalValues(Step_Goal step_goal) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Step_Goal.COL__ID, step_goal.get_ID());
        values.put(DbSchema.Table_Step_Goal.COL_UID, step_goal.getuid());
        values.put(DbSchema.Table_Step_Goal.COL_STEP_GOAL, step_goal.getstep_goal());
        values.put(DbSchema.Table_Step_Goal.COL_STEP_GOAL_DATE, step_goal.getstep_goal_date());

        return values;
    }

    protected static Step_Goal cursorToStep_Goal(Cursor cursor)  {
        Step_Goal step_goal = new Step_Goal();

        step_goal.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Step_Goal.COL__ID)));
        step_goal.setuid(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step_Goal.COL_UID)));
        step_goal.setstep_goal(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step_Goal.COL_STEP_GOAL)));
        step_goal.setstep_goal_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Step_Goal.COL_STEP_GOAL_DATE)));

        return step_goal;
    }

    

}

