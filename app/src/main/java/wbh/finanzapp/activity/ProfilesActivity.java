package wbh.finanzapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.FinanceDataSource;
import wbh.finanzapp.business.ProfileBean;

public class ProfilesActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private FinanceDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        dataSource = new FinanceDataSource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        ProfileBean profileBean = dataSource.createProfileBean("testName1", "testDescr1");
        Log.d(LOG_TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(LOG_TAG, profileBean.toString());

        Log.d(LOG_TAG, "Es sind folgende Einträge in der Datenbank vorhanden:");
        showAllListEntries();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void showAllListEntries() {
        List<ProfileBean> profiles = dataSource.getAllProfileBeans();

        ArrayAdapter<ProfileBean> profileArrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_single_choice,
            profiles);

        ListView profilesListView = (ListView) findViewById(R.id.listview_profiles);
        profilesListView.setAdapter(profileArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
