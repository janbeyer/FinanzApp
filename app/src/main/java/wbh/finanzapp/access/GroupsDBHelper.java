package wbh.finanzapp.access;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Helper class for SQL statements for Groups table.
 */
class GroupsDBHelper {

    private static final String LOG_TAG = GroupsDBHelper.class.getSimpleName();

    static final String TABLE_NAME = "groups";
    static final int TABLE_VERSION = 1;

    static final String COLUMN_ID = "_id";
    static final String COLUMN_PROFILE_ID = "_profileId";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DESCRIPTION = "description";

    /**
     * The table columns for the Groups table.
     */
    static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_PROFILE_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION
    };

    /**
     * Groups table SQL Create statement.
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROFILE_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    "CONSTRAINT fk_profile FOREIGN KEY (" + COLUMN_PROFILE_ID + ") REFERENCES " + ProfilesDBHelper.TABLE_NAME + " (" + ProfilesDBHelper.COLUMN_ID + ") ON DELETE CASCADE" +
                    ");";

    /**
     * Create the Groups Table.
     */
    static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the groups table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the groups table: " + e.getMessage());
        }
    }
}
