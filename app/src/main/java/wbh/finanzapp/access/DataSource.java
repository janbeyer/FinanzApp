package wbh.finanzapp.access;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import wbh.finanzapp.business.AbstractBean;

/**
 * Parent class for all data source classes
 */
public abstract class DataSource {

    /**
     * Insert a Bean to the database.
     */
    public abstract AbstractBean insert(String name, String description);

    /**
     * Update a database Bean.
     */
    public abstract AbstractBean update(long id, String newName, String newDescription);

    /**
     * Delete a Bean from database.
     */
    public abstract void delete(AbstractBean profile);

    /**
     * Get the Bean to the given database Cursor.
     */
    public abstract AbstractBean cursorToBean(Cursor cursor);

    /**
     * Create Value from the given parameter.
     */
    public abstract ContentValues createValues(Long id, String name, String description);

    /**
     * Return a list with AbstractBean.
     */
    public abstract List<AbstractBean> getDataSources();

}
