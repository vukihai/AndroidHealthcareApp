package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class Heart_RateDao extends DbManager {
    private static final String TAG = "Heart_RateDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_Heart_Rate.allColumns;


    protected Heart_RateDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static Heart_Rate loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_Heart_Rate.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Heart_Rate heart_rate = new Heart_Rate();
        heart_rate = cursorToHeart_Rate(cursor);

        cursor.close();
        database_close();

        return heart_rate;
    }

    public static ArrayList<Heart_Rate> loadAllRecords() {
        ArrayList<Heart_Rate> heart_rateList = new ArrayList<Heart_Rate>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_Heart_Rate.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Heart_Rate heart_rate = cursorToHeart_Rate(cursor);
            heart_rateList.add(heart_rate);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return heart_rateList;
    }

    // Please always use the typed column names (Table_Heart_Rate) when passing arguments.
    // Example: Table_Heart_Rate.Column_Name
    public static ArrayList<Heart_Rate> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Heart_Rate> heart_rateList = new ArrayList<Heart_Rate>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_Heart_Rate.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Heart_Rate heart_rate = cursorToHeart_Rate(cursor);
            heart_rateList.add(heart_rate);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return heart_rateList;
    }

    public static long insertRecord(Heart_Rate heart_rate) {
        ContentValues values = new ContentValues();
        values = getHeart_RateValues(heart_rate);
        database_open();
        long insertId = database.insert(DbSchema.Table_Heart_Rate.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(Heart_Rate heart_rate) { 
        ContentValues values = new ContentValues();
        values = getHeart_RateValues(heart_rate);
        database_open();
        String[] where = new String[] { String.valueOf(heart_rate.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_Heart_Rate.TABLE_NAME , values, DbSchema.Table_Heart_Rate.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(Heart_Rate heart_rate) { 
        database_open();
        String[] where = new String[] { String.valueOf(heart_rate.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_Heart_Rate.TABLE_NAME , DbSchema.Table_Heart_Rate.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_Heart_Rate.TABLE_NAME , DbSchema.Table_Heart_Rate.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_Heart_Rate.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getHeart_RateValues(Heart_Rate heart_rate) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_Heart_Rate.COL__ID, heart_rate.get_ID());
        values.put(DbSchema.Table_Heart_Rate.COL_UID, heart_rate.getuid());
        values.put(DbSchema.Table_Heart_Rate.COL_HEART_RATE, heart_rate.getheart_rate());
        values.put(DbSchema.Table_Heart_Rate.COL_HR_DATE, heart_rate.gethr_date());

        return values;
    }

    protected static Heart_Rate cursorToHeart_Rate(Cursor cursor)  {
        Heart_Rate heart_rate = new Heart_Rate();

        heart_rate.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_Heart_Rate.COL__ID)));
        heart_rate.setuid(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Heart_Rate.COL_UID)));
        heart_rate.setheart_rate(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Heart_Rate.COL_HEART_RATE)));
        heart_rate.sethr_date(cursor.getString(cursor.getColumnIndex(DbSchema.Table_Heart_Rate.COL_HR_DATE)));

        return heart_rate;
    }

    

}

