package wbh.finanzapp.access;

import android.database.sqlite.SQLiteDatabase;

/**
 * Interface for DBHelper class.
 */
public interface AbstractDbHelper {

    /**
     * Open the SQLite database.
     */
    void open();

    /**
     * Close the SQLite database.
     */
    void close();

    /**
     * @return the handle to the SQLite database.
     */
    SQLiteDatabase getDatabase();
}
