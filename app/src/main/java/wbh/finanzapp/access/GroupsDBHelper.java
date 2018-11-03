package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class GroupsDBHelper {

    private static final String LOG_TAG = GroupsDBHelper.class.getSimpleName();

    static final String TABLE_NAME = "groups";

    static final int TABLE_VERSION = 1;

    static final String COLUMN_ID = "_id";

    static final String COLUMN_PROFILE_ID = "_profileId";

    static final String COLUMN_NAME = "name";

    static final String COLUMN_DESCRIPTION = "description";

    static final String COLUMN_WRITEABLE = "writeable";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROFILE_ID + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_WRITEABLE + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_PROFILE_ID + ") REFERENCES " + ProfilesDBHelper.TABLE_NAME + "(" + ProfilesDBHelper.COLUMN_ID + ")" +
                    ");";

    static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the groups table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the groups table: " + e.getMessage());
        }
    }
}
