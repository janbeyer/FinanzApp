package wbh.finanzapp.access;

import android.database.Cursor;

import java.util.List;

import wbh.finanzapp.business.AbstractBean;

/**
 * Parent class for all data source classes
 */
public abstract class AbstractDataSource {

    /**
     * Return a list with AbstractBean.
     */
    public abstract List<AbstractBean> getBeans();

    /**
     * Delete a Bean from database.
     */
    public abstract void delete(long id);

    /**
     * Get the Bean to the given database Cursor.
     */
    public abstract AbstractBean cursorToBean(Cursor cursor);

}
