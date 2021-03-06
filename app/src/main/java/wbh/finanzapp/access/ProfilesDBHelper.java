package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Helper class for SQL statements for Profile table.
 */
class ProfilesDBHelper {

    private static final String LOG_TAG = ProfilesDBHelper.class.getSimpleName();

    static final String TABLE_NAME = "profiles";
    static final int TABLE_VERSION = 1;

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_LAST_USE = "last_use";

    /**
     * The table columns for the Profile table.
     */
    static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION,
            COLUMN_LAST_USE
    };

    /**
     * Profiles table SQL Create statement.
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_LAST_USE + " INTEGER NOT NULL" +
                    ");";

    /**
     * Create the Profile Table.
     */
    static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the profile table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the profile table: " + e.getMessage());
        }
    }
}
