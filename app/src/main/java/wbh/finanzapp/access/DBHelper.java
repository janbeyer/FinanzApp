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

    private static final String DB_NAME = "financeApp.db";

    private static final Map<String, Integer> DB_VERSION_MODULES;
    static {
        Map<String, Integer> tmpMap = new HashMap<>();
        tmpMap.put(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.TABLE_VERSION);
        tmpMap.put(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.TABLE_VERSION);
        DB_VERSION_MODULES = Collections.unmodifiableMap(tmpMap);
    }

    private static final int DB_VERSION = DB_VERSION_MODULES.values().stream().mapToInt(Number::intValue).sum();

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "--> DBHelper creates the db: " + getDatabaseName());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ProfilesDBHelper.createTable(db);
        GroupsDBHelper.createTable(db);
        // TODO: Insert Default Groups here ...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we need to upgrade the db ...
        // Check the version of each module and upgrade the tables ...
    }
}