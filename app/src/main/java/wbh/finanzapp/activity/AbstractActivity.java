package wbh.finanzapp.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    /**
     * Text name input field.
     */
    protected EditText textNameInputField;

    /**
     * Text description input field.
     */
    protected EditText textDescriptionInputField;

    /**
     * The save button.
     */
    protected Button saveButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_option_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            ViewGroup viewHelp = findViewById(R.id.dialog_help);
            View dialogsView = inflater.inflate(R.layout.dialog_help, viewHelp);

            final TextView helpText = dialogsView.findViewById(R.id.dialog_help_text);
            helpText.setText(getHelpText());

            builder.setView(dialogsView).setTitle(R.string.help_title).setPositiveButton(R.string.dialog_button_ok, (dialog, id) -> dialog.dismiss());

            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create the list view and set the array adapter.
     */
    public ListView createListView(List<AbstractBean> list, int layoutType, int listViewId) {
        ArrayAdapter<AbstractBean> arrayAdapter = new ArrayAdapter<>(this, layoutType, list);
        ListView listView = findViewById(listViewId);
        listView.setAdapter(arrayAdapter);
        return listView;
    }

    /**
     * Return a new AlertDialog.
     */
    public AlertDialog createDialog(int title, CustomListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = findViewById(R.id.dialog_write_profile_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_write_profile, viewGroup);
        builder.setView(dialogsView);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());
        builder.setPositiveButton(R.string.dialog_button_save, listener);
        AlertDialog dialog = builder.create();
        dialog.show();

        textNameInputField = dialogsView.findViewById(R.id.profile_new_name);
        textDescriptionInputField = dialogsView.findViewById(R.id.profile_new_description);
        saveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // if an field is an obligation field we have to do 2 steps
        // 1. deactivate the save button at the beginning
        // 2. add an OnKeyListener to activate the button, if an valid input is done

        // 1. set the field to red because it is obligation field
        if(textNameInputField.getText().toString().isEmpty()) {
            textNameInputField.setError(getString(R.string.field_name_error_required));
            saveButton.setEnabled(false);
        }

        // 2. This is called when the first text input is done
        textNameInputField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String s = textNameInputField.getText().toString();
                if(s.isEmpty()) {
                    textNameInputField.setError(getString(R.string.field_name_error_required));
                    saveButton.setEnabled(false);
                } else {
                    textNameInputField.setError(null);
                    saveButton.setEnabled(true);
                }
                return false;
            }
        });

        return dialog;
    }

    /**
     * A custom OnClickListener base class.
     */
    class CustomListener implements DialogInterface.OnClickListener {

        protected String name;
        protected String description;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            name = textNameInputField.getText().toString();
            description = textDescriptionInputField.getText().toString();
            if ((TextUtils.isEmpty(name))) {
                textNameInputField.setError(getString(R.string.field_name_error_required));
                saveButton.setEnabled(false);
            }
        }
    }
}
