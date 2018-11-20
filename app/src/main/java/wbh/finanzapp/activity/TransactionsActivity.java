package wbh.finanzapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Date;

import wbh.finanzapp.R;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.TransactionBean;
import wbh.finanzapp.util.ProfileMemory;

public class TransactionsActivity extends AbstractActivity {

    private static final String LOG_TAG = TransactionsActivity.class.getSimpleName();

    private TransactionsDataSource transactionsDataSource;

    protected EditText textAmountInputField;

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create TransactionsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        transactionsDataSource = new TransactionsDataSource(this, ProfileMemory.getCurProfileBean().getId());
        Button buttonAddTransaction = findViewById(R.id.button_add_transaction);

        buttonAddTransaction.setOnClickListener(view -> {
            View addView = super.createView(R.id.dialog_write_transaction_root_view, R.layout.dialog_write_transaction);
            createDialog(addView, R.string.transaction_add_title, new AddListener(), false);
        });
        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "--> Resume TransactionsActivity");
        super.onResume();
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "--> Pause TransactionsActivity");
        super.onPause();
    }

    public void showAllListEntries() {
        Log.d(LOG_TAG, "--> Show all list entries.");
        createListView(transactionsDataSource.getBeans(), android.R.layout.simple_list_item_activated_1,
                R.id.list_view_transactions);
    }

    private void initializeContextualActionBar() {
        final ListView transactionsListView = findViewById(R.id.list_view_transactions);
        transactionsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        transactionsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selCount = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                if (checked) selCount++;
                else selCount--;
                String cabTitle = selCount + " " + getString(R.string.cab_checked_string);
                actionMode.setTitle(cabTitle);
                actionMode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                getMenuInflater().inflate(R.menu.transaction_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                MenuItem item = menu.findItem(R.id.button_edit_transaction);
                if (selCount == 1) item.setVisible(true);
                else item.setVisible(false);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                boolean returnValue = true;
                SparseBooleanArray touchedPositions = transactionsListView.getCheckedItemPositions();

                switch (menuItem.getItemId()) {
                    case R.id.button_delete_transaction:
                        for (int i = 0; i < touchedPositions.size(); i++) {
                            boolean isChecked = touchedPositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedPositions.keyAt(i);
                                TransactionBean transaction = (TransactionBean) transactionsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + transaction.toString());
                                transactionsDataSource.delete(transaction.getId());
                                Log.d(LOG_TAG, "--> Delete old entry: " + transaction.toString());
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    case R.id.button_edit_transaction:
                        for (int i = 0; i < touchedPositions.size(); i++) {
                            boolean isChecked = touchedPositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedPositions.keyAt(i);
                                TransactionBean transactionBean = (TransactionBean) transactionsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + transactionBean.toString());
                                createEditTransactionDialog(transactionBean);
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    default:
                        returnValue = false;
                        break;
                }
                return returnValue;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                selCount = 0;
            }
        });
    }

    public void addTransaction(String name, String description, double amount) {
        // MOCK DATA.
        long groupId = 1; // must exists -> else error!
        int state = 1;
        long uniqueDate = new Date().getTime();

        TransactionBean newTransaction = transactionsDataSource.insert(name, description, groupId, amount, state, uniqueDate, null, null, null, null);
        Log.d(LOG_TAG, "--> Insert new entry: " + newTransaction.toString());
    }

    public void editTransaction(TransactionBean transaction, String name, String description, double amount) {
        // MOCK DATA.
        long groupId = 1; // must exists -> else error!
        int state = 1;
        long uniqueDate = new Date().getTime();

        TransactionBean updatedTransaction = transactionsDataSource.update(transaction.getId(), name, description, groupId, amount, state, uniqueDate, null, null, null, null);
        Log.d(LOG_TAG, "--> Update old entry: " + transaction.toString());
        Log.d(LOG_TAG, "--> Update new entry: " + updatedTransaction.toString());
    }

    @Override
    public void createDialog(View view, int title, CustomListener listener, boolean edit) {
        super.createDialog(view, title, listener, edit);

        textAmountInputField = view.findViewById(R.id.transaction_amount);

        // Initial validation by create a new bean.
        if(!edit) {
            textAmountInputField.setError(getString(R.string.transaction_amount_validation_error));
        }

        // Validation of the amount.
        textAmountInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String amountStr = charSequence.toString();
                Double amount = null;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {}

                if(amountStr == null || amountStr.isEmpty() || amountStr.equals("-") || amountStr.endsWith(".") || (amount != null && amount == 0.0)) {
                    textAmountInputField.setError(getString(R.string.transaction_amount_validation_error));
                    saveButton.setEnabled(false);
                } else {
                    textAmountInputField.setError(null);
                    enableButtonIfErrorFree(view);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String amountStr = editable.toString();
                Double amount = null;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {}

                if(amount != null) {
                    int decimalAmount = (int) (amount * 100);
                    if((amount * 100) != ((double) decimalAmount)) {
                        String newText = "" + ((double) decimalAmount / 100);
                        textAmountInputField.setText(newText);
                        textAmountInputField.setSelection(newText.length());
                    }
                }
            }
        });
    }

    class AddListener extends CustomListener {

        protected double amount;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            amount = Double.parseDouble(textAmountInputField.getText().toString());
            addTransaction(name, description, amount);
            showAllListEntries();
            dialog.dismiss();
        }
    }

    class EditListener extends CustomListener {

        protected double amount;

        TransactionBean transaction;
        EditListener(TransactionBean transaction) {
            this.transaction = transaction;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            amount = Double.parseDouble(textAmountInputField.getText().toString());
            editTransaction(transaction, name, description, amount);
            showAllListEntries();
            dialog.dismiss();
        }
    }

    public void createEditTransactionDialog(final TransactionBean transaction) {
        View editView = super.createView(R.id.dialog_write_transaction_root_view, R.layout.dialog_write_transaction);
        createDialog(editView, R.string.transaction_edit_title, new EditListener(transaction), true);
        textNameInputField.setText(transaction.getName());
        textDescriptionInputField.setText(transaction.getDescription());
        textAmountInputField.setText("" + transaction.getAmount());

    }

    protected int getHelpText() {
        return R.string.help_transaction_text;
    }
}
