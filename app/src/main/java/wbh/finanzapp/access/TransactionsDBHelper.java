package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Helper class for SQL statements for Transaction table.
 */
class TransactionsDBHelper {

    private static final String LOG_TAG = TransactionsDBHelper.class.getSimpleName();

    static final String TABLE_NAME = "transactions";
    static final int TABLE_VERSION = 1;

    static final String COLUMN_ID = "_id";
    static final String COLUMN_PROFILE_ID = "_profileId";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_AMOUNT = "amount";
    // 1=expenditure;2=revenue
    static final String COLUMN_EXPENDITURE = "expenditure";
    // 1=unique;2=daily;3=weekly;4=monthly;5=yearly
    static final String COLUMN_STATE = "state";
    static final String COLUMN_UNIQUE_DATE = "uniqueDate";
    // 1=monday;2=tuesday;...;7=sunday
    static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    static final String COLUMN_MONTHLY_DAY = "monthlyDay";
    static final String COLUMN_YEARLY_MONTH = "yearlyMonth";
    static final String COLUMN_YEARLY_DAY = "yearlyDay";

    /**
     * The table columns for the Transaction table.
     */
    static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_DESCRIPTION,
            COLUMN_PROFILE_ID,
            COLUMN_AMOUNT,
            COLUMN_EXPENDITURE,
            COLUMN_STATE,
            COLUMN_UNIQUE_DATE,
            COLUMN_DAY_OF_WEEK,
            COLUMN_MONTHLY_DAY,
            COLUMN_YEARLY_MONTH,
            COLUMN_YEARLY_DAY
    };

    /**
     * Transactions table SQL Create statement.
     */
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_ID + " INTEGER NOT NULL, " +
                    COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                    COLUMN_EXPENDITURE + " INTEGER NOT NULL, " +
                    COLUMN_STATE + " INTEGER NOT NULL, " +
                    COLUMN_UNIQUE_DATE + " INTEGER, " +
                    COLUMN_DAY_OF_WEEK + " INTEGER, " +
                    COLUMN_MONTHLY_DAY + " INTEGER, " +
                    COLUMN_YEARLY_MONTH + " INTEGER, " +
                    COLUMN_YEARLY_DAY + " INTEGER, " +
                    "CONSTRAINT fk_profile FOREIGN KEY (" + COLUMN_PROFILE_ID + ") REFERENCES " + ProfilesDBHelper.TABLE_NAME + " (" + ProfilesDBHelper.COLUMN_ID + ") ON DELETE CASCADE" +
                    ");";

    /**
     * Create the Transaction Table.
     */
    static void createTable(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "--> Create the transaction table with the statement: " + SQL_CREATE);
            db.execSQL(SQL_CREATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "--> Error by creating the transaction table: " + e.getMessage());
        }
    }
}
