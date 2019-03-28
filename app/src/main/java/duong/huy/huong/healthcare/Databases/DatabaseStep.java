package duong.huy.huong.healthcare.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseStep extends SQLiteOpenHelper {

    public static final String DB_STEP = "Step"; //tên database

    public static final String TB_SUMSTEP = "SumStep"; //tên bảng
    public static final String KEY_ID_SUMSTEP = "id"; //tên ID bảng SumStep
    public static final String KEY_DAY_SUMSTEP = "day"; //tên cột day
    public static final String KEY_SUMSTEP_SUMSTEP = "SumStep"; //tên cột tổng bước

    public static final int DB_VERSION = 1;    // số version
    private SQLiteDatabase db;

    public static final String CREATE_TABLE_SUMSTEP =
            "CREATE TABLE " + TB_SUMSTEP + "(" +
                    KEY_ID_SUMSTEP + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                    ", " + KEY_DAY_SUMSTEP + " TEXT NOT NULL" +
                    "," + KEY_SUMSTEP_SUMSTEP + " TEXT NOT NULL" +
                    ")";

    public DatabaseStep(Context context) {
        super(context, DB_STEP, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      /**  //tạo bảng
        String query = "CREATE TABLE SUMSTEP( _id INTEGER PRIMARY KEY AUTOINCREMENT, Day DATE, SumStep INT);";
        db.execSQL(query);
        database.getWritableDatabase();
        this.close();
       */

        try{
            db.execSQL(CREATE_TABLE_SUMSTEP);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** //nếu đã có bảng rồi thì xoá
         db.execSQL("DROP TABLE IF EXISTS SUMSTEP");
         onCreate(db);
         */
    }
    // mở csdl
    public void open(){
        try{
            db = getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //đóng csdl
    public void close(){
        if (db != null && db.isOpen()){
            try{
                db.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * nhận dữ liệu từ bảng sql , con trỏ sẽ di chuyển đến phần tử đầu tiên để lấy dữ liệu
     * @param sql từ bảng sql
     * @return trả về con trỏ
     */
    public Cursor getAll(String sql){
        open();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }

    /**
     * chèn nội dung giá trị vào bảng
     * @param table bảng chèn
     * @param values giá trị chèn
     * @return chèn vào bảng
     */
    public long insert(String table, ContentValues values){
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }

    /**
     *  cập nhật các giá trị khi thay đổi cần cập nhật vào bảng
     * @param table bảng cập nhật
     * @param values giá trị cập nhật
     * @param where điều kiện khi cập nhật
     * @return cập nhật bảng
     */
    public boolean update(String table, ContentValues values, String where){
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }

    /**
     * xóa bảng hay xóa cột với điều kiện cần ra
     * @param table xóa trong bảng
     * @param where điều kiện xóa
     * @return bảng xóa
     */
    public boolean delete(String table, String where){
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }
}
