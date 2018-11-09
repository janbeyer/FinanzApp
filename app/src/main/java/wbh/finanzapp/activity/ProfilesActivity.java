package wbh.finanzapp.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.ProfileBean;

public class ProfilesActivity extends AbstractActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private ProfilesDataSource profileDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        profileDataSource = new ProfilesDataSource(this);

        Button buttonAddProfile = findViewById(R.id.button_add_profile);
        buttonAddProfile.setOnClickListener(view -> {
            AlertDialog addProfileDialog = createAddProfileDialog();
            addProfileDialog.show();
        });

        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "--> Resume ProfilesActivity");
        super.onResume();
        Log.d(LOG_TAG, "--> Open the data source.");
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "--> Pause ProfilesActivity");
        super.onPause();
    }

    /**
     * Show all profile list entries in the ProfileActivity.
     */
    private void showAllListEntries() {
        Log.d(LOG_TAG, "--> Show all list entries.");
        List<AbstractBean> profiles = profileDataSource.getProfiles();

        ArrayAdapter<AbstractBean> profileArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_activated_1, profiles);

        ListView profilesListView = findViewById(R.id.list_view_profiles);
        profilesListView.setAdapter(profileArrayAdapter);

        profilesListView.setOnItemClickListener((adapterView, view, position, id) -> {
            ProfileBean profile = (ProfileBean) adapterView.getItemAtPosition(position);
            profileDataSource.update(profile.getId(), null, null);

            Intent myIntent = new Intent(this, MenuActivity.class);
            myIntent.putExtra(MenuActivity.PARAM_PROFILE_ID, profile.getId());
            Log.d(LOG_TAG, "--> Start the menu activity for the profile: " + profile.toString());
            startActivity(myIntent);
        });
    }

    private void initializeContextualActionBar() {
        Log.d(LOG_TAG, "--> initialize contextual ActionBar.");
        final ListView profilesListView = findViewById(R.id.list_view_profiles);
        profilesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        profilesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
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
                getMenuInflater().inflate(R.menu.profile_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                MenuItem item = menu.findItem(R.id.button_edit_profile);
                if (selCount == 1) item.setVisible(true);
                else item.setVisible(false);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                SparseBooleanArray touchedProfilePositions = profilesListView.getCheckedItemPositions();

                switch (menuItem.getItemId()) {
                    case R.id.button_delete_profile:
                        for (int i = 0; i < touchedProfilePositions.size(); i++) {
                            boolean isChecked = touchedProfilePositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedProfilePositions.keyAt(i);
                                ProfileBean profile = (ProfileBean) profilesListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + profile.toString());
                                profileDataSource.delete(profile);
                                Log.d(LOG_TAG, "--> Delete old entry: " + profile.toString());
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    case R.id.button_edit_profile:
                        for (int i = 0; i < touchedProfilePositions.size(); i++) {
                            boolean isChecked = touchedProfilePositions.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedProfilePositions.keyAt(i);
                                ProfileBean profile = (ProfileBean) profilesListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position in ListView: " + positionInListView + " Content: " + profile.toString());
                                AlertDialog editProfileDialog = createEditProfileDialog(profile);
                                editProfileDialog.show();
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                selCount = 0;
            }
        });
    }

    @SuppressWarnings({"CodeBlock2Expr", "Convert2MethodRef"})
    private AlertDialog createAddProfileDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_add_profile_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_add_profile, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.profile_new_name);
        editTextNewName.setText("");

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.profile_new_description);
        editTextNewDescription.setText("");

        builder.setView(dialogsView)
                .setTitle(R.string.profile_add_title)
                .setPositiveButton(R.string.dialog_button_positive, (dialog, id) -> {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if ((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    Map<String, String> basicGroups = getBasicGroups(this);

                    ProfileBean newProfile = (ProfileBean)profileDataSource.insert(name, description);
                    GroupsDataSource groupsDataSource = new GroupsDataSource(this, newProfile.getId());
                    basicGroups.forEach((name2, description2) -> {
                        groupsDataSource.insert(name2, description2);
                    });

                    Log.d(LOG_TAG, "--> Insert new entry: " + newProfile.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_negative, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private AlertDialog createEditProfileDialog(final ProfileBean profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup viewGroup = findViewById(R.id.dialog_edit_profile_root_view);
        View dialogsView = inflater.inflate(R.layout.dialog_edit_profile, viewGroup);

        final EditText editTextNewName = dialogsView.findViewById(R.id.profile_new_name);
        editTextNewName.setText(profile.getName());

        final EditText editTextNewDescription = dialogsView.findViewById(R.id.profile_new_description);
        editTextNewDescription.setText(profile.getDescription());

        builder.setView(dialogsView)
                .setTitle(R.string.profile_edit_title)
                .setPositiveButton(R.string.dialog_button_positive, (dialog, id) -> {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if ((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.field_name_error_required));
                        return;
                    }

                    ProfileBean updatedProfile = (ProfileBean)profileDataSource.update(profile.getId(), name, description);

                    Log.d(LOG_TAG, "--> Update old entry: " + profile.toString());
                    Log.d(LOG_TAG, "--> Update new entry: " + updatedProfile.toString());

                    showAllListEntries();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_negative, (dialog, id) -> dialog.cancel());

        return builder.create();
    }

    private Map<String, String> getBasicGroups(Context context) {
        Map<String, String> basicGroups = new HashMap<>();
        basicGroups.put(context.getString(R.string.group_default_housekeeping_name), context.getString(R.string.group_default_housekeeping_descr));
        basicGroups.put(context.getString(R.string.group_default_groceries_name), context.getString(R.string.group_default_groceries_descr));
        basicGroups.put(context.getString(R.string.group_default_restaurant_name), context.getString(R.string.group_default_restaurant_descr));
        basicGroups.put(context.getString(R.string.group_default_transport_name), context.getString(R.string.group_default_transport_descr));
        basicGroups.put(context.getString(R.string.group_default_entertainment_name), context.getString(R.string.group_default_entertainment_descr));
        basicGroups.put(context.getString(R.string.group_default_personal_name), context.getString(R.string.group_default_personal_descr));
        basicGroups.put(context.getString(R.string.group_default_health_name), context.getString(R.string.group_default_health_descr));
        basicGroups.put(context.getString(R.string.group_default_insurance_name), context.getString(R.string.group_default_insurance_descr));
        basicGroups.put(context.getString(R.string.group_default_realestate_name), context.getString(R.string.group_default_restaurant_descr));
        basicGroups.put(context.getString(R.string.group_default_clothing_name), context.getString(R.string.group_default_clothing_descr));
        basicGroups.put(context.getString(R.string.group_default_education_name), context.getString(R.string.group_default_education_descr));
        basicGroups.put(context.getString(R.string.group_default_holiday_name), context.getString(R.string.group_default_holiday_descr));
        basicGroups.put(context.getString(R.string.group_default_freetime_name), context.getString(R.string.group_default_freetime_descr));
        basicGroups.put(context.getString(R.string.group_default_salary_name), context.getString(R.string.group_default_salary_descr));
        return basicGroups;
    }
}
