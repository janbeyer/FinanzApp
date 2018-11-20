package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.GroupBean;

public class GroupsDataSource extends AbstractDataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    private DBHelper dbHelper;

    private long profileId;

    public GroupsDataSource(Context context, long profileId) {
        Log.d(LOG_TAG, "--> Create GroupsDataSource.");
        this.profileId = profileId;
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    public GroupBean getBean(long id) {
        GroupBean group = null;
        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMNS,
                GroupsDBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            group = cursorToBean(cursor);
        }
        return group;
    }

    @Override
    public List<AbstractBean> getBeans() {
        List<AbstractBean> groupList = new ArrayList<>();
        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMNS,
                GroupsDBHelper.COLUMN_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)},
                null, null, GroupsDBHelper.COLUMN_NAME + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroupBean group = cursorToBean(cursor);
            groupList.add(group);
            cursor.moveToNext();
        }
        cursor.close();
        return groupList;
    }

    public GroupBean insert(String name, String description) {
        ContentValues values = createValues(null, name, description);
        long insertId = dbHelper.getDatabase().insert(GroupsDBHelper.TABLE_NAME, null, values);

        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMNS,
                GroupsDBHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToBean(cursor);
        cursor.close();
        return group;
    }

    public GroupBean update(long id, String newName, String newDescription) {
        ContentValues values = createValues(id, newName, newDescription);
        dbHelper.getDatabase().update(GroupsDBHelper.TABLE_NAME, values, GroupsDBHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMNS,
                GroupsDBHelper.COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToBean(cursor);
        cursor.close();
        return group;
    }

    @Override
    public void delete(long groupId) {
        dbHelper.getDatabase().delete(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMN_ID + "=" + groupId, null);
    }
    
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

    public ContentValues createValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(GroupsDBHelper.COLUMN_ID, id);
        values.put(GroupsDBHelper.COLUMN_PROFILE_ID, profileId);
        if (name != null) values.put(GroupsDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(GroupsDBHelper.COLUMN_DESCRIPTION, description);
        return values;
    }

}