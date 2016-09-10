package vn.dat.greennote.DatabaseHandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Alone on 04/11/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private Context DB_CONTEXT = null;
    private static String DB_NAME = "NoteDatabase.sqlite";
    private static int DB_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_CONTEXT = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Note (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                "title NVARCHAR(50) NOT NULL , " +
                "content NVARCHAR(1000) NOT NULL , " +
                "date NVARCHAR(30), " +
                "location NVARCHAR(50)," +
                "image BLOB)");
    }

    public void execute(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(sql);
    }

    public Cursor rawQuery(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void insertNote(String title, String content, String date, String location, byte[] image) {
        SQLiteDatabase db = this.getReadableDatabase();
        String insert = "INSERT INTO Note VALUES(null,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(insert);
        statement.clearBindings();

        statement.bindString(1, title);
        statement.bindString(2, content);
        statement.bindString(3, date);
        statement.bindString(4, location);
        statement.bindBlob(5, image);

        statement.executeInsert();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP IF EXISTS Note");
    }

}
