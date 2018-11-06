package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wbh.finanzapp.business.GroupBean;

public class GroupsDataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    private static final Map<String, String> BASIC_GROUPS;

    static {
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("Haushalt", "Transaktionen die den Haushalt betreffen");
        tmpMap.put("Lebensmittel", "Transaktionen zur Lebensmittelvorsorge");
        tmpMap.put("Restaurant", "Restaurantbesuche");
        tmpMap.put("Verkehrsmittel", "Kosten für Pkw, Bahn o.ä.");
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
    }

    private DBHelper dbHelper;

    private final String[] columns = {
            GroupsDBHelper.COLUMN_ID,
            GroupsDBHelper.COLUMN_PROFILE_ID,
            GroupsDBHelper.COLUMN_NAME,
            GroupsDBHelper.COLUMN_DESCRIPTION
    };

    public GroupsDataSource(Context context) {
        Log.d(LOG_TAG, "--> Create GroupsDataSource.");
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }


    public List<GroupBean> getProfileGroups(long profileId) {
        List<GroupBean> groupList = new ArrayList<>();

        Cursor cursor = dbHelper.getDatabase().query(
                GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_PROFILE_ID + "=?",
                new String[]{String.valueOf(profileId)}, null, null, GroupsDBHelper.COLUMN_NAME + " ASC");

        cursor.moveToFirst();
        GroupBean group;

        while (!cursor.isAfterLast()) {
            group = cursorToGroup(cursor);
            groupList.add(group);
            cursor.moveToNext();
        }

        cursor.close();

        return groupList;
    }

    public void insertBasics(long profileId) {
        BASIC_GROUPS.forEach((name, descr) -> insertGroup(profileId, name, descr));
    }

    public GroupBean insertGroup(Long profileId, String name, String description) {
        ContentValues values = createGroupValues(null, profileId, name, description);
        long insertId = dbHelper.getDatabase().insert(GroupsDBHelper.TABLE_NAME, null, values);
        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=" + insertId,null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToGroup(cursor);
        cursor.close();
        return group;
    }

    public GroupBean updateGroup(long id, long profileId, String newName, String newDescription) {
        ContentValues values = createGroupValues(id, profileId, newName, newDescription);
        dbHelper.getDatabase().update(GroupsDBHelper.TABLE_NAME, values, GroupsDBHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = dbHelper.getDatabase().query(GroupsDBHelper.TABLE_NAME, columns, GroupsDBHelper.COLUMN_ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        GroupBean group = cursorToGroup(cursor);
        cursor.close();
        return group;
    }

    public void deleteGroup(long id) {
        dbHelper.getDatabase().delete(GroupsDBHelper.TABLE_NAME, GroupsDBHelper.COLUMN_ID + "=" + id, null);
    }

    private GroupBean cursorToGroup(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(GroupsDBHelper.COLUMN_ID);
        int idProfile = cursor.getColumnIndex(GroupsDBHelper.COLUMN_PROFILE_ID);
        int idName = cursor.getColumnIndex(GroupsDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(GroupsDBHelper.COLUMN_DESCRIPTION);

        long id = cursor.getLong(idIndex);
        long profileId = cursor.getLong(idProfile);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);

        return new GroupBean(id, profileId, name, description);
    }

    private ContentValues createGroupValues(Long id, Long profileId, String name, String description) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(GroupsDBHelper.COLUMN_ID, id);
        if (profileId != null) values.put(GroupsDBHelper.COLUMN_PROFILE_ID, profileId);
        if (name != null) values.put(GroupsDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(GroupsDBHelper.COLUMN_DESCRIPTION, description);
        return values;
    }
}