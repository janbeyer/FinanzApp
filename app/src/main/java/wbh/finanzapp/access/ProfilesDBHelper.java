package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfilesDBHelper {

    private static final String LOG_TAG = ProfilesDBHelper.class.getSimpleName();

    public static final String TABLE_NAME ="profiles";

    public static final int TABLE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LASTUSE = "last_use";
    public static final String COLUMN_STARTVALUE = "start_value";

    private static final String SQL_CREATE =
        "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            COLUMN_LASTUSE + " INTEGER NOT NULL, " +
            COLUMN_STARTVALUE + " INTEGER NOT NULL DEFAULT 0" +
            ");";

    public static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the profile table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the profile table: " + e.getMessage());
        }
    }
}
