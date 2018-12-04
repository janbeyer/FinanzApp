package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.ProfileBean;

public class ProfilesDataSource extends AbstractDataSource {

    private static final String LOG_TAG = ProfilesDataSource.class.getSimpleName();

    private final DBHelper dbHelper;

    public ProfilesDataSource(Context context) {
        Log.d(LOG_TAG, "--> Create ProfilesDataSource.");
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    /**
     * Close the ProfilesDataSource.
     */
    public void close() {
        dbHelper.close();
    }

    @Override
    public List<AbstractBean> getBeans() {
        List<AbstractBean> profileList = new ArrayList<>();
        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMNS,
                null, null, null, null, ProfilesDBHelper.COLUMN_LAST_USE + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProfileBean profile = cursorToBean(cursor);
            profileList.add(profile);
            cursor.moveToNext();
        }
        cursor.close();
        return profileList;
    }

    public ProfileBean insert(String name, String description) {
        ContentValues values = createValues(null, name, description);
        long insertId = dbHelper.getDatabase().insert(ProfilesDBHelper.TABLE_NAME, null, values);

        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMNS,
                ProfilesDBHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToBean(cursor);
        cursor.close();
        return profile;
    }

    public ProfileBean update(long id, String newName, String newDescription) {
        ContentValues values = createValues(id, newName, newDescription);
        dbHelper.getDatabase().update(ProfilesDBHelper.TABLE_NAME, values, ProfilesDBHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = dbHelper.getDatabase().query(ProfilesDBHelper.TABLE_NAME,  ProfilesDBHelper.COLUMNS,
                ProfilesDBHelper.COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        ProfileBean profile = cursorToBean(cursor);
        cursor.close();
        return profile;
    }

    @Override
    public void delete(long profileId) {
        dbHelper.getDatabase().delete(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMN_ID + "=" + profileId, null);
    }

    @Override
    public ProfileBean cursorToBean(Cursor cursor) {
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

    private ContentValues createValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(ProfilesDBHelper.COLUMN_ID, id);
        if (name != null) values.put(ProfilesDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(ProfilesDBHelper.COLUMN_DESCRIPTION, description);
        values.put(ProfilesDBHelper.COLUMN_LAST_USE, System.currentTimeMillis()); // set the current date.
        return values;
    }
}