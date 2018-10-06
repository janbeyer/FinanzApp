package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GroupsDBHelper {

    private static final String LOG_TAG = GroupsDBHelper.class.getSimpleName();

    public static final String TABLE_NAME ="groups";

    public static final int TABLE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String SQL_CREATE =
        "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL" +
            ");";

    public static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the groups table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the groups table: " + e.getMessage());
        }
    }
}
