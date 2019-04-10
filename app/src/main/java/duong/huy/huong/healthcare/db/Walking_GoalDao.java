package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class Walking_GoalDao extends DbManager {
    private static final String TAG = "Walking_GoalDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Walking_Goal.allColumns;


    protected Walking_GoalDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Walking_Goal loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Walking_Goal.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Walking_Goal walking_goal = new Walking_Goal();
        walking_goal = cursorToWalking_Goal(cursor);

        cursor.close();
        database_close();

        return walking_goal;
    }

    public static ArrayList<Walking_Goal> loadAllRecords() {
        ArrayList<Walking_Goal> walking_goalList = new ArrayList<Walking_Goal>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Walking_Goal.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Walking_Goal walking_goal = cursorToWalking_Goal(cursor);
            walking_goalList.add(walking_goal);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return walking_goalList;
    }

    // Please always use the typed column names (Table_Walking_Goal) when passing arguments.
    // Example: Table_Walking_Goal.Column_Name
    public static ArrayList<Walking_Goal> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Walking_Goal> walking_goalList = new ArrayList<Walking_Goal>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Walking_Goal.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Walking_Goal walking_goal = cursorToWalking_Goal(cursor);
            walking_goalList.add(walking_goal);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return walking_goalList;
    }

    public static long insertRecord(Walking_Goal walking_goal) {
        ContentValues values = new ContentValues();
        values = getWalking_GoalValues(walking_goal);
        database_open();
        long insertId = database.insert(DbSchema.Table_Walking_Goal.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Walking_Goal walking_goal) { 
        ContentValues values = new ContentValues();
        values = getWalking_GoalValues(walking_goal);
        database_open();
        String[] where = new String[] { String.valueOf(walking_goal.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_Walking_Goal.TABLE_NAME , values, DbSchema.Table_Walking_Goal.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Walking_Goal walking_goal) { 
        database_open();
        String[] where = new String[] { String.valueOf(walking_goal.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_Walking_Goal.TABLE_NAME , DbSchema.Table_Walking_Goal.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_Walking_Goal.TABLE_NAME , DbSchema.Table_Walking_Goal.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Walking_Goal.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getWalking_GoalValues(Walking_Goal walking_goal) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Walking_Goal.COL__ID, walking_goal.get_ID());
        values.put(DbSchema.Table_Walking_Goal.COL_UID, walking_goal.getuid());
        values.put(DbSchema.Table_Walking_Goal.COL_WALKING_GOAL, walking_goal.getwalking_goal());
        values.put(DbSchema.Table_Walking_Goal.COL_WALKING_GOAL_DATE, walking_goal.getwalking_goal_date());

        return values;
    }

    protected static Walking_Goal cursorToWalking_Goal(Cursor cursor)  {
        Walking_Goal walking_goal = new Walking_Goal();

        walking_goal.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Walking_Goal.COL__ID)));
        walking_goal.setuid(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking_Goal.COL_UID)));
        walking_goal.setwalking_goal(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking_Goal.COL_WALKING_GOAL)));
        walking_goal.setwalking_goal_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking_Goal.COL_WALKING_GOAL_DATE)));

        return walking_goal;
    }

    

}

