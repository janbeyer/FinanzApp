package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wbh.finanzapp.business.GroupBean;

public class GroupsDataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    private static boolean BASICS_AVAILABLE_IN_DB = false;
    private static final Map<String, String> BASIC_GROUPS;
    static {
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("Haushalt", "Transaktionen die den Haushalt betreffen");
        tmpMap.put("Lebensmittel", "Transaktionen zur Lebensmittelvorsorge");
        tmpMap.put("Restaurant", "Restaurantbesuche");
        tmpMap.put("Verkehrsmittel", "Kosten für Pkw, Bahn oder ähnliches");
        tmpMap.put("Unterhaltung", "Unterhaltung wie z.B. Spiele");
        tmpMap.put("Persönlich", "Persönliche Transaktionen");
        tmpMap.put("Gesundheit", "Arztkosten, Medikamente o.ä.");
        tmpMap.put("Versicherung", "Versicherung für Haus, Pkw o.ä.");
        tmpMap.put("Wohnen", "Miete, Heizung, Strom u.ä.");
        tmpMap.put("Kleidung", "Mode und Accessoires");
        tmpMap.put("Bildung", "Kosten für Bildung wie z.B. Schule oder Studium");
        tmpMap.put("Urlaub", "Urlaubskosten");
        tmpMap.put("Freizeit", "Kosten die in der Frezeit entstehen");
        tmpMap.put("Gehalt", "Monatliches Grundeinkommen");
        BASIC_GROUPS = Collections.unmodifiableMap(tmpMap);
    };

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    private String[] columns = {
        GroupsDBHelper.COLUMN_ID,
        GroupsDBHelper.COLUMN_NAME,
        GroupsDBHelper.COLUMN_DESCRIPTION,
        GroupsDBHelper.COLUMN_WRITEABLE
    };

    public GroupsDataSource(Context context) {
        Log.d(LOG_TAG, "--> Now the data source createss the dbHelper.");
        dbHelper = new DBHelper(context);
        open();
    }

    public void open() {
        Log.d(LOG_TAG, "--> Start getting a reference of the db.");
        database = dbHelper.getWritableDatabase();
        if(!BASICS_AVAILABLE_IN_DB) insertBasics();
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

    public boolean existGroup(String name) {
        Cursor cursor = null;
        GroupBean group = null;
        try {
            cursor = database.query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_NAME + "=?",
                    new String[] { name },null, null, null);
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }

    public List<GroupBean> getAllGroups() {
        List<GroupBean> groupList = new ArrayList<>();

        Cursor cursor = database.query(GroupsDBHelper.TABLE_NAME, columns,
                null, null, null, null, GroupsDBHelper.COLUMN_NAME + " ASC");

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

    private void insertBasics() {
        BASIC_GROUPS.forEach((name, descr) -> {
            if(!existGroup(name)) insertGroup(name, descr, false);
        });
        BASICS_AVAILABLE_IN_DB = true;
    }

    public GroupBean insertGroup(String name, String description, Boolean writable) {
        ContentValues values = createGroupValues(null, name, description, writable);
        long insertId = database.insert(GroupsDBHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=" + insertId,
            null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToGroup(cursor);
        cursor.close();
        return group;
    }

    public GroupBean updateGroup(long id, String newName, String newDescription) {
        ContentValues values = createGroupValues(id, newName, newDescription, true);
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
        int idWritable = cursor.getColumnIndex(GroupsDBHelper.COLUMN_WRITEABLE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        Boolean writeable = (cursor.getInt(idWritable) == 1) ? true : false;

        return new GroupBean(id, name, description, writeable);
    }

    private ContentValues createGroupValues(Long id, String name, String description, Boolean writable) {
        ContentValues values = new ContentValues();
        if(id != null) values.put(GroupsDBHelper.COLUMN_ID, id.longValue());
        if(name != null) values.put(GroupsDBHelper.COLUMN_NAME, name);
        if(description != null) values.put(GroupsDBHelper.COLUMN_DESCRIPTION, description);
        if(writable != null) values.put(GroupsDBHelper.COLUMN_WRITEABLE, writable);
        return values;
    }
}