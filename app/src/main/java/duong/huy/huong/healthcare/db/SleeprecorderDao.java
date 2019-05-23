package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class SleeprecorderDao extends DbManager {
    private static final String TAG = "SleeprecorderDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Sleeprecorder.allColumns;


    protected SleeprecorderDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Sleeprecorder loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Sleeprecorder.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Sleeprecorder sleeprecorder = new Sleeprecorder();
        sleeprecorder = cursorToSleeprecorder(cursor);

        cursor.close();
        database_close();

        return sleeprecorder;
    }

    public static ArrayList<Sleeprecorder> loadAllRecords() {
        ArrayList<Sleeprecorder> sleeprecorderList = new ArrayList<Sleeprecorder>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Sleeprecorder.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sleeprecorder sleeprecorder = cursorToSleeprecorder(cursor);
            sleeprecorderList.add(sleeprecorder);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return sleeprecorderList;
    }

    // Please always use the typed column names (Table_Sleeprecorder) when passing arguments.
    // Example: Table_Sleeprecorder.Column_Name
    public static ArrayList<Sleeprecorder> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Sleeprecorder> sleeprecorderList = new ArrayList<Sleeprecorder>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Sleeprecorder.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sleeprecorder sleeprecorder = cursorToSleeprecorder(cursor);
            sleeprecorderList.add(sleeprecorder);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return sleeprecorderList;
    }

    public static long insertRecord(Sleeprecorder sleeprecorder) {
        ContentValues values = new ContentValues();
        values = getSleeprecorderValues(sleeprecorder);
        database_open();
        long insertId = database.insert(DbSchema.Table_Sleeprecorder.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Sleeprecorder sleeprecorder) { 
        ContentValues values = new ContentValues();
        values = getSleeprecorderValues(sleeprecorder);
        database_open();
        String[] where = new String[] { String.valueOf(sleeprecorder.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_Sleeprecorder.TABLE_NAME , values, DbSchema.Table_Sleeprecorder.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Sleeprecorder sleeprecorder) { 
        database_open();
        String[] where = new String[] { String.valueOf(sleeprecorder.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_Sleeprecorder.TABLE_NAME , DbSchema.Table_Sleeprecorder.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_Sleeprecorder.TABLE_NAME , DbSchema.Table_Sleeprecorder.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Sleeprecorder.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getSleeprecorderValues(Sleeprecorder sleeprecorder) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Sleeprecorder.COL__ID, sleeprecorder.get_ID());
        values.put(DbSchema.Table_Sleeprecorder.COL_TIME, sleeprecorder.gettime());
        values.put(DbSchema.Table_Sleeprecorder.COL_STATUS, sleeprecorder.getstatus());

        return values;
    }

    protected static Sleeprecorder cursorToSleeprecorder(Cursor cursor)  {
        Sleeprecorder sleeprecorder = new Sleeprecorder();

        sleeprecorder.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Sleeprecorder.COL__ID)));
        sleeprecorder.settime(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Sleeprecorder.COL_TIME)));
        sleeprecorder.setstatus(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Sleeprecorder.COL_STATUS)));

        return sleeprecorder;
    }

    

}

