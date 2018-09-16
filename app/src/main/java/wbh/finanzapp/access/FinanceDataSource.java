package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.business.ProfileBean;

public class FinanceDataSource {

    private static final String LOG_TAG = FinanceDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private FinanceDBHelper dbHelper;

    private String[] columns = {
        FinanceDBHelper.COLUMN_ID,
        FinanceDBHelper.COLUMN_NAME,
        FinanceDBHelper.COLUMN_DESCRIPTION,
        FinanceDBHelper.COLUMN_LASTUSE
    };

    public FinanceDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new FinanceDBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DBHelpers geschlossen.");
    }

    public ProfileBean createProfileBean(String name, String description) {
        ContentValues values = new ContentValues();
        values.put(FinanceDBHelper.COLUMN_NAME, name);
        values.put(FinanceDBHelper.COLUMN_DESCRIPTION, description);
        values.put(FinanceDBHelper.COLUMN_LASTUSE, new Date().getTime()); // set the current date.

        long insertId = database.insert(FinanceDBHelper.TABLE_PROFILES, null, values);

        Cursor cursor = database.query(FinanceDBHelper.TABLE_PROFILES, columns, FinanceDBHelper.COLUMN_ID + "=" + insertId,
            null, null, null, null);

        cursor.moveToFirst();
        ProfileBean profile = cursorToProfileBean(cursor);
        cursor.close();

        return profile;
    }

    private ProfileBean cursorToProfileBean(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(FinanceDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(FinanceDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(FinanceDBHelper.COLUMN_DESCRIPTION);
        int idLastUse = cursor.getColumnIndex(FinanceDBHelper.COLUMN_LASTUSE);

        long id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        String desciption = cursor.getString(idDescription);
        long lastUse = cursor.getInt(idLastUse);

        ProfileBean profileBean = new ProfileBean(id, name, desciption, lastUse);

        return profileBean;
    }

    public List<ProfileBean> getAllProfileBeans() {
        List<ProfileBean> profileList = new ArrayList<>();

        Cursor cursor = database.query(FinanceDBHelper.TABLE_PROFILES, columns,
            null, null, null, null, null);

        cursor.moveToFirst();
        ProfileBean profile;

        while(!cursor.isAfterLast()) {
            profile = cursorToProfileBean(cursor);
            profileList.add(profile);
            Log.d(LOG_TAG, profile.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return profileList;
    }
}