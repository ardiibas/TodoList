package id.todo.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ibas on 20/04/2017.
 */

public class LocalDatabaseConfiguration extends SQLiteOpenHelper {
    private static final String TAG = "LocalDatabaseConfiguration";
    public static final String DATABASE_NAME = "todo.db";
    public static final int DATABASE_VERSION = 1;

    public LocalDatabaseConfiguration(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableTodos.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TableTodos.DROP_TABLE);
        onCreate(db);
    }
}
