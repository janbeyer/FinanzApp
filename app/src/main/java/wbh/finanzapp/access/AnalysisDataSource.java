package wbh.finanzapp.access;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.List;

import wbh.finanzapp.business.AbstractBean;

/**
 * GroupsDataSource class.
 */
public class AnalysisDataSource extends AbstractDataSource {

    private static final String LOG_TAG = GroupsDataSource.class.getSimpleName();

    /**
     * The DBHelper instance.
     */
    private DBHelper dbHelper;

    /**
     * Create a new GroupsDataSource.
     *
     * @param context the application context.
     */
    public AnalysisDataSource(Context context) {
        Log.d(LOG_TAG, "--> Create AnalysisDataSource.");
        dbHelper = new DBHelper(context);
        dbHelper.open();
    }

    @Override
    public AbstractBean getBean(long id) {
        return null;
    }

    @Override
    public List<AbstractBean> getBeans() {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public AbstractBean cursorToBean(Cursor cursor) {
        return null;
    }
}
