package wbh.finanzapp.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.TransactionBean;

/**
 * GroupsDataSource class.
 */
public class TransactionsDataSource extends AbstractDataSource {

    private static final String LOG_TAG = TransactionsDataSource.class.getSimpleName();

    /**
     * The DBHelper instance.
     */
    private DBHelper dbHelper;

    /**
     * The profile id to identify the Profile.
     */
    private long profileId;

    /**
     * Create a new TransactionDataSource.
     *
     * @param context the application context.
     */
    public TransactionsDataSource(Context context, long profileId) {
        Log.d(LOG_TAG, "--> Create GroupsDataSource.");
        this.profileId = profileId;
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    /**
     * Returns a TransactionBean.
     */
    public TransactionBean getBean(long id) {
        TransactionBean transaction = null;
        Cursor cursor = dbHelper.getDatabase().query(TransactionsDBHelper.TABLE_NAME, TransactionsDBHelper.COLUMNS,
                TransactionsDBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            transaction = cursorToBean(cursor);
        }
        return transaction;
    }

    /**
     * Iterate over all database elements and store it to a List.
     */
    @Override
    public List<AbstractBean> getBeans() {
        List<AbstractBean> transactionList = new ArrayList<>();
        Cursor cursor = dbHelper.getDatabase().query(TransactionsDBHelper.TABLE_NAME, TransactionsDBHelper.COLUMNS,
                TransactionsDBHelper.COLUMN_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)},
                null, null, TransactionsDBHelper.COLUMN_NAME + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TransactionBean transaction = cursorToBean(cursor);
            transactionList.add(transaction);
            cursor.moveToNext();
        }
        cursor.close();
        return transactionList;
    }

    public TransactionBean insert(String name, String description, long amount, boolean expenditure, int state, Long uniqueDate, Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        ContentValues values = createValues(null, name, description, profileId, amount, expenditure, state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);
        long insertId = dbHelper.getDatabase().insert(TransactionsDBHelper.TABLE_NAME, null, values);

        Cursor cursor = dbHelper.getDatabase().query(TransactionsDBHelper.TABLE_NAME, TransactionsDBHelper.COLUMNS,
                TransactionsDBHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        TransactionBean transaction = cursorToBean(cursor);
        cursor.close();
        return transaction;
    }

    public TransactionBean update(long id, String newName, String newDescription, long newAmount, boolean newExpenditure, int newState, Long newUniqueDate, Integer newDayOfWeek, Integer newMonthlyDay, Integer newYearlyMonth, Integer newYearlyDay) {
        ContentValues values = createValues(id, newName, newDescription, profileId, newAmount, newExpenditure, newState, newUniqueDate, newDayOfWeek, newMonthlyDay, newYearlyMonth, newYearlyDay);
        dbHelper.getDatabase().update(TransactionsDBHelper.TABLE_NAME, values, TransactionsDBHelper.COLUMN_ID + "=" + id, null);

        Cursor cursor = dbHelper.getDatabase().query(TransactionsDBHelper.TABLE_NAME, TransactionsDBHelper.COLUMNS,
                TransactionsDBHelper.COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        TransactionBean transaction = cursorToBean(cursor);
        cursor.close();
        return transaction;
    }

    @Override
    public void delete(long transactionId) {
        dbHelper.getDatabase().delete(TransactionsDBHelper.TABLE_NAME, TransactionsDBHelper.COLUMN_ID + "=" + transactionId, null);
    }
    
    @Override
    public TransactionBean cursorToBean(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_NAME);
        int idDescription = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_DESCRIPTION);
        int idProfile = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_PROFILE_ID);
        int idAmount = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_AMOUNT);
        int idExpenditure = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_EXPENDITURE);
        int idState = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_STATE);
        int idUniqueDate = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_UNIQUE_DATE);
        int idDayOfWeek = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_DAY_OF_WEEK);
        int idMonthlyDay = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_MONTHLY_DAY);
        int idYearlyMonth = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_YEARLY_MONTH);
        int idYearlyDay = cursor.getColumnIndex(TransactionsDBHelper.COLUMN_YEARLY_DAY);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        long profileId = cursor.getLong(idProfile);
        long amount = cursor.getLong(idAmount);
        boolean expenditure = cursor.getInt(idExpenditure) == 1 ? true : false;
        int state = cursor.getInt(idState);
        Long uniqueDate = cursor.getLong(idUniqueDate);
        Integer dayOfWeek = cursor.getInt(idDayOfWeek);
        Integer monthlyDay = cursor.getInt(idMonthlyDay);
        Integer yearlyMonth = cursor.getInt(idYearlyMonth);
        Integer yearlyDay = cursor.getInt(idYearlyDay);

        return new TransactionBean(id, name, description, profileId, amount, expenditure, state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);
    }

    public ContentValues createValues(Long id, String name, String description, long profileId, long amount, boolean expenditure, int state, Long uniqueDate, Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        ContentValues values = new ContentValues();
        if (id != null) values.put(TransactionsDBHelper.COLUMN_ID, id);
        if (name != null) values.put(TransactionsDBHelper.COLUMN_NAME, name);
        if (description != null) values.put(TransactionsDBHelper.COLUMN_DESCRIPTION, description);
        values.put(TransactionsDBHelper.COLUMN_PROFILE_ID, profileId);
        values.put(TransactionsDBHelper.COLUMN_AMOUNT, amount);
        values.put(TransactionsDBHelper.COLUMN_EXPENDITURE, expenditure);
        values.put(TransactionsDBHelper.COLUMN_STATE, state);
        if (uniqueDate != null) values.put(TransactionsDBHelper.COLUMN_UNIQUE_DATE, uniqueDate);
        if (dayOfWeek != null) values.put(TransactionsDBHelper.COLUMN_DAY_OF_WEEK, dayOfWeek);
        if (monthlyDay != null) values.put(TransactionsDBHelper.COLUMN_MONTHLY_DAY, monthlyDay);
        if (yearlyMonth != null) values.put(TransactionsDBHelper.COLUMN_YEARLY_MONTH, yearlyMonth);
        if (yearlyDay != null) values.put(TransactionsDBHelper.COLUMN_YEARLY_DAY, yearlyDay);
        return values;
    }

}