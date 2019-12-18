package ir.farzadshami.quran.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ir.farzadshami.quran.models.DbModel;

public class SqliteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "quran.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context mCtx;
    private static SqliteDbHelper dbInstance;
    public enum SearchIn{
        FARSI , ARABI
    }

    private SqliteDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCtx = context;
    }

    public static SqliteDbHelper getInstance(Context context) {
        if (dbInstance == null)
            dbInstance = new SqliteDbHelper(context);
        return dbInstance;
    }


    private static String getDatabasePath() {
        return mCtx.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public ArrayList<DbModel> getDetails(String condition, String situation) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DbModel> modelList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM quran WHERE " + condition + " = " + situation, null);
        Log.d("sql" , "SELECT * FROM quran WHERE " + condition + " = " + situation);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DbModel count = new DbModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5) == 1);
                modelList.add(count);
            }
            cursor.close();
            db.close();
        }
        return modelList;
    }

    public ArrayList<DbModel> getSearchDetails(String searchText , SearchIn searchIn , int sure_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DbModel> modelList = new ArrayList<>();
        String sql;
        if(sure_id != -1) {
             sql = "SELECT * FROM quran WHERE SuraId = " + (sure_id + 1) + " AND ";
        }else{
            sql = "SELECT * FROM quran WHERE ";
        }
        if(searchIn == SearchIn.FARSI){
            sql+=("(FarsiText LIKE '%" + searchText + "%')");
        }else {
            sql+=("(ArabicText LIKE '%" + searchText + "%')");
        }
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DbModel count = new DbModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5) == 1);
                modelList.add(count);
            }
            cursor.close();
            db.close();
        }
        return modelList;
    }

    public ArrayList<DbModel> getSearchDetails(String searchText) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DbModel> modelList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM quran WHERE (FarsiText LIKE '%" + searchText + "%' OR ArabicText Like '%" + searchText + "%')", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DbModel count = new DbModel(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5) == 1);
                modelList.add(count);
            }
            cursor.close();
            db.close();
        }
        return modelList;
    }

    public void setDetails(String sqlQuery) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sqlQuery, null);
        c.moveToFirst();
        c.close();
    }

    public void CopyDatabaseFromAssets() throws IOException {
        InputStream myInput = mCtx.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();
        File file = new File(mCtx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!file.exists())
            file.mkdir();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
            myOutput.write(buffer, 0, length);
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDatabase() throws SQLException {
        File dbFile = mCtx.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDatabaseFromAssets();
                Toast.makeText(mCtx, "Copying success from assets to folder", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                throw new RuntimeException("Error Creating database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
