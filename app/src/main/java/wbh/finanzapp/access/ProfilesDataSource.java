package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.business.ProfileBean;

public class ProfilesDataSource {

    private static final String LOG_TAG = ProfilesDataSource.class.getSimpleName();

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] columns = {
        ProfilesHelper.COLUMN_ID,
        ProfilesHelper.COLUMN_NAME,
        ProfilesHelper.COLUMN_DESCRIPTION,
        ProfilesHelper.COLUMN_LASTUSE,
        ProfilesHelper.COLUMN_STARTVALUE
    };

    public ProfilesDataSource(Context context) {
        Log.d(LOG_TAG, "--> Now the data source createss the dbHelper.");
        dbHelper = new DBHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "--> Start getting a reference of the db.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--> Finish getting the db reference. Path of the db: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "--> Close the db with the help of the DBHelper.");
    }

    public List<ProfileBean> getAllProfiles() {
        List<ProfileBean> profileList = new ArrayList<>();

        Cursor cursor = database.query(ProfilesHelper.TABLE_NAME, columns,
                null, null, null, null, ProfilesHelper.COLUMN_LASTUSE + " DESC");

        cursor.moveToFirst();
        ProfileBean profile;

        while(!cursor.isAfterLast()) {
            profile = cursorToProfile(cursor);
            profileList.add(profile);
            Log.d(LOG_TAG, "--> " + profile.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return profileList;
    }

    public ProfileBean insertProfile(String name, String description) {
        ContentValues values = createProfileValues(null, name, description, null);
        long insertId = database.insert(ProfilesHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(ProfilesHelper.TABLE_NAME, columns, ProfilesHelper.COLUMN_ID + "=" + insertId,
            null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        Log.d(LOG_TAG, "--> Add new entry! Profile: " + profile.toString());
        return profile;
    }

    public ProfileBean updateProfile(long id, String newName, String newDescription, Integer newStartValue) {
        ContentValues values = createProfileValues(id, newName, newDescription, newStartValue);
        database.update(ProfilesHelper.TABLE_NAME, values, ProfilesHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = database.query(ProfilesHelper.TABLE_NAME, columns, ProfilesHelper.COLUMN_ID + "=" + id,
            null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        Log.d(LOG_TAG, "--> Update entry! Profile: " + profile.toString());
        return profile;
    }

    public void deleteProfile(ProfileBean profile) {
        long id = profile.getId();
        database.delete(ProfilesHelper.TABLE_NAME, ProfilesHelper.COLUMN_ID + "=" + id, null);
        Log.d(LOG_TAG, "--> Delete entry! Profile: " + profile.toString());
    }

    private ProfileBean cursorToProfile(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ProfilesHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(ProfilesHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(ProfilesHelper.COLUMN_DESCRIPTION);
        int idLastUse = cursor.getColumnIndex(ProfilesHelper.COLUMN_LASTUSE);
        int idStartValue = cursor.getColumnIndex(ProfilesHelper.COLUMN_STARTVALUE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String desciption = cursor.getString(idDescription);
        long lastUse = cursor.getLong(idLastUse);
        int startValue = cursor.getInt(idStartValue);

        return new ProfileBean(id, name, desciption, lastUse, startValue);
    }

    private ContentValues createProfileValues(Long id, String name, String description, Integer startValue) {
        ContentValues values = new ContentValues();
        if(id != null) values.put(ProfilesHelper.COLUMN_ID, id.longValue());
        if(name != null) values.put(ProfilesHelper.COLUMN_NAME, name);
        if(description != null) values.put(ProfilesHelper.COLUMN_DESCRIPTION, description);
        values.put(ProfilesHelper.COLUMN_LASTUSE, System.currentTimeMillis()); // set the current date.
        if(startValue != null) values.put(ProfilesHelper.COLUMN_STARTVALUE, startValue.intValue());
        return values;
    }
}