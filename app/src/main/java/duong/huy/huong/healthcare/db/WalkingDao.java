package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class WalkingDao extends DbManager {
    private static final String TAG = "WalkingDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Walking.allColumns;


    protected WalkingDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Walking loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Walking.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Walking walking = new Walking();
        walking = cursorToWalking(cursor);

        cursor.close();
        database_close();

        return walking;
    }

    public static ArrayList<Walking> loadAllRecords() {
        ArrayList<Walking> walkingList = new ArrayList<Walking>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Walking.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Walking walking = cursorToWalking(cursor);
            walkingList.add(walking);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return walkingList;
    }

    // Please always use the typed column names (Table_Walking) when passing arguments.
    // Example: Table_Walking.Column_Name
    public static ArrayList<Walking> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Walking> walkingList = new ArrayList<Walking>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Walking.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Walking walking = cursorToWalking(cursor);
            walkingList.add(walking);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return walkingList;
    }

    public static long insertRecord(Walking walking) {
        ContentValues values = new ContentValues();
        values = getWalkingValues(walking);
        database_open();
        long insertId = database.insert(DbSchema.Table_Walking.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Walking walking) { 
        ContentValues values = new ContentValues();
        values = getWalkingValues(walking);
        database_open();
        String[] where = new String[] { String.valueOf(walking.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_Walking.TABLE_NAME , values, DbSchema.Table_Walking.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Walking walking) { 
        database_open();
        String[] where = new String[] { String.valueOf(walking.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_Walking.TABLE_NAME , DbSchema.Table_Walking.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_Walking.TABLE_NAME , DbSchema.Table_Walking.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Walking.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getWalkingValues(Walking walking) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Walking.COL__ID, walking.get_ID());
        values.put(DbSchema.Table_Walking.COL_UID, walking.getuid());
        values.put(DbSchema.Table_Walking.COL_ROAD, walking.getroad());
        values.put(DbSchema.Table_Walking.COL_TIME_BEGIN, walking.gettime_begin());
        values.put(DbSchema.Table_Walking.COL_TIME_END, walking.gettime_end());
        values.put(DbSchema.Table_Walking.COL_DISTANCE, walking.getdistance());

        return values;
    }

    protected static Walking cursorToWalking(Cursor cursor)  {
        Walking walking = new Walking();

        walking.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Walking.COL__ID)));
        walking.setuid(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking.COL_UID)));
        walking.setroad(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking.COL_ROAD)));
        walking.settime_begin(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking.COL_TIME_BEGIN)));
        walking.settime_end(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking.COL_TIME_END)));
        walking.setdistance(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Walking.COL_DISTANCE)));

        return walking;
    }

    

}

