package wbh.finanzapp.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.GroupBean;
import wbh.finanzapp.business.ProfileBean;

public class GroupsActivity extends AppCompatActivity {

    private static final String LOG_TAG = GroupsActivity.class.getSimpleName();

    public static final String PARAM_PROFILE_ID = "profileId";

    private ProfileBean profileBean;

    private GroupsDataSource groupsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        ProfilesDataSource profileDataSource = new ProfilesDataSource(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Long profileId = (Long) bundle.get(PARAM_PROFILE_ID);
            if (profileId != null) {
                profileBean = profileDataSource.getProfile(profileId);
            }
        }

        groupsDataSource = new GroupsDataSource(this);
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
        Log.d(LOG_TAG, "--> Open the data source.");
        //profileDataSource.open();
        //groupsDataSource.open();
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "--> Close the data source.");
        //profileDataSource.close();
        //groupsDataSource.close();
    }

    private void showAllListEntries() {
        List<GroupBean> groups = groupsDataSource.getProfileGroups(profileBean.getId());

        ArrayAdapter<GroupBean> groupArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_activated_1, groups);

        ListView groupsListView = findViewById(R.id.list_view_groups);
        groupsListView.setAdapter(groupArrayAdapter);
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
                                groupsDataSource.deleteGroup(group.getId());
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

        ViewGroup viewGroup = findViewById(R.id.dialog_add_profile_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_add_group, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.group_new_name);
        editTextNewName.setText("");

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.group_new_description);
        editTextNewDescription.setText("");

        builder.setView(dialogsView)
                .setTitle(R.string.group_add_title)
                .setPositiveButton(R.string.dialog_button_positive, (dialog, id) -> {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if ((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    GroupBean newGroup = groupsDataSource.insertGroup(profileBean.getId(), name, description, true);

                    Log.d(LOG_TAG, "--> Insert new entry: " + newGroup.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_negative, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private AlertDialog createEditGroupDialog(final GroupBean group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.select_dialog_list_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_edit_group, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.group_new_name);
        editTextNewName.setText(group.getName());

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.group_new_description);
        editTextNewDescription.setText(group.getDescription());

        builder.setView(dialogsView)
                .setTitle(R.string.group_edit_title)
                .setPositiveButton(R.string.dialog_button_positive, (dialog, id) -> {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if ((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    GroupBean updatedGroup = groupsDataSource.updateGroup(profileBean.getId(), group.getId(), name, description);

                    Log.d(LOG_TAG, "--> Update old entry: " + group.toString());
                    Log.d(LOG_TAG, "--> Update new entry: " + updatedGroup.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_negative, (dialog, id) -> dialog.cancel());

        return builder.create();
    }
}
