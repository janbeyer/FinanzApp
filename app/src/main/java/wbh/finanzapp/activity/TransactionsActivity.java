package wbh.finanzapp.activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.GroupBean;
import wbh.finanzapp.business.TransactionBean;
import wbh.finanzapp.util.DateDialog;
import wbh.finanzapp.util.ProfileMemory;

@SuppressWarnings("Convert2Diamond")
public class TransactionsActivity extends AbstractActivity {

    private static final String LOG_TAG = TransactionsActivity.class.getSimpleName();

    private TransactionsDataSource transactionsDataSource;
    private GroupsDataSource groupsDataSource;

    private EditText textAmountInputField;

    /**
     * Contains all the defined Groups
     */
    private Map<Integer, Long> spinnerGroupMap;
    private Spinner spinnerGroups;

    /**
     * Contains the radio group with the Transaction date options.
     */
    private RadioGroup radioGroup;

    private TransactionStates transactionStates;

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create TransactionsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        transactionsDataSource = new TransactionsDataSource(this, ProfileMemory.getCurProfileBean().getId());
        groupsDataSource = new GroupsDataSource(this, ProfileMemory.getCurProfileBean().getId());

        transactionStates = new TransactionStates();

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


    /**
     * Helper class for Transaction date states.
     */
    public class TransactionStates {
        // state can be: 1=unique; 2=daily; 3=weekly;  4=monthly;  5=yearly
        int state = 0;

        // 1 = Unique --> DatePicker    --> Default current date
        long uniqueDate = new Date().getTime();

        // 2 = Daily

        // 3 = Weekly --> Dropdown box (1 ...  7) --> Default is Monday
        int dayOfWeek = 0;

        // 4 = Monthly--> Dropdown box (1 ... 31) --> Default is 1
        int monthlyDay = 0;

        // 5 = Yearly --> Dropdown box (1 ... 12) --> Default is 1
        //            --> Dropdown box (1 ... 31) --> Default is 1
        int yearlyMonth = 0;
        int yearlyDay = 0;

        void checkStates(int rbDateMode) {
            if(rbDateMode == R.id.rb_unique) {
                Log.d(LOG_TAG, "--> The selected rb state: unique");
                state = 1;
            } else if(rbDateMode == R.id.rb_daily) {
                Log.d(LOG_TAG, "--> The selected rb state: daily");
                state = 2;
            } else if(rbDateMode == R.id.rb_weekly) {
                Log.d(LOG_TAG, "--> The selected rb state: weekly");
                state = 3;
                dayOfWeek = 1;
            } else if(rbDateMode == R.id.rb_monthly) {
                Log.d(LOG_TAG, "--> The selected rb state: monthly");
                state = 4;
                monthlyDay = 1;
            } else if(rbDateMode == R.id.rb_yearly) {
                Log.d(LOG_TAG, "--> The selected rb state: yearly");
                state = 5;
                yearlyMonth = 1;
                yearlyDay = 1;
            }
        }

        public void setUniqueDate(long uniqueDate) {
            this.uniqueDate = uniqueDate;
        }
    }

    /**
     * Add an Transaction.
     */
    public void addTransaction(String name, String description, double amount, long groupId) {

        // Check the radio button state
        int rbDateMode = radioGroup.getCheckedRadioButtonId();
        transactionStates.checkStates(rbDateMode);
        Date date = new Date(transactionStates.uniqueDate);
        Log.d(LOG_TAG, "--> Date: " + date);

        TransactionBean newTransaction = transactionsDataSource.insert(name, description, groupId, amount, transactionStates.state, transactionStates.uniqueDate, transactionStates.dayOfWeek, transactionStates.monthlyDay, transactionStates.yearlyMonth, transactionStates.yearlyDay);
        Log.d(LOG_TAG, "--> Insert new entry: " + newTransaction.toString());
    }

    public void editTransaction(TransactionBean transaction, String name, String description, double amount, long groupId) {
        // MOCK DATA.
        int state = 1;
        long uniqueDate = new Date().getTime();

        TransactionBean updatedTransaction = transactionsDataSource.update(transaction.getId(), name, description, groupId, amount, state, uniqueDate, null, null, null, null);
        Log.d(LOG_TAG, "--> Update old entry: " + transaction.toString());
        Log.d(LOG_TAG, "--> Update new entry: " + updatedTransaction.toString());
    }

    @SuppressLint("UseSparseArrays")
    private void fillGroupSpinner() {
        List<AbstractBean> groups = groupsDataSource.getBeans();
        spinnerGroupMap = new HashMap<Integer, Long>();
        String[] spinnerArray = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            GroupBean curGroup = (GroupBean) groups.get(i);
            spinnerGroupMap.put(i, curGroup.getId());
            spinnerArray[i] = curGroup.getName();
        }
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinnerGroups.setAdapter(groupsAdapter);
    }

    @Override
    public void createDialog(View view, int title, CustomListener listener, boolean edit) {
        super.createDialog(view, title, listener, edit);

        textAmountInputField = view.findViewById(R.id.transaction_amount);
        spinnerGroups = view.findViewById(R.id.transaction_group);
        radioGroup = view.findViewById(R.id.transaction_rb);

        // activate the first radio button in the group which is daily because
        // in this state no date picker is needed
        radioGroup.check(R.id.rb_daily);

        // Fill the spinner drop down box with the groups
        fillGroupSpinner();

        // Initial validation by create a new bean.
        if (!edit) {
            textAmountInputField.setError(getString(R.string.transaction_amount_validation_error));
        }

        // Validation of the amount.
        textAmountInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String amountStr = charSequence.toString();

                Double amount = null;
                try { amount = Double.parseDouble(amountStr); } catch (NumberFormatException e) { e.printStackTrace(); }

                if (amountStr.isEmpty() || amountStr.equals("-") || amountStr.endsWith(".") || (amount != null && amount == 0.0)) {
                    textAmountInputField.setError(getString(R.string.transaction_amount_validation_error));
                    saveButton.setEnabled(false);
                } else {
                    textAmountInputField.setError(null);
                    enableSaveButtonIfErrorFree(view);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String amountStr = editable.toString();
                Double amount = null;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (amount != null) {
                    int decimalAmount = (int) (amount * 100);
                    if ((amount * 100) != ((double) decimalAmount)) {
                        String newText = "" + ((double) decimalAmount / 100);
                        textAmountInputField.setText(newText);
                        textAmountInputField.setSelection(newText.length());
                    }
                }
            }
        });
    }

    /**
     * Called when an Transaction RadioButton is clicked
     */
    public void onRadioButtonClicked(View view) {
        // 1 = Unique --> DatePicker    --> Default current date
        // 3 = Weekly --> Dropdown box (1 ...  7) --> Default is Monday
        // 4 = Monthly--> Dropdown box (1 ... 31) --> Default is 1
        // 5 = Yearly --> Dropdown box (1 ... 12) --> Default is 1
        //            --> Dropdown box (1 ... 31) --> Default is 1
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_unique:
                if (checked) {
                    DateDialog dateDialog = new DateDialog();
                    dateDialog.setTransactionStates(transactionStates);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    dateDialog.show(transaction, "Date Dialog");
                    break;
                }
            case R.id.rb_weekly:
                if (checked) {
                    break;
                }
            case R.id.rb_monthly:
                if (checked) {
                    break;
                }
            case R.id.rb_yearly:
                if (checked) {
                    break;
                }
        }

    }

    public void createEditTransactionDialog(final TransactionBean transaction) {
        View editView = super.createView(R.id.dialog_write_transaction_root_view, R.layout.dialog_write_transaction);
        createDialog(editView, R.string.transaction_edit_title, new EditListener(transaction), true);
        textNameInputField.setText(transaction.getName());
        textDescriptionInputField.setText(transaction.getDescription());
        double d = transaction.getAmount();
        textAmountInputField.setText(String.valueOf(d));

        spinnerGroups.setSelection(
                spinnerGroupMap.entrySet().stream().filter(e -> e.getValue() == transaction.getGroupId()).findFirst().get().getKey()
        );
    }

    protected int getHelpText() {
        return R.string.help_transaction_text;
    }

    class AddListener extends CustomListener {

        double amount;
        long groupId;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            amount = Double.parseDouble(textAmountInputField.getText().toString());
            groupId = spinnerGroupMap.get(spinnerGroups.getSelectedItemPosition());
            addTransaction(name, description, amount, groupId);
            showAllListEntries();
            dialog.dismiss();
        }
    }

    class EditListener extends CustomListener {

        double amount;
        long groupId;

        TransactionBean transaction;

        EditListener(TransactionBean transaction) {
            this.transaction = transaction;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            amount = Double.parseDouble(textAmountInputField.getText().toString());
            groupId = spinnerGroupMap.get(spinnerGroups.getSelectedItemPosition());
            editTransaction(transaction, name, description, amount, groupId);
            showAllListEntries();
            dialog.dismiss();
        }
    }
}
