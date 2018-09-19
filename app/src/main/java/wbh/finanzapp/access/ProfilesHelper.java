package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfilesHelper {

    private static final String LOG_TAG = ProfilesHelper.class.getSimpleName();

    public static final String TABLE_NAME ="profiles";

    public static final int TABLE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LASTUSE = "last_use";

    private static final String SQL_CREATE =
        "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_LASTUSE + " INTEGER NOT NULL" +
            ");";

    public static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Die Tabelle wird mit dem SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }
}
