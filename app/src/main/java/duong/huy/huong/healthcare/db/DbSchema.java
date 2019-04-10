package duong.huy.huong.healthcare.db;

import android.provider.BaseColumns;

public class DbSchema {
    private static final String TAG = "DbSchema";

    public static final String DATABASE_NAME = "data.sqlite";
    public static final int DATABASE_VERSION = 1;
    public static final String SORT_ASC = " ASC";
    public static final String SORT_DESC = " DESC";
    public static final String[] ORDERS = {SORT_ASC,SORT_DESC};
    public static final int OFF = 0;
    public static final int ON = 1;

    public static final class Table_Heart_Rate implements BaseColumns  { 
        // Table Name
        public static final String TABLE_NAME = "heart_rate";

        // Table Columns
        public static final String COL__ID = "_ID";
        public static final String COL_UID = "uid";
        public static final String COL_HEART_RATE = "heart_rate";
        public static final String COL_HR_DATE = "hr_date";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS heart_rate ( " + 
            COL__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " + 
            COL_UID + " ," + 
            COL_HEART_RATE + " ," + 
            COL_HR_DATE + "  );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS heart_rate;";

        // Columns list array
        public static final String[] allColumns = {
            COL__ID,
            COL_UID,
            COL_HEART_RATE,
            COL_HR_DATE };
    }

    public static final class Table_Walking implements BaseColumns  { 
        // Table Name
        public static final String TABLE_NAME = "walking";

        // Table Columns
        public static final String COL__ID = "_ID";
        public static final String COL_UID = "uid";
        public static final String COL_ROAD = "road";
        public static final String COL_TIME_BEGIN = "time_begin";
        public static final String COL_TIME_END = "time_end";
        public static final String COL_DISTANCE = "distance";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS walking ( " + 
            COL__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " + 
            COL_UID + " ," + 
            COL_ROAD + " ," + 
            COL_TIME_BEGIN + " ," + 
            COL_TIME_END + " ," + 
            COL_DISTANCE + "  );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS walking;";

        // Columns list array
        public static final String[] allColumns = {
            COL__ID,
            COL_UID,
            COL_ROAD,
            COL_TIME_BEGIN,
            COL_TIME_END,
            COL_DISTANCE };
    }

    public static final class Table_Walking_Goal implements BaseColumns  { 
        // Table Name
        public static final String TABLE_NAME = "walking_goal";

        // Table Columns
        public static final String COL__ID = "_ID";
        public static final String COL_UID = "uid";
        public static final String COL_WALKING_GOAL = "walking_goal";
        public static final String COL_WALKING_GOAL_DATE = "walking_goal_date";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS walking_goal ( " + 
            COL__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " + 
            COL_UID + " ," + 
            COL_WALKING_GOAL + " ," + 
            COL_WALKING_GOAL_DATE + "  );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS walking_goal;";

        // Columns list array
        public static final String[] allColumns = {
            COL__ID,
            COL_UID,
            COL_WALKING_GOAL,
            COL_WALKING_GOAL_DATE };
    }

    public static final class Table_Step_Goal implements BaseColumns  { 
        // Table Name
        public static final String TABLE_NAME = "step_goal";

        // Table Columns
        public static final String COL__ID = "_ID";
        public static final String COL_UID = "uid";
        public static final String COL_STEP_GOAL = "step_goal";
        public static final String COL_STEP_GOAL_DATE = "step_goal_date";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS step_goal ( " + 
            COL__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " + 
            COL_UID + " ," + 
            COL_STEP_GOAL + " ," + 
            COL_STEP_GOAL_DATE + "  );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS step_goal;";

        // Columns list array
        public static final String[] allColumns = {
            COL__ID,
            COL_UID,
            COL_STEP_GOAL,
            COL_STEP_GOAL_DATE };
    }

    public static final class Table_Step implements BaseColumns  { 
        // Table Name
        public static final String TABLE_NAME = "step";

        // Table Columns
        public static final String COL__ID = "_ID";
        public static final String COL_UID = "uid";
        public static final String COL_STEP = "step";
        public static final String COL_STEP_DATE = "step_date";

        // Create Table Statement
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS step ( " + 
            COL__ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " + 
            COL_UID + " ," + 
            COL_STEP + " ," + 
            COL_STEP_DATE + "  );";

        // Drop table statement
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS step;";

        // Columns list array
        public static final String[] allColumns = {
            COL__ID,
            COL_UID,
            COL_STEP,
            COL_STEP_DATE };
    }

}
