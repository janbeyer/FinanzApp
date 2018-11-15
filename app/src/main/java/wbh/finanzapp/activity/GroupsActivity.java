package wbh.finanzapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.business.GroupBean;

public class GroupsActivity extends AbstractActivity {

    private static final String LOG_TAG = GroupsActivity.class.getSimpleName();

    private GroupsDataSource groupsDataSource;

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupsDataSource = new GroupsDataSource(this, ActivityMemory.getCurProfileBean().getId());
        Button buttonAddGroup = findViewById(R.id.button_add_group);

        buttonAddGroup.setOnClickListener(view -> {
            createDialog(R.string.group_add_title, new AddListener(), false);
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
        createListView(groupsDataSource.getBeans(), android.R.layout.simple_list_item_activated_1,
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
                                createEditGroupDialog(group);
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
     * Add a new Group.
     */
    public void addGroup(String name, String description) {
        GroupBean newGroup = groupsDataSource.insert(name, description);
        Log.d(LOG_TAG, "--> Insert new entry: " + newGroup.toString());
        showAllListEntries();
    }

    /**
     * Edit a group.
     */
    public void editGroup(GroupBean group, String name, String description) {
        GroupBean updatedGroup = groupsDataSource.update(group.getId(), name, description);
        Log.d(LOG_TAG, "--> Update old entry: " + group.toString());
        Log.d(LOG_TAG, "--> Update new entry: " + updatedGroup.toString());
        showAllListEntries();
    }

    /**
     * Add listener class.
     */
    class AddListener extends CustomListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            addGroup(name, description);
            dialog.dismiss();
        }
    }

    /**
     * Edit listener class.
     */
    class EditListener extends CustomListener {
        GroupBean group;

        EditListener(GroupBean group) {
            this.group = group;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            editGroup(group, name, description);
            dialog.dismiss();
        }
    }

    public void createEditGroupDialog(final GroupBean group) {
        createDialog(R.string.group_edit_title, new EditListener(group), true);
        textNameInputField.setText(group.getName());
        textDescriptionInputField.setText(group.getDescription());
    }

    protected int getHelpText() {
        return R.string.help_group_text;
    }
}
