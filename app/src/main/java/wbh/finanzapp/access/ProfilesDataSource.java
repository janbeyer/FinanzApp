package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.ProfileBean;

public class ProfilesDataSource {

    private static final String LOG_TAG = ProfilesDataSource.class.getSimpleName();

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] columns = {
        ProfilesDBHelper.COLUMN_ID,
        ProfilesDBHelper.COLUMN_NAME,
        ProfilesDBHelper.COLUMN_DESCRIPTION,
        ProfilesDBHelper.COLUMN_LASTUSE,
        ProfilesDBHelper.COLUMN_STARTVALUE
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

        Cursor cursor = database.query(ProfilesDBHelper.TABLE_NAME, columns,
                null, null, null, null, ProfilesDBHelper.COLUMN_LASTUSE + " DESC");

        cursor.moveToFirst();
        ProfileBean profile;

        while(!cursor.isAfterLast()) {
            profile = cursorToProfile(cursor);
            profileList.add(profile);
            cursor.moveToNext();
        }

        cursor.close();

        return profileList;
    }

    public ProfileBean insertProfile(String name, String description) {
        ContentValues values = createProfileValues(null, name, description, null);
        long insertId = database.insert(ProfilesDBHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(ProfilesDBHelper.TABLE_NAME, columns, ProfilesDBHelper.COLUMN_ID + "=" + insertId,
            null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public ProfileBean updateProfile(long id, String newName, String newDescription, Integer newStartValue) {
        ContentValues values = createProfileValues(id, newName, newDescription, newStartValue);
        database.update(ProfilesDBHelper.TABLE_NAME, values, ProfilesDBHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = database.query(ProfilesDBHelper.TABLE_NAME, columns, ProfilesDBHelper.COLUMN_ID + "=" + id,
            null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public void deleteProfile(ProfileBean profile) {
        long id = profile.getId();
        database.delete(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMN_ID + "=" + id, null);
    }

    private ProfileBean cursorToProfile(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_DESCRIPTION);
        int idLastUse = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_LASTUSE);
        int idStartValue = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_STARTVALUE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        long lastUse = cursor.getLong(idLastUse);
        int startValue = cursor.getInt(idStartValue);

        return new ProfileBean(id, name, description, lastUse, startValue);
    }

    private ContentValues createProfileValues(Long id, String name, String description, Integer startValue) {
        ContentValues values = new ContentValues();
        if(id != null) values.put(ProfilesDBHelper.COLUMN_ID, id.longValue());
        if(name != null) values.put(ProfilesDBHelper.COLUMN_NAME, name);
        if(description != null) values.put(ProfilesDBHelper.COLUMN_DESCRIPTION, description);
        values.put(ProfilesDBHelper.COLUMN_LASTUSE, System.currentTimeMillis()); // set the current date.
        if(startValue != null) values.put(ProfilesDBHelper.COLUMN_STARTVALUE, startValue.intValue());
        return values;
    }
}