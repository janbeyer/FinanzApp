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
    private static final String[] columns = {
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
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
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

    /**
     * Return an database insert Cursor to a Group.
     */
    static Cursor getInsertCursor(DBHelper dbHelper, long insertId) {
        return dbHelper.getDatabase().query(
                TABLE_NAME,
                columns,
                COLUMN_ID + "=" + insertId,
                null,
                null,
                null,
                null);
    }

    /**
     * Return an database get Cursor to a Group.
     */
    static Cursor getGroupsCursor(DBHelper dbHelper, long profileId) {
        return dbHelper.getDatabase().query(
                TABLE_NAME,
                columns,
                COLUMN_PROFILE_ID + "=?",
                new String[]{String.valueOf(profileId)},
                null,
                null,
                COLUMN_NAME + " ASC");
    }

    /**
     * Return an database update Cursor to a Group.
     */
    static Cursor getUpdateCursor(DBHelper dbHelper, long id) {
        return dbHelper.getDatabase().query(
                TABLE_NAME,
                columns,
                COLUMN_ID + "=" + id,
                null,
                null,
                null,
                null);
    }
}
