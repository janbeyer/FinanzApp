package wbh.finanzapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import wbh.finanzapp.business.AbstractBean;

public abstract class AbstractActivity extends AppCompatActivity {

    /**
     * Show all list entries.
     */
    public ListView showAllListEntries(List<AbstractBean> list, int resource, int id){
        ArrayAdapter<AbstractBean> arrayAdapter = new ArrayAdapter<>(this, resource, list);
        ListView listView = findViewById(id);
        listView.setAdapter(arrayAdapter);
        return listView;
    }
}
