package duong.huy.huong.healthcare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager {
    private static final String TAG = "DBMANAGER";

    private static DbManager sInstance;

    private static long errorId = 0;

    private Context mCtx;
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    public static DbManager setConfig(Context context) {
        if (sInstance==null){
            sInstance = new DbManager(context);}
        return sInstance;
    }


    protected static DbManager getsInstance() {
        if (sInstance==null) throw new NullPointerException("DbManager is null, Please set DbManger.setConfig(this) in your activity before using the DAO objects.");
        return sInstance;
    }


    private DbManager(Context ctx) {
        this.mCtx = ctx;
    }

    protected DbManager() {}

    public DbManager open() {
        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
        return mDb;
    }

    private static class DbHelper extends SQLiteOpenHelper {
        private static DbHelper sInstance;

        private static final String LOG_TAG = "DbHelper";
        private DbManager mDbManager;

        public static DbHelper getInstance(DbManager manager) {
            if (sInstance == null) {
                sInstance = new DbHelper(manager);
            }
            return sInstance;
        }


        private DbHelper(DbManager dbmanger) {
            super(dbmanger.mCtx, DbSchema.DATABASE_NAME, null, DbSchema.DATABASE_VERSION);
            mDbManager = dbmanger;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbSchema.Table_Heart_Rate.CREATE_TABLE);
            db.execSQL(DbSchema.Table_Walking.CREATE_TABLE);
            db.execSQL(DbSchema.Table_Walking_Goal.CREATE_TABLE);
            db.execSQL(DbSchema.Table_Sleeprecorder.CREATE_TABLE);
            db.execSQL(DbSchema.Table_Step_Goal.CREATE_TABLE);
            db.execSQL(DbSchema.Table_Step.CREATE_TABLE);
            db.execSQL(DbSchema.Table_User_Info.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DbSchema.Table_Heart_Rate.DROP_TABLE);
            db.execSQL(DbSchema.Table_Walking.DROP_TABLE);
            db.execSQL(DbSchema.Table_Walking_Goal.DROP_TABLE);
            db.execSQL(DbSchema.Table_Sleeprecorder.DROP_TABLE);
            db.execSQL(DbSchema.Table_Step_Goal.DROP_TABLE);
            db.execSQL(DbSchema.Table_Step.DROP_TABLE);
            db.execSQL(DbSchema.Table_User_Info.DROP_TABLE);
            this.onCreate(db);
        }

    }
}
