package wbh.finanzapp.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FinanceDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = FinanceDBHelper.class.getSimpleName();

    public static final String DB_NAME = "financeApp.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PROFILES ="profiles";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LASTUSE = "last_use";

    public static final String SQL_CREATE =
        "CREATE TABLE " + TABLE_PROFILES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_LASTUSE + " INTEGER NOT NULL" +
        ");";

    public FinanceDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit dem SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}