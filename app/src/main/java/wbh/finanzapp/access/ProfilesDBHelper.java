package wbh.finanzapp.access;

import android.database.Cursor;
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
    static final String[] columns = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION,
            COLUMN_LAST_USE
    };

    /**
     * Groups table SQL Create statement.
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_LAST_USE + " INTEGER NOT NULL" +
                    ");";

    /**
     * Create the Groups Table.
     */
    static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the profile table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the profile table: " + e.getMessage());
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
    static Cursor getProfileCursor(DBHelper dbHelper, long id) {
        return dbHelper.getDatabase().query(
                TABLE_NAME,
                columns,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
    }

    /**
     * Return an database get Cursor to a Group.
     */
    static Cursor getProfilesCursor(DBHelper dbHelper) {
        return dbHelper.getDatabase().query(
                TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                COLUMN_LAST_USE + " DESC");
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
