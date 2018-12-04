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
import android.widget.ImageButton;
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
import wbh.finanzapp.util.MonthPicker;
import wbh.finanzapp.util.ProfileMemory;
import wbh.finanzapp.util.TransactionStates;
import wbh.finanzapp.util.WeekPicker;
import wbh.finanzapp.util.YearPicker;

@SuppressWarnings("Convert2Diamond")
public class TransactionsActivity extends AbstractActivity {

    private static final String LOG_TAG = TransactionsActivity.class.getSimpleName();

    /**
     * The TransactionsDataSource store the transaction state in the database.
     */
    private TransactionsDataSource transactionsDataSource;

    /**
     * The GroupsDataSource store the group state in the database.
     */
    private GroupsDataSource groupsDataSource;

    /**
     * A text input field. The user can choose an amount for every transaction.
     */
    private EditText textAmountInputField;

    /**
     * Contains all the defined Groups
     */
    private Map<Integer, Long> spinnerGroupMap;

    /**
     * A DropDown menu, so that the user can choose the group for a transaction.
     */
    private Spinner spinnerGroups;

    /**
     * Contains the radio group with the Transaction date options.
     */
    private RadioGroup radioGroupTransactionDate;

    /**
     * Contains the transaction states: unique, daily, weekly, monthly and yearly.
     */
    private TransactionStates transactionStates;

    private ImageButton button_unique;
    private ImageButton button_weekly;
    private ImageButton button_monthly;
    private ImageButton button_yearly;

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
     * This enum define the possible date mode.
     */
    enum DateMode {
        daily,
        unique,
        weekly,
        monthly,
        yearly
    }

    /**
     * The current selected date mode.
     */
    static DateMode dateMode = DateMode.daily;

    /**
     * Add an Transaction to the transaction database.
     */
    public void addTransaction(String name, String description, double amount, long groupId) {
        // Check the radio button state
        int rbDateMode = radioGroupTransactionDate.getCheckedRadioButtonId();
        transactionStates.checkStates(rbDateMode);
        Log.d(LOG_TAG, "--> TransactionStates: " + transactionStates);
        TransactionBean newTransaction = transactionsDataSource.insert(name, description, groupId, amount, transactionStates.state, transactionStates.uniqueDate, transactionStates.dayOfWeek, transactionStates.monthlyDay, transactionStates.yearlyMonth, transactionStates.yearlyDay);
        Log.d(LOG_TAG, "--> Insert new entry: " + newTransaction.toString());
    }

    /**
     * Edit an existing transaction in the database.
     */
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
        radioGroupTransactionDate = view.findViewById(R.id.transaction_rb);

        // activate the first radio button in the group which is daily because
        // in this state no date picker is needed
        radioGroupTransactionDate.check(R.id.rb_daily);

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
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

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

        button_unique = view.findViewById(R.id.b_unique);
        button_weekly = view.findViewById(R.id.b_weekly);
        button_monthly = view.findViewById(R.id.b_monthly);
        button_yearly = view.findViewById(R.id.b_yearly);
    }

    /**
     * This function is called if an date button is clicked
     */
    public void onDateButtonClick(View view) {
        if (dateMode == DateMode.unique) {
            DateDialog dateDialog = new DateDialog();
            dateDialog.setTransactionStates(transactionStates);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            dateDialog.show(transaction, "Date Dialog");
        } else if (dateMode == DateMode.weekly) {
            WeekPicker weekPicker = new WeekPicker(this, transactionStates);
            weekPicker.show();
        } else if (dateMode == DateMode.monthly) {
            MonthPicker monthPicker = new MonthPicker(this, transactionStates);
            monthPicker.show();
        } else if (dateMode == DateMode.yearly) {
            YearPicker yearPicker = new YearPicker(this, transactionStates);
            yearPicker.show();
        }
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
        // if unchecked return
        if (!checked)
            return;
        int id = view.getId();

        setButtonsInvisible();
        // Check which radio button was clicked
        switch (id) {
            case R.id.rb_unique:
                Log.d(LOG_TAG, "--> onRadioButtonClicked(): rb_unique");
                dateMode = DateMode.unique;
                button_unique.setVisibility(Button.VISIBLE);
                break;
            case R.id.rb_weekly:
                Log.d(LOG_TAG, "--> onRadioButtonClicked(): rb_weekly");
                dateMode = DateMode.weekly;
                button_weekly.setVisibility(Button.VISIBLE);
                break;
            case R.id.rb_monthly:
                Log.d(LOG_TAG, "--> onRadioButtonClicked(): rb_monthly");
                dateMode = DateMode.monthly;
                button_monthly.setVisibility(Button.VISIBLE);
                break;
            case R.id.rb_yearly:
                Log.d(LOG_TAG, "--> onRadioButtonClicked(): rb_yearly");
                dateMode = DateMode.yearly;
                button_yearly.setVisibility(Button.VISIBLE);
                break;
        }
    }

    /**
     * Make the date picker buttons invisible.
     */
    public void setButtonsInvisible() {
        button_unique.setVisibility(Button.INVISIBLE);
        button_weekly.setVisibility(Button.INVISIBLE);
        button_monthly.setVisibility(Button.INVISIBLE);
        button_yearly.setVisibility(Button.INVISIBLE);
    }

    /**
     * Create the edit dialog for the transaction editing.
     */
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

    /**
     * Return the help text for the transaction activity.
     */
    protected int getHelpText() {
        return R.string.help_transaction_text;
    }

    /**
     * If the add button in the transaction dialog is clicked this listener start
     * the add action and refresh the transaction list view.
     */
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

    /**
     * If the edit button is clicked this listener start the edit action and
     * refresh the transaction list view.
     */
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
