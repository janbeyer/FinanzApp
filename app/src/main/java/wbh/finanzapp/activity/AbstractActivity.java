package wbh.finanzapp.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.business.AbstractBean;

public abstract class AbstractActivity extends AppCompatActivity {

    /**
     * Returns the text to show in the online help.
     */
    protected abstract int getHelpText();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_option_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            ViewGroup viewHelp = findViewById(R.id.dialog_help);
            View dialogsView = inflater.inflate(R.layout.dialog_help, viewHelp);

            final TextView helpText = dialogsView.findViewById(R.id.dialog_help_text);
            helpText.setText(getHelpText());

            builder.setView(dialogsView)
                    .setTitle(R.string.help_title)
                    .setPositiveButton(R.string.dialog_button_ok, (dialog, id) -> dialog.dismiss());

            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
