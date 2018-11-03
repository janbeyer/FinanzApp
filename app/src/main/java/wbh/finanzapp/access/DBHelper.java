package wbh.finanzapp.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class for SQLite. Encapsulate the raw database access.
 */
class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getSimpleName();

    /**
     * The database name.
     */
    private static final String DB_NAME = "financeApp.db";


    /**
     * The SQLite database instance.
     */
    private SQLiteDatabase database;

    /**
     * DB VERSION MODULES
     */
    private static final Map<String, Integer> DB_VERSION_MODULES;

    // Static initializer.
    static {
        Map<String, Integer> tmpMap = new HashMap<>();
        tmpMap.put(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.TABLE_VERSION);
        tmpMap.put(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.TABLE_VERSION);
        DB_VERSION_MODULES = Collections.unmodifiableMap(tmpMap);
    }

    /**
     * Create a unique database version this should be able to get every changes to the
     * database model.
     */
    private static final int DB_VERSION = DB_VERSION_MODULES.values().stream().mapToInt(Number::intValue).sum();

    /**
     * Create new DBHelper instance.
     *
     * @param context the application context.
     */
    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "--> Create DBHelper with db name: " + getDatabaseName());
        Log.d(LOG_TAG, "--> The SQLite db version is    : " + DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--> onCreate()");
        Log.d(LOG_TAG, "--> Try to create the SQLite database.");
        ProfilesDBHelper.createTable(db);
        GroupsDBHelper.createTable(db);
        // TODO: Insert Default Groups here ...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO If we need to upgrade the db ...
        // Check the version of each module and upgrade the tables ...
    }

    /**
     * Open the SQLite database.
     */
    void open() {
        Log.d(LOG_TAG, "--> Try to open the SQLite database.");

        // Create and/or open a database that will be used for reading and writing.
        // TODO handle SQLite exception
        database = getWritableDatabase();

        Log.d(LOG_TAG, "--> Open the db was successful.");
        Log.d(LOG_TAG, "--> Db Path: " + database.getPath());
    }

    /**
     * Close the SQLite database.
     */
    public void close() {
        super.close();
        Log.d(LOG_TAG, "--> Close the db with the help of the DBHelper.");
    }

    /**
     * @return the handle to the SQLite database.
     */
    SQLiteDatabase getDatabase() {
        return database;
    }
}