package duong.huy.huong.healthcare.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class User_InfoDao extends DbManager {
    private static final String TAG = "User_InfoDao";

    protected static SQLiteDatabase database;
    protected static DbManager mDbManager;
    protected static  String[] allColumns = DbSchema.Table_User_Info.allColumns;


    protected User_InfoDao() {
    }

    protected static void database_open() throws SQLException {
        mDbManager = DbManager.getsInstance();
        database = mDbManager.getDatabase();
    }

    protected static void database_close() {
        mDbManager = DbManager.getsInstance();
        mDbManager.close();
    }

    public static User_Info loadRecordById(int m_ID)  { 
        database_open();
        Cursor cursor = database.query(DbSchema.Table_User_Info.TABLE_NAME,allColumns,  "_ID = ?" , new String[] { String.valueOf(m_ID) } , null, null, null,null);

        if (cursor != null)
            cursor.moveToFirst();

        User_Info user_info = new User_Info();
        user_info = cursorToUser_Info(cursor);

        cursor.close();
        database_close();

        return user_info;
    }

    public static ArrayList<User_Info> loadAllRecords() {
        ArrayList<User_Info> user_infoList = new ArrayList<User_Info>();
        database_open();

        Cursor cursor = database.query(
                DbSchema.Table_User_Info.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User_Info user_info = cursorToUser_Info(cursor);
            user_infoList.add(user_info);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return user_infoList;
    }

    // Please always use the typed column names (Table_User_Info) when passing arguments.
    // Example: Table_User_Info.Column_Name
    public static ArrayList<User_Info> loadAllRecords(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<User_Info> user_infoList = new ArrayList<User_Info>();
        database_open();

        if(TextUtils.isEmpty(selection)){
            selection = null;
            selectionArgs = null;
        }

        Cursor cursor = database.query(
                DbSchema.Table_User_Info.TABLE_NAME,
                allColumns,
                selection==null ? null : selection,
                selectionArgs==null ? null : selectionArgs,
                groupBy==null ? null : groupBy,
                having==null ? null : having,
                orderBy==null ? null : orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User_Info user_info = cursorToUser_Info(cursor);
            user_infoList.add(user_info);
            cursor.moveToNext();
        }
        cursor.close();
        database_close();
        return user_infoList;
    }

    public static long insertRecord(User_Info user_info) {
        ContentValues values = new ContentValues();
        values = getUser_InfoValues(user_info);
        database_open();
        long insertId = database.insert(DbSchema.Table_User_Info.TABLE_NAME , null, values);
        database_close();
        return insertId;
    }

    public static int updateRecord(User_Info user_info) { 
        ContentValues values = new ContentValues();
        values = getUser_InfoValues(user_info);
        database_open();
        String[] where = new String[] { String.valueOf(user_info.get_ID()) }; 
        int updatedId = database.update(DbSchema.Table_User_Info.TABLE_NAME , values, DbSchema.Table_User_Info.COL__ID + " = ? ",where );
        database_close();
        return updatedId;
    }

    public static int deleteRecord(User_Info user_info) { 
        database_open();
        String[] where = new String[] { String.valueOf(user_info.get_ID()) }; 
        int deletedCount = database.delete(DbSchema.Table_User_Info.TABLE_NAME , DbSchema.Table_User_Info.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteRecord(String id) {
        database_open();
        String[] where = new String[] { id }; 
        int deletedCount = database.delete(DbSchema.Table_User_Info.TABLE_NAME , DbSchema.Table_User_Info.COL__ID + " = ? ",where );
        database_close();
        return deletedCount;
    }

    public static int deleteAllRecords() {
        database_open();
        int deletedCount = database.delete(DbSchema.Table_User_Info.TABLE_NAME , null, null );
        database_close();
        return deletedCount;
    }

    protected static ContentValues getUser_InfoValues(User_Info user_info) {
        ContentValues values = new ContentValues();

        values.put(DbSchema.Table_User_Info.COL__ID, user_info.get_ID());
        values.put(DbSchema.Table_User_Info.COL_NAME, user_info.getname());
        values.put(DbSchema.Table_User_Info.COL_SEX, user_info.getsex());
        values.put(DbSchema.Table_User_Info.COL_DATE_OF_BIRTH, user_info.getdate_of_birth());
        values.put(DbSchema.Table_User_Info.COL_WEIGHT, user_info.getweight());
        values.put(DbSchema.Table_User_Info.COL_HEIGHT, user_info.getheight());

        return values;
    }

    protected static User_Info cursorToUser_Info(Cursor cursor)  {
        User_Info user_info = new User_Info();

        user_info.set_ID(cursor.getInt(cursor.getColumnIndex(DbSchema.Table_User_Info.COL__ID)));
        user_info.setname(cursor.getString(cursor.getColumnIndex(DbSchema.Table_User_Info.COL_NAME)));
        user_info.setsex(cursor.getString(cursor.getColumnIndex(DbSchema.Table_User_Info.COL_SEX)));
        user_info.setdate_of_birth(cursor.getString(cursor.getColumnIndex(DbSchema.Table_User_Info.COL_DATE_OF_BIRTH)));
        user_info.setweight(cursor.getString(cursor.getColumnIndex(DbSchema.Table_User_Info.COL_WEIGHT)));
        user_info.setheight(cursor.getString(cursor.getColumnIndex(DbSchema.Table_User_Info.COL_HEIGHT)));

        return user_info;
    }

    

}

