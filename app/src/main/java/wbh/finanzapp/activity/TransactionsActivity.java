package wbh.finanzapp.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.access.TransactionsDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.TransactionBean;

public class TransactionsActivity extends AbstractActivity {

    private static final String LOG_TAG = TransactionsActivity.class.getSimpleName();

    private GroupsDataSource groupsDataSource;
    private TransactionsDataSource transactionsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        groupsDataSource = new GroupsDataSource(this, ActivityMemory.getCurProfileBean().getId());
        transactionsDataSource = new TransactionsDataSource(this, ActivityMemory.getCurProfileBean().getId());
        Button buttonAddTransaction = findViewById(R.id.button_add_transaction);

        buttonAddTransaction.setOnClickListener(view -> {
            createAddDialog();
        });
        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Show all transaction list entries in the TransactionActivity.
     */
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
                                TransactionBean transaction = (TransactionBean) transactionsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + transaction.toString());
                                AlertDialog editDialog = createEditDialog(transaction);
                                editDialog.show();
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

    private void createAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_write_transaction_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_write_transaction, viewGroup);

        Spinner spinner = dialogsView.findViewById(R.id.transaction_new_group);
        List<AbstractBean> groups = groupsDataSource.getBeans();
        String[] spinnerArray = new String[groups.size()];
        HashMap<Integer,Long> spinnerMap = new HashMap<Integer, Long>();
        for (int i = 0; i < groups.size(); i++) {
            spinnerMap.put(i, groups.get(i).getId());
            spinnerArray[i] = groups.get(i).getName();
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        EditText editTextName = dialogsView.findViewById(R.id.transaction_new_name);
        EditText editTextDesc = dialogsView.findViewById(R.id.transaction_new_description);
        EditText editTextAmount = dialogsView.findViewById(R.id.transaction_new_amount);
        RadioButton radioButtonExpenditure = dialogsView.findViewById(R.id.transaction_new_expenditure);
        RadioButton rbStateUnique = dialogsView.findViewById(R.id.transaction_new_state_unique);
        RadioButton rbStateDaily = dialogsView.findViewById(R.id.transaction_new_state_daily);
        RadioButton rbStateWeekly = dialogsView.findViewById(R.id.transaction_new_state_weekly);
        RadioButton rbStateMonthly = dialogsView.findViewById(R.id.transaction_new_state_monthly);
        RadioButton rbStateYearly = dialogsView.findViewById(R.id.transaction_new_state_yearly);


        builder.setView(dialogsView)
                .setTitle(R.string.transaction_add_title)
                .setPositiveButton(R.string.dialog_button_save, (dialog, id) -> {

                    String name = editTextName.getText().toString();
                    String descr = editTextDesc.getText().toString();
                    Integer amount = Integer.parseInt(editTextAmount.getText().toString());
                    boolean expenditure = radioButtonExpenditure.isChecked();
                    String groupName = spinner.getSelectedItem().toString();
                    Long groupId = spinnerMap.get(spinner.getSelectedItemPosition());
                    Integer state = null;
                    Long uniqueDate = null;
                    Integer dayOfWeek = null;
                    Integer monthlyDay = null;
                    Integer yearlyMonth = null;
                    Integer yearlyDay = null;
                    if(rbStateUnique.isChecked()) {
                        state = 1;
                        EditText editTextUniqueDate = dialogsView.findViewById(R.id.transaction_new_state_unique_date);
                        uniqueDate = Long.parseLong(editTextUniqueDate.getText().toString());
                    }

                    if(rbStateDaily.isChecked()) {
                        state = 2;
                    }

                    if(rbStateWeekly.isChecked()) {
                        state = 3;
                        EditText editTextDayOfWeek = dialogsView.findViewById(R.id.transaction_new_state_weekly_day);
                        dayOfWeek = Integer.parseInt(editTextDayOfWeek.getText().toString());
                    }

                    if(rbStateMonthly.isChecked()) {
                        state = 4;
                        EditText editTextMonthlyDay = dialogsView.findViewById(R.id.transaction_new_state_monthly_day);
                        monthlyDay = Integer.parseInt(editTextMonthlyDay.getText().toString());
                    }

                    if(rbStateYearly.isChecked()) {
                        state = 5;
                        EditText editTextYearlyMonth = dialogsView.findViewById(R.id.transaction_new_state_yearly_month);
                        yearlyMonth = Integer.parseInt(editTextYearlyMonth.getText().toString());
                        EditText editTextYearlyDay = dialogsView.findViewById(R.id.transaction_new_state_yearly_day);
                        yearlyDay = Integer.parseInt(editTextYearlyDay.getText().toString());
                    }

                    transactionsDataSource.insert(name, descr, groupId, amount, expenditure, state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);

                    // TODO: Validation.

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    private AlertDialog createEditDialog(final TransactionBean transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_write_transaction_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_write_transaction, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.transaction_new_name);
        editTextNewName.setText(transaction.getName());

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.transaction_new_description);
        editTextNewDescription.setText(transaction.getDescription());

        builder.setView(dialogsView)
                .setTitle(R.string.group_edit_title)
                .setPositiveButton(R.string.dialog_button_save, (dialog, id) -> {
                    // TODO: Validation.
                    // TODO: Update the validation
                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    protected int getHelpText() {
        return R.string.help_transaction_text;
    }
}
