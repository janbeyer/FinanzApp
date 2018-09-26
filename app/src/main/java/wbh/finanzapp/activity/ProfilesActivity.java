package wbh.finanzapp.activity;

import android.content.DialogInterface;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import wbh.finanzapp.R;
import wbh.finanzapp.access.ProfilesDataSource;
import wbh.finanzapp.business.ProfileBean;

public class ProfilesActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();

    private ProfilesDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        dataSource = new ProfilesDataSource(this);

        activateAddButton();
        initializeContextualActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "--> Die Datenquelle wird geöffnet.");
        dataSource.open();

        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "--> Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void showAllListEntries() {
        List<ProfileBean> profiles = dataSource.getAllProfiles();

        ArrayAdapter<ProfileBean> profileArrayAdapter = new ArrayAdapter<>(
            this, android.R.layout.simple_list_item_activated_1, profiles);

        ListView profilesListView = (ListView) findViewById(R.id.list_view_profiles);
        profilesListView.setAdapter(profileArrayAdapter);
    }

    private void activateAddButton() {
        Button buttonAddProfile = (Button) findViewById(R.id.button_add_profile);

        buttonAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog addProfileDialog = createAddProfileDialog();
                addProfileDialog.show();
            }
        });
    }

    private void initializeContextualActionBar() {
        final ListView profilesListView = (ListView) findViewById(R.id.list_view_profiles);
        profilesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        profilesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selCount = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                if(checked) selCount++;
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
                if(selCount == 1) item.setVisible(true);
                else item.setVisible(false);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                boolean returnValue = true;
                SparseBooleanArray touchedProfilePositions = profilesListView.getCheckedItemPositions();

                switch(menuItem.getItemId()) {
                    case R.id.button_delete_profile:
                        for(int i = 0; i < touchedProfilePositions.size(); i++) {
                            boolean isChecked = touchedProfilePositions.valueAt(i);
                            if(isChecked) {
                                int positionInListView = touchedProfilePositions.keyAt(i);
                                ProfileBean profile = (ProfileBean) profilesListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position im ListView: " + positionInListView + " Inhalt: " + profile.toString());
                                dataSource.deleteProfile(profile);
                            }
                        }
                        showAllListEntries();
                        actionMode.finish();
                        break;
                    case R.id.button_edit_profile:
                        for(int i = 0; i < touchedProfilePositions.size(); i++) {
                            boolean isChecked = touchedProfilePositions.valueAt(i);
                            if(isChecked) {
                                int positionInListView = touchedProfilePositions.keyAt(i);
                                ProfileBean profile = (ProfileBean) profilesListView.getItemAtPosition(positionInListView);
                                Log.d(LOG_TAG, "--> Position im ListView: " + positionInListView + " Inhalt: " + profile.toString());
                                AlertDialog editProfileDialog = createEditProfileDialog(profile);
                                editProfileDialog.show();
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

    private AlertDialog createAddProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogsView = inflater.inflate(R.layout.dialog_add_profile, null);

        final EditText editTextNewName = (EditText) dialogsView.findViewById(R.id.profile_new_name);
        editTextNewName.setText("");

        final EditText editTextNewDescription = (EditText) dialogsView.findViewById(R.id.profile_new_description);
        editTextNewDescription.setText("");

        builder.setView(dialogsView)
            .setTitle(R.string.profile_add_title)
            .setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = editTextNewName.getText().toString();
                    String description = editTextNewDescription.getText().toString();

                    if((TextUtils.isEmpty(name))) {
                        editTextNewName.setError(getString(R.string.profile_name_error));
                        return;
                    }

                    ProfileBean newProfile = dataSource.insertProfile(name, description, null);
                    Log.d(LOG_TAG, "--> Neuer Eintrag: " + newProfile.toString());

                    showAllListEntries();
                    dialog.dismiss();
                }
            })
            .setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        return builder.create();
    }

    private AlertDialog createEditProfileDialog(final ProfileBean profile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogsView = inflater.inflate(R.layout.dialog_edit_profile, null);

        final EditText editTextNewName = (EditText) dialogsView.findViewById(R.id.profile_new_name);
        editTextNewName.setText(profile.getName());

        final EditText editTextNewDescription = (EditText) dialogsView.findViewById(R.id.profile_new_description);
        editTextNewDescription.setText(profile.getDescription());

        builder.setView(dialogsView)
                .setTitle(R.string.profile_edit_title)
                .setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = editTextNewName.getText().toString();
                        String description = editTextNewDescription.getText().toString();

                        if((TextUtils.isEmpty(name))) {
                            editTextNewName.setError(getString(R.string.profile_name_error));
                            return;
                        }

                        ProfileBean updatedProfile = dataSource.updateProfile(profile.getId(), name, description, null);

                        Log.d(LOG_TAG, "--> Alter Eintrag: " + profile.toString());
                        Log.d(LOG_TAG, "--> Neuer Eintrag: " + updatedProfile.toString());

                        showAllListEntries();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
