package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.ProfileBean;

public class ProfilesDataSource {

    private static final String LOG_TAG = ProfilesDataSource.class.getSimpleName();

    private final DBHelper dbHelper;

    private final String[] columns = {
            ProfilesDBHelper.COLUMN_ID,
            ProfilesDBHelper.COLUMN_NAME,
            ProfilesDBHelper.COLUMN_DESCRIPTION,
            ProfilesDBHelper.COLUMN_LAST_USE
    };

    public ProfilesDataSource(Context context) {
        Log.d(LOG_TAG, "--> Create ProfilesDataSource.");
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    public ProfileBean getProfile(long id) {
        ProfileBean profile = null;

        try (Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, columns, ProfilesDBHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                profile = cursorToProfile(cursor);
            }
            return profile;
        }
    }

    public List<ProfileBean> getAllProfiles() {
        List<ProfileBean> profileList = new ArrayList<>();

        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, columns,
                null, null, null, null, ProfilesDBHelper.COLUMN_LAST_USE + " DESC");

        cursor.moveToFirst();
        ProfileBean profile;

        while (!cursor.isAfterLast()) {
            profile = cursorToProfile(cursor);
            profileList.add(profile);
            cursor.moveToNext();
        }

        cursor.close();

        return profileList;
    }

    public ProfileBean insertProfile(String name, String description) {
        ContentValues values = createProfileValues(null, name, description);
        long insertId = dbHelper.getDatabase().insert(ProfilesDBHelper.TABLE_NAME, null, values);
        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, columns, ProfilesDBHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public ProfileBean updateProfile(long id, String newName, String newDescription) {
        ContentValues values = createProfileValues(id, newName, newDescription);
        dbHelper.getDatabase().update(ProfilesDBHelper.TABLE_NAME, values, ProfilesDBHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, columns, ProfilesDBHelper.COLUMN_ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public void deleteProfile(ProfileBean profile) {
        long id = profile.getId();
        dbHelper.getDatabase().delete(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMN_ID + "=" + id, null);
    }

    private ProfileBean cursorToProfile(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_DESCRIPTION);
        int idLastUse = cursor.getColumnIndex(ProfilesDBHelper.COLUMN_LAST_USE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        long lastUse = cursor.getLong(idLastUse);

        return new ProfileBean(id, name, description, lastUse);
    }

    private ContentValues createProfileValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(ProfilesDBHelper.COLUMN_ID, id);
        if (name != null) values.put(ProfilesDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(ProfilesDBHelper.COLUMN_DESCRIPTION, description);
        values.put(ProfilesDBHelper.COLUMN_LAST_USE, System.currentTimeMillis()); // set the current date.
        return values;
    }
}