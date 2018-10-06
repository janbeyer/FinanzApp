package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.GroupBean;

public class GroupsDataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] columns = {
        ProfilesDBHelper.COLUMN_ID,
        ProfilesDBHelper.COLUMN_NAME,
        ProfilesDBHelper.COLUMN_DESCRIPTION
    };

    public GroupsDataSource(Context context) {
        Log.d(LOG_TAG, "--> Now the data source createss the dbHelper.");
        dbHelper = new DBHelper(context);
        open();
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

    public GroupBean getGroup(long id) {
        Cursor cursor = null;
        GroupBean group = null;

        try {
            cursor = database.query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=?",
                new String[] { String.valueOf(id) },null, null, GroupsDBHelper.COLUMN_NAME + " ASC");
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                group = cursorToGroup(cursor);
            }
            return group;
        } finally {
            cursor.close();
        }
    }

    public List<GroupBean> getAllGroups() {
        List<GroupBean> groupList = new ArrayList<>();

        Cursor cursor = database.query(GroupsDBHelper.TABLE_NAME, columns,
                null, null, null, null, null);

        cursor.moveToFirst();
        GroupBean group;

        while(!cursor.isAfterLast()) {
            group = cursorToGroup(cursor);
            groupList.add(group);
            cursor.moveToNext();
        }

        cursor.close();

        return groupList;
    }

    public GroupBean insertGroup(String name, String description) {
        ContentValues values = createGroupValues(null, name, description);
        long insertId = database.insert(GroupsDBHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=" + insertId,
            null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToGroup(cursor);
        cursor.close();
        return group;
    }

    public GroupBean updateGroup(long id, String newName, String newDescription) {
        ContentValues values = createGroupValues(id, newName, newDescription);
        database.update(GroupsDBHelper.TABLE_NAME, values, GroupsDBHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = database.query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=" + id,
            null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToGroup(cursor);
        cursor.close();
        return group;
    }

    public void deleteGroup(GroupBean group) {
        long id = group.getId();
        database.delete(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMN_ID + "=" + id, null);
    }

    private GroupBean cursorToGroup(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(GroupsDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(GroupsDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(GroupsDBHelper.COLUMN_DESCRIPTION);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);

        return new GroupBean(id, name, description);
    }

    private ContentValues createGroupValues(Long id, String name, String description) {
        ContentValues values = new ContentValues();
        if(id != null) values.put(GroupsDBHelper.COLUMN_ID, id.longValue());
        if(name != null) values.put(GroupsDBHelper.COLUMN_NAME, name);
        if(description != null) values.put(GroupsDBHelper.COLUMN_DESCRIPTION, description);
        return values;
    }
}