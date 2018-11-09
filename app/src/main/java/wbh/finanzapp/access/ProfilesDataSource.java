package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.ProfileBean;

/**
 * ProfilesDataSource class.
 */
public class ProfilesDataSource extends DataSource {

    private static final String LOG_TAG = ProfilesDataSource.class.getSimpleName();

    /**
     * The DBHelper instance.
     */
    private final DBHelper dbHelper;

    /**
     * Create a new ProfilesDataSource.
     *
     * @param context the application context.
     */
    public ProfilesDataSource(Context context) {
        Log.d(LOG_TAG, "--> Create ProfilesDataSource.");
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    /**
     * Return a new ProfileBean.
     */
    public AbstractBean getProfile(long id) {
        AbstractBean profile = null;
        Cursor cursor = ProfilesDBHelper.getProfileCursor(dbHelper, id);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            profile = cursorToBean(cursor);
        }
        return profile;
    }

    /**
     * Iterate over all database elements and store it to a List.
     */
    public List<AbstractBean> getProfiles() {
        List<AbstractBean> profileList = new ArrayList<>();
        Cursor cursor = ProfilesDBHelper.getProfilesCursor(dbHelper);
        cursor.moveToFirst();
        AbstractBean profile;
        while (!cursor.isAfterLast()) {
            profile = cursorToBean(cursor);
            profileList.add(profile);
            cursor.moveToNext();
        }
        cursor.close();
        return profileList;
    }
    
    @Override
    public AbstractBean insert(String name, String description) {
        ContentValues values = createValues(null, name, description);
        long insertId = dbHelper.getDatabase().insert(ProfilesDBHelper.TABLE_NAME, null, values);

        Cursor cursor = ProfilesDBHelper.getInsertCursor(dbHelper, insertId);
        cursor.moveToFirst();
        AbstractBean profile = cursorToBean(cursor);
        cursor.close();
        return profile;
    }
    
    @Override
    public AbstractBean update(long id, String newName, String newDescription) {
        ContentValues values = createValues(id, newName, newDescription);
        dbHelper.getDatabase().update(ProfilesDBHelper.TABLE_NAME, values, ProfilesDBHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = ProfilesDBHelper.getUpdateCursor(dbHelper, id);
        cursor.moveToFirst();
        AbstractBean profile = cursorToBean(cursor);
        cursor.close();
        return profile;
    }
    
    @Override
    public void delete(AbstractBean profile) {
        long id = profile.getId();
        dbHelper.getDatabase().delete(ProfilesDBHelper.TABLE_NAME, ProfilesDBHelper.COLUMN_ID + "=" + id, null);
    }
    
    @Override
    public AbstractBean cursorToBean(Cursor cursor) {
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
   
    @Override
    public ContentValues createValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(ProfilesDBHelper.COLUMN_ID, id);
        if (name != null) values.put(ProfilesDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(ProfilesDBHelper.COLUMN_DESCRIPTION, description);
        values.put(ProfilesDBHelper.COLUMN_LAST_USE, System.currentTimeMillis()); // set the current date.
        return values;
    }
}