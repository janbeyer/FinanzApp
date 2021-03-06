package wbh.finanzapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import wbh.finanzapp.R;
import wbh.finanzapp.access.GroupsDataSource;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.ProfileBean;
import wbh.finanzapp.util.ProfileMemory;

public class ProfilesActivity extends AbstractActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private ProfilesDataSource profileDataSource;

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "--> Create ProfilesActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        profileDataSource = new ProfilesDataSource(this);
        Button buttonAddProfile = findViewById(R.id.button_add_profile);
        buttonAddProfile.setOnClickListener(view -> {
            View addView = super.createView(R.id.dialog_write_basic_root_view, R.layout.dialog_write_basic);
            createDialog(addView, R.string.profile_add_title, new AddListener(), false);
        });
        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "--> Resume ProfilesActivity");
        super.onResume();
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "--> Pause ProfilesActivity");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "--> Destroy ProfilesActivity");
        profileDataSource.close();
        super.onDestroy();
    }

    public void showAllListEntries() {
        Log.d(LOG_TAG, "--> Show all list entries.");
        ListView profilesListView = createListView(profileDataSource.getBeans(), R.id.list_view_profiles);
        profilesListView.setOnItemClickListener((adapterView, view, position, id) -> {
            ProfileBean selectedProfile = (ProfileBean) adapterView.getItemAtPosition(position);
            ProfileMemory.setCurProfileBean(selectedProfile);
            profileDataSource.update(selectedProfile.getId(), null, null);
            Intent myIntent = new Intent(this, MenuActivity.class);
            Log.d(LOG_TAG, "--> Start the menu activity for the profile: " + selectedProfile.toString());
            startActivity(myIntent);
        });
    }

    public void initializeContextualActionBar() {
        Log.d(LOG_TAG, "--> initialize contextual ActionBar.");
        final ListView profilesListView = findViewById(R.id.list_view_profiles);
        profilesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        profilesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selCount = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                if (checked)
                    selCount++;
                else
                    selCount--;
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
                if (selCount == 1)
                    item.setVisible(true);
                else
                    item.setVisible(false);
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
                                profileDataSource.delete(profile.getId());
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
                                createEditProfileDialog(profile);
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

    @SuppressWarnings({"Convert2MethodRef", "CodeBlock2Expr"})
    public void addProfile(String profileName, String profileDescription) {
        Map<String, String> basicGroups = getBasicGroups(this);
        ProfileBean newProfile = profileDataSource.insert(profileName, profileDescription);
        GroupsDataSource groupsDataSource = new GroupsDataSource(this, newProfile.getId());
        basicGroups.forEach((groupName, groupDescription) -> {
            groupsDataSource.insert(groupName, groupDescription);
        });
        Log.d(LOG_TAG, "--> Insert new entry: " + newProfile.toString());
    }

    public void editProfile(ProfileBean profile, String name, String description) {
        ProfileBean updatedProfile = profileDataSource.update(profile.getId(), name, description);
        Log.d(LOG_TAG, "--> Update old entry: " + profile.toString());
        Log.d(LOG_TAG, "--> Update new entry: " + updatedProfile.toString());
    }

    class AddListener extends CustomListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            addProfile(name, description);
            showAllListEntries();
            dialog.dismiss();
        }
    }

    class EditListener extends CustomListener {
        ProfileBean profile;

        EditListener(ProfileBean profile) {
            this.profile = profile;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            editProfile(profile, name, description);
            showAllListEntries();
            dialog.dismiss();
        }
    }

    public void createEditProfileDialog(final ProfileBean profile) {
        View editView = super.createView(R.id.dialog_write_basic_root_view, R.layout.dialog_write_basic);
        createDialog(editView, R.string.profile_edit_title, new EditListener(profile), true);
        textNameInputField.setText(profile.getName());
        textDescriptionInputField.setText(profile.getDescription());
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

    protected int getHelpText() {
        return R.string.help_profile_text;
    }

}
