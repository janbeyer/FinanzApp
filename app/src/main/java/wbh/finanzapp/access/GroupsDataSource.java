package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.GroupBean;

/**
 * GroupsDataSource class.
 */
public class GroupsDataSource extends DataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    /**
     * The DBHelper instance.
     */
    private DBHelper dbHelper;

    /**
     * The profile id to identify the Profile.
     */
    private long profileId;

    /**
     * Create a new GroupsDataSource.
     *
     * @param context the application context.
     */
    public GroupsDataSource(Context context, long profileId) {
        Log.d(LOG_TAG, "--> Create GroupsDataSource.");
        this.profileId = profileId;
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    /**
     * Iterate over all database elements and store it to a List.
     */
    public List<GroupBean> getProfileGroups() {
        List<GroupBean> groupList = new ArrayList<>();
        Cursor cursor = GroupsDBHelper.getGroupsCursor(dbHelper, profileId);
        cursor.moveToFirst();
        GroupBean group;
        while (!cursor.isAfterLast()) {
            group = cursorToBean(cursor);
            groupList.add(group);
            cursor.moveToNext();
        }
        cursor.close();
        return groupList;
    }

    @Override
    public AbstractBean insert(String name, String description) {
        ContentValues values = createValues(null, name, description);
        long insertId = dbHelper.getDatabase().insert(GroupsDBHelper.TABLE_NAME, null, values);

        Cursor cursor = GroupsDBHelper.getInsertCursor(dbHelper, insertId);
        cursor.moveToFirst();
        GroupBean group = cursorToBean(cursor);
        cursor.close();
        return group;
    }

    @Override
    public AbstractBean update(long id, String newName, String newDescription) {
        ContentValues values = createValues(id, newName, newDescription);
        dbHelper.getDatabase().update(GroupsDBHelper.TABLE_NAME, values, GroupsDBHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = GroupsDBHelper.getUpdateCursor(dbHelper, id);
        cursor.moveToFirst();
        GroupBean group = cursorToBean(cursor);
        cursor.close();
        return group;
    }

    @Override
    public void delete(AbstractBean profile) {
        long id = profile.getId();
        dbHelper.getDatabase().delete(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMN_ID + "=" + id, null);
    }

    /**
     * Move the cursor to Group.
     */
    @Override
    public GroupBean cursorToBean(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(GroupsDBHelper.COLUMN_ID);
        int idProfile = cursor.getColumnIndex(GroupsDBHelper.COLUMN_PROFILE_ID);
        int idName = cursor.getColumnIndex(GroupsDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(GroupsDBHelper.COLUMN_DESCRIPTION);

        long id = cursor.getLong(idIndex);
        long profileId = cursor.getLong(idProfile);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);

        return new GroupBean(id, name, description, profileId);
    }

    @Override
    public ContentValues createValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(GroupsDBHelper.COLUMN_ID, id);
        if (profileId != 0) values.put(GroupsDBHelper.COLUMN_PROFILE_ID, profileId);
        if (name != null) values.put(GroupsDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(GroupsDBHelper.COLUMN_DESCRIPTION, description);
        return values;
    }

}