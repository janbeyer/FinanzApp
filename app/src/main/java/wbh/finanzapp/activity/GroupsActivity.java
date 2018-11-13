package wbh.finanzapp.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.business.GroupBean;

public class GroupsActivity extends AbstractActivity {

    private static final String LOG_TAG = GroupsActivity.class.getSimpleName();

    private GroupsDataSource groupsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupsDataSource = new GroupsDataSource(this, ActivityMemory.getCurProfileBean().getId());
        Button buttonAddGroup = findViewById(R.id.button_add_group);

        buttonAddGroup.setOnClickListener(view -> {
            AlertDialog addGroupDialog = createAddGroupDialog();
            addGroupDialog.show();
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
     * Show all profile list entries in the GroupsActivity.
     */
    public void showAllListEntries() {
        Log.d(LOG_TAG, "--> Show all list entries.");
        showAllListEntries(groupsDataSource.getBeans(), android.R.layout.simple_list_item_activated_1,
                R.id.list_view_groups);
    }

    private void initializeContextualActionBar() {
        final ListView groupsListView = findViewById(R.id.list_view_groups);
        groupsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        groupsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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
                getMenuInflater().inflate(R.menu.group_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                MenuItem item = menu.findItem(R.id.button_edit_group);
                if (selCount == 1) item.setVisible(true);
                else item.setVisible(false);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                boolean returnValue = true;
                SparseBooleanArray touchedGroupPositions = groupsListView.getCheckedItemPositions();

                switch (menuItem.getItemId()) {
                    case R.id.button_delete_group:
                        for (int i = 0; i < touchedGroupPositions.size(); i++) {
                            boolean isChecked = touchedGroupPositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedGroupPositions.keyAt(i);
                                GroupBean group = (GroupBean) groupsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + group.toString());
                                groupsDataSource.delete(group.getId());
                                Log.d(LOG_TAG, "--> Delete old entry: " + group.toString());
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    case R.id.button_edit_group:
                        for (int i = 0; i < touchedGroupPositions.size(); i++) {
                            boolean isChecked = touchedGroupPositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedGroupPositions.keyAt(i);
                                GroupBean group = (GroupBean) groupsListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + group.toString());
                                AlertDialog editGroupDialog = createEditGroupDialog(group);
                                editGroupDialog.show();
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

    private AlertDialog createAddGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_write_group_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_write_group, viewGroup);

        builder.setView(dialogsView)
                .setTitle(R.string.group_add_title)
                .setPositiveButton(R.string.dialog_button_save, (dialog, id) -> {
                    EditText editTextName = dialogsView.findViewById(R.id.group_new_name);
                    final String groupName = editTextName.getText().toString();

                    EditText editTextDescr = dialogsView.findViewById(R.id.group_new_description);
                    final String groupDescr = editTextDescr.getText().toString();

                    if ((TextUtils.isEmpty(groupName))) {
                        editTextName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    GroupBean newGroup = (GroupBean) groupsDataSource.insert(groupName, groupDescr);

                    Log.d(LOG_TAG, "--> Insert new entry: " + newGroup.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private AlertDialog createEditGroupDialog(final GroupBean group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_write_group_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_write_group, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.group_new_name);
        editTextNewName.setText(group.getName());

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.group_new_description);
        editTextNewDescription.setText(group.getDescription());

        builder.setView(dialogsView)
                .setTitle(R.string.group_edit_title)
                .setPositiveButton(R.string.dialog_button_save, (dialog, id) -> {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if ((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    GroupBean updatedGroup = (GroupBean) groupsDataSource.update(group.getId(), name, description);

                    Log.d(LOG_TAG, "--> Update old entry: " + group.toString());
                    Log.d(LOG_TAG, "--> Update new entry: " + updatedGroup.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_cancel, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    protected int getHelpText() {
        return R.string.help_group_text;
    }
}
