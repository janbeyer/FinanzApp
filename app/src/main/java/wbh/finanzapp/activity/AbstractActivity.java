package wbh.finanzapp.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public abstract class AbstractActivity extends AppCompatActivity {

    protected abstract int getHelpText();

    protected EditText textNameInputField;

    protected EditText textDescriptionInputField;

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

    public ListView createListView(List<?> list, int listViewId) {
        ArrayAdapter<?> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, list);
        ListView listView = findViewById(listViewId);
        listView.setAdapter(arrayAdapter);
        return listView;
    }


    public View createView(int viewId, int layoutId) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = findViewById(viewId);
        return inflater.inflate(layoutId, viewGroup);
    }

    private AlertDialog.Builder createBuilder(View view, int titleId, CustomListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(titleId);
        builder.setPositiveButton(R.string.dialog_button_save, listener);
        builder.setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());
        return builder;
    }

    public void createDialog(View view, int titleId, CustomListener listener, boolean edit) {
        // if an field is an obligation field we have to do 2 steps
        // 1. deactivate the save button at the beginning
        // 2. add an OnKeyListener to activate the button, if an valid input is done

        // 1. set the field to red because it is obligation field
        // in edit dialogs the text is set to the previous data
        // in that case the text is not empty
        AlertDialog.Builder builder = createBuilder(view, titleId, listener);

        AlertDialog dialog = builder.create();
        dialog.show();

        textNameInputField = view.findViewById(R.id.basic_name);
        textDescriptionInputField = view.findViewById(R.id.basic_description);
        saveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // Initial validation by create a new bean.
        if (!edit) {
            textNameInputField.setError(getString(R.string.field_name_validation_error));
            saveButton.setEnabled(false);
        }

        // Validation of the name.
        textNameInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String name = charSequence.toString();
                if (name.isEmpty()) {
                    textNameInputField.setError(getString(R.string.field_name_validation_error));
                    saveButton.setEnabled(false);
                } else {
                    textNameInputField.setError(null);
                    enableSaveButtonIfErrorFree(view);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void enableSaveButtonIfErrorFree(View view) {
        if (checkIfViewIsErrorFree(view)) {
            saveButton.setEnabled(true);
        }
    }

    public boolean checkIfViewIsErrorFree(View view) {
        ViewGroup viewGroup = null;
        try {
            viewGroup = (ViewGroup) view;
        } catch (ClassCastException e) {
//            e.printStackTrace();
        }
        if (viewGroup == null || viewGroup.getChildCount() == 0) return true;
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            View curView = viewGroup.getChildAt(i);
            if (curView instanceof EditText) {
                EditText curTextField = (EditText) curView;
                if (!TextUtils.isEmpty(curTextField.getError())) return false;
            } else if (!checkIfViewIsErrorFree(curView)) {
                return false;
            }
        }
        return true;
    }

    class CustomListener implements DialogInterface.OnClickListener {

        protected String name;
        protected String description;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            name = textNameInputField.getText().toString();
            description = textDescriptionInputField.getText().toString();
            if ((TextUtils.isEmpty(name))) {
                textNameInputField.setError(getString(R.string.field_name_validation_error));
                saveButton.setEnabled(false);
            }
        }
    }
}
