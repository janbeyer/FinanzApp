package wbh.finanzapp.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "financeApp.db";
    public static final Map<String, Integer> DB_VERSION_MODULES;
    static {
        Map<String, Integer> tmpMap = new HashMap<>();
        tmpMap.put(ProfilesHelper.TABLE_NAME, ProfilesHelper.TABLE_VERSION);
        DB_VERSION_MODULES = Collections.unmodifiableMap(tmpMap);
    }
    public static final int DB_VERSION = DB_VERSION_MODULES.values().stream().mapToInt(Number::intValue).sum();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ProfilesHelper.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we need to upgrade the db ...
        // Check the version of each module and upgrade the tables ...
    }
}